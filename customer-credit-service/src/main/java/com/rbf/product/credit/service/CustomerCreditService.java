package com.rbf.product.credit.service;

import com.rbf.product.common.tenant.OrgContext;
import com.rbf.product.credit.dto.CreditAccountRequest;
import com.rbf.product.credit.dto.CreditAccountResponse;
import com.rbf.product.credit.dto.CreditSaleRequest;
import com.rbf.product.credit.dto.CreditSettlementRequest;
import com.rbf.product.credit.dto.CreditTransactionResponse;
import com.rbf.product.credit.dto.CreditValidationRequest;
import com.rbf.product.credit.dto.CreditValidationResponse;
import com.rbf.product.credit.model.CreditTransactionType;
import com.rbf.product.credit.model.CustomerCreditAccount;
import com.rbf.product.credit.model.CustomerCreditTransaction;
import com.rbf.product.credit.repository.CustomerCreditAccountRepository;
import com.rbf.product.credit.repository.CustomerCreditTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class CustomerCreditService {

    private static final Logger log = LoggerFactory.getLogger(CustomerCreditService.class);

    private final CustomerCreditAccountRepository accountRepository;
    private final CustomerCreditTransactionRepository transactionRepository;

    public CustomerCreditService(CustomerCreditAccountRepository accountRepository,
                                 CustomerCreditTransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<CreditAccountResponse> listAccounts() {
        return accountRepository.findByOrgIdOrderByCustomerNameAsc(OrgContext.requireOrgId()).stream()
                .map(this::toAccountResponse)
                .toList();
    }

    public CreditAccountResponse getByCustomer(Long customerId) {
        return toAccountResponse(getAccount(customerId));
    }

    @Transactional
    public CreditAccountResponse createOrUpdateAccount(CreditAccountRequest request) {
        Long orgId = OrgContext.requireOrgId();
        CustomerCreditAccount account = accountRepository.findByCustomerIdAndOrgId(request.getCustomerId(), orgId)
                .orElseGet(() -> newAccount(request.getCustomerId(), orgId));
        account.setCustomerName(request.getCustomerName());
        account.setCreditLimit(request.getCreditLimit());
        account.setActive(true);
        CustomerCreditAccount saved = accountRepository.save(account);
        log.info("Customer credit account saved orgId={} customerId={} creditLimit={} dueAmount={}",
                orgId, saved.getCustomerId(), saved.getCreditLimit(), saved.getDueAmount());
        return toAccountResponse(saved);
    }

    public CreditValidationResponse validateLimit(CreditValidationRequest request) {
        CustomerCreditAccount account = getAccount(request.getCustomerId());
        BigDecimal available = availableCredit(account);
        boolean allowed = account.isActive() && available.compareTo(request.getAmount()) >= 0;
        return new CreditValidationResponse(
                account.getCustomerId(),
                account.getCreditLimit(),
                account.getDueAmount(),
                available,
                allowed
        );
    }

    @Transactional
    public CreditAccountResponse recordCreditSale(CreditSaleRequest request) {
        CustomerCreditAccount account = getAccount(request.getCustomerId());
        CreditValidationResponse validation = validateLimit(toValidationRequest(request));
        if (!validation.isAllowed()) {
            throw new IllegalArgumentException("Customer credit limit exceeded");
        }
        account.setDueAmount(account.getDueAmount().add(request.getAmount()));
        CustomerCreditAccount saved = accountRepository.save(account);
        transactionRepository.save(newTransaction(saved, request.getInvoiceId(), CreditTransactionType.CREDIT_SALE,
                request.getAmount(), "Credit sale invoice " + request.getInvoiceId()));
        log.info("Credit sale recorded orgId={} customerId={} invoiceId={} amount={} dueAmount={}",
                saved.getOrgId(), saved.getCustomerId(), request.getInvoiceId(), request.getAmount(), saved.getDueAmount());
        return toAccountResponse(saved);
    }

    @Transactional
    public CreditAccountResponse settlePayment(Long customerId, CreditSettlementRequest request) {
        CustomerCreditAccount account = getAccount(customerId);
        if (request.getAmount().compareTo(account.getDueAmount()) > 0) {
            throw new IllegalArgumentException("Settlement cannot exceed due amount");
        }
        account.setDueAmount(account.getDueAmount().subtract(request.getAmount()));
        CustomerCreditAccount saved = accountRepository.save(account);
        transactionRepository.save(newTransaction(saved, null, CreditTransactionType.SETTLEMENT,
                request.getAmount(), request.getReference()));
        log.info("Credit settlement recorded orgId={} customerId={} amount={} dueAmount={}",
                saved.getOrgId(), saved.getCustomerId(), request.getAmount(), saved.getDueAmount());
        return toAccountResponse(saved);
    }

    public List<CreditTransactionResponse> listTransactions(Long customerId) {
        return transactionRepository.findByCustomerIdAndOrgIdOrderByCreatedAtAsc(customerId, OrgContext.requireOrgId()).stream()
                .map(this::toTransactionResponse)
                .toList();
    }

    private CustomerCreditAccount newAccount(Long customerId, Long orgId) {
        CustomerCreditAccount account = new CustomerCreditAccount();
        account.setOrgId(orgId);
        account.setCustomerId(customerId);
        account.setDueAmount(BigDecimal.ZERO);
        account.setActive(true);
        return account;
    }

    private CustomerCreditAccount getAccount(Long customerId) {
        return accountRepository.findByCustomerIdAndOrgId(customerId, OrgContext.requireOrgId())
                .orElseThrow(() -> new IllegalArgumentException("Customer credit account not found"));
    }

    private BigDecimal availableCredit(CustomerCreditAccount account) {
        return account.getCreditLimit().subtract(account.getDueAmount());
    }

    private CreditValidationRequest toValidationRequest(CreditSaleRequest request) {
        CreditValidationRequest validationRequest = new CreditValidationRequest();
        validationRequest.setCustomerId(request.getCustomerId());
        validationRequest.setAmount(request.getAmount());
        return validationRequest;
    }

    private CustomerCreditTransaction newTransaction(CustomerCreditAccount account, Long invoiceId,
                                                     CreditTransactionType type, BigDecimal amount, String reference) {
        CustomerCreditTransaction transaction = new CustomerCreditTransaction();
        transaction.setOrgId(account.getOrgId());
        transaction.setCustomerId(account.getCustomerId());
        transaction.setInvoiceId(invoiceId);
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setReference(reference);
        return transaction;
    }

    private CreditAccountResponse toAccountResponse(CustomerCreditAccount account) {
        return new CreditAccountResponse(
                account.getId(),
                account.getOrgId(),
                account.getCustomerId(),
                account.getCustomerName(),
                account.getCreditLimit(),
                account.getDueAmount(),
                availableCredit(account),
                account.isActive()
        );
    }

    private CreditTransactionResponse toTransactionResponse(CustomerCreditTransaction transaction) {
        return new CreditTransactionResponse(
                transaction.getId(),
                transaction.getCustomerId(),
                transaction.getInvoiceId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getTransactionDate(),
                transaction.getReference()
        );
    }
}
