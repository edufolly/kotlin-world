-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
INSERT INTO my_kotlin_entity (name, description, created_at, updated_at, deleted_at)
  VALUES ('Name 1', 'Description 1', NOW(), NOW(), '1970-01-01 00:00:00+00');
