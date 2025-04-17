package org.example.hexlet;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import io.javalin.validation.ValidationException;
import org.example.hexlet.controller.SessionsController;
import org.example.hexlet.controller.UsersController;
import org.example.hexlet.dto.MainPage;
import org.example.hexlet.dto.courses.CoursesPage;
import org.example.hexlet.dto.users.BuildUserPage;
import org.example.hexlet.model.Course;

import org.apache.commons.text.StringEscapeUtils;
import org.example.hexlet.model.User;
import org.example.hexlet.repository.CourseRepository;
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

        app.before(ctx -> {
            var path = ctx.path();
            System.out.println("Request path: " + path);
        });

//        app.before(ctx -> {
//            String id = ctx.queryParam("id");
//            if (id == null || id.isEmpty()) {
//                ctx.status(400).result("Bad Request: Missing 'id' parameter");
//                ctx.skipRemainingHandlers(); // Завершаем обработку
//            }
//        });

        app.before(ctx -> {
            ctx.header("X-Custom-Header", "value");
        });

//        app.get("/", ctx -> {
//            var visited = Boolean.valueOf(ctx.cookie("visited"));
//            var page = new MainPage(visited);
//            ctx.render("index.jte", model("page", page));
//            ctx.cookie("visited", String.valueOf(true));
//        }

        app.get("/", ctx -> {
            var page = new MainPage(ctx.sessionAttribute("currentUser"));
            ctx.render("index.jte", model("page", page));
        });

        // Отображение формы логина
        app.get("/sessions/build", SessionsController::build);
        // Процесс логина
        app.post("/sessions", SessionsController::create);
        // Процесс выхода из аккаунта
        app.delete("/sessions", SessionsController::destroy);

        app.after(ctx -> {
            System.out.println("Response has been sent");
        });

        app.get(NamedRoutes.usersPath(), ctx -> ctx.result("GET /users"));

        app.get("/hello", ctx -> {
            var name = ctx.queryParamAsClass("name", String.class).getOrDefault("World");
            ctx.result("Hello " + name + "!");
        });

        app.post(NamedRoutes.coursesPath(), ctx -> {
            var name = ctx.formParam("name");
            var description = ctx.formParam("description");

            var course = new Course(name, description);
            CourseRepository.save(course);
            // Добавляем сообщение в сессию
            // Ключ может иметь любое название, здесь мы выбрали flash
            ctx.sessionAttribute("flash", "Course has been created!");
            ctx.redirect(NamedRoutes.coursesPath());
        });

        app.get(NamedRoutes.coursesPath(), ctx -> {
            var courses = List.of(new Course("java", "basics"));
            var term = "java";
            var page = new CoursesPage(courses, term);
            page.setFlash(ctx.consumeSessionAttribute("flash"));
            ctx.render("courses/index.jte", model("page", page));
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

        app.get("/users", UsersController::index);
        app.get("/users/build", UsersController::build);
        app.get("/users/{id}", UsersController::show);
        app.post("/users", UsersController::create);
        app.get("/users/{id}/edit", UsersController::edit);
        app.patch("/users/{id}", UsersController::update);
        app.delete("/users/{id}", UsersController::destroy);

        app.get("/courses/{courseId}/lessons/{id}", ctx -> {
            var courseId = ctx.pathParam("courseId");
            var lessonId =  ctx.pathParam("id");
            ctx.result("Course ID: " + courseId + " Lesson ID: " + lessonId);
        });

        app.start(7070);
    }
}
