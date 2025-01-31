-- Add Unique Constraints to candidate table
ALTER TABLE candidate ADD CONSTRAINT UK_candidate_report_id UNIQUE (report_id);
ALTER TABLE candidate ADD CONSTRAINT UK_candidate_drivers_license_number UNIQUE (drivers_license_number);
ALTER TABLE candidate ADD CONSTRAINT UK_candidate_email UNIQUE (email);
ALTER TABLE candidate ADD CONSTRAINT UK_candidate_phone UNIQUE (phone);
ALTER TABLE candidate ADD CONSTRAINT UK_candidate_social_security_number UNIQUE (social_security_number);

-- Add Unique Constraint to user table
ALTER TABLE user ADD CONSTRAINT UK_user_email UNIQUE (email);