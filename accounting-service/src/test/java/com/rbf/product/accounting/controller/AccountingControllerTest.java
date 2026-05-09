package com.rbf.product.accounting.controller;

import com.rbf.product.accounting.service.AccountingService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccountingControllerTest {

    @Test
    void listsLedgerEntriesForCurrentOrg() throws Exception {
        AccountingService accountingService = mock(AccountingService.class);
        when(accountingService.ledgerEntries()).thenReturn(List.of());
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new AccountingController(accountingService)).build();

        mockMvc.perform(get("/api/accounting/ledger-entries"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
