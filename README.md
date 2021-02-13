###### Pregunton API

* [Description](#description)
* [Prerequisites](#prerequisites)
* [Installation](#installation)
* [Start Up](#start-up)
* [Endpoints](#endpoints)

## Description

___
This is a project in which some players ask questions to a master who answers them, in order to make successful attempts
and win the game.

## Prerequisites

___
I will provide a list with the depedencies and their download link:

* Java 8 ([Download link](https://www.java.com/es/download/))
* Maven ([Download link](https://maven.apache.org/))
* Git ([Download link](https://git-scm.com/downloads))
* MariaDB ([Download link](https://downloads.mariadb.org/))

## Installation

___

1. Open your command prompt and go to a root projects folder.
2. Then clone the project with ```git clone https://github.com/Pregunton-Accenture/pregunton.git``` command.
3. Execute the pregunton_db.sql in ../pregunton/src/main/resources/db/pregunton_db.sql on MariaDB.
4. Create a new file named: ```database.properties``` into ../pregunton/src/main/resources/ with the following data:
    * spring.datasource.url=jdbc:mariadb://localhost:3306/accenture_pregunton
    * spring.jpa.properties.hibernate.id.new_generator_mappings = true
    * spring.jpa.hibernate.ddl-auto=validate
    * spring.datasource.username = ```your username```
    * spring.datasource.password = ```your password``` (if you have not set any password, you can omit this line)
5. After the installation, you must jump into the project folder with your command prompt.
6. Finally, you must execute the next command: ```mvn clean install```.

## Start Up

___

1. Open your command prompt and go to the application folder.
2. Then, you have to start up the application with ```mvn spring-boot:run``` command.
3. If the process executed successfully, you could make requests to the application endpoints via http://localhost:8080.

**NOTE:** You could access to an user interface with http://localhost:8080/swagger-ui.html to do those requests and
provides to you some hints to know the data to sent.

## Endpoints

___

* http://localhost:8080
    * /games
        * __POST__ method
            * /v1.0 => creates a new game into the database
        * __PATCH__ method
            * /v1.0/{gameCode}/players => adds a new player into the game that matches with the provided ID.
        * __DELETE__ method
            * /v1.0/{gameId} => remove the game that matches with the provided ID from the database.
        * __GET__ method
            * /v1.0/game/{gameId} => retrieves the game information that matches with the provided ID from the database.
            * /v1.0/{code}/questions => get the questions of a specific game or get only the questions that are not anwered yet.
    * /players
        * __POST__ method
            * /v1.0/{nickName}/guess => grants a player to make question to the master.
        * __PATCH__ method
            * /v1.0/{nickName}/questions => adds a new question into the player, and the game that matches with the provided CODE and
              player ID.
        * __GET__ method
            * /v1.0/{nickName} => search a player to get his information that matches with the provided ID from the
              database.
    * /categories
        * __GET__ method
            * /v1.0 => retrieves all the categories' information saved on the database