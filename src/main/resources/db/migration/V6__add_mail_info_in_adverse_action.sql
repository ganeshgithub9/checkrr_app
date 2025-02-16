ALTER TABLE adverse_action ADD COLUMN mail_subject VARCHAR(255) NOT NULL, ADD COLUMN mail_content_in_html mediumtext NOT NULL, ADD COLUMN created_by_user_id bigint NOT NULL;
ALTER TABLE adverse_action ADD CONSTRAINT FK__adverse_action__user__id FOREIGN KEY (created_by_user_id) REFERENCES user (id);

CREATE TABLE attachment (
    adverse_action_id BIGINT NOT NULL,
    attachment_url VARCHAR(255) NOT NULL
);


ALTER TABLE attachment ADD CONSTRAINT FK__attachment__adverse_action__id FOREIGN KEY (adverse_action_id) REFERENCES adverse_action(id);
ALTER TABLE attachment ADD CONSTRAINT PK__attachment PRIMARY KEY(adverse_action_id,attachment_url);

