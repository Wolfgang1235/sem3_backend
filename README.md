## Instruktioner, opsummeret:

* Klon denne startskode
* Fjern .git mappen (rm -rf .git eller stifinder)
* Opdatér pom.xml 4 steder (artifactId, name, remote.server, db.name)
* Tryk "Load Maven Changes" mens man stadig står i pom.xml
* Ændr 2 steder i persistence.xml (databasenavnene på linje 25 og 43)
* Ændr databasenavn i mavenworkflow.yml (linje 41)
* I IDE'et skal File -> Project Structure -> SDK ændres til version 11
* Opret tomme databaser både almindelig og test (lokalt og på droplet)
* Lav nyt repo på Github
* Indsæt REMOTE_USER og REMOTE_PW på under det nye repo på Github gennem Settings -> Secrets -> Actions (Det er logininformationer til Tomcat)
* I terminal under projektmappen: git init, git add ., git commit -m "first commit", git branch -M main, git remote add origin git@github.com:USERNAME/REPONAME.git, git push -u origin main
* Se at den deployes under Github projektet -> actions
* Når deployet, kan man med Postman kalde endpointet https://REMOTE_SERVER/tomcat/PROJEKTNAVN/api/utilities/populate med POST og JSON body {"secret":"exam3sem"} for at populate databasen med rollerne user og admin samt brugerne user -> test123 og admin -> test123 
* Opsætning af local tomcat server: project -> + -> vælg tomcat server local -> tryk fix -> vælg war.....exploded -> ændr URL til / -> ok
* Start tomcat server
* Populate lokal DB med Postman: localhost:8080/api/utilities/populate
*
* For at tilføje tabeller til projektet
* Lav reverse engineer -> indsæt ny tabel -> udfyld kolonner -> forward engineer
* Til tabellen til projekt: Tryk på JPA Buddy -> DB connection -> new DB connection -> + -> mysql -> user:dev, pw:ax2, databasenavn skrives -> evt test connection -> apply
* JPA Buddy -> persistence -> new -> JPA Entities from DB -> vælg tabel (rigitge package) -> ok
* DETTE GÆLDER IKKE USER, ROLE OG USER_ROLE, DISSE SKAL ALDRIG SLETTES! Hvis man senere tilføjer eller ændrer tabeller, er det nemmest at slette tabellerne i DB og entiteterne i projektet. 
* Ved mange til en relation brug stiplet forbindelse.



## Startcode

### Preconditions
*In order to use this code, you should have a local developer setup + a "matching" droplet on Digital Ocean

### Getting Started

This document explains how to use this code (build, test and deploy), locally with maven, and remotely with maven controlled by Github actions
- [How to use](https://docs.google.com/document/d/1rymrRWF3VVR7ujo3k3sSGD_27q73meGeiMYtmUtYt6c/edit?usp=sharing)
- 

## Update pom.xml
- Change artifactId, name, database name and remote-server to your desired values. The database name should exist on your droplet.

## Update mavenworkflow.yml
- Update database name and mysql credentials

Build the project.

## Running Mysql database locally
- If using a local setup with the database running in a Docker container, make sure that the container is running before connecting to the database
- Create a schema/database in Mysql and open it up for the desired Mysql user. Make sure to change the schema name, Mysql username and Mysql password in the persistence.xml file so that it matches your created schema.
- Repeat the step above for the test database as well.

### Populate tables with test users and roles
- run the method in SetupTestUsers to create tables and populate them in the database

### Add/change tables In MySQL Workbench
- In the menu, choose Database -> Reverse Engineer
- Press Continue twice
- Select schema
- Continue, execute and close
- Make the desired changes in the EER diagram
- In the menu, choose Database -> Forward Engineer
- Continue x2. Copy SQL script to be executed.
- Delete your old tables and data
- Run the script

### Create entities from database in IntelliJ (Persistence mappings)
- From inside the Persistence window:
- Right-click a persistence unit, point to Generate Persistence Mapping and select By Database Schema.
- Select the
    - data source
    - package
    - tick tables to include
    - open tables to see columns and add the ones with mapped type: Collection<SomeEntity> and SomeEntity
    - click OK.
