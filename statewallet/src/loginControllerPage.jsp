<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setCharacterEncoding("UTF-8"); // Για ελληνικά
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    String message = "";

    if(username == null || password == null || username.isEmpty() || password.isEmpty()) {
        message = "Συμπληρώστε username και password!";
        request.setAttribute("message", message);
        request.getRequestDispatcher("loginPage.jsp").forward(request, response);
        return;
    }

    Statewallet_ex_2025_2026.UserPageDAO dao = new Statewallet_ex_2025_2026.UserPageDAO();
    Statewallet_ex_2025_2026.UserPage user = dao.getUserByUsername(username);

    if(user != null && user.getPassword().equals(password)) { // Αν δεν κάνουμε hash
        session.setAttribute("userObj2025", user);
        response.sendRedirect("indexPage.jsp"); // Redirect στην αρχική
    } else {
        message = "Λανθασμένο username ή password!";
        request.setAttribute("message", message);
        request.getRequestDispatcher("loginPage.jsp").forward(request, response);
    }
%>
