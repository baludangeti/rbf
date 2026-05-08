<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="../layouts/app-start.jsp" %>
<link href="${pageContext.request.contextPath}/assets/css/invoice-print.css" rel="stylesheet">

<div class="page-heading no-print">
    <div>
        <h1>GST Invoice</h1>
        <p>Load an invoice and print the GST copy in A4 format.</p>
    </div>
</div>

<section class="content-panel no-print">
    <form method="get" action="${pageContext.request.contextPath}/billing/invoice-view" class="row g-2 align-items-end">
        <div class="col-md-4">
            <label class="form-label" for="invoiceId">Invoice ID</label>
            <input class="form-control" id="invoiceId" name="invoiceId" value="<c:out value='${invoiceId}'/>" placeholder="Enter invoice ID">
        </div>
        <div class="col-md-auto">
            <button class="btn btn-primary" type="submit">Load Invoice</button>
        </div>
        <c:if test="${not empty invoicePrint}">
            <div class="col-md-auto">
                <button class="btn btn-outline-dark" type="button" onclick="window.print()">Print</button>
            </div>
            <div class="col-md-auto">
                <button class="btn btn-outline-secondary" type="button" disabled>Download PDF</button>
            </div>
        </c:if>
    </form>
</section>

<c:choose>
    <c:when test="${empty invoicePrint}">
        <section class="content-panel no-print">
            <div class="text-muted">Invoice details will appear here after loading an invoice.</div>
        </section>
    </c:when>
    <c:otherwise>
        <section class="invoice-a4">
            <div class="invoice-title-row">
                <div>
                    <h2><c:out value="${invoicePrint.organizationName}"/></h2>
                    <div class="muted-line"><c:out value="${invoicePrint.organizationAddress}"/></div>
                    <div class="muted-line">GSTIN: <strong><c:out value="${invoicePrint.organizationGstin}"/></strong></div>
                    <div class="muted-line">
                        Email: <c:out value="${invoicePrint.organizationEmail}"/> |
                        Phone: <c:out value="${invoicePrint.organizationPhone}"/>
                    </div>
                </div>
                <div class="invoice-badge">
                    <div>Tax Invoice</div>
                    <small><c:out value="${invoicePrint.invoiceStatus}"/></small>
                </div>
            </div>

            <div class="invoice-meta-grid">
                <div>
                    <h3>Customer Details</h3>
                    <p class="mb-1"><strong><c:out value="${invoicePrint.customerName}"/></strong></p>
                    <p class="mb-1">GSTIN: <c:out value="${invoicePrint.customerGstin}"/></p>
                    <p class="mb-1">Phone: <c:out value="${invoicePrint.customerPhone}"/></p>
                    <p class="mb-0"><c:out value="${invoicePrint.customerAddress}"/></p>
                </div>
                <div>
                    <h3>Invoice Details</h3>
                    <div class="meta-line"><span>Invoice No</span><strong><c:out value="${invoicePrint.invoiceNumber}"/></strong></div>
                    <div class="meta-line"><span>Invoice Date</span><strong><c:out value="${invoicePrint.invoiceDate}"/></strong></div>
                    <div class="meta-line"><span>Place of Supply</span><strong><c:out value="${invoicePrint.placeOfSupply}"/></strong></div>
                </div>
            </div>

            <table class="table invoice-table">
                <thead>
                <tr>
                    <th>#</th>
                    <th>Product</th>
                    <th>HSN/SAC</th>
                    <th class="text-end">Qty</th>
                    <th class="text-end">Rate</th>
                    <th class="text-end">Taxable</th>
                    <th class="text-end">GST</th>
                    <th class="text-end">Total</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${invoicePrint.items}" var="item" varStatus="loop">
                    <tr>
                        <td><c:out value="${loop.count}"/></td>
                        <td>
                            <strong><c:out value="${item.productName}"/></strong>
                            <div class="muted-line">Product ID: <c:out value="${item.productId}"/></div>
                        </td>
                        <td><c:out value="${item.hsnSacCode}"/></td>
                        <td class="text-end"><c:out value="${item.qty}"/></td>
                        <td class="text-end"><fmt:formatNumber value="${item.price}" minFractionDigits="2"/></td>
                        <td class="text-end"><fmt:formatNumber value="${item.lineTotal - item.tax}" minFractionDigits="2"/></td>
                        <td class="text-end"><fmt:formatNumber value="${item.tax}" minFractionDigits="2"/></td>
                        <td class="text-end"><fmt:formatNumber value="${item.lineTotal}" minFractionDigits="2"/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <div class="invoice-summary-grid">
                <div>
                    <h3>Tax Breakup</h3>
                    <table class="table table-sm invoice-mini-table">
                        <thead>
                        <tr>
                            <th>Tax</th>
                            <th class="text-end">Rate</th>
                            <th class="text-end">Taxable</th>
                            <th class="text-end">Amount</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${invoicePrint.taxBreakups}" var="tax">
                            <tr>
                                <td><c:out value="${tax.taxType}"/> - <c:out value="${tax.taxName}"/></td>
                                <td class="text-end"><fmt:formatNumber value="${tax.taxRate}" minFractionDigits="2"/>%</td>
                                <td class="text-end"><fmt:formatNumber value="${tax.taxableAmount}" minFractionDigits="2"/></td>
                                <td class="text-end"><fmt:formatNumber value="${tax.taxAmount}" minFractionDigits="2"/></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty invoicePrint.taxBreakups}">
                            <tr>
                                <td colspan="4" class="text-muted">Tax breakup not available.</td>
                            </tr>
                        </c:if>
                        </tbody>
                    </table>

                    <h3>Payment Details</h3>
                    <div class="payment-grid">
                        <div><span>Status</span><strong><c:out value="${invoicePrint.paymentDetails.status}"/></strong></div>
                        <div><span>Paid</span><strong><fmt:formatNumber value="${invoicePrint.paymentDetails.paidAmount}" minFractionDigits="2"/></strong></div>
                        <div><span>Due</span><strong><fmt:formatNumber value="${invoicePrint.paymentDetails.dueAmount}" minFractionDigits="2"/></strong></div>
                        <div><span>Mode</span><strong><c:out value="${empty invoicePrint.paymentDetails.paymentMode ? '-' : invoicePrint.paymentDetails.paymentMode}"/></strong></div>
                    </div>
                </div>

                <div class="totals-box">
                    <div><span>Taxable Amount</span><strong><fmt:formatNumber value="${invoicePrint.taxableAmount}" minFractionDigits="2"/></strong></div>
                    <div><span>CGST</span><strong><fmt:formatNumber value="${invoicePrint.cgst}" minFractionDigits="2"/></strong></div>
                    <div><span>SGST</span><strong><fmt:formatNumber value="${invoicePrint.sgst}" minFractionDigits="2"/></strong></div>
                    <div><span>IGST</span><strong><fmt:formatNumber value="${invoicePrint.igst}" minFractionDigits="2"/></strong></div>
                    <div><span>Total GST</span><strong><fmt:formatNumber value="${invoicePrint.totalGst}" minFractionDigits="2"/></strong></div>
                    <div><span>Discount</span><strong><fmt:formatNumber value="${invoicePrint.discount}" minFractionDigits="2"/></strong></div>
                    <div><span>Round Off</span><strong><fmt:formatNumber value="${invoicePrint.roundOff}" minFractionDigits="2"/></strong></div>
                    <div class="grand-total"><span>Grand Total</span><strong><fmt:formatNumber value="${invoicePrint.grandTotal}" minFractionDigits="2"/></strong></div>
                </div>
            </div>

            <div class="invoice-footer-row">
                <div class="qr-placeholder">QR Code</div>
                <div class="signature-box">
                    <div>For <c:out value="${invoicePrint.organizationName}"/></div>
                    <strong>Authorized Signatory</strong>
                </div>
            </div>
        </section>
    </c:otherwise>
</c:choose>

<%@ include file="../layouts/app-end.jsp" %>
