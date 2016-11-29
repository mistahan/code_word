<?php
    
    use \Firebase\JWT\JWT;
    define('SECRET_KEY','MFswDQYJKoZIhvcNAQEBBQADSgAwRwJARQqcj0UOTTzLoIGv2ljJVrvcz8CeAc/cUgFqo5gXwOo+2VHGvxz35f06GeyL4dYjTmuxquTrfikHNn+5Xc8brwIDAQAB'); /// secret key can be a random string and keep in secret from everyone
    define('ALGORITHM','HS256');
    require_once 'Dbconnect.php';
    require 'JWT.php';
    
    header("Access-Control-Allow-Origin: *");
    header("Access-Control-Allow-Headers: Authorization, Origin, X-Requested-With, Content-Type,      Accept");
    header("Content-Type: application/json");
    
    $headers = getallheaders();
    $jsonRecieved = $headers['Authorization'];
    $pieces = explode(" ", $jsonRecieved);
    $token = $pieces[1];
    
    $secretK = base64_decode(SECRET_KEY);
    $decoded_data_array = JWT::decode($token,$secretK,false);
    
    $encodeArray = json_encode($decoded_data_array);
    echo "data checked";
    
    
    
    echo $encodeArray;
    
   // $unencodedArray = ['information' => $decoded_data_array];
    
    
    /*
    
    $check_target_user  = $DBcon->query("SELECT * FROM users WHERE username='$targetUser'");
    $messages           = $DBcon->query("SELECT * FROM messages WHERE targetUser='$targetUser' ORDER BY MessageTime ASC");
    
    $count=$check_target_user->num_rows;
    
    if($count == 0){
        echo "{'status' : 'error','msg':'User does not exist'}";
    }else{
        
        while ($row=mysqli_fetch_row($messages))
        {
            printf ("%s %s %s %s\n",$row[0],$row[1],$row[2],$row[3]);
        
        }
    }*/
    
    $DBcon->close();

    
?>
