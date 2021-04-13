<?php
require 'vendor/autoload.php';
include 'Configs.php';


use Parse\ParseUser;


// Require https, remove of comment if you are in insecure HTTP or in localhost
/*if ($_SERVER['HTTPS'] !== "on") {
    $url = "http://". $_SERVER['SERVER_NAME'] . $_SERVER['REQUEST_URI'];
    header("Location: $url"); php -S localhost:63342
    exit;
}*/

// Open login.php in case current user is logged out
$currUser = ParseUser::getCurrentUser();
if ($currUser && $currUser->get("role") === 'admin') {

     header('Refresh:0; url=dashboard/panel.php');

} else {
    header('Refresh:0; url=auth/login.php');
}

