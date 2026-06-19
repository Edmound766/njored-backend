CREATE TABLE businesses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    contact_email VARCHAR(255),
    created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE whatsapp_accounts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    business_id UUID NOT NULL REFERENCES businesses(id),

    phone_number_id VARCHAR(255) NOT NULL,
    business_account_id VARCHAR(255) NOT NULL,

    access_token TEXT NOT NULL ,

    webhook_verify_token VARCHAR(255) NOT NULL,

    created_at TIMESTAMPTZ DEFAULT NOW()
);

ALTER TABLE leads
    ADD COLUMN business_id UUID REFERENCES businesses(id);




