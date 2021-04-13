<?php
/**
 * Created by PhpStorm.
 * User: Barker81
 * Date: 10/06/18
 * Time: 17:41
 */

use Parse\ParseServerInfo;

echo '

<!-- loading modal -->
<div id="loadingModal" class="modal" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <div class="text-center">
                    <h5><i class="fa fa-spin fa-spinner"></i><br><br>
                        <p id="loadingText"> Loading...</p>
                    </h5>
                    </div>
                </div><!-- end modal body -->
            </div>
        </div>
    </div><!-- ./ loading modal -->

';
?>

<head>

    <link href="../assets/dashboard/css/lib/toastr/toastr.min.css" rel="stylesheet">
    <!-- Bootstrap Core CSS -->
    <link href="../assets/dashboard/css/lib/bootstrap/bootstrap.min.css" rel="stylesheet">
    <!-- Custom CSS -->

    <link href="../assets/dashboard/css/lib/calendar2/semantic.ui.min.css" rel="stylesheet">
    <link href="../assets/dashboard/css/lib/calendar2/pignose.calendar.min.css" rel="stylesheet">
    <link href="../assets/dashboard/css/lib/owl.carousel.min.css" rel="stylesheet" />
    <link href="../assets/dashboard/css/lib/owl.theme.default.min.css" rel="stylesheet" />
    <link href="../assets/dashboard/css/helper.css" rel="stylesheet">
    <link href="../assets/dashboard/css/style.css" rel="stylesheet">
    <link href="../assets/dashboard/css/aliki.css" rel="stylesheet">
    <link href="../assets/dashboard/css/sweetalert2.css" rel="stylesheet">
    <link href="../assets/dashboard/css/sweetalert.css" rel="stylesheet">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:** -->
    <!--[if lt IE 9]>
    <script src="https:**oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https:**oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<!-- <script src="assets/js/jquery.js"></script> -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<!-- Lightbox -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/lightbox2/2.8.1/js/lightbox.min.js"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/lightbox2/2.8.1/css/lightbox.min.css" rel="stylesheet" />

<!-- All Jquery -->
<script src="../assets/dashboard/js/lib/jquery/jquery.min.js"></script>
<!-- Bootstrap tether Core JavaScript -->
<!-- New JS for poupup-->
<script src="../assets/dashboard/js/popper.min.js"></script>
<script src="../assets/dashboard/js/lib/bootstrap/js/popper.min.js"></script>
<script src="../assets/dashboard/js/lib/bootstrap/js/bootstrap.min.js"></script>
<!-- slimscrollbar scrollbar JavaScript -->
<script src="../assets/dashboard/js/jquery.slimscroll.js"></script>
<!--Menu sidebar -->
<script src="../assets/dashboard/js/sidebarmenu.js"></script>
<!--stickey kit -->
<script src="../assets/dashboard/js/lib/sticky-kit-master/dist/sticky-kit.min.js"></script>
<!--Custom JavaScript -->


<!-- Amchart -->
<script src="../assets/dashboard/js/lib/morris-chart/raphael-min.js"></script>
<script src="../assets/dashboard/js/lib/morris-chart/morris.js"></script>
<script src="../assets/dashboard/js/lib/morris-chart/dashboard1-init.js"></script>


<script src="../assets/dashboard/js/lib/calendar-2/moment.latest.min.js"></script>
<!-- scripit init-->
<script src="../assets/dashboard/js/lib/calendar-2/semantic.ui.min.js"></script>
<!-- scripit init-->
<script src="../assets/dashboard/js/lib/calendar-2/prism.min.js"></script>
<!-- scripit init-->
<script src="../assets/dashboard/js/lib/calendar-2/pignose.calendar.min.js"></script>
<!-- scripit init-->
<script src="../assets/dashboard/js/lib/calendar-2/pignose.init.js"></script>

<script src="../assets/dashboard/js/lib/owl-carousel/owl.carousel.min.js"></script>
<script src="../assets/dashboard/js/lib/owl-carousel/owl.carousel-init.js"></script>

<!-- scripit init-->

<script src="../assets/dashboard/js/scripts.js"></script>


<!--Datatables scriptes-->

<script src="../assets/dashboard/js/lib/datatables/datatables.min.js"></script>
<script src="../assets/dashboard/js/lib/datatables/cdn.datatables.net/buttons/1.2.2/js/dataTables.buttons.min.js"></script>
<script src="../assets/dashboard/js/lib/datatables/cdn.datatables.net/buttons/1.2.2/js/buttons.flash.min.js"></script>
<script src="../assets/dashboard/js/lib/datatables/cdnjs.cloudflare.com/ajax/libs/jszip/2.5.0/jszip.min.js"></script>
<script src="../assets/dashboard/js/lib/datatables/cdn.rawgit.com/bpampuch/pdfmake/0.1.18/build/pdfmake.min.js"></script>
<script src="../assets/dashboard/js/lib/datatables/cdn.rawgit.com/bpampuch/pdfmake/0.1.18/build/vfs_fonts.js"></script>
<script src="../assets/dashboard/js/lib/datatables/cdn.datatables.net/buttons/1.2.2/js/buttons.html5.min.js"></script>
<script src="../assets/dashboard/js/lib/datatables/cdn.datatables.net/buttons/1.2.2/js/buttons.print.min.js"></script>
<!--<script src="../assets/dashboard/js/lib/datatables/datatables-init.js"></script>-->
<script src="../assets/dashboard/js/custom/datatables-own-init.js"></script>

<!-- Form validation -->
<script src="../assets/dashboard/js/lib/form-validation/jquery.validate.min.js"></script>
<!--<script src="../assets/dashboard/js/lib/form-validation/jquery.validate-init.js"></script>-->
<script src="../assets/dashboard/js/custom/jquery.validate-own-init.js"></script>

<script src="../assets/dashboard/js/lib/sticky-kit-master/dist/sticky-kit.min.js"></script>

<script src="../assets/dashboard/js/lib/toastr/toastr.min.js"></script>
<!-- scripit init-->
<script src="../assets/dashboard/js/lib/toastr/toastr.init.js"></script>

<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>

<!-- javascript functions -->
<script>
    // LOGOUT -----------------------------------
    function logOut() {
        // Show Loading modal
        document.getElementById("loadingText").innerHTML = "Loging out...";
        $('#loadingModal').modal('show');

        $.ajax({
            url:"../auth/logout.php",

            success:function(data) {
                var results = data;
                console.debug(results);

                window.location.href = "../index.php";

                // error
            }, error: function () {
                alert('Error!. Try again!');
            }
        });
    }

    function logIn() {
        // Show Loading modal
        document.getElementById("loadingText").innerHTML = "Loging...";
        $('#loadingModal').modal('show');

        $.ajax({
            url:"../auth/logout.php",

            success:function(data) {
                var results = data;
                console.debug(results);

                window.location.href = "../index.php";

                // error
            }, error: function () {
                alert('Error!. Try again!');
            }
        });
    }

    // SHOW LOADING MODAL
    function showLoadingModal() {
        // Show loading modal
        document.getElementById("loadingText").innerHTML = "Please, wait...";
        $('#loadingModal').modal('show');
    }


    // SHOW LOADING MODAL (FOR EDIT PROFILE)
    function showLoadingForEditProfile() {
        // Show loading modal
        document.getElementById("loadingText").innerHTML = "Updating ...";
        $('#loadingModal').modal('show');


        setTimeout(function(){
            location.reload();
        }, 1000);
    }

    // SHOW LOADING MODAL (Add new School)
    function showLoadingForAddNewSchool() {
        // Show loading modal
        document.getElementById("loadingText").innerHTML = "Adding value ...";
        $('#loadingModal').modal('show');


        setTimeout(function(){
            location.reload();
        }, 3000);
    }

    function showToastTest() {
        toastr.success('This Is Success Message','Bottom Left',{
            "positionClass": "toast-bottom-left",
            timeOut: 5000,
            "closeButton": true,
            "debug": false,
            "newestOnTop": true,
            "progressBar": true,
            "preventDuplicates": true,
            "onclick": null,
            "showDuration": "300",
            "hideDuration": "1000",
            "extendedTimeOut": "1000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut",
            "tapToDismiss": false

        })
    }

</script>

<?php

$version = ParseServerInfo::getVersion();

echo '

<div class="footerFixed">
    <p> © 2020 All rights reserved. Developed with <i class="fa fa-heart"></i> by <a href="/" target="_blank">Barker81</a></p>
    <p> Parse Server v.'.$version.' / Admin Panel v.1.0.2</p>
</div>
 
 ';

?>







