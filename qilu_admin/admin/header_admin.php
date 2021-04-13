<?php
/**
 * Created by PhpStorm.
 * User: barker81
 * Date: 10/06/18
 * Time: 15:21
 */

require '../vendor/autoload.php';
include '../Configs.php';

use Parse\ParseUser;

$currUser = ParseUser::getCurrentUser();

// Get avatar

try {
    if (sizeof($currUser->get("photos") > 0)) {

        $photos = $currUser->get('photos');
        $avatarPosition = $currUser->get('avatar_position');

        if ($avatarPosition !== null){
            $avatarURL = $photos[$avatarPosition]->getURL();
        } else {
            $avatarURL = $photos[0]->getURL();
        }

    } else {
        $avatarURL = "../assets/dashboard/images/avatar_blank.png";
    }
} catch (Exception $e) {
}

?>

<?php

echo '
<div class="header">
    <nav class="navbar top-navbar navbar-expand-md navbar-light">
        <!-- Logo -->
        <div class="navbar-header">
            <a class="navbar-brand" href="../dashboard/panel.php">
                <!-- Logo icon -->
                <b><img src="../assets/dashboard/images/logo.png" alt="homepage" class="dark-logo" width="40" /></b>
                <!--End Logo icon -->
                <!-- Logo text -->
                <span><img src="../assets/dashboard/images/logo-text.png" alt="homepage" class="dark-logo" width="140" /></span>
            </a>
        </div>
        <!-- End Logo -->
        <div class="navbar-collapse">
            <!-- toggle and nav items -->
            <ul class="navbar-nav mr-auto mt-md-0">
                <!-- This is  -->
                <li class="nav-item"> <a class="nav-link nav-toggler hidden-md-up text-muted  " href="javascript:void(0)"><i class="mdi mdi-menu"></i></a> </li>
                <li class="nav-item m-l-10"> <a class="nav-link sidebartoggler hidden-sm-down text-muted  " href="javascript:void(0)"><i class="ti-menu"></i></a> </li>
            </ul>
            <!-- User profile and search -->
            <ul class="navbar-nav my-lg-0">

                <!-- Search -->
                <li class="nav-item hidden-sm-down search-box"> <a class="nav-link hidden-sm-down text-muted  " href="javascript:void(0)"><i class="ti-search"></i></a>
                    <form class="app-search">
                        <input type="text" class="form-control" placeholder="Search here"> <a class="srh-btn"><i class="ti-close"></i></a> </form>
                </li>

                <!-- End Messages -->
                <!-- Profile -->
                
                <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle text-muted  " href="#" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><img src="' .$avatarURL.'" alt="user" class="profile-pic" /></a>
                 
                    <div class="dropdown-menu dropdown-menu-right animated zoomIn">
                        <ul class="dropdown-user">
                           
                            <li><a href="#" onclick="logOut()"><i class="fa fa-power-off"></i> Logout</a></li>
                       
                        </ul>
                    </div>
                </li>
            </ul>
        </div>
    </nav>
</div>

';

?>
