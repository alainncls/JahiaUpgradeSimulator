# JahiaUpgradeSimulator v0.8
### Released 02/03/2015

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


Changelog
========

04/02/2015 - v0.1 : [ GRAPHIQUE ] Liste des patchs et des liens associés
* Fiches menu, patchs et instructions
* Téléchargement des patchs via navigateur
* Estimation des étapes
* Détection de la version de jhaia installée
09/02/2015 - v0.2 : [ GRAPHIQUE ] Prise en compte des reboots + Téléchargement des patchs directement dans l'application
* Amélioration UI fiche patchs + Affichage des instructions
* Téléchargements directs
* MAJ du JSON : warning, instructions, reboot
* ActionButton pour gérer le cycle d'application d'un patch
17/02/2015 - v0.3 : [ GRAPHIQUE ] Chiffrage des migrations
* Définition d'une formule pour le chiffrage
* Download et Apply sont multithreadés
* Fichiers de logs
18/02/2015 - v0.4 : [ GRAPHIQUE ] Prise en compte des changements de licence
* Vérification des changements de licence nécessaires
* MAJ du JSON : licence
* Optimisation de la fiche patchs
* Détection de patchs exécutables automatiquement ou à la main
* ActionButton activé selon la version de Jahia installée
* ActionButton caché si pas de version de Jahia détectée
19/02/2015 - v0.5 : [ CONSOLE ] Saisie d'une simulation + chiffrage
* Optimisation du code
* Passage en mode console
* MAJ du JSON
26/02/2015 - v0.6 : [ MAVEN ] Mavenization + Mise en place d'un système de logs
* Conversion du projet en Maven
* Passage de Sonar sur le projet
* Utilisation d'un logger (slf4j)
02/03/2015 - v0.7 : [ SONAR ] Qualité du code : Technical debt = 0
* Applciation des règles de qualité de code Sonar
02/03/2015 - v0.8 : [ GRAPHIQUE ] Ajout d'une page de changement de configuration
* Page de changement de configuration del'installation de Jahia en graphique