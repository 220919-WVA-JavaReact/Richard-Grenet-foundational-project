CREATE TABLE managers (
	manager_id serial PRIMARY KEY,
	"first" varchar(25),
	"last" varchar(25),
	email varchar(50),
	department varchar(25)
);

CREATE TABLE employees(
	employee_id serial PRIMARY KEY,
	"first" varchar(25),
	"last" varchar(25),
	email varchar(50),
	department varchar(25),
	manager int REFERENCES managers
);

CREATE TABLE tickets(
	ticket_id serial PRIMARY KEY,
	employee_id int REFERENCES employees,
	amount float,
	description varchar(255),
	status int DEFAULT 0,
	resolvedBy int DEFAULT 0,
	reason varchar(255) DEFAULT ''
);

CREATE TABLE accounts(
	account_id serial PRIMARY KEY,
	username varchar(25) UNIQUE,
	"password" varchar(50),
	manager boolean DEFAULT false,
	related_id int --REFERENCES either manager_id OR employee_id depending ON manager boolean
);