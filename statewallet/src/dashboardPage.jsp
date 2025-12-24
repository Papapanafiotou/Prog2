<%@ page contentType="text/html; charset=UTF-8" errorPage="errorPage.jsp" %>
<%@ page import="java.util.List" %>
<%@ page import="Statewallet_ex_2025_2026.UserPage" %>
<%@ page import="Statewallet_ex_2025_2026.UserPageDAO" %>

<%
    request.setAttribute("currentPage", "Dashboard");

    // Λήψη του συνδεδεμένου χρήστη από το session [cite: 10]
    UserPage currentUser = (UserPage) session.getAttribute("userObj2025");

    // Αν δεν υπάρχει χρήστης στο session, ανακατεύθυνση στο login 
    if(currentUser == null) {
        response.sendRedirect("loginPage.jsp");
        return;
    }

    // Δημιουργία της DAO για να πάρουμε τη λίστα των μελών
    UserPageDAO dao = new UserPageDAO();
    List<UserPage> allUsers = dao.getUsers();
%>

<%@ include file="headerPage.jsp" %>

<div class="container" style="margin-top:120px; margin-bottom:100px;">

    <div class="jumbotron" style="background-color:#e0e0e0; padding: 40px;">
        <h1 style="margin:0;">StateWallet Dashboard</h1>
    </div>

    <div style="margin-bottom:30px; padding-left:15px;">
        <h2>
            Welcome 
            <span style="background-color:#ff8a80; padding:5px 10px; border-radius:5px;">
                <%= currentUser.getFirstname() + " " + currentUser.getLastname() %>
            </span>
        </h2>
    </div>

    <div class="row">
        <div class="col-xs-12">
            <h3 style="margin-bottom:20px;">Team Members List</h3>
            <table class="table table-bordered table-striped">
                <thead>
                    <tr style="background-color:#a3c4f3; color:#000; font-weight:bold;">
                        <th>A/A</th>
                        <th>Last Name</th>
                        <th>First Name</th>
                        <th>Username</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        int count = 1;
                        for(UserPage u : allUsers) {
                            // Έλεγχος αν ο χρήστης της γραμμής είναι ο ίδιος με τον συνδεδεμένο
                            boolean isCurrent = u.getUsername().equals(currentUser.getUsername());
                    %>
                    <tr style="<%= isCurrent ? "background-color:#c8e6c9; font-weight:bold;" : "" %>">
                        <td><%= count++ %></td>
                        <td><%= u.getLastname() %></td>
                        <td><%= u.getFirstname() %></td>
                        <td><%= u.getUsername() %></td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="footerPage.jsp" %>