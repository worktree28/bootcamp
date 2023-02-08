# Spring Boot and React.js Template
## install npm 

1. https://nodejs.org/en/download/
2. sudo chown -R $(whoami) ~/.npm



## Database Setup using PostgreSql

Install docker and run postgres server as container: 
docker run -d -p 5432:5432 -e POSTGRES_PASSWORD=playground -e POSTGRES_USER=postgres -e POSTGRES_DB=playground postgres:13.3

## Run the application

mvn spring-boot:run
