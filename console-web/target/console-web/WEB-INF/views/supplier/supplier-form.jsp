<div class="modal fade" id="supplierModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-scrollable">
        <div class="modal-content">
            <form id="supplierForm">
                <input type="hidden" id="supplierId" name="id">
                <div class="modal-header">
                    <h2 class="modal-title h5">Supplier</h2>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="row g-3">
                        <div class="col-md-4"><label class="form-label">Supplier Name</label><input class="form-control" id="supplierName" name="supplierName" required></div>
                        <div class="col-md-4"><label class="form-label">Contact Person</label><input class="form-control" id="supplierContactPerson" name="contactPerson"></div>
                        <div class="col-md-4"><label class="form-label">Phone</label><input class="form-control" id="supplierPhone" name="phone"></div>
                        <div class="col-md-4"><label class="form-label">Email</label><input class="form-control" id="supplierEmail" name="email" type="email"></div>
                        <div class="col-md-4"><label class="form-label">GSTIN</label><input class="form-control" id="supplierGstin" name="gstin"></div>
                        <div class="col-md-4"><label class="form-label">City</label><input class="form-control" id="supplierCity" name="city"></div>
                        <div class="col-md-4"><label class="form-label">State</label><input class="form-control" id="supplierState" name="state"></div>
                        <div class="col-md-4"><label class="form-label">Country</label><input class="form-control" id="supplierCountry" name="country"></div>
                        <div class="col-md-4"><label class="form-label">Pincode</label><input class="form-control" id="supplierPincode" name="pincode"></div>
                        <div class="col-md-12"><label class="form-label">Billing Address</label><textarea class="form-control" id="supplierBillingAddress" name="billingAddress" rows="2"></textarea></div>
                        <div class="col-md-12">
                            <div class="form-check form-switch">
                                <input class="form-check-input" type="checkbox" id="supplierActive" name="active" checked>
                                <label class="form-check-label" for="supplierActive">Active</label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-outline-secondary" type="button" data-bs-dismiss="modal">Cancel</button>
                    <c:if test="${canManageSuppliers}">
                        <button class="btn btn-primary" type="submit">Save Supplier</button>
                    </c:if>
                </div>
            </form>
        </div>
    </div>
</div>
