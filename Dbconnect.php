<?php

	$DBhost = "localhost";
	$DBuser = "root";
  	$DBpass = "";
 	$DBname = "SeniorProject";
  
   	$DBcon = new mysqli($DBhost,$DBuser,$DBpass,$DBname);
    
    if ($DBcon->connect_error) {
        die("ERROR : -> ".$DBcon->connect_error);
    }
?>
