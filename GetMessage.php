<?php
    require_once 'Dbconnect.php';
    require_once 'JWT.php';
    
   /* header("Access-Control-Allow-Origin: *");
    header("Access-Control-Allow-Headers: Authorization, Origin, X-Requested-With, Content-Type,      Accept");
    header("Content-Type: application/json");
    
    $headers = getallheaders();
    $jsonRecieved = $headers['Authorization'];
    $token = $jsonRecieved;
    
    var_dump($jsonRecieved);
    
    $secretKey = base64_decode(SECRET_KEY);
    $decoded_data_array = JWT::decode($token,$secretKey);
    $encodeArray = json_encode($decoded_data_array);
    $unencodedArray = ['information' => $decoded_data_array];*/
    
    
    $targetUser = strip_tags($_GET['targetUser']);
    $targetUser = $DBcon->real_escape_string($targetUser);
    
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
    }
    
    $DBcon->close();

    
?>
