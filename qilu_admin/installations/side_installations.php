<?php
/**
 * Created by PhpStorm.
 * User: maravilhosinga
 * Date: 13/06/18
 * Time: 01:26
 */

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
                            <span><i class="fa fa-tablet f-s-40 color-warning"></i></span>
                        </div>

                        <?php

                        try {
                        $query = new ParseQuery('_Installation');
                        $query->greaterThanOrEqualToRelativeTime('createdAt', '24 hrs ago');
                        $registedToday = $query->count(true);


                            echo '<div class="media-body media-text-right">
                            <h2>'.$registedToday.'</h2>
                            <p class="m-b-0">Installations today</p>
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
                            <span><i class="fa fa-tablet f-s-40 color-success"></i></span>
                        </div>

                        <?php

                        $query = new ParseQuery('_Installation');
                        $count = $query->count(true);

                        echo '
                        
                        <div class="media-body media-text-right">
                            <h2>'.$count.'</h2>
                            <p class="m-b-0">Total Installations</p>
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
                            <span><i class="fa fa-android f-s-40 color-warning"></i></span>
                        </div>

                        <?php

                        $query = new ParseQuery('_Installation');
                        $query->equalTo('deviceType', 'android');
                        $count = $query->count(true);
                        // The count request succeeded. Show the count
                        //echo "Sean has played " . $count . " games";

                        echo '
                        
                        <div class="media-body media-text-right">
                            <h2>'.$count.'</h2>
                            <p class="m-b-0">Android</p>
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
                            <span><i class="fa fa-apple f-s-40 color-danger"></i></span>
                        </div>

                        <?php

                        $query = new ParseQuery('_Installation');
                        $query->equalTo('deviceType', 'ios');
                        $count = $query->count(true);

                        echo '
                        
                        <div class="media-body media-text-right">
                            <h2>'.$count.'</h2>
                            <p class="m-b-0">iOS</p>
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
                        <h4>Latest Installations</h4>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table">
                                <thead>
                                <tr>
                                    <th>ObjectId</th>
                                    <th>Created</th>
                                    <th>Device</th>
                                    <th>Local</th>
                                    <th>Time Zone</th>
                                    <th>Push Type</th>
                                    <th>App Version</th>
                                </tr>
                                </thead>
                                <tbody>

                                <?php
                                try {


                                    $query = new ParseQuery('_Installation');
                                    $query->descending('createdAt');
                                    $query->includeKey('user');
                                    $catArray = $query->find(true);

                                    foreach ($catArray as $iValue) {
                                        // Get Parse Object

                                        $objectId = $iValue->getObjectId();

                                        $createdDate= $iValue->getCreatedAt();

                                        $createdAt = date_format($createdDate,"d/m/Y");

                                        $device = $iValue->get('deviceType');
                                        if ($device === 'android'){
                                            $deviceType = "<span class=\"badge badge-success\">Android</span>";
                                        } else if ($device === 'ios'){
                                            $deviceType = "<span class=\"badge badge-dark\">iOS</span>";
                                        } else {
                                            $deviceType = "<span class=\"badge badge-info\">$device</span>";
                                        }

                                        $local = $iValue->get('localeIdentifier');
                                        $timeZone = $iValue->get('timeZone');

                                        $pushType = $iValue->get('pushType');
                                        $appVersion = $iValue->get('appVersion');

                                        echo '
		            	
		            	        <tr>
                                    <td>'.$objectId.'</td>
                                    <td><span>'.$createdAt.'</span></td>
                                    <td><span>'.$deviceType.'</span></td>
                                    <td><span>'.$local.'</span></td>
                                    <td>'.$timeZone.'</td>
                                    <td>'.$pushType.'</td>
                                    <td>'.$appVersion.'</td>
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
