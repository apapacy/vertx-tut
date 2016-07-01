package io.vertx.blog.first;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
//import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class MyFirstVerticle extends AbstractVerticle {

@Override
public void start(Future<Void> fut) {


        createSomeData();

        // Create a router object.
        Router router = Router.router(vertx);

        router.get("/api/whiskies").handler(this::getAll);


        // Bind "/" to our hello message - so we are still compatible.
        router.route("/").handler(routingContext -> {
                                          HttpServerResponse response = routingContext.response();
                                          response
                                          .putHeader("content-type", "text/html")
                                          .end("<h1>Hello from my first Vert.x 3 application</h1>");
                                  });

        // Serve static resources from the /assets directory
        router.route("/static/*").handler(StaticHandler.create("assets"));
        // Create the HTTP server and pass the "accept" method to the request handler.
        vertx
        .createHttpServer()
        .requestHandler(router::accept)
        .listen(
                // Retrieve the port from the configuration,
                // default to 8080.
                config().getInteger("http.port", 8080),
                result -> {
                        if (result.succeeded()) {
                                fut.complete();
                        } else {
                                fut.fail(result.cause());
                        }
                }
                );
}

// Store our product
private Map<Integer, Whisky> products = new LinkedHashMap<>();
// Create some product
private void createSomeData() {
        Whisky bowmore = new Whisky("Bowmore 15 Years Laimrig", "Scotland, Islay");
        products.put(bowmore.getId(), bowmore);
        Whisky talisker = new Whisky("Talisker 57° North", "Scotland, Island");
        products.put(talisker.getId(), talisker);
}

private void getAll(RoutingContext routingContext) {
        routingContext.response()
        .putHeader("content-type", "application/json; charset=utf-8")
        .end(Json.encodePrettily(products.values()));
}



}
