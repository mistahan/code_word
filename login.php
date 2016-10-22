<?php
    use \Firebase\JWT\JWT;
    require_once 'Dbconnect.php';

    require_once 'JWT.php';
    define('SECRET_KEY','secret'); /// secret key can be a random string and keep in secret from everyone
    define('ALGORITHM','HS512');
    
    $email = strip_tags($_POST['email']);
    $upass = strip_tags($_POST['password']);
    
    
    $email = $DBcon->real_escape_string($email);
    $upass = $DBcon->real_escape_string($upass);
    
    
    $check_email = $DBcon->query("SELECT * FROM users WHERE email='$email'");
    $count=$check_email->num_rows;
    while ($row=mysqli_fetch_row($check_email))
    {
        
        //print the columns of the row
        printf ("%s %s %s %s\n",$row[0],$row[1],$row[2],$row[3]);
        
        
        if ($count==1) {
            if(password_verify($upass,$row[3])){
                $tokenId    = base64_encode(mcrypt_create_iv(32));
                $issuedAt   = time();
                $notBefore  = $issuedAt + 10;  //Adding 10 seconds
                $expire     = $notBefore + 60; // Adding 60 seconds
                $serverName = 'https://www.codeword.tech'; /// set your domain name
                
               

                /*
                 * Create the token as an array
                 */
                $data = [
                'iat'  => $issuedAt,         // Issued at: time when the token was generated
                'jti'  => $tokenId,          // Json Token Id: an unique identifier for the token
                'iss'  => $serverName,       // Issuer
                'nbf'  => $notBefore,        // Not before
                'exp'  => $expire,           // Expire
                'data' => [                  // Data related to the logged user you can set your required data
                    'user_id'  => $row[0],     // id from the users table
                    'username' => $row[1],   //  name
                        ]
                
                ];
                
                $secretKey = base64_decode(SECRET_KEY);
                /// Here we will transform this array into JWT:
                $jwt = JWT::encode($data,$secretKey);
                $unencodedArray = ['jwt' => $jwt];
                echo "{'status' : 'success','resp':".json_encode($unencodedArray)."}";
            }else{
                 echo  "{'status' : 'error','msg':'Invalid email or passowrd'}";
            }
        }
    }
    
    $DBcon->close();
    
    ?>
