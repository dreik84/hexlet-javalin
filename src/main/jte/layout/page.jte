@import org.example.hexlet.dto.courses.CoursesPage
@import gg.jte.Content

@param CoursesPage page
@param Content content
@param Content footer = null

<head>
    <meta charset="UTF-8">
    <title>Document</title>
</head>
<body>
    <h1>Hello</h1>
    <div class="content">
        ${content}
    </div>
    @if (footer != null)
        <div class="footer">
            ${footer}
        </div>
    @endif
</body>