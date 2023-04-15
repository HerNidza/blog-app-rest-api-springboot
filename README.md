# Blog SpringBoot Application with REST APIs and Spring Security

## Guide

- Make sure that you are using MySQL becasue of MySQL dependecy in pom.xml -> in application.properties configure username and password to your Database
- Create only 'my_blog' database in MySQL, application will do creation of tables 
- Also 2 Roles will be added 'ROLE_ADMIN, and 'ROLE_USER'
- You will need to signup to fully test app, first registering you should do directly in database and make sure that password is BCrypt - 
in docs to be more precise under package com.nikola.blog.utils.PasswordGeneratorEncoder - is public main method to run BCrypt passowrd generator or go to https://www.bcryptcalculator.com/ 
- Once added you fill the table 'users_roles' to appropriate user you just created.
- Link for swagger -> http://localhost:8080/swagger-ui/index.html
