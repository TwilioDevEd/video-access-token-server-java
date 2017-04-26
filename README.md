# Video Access Token Server for Java

#### Looking for the JavaScript Video Quickstart? It has been moved [here](https://github.com/twilio/video-quickstart-js).

This server-side application demonstrates generating Access Token for Twilio Video.
Before we begin, we need to collect
all the config values we need to run the application:

| Config Value  | Description |
| :-------------  |:------------- |
Account SID | Your primary Twilio account identifier - find this [in the console here](https://www.twilio.com/console).
API Key | Used to authenticate - [generate one here](https://www.twilio.com/console/video/dev-tools/api-keys).
API Secret | Used to authenticate - [just like the above, you'll get one here](https://www.twilio.com/console/video/dev-tools/api-keys).

## A Note on API Keys

When you generate an API key pair at the URLs above, your API Secret will only
be shown once - make sure to save this in a secure location, 
or possibly your `~/.bash_profile`.

## Setting up the Java Application

This application uses the lightweight [Spark Framework](http://sparkjava.com/), and
requires Java 8 and [Maven](https://maven.apache.org/install.html). 

Begin by creating a configuration file for your application:

```bash
cp .env.example .env
```

Edit `.env` with the three configuration parameters we gathered from above. 

Next, we compile our application code:

```bash
mvn package
```

Now we should be all set! Run the application using the `java -jar` command.

```bash
java -jar target/video-quickstart-1.0-SNAPSHOT.jar
```

To generate Access Token, visit [http://localhost:4567?identity=alice&room=example](http://localhost:4567?identity=alice&room=example).

## License

MIT
