package org.example.hexlet;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import org.example.hexlet.dto.courses.CoursesPage;
import org.example.hexlet.model.Course;

import org.apache.commons.text.StringEscapeUtils;

import java.util.List;

import static io.javalin.rendering.template.TemplateUtil.model;

public class HelloWorld  {
    public static void main(String[] args) {

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte());
        });

        app.get("/", ctx -> ctx.render("index.jte"));
        app.get("/users", ctx -> ctx.result("GET /users"));
        app.post("/users", ctx -> ctx.result("POST /users"));

        app.get("/hello", ctx -> {
            var name = ctx.queryParamAsClass("name", String.class).getOrDefault("World");
            ctx.result("Hello " + name + "!");
        });

        app.get("/courses/{id}", ctx -> {
            var id = ctx.pathParam("id");
            var course = new Course("java", "java basics");
            var page = new CoursesPage(List.of(new Course("java", "basics")), "java");
            ctx.render("courses/show.jte", model("page", page));
        });

//        app.get("/users/{id}", ctx -> {
//            var id = ctx.pathParamAsClass("id", Integer.class).get();
//            ctx.result("User ID: " + id);
//        });

        app.get("/users/{id}", ctx -> {
            var id = ctx.pathParam("id");
            var escapedId = StringEscapeUtils.escapeHtml4(id);
            ctx.contentType("text/html");
            ctx.result(escapedId);
        });

        app.get("/courses/{courseId}/lessons/{id}", ctx -> {
            var courseId = ctx.pathParam("courseId");
            var lessonId =  ctx.pathParam("id");
            ctx.result("Course ID: " + courseId + " Lesson ID: " + lessonId);
        });

        app.start(7070);
    }
}
