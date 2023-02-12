# Spring Boot and React.js Template

## git instruction 

    git pull
    git switch branch3
    git checkout -b ＜new-branch＞

### remember to git pull before checking out new branch

## install Java 19 

use intellij to install openjdk 19 
`export JAVA_HOME=~/Library/Java/JavaVirtualMachines/openjdk-19.0.2/Contents/Home`
in .zshrc
steps `cd ~` -> `open .zshrc` -> copy paste -> cmd+s

## install npm 

1. https://nodejs.org/en/download/
2. sudo chown -R $(whoami) ~/.npm

for frontend you can `cd client` -> `npm i` -> `npm start`


## Database Setup using PostgreSql

Install docker and run postgres server as container: 
`docker run -d -p 5432:5432 -e POSTGRES_PASSWORD=playground -e POSTGRES_USER=postgres -e POSTGRES_DB=playground postgres:13.3`

## Run the application

mvn spring-boot:run
