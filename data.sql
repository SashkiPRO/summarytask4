CREATE TABLE taskinit.employee (
  id int NOT NULL AUTO_INCREMENT,
  name varchar(50) DEFAULT NULL,
  date_birth datetime DEFAULT NULL,
  salary double DEFAULT NULL,
  email varchar(50) DEFAULT NULL,
  PRIMARY KEY (id)
)
ENGINE = INNODB;

CREATE TABLE taskinit.department (
  name varchar(50) NOT NULL,
  PRIMARY KEY (name)
)
ENGINE = INNODB
CHARACTER SET utf8
COLLATE utf8_general_ci
ROW_FORMAT = DYNAMIC;

CREATE TABLE taskinit.department_employees (
  dep_id int(11) DEFAULT NULL,
  emp_id int(11) DEFAULT NULL,
  CONSTRAINT FK_department_employees_department_id FOREIGN KEY (dep_id)
  REFERENCES taskinit.department (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT FK_department_employees_employee_id FOREIGN KEY (emp_id)
  REFERENCES taskinit.employee (id) ON DELETE RESTRICT ON UPDATE RESTRICT
)
ENGINE = INNODB
CHARACTER SET utf8
COLLATE utf8_general_ci
ROW_FORMAT = DYNAMIC;
