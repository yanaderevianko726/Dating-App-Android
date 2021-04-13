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
            <h3 class="text-primary">Calls</h3> </div>
        <div class="col-md-7 align-self-center">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="javascript:void(0)">Features</a></li>
                <li class="breadcrumb-item active">Calls</li>
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

                    $query = new ParseQuery("Calls");
                    $messagesCounter = $query->count(true);

                    echo ' <h2 class="card-title">'.$messagesCounter.' Calls in total</h2> ';

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
                                    <th>From</th>
                                    <th>To</th>
                                    <th>Call</th>
                                    <th>Accepted</th>
                                    <th>End Reason</th>
                                    <th>Duration</th>
                                </tr>
                                </thead>
                                <tbody>

                                <?php
                                try {

                                    $currUser = ParseUser::getCurrentUser();
                                    $cuObjectID = $currUser->getObjectId();

                                    $query = new ParseQuery("Calls");
                                    $query->ascending('createdAt');
                                    $query->includeKey("fromUser");
                                    $query->includeKey("toUser");
                                    $catArray = $query->find(false);

                                    foreach ($catArray as $iValue) {
                                        // Get Parse Object
                                        $cObj = $iValue;

                                        $objectId = $cObj->getObjectId();
                                        $date= $cObj->getCreatedAt();
                                        $created = date_format($date,"d/m/Y");

                                        $fromName = $cObj->get('fromUser')->get('name');
                                        $toName = $cObj->get('toUser')->get('name');

                                        $calltypeBo = $cObj->get('isVoiceCall');
                                        if ($calltypeBo === true){
                                            $calltype = "<span class=\"badge badge-primary\">VOICE</span>";
                                        } else{
                                            $calltype = "<span class=\"badge badge-danger\">VIDEO</span>";
                                        }

                                        $call_status = $cObj->get('accepted');
                                        if ($call_status === true){
                                            $status_call = "<span class=\"badge badge-success\">YES</span>";
                                        } else{
                                            $status_call = "<span class=\"badge badge-red\">NO</span>";
                                        }

                                        $reason = $cObj->get('reason');

                                        $duration = $cObj->get('duration');

                                        echo '
		            	
		            	        <tr>
                                    <td>'.$objectId.'</td>
                                    <td>'.$created.'</td>
                                    <td>'.$fromName.'</td>
                                    <td>'.$toName.'</td>
                                    <td>'.$calltype.'</td>
                                    <td>'.$status_call.'</td>
                                    <td>'.$reason.'</td>
                                    <td>'.$duration.'</td>
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
