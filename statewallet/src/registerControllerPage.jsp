<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setCharacterEncoding("UTF-8"); // Για ελληνικά
    String ministry = request.getParameter("ministry");
    String name = request.getParameter("name");
    String surname = request.getParameter("surname");
    String email = request.getParameter("email");
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    String message = "";

    if(ministry == null || name == null || surname == null || email == null || username == null || password == null ||
       ministry.isEmpty() || name.isEmpty() || surname.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
        message = "Παρακαλώ συμπληρώστε όλα τα πεδία!";
        request.setAttribute("message", message);
        request.getRequestDispatcher("registerPage.jsp").forward(request, response);
        return;
    }

    // Δημιουργία αντικειμένου χρήστη
    Statewallet_ex_2025_2026.UserPage newUser = new Statewallet_ex_2025_2026.UserPage();
    newUser.setMinistry(ministry);
    newUser.setFirstname(name);
    newUser.setLastname(surname);
    newUser.setEmail(email);
    newUser.setUsername(username);
    newUser.setPassword(password); // Μπορείς να προσθέσεις κρυπτογράφηση αν θες

    Statewallet_ex_2025_2026.UserPageDAO dao = new Statewallet_ex_2025_2026.UserPageDAO();

    // Έλεγχος αν υπάρχει ήδη username
    if(dao.getUserByUsername(username) != null) {
        message = "Το username υπάρχει ήδη! Επιλέξτε άλλο.";
        request.setAttribute("message", message);
        request.getRequestDispatcher("registerPage.jsp").forward(request, response);
        return;
    }

    // Προσθήκη χρήστη στη βάση
    dao.addUser(newUser);

    // Μήνυμα επιτυχίας και redirect στο login
    message = "Η εγγραφή ολοκληρώθηκε! Συνδεθείτε τώρα.";
    request.setAttribute("message", message);
    request.getRequestDispatcher("loginPage.jsp").forward(request, response);
%>
