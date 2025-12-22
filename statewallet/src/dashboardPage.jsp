<%@ page contentType="text/html; charset=UTF-8" errorPage="errorPage.jsp" %>
<%
    request.setAttribute("currentPage", "Dashboard");
    StateWallet_ex_2025_2026.UserPage currentUser =
        (StateWallet_ex_2025_2026.UserPage) session.getAttribute("userObj2025");

    if(currentUser == null) {
        response.sendRedirect("loginPage.jsp");
        return;
    }
%>

<%@ include file="headerPage.jsp" %>

<div class="container" style="margin-top:120px; margin-bottom:100px;">

    <!-- Μεγάλο γκρι πλαίσιο με τίτλο -->
    <div class="jumbotron" style="background-color:#e0e0e0; padding: 40px;">
        <h1 style="margin:0;">StateWallet</h1>
    </div>

    <!-- Welcome αριστερά με κόκκινο πλαίσιο για το όνομα -->
    <div style="margin-bottom:30px; padding-left:15px;">
        <h2>
            Welcome 
            <span style="background-color:#ff8a80; padding:5px 10px; border-radius:5px;">
                <%= currentUser.getFirstname() + " " + currentUser.getLastname() %>
            </span>
        </h2>
    </div>

    <!-- Πίνακας με τα μέλη -->
    <div class="row">
        <div class="col-xs-12">
            <table class="table table-bordered">
                <thead>
                    <tr style="background-color:#a3c4f3; color:#000; font-weight:bold;">
                        <th>A/A</th>
                        <th>Last Name</th>
                        <th>First Name</th>
                        <th>Email</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        class UserRow {
                            String first, last, email;
                            UserRow(String f, String l, String e) { first = f; last = l; email = e; }
                        }

                        UserRow[] users = {
                            new UserRow("Chris", "Karahalios", "kchris@somewhere.com"),
                            new UserRow("Marios", "Voutsas", "mvoutsas@somewhere.com"),
                            new UserRow("Christine", "Gallou", "cgallou@somewhere.com")
                            new UserRow("Axilleas", "Damianidis", "adam@somewhere.com")
                            new UserRow("Max", "Denaxas", "mdenaxas@somewhere.com")
                            new UserRow("John", "Mpal", "jmpal@somewhere.com")
                            new UserRow("Jason", "Damianidis", "jdam@somewhere.com")
                            new UserRow("Konstantina", "Papapanag", "kpap@somewhere.com")
                        };

                        for(int i=0; i<users.length; i++){
                            UserRow u = users[i];
                            boolean isCurrent = u.first.equals(currentUser.getFirstname()) && u.last.equals(currentUser.getLastname());
                    %>
                    <tr style="<%= isCurrent ? "background-color:#c8e6c9;" : "" %>">
                        <td><%= i+1 %></td>
                        <td><%= u.last %></td>
                        <td><%= u.first %></td>
                        <td><%= u.email %></td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="footerPage.jsp" %>
