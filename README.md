# JahiaUpgradeSimulator v0.6
### Released 02/26/2015

Prerequisites
========

* Java 1.7+

* Apache Maven

Libraries used :
========
* JSON Simple
https://code.google.com/p/json-simple/downloads/detail?name=json-simple-1.1.1.jar&can=2&q=

* Apache Commons IO
http://commons.apache.org/proper/commons-io/download_io.cgi

* SLF4J
http://www.slf4j.org/download.html

* LOG4J
http://logging.apache.org/log4j/2.x/download.html

IDE & dev environment
========
* Eclipse Kepler+
http://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/kepler/SR2/eclipse-standard-kepler-SR2-linux-gtk-x86_64.tar.gz

* WindowBuilder Plugin
http://www.vogella.com/tutorials/EclipseWindowBuilder/article.html

How to install & launch
========

- Download a zip archive
- Unzip it
- Use Maven as following :
```shell
cd path_to_archive
mvn clean package
cd target
```
- Execute the generated JAR :
```shell
java -jar jar_name.jar \[PATH_TO_JAHIA] [context]
```
