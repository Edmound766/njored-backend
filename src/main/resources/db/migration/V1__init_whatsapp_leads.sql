-- Extensions
CREATE
EXTENSION IF NOT EXISTS pgcrypto;

-- ENUMS

CREATE TYPE lead_status AS ENUM (
    'BOT_QUALIFYING',
    'BOOKING',
    'HUMAN_HANDOVER',
    'COLD_FOLLOWUP'
);

CREATE TYPE channel_type AS ENUM (
    'WHATSAPP'
);

CREATE TYPE message_direction AS ENUM (
    'INBOUND',
    'OUTBOUND_BOT',
    'OUTBOUND_HUMAN'
);

-- AGENTS

CREATE TABLE agents
(
    id               UUID PRIMARY KEY            DEFAULT gen_random_uuid(),

    name             VARCHAR(255)       NOT NULL,

    phone_number     VARCHAR(50) UNIQUE NOT NULL,

    telegram_chat_id VARCHAR(100),

    is_active        BOOLEAN            NOT NULL DEFAULT TRUE,

    created_at       TIMESTAMPTZ        NOT NULL DEFAULT NOW()
);

-- LEADS

CREATE TABLE leads
(
    id                  UUID PRIMARY KEY      DEFAULT gen_random_uuid(),

    external_id         VARCHAR(100) NOT NULL UNIQUE,

    channel             channel_type NOT NULL DEFAULT 'WHATSAPP',

    name                VARCHAR(255),

    status              lead_status  NOT NULL DEFAULT 'BOT_QUALIFYING',

    assigned_agent_id   UUID         REFERENCES agents (id)
                                         ON DELETE SET NULL,

    metadata            JSONB        NOT NULL DEFAULT '{}'::jsonb,

    last_interaction_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),

    created_at          TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_leads_external_id
    ON leads (external_id);

CREATE INDEX idx_leads_status
    ON leads (status);

CREATE INDEX idx_leads_last_interaction
    ON leads (last_interaction_at);

-- CONVERSATIONS

CREATE TABLE conversations
(
    id                  UUID PRIMARY KEY           DEFAULT gen_random_uuid(),

    lead_id             UUID              NOT NULL
        REFERENCES leads (id)
            ON DELETE CASCADE,

    message_external_id VARCHAR(255),

    direction           message_direction NOT NULL,

    message_text        TEXT              NOT NULL,

    raw_payload         JSONB             NOT NULL DEFAULT '{}'::jsonb,

    created_at          TIMESTAMPTZ       NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX idx_conversation_message_id
    ON conversations (message_external_id) WHERE message_external_id IS NOT NULL;

CREATE INDEX idx_conversations_lead_id
    ON conversations (lead_id);

-- STATUS HISTORY

CREATE TABLE lead_status_history
(
    id         UUID PRIMARY KEY     DEFAULT gen_random_uuid(),

    lead_id    UUID        NOT NULL
        REFERENCES leads (id)
            ON DELETE CASCADE,

    old_status lead_status,

    new_status lead_status NOT NULL,

    reason     TEXT,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_status_history_lead
    ON lead_status_history (lead_id);

-- WEBHOOK IDEMPOTENCY

CREATE TABLE processed_webhooks
(
    webhook_id   VARCHAR(255) PRIMARY KEY,

    processed_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);