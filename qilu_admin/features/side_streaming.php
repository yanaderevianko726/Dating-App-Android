<?php

require '../vendor/autoload.php';
include '../Configs.php';

use Parse\ParseException;
use Parse\ParseQuery;
use Parse\ParseUser;


session_start();

$currUser = ParseUser::getCurrentUser();
if ($currUser){

    // Store current user session token, to restore in case we create new user
    $_SESSION['token'] = $currUser -> getSessionToken();
} else {

    header("Refresh:0; url=../index.php");
}

?>

<div class="page-wrapper">
    <!-- Bread crumb -->
    <div class="row page-titles">
        <div class="col-md-5 align-self-center">
            <h3 class="text-primary">Live Streams</h3> </div>
        <div class="col-md-7 align-self-center">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="javascript:void(0)">Features</a></li>
                <li class="breadcrumb-item active">Live Streams</li>
            </ol>
        </div>
    </div>

    <!-- Container fluid  -->
    <div class="container-fluid">
        <!-- Start Page Content -->
        <div class="row bg-white m-l-0 m-r-0 box-shadow ">

        </div>
        <div class="row">
            <div class="col-lg">
                <div class="card">

                    <?php

                    $query = new ParseQuery("Streaming");
                    $streamCounter = $query->count(true);

                    echo ' <h2 class="card-title">'.$streamCounter.' Streams in total</h2> ';

                    ?>

                    <h5 class="card-subtitle">Copy or Export CSV, Excel, PDF and Print data</h5>
                    <div class="card-body">
                        <div class="table-responsive">
                            <!--<table class="table">-->
                            <table id="example23" class="display nowrap table table-hover table-striped table-bordered" cellspacing="0" width="100%">
                                <thead>
                                <tr>
                                    <th>ObjectId</th>
                                    <th>Date</th>
                                    <th>Streamer</th>
                                    <th>Gender</th>
                                    <th>Views</th>
                                    <th>Credits</th>
                                    <th>Duration</th>
                                    <th>Status</th>
                                </tr>
                                </thead>
                                <tbody>

                                <?php
                                try {

                                    $currUser = ParseUser::getCurrentUser();
                                    $cuObjectID = $currUser->getObjectId();

                                    $query = new ParseQuery("Streaming");
                                    $query->descending('createdAt');
                                    $query->includeKey("author");
                                    $catArray = $query->find(false);

                                    foreach ($catArray as $iValue) {
                                        // Get Parse Object
                                        $cObj = $iValue;

                                        $objectId = $cObj->getObjectId();
                                        $date= $cObj->getCreatedAt();
                                        $created = date_format($date,"d/m/Y");

                                        $name = $cObj->get('author')->get('name');

                                        $gender = $cObj->get('author')->get('gender');

                                        if ($gender === "male"){
                                            $UserGender = "Male";
                                        } else if ($gender === "female"){
                                            $UserGender = "Female";
                                        } else {
                                            $UserGender = "Private";
                                        }

                                        $views = count($cObj->get('viewers_id'));

                                        $credits = $cObj->get('streaming_tokens');
                                        $duration = $cObj->get('streaming_time');

                                        // pegar status
                                        $status = $cObj->get('streaming');
                                        if ($status == true){
                                            $status_mine = "<span class=\"badge badge-red\">LIVE NOW</span>";
                                        } else{
                                            $status_mine = "<span class=\"badge badge-success\">FINISHED</span>";
                                        }

                                        echo '
		            	
		            	        <tr>
                                    <td>'.$objectId.'</td>
                                    <td>'.$created.'</td>
                                    <td>'.$name.'</td>
                                    <td><span>'.$UserGender.'</span></td>
                                    <td><span>'.$views.'</span></td>
                                    <td>'.$credits.'</td>
                                    <td>'.$duration.'</td>
                                    <td>'.$status_mine.'</td>
                                </tr>
                                
                                ';
                                    }
                                    // error in query
                                } catch (ParseException $e){ echo $e->getMessage(); }
                                ?>

                                </tbody>
                            </table>
                        </div>
                    </div>



                </div>
            </div>
        </div>

        <!-- End PAge Content -->
    </div>
    <!-- End Container fluid  -->
    <!-- footer -->

    <!-- End footer -->
</div>
