package com.example.alexhan.codeword;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {


    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextUsername;
    private TextView textViewSignin;
    private String username;
    private String email;
    private String password;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        progressDialog = new ProgressDialog(this);

        buttonRegister = (Button) findViewById(R.id.buttonSignup);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);

        textViewSignin = (TextView) findViewById(R.id.textViewSignin);


        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
    }

    private void registerUser() {
        username = editTextUsername.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            //email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }


        if (!validateNewPass(password)) {
            //email is empty
            Toast.makeText(this, "Please enter proper password", Toast.LENGTH_SHORT).show();
            return;
        }
        //validation ok


        new RegisterOperation().execute();



    }

    @Override
    public void onClick(View view) {
        if (view == buttonRegister) {
            registerUser();
        }
        if (view == textViewSignin) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));

        }
    }


    public boolean validateNewPass(String pass1) {

        final Pattern hasUppercase = Pattern.compile("[A-Z]");
        final Pattern hasLowercase = Pattern.compile("[a-z]");
        final Pattern hasNumber = Pattern.compile("\\d");
        final Pattern hasSpecialChar = Pattern.compile("[^a-zA-Z0-9 ]");

        if (pass1 == null) {
            Toast.makeText(this, "Please enter proper password", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (pass1.isEmpty()) {
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (pass1.length() < 6) {
            Toast.makeText(this, "Password needs at least 7 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!hasLowercase.matcher(pass1).find()) {
            Toast.makeText(this, "Password needs a lowercase", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!hasNumber.matcher(pass1).find()) {
            Toast.makeText(this, "Password needs a number ", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private class RegisterOperation extends AsyncTask<String, String, String> {




        @Override
        protected String doInBackground(String... params) {
            //Encode post data
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("https://www.codeword.tech/Register.php");


            List<NameValuePair> nameValuePair = new ArrayList<>(3);
            nameValuePair.add(new BasicNameValuePair("username", username));
            nameValuePair.add(new BasicNameValuePair("email", email));
            nameValuePair.add(new BasicNameValuePair("password", password));



            try {
                String result ="";


                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair,"UTF-8"));
                HttpResponse response = httpClient.execute(httpPost);

                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        response.getEntity().getContent()));
                String line="";
                while((line = rd.readLine()) != null){
                        result = result + line;
                }
                return result;

            } catch (UnsupportedEncodingException e) {
                //do something here
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
        @Override
        protected void onPostExecute(String response) {



            progressDialog = new ProgressDialog(getApplicationContext());


            if (response.contains("false")){

                Log.d("If statement result is:", "false");

                Toast.makeText(getApplicationContext(),"Incorrect Email/Password.",Toast.LENGTH_LONG).show();
            }
            if (response.contains("true")){

                Log.d("If statement result is:", "true");

                super.onPostExecute(response);
                Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);


            }
        }
    }
}
