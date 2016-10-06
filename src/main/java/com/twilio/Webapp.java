package com.twilio;

import com.google.gson.Gson;

import com.github.javafaker.Faker;
import com.twilio.sdk.auth.AccessToken;
import com.twilio.sdk.auth.ConversationsGrant;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;

import static spark.Spark.get;
import static spark.Spark.staticFileLocation;

public class Webapp {

  public static void main(String[] args) throws Exception {
    // Load the .env file into environment
    dotenv();

    // Serve static files from src/main/resources/public
    staticFileLocation("/public");

    // Create a Faker instance to generate a random username for the connecting user
    Faker faker = new Faker();

    // Create an access token using our Twilio credentials
    get("/token", "application/json", (request, response) -> {
      // Generate a random username for the connecting client
      String identity = faker.firstName() + faker.lastName() + faker.zipCode();

      // Create Conversations messaging grant
      ConversationsGrant grant = new ConversationsGrant();
      grant.configurationProfileSid = System.getProperty("TWILIO_CONFIGURATION_SID");

      // Create access token
      AccessToken token = new AccessToken.Builder(
        System.getProperty("TWILIO_ACCOUNT_SID"),
        System.getProperty("TWILIO_API_KEY"),
        System.getProperty("TWILIO_API_SECRET")
      ).identity(identity).grant(grant).build();

      // create JSON response payload
      HashMap<String, String> json = new HashMap<>();
      json.put("identity", identity);
      json.put("token", token.toJWT());

      // Render JSON response
      Gson gson = new Gson();
      return gson.toJson(json);
    });
  }

  private static void dotenv() throws Exception {
    File env = new File(".env");
    if (!env.exists()) {
      return;
    }

    Properties props = new Properties();
    props.load(new FileInputStream(env));
    props.putAll(System.getenv());
    props.entrySet().forEach(p -> System.setProperty(p.getKey().toString(), p.getValue().toString()));
  }
}
