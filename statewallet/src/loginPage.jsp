<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="headerPage.jsp" %>

<div class="container" style="margin:50px auto; max-width:400px; text-align:center;">

    <% if(request.getAttribute("message") != null) { %>
    <div id="loginError" style="background-color: #f8d7da; color: #721c24; padding: 12px; border-radius: 8px; margin-bottom: 15px;">
        <%= (String)request.getAttribute("message") %>
    </div>
    <% } %>

    <div style="background-color:#f5f5f5; padding:30px; border-radius:15px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);">
        <form method="post" action="loginControllerPage.jsp">
            <h2 style="margin-bottom:20px;">Please Sign In</h2>

            <div style="margin-bottom:15px; text-align:left;">
                <label for="username" style="display:block; margin-bottom:5px; font-weight:bold;">Username</label>
                <input id="username" type="text" name="username" placeholder="Enter your username" required
                       style="width:100%; padding:10px; border-radius:8px; border:1px solid #ccc; box-sizing:border-box;">
            </div>

            <div style="margin-bottom:20px; text-align:left;">
                <label for="password" style="display:block; margin-bottom:5px; font-weight:bold;">Password</label>
                <input id="password" type="password" name="password" placeholder="Enter your password" required
                       style="width:100%; padding:10px; border-radius:8px; border:1px solid #ccc; box-sizing:border-box;">
            </div>

            <button type="submit" style="width:100%; padding:12px; border:none; border-radius:10px; background-color:#337ab7; color:white; font-size:16px; cursor:pointer;">
                Sign In
            </button>
        </form>

        <div style="margin-top:20px; padding:10px; background-color:#e7f3fe; color:#31708f; border-radius:8px; text-align:left;">
            <strong>Help: </strong>
            <ul style="padding-left:20px;">
                <% 
                    Statewallet_ex_2025_2026.UserPageDAO dao = new Statewallet_ex_2025_2026.UserPageDAO();
                    for(Statewallet_ex_2025_2026.UserPage u : dao.getUsers()) {
                %>
                    <li>For <%= u.getFirstname() + " " + u.getLastname() %>: <%= u.getUsername() %> / <%= u.getPassword() %></li>
                <% } %>
            </ul>
        </div>
    </div>

</div>



<%@ include file="footerPage.jsp" %>
