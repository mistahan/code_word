<?php

	//This file will post a message to the server
	//intended for another client

    use \Firebase\JWT\JWT;
    define('SECRET_KEY','secret'); /// secret key can be a random string and keep in secret from everyone
    define('ALGORITHM','HS256');
    //use to connect to the database
	  require_once 'Dbconnect.php';
    require 'JWT.php';

    $jsonRecieved = strip_tags($_POST['authorization']);
    $token = $jsonRecieved;

    $secretKey = base64_decode(SECRET_KEY);
    $decoded_data_array = JWT::decode($token,$secretKey);
    $encodeArray = json_encode($decoded_data_array);


    $unencodedArray = ['information' => $decoded_data_array];
    $data = $unencodedArray['information']->data;

    $fromUser = $data->username;
	$message    = strip_tags($_POST['message']);
	$targetUser = strip_tags($_POST['targetUser']);

	$message    = $DBcon->real_escape_string($message);
    $targetUser = $DBcon->real_escape_string($targetUser);

    $check_from_user    = $DBcon->query("SELECT * FROM users WHERE username = '$fromUser'");
    $check_target_user  = $DBcon->query("SELECT * FROM users WHERE username ='$targetUser'");
    $count_target       = $check_target_user->num_rows;
    $count_from         = $check_from_user->num_rows;

    $count_total = $count_target + $count_from;


    if($count_total == 2 && $message != null && $fromUser != null && $targetUser !=null){
        $squery = "INSERT INTO messages(fromUser,message,targetUser) VALUES ('$fromUser', '$message','$targetUser')";
        if($DBcon->query($squery)) {


            $response['success'] = true;
            echo json_encode($response);


        } else{

            $response['success'] = false;
            echo json_encode($response);
        }

    }else{
            $response['success'] = false;
            echo json_encode($response);
    }

?>
