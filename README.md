# README #

	This project is intended to be used as user Manual.

### What is this repository for? ###

* Quick summary

	In this document we describe the procedure for testing the automatic creation of tasks of the isy-task module in a Spring application.
* Version
* [Learn Markdown](https://bitbucket.org/tutorials/markdowndemo)

### How do I get set up? ###
* Dependencies

	1. On Windows you need to have Docker Desktop installed (https://docs.docker.com/desktop/install/windows-install/)
	2. you need to create a local docker-compose.yml file and to past the following content into it:
	    ```
	    version: "3.9"
        services:
          test_isy_task:
            image: artifactory.adesso-group.com/bva_ea52-docker/isy_auto_task_test:latest
            ports:
              - "8181:8080"
            container_name: isy_task_container
            volumes:
              - C:/tasks/output/auto/archive:/app/output/archive
              - C:/tasks/output/auto/backup:/app/output/backup
        ```
* Source code installation (development)

  1. Clone the project from adesso BitBucket: https://bitbucket.adesso-group.com/scm/if-qs/test_isy_task.git
  2. import the project into your IDE.
  3. In the project's parent directory, run `docker-compose up -d` to build and run the Docker containers in `detached` mode.
  3. To stop the container just run: `docker-compose down`


        
* Deployment instructions


* Deployment with Docker from Artifactory
  1. `docker login -u ${artifactory_username} -p ${artifactory_password} artifactory.adesso-group.com` or you can login im browser
  2. `docker-compose up -d`  you need to be in the folder where the docker-compose.yml is located
  3.   to stop it just type  `docker-compose down`

* How to run tests

 	From any browser (or using Postman) type one of the following:

            fixed Uri:
      		 localhost:8181/api/v2/tasks/archive/once
      		 localhost:8181/api/v2/tasks/backup/once
      		 localhost:8181/api/v2/tasks/stopall

            Uri's  that need parameters:

            localhost:8181/api/v2/tasks/archive/once?logs=X
             - X by default it is equal to S, this will force logging using the  "isyloggerStandard" interface
             - X can be F to log with "isyloggerFachdaten" interface

            localhost:8181/api/v2/tasks/{taskName}/{plan}/{timeUnit}?rate=xxx
             - taskName: can be backup or archive
             - plan: can be  fixedrate or fixeddelay
      	     - timeUnit: can be seconds, minutes, hours or days
      	     - xxx: any decimal number depending on the chosen timeUnit above

### Contribution guidelines ###

* Writing tests
* Code review
* Other guidelines

### Who do I talk to? ###

* Repo owner or admin
* Other community or team contact
