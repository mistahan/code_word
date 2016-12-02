package zeec.codeword;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static android.R.id.message;
import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView welcome;
    private TextView messages;
    private Button sendMessage;
    private ProgressDialog progressDialog;
    private String response;
    private String jwt;
    private EditText editTargetUser;
    private EditText editMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        welcome = (TextView) findViewById(R.id.welcome);
        messages = (TextView) findViewById(R.id.messages);
        sendMessage = (Button) findViewById(R.id.sendMessage);
        editMessage = (EditText) findViewById(R.id.message);
        editTargetUser = (EditText) findViewById(R.id.targetUser);
        progressDialog = new ProgressDialog(this);



        Bundle bundle = getIntent().getExtras();
        response = bundle.getString("jwt");

        jwt = parseString(response);
        new RetrieveMessagesOperation().execute();

      //  messages.setText(response);
    }

    @Override
    public void onClick(View v) {

        if(v == sendMessage){
            new SendMessagesOperation().execute(editMessage.getText().toString(),editTargetUser.toString());
        }

    }
    public String parseString(String token){
        String namepass[] = token.split("jwt");
        String firstPart = namepass[0];
        Log.d("first part", firstPart);

        String secondPart [] = namepass[1].split("\"");

        return secondPart[2];

    }

    private class SendMessagesOperation extends AsyncTask<String, String, String> {




        @Override
        protected String doInBackground(String... params) {
            //Encode post data
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("https://www.codeword.tech/SendMessage.php");


            List<NameValuePair> nameValuePair = new ArrayList<>(3);
            nameValuePair.add(new BasicNameValuePair("authorization", jwt));
            nameValuePair.add(new BasicNameValuePair("message", params[0]));
            nameValuePair.add(new BasicNameValuePair("targetUser", params[1]));


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
              //  Toast.makeText(getApplicationContext(),"Incorrect Email/Password.",Toast.LENGTH_LONG).show();
            }
            if (response.contains("true")){
                messages.setText(response);
            }
        }
    }


    private class RetrieveMessagesOperation extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            //Encode post data
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("https://www.codeword.tech/GetMessage.php");


            try {
                String result ="";

                HttpResponse response = httpClient.execute(httpGet);
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

                Toast.makeText(getApplicationContext(),"Incorrect Email/Password.",Toast.LENGTH_LONG).show();
            }
            if (response.contains("true")){

                messages.setText(response);

            }
        }
    }
}
