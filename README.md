Application can be run in two ways.

	- should have mail server running on the localhost(can be configured to any host later)
	- one option would be mailhog email server
	- can be used mailhog docker image from docker hub

1. From eclipse 

	1.1 run following docker command to start the email server
		docker run --restart unless-stopped --name mailhog -p 1025:1025 -p 8025:8025 -d mailhog/mailhog
		
			- go to http://localhost:8025
	1.2 run EmailSender.java
	1.3 run EmailClient.java (can be configured to n requests using t threads.)
	1.4 acknowledgement message should be displayed in client console
	1.5 email messages should be available in the mailhog inbox
	
2. Package Jar and Run

	2.1 run following docker command to start the email server
			docker run --restart unless-stopped --name mailhog -p 1025:1025 -p 8025:8025 -d mailhog/mailhog
			
			- go to http://localhost:8025
	2.2 package jar using following command
			mvn clean compile assembly:single 
	2.4 go to target folder
	2.3 run email sender
			java -jar emailsender-0.0.1-SNAPSHOT-jar-with-dependencies.jar
	2.4 run email client
			java -cp emailsender-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.emailer.client.EmailClient
	2.5 acknowledgement message should be displayed in client console
	2.6 email messages should be available in the mailhog inbox
	
Note: Sample Request/ Response files are available in resources folder