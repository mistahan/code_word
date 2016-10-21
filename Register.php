<?php
session_start();

require_once 'Dbconnect.php';

 $uname = strip_tags($_POST['username']);
 $email = strip_tags($_POST['email']);
 $upass = strip_tags($_POST['password']);
    
    
 $uname = $DBcon->real_escape_string($uname);
 $email = $DBcon->real_escape_string($email);
 $upass = $DBcon->real_escape_string($upass);
 
 $hashed_password = password_hash($upass, PASSWORD_BCRYPT);
 
    $check_email = $DBcon->query("SELECT email FROM users WHERE email='$email'");
    $count=$check_email->num_rows;
    
 if ($count==0) {
  
  $query = "INSERT INTO users(username,email,password) VALUES('$uname','$email','$hashed_password')";

  if ($DBcon->query($query)) {
   
  echo "{'status' : 'success','msg': 'Registration Success'}";
  
 } else {
  
  echo  "{'status' : 'error','msg':'Invalid email or passowrd'}";
   
 }
 }
 $DBcon->close();
 
 

?>
