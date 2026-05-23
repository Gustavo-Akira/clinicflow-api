CREATE SCHEMA IF NOT EXISTS clinicflow;

CREATE TABLE IF NOT EXISTS clinicflow.schema_version_probe (
    version VARCHAR(32) PRIMARY KEY,
    executed_at TIMESTAMP WITH TIME ZONE NOT NULL
);

INSERT INTO clinicflow.schema_version_probe (version, executed_at)
VALUES ('V1', CURRENT_TIMESTAMP)
ON CONFLICT (version) DO NOTHING;
