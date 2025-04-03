package org.example.hexlet;

import io.javalin.Javalin;

public class HelloWorld  {
    public static void main(String[] args) {

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
        });

        app.get("/", ctx -> ctx.result("Hello World"));
        app.get("/users", ctx -> ctx.result("GET /users"));
        app.post("/users", ctx -> ctx.result("POST /users"));

        app.get("/hello", ctx -> {
            var name = ctx.queryParamAsClass("name", String.class).getOrDefault("World");
            ctx.result("Hello " + name + "!");
        });

        app.get("/courses/{id}", ctx -> {
            ctx.result("Course ID: " + ctx.pathParam("id"));
        });
        app.get("/users/{id}", ctx -> {
            var id = ctx.pathParamAsClass("id", Integer.class).get();
            ctx.result("User ID: " + id);
        });

        app.get("/courses/{courseId}/lessons/{id}", ctx -> {
            var courseId = ctx.pathParam("courseId");
            var lessonId =  ctx.pathParam("id");
            ctx.result("Course ID: " + courseId + " Lesson ID: " + lessonId);
        });

        app.start(7070);
    }
}
