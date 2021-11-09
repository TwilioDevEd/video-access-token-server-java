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
import spark.ModelAndView;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import java.util.HashMap;
import java.util.Map;

public class Webapp {
  private static Dotenv env = Dotenv.configure().ignoreIfMissing().load();

  public static void main(String[] args) throws Exception {
    // Log all requests and responses
    afterAfter(new LoggingFilter());

    // Handle errors
    internalServerError((request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      return new ThymeleafTemplateEngine().render(
          new ModelAndView(model, "error")
      );
    });
    notFound((request, response) -> {
        Map<String, Object> model = new HashMap<String, Object>();
        return new ThymeleafTemplateEngine().render(
            new ModelAndView(model, "error")
        );
    });

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
}
