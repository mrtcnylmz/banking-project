# Banking Project

This Banking project is made with Spring Boot Framework using MySQL and more.

## Technologies

- Java
- Spring Boot
- MyBatis
- Kafka
- Spring Security
- MySQL
- Log4J
- Collect API

## SERVICES
- 1.Create Bank ------------- >	POST	/banks
- 2.User Register ----------- >	POST	/register
- 3.User Login -------------- >	POST	/auth
- 4.User Enable/Disable ----- >	POST	/users/{id}
- 5.Account Create ---------- >	POST	/accounts
- 6.Account Detail ---------- >	GET		/accounts/{id}
- 7.Account Remove ---------- >	DELETE	/accounts/{id}
- 8.Account Deposit --------- >	PATCH	/accounts/{id}/deposit
- 9.Account Balance Transfer 	>	PATCH	/accounts/{id}/transfer

## ENTITIES
- Users
```sql
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(255) NOT NULL,
  `enabled` tinyint NOT NULL DEFAULT '0',
  `authorities` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email_UNIQUE` (`email`)
)
```

- Banks
```sql
CREATE TABLE `banks` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
)
```
- Accounts
```sql
CREATE TABLE `accounts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `bank_id` int NOT NULL,
  `number` bigint NOT NULL,
  `type` varchar(45) NOT NULL,
  `balance` double NOT NULL DEFAULT '0',
  `creation_date` datetime NOT NULL,
  `last_update_date` datetime NOT NULL,
  `is_deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_UNIQUE` (`number`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `FK_user_id` (`user_id`),
  KEY `FK_bank_id` (`bank_id`),
  CONSTRAINT `FK_bank_id` FOREIGN KEY (`bank_id`) REFERENCES `banks` (`id`),
  CONSTRAINT `FK_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
)
```
## SECURITY
Securities all handled with JWT.
