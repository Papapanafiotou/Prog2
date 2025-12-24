<%@ page contentType="text/html; charset=UTF-8" errorPage="errorPage.jsp" %>
<%
    request.setAttribute("currentPage", "Home");
%>
<jsp:include page="headerPage.jsp" />

<div class="main-container" style="margin: 30px; font-family: Arial, sans-serif; line-height:1.6;">
    <h2 style="color:#222;">Κεντρική Σελίδα - "StateWallet"</h2>
    <p style="font-size:14px; color:#444;">
        Καλωσήρθατε στο <strong>StateWallet</strong>, <%
            Statewallet_ex_2025_2026.UserPage user =
                (Statewallet_ex_2025_2026.UserPage) session.getAttribute("userObj2025");
            if(user != null) {
                out.print(user.getFirstname() + " " + user.getLastname());
            } else {
                out.print("επισκέπτης");
            }
        %>!
    </p>

    <hr style="border:0; border-top:1px solid #ccc; margin:20px 0;">

    <h3 style="color:#222; margin-bottom:15px;">Στατιστικά Έσοδα/Εξοδα (Demo)</h3>
    <p style="font-size:12px; color:#777;">
        Σημείωση: Τα παρακάτω στοιχεία είναι ενδεικτικά. Στην τελική έκδοση θα αντικατασταθούν με πραγματικά δεδομένα από την ΕΛΣΤΑΤ.
    </p>

    <table style="width:100%; border-collapse:collapse; margin-top:10px;">
        <thead>
            <tr style="background-color:#f2f2f2; text-align:left; color:#333;">
                <th style="padding:10px; border:1px solid #ddd;">Μήνας/Έτος</th>
                <th style="padding:10px; border:1px solid #ddd;">Έσοδα (εκατ. €)</th>
                <th style="padding:10px; border:1px solid #ddd;">Έξοδα (εκατ. €)</th>
            </tr>
        </thead>
        <tbody>
            <tr><td style="padding:8px; border:1px solid #ddd;">Ιανουάριος 2022</td><td style="padding:8px; border:1px solid #ddd;">125</td><td style="padding:8px; border:1px solid #ddd;">100</td></tr>
            <tr><td style="padding:8px; border:1px solid #ddd;">Φεβρουάριος 2022</td><td style="padding:8px; border:1px solid #ddd;">130</td><td style="padding:8px; border:1px solid #ddd;">105</td></tr>
            <tr><td style="padding:8px; border:1px solid #ddd;">Μάρτιος 2022</td><td style="padding:8px; border:1px solid #ddd;">140</td><td style="padding:8px; border:1px solid #ddd;">110</td></tr>
            <tr><td style="padding:8px; border:1px solid #ddd;">Απρίλιος 2022</td><td style="padding:8px; border:1px solid #ddd;">135</td><td style="padding:8px; border:1px solid #ddd;">115</td></tr>
            <tr><td style="padding:8px; border:1px solid #ddd;">Μάιος 2022</td><td style="padding:8px; border:1px solid #ddd;">145</td><td style="padding:8px; border:1px solid #ddd;">120</td></tr>
            <tr><td style="padding:8px; border:1px solid #ddd;">Ιούνιος 2022</td><td style="padding:8px; border:1px solid #ddd;">150</td><td style="padding:8px; border:1px solid #ddd;">125</td></tr>
            <tr><td style="padding:8px; border:1px solid #ddd;">Ιούλιος 2022</td><td style="padding:8px; border:1px solid #ddd;">155</td><td style="padding:8px; border:1px solid #ddd;">130</td></tr>
            <tr><td style="padding:8px; border:1px solid #ddd;">Αύγουστος 2022</td><td style="padding:8px; border:1px solid #ddd;">150</td><td style="padding:8px; border:1px solid #ddd;">128</td></tr>
            <tr><td style="padding:8px; border:1px solid #ddd;">Σεπτέμβριος 2022</td><td style="padding:8px; border:1px solid #ddd;">160</td><td style="padding:8px; border:1px solid #ddd;">135</td></tr>
            <tr><td style="padding:8px; border:1px solid #ddd;">Οκτώβριος 2022</td><td style="padding:8px; border:1px solid #ddd;">165</td><td style="padding:8px; border:1px solid #ddd;">140</td></tr>
            <tr><td style="padding:8px; border:1px solid #ddd;">Νοέμβριος 2022</td><td style="padding:8px; border:1px solid #ddd;">170</td><td style="padding:8px; border:1px solid #ddd;">145</td></tr>
            <tr><td style="padding:8px; border:1px solid #ddd;">Δεκέμβριος 2022</td><td style="padding:8px; border:1px solid #ddd;">180</td><td style="padding:8px; border:1px solid #ddd;">155</td></tr>
        </tbody>
    </table>
</div>

<jsp:include page="footerPage.jsp" />
