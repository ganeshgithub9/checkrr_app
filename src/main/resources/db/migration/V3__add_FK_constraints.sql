-- Add Foreign Key Constraints
ALTER TABLE adverse_action
    ADD CONSTRAINT FK__adverse_action__candidate__id
    FOREIGN KEY (candidate_id) REFERENCES candidate (id);

ALTER TABLE candidate
    ADD CONSTRAINT FK__candidate__report__id
    FOREIGN KEY (report_id) REFERENCES report (id);

ALTER TABLE court_search
    ADD CONSTRAINT FK__court_search__candidate__id
    FOREIGN KEY (candidate_id) REFERENCES candidate (id);