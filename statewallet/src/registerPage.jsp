<%@ page contentType="text/html; charset=UTF-8" errorPage.jsp" %>
<%@ include file="headerPage.jsp" %>

<!-- Registration Form Section -->
<div class="container" style="margin-top:30px;">
    <div class="form-section">
        <h3>Registration Form</h3>
        <form action="registerControllerPage.jsp" method="post">

            <div class="alert-box">
                Please fill in the following form to create an account
            </div>

            <div class="form-group">
                <label>Name</label>
                <input type="text" class="form-control" name="name" placeholder="your name">
            </div>

            <div class="form-group">
                <label>Surname</label>
                <input type="text" class="form-control" name="Surname" placeholder="your surname">
            </div>

            <div class="form-group">
                <label>Email</label>
                <input type="email" class="form-control" name="email" placeholder="your email">
            </div>

            <div class="form-group">
                <label>Username</label>
                <input type="text" class="form-control" name="Username" placeholder="your username">
            </div> 

            <div class="form-group">
                <label>Password</label>
                <input type="password" class="form-control" name="Password" placeholder="your password">
            </div>

            <div class="form-group">
                <label>Confirm</label>
                <input type="password" class="form-control" name="Confirm" placeholder="confirm your password">
            </div>

            <div class="form-group">
                <label class="checkbox-inline">
                    <input type="checkbox" name="terms" value="yes"> I agree to the terms and conditions
                </label>
            </div>

            <div class="btn-container">
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
