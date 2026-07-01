-- USERS TABLE

CREATE TABLE users(

                      id SERIAL PRIMARY KEY,

                      username VARCHAR(50) UNIQUE NOT NULL,

                      password VARCHAR(64) NOT NULL,

                      email VARCHAR(100) UNIQUE NOT NULL,

                      role VARCHAR(20)
                          CHECK(role IN ('ADMIN','USER')),

                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP

);

-------------------------------------------------------

-- EMPLOYEES TABLE

CREATE TABLE employees(

                          id SERIAL PRIMARY KEY,

                          name VARCHAR(100) NOT NULL,

                          profile_image VARCHAR(100) NOT NULL,

                          gender VARCHAR(10)
                              CHECK(gender IN ('Male','Female')),

                          salary NUMERIC(10,2)
                              CHECK(salary>=0),

                          start_date DATE NOT NULL,

                          notes TEXT,

                          created_by INTEGER,

                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT fk_user
                              FOREIGN KEY(created_by)
                                  REFERENCES users(id)
                                  ON DELETE SET NULL

);

-------------------------------------------------------

-- DEPARTMENTS

CREATE TABLE employee_departments(

                                     employee_id INTEGER,

                                     department VARCHAR(50),

                                     PRIMARY KEY(employee_id,department),

                                     CONSTRAINT fk_employee
                                         FOREIGN KEY(employee_id)
                                             REFERENCES employees(id)
                                             ON DELETE CASCADE

);

-------------------------------------------------------

-- PAYROLL AUDIT

CREATE TABLE payroll_audit(

                              id SERIAL PRIMARY KEY,

                              employee_id INTEGER NOT NULL,

                              action_type VARCHAR(10) NOT NULL,

                              old_salary NUMERIC(10,2),

                              new_salary NUMERIC(10,2),

                              changed_by VARCHAR(50),

                              changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP

);

-------------------------------------------------------

CREATE UNIQUE INDEX idx_users_username
    ON users(username);

CREATE INDEX idx_emp_dept_id
    ON employee_departments(employee_id);

-------------------------------------------------------

INSERT INTO users(username,password,email,role)
VALUES
    ('admin',
     'admin',
     'admin@gmail.com',
     'ADMIN');

INSERT INTO users(username,password,email,role)
VALUES
    ('user',
     'user',
     'user@gmail.com',
     'USER');