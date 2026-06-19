ALTER TABLE agents
    ADD COLUMN  business_id UUID REFERENCES businesses(id);

CREATE INDEX idx_leads_business_external ON leads(business_id, external_id);
CREATE INDEX idx_agents_business_active ON agents(business_id, is_active);

ALTER TABLE leads DROP CONSTRAINT leads_external_id_key;

ALTER TABLE leads ADD CONSTRAINT leads_business_external_unique UNIQUE (business_id,external_id);