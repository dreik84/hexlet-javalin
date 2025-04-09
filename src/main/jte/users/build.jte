@import org.example.hexlet.dto.courses.CoursesPage
@param CoursesPage page

@template.layout.page(
    page = page,
    content = @`
        <p>Welcome, .</p>
        <form action="/users" method="post">
          <div>
            <label>
              Имя
              <input type="text" name="name" />
            </label>
          </div>
          <div>
            <label>
              Email
              <input type="email" required name="email" />
            </label>
          </div>
          <div>
            <label>
              Пароль
              <input type="password" required name="password" />
            </label>
          </div>
          <div>
            <label>
              Подтверждение пароля
              <input type="password" required name="passwordConfirmation" />
            </label>
          </div>
          <input type="submit" value="Зарегистрировать" />
        </form>
    `,
    footer = @`
        <p>Thanks for visiting, come again soon!</p>
    `
)