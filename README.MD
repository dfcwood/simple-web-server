SIMPLE_WEB_SERVER

Provides a simple webserver that avoids the use of HTTP processing libraries.  Provides support for two GET requests and one POST request.  One GET request returns the contents of a file directory.  The other GET request returns a .png file from that directory.  The POST request supports uploading a file and saving it in that directory.  The server is designed to be used from the CLI.

#############################


Getting Started

These notes will assist you in getting started with setting up a dev environment and deploying the server.

#############################


Prerequisites
JavaSE-1.8 or higher
	See Oracle website for installation instructions
	http://www.oracle.com/technetwork/java/javase/downloads/index.html
Maven
	See Apache Maven website for installation instructions
	https://maven.apache.org/install.html
Git
	See Git website for installation instructions
	https://git-scm.com/book/en/v2/Getting-Started-Installing-Git

#############################
	
Installing

1.  Open a command line interface
	-on a linux system, open terminal
	
2.  Select the installation location and create a webserver directory
	Example:  mkdir /Users/dfcwood/webserver
	
3.  Go to the directory just created
	Example:  cd /Users/dfcwood/webserver
	
4.  Clone the GIT repository
	Example:  git clone https://github.com/dfcwood/simple-web-server.git
	
All files necessary are now located in /path/to/your/folder/simple-web-server.

Source files are located in src/main/java

Resource files are located in src/main/resources

Test files are located in src/test/java

#############################

Deploying the Code

1.  Once you have cloned the GIT repository, you can deploy the code

2.  Configure the server
	a.  Open the webserver configuration file in a text editor
		Located in src/main/resources/custom.properties
	b.  There are two main properties that need to be configured
		saved.files.directory  -- Defines the directory that will be used to save and return images
		socket.port  -- Defines the port number that is used by the server
						NOTE:  Do not use a part that is already in use or will be in use by another application
3.  Compile and Build the executible jar file
	a.  From the simple-webserver-directory call Maven to clean and install the application
		Example:  mvn clean install
	b.  Verify the JAR file was created by navigating to folder target and looking at file list
		Example:  cd target
		Example:  ls target/
4.  Once the jar file is created, launch the application using java -jar
	Example:  java -jar webserver-0.0.1-SNAPSHOT.jar
5.  Logs are sent to STDOUT and can be redirected to a file
	Example:  java -jar webserver-0.0.1-SNAPSHOT.jar > /tmp/webserver/webserver.logs
6.  Application can be run in the background
	Example:  java -jar webserver-0.0.1-SNAPSHOT.jar > /tmp/webserver/webserver.logs &
	

#############################
	
Usage

The following notes will assist you in using the webserver once installed

Endpoint /images
	Usage:  Returns a list of files from the specified directory
	Example:  curl http://<SERVER_IP_OR_FQDN>:<PORT>/images
	
	Expected Response:  Comma separated list of files in directory
	
Endpoint /image/<filename.png>
	Usage:  Returns a file with the specified <filename.png>
	Example:  curl http://<SERVER_IP_OR_FQDN>:<PORT>/image/<filename.png>
	
	Expected Response:  Returns 200 and the file.
			Returns 404 if the file doesn't exist in the directory
	
Endpoint /upload/<filename.png>
	Usage:  POST a file to this endpoint to upload a file and save it to the specified directory with filename <filename.png>
	Example:  curl -X POST --upload-file /Users/dfcwood/Pictures/trollface.png http://localhost:9991/upload/troll.png
	
	Expected Response:  Returns 100 to start the upload and then 200 when the upload is complete

Any http request other than GET and POST will return a 400.
Any http request to an endpoint other than the ones above will return a 404.

#############################

Testing Code

This next section contains notes on how to run provided test cases.  There are some prerequisites which will be defined below.  One limitation of the test cases is it does not test App.java, but tests all other classes.

Prerequisites:
1.  The directory defined in custom.properties must exist
2.  A file called picture.png must exist in the directory.  Not required to contain data.

Steps:
Run maven with the test life cycle.  Recommend running clean and compile lifecycles as well.
Example:  mvn clean compile test


Built With:
1.  Eclipse
2.  Maven
3.  JUnit

Authors:
1.  Daniel Calderwood (myself)
