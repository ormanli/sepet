CREATE TABLE readings
(
    id          VARCHAR(26)      NOT NULL,
    time        TIMESTAMP        NOT NULL,
    machine_id  TEXT             NOT NULL,
    temperature DOUBLE PRECISION NOT NULL
);

SELECT create_hypertable('readings', 'time');

create
    index if not exists readings_machine_id_index
    on readings (machine_id);