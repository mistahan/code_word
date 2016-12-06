<?php

    use \Firebase\JWT\JWT;
    define('SECRET_KEY','secret'); /// secret key can be a random string and keep in secret from everyone
    define('ALGORITHM','HS256');
    require_once 'Dbconnect.php';
    require 'JWT.php';

    header("Access-Control-Allow-Origin: *");
    header("Access-Control-Allow-Headers: Authorization, Origin, X-Requested-With, Content-Type,      Accept");
    header("Content-Type: application/json");

    $headers = getallheaders();
    $jsonRecieved = $headers['Authorization'];
    $pieces = explode(" ", $jsonRecieved);
    $token = $pieces[2];
    $secretK = base64_decode(SECRET_KEY);


    $decoded_data_array = JWT::decode($token,$secretK);
    $encodeArray = json_encode($decoded_data_array);
    $unencodedArray = ['information' => $decoded_data_array];
    $data = $unencodedArray['information']->data;

    $user = $data->username;

    $messages = $DBcon->query("SELECT * FROM messages WHERE targetUser='$user' ORDER BY MessageTime DESC");

    while ($row=mysqli_fetch_row($messages)) {
      $space_separated = implode(" ", $row);
      echo "$space_separated\n";
    }
    $DBcon->close();


?>
