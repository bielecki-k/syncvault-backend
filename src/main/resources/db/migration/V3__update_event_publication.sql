ALTER TABLE event_publication
    ADD COLUMN completion_attempts INTEGER;

ALTER TABLE event_publication
    ADD COLUMN status TEXT;

ALTER TABLE event_publication
    ADD COLUMN last_resubmission_date TIMESTAMP WITH TIME ZONE;