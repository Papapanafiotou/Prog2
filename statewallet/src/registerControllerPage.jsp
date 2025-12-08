<%@ page contentType="text/html;charset=UTF-8" errorPage.jsp" %>
<%@ include file="headerPage.jsp" %>

<div class="container" style="margin-top:30px;">
    <div class="main-section">
    <%
        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");
        String surname = request.getParameter("Surname");
        String username = request.getParameter("Username");
        String password = request.getParameter("Password");
        String confirm = request.getParameter("Confirm");
        String email = request.getParameter("email");
        String agree = request.getParameter("terms");

        List<String> errors = new ArrayList<>();

        if (name == null || name.trim().length() < 3)
            errors.add("1. Name must be at least 3 characters long");
        if (surname == null || surname.trim().length() < 3)
            errors.add("2. Surname must be at least 3 characters long");
        if (username == null || username.trim().length() < 5)
            errors.add("3. Username must be at least 5 characters long");
        if (password == null || password.trim().length() < 6)
            errors.add("4. Password must be at least 6 characters long");
        if (confirm == null || !confirm.equals(password))
            errors.add("5. Password and confirm do not match");
        if (agree == null)
            errors.add("6. You must agree to terms and conditions");

        if (errors.isEmpty()) {
    %>
        <h3>Registration almost done!</h3>
        <div class="success-box">
            <p>Note: A verification link has been sent to the email: <strong><%= email %></strong></p>
        </div>
        <ul class="list-unstyled">
            <li><strong>Name:</strong> <%= name %></li>
            <li><strong>Surname:</strong> <%= surname %></li>
            <li><strong>Username:</strong> <%= username %></li>
            <li><strong>Email:</strong> <%= email %></li>
        </ul>
    <%
        } else {
    %>
        <h3 style="color:#000; margin-bottom:10px;">Registration form has errors</h3>
        <div class="error-box">
            <ol style="margin:0;">
            <%
                for (String error : errors) {
            %>
                <li><%= error %></li>
            <%
                }
            %>
            </ol>
        </div>
        <p style="margin-top:15px;">
            <a href="registerPage.jsp" class="btn btn-primary">
                <strong>&lt;</strong> Back to the form
            </a>
        </p>
    <%
        }
    %>
    </div>
</div>

<%@ include file="footerPage.jsp" %>
