CREATE TABLE readings
(
    time        TIMESTAMP        NOT NULL,
    machine_id  TEXT             NOT NULL,
    temperature DOUBLE PRECISION NOT NULL,
    PRIMARY KEY (time, machine_id)
);

SELECT create_hypertable('readings', 'time');

create index if not exists readings_machine_id_index
    on readings (machine_id);