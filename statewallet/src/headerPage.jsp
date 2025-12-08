<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Παίρνουμε τον χρήστη από session
    Statewallet_ex_2025_2026.UserPage user =
        (Statewallet_ex_2025_2026.UserPage) session.getAttribute("userObj2025");
    boolean isLoggedIn = (user != null);
    String fullName = isLoggedIn ? user.getFirstname() + " " + user.getLastname() : "Guest";

    // Για να ενεργοποιείται σωστά το μενού
    String currentPage = (String) request.getAttribute("currentPage");
    if (currentPage == null) currentPage = "";
%>

<!-- Header / Menu -->
<div style="background-color:#222; padding:10px; color:white;">
    <span style="font-weight:bold;">StateWallet</span>
    <span style="float:right;">
        <% if(isLoggedIn) { %>
            Welcome, <%= fullName %> |
            <a href="logoutPage.jsp" style="color:white; text-decoration:none;">Logout</a>
        <% } else { %>
            <a href="loginPage.jsp" style="color:white; text-decoration:none;">Login</a> |
            <a href="registerPage.jsp" style="color:white; text-decoration:none;">Register</a>
        <% } %>
    </span>
</div>

<!-- Navigation Menu -->
<div style="background-color:#444; padding:5px;">
    <a href="indexPage.jsp" style="color:white; margin-right:15px; text-decoration:none;">
        Home
    </a>
    <a href="aboutPage.jsp" style="color:white; margin-right:15px; text-decoration:none;">
        About
    </a>
    <% if(isLoggedIn) { %>
        <a href="dashboardPage.jsp" style="color:white; margin-right:15px; text-decoration:none;">
            Dashboard
        </a>
    <% } %>
</div>
