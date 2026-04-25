ALTER TABLE projects DROP COLUMN repository_url;

ALTER TABLE projects RENAME COLUMN description TO markdown;