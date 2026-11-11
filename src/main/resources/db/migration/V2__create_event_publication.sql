CREATE TABLE event_publication
(
    id               UUID                     NOT NULL,
    listener_id      TEXT                     NOT NULL,
    event_type       TEXT                     NOT NULL,
    serialized_event TEXT                     NOT NULL,
    publication_date TIMESTAMP WITH TIME ZONE NOT NULL,
    completion_date  TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (id)
);

CREATE TABLE workspace
(
    id               BIGINT    NOT NULL,
    connection_start TIMESTAMP NOT NULL,
    name             TEXT      NOT NULL,
    status           TEXT      NOT NULL,
    PRIMARY KEY (id)
);
