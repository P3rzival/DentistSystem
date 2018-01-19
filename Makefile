javafiles := $(wildcard src/*.java)

bin/DentistApplication: $(javafiles) dependencies/mysql-connector.jar
	mkdir -p bin
	javac -d bin/ $(javafiles)

# Download the mysql connector and put it in the dependencies folder
dependencies/mysql-connector.jar:
	mkdir -p dependencies
	wget https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-5.1.44.tar.gz
	tar xzf mysql-connector-java-*.tar.gz
	mv mysql-connector-java-*/mysql-connector-java-*.jar dependencies/mysql-connector.jar
	rm -rf mysql-connector-java-*

run: bin/DentistApplication
	cd bin;java -cp ../dependencies/*:. DentistApplication

createdatabase: bin/DentistApplication
	cd bin;java -cp ../dependencies/*:. CreateDatabase

clean:
	rm -rf bin/
