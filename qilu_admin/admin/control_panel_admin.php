<?php

use Parse\ParseException;
use Parse\ParseQuery;
use Parse\ParseUser;

?>

<div class="page-wrapper">
    <!-- Bread crumb -->
    <div class="row page-titles">
        <div class="col-md-5 align-self-center">
            <h3 class="text-primary">Control panel</h3> </div>
        <div class="col-md-7 align-self-center">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="javascript:void(0)">Home</a></li>
                <li class="breadcrumb-item active">Control panel</li>
            </ol>
        </div>
    </div>


    <!-- Container fluid  -->
    <div class="container-fluid">
        <!-- Start Page Content -->


        <div class="row">
            <div class="col-md-3">
                <div class="card p-30">
                    <div class="media">
                        <div class="media-left meida media-middle">
                            <span><i class="fa fa-user-plus f-s-40 color-warning"></i></span>
                        </div>

                        <?php

                        try {
                        $query = new ParseQuery('_User');
                        $query->greaterThanOrEqualToRelativeTime('createdAt', '24 hrs ago');
                        $registedToday = $query->count();


                            echo '<div class="media-body media-text-right">
                            <h2>'.$registedToday.'</h2>
                            <p class="m-b-0">Registered today</p>
                        </div>';
                            // error in query
                        } catch (ParseException $e){ echo $e->getMessage(); } catch (Exception $e) {
                        }
                        ?>

                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card p-30">
                    <div class="media">
                        <div class="media-left meida media-middle">
                            <span><i class="fa fa-users f-s-40 color-success"></i></span>
                        </div>

                        <?php

                        $query = new ParseQuery("_User");
                        $count = $query->count();

                        echo '
                        
                        <div class="media-body media-text-right">
                            <h2>'.$count.'</h2>
                            <p class="m-b-0">Total Users</p>
                        </div>
                        
                        ';

                        ?>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card p-30">
                    <div class="media">
                        <div class="media-left meida media-middle">
                            <span><i class="fa fa-comments-o f-s-40 color-warning"></i></span>
                        </div>

                        <?php

                        $query = new ParseQuery("Message");
                        $count = $query->count();

                        echo '
                        
                        <div class="media-body media-text-right">
                            <h2>'.$count.'</h2>
                            <p class="m-b-0">Messages</p>
                        </div>
                        
                        ';

                        ?>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card p-30">
                    <div class="media">
                        <div class="media-left meida media-middle">
                            <span><i class="fa fa-video-camera f-s-40 color-danger"></i></span>
                        </div>

                        <?php

                        $query = new ParseQuery("Streaming");
                        //$query->equalTo("tipo", "aluno");
                        $count = $query->count();
                        // The count request succeeded. Show the count
                        //echo "Sean has played " . $count . " games";

                        echo '
                        
                        <div class="media-body media-text-right">
                            <h2>'.$count.'</h2>
                            <p class="m-b-0">Streamings</p>
                        </div>
                        
                        ';

                        ?>
                    </div>
                </div>
            </div>
        </div>

        <div class="row bg-white m-l-0 m-r-0 box-shadow ">

        </div>
        <div class="row">
            <div class="col-lg">
                <div class="card">
                    <div class="card-title">
                        <h4>Latest Users</h4>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table">
                                <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Username</th>
                                    <th>Avatar</th>
                                    <th>Gender</th>
                                    <th>Bithday</th>
                                    <th>Age</th>
                                    <th>Location</th>
                                    <th>Status</th>
                                </tr>
                                </thead>
                                <tbody>

                                <?php
                                try {

                                    $currUser = ParseUser::getCurrentUser();
                                    $cuObjectID = $currUser->getObjectId();

                                    $query = new ParseQuery("_User");
                                    $query->descending('createdAt');
                                    $query->limit(10);
                                    $query->notEqualTo('objectId', $cuObjectID);
                                    $catArray = $query->find(false);

                                    foreach ($catArray as $iValue) {

                                        $cObj = $iValue;

                                        $name = $cObj->get('name');
                                        $username = $cObj->get('username');

                                        if ($cObj->get("photos") !== null && sizeof($cObj->get("photos") > 0)) {

                                            $photos = $cObj->get('photos');

                                            $avatarPosition = $cObj->get('avatar_position');

                                            if ($avatarPosition !== null){
                                                $profilePhotoUrl = $photos[$avatarPosition]->getURL();
                                            } else {
                                                $profilePhotoUrl = $photos[0]->getURL();
                                            }

                                            $avatar = "<span/><a target='_blank' href=\"$profilePhotoUrl\" class=\"badge badge-info\">Download</a></span>";
                                        } else {
                                            $avatar = "<span/><a class=\"badge badge-warning\">No Avatar</a></span>";
                                        }

                                        $gender = $cObj->get('gender');

                                        if ($gender === "male"){
                                            $UserGender = "Male";
                                        } else if ($gender === "female"){
                                            $UserGender = "Female";
                                        } else {
                                            $UserGender = "Private";
                                        }


                                        $birthday= $cObj->get('birthday');
                                        $birthDate = date_format($birthday,"d/m/Y");

                                        $age = $cObj->get('age');


                                        $verified = $cObj->get('emailVerified');
                                        if ($verified == false){
                                            $verification = "<span class=\"badge badge-warning\">UNVERIFIED</span>";
                                        } else {
                                            $verification = "<span class=\"badge badge-success\">VERIFED</span>";
                                        }

                                        $locaton = $cObj->get('location');
                                        if ($locaton == null){
                                            $city_location = "<span class=\"badge badge-warning\">Unavailable</span>";
                                        } else{
                                            $city_location = "<span class=\"badge badge-info\">$locaton</span>";
                                        }

                                        echo '
		            	
		            	        <tr>
                                    <td>'.$name.'</td>
                                    <td><span>'.$username.'</span></td>
                                    <td>'.$avatar.'</td>
                                    <td><span>'.$UserGender.'</span></td>
                                    <td><span>'.$birthDate.'</span></td>
                                    <td>'.$age.'</td>
                                    <td>'.$city_location.'</td>
                                    <td>'.$verification.'</td>
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
