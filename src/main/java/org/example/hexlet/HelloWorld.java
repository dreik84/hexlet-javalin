package org.example.hexlet;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import io.javalin.validation.ValidationException;
import org.example.hexlet.dto.courses.CoursesPage;
import org.example.hexlet.dto.users.BuildUserPage;
import org.example.hexlet.model.Course;

import org.apache.commons.text.StringEscapeUtils;
import org.example.hexlet.model.User;
import org.example.hexlet.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static io.javalin.rendering.template.TemplateUtil.model;

public class HelloWorld  {
    public static void main(String[] args) {

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte());
        });

        app.get("/", ctx -> ctx.render("index.jte"));
        app.get(NamedRoutes.usersPath(), ctx -> ctx.result("GET /users"));



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

        app.get("/courses", ctx -> {
            var term = ctx.queryParam("term");
            ArrayList<Course> courses;
            // Фильтруем, только если была отправлена форма
            if (term != null) {
                courses = new ArrayList<>();
            } else {
                courses = new ArrayList<>();
            }
            var page = new CoursesPage(courses, term);
            ctx.render("courses/index.jte", model("page", page));
        });
        app.start(7070);

//        app.get("/users/{id}", ctx -> {
//            var id = ctx.pathParamAsClass("id", Integer.class).get();
//            ctx.result("User ID: " + id);
//        });

        app.get("/users/build", ctx -> {
            var page = new BuildUserPage();
            ctx.render("users/build.jte", model("page", page));
        });

        app.post("/users", ctx -> {
            var name = ctx.formParam("name");
            var email = ctx.formParam("email");

            try {
                var passwordConfirmation = ctx.formParam("passwordConfirmation");
                var password = ctx.formParamAsClass("password", String.class)
                        .check(value -> value.equals(passwordConfirmation), "Пароли не совпадают")
                        .get();
                var user = new User(name, email, password);
                UserRepository.save(user);
                ctx.header("Content-Type: text/html; charset=utf-8");
                ctx.redirect("/users");
            } catch (ValidationException e) {
                var page = new BuildUserPage(name, email, e.getErrors());
                ctx.header("Content-Type: text/html; charset=utf-8");
                ctx.render("users/build.jte", model("page", page));
            }
        });

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
