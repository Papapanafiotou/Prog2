<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage="errorPage.jsp" %>
<%@ include file="headerPage.jsp" %>

<div class="container" style="margin-top:20px;">
    <div class="header">
        <h2 style="border-bottom: 1px solid #ddd; padding-bottom: 10px;">Ελληνική Δημοκρατία</h2>
    </div>
</div>

<div class="container" style="margin-top:20px; margin-bottom:50px;">
    <div class="form-section" style="background: #fff; padding: 20px; border-radius: 5px;">
        <h3>Registration</h3>
        <hr>
        
        <form action="registerControllerPage.jsp" method="post">

            <div class="form-group">
                <label>Υπουργεία</label>
                <select class="form-control" name="ministry" required>
                    <option value="" disabled selected>-- Please choose a ministry --</option>
                    <option>Υπουργείο Αγροτικής Ανάπτυξης και Τροφίμων</option>
                    <option>Υπουργείο Ανάπτυξης</option>
                    <option>Υπουργείο Δικαιοσύνης</option>
                    <option>Υπουργείο Εθνικής Άμυνας</option>
                    <option>Υπουργείο Εθνικής Οικονομίας και Οικονομικών</option>
                    <option>Υπουργείο Εσωτερικών</option>
                    <option>Υπουργείο Εξωτερικών</option>
                    <option>Υπουργείο Κλιματικής Κρίσης και Πολιτικής Προστασίας</option>
                    <option>Υπουργείο Κοινωνικής Συνοχής και Οικογένειας</option>
                    <option>Υπουργείο Μετανάστευσης και Ασύλου</option>
                    <option>Υπουργείο Ναυτιλίας και Νησιωτικής Πολιτικής</option>
                    <option>Υπουργείο Πολιτισμού και Αθλητισμού</option>
                    <option>Υπουργείο Προστασίας του Πολίτη</option>
                    <option>Υπουργείο Τουρισμού</option>
                    <option>Υπουργείο Υγείας</option>
                    <option>Υπουργείο Υποδομών και Μεταφορών</option>
                    <option>Υπουργείο Ψηφιακής Διακυβέρνησης</option>
                    <option>Υπουργείο Περιβάλλοντος και Ενέργειας</option>
                    <option>Υπουργείο Παιδείας, Θρησκευμάτων και Αθλητισμού</option>
                    <option>Υπουργείο Εργασίας και Κοινωνικής Ασφάλισης</option>
                </select>
            </div>

            <div class="form-group">
                <label for="id">ID</label>
                <input id="id" name="id" type="text" class="form-control" placeholder="8XXXXXX" required>
            </div>

            <div class="form-group">
                <label>Name</label>
                <input type="text" name="name" class="form-control" placeholder="Name" required>
            </div>

            <div class="form-group">
                <label>Surname</label>
                <input type="text" name="surname" class="form-control" placeholder="Surname" required>
            </div>

            <div class="form-group">
                <label>Email</label>
                <input type="email" name="email" class="form-control" placeholder="Email" required>
            </div>
            
            <div class="form-group">
                <label>Username</label>
                <input type="text" name="username" class="form-control" placeholder="your username" required>
            </div>

            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" class="form-control" placeholder="your password" required>
            </div>

            <div class="form-group" style="margin-top: 20px;">
                <button type="submit" class="btn btn-success">
                    <span class="glyphicon glyphicon-ok"></span> Submit
                </button>
                <button type="reset" class="btn btn-danger">
                    <span class="glyphicon glyphicon-remove"></span> Cancel
                </button>
            </div>
        </form>
    </div>
</div>

<%@ include file="footerPage.jsp" %>