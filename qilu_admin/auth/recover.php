<?php
require '../vendor/autoload.php';
include '../Configs.php';

use Parse\ParseUser;
use Parse\ParseException;

// FORGOT PASSWORD -----------------------------------
if(isset($_GET['email'])) {
    $email = $_GET['email'];

    try {
        ParseUser::requestPasswordReset($email);

        echo '<script type="text/javascript">';
        echo 'setTimeout(function () { swal("Email sent!","Check your email, follow instructions to complete the recovery","sucess");';
        echo '}, 1000);</script>';

        // error
    } catch (ParseException $error) { $e = $error->getMessage();

        echo '<script type="text/javascript">';
        echo 'setTimeout(function () { swal("Error!","This email does not exist in our servers! Please double check and try again","error");';
        echo '}, 1000);</script>';
    }
}

?>
<!-- header -->

<head>
    <title>Qilu Dashboard - Recover</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!--===============================================================================================-->
    <link rel="icon" type="image/png" href="../assets/login/images/icons/favicon.ico"/>
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="../vendor/bootstrap/css/bootstrap.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="../assets/login/fonts/font-awesome-4.7.0/css/font-awesome.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="../vendor/animate/animate.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="../vendor/css-hamburgers/hamburgers.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="../vendor/select2/select2.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="../assets/login/css/util.css">
    <link rel="stylesheet" type="text/css" href="../assets/login/css/main.css">
    <!--===============================================================================================-->

    <link href="../assets/dashboard/css/sweetalert2.css" rel="stylesheet">
    <link href="../assets/dashboard/css/sweetalert.css" rel="stylesheet">

</head>

<body>

<div class="limiter">
    <div class="container-login100">
        <div class="wrap-login100">
            <div class="login100-pic js-tilt" data-tilt>
                <img src="../assets/login/images/img-01.png" alt="IMG">
            </div>

            <form class="login100-form validate-form">
					<span class="login100-form-title">Forgot your password? No problem</span>

                <div class="wrap-input100 validate-input" data-validate = "Seu email por favor">
                    <input class="input100" type="email" name="email" placeholder="Email">
                    <span class="focus-input100"></span>
                    <span class="symbol-input100"><i class="fa fa-envelope" aria-hidden="true"></i></span>
                </div>

                <div class="container-login100-form-btn">
                    <button class="login100-form-btn">Get new password</button>
                </div>

                <div class="text-center p-t-12">
						<span class="txt1">You Remember? </span>
                    <a class="txt2" href="login.php">Username or Password?</a>
                </div>

                <div class="text-center p-t-136">
                </div>
            </form>
        </div>
    </div>
</div>

<!--===============================================================================================-->
<script src="../vendor/jquery/jquery-3.2.1.min.js"></script>
<!--===============================================================================================-->
<script src="../vendor/bootstrap/js/popper.js"></script>
<script src="../vendor/bootstrap/js/bootstrap.min.js"></script>
<!--===============================================================================================-->
<script src="../vendor/select2/select2.min.js"></script>
<!--===============================================================================================-->
<script src="../vendor/tilt/tilt.jquery.min.js"></script>
<script >
    $('.js-tilt').tilt({
        scale: 1.1
    })
</script>
<!--===============================================================================================-->
<script src="../assets/login/js/main.js"></script>

<script src="../assets/dashboard/js/sweetalert2.js"></script>
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>

</body>
