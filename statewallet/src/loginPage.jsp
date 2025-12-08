<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="headerPage.jsp" %>

<div class="container" style="margin-top: 50px; max-width: 400px;">

    <!-- Κόκκινο popup alert -->
    <% if(request.getAttribute("message") != null) { %>
    <div id="loginError" style="background-color: #f8d7da; color: #721c24; padding: 10px; border-radius: 5px; text-align:center; margin-bottom: 15px;">
        <%= (String)request.getAttribute("message") %>
    </div>
    <% } %>

    <form method="post" action="loginControllerPage.jsp">
        <h2 style="text-align:center;">Please sign in</h2>
        <label>Username</label>
        <input type="text" name="username" placeholder="username" required style="width:100%; padding:8px; margin-bottom:10px;">

        <label>Password</label>
        <input type="password" name="password" placeholder="password" required style="width:100%; padding:8px; margin-bottom:10px;">

        <button type="submit" style="width:100%; padding:10px; background-color:#337ab7; color:white; border:none;">Sign in</button>
    </form>

    <div style="margin-top:15px; padding:10px; background-color:#e7f3fe; color:#31708f; border-radius:5px;">
        <strong>Help: </strong>
        <ul>
            <% 
                Statewallet_ex_2025_2026.UserPageDAO dao = new Statewallet_ex_2025_2026.UserPageDAO();
                for(Statewallet_ex_2025_2026.UserPage u : dao.getUsers()) {
            %>
                <li>For <%= u.getFirstname() + " " + u.getLastname() %>: <%= u.getUsername() %> <%= u.getPassword() %></li>
            <% } %>
        </ul>
    </div>

</div>

<%@ include file="footerPage.jsp" %>
