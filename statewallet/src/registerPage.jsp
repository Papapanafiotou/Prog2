<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage="errorPage.jsp" %>
<%@ include file="headerPage.jsp" %>

<div class="container" style="margin-top:50px; margin-bottom:50px; max-width:500px; margin-left:auto; margin-right:auto;">

    <!-- Εισαγωγικό κείμενο -->
    <p style="font-size:14px; color:#555; text-align:center; margin-bottom:20px;">
        Η παρούσα σελίδα επιτρέπει την εγγραφή νέου χρήστη στην εφαρμογή <strong>StateWallet</strong>.
        Κατά την εγγραφή μπορείτε να επιλέξετε το υπουργείο στο οποίο ανήκετε ή 
        να εγγραφείτε ως <strong>Πρωθυπουργός</strong> για να έχετε πλήρη πρόσβαση σε όλα τα δεδομένα.
    </p>

    <div style="background:#fff; padding:25px; border-radius:12px; box-shadow:0 0 15px rgba(0,0,0,0.1);">
        <h3 style="text-align:center; margin-bottom:20px;">Registration of the User</h3>
        <form action="registerControllerPage.jsp" method="post">

            <div class="form-group" style="margin-bottom:15px;">
                <label>Υπουργείο</label>
                <select name="ministry" required style="width:100%; padding:10px; border-radius:8px; border:1px solid #ccc;">
                    <option value="" disabled selected>-- Choose Ministry --</option>
                    <option>Πρωθυπουργός</option>
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


            <div class="form-group" style="margin-bottom:15px;">
                <label>Όνομα</label>
                <input type="text" name="name" placeholder="Enter your name" required 
                    style="width:100%; padding:10px; border-radius:8px; border:1px solid #ccc;">
            </div>

            <div class="form-group" style="margin-bottom:15px;">
                <label>Επώνυμο</label>
                <input type="text" name="surname" placeholder="Enter your Surname" required 
                    style="width:100%; padding:10px; border-radius:8px; border:1px solid #ccc;">
            </div>

            <div class="form-group" style="margin-bottom:15px;">
                <label>Email</label>
                <input type="email" name="email" placeholder="Enter your Email" required 
                    style="width:100%; padding:10px; border-radius:8px; border:1px solid #ccc;">
            </div>

            <div class="form-group" style="margin-bottom:15px;">
                <label>Username</label>
                <input type="text" name="username" placeholder="Enter your Username" required 
                    style="width:100%; padding:10px; border-radius:8px; border:1px solid #ccc;">
            </div>

            <div class="form-group" style="margin-bottom:20px;">
                <label>Κωδικός</label>
                <input type="password" name="password" placeholder="Enter your Password" required 
                    style="width:100%; padding:10px; border-radius:8px; border:1px solid #ccc;">
            </div>

            <div style="text-align:center;">
                <button type="submit" style="padding:10px 25px; border-radius:8px; border:none; background:#337ab7; color:white; margin-right:10px;">
                    Submit
                </button>
                <button type="reset" style="padding:10px 25px; border-radius:8px; border:none; background:#d9534f; color:white;">
                    Cancel
                </button>
            </div>

        </form>

        <!-- Disclaimer -->
        <p style="font-size:12px; color:#888; text-align:center; margin-top:20px;">
            Τα στοιχεία που καταχωρούνται στην εφαρμογή χρησιμοποιούνται μόνο για ακαδημαϊκούς σκοπούς
            και δεν αποτελούν επίσημο πληροφοριακό σύστημα δημοσίου φορέα.
        </p>
    </div>
</div>

<%@ include file="footerPage.jsp" %>
