This is inteded to be used to pull data from zenhub and github and export it to a TSV.

#Build
`docker run -it --rm -u gradle -v "$PWD":/home/gradle/project -w /home/gradle/project gradle bash`
or
`docker run -it --rm -u gradle -v "$PWD":/home/gradle/project -w /home/gradle/project gradle ./gradlew build`


#Run
`java -jar ./build/libs/github_exporter-1.0-SNAPSHOT.jar SmartColumbusOS,smartcitiesdata <ZEN_HUB_TOKEN> <GITHUB_TOKEN>`
or
`cd build/libs && docker run -it -v "$PWD":/home/ -w /home/ openjdk:15-slim java -jar github_exporter-1.0-SNAPSHOT.jar SmartColumbusOS,smartcitiesdata <ZEN_HUB_TOKEN> <GITHUB_TOKEN>`

#Tokens
ZenHub Token:
https://app.zenhub.com/dashboard/tokens

Github Token:
https://github.com/settings/tokens (only need repo access)