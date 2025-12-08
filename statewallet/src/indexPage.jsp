<%@ page contentType="text/html; charset=UTF-8" errorPage="error_ex2_8230118.jsp" %>
<%
    request.setAttribute("currentPage", "Home");
%>
<jsp:include page="header.jsp" />

<div class="main-container" style="margin: 20px;">
    <h2>Κεντρική Σελίδα - "StateWallet"</h2>
    <p>Καλωσήρθατε στο StateWallet, <%
        Statewallet_ex_2025_2026.UserPage user =
            (Statewallet_ex_2025_2026.UserPage) session.getAttribute("userObj2025");
        if(user != null) {
            out.print(user.getFirstname() + " " + user.getLastname());
        } else {
            out.print("επισκέπτης");
        }
    %>!</p>

    <hr>

    <h3>Στατιστικά (demo)</h3>
    <table border="1" cellpadding="8" cellspacing="0">
        <tr style="background-color:#eee;">
            <th>Μήνας/Έτος</th>
            <th>Έσοδα (εκατ. €)</th>
            <th>Έξοδα (εκατ. €)</th>
        </tr>
        <tr>
            <td>Ιανουάριος 2022</td>
            <td>120</td>
            <td>95</td>
        </tr>
        <tr>
            <td>Ιούνιος 2022</td>
            <td>150</td>
            <td>130</td>
        </tr>
        <tr>
            <td>Δεκέμβριος 2022</td>
            <td>180</td>
            <td>160</td>
        </tr>
    </table>

    <p style="margin-top:20px;">
        Σημείωση: Τα παραπάνω στοιχεία είναι ενδεικτικά. Στην τελική έκδοση μπορούν να αντικατασταθούν με πραγματικά δεδομένα από την ΕΛΣΤΑΤ.
    </p>
</div>

<jsp:include page="footer.jsp" />
