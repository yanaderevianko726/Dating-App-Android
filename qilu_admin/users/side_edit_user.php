<?php

require '../vendor/autoload.php';
include '../Configs.php';

use Parse\ParseQuery;
use Parse\ParseUser;
use Parse\ParseException;

session_start();

$currUser = ParseUser::getCurrentUser();
if ($currUser){

    // Store current user session token, to restore in case we create new user
    $_SESSION['token'] = $currUser -> getSessionToken();
} else {

    header("Refresh:0; url=../index.php");
}

// Update data ------------------------------------------------
if(isset($_POST['username']) && isset($_POST['fullname'])){
    $username = $_POST['username'];
    $fullName = $_POST['fullname'];
    $gender = $_POST['gender'];
    $birthday = $_POST['birthday'];
    $status = $_POST['status'];
    $account = $_POST['account'];


    $birthdayDate = new DateTime($birthday);

    // 4 days ago
    //$date = DateTime::createFromFormat('m/d/Y H:i:s', date("m/d/Y H:i:s"));

    $adObjID = $_GET['objectId'];

    $query = new ParseQuery("_User");
    try {
        $user = $query->get($adObjID, true);
        // The object was retrieved successfully.
    } catch (ParseException $ex) {
        // The object was not retrieved successfully.
        // error is a ParseException with an error code and message.
    }

    try {
        $user->set("username", $username);
        $user->set("name", $fullName);
        $user->set("gender", $gender);
        $user->set("birthday", $birthdayDate);
        $user->set("emailVerified", filter_var($status, FILTER_VALIDATE_BOOLEAN));
        $user->set("activationStatus", filter_var($account, FILTER_VALIDATE_BOOLEAN));

        /*if ($status === "true"){


        } else {

            $user->set("emailVerified", false);
        }

        if ($account === "true"){

            $user->set("activationStatus", true);
        } else {

            $user->set("activationStatus", false);
        }*/

    } catch (Exception $e) {
    }


    $user->save(true);

}

?>

<?php
// Get current User
$adObjID = $_GET['objectId'];

$query = new ParseQuery("_User");
try {
    $currUser = $query->get($adObjID, true);
    // The object was retrieved successfully.
} catch (ParseException $ex) {
    // The object was not retrieved successfully.
    // error is a ParseException with an error code and message.
}

//$currUser = ParseUser::getCurrentUser();
$cuObjectID = $currUser->getObjectId();

//Get username
$username = $currUser->getUsername();

// Nome completo
$name = $currUser->get('name');

if ($name != null){

    $fullName = $name;
} else{

    $fullName = "N/A";
}

// genero
$genre= $currUser->get('gender');

if ($genre != null){

    $genero = $genre;

    if ($genero == "male"){

        $gen1 = "Male";
        $genT1 = "male";

        $gen2 = "Female";
        $genT2 = "female";

    } elseif ($genero == "female"){

        $gen1 = "Female";
        $genT1 = "female";

        $gen2 = "Male";
        $genT2 = "male";
    }

} else {

    $genero = "N/A";

    $gen1 = "Male";
    $genT1 = "male";

    $gen2 = "Female";
    $genT2 = "female";
}



// Data de nascimento
$aniv = $currUser->get("birthday");


if ($aniv != null){

    $anivDate = date_format($aniv,"Y-m-d");

} else{

    $anivDate = "N/A";
}

// genero
$accountStatusDisabled= $currUser->get('activationStatus');

if ($accountStatusDisabled == true){

    $status1 = "Suspended";
    $statusDisabled1 = "true";

    $status2 = "Enabled";
    $statusDisabled2 = "false";

} else {

    $status1 = "Enabled";
    $statusDisabled1 = "false";

    $status2 = "Suspended";
    $statusDisabled2 = "true";

}

// status de processo

// genero
$emailVerified= $currUser->get('emailVerified');

if ($emailVerified == true){

    $email1 = "Verified";
    $emailVeri1 = "true";

    $email2 = "Unverified";
    $emailVeri2 = "false";

} else {

    $email1 = "Unverified";
    $emailVeri1 = "false";

    $email2 = "Verified";
    $emailVeri2 = "true";

}

$_SESSION['name']   = $fullName;
$_SESSION['username']     = $username;
$_SESSION['gender'] = $gender;
$_SESSION['birthday']     = $birthday;
$_SESSION['emailVerified']     = $status;
$_SESSION['activationStatus']     = $account;

?>

<div class="page-wrapper">
    <!-- Bread crumb -->
    <div class="row page-titles">
        <div class="col-md-5 align-self-center">
            <h3 class="text-primary">All Users </h3> </div>
        <div class="col-md-7 align-self-center">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="javascript:void(0)">Users</a></li>
                <li class="breadcrumb-item active">Edit user </li>
            </ol>
        </div>
    </div>

    <?php

    echo '
    
    <!-- Container fluid  -->
    <div class="container-fluid">
        <!-- Start Page Content -->
        <div class="row bg-white m-l-0 m-r-0 box-shadow ">

        </div>
        <div class="row">
            <div class="col-lg">
                <div class="card">
                    <div class="card-body">
                        <div class="form-validation">
                        <form class="form-valide" action="" method="post">
                        <div class="form-group row">
                            <label for="fullname" class="col-sm-2 col-form-label">Fullname <span class="text-danger">*</span> </label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="fullname" name="fullname" value="'.$name.'" placeholder="Por favor coloque o nome completo">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="username" class="col-sm-2 col-form-label">Username <span class="text-danger">*</span> </label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="username" name="username" value="'.$username.'" placeholder="Por favor coloque o nome de utilizador">
                            </div>
                        </div>
                        
                            <div class="form-group row">
                                <label class="col-lg-2 col-form-label" for="gender">Gender <span class="text-danger">*</span></label>
                                <div class="col-sm-10">
                                    <select class="form-control" id="gender" name="gender">
                                        <option value="'.$genT1.'">'.$gen1.'</option>
                                        <option value="'.$genT2.'">'.$gen2.'</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-lg-2 col-form-label" for="birthday">Birthday <span class="text-danger">*</span></label>
                                <div class="col-sm-10">
                                    <input type="date" class="form-control" id="aniversario" value="'.$anivDate.'" name="birthday" placeholder="dd/mm/yyyy">
                                </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-lg-2 col-form-label" for="status">Status <span class="text-danger">*</span></label>
                                <div class="col-sm-10">
                                    <select class="form-control" id="status" name="status">
                                        <option value="'.$emailVeri1.'">'.$email1.'</option>
                                        <option value="'.$emailVeri2.'">'.$email2.'</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-lg-2 col-form-label" for="account">Account Status <span class="text-danger">*</span></label>
                                <div class="col-sm-10">
                                    <select class="form-control" id="account" name="account">
                                         <option value="'.$statusDisabled1.'">'.$status1.'</option>
                                         <option value="'.$statusDisabled2.'">'.$status2.'</option>
                                    </select>
                                </div>
                            </div>
                      
                        <div class="form-group row">
                            <div class="col-sm-10">
                                <button type="submit" class="btn btn-info"> Save </button>
                                <a class="btn btn-inverse" href="../dashboard/all_users.php"> Back </a>

                            </div>
                        </div>
                    </form>
                </div>

            </div>
                </div>
        </div>
        </div>
        <!-- End PAge Content -->
    </div>
    <!-- End Container fluid  -->
    
    ';
    ?>


</div>