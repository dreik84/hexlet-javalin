@import org.example.hexlet.dto.MainPage
@param MainPage page

<!DOCTYPE html>
<html lang="ru">
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Hello Hexlet!</title>
  </head>
  <body>
    <main>
      <h1>Привет, Хекслет!</h1>
      @if(!page.isVisited())
        Это сообщение показывается только один раз. Если вы хотите увидеть его снова, сотрите куки
      @endif
    </main>
  </body>
</html>