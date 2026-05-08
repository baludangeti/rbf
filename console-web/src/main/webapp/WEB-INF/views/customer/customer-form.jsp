<div class="modal fade" id="customerModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-scrollable">
        <div class="modal-content">
            <form id="customerForm">
                <input type="hidden" id="customerId" name="id">
                <div class="modal-header">
                    <h2 class="modal-title h5">Customer</h2>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="row g-3">
                        <div class="col-md-4"><label class="form-label">Name</label><input class="form-control" id="customerName" name="customerName" required></div>
                        <div class="col-md-4"><label class="form-label">Phone</label><input class="form-control" id="customerPhone" name="phone"></div>
                        <div class="col-md-4"><label class="form-label">Email</label><input class="form-control" id="customerEmail" name="email" type="email"></div>
                        <div class="col-md-4"><label class="form-label">GSTIN</label><input class="form-control" id="customerGstin" name="gstin"></div>
                        <div class="col-md-4"><label class="form-label">City</label><input class="form-control" id="customerCity" name="city"></div>
                        <div class="col-md-4"><label class="form-label">State</label><input class="form-control" id="customerState" name="state"></div>
                        <div class="col-md-4"><label class="form-label">Country</label><input class="form-control" id="customerCountry" name="country"></div>
                        <div class="col-md-4"><label class="form-label">Pincode</label><input class="form-control" id="customerPincode" name="pincode"></div>
                        <div class="col-md-12"><label class="form-label">Billing Address</label><textarea class="form-control" id="billingAddress" name="billingAddress" rows="2"></textarea></div>
                        <div class="col-md-12"><label class="form-label">Shipping Address</label><textarea class="form-control" id="shippingAddress" name="shippingAddress" rows="2"></textarea></div>
                        <div class="col-md-12">
                            <div class="form-check form-switch">
                                <input class="form-check-input" type="checkbox" id="customerActive" name="active" checked>
                                <label class="form-check-label" for="customerActive">Active</label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-outline-secondary" type="button" data-bs-dismiss="modal">Cancel</button>
                    <c:if test="${canManageCustomers}">
                        <button class="btn btn-primary" type="submit">Save Customer</button>
                    </c:if>
                </div>
            </form>
        </div>
    </div>
</div>
