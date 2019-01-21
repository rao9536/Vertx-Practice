package io.vertx.example.web.angularjs;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public class Server extends AbstractVerticle {

  // vertx application starting
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }


  @Override
  public void start() throws Exception {

    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    // define rest api to accpet request
    router.get("/api/imageList").handler(ctx -> {
        JSONArray jsonArray=new JSONArray();
        JSONParser parser = new JSONParser();
      try {
          // external source for images
          String url = "https://jsonplaceholder.typicode.com/photos";
          // httpclient to connect with given url
          HttpClient httpClient = HttpClientBuilder.create().build();
          HttpGet httpGet = new HttpGet(url);
          ResponseHandler<String> responseHandler = new BasicResponseHandler();
          
          // accepting the response from given url
          String responseBody = httpClient.execute(httpGet, responseHandler);
          jsonArray = (JSONArray) parser.parse(responseBody);

      }catch (Exception e){
        e.printStackTrace();
      }


        // now convert the list to a JsonArray because it will be easier to encode the final object as the response.
        final JsonArray json = new JsonArray();

        for (int i=0; i<jsonArray.size();i++) {
            JsonObject object = JsonObject.mapFrom(jsonArray.get(i));
            json.add(object);
        }

        ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        ctx.response().end(json.encode());
        System.out.println("All images sent to response object");

    });


    // Create a router endpoint for the static content.
    router.route().handler(StaticHandler.create());

    // setting up the port
    vertx.createHttpServer().requestHandler(router).listen(8443);
  }

}
