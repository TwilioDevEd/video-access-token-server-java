package com.twilio;

import com.twilio.jwt.accesstoken.AccessToken;
import com.twilio.jwt.accesstoken.VideoGrant;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import static spark.Spark.afterAfter;
import static spark.Spark.get;
import static spark.Spark.staticFileLocation;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;
import io.github.cdimascio.dotenv.Dotenv;

public class Webapp {
  private static Dotenv env = Dotenv.configure().ignoreIfMissing().load();

  public static void main(String[] args) throws Exception {
    // Load the .env file into environment
    //dotenv();
    staticFileLocation("/public");

    // Log all requests and responses
    afterAfter(new LoggingFilter());
    // Using string/html to handle errors
    internalServerError("<html><body><h1>Something went wrong!</h1><h2>Our Engineers are on it</h2><a href='/'>Go Home</a></body></html>");
    notFound("<html><body><h1>Page not found</h1><a href='/'>Go Home</a></body></html>");

    // Create an access token using our Twilio credentials
    get("/", (request, response) -> {
      // Generate a random username for the connecting client
      final String identity = request.queryParams("identity") != null ? request.queryParams("identity") : "identity";

      System.out.println(identity);

      // Create Video grant
      final VideoGrant grant = new VideoGrant();
      grant.setRoom(request.queryParams("room"));

      // Create access token
      final AccessToken token = new AccessToken.Builder(
        env.get("TWILIO_ACCOUNT_SID"),
        env.get("TWILIO_API_KEY"),
        env.get("TWILIO_API_SECRET")
      ).identity(identity).grant(grant).build();

      return token.toJwt();
    });
  }

  private static void dotenv() throws Exception {
    final File env = new File(".env");
    if (!env.exists()) {
      return;
    }

    final Properties props = new Properties();
    props.load(new FileInputStream(env));
    props.putAll(System.getenv());
    props.entrySet().forEach(p -> System.setProperty(p.getKey().toString(), p.getValue().toString()));
  }
}
