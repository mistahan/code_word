package zeec.codeword;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import org.apache.http.client.HttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import java.io.BufferedReader;
import java.io.IOException;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "j0uAeh292R3YTZce04WpZVv9H";
    private static final String TWITTER_SECRET = "z76taDKlx0HxGEJYysvBQAE2Kv1ipmzEP9Dshvzp1EXM81vEX4";



    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignUp;
    private CheckBox checkBoxRememberMe;
    private SharedPreferences loginPrefrences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private ProgressDialog progressDialog;
    private String email;
    private String password;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignin);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);

        loginPrefrences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPrefrences.edit();
        saveLogin = loginPrefrences.getBoolean("saveLogin",false);
        if(saveLogin == true){
            editTextEmail.setText(loginPrefrences.getString("username",""));
            editTextPassword.setText(loginPrefrences.getString("password", ""));
            checkBoxRememberMe.setChecked(true);
        }

        buttonSignIn.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);



    }

    private void userLogin(){
        email = editTextEmail.getText().toString();
        editTextEmail.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
        inputManager.restartInput(editTextEmail);

        password = editTextPassword.getText().toString();
        editTextPassword.requestFocus();
        inputManager.restartInput(editTextPassword);

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            //email is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        //validation ok
       // InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
       // imm.hideSoftInputFromWindow(editTextEmail.getWindowToken(), 0);

        progressDialog.setMessage("Logging In...");
        progressDialog.show();



        new LoginOperation().execute();

        progressDialog.dismiss();

    }
    @Override
    public void onClick(View view){
        if(view == buttonSignIn){
            userLogin();
        }
        if(view == textViewSignUp){
            finish();
            startActivity(new Intent(this, RegistrationActivity.class));
        }
    }
    private class LoginOperation extends AsyncTask<String, String, String> {




        @Override
        protected String doInBackground(String... params) {
            //Encode post data
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("https://www.codeword.tech/login.php");


            List<NameValuePair> nameValuePair = new ArrayList<>(2);
            nameValuePair.add(new BasicNameValuePair("email", email));
            nameValuePair.add(new BasicNameValuePair("password", password));



            try {
                String result ="";


                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                HttpResponse response = httpClient.execute(httpPost);

                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        response.getEntity().getContent()));
                String line="";
                while((line = rd.readLine()) != null){
                    result = result + line;
                }
                rd.close();
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




            if (response.contains("false")){

                Toast.makeText(getApplicationContext(),"Incorrect Email/Password.",Toast.LENGTH_LONG).show();
            }
            if (response.contains("true")){


                super.onPostExecute(response);
                Intent intent = new Intent(LoginActivity.this,ProfileActivity.class);
                intent.putExtra("jwt",response);
                intent.putExtra("email", email);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                progressDialog.show();
                getApplicationContext().startActivity(intent);
                finish();
                progressDialog.dismiss();

            }
        }
    }
}