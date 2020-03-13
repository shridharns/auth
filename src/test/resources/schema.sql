DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS client;
DROP TABLE IF EXISTS privilege;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS certificates;

CREATE TABLE user (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
  email VARCHAR(250) unique NOT NULL,
  password VARCHAR(250) NOT NULL
);

CREATE TABLE client (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  clientKey VARCHAR(250) NOT NULL,
  secret VARCHAR(250) NOT NULL,
  contact_email VARCHAR(250) DEFAULT NULL,
  valid_until DATETIME DEFAULT NULL
);

CREATE TABLE privilege (
  id bigint NOT NULL,
  name varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE roles (
  id bigint NOT NULL,
  name varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE certificates (
  id varchar(255) NOT NULL,
  private_key varchar(10000) DEFAULT NULL,
  public_key varchar(10000) DEFAULT NULL,
  valid_until datetime DEFAULT NULL,
  PRIMARY KEY (id)
);

/* Populate the data */
INSERT INTO user (id, first_name, last_name, email, password) VALUES
  (1, 'One', 'User', 'one@test.com', '$2a$10$sJ33Q9MhOoYb7RMKv8wVt.V8bVOvXgDte9iIhzF2C3qAIBbqfQuGm'),
  (2, 'Two', 'User', 'two@test.com', '$2a$10$sJ33Q9MhOoYb7RMKv8wVt.V8bVOvXgDte9iIhzF2C3qAIBbqfQuGm'),
  (3, 'Three', 'User', 'three@test.com', '$2a$10$sJ33Q9MhOoYb7RMKv8wVt.V8bVOvXgDte9iIhzF2C3qAIBbqfQuGm');

INSERT INTO client (id, clientKey, secret, contact_email, valid_until) VALUES
  (1, 'c1234', 'secret', 'someone@test.com', '2020-09-17 18:47:52.69'),
  (2, 'c2345', 'secret', 'someonelse@test.com', '2020-09-17 18:47:52.69');