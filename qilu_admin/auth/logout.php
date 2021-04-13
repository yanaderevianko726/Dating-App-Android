<?php
require '../autoload.php';
include '../Configs.php';

use Parse\ParseUser;
	
ParseUser::logOut();

$currUser = ParseUser::getCurrentUser();
if (!$currUser) {

    { header('Refresh:0; url=../index.php'); }

}