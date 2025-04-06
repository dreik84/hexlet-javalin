@import org.example.WelcomePage
@param WelcomePage welcomePage

@template.layout.page(
    page = welcomePage,
    content = @`
        <p>Welcome, ${welcomePage.getUserName()}.</p>
    `,
    footer = @`
        <p>Thanks for visiting, come again soon!</p>
    `
)