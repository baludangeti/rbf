<%@ include file="../layouts/app-start.jsp" %>
<div class="page-heading compact">
    <div>
        <h1>Billing POS</h1>
        <p>Search by product name, SKU, or barcode. Use Enter to add the first result.</p>
    </div>
    <div class="d-flex gap-2">
            <button class="btn btn-outline-secondary" id="printInvoiceBtn" disabled>Print Invoice</button>
        <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/billing/invoice-view">Invoice View</a>
    </div>
</div>

<div class="pos-grid">
    <section class="content-panel">
        <div class="row g-2 align-items-end mb-3">
            <div class="col-md-8">
                <label class="form-label" for="productSearchInput">Product Search</label>
                <input class="form-control form-control-lg" id="productSearchInput" placeholder="Name, SKU, or barcode" autocomplete="off" autofocus>
            </div>
            <div class="col-md-2">
                <label class="form-label" for="quickQtyInput">Qty</label>
                <input class="form-control form-control-lg" id="quickQtyInput" type="text" inputmode="numeric" value="1">
            </div>
            <div class="col-md-2">
                <button class="btn btn-primary btn-lg w-100" id="searchProductBtn" type="button">Search</button>
            </div>
        </div>

        <div class="list-group mb-3" id="productSearchResults"></div>

        <div class="table-responsive">
            <table class="table table-sm align-middle">
                <thead>
                <tr>
                    <th>Product</th>
                    <th>SKU</th>
                    <th class="text-end">Price</th>
                    <th class="text-end">Qty</th>
                    <th class="text-end">GST</th>
                    <th class="text-end">Line Total</th>
                    <th class="text-end">Action</th>
                </tr>
                </thead>
                <tbody id="cartTableBody">
                <tr><td colspan="7" class="text-muted">Cart is empty.</td></tr>
                </tbody>
            </table>
        </div>
    </section>

    <section class="content-panel pos-summary">
        <h2>Invoice Summary</h2>
        <div class="row g-2 mb-3">
            <div class="col-6">
                <label class="form-label" for="sellerRegion">Seller State</label>
                <input class="form-control form-control-sm" id="sellerRegion" value="KA">
            </div>
            <div class="col-6">
                <label class="form-label" for="customerRegion">Customer State</label>
                <input class="form-control form-control-sm" id="customerRegion" value="KA">
            </div>
        </div>
        <div class="summary-row"><span>Subtotal</span><strong id="subtotalValue">0.00</strong></div>
        <div class="summary-row">
            <span>Discount %</span>
            <input id="discountPercentage" class="form-control form-control-sm" type="text" inputmode="decimal" value="0">
        </div>
        <div class="summary-row"><span>Discount</span><strong id="discountValue">0.00</strong></div>
        <div class="summary-row"><span>Taxable</span><strong id="taxableValue">0.00</strong></div>
        <div class="summary-row"><span>CGST</span><strong id="cgstValue">0.00</strong></div>
        <div class="summary-row"><span>SGST</span><strong id="sgstValue">0.00</strong></div>
        <div class="summary-row"><span>IGST</span><strong id="igstValue">0.00</strong></div>
        <div class="summary-row"><span>Total Tax</span><strong id="taxValue">0.00</strong></div>
        <div class="summary-row"><span>Round Off</span><strong id="roundOffValue">0.00</strong></div>
        <div class="summary-row total"><span>Total</span><strong id="totalValue">0.00</strong></div>

        <label class="form-label mt-3" for="paymentMode">Payment Mode</label>
        <select class="form-select" id="paymentMode">
            <option value="CASH">Cash</option>
            <option value="UPI">UPI</option>
            <option value="CARD">Card</option>
            <option value="CREDIT">Credit</option>
        </select>

        <div class="d-grid gap-2 mt-4">
            <c:if test="${canCreateBilling}">
                <button class="btn btn-success" id="generateInvoiceBtn">Generate Invoice</button>
            </c:if>
            <button class="btn btn-outline-secondary" id="clearCartBtn">Clear Cart</button>
        </div>
        <div class="alert alert-info mt-3 d-none" id="invoiceResult"></div>
    </section>
</div>
<%@ include file="../layouts/app-end.jsp" %>
