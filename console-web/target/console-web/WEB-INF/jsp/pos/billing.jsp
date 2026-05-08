<%@ include file="../common/layout-start.jsp" %>
<div class="page-heading compact">
    <div>
        <h1>Billing POS</h1>
        <p>Scan barcode, search SKU, update cart, and finalize invoice.</p>
    </div>
    <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/pos/held">Held Invoices</a>
</div>

<div class="pos-grid">
    <section class="content-panel">
        <div class="input-group mb-3">
            <input class="form-control form-control-lg" id="barcodeInput" placeholder="Scan barcode or enter SKU">
            <button class="btn btn-primary" id="addProductBtn" type="button">Add</button>
        </div>
        <table class="table table-sm align-middle">
            <thead>
            <tr>
                <th>Product</th>
                <th class="text-end">Qty</th>
                <th class="text-end">Price</th>
                <th class="text-end">Total</th>
            </tr>
            </thead>
            <tbody id="cartTableBody">
            <tr><td colspan="4" class="text-muted">Cart is empty.</td></tr>
            </tbody>
        </table>
    </section>

    <section class="content-panel pos-summary">
        <h2>Invoice Summary</h2>
        <div class="summary-row"><span>Subtotal</span><strong id="subtotalValue">0.00</strong></div>
        <div class="summary-row"><span>Discount %</span><input id="discountPercentage" class="form-control form-control-sm" type="number" value="0" min="0"></div>
        <div class="summary-row"><span>Tax</span><strong id="taxValue">0.00</strong></div>
        <div class="summary-row total"><span>Total</span><strong id="totalValue">0.00</strong></div>
        <label class="form-label mt-3" for="paymentMode">Payment Mode</label>
        <select class="form-select" id="paymentMode">
            <option value="CASH">Cash</option>
            <option value="UPI">UPI</option>
            <option value="CARD">Card</option>
            <option value="CREDIT">Credit</option>
        </select>
        <div class="d-grid gap-2 mt-4">
            <button class="btn btn-success" id="finalizeInvoiceBtn">Finalize Invoice</button>
            <button class="btn btn-outline-secondary" id="holdInvoiceBtn">Hold Invoice</button>
        </div>
    </section>
</div>
<%@ include file="../common/layout-end.jsp" %>
