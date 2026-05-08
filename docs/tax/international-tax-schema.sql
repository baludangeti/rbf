CREATE TABLE tax_countries (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  country_code VARCHAR(10) NOT NULL UNIQUE,
  country_name VARCHAR(100) NOT NULL,
  currency_code VARCHAR(10) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  created_by VARCHAR(100),
  updated_by VARCHAR(100)
);

CREATE TABLE tax_regions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  country_code VARCHAR(10) NOT NULL,
  region_code VARCHAR(30) NOT NULL,
  region_name VARCHAR(100) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  created_by VARCHAR(100),
  updated_by VARCHAR(100)
);

CREATE TABLE tax_regimes (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  country_code VARCHAR(10) NOT NULL,
  regime_name VARCHAR(100) NOT NULL,
  regime_type VARCHAR(30) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  created_by VARCHAR(100),
  updated_by VARCHAR(100)
);

CREATE TABLE tax_slabs (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  org_id BIGINT NOT NULL,
  country_code VARCHAR(10) NOT NULL,
  region_code VARCHAR(30),
  tax_regime_id BIGINT NOT NULL,
  tax_name VARCHAR(100) NOT NULL,
  tax_type VARCHAR(30) NOT NULL,
  tax_rate DECIMAL(8,4) NOT NULL,
  hsn_sac_code VARCHAR(50),
  product_category_id BIGINT,
  effective_from DATE NOT NULL,
  effective_to DATE,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  created_by VARCHAR(100),
  updated_by VARCHAR(100)
);

CREATE TABLE tax_rules (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  org_id BIGINT NOT NULL,
  source_country VARCHAR(10) NOT NULL,
  source_region VARCHAR(30),
  destination_country VARCHAR(10) NOT NULL,
  destination_region VARCHAR(30),
  tax_regime_id BIGINT NOT NULL,
  tax_type VARCHAR(30) NOT NULL,
  transaction_type VARCHAR(30) NOT NULL,
  customer_type VARCHAR(30) NOT NULL,
  product_category_id BIGINT,
  hsn_sac_code VARCHAR(50),
  priority INT NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  created_by VARCHAR(100),
  updated_by VARCHAR(100)
);

CREATE TABLE invoice_tax_breakup (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  org_id BIGINT NOT NULL,
  invoice_id BIGINT NOT NULL,
  invoice_item_id BIGINT NOT NULL,
  tax_name VARCHAR(100) NOT NULL,
  tax_type VARCHAR(30) NOT NULL,
  tax_rate DECIMAL(8,4) NOT NULL,
  taxable_amount DECIMAL(12,2) NOT NULL,
  tax_amount DECIMAL(12,2) NOT NULL,
  country_code VARCHAR(10) NOT NULL,
  region_code VARCHAR(30),
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  created_by VARCHAR(100),
  updated_by VARCHAR(100)
);

CREATE TABLE customer_tax_profiles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  org_id BIGINT NOT NULL,
  customer_id BIGINT NOT NULL,
  country_code VARCHAR(10) NOT NULL,
  region_code VARCHAR(30),
  tax_registration_number VARCHAR(80),
  customer_type VARCHAR(30) NOT NULL,
  tax_exempt BOOLEAN NOT NULL DEFAULT FALSE,
  reverse_charge_applicable BOOLEAN NOT NULL DEFAULT FALSE,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  created_by VARCHAR(100),
  updated_by VARCHAR(100)
);

CREATE INDEX idx_tax_slabs_org_lookup ON tax_slabs(org_id, country_code, region_code, tax_regime_id, tax_type, active);
CREATE INDEX idx_tax_rules_org_priority ON tax_rules(org_id, priority, active);
CREATE INDEX idx_invoice_tax_breakup_invoice ON invoice_tax_breakup(org_id, invoice_id);
