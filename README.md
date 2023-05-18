# Map-Postcode-Tool

## Description

This is a repository for a tool which handles postcodes with the corresponding latitude and longitube. Users will need to register themselves in order to use this application. The documentation of this application is found in the /doc folder (https://github.com/SehDev96/tool-backend/tree/main/doc).


---

## Tech Stack 

Backend: `Spring Boot` <br>
Database: `Postgresql`

---

## Prerequisite 

1. Docker should be installed (non-sudo access) 

--- 


## Steps to run application 

Make sure docker daemon is running before running docker compose command.

### Run application using docker-compose: 
1. $ git clone https://github.com/SehDev96/tool-backend.git
2. $ cd tool-backend
3. $ docker-compose up -d 
4. The application should be up and running at http://localhost:8080

<br>

### Run application for dev purpose
1. $ git clone https://github.com/SehDev96/tool-backend.git
2. $ cd tool-backend
3. docker-compose -f docker-composed-dev.yml up -d
4. This will spin up a postgres container. 
5. The backend source can be opened in preferred IDE to run the code.
