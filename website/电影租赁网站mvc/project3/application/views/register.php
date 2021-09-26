

<nav class="navbar navbar-light bg-light">
    <a class="navbar-brand" href="#">
        <img src="images/Original.png" width="50" height="50" alt="">
    </a>
    <a href="<?php echo base_url(); ?>home" <button class="btn btn-outline-success my-2 my-sm-0">Home Page</button></a>
</nav>

<div id="signup_form" class="container bg-light w-50 mt-5 mb-5">
    <h3 class="mx-auto" style="width: 100px;">Sign up</h3>
    <form class="needs-validation" action="<?php echo base_url(); ?>register/sign_up" method="POST" novalidate>
        <div class="form-group">

            <?php

            if (strlen($status) > 0) {
                echo '<div class="p-1 rounded-lg mb-2 '.$status.' text-white">' . $message . '</div>';
            }
            ?>
            <label for="validationCustom01">Username</label>
            <input type="text" class="form-control" name="username" placeholder="Username" required>
            <div class="invalid-feedback">
                You need provide a username.
            </div>
        </div>
        <div class="form-group">
            <label for="email">Email address</label>
            <input type="email" class="form-control" name="email" placeholder="Email address" required>
            <div class="invalid-feedback">
                You need provide a valid email address.
            </div>
        </div>

        <div class="form-group">

            <label for="password">Password</label>
            <input type=password id="pwd" onkeyup="validatePassword(this.value)" class="form-control" name="password" pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$" placeholder="Password" required>
            <span id="msg"></span>

            <div class="invalid-feedback">
                You need provide a minimum of 8 characters and include at least one upper case letter, one lower case letter and one number.
            </div>
        </div>
        <div class="form-group">
            <p id="captImg"><?php echo $captchaImg; ?></p>
            <input type="text" class="form-control" name="captcha" placeholder="Captcha" required>
            <div class="invalid-feedback">
                You need provide a captcha
            </div>
        </div>
        <p class="check_register">Already registered?</p>
        <button type="button" onclick="window.location.href='<?php echo base_url(); ?>user'" class="login btn btn-link pb-2 pl-1">Sign in</button>

        <button class="btn btn-primary float-right mt-4" type="submit">Register</button>

    </form>

</div>