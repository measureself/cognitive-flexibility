cognitive-flexibility: taskswitching
=====================

Web application for running task switching experiments with participants. For
details on existing studies, see

"Cognitive Flexibility and Programming Performance"
http://www.sussex.ac.uk/Users/bend/ppig2014/1ppig2014_submission_19.pdf

The maven commands are to be written in the same directory where this file is.

*Testing*

Assuming that you have Java (version >= 1.7) and Maven on your computer, you can
try this directly on your local machine. Create a local mysql database and modify
the values in the method dataSource() of TSProfileDevelopment.java to match
your database details.

Once you have that set, write

mvn spring-boot:run

on command line, and the application is running at http://localhost:8080


*Packaging*

To package the source code for production, type the command 

mvn clean package

After packaging, you'll have a new war-file in the newly created target-folder.
You can launch it by e.g. saying

java -jar thewarfilename.war

*Configuration*

To use production mode (incl a real database, MySQL assumed), set the production 
server environment variable "SPRING_PROFILES_ACTIVE" to production;

export SPRING_PROFILES_ACTIVE=production

Now when the application starts, it knows to utilize the configuration in 
TsProfileDevelopment.java -- by default, it will assume that a file called
application.properties exists, and that it contains details on the database, e.g.

db.hostname=localhost
db.port=3306
db.path=/mydatabase?zeroDateTimeBehavior=convertToNull
db.username=something
db.username=else

*Deployment*

Copy the created war-file to the server. For example in tomcat, you need to copy
the war-file to the webapps-folder of tomcat. The application will start under
the path that corresponds to the name of the war-file.


*Using this in your research (or just for fun)*

Ask for more details! :)

*Have fun*
