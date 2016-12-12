package com.example.alexhan.codeword;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.spongycastle.util.encoders.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static com.example.alexhan.codeword.R.id.messagesView;

public class GetMessagesActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button refresh;
    private HttpEntity entity;
    private AES aes;
    private RSA rsa;
    private String username;

    String token;

    ArrayList<Messages> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_messages);

        mRecyclerView = (RecyclerView) findViewById(messagesView);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        refresh = (Button) findViewById(R.id.refresh);

        Bundle bundle = getIntent().getExtras();
        token = bundle.getString("jwt");
        username = bundle.getString("email");
        refresh.setOnClickListener(this);
        aes = new AES();
        rsa = new RSA(username,getApplication());


    }

    @Override
    public void onClick(View v) {

        if(v == refresh){

            new RetrieveMessagesOperation().execute();

        }
    }

    private class RetrieveMessagesOperation extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            //Encode post data
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("https://www.codeword.tech/GetMessage.php");

            try {
                String result = "";
                String jsonString = "";
                httpGet.addHeader("Authorization", "Bearer " + token);
                HttpResponse response = httpClient.execute(httpGet);
                Log.d("response code", response.getStatusLine().toString());

                entity = response.getEntity();
                if (entity != null) {
                    jsonString = new BasicResponseHandler().handleResponse(response);
                    //System.out.println(jsonString);
                } else {
                    System.out.println("was null");
                }

                JSONObject obj = new JSONObject(jsonString);
                    byte[] encryptedMessage = Base64.decode(obj.getString("message"));
                    byte[] decryptedMessage = aes.decrypt(encryptedMessage, obj.getString("sender"));
                    String message = new String(decryptedMessage);
                    String sender = obj.getString("sender");
                    String to = obj.getString("to");
                    String time = obj.getString("date");
                    System.out.println(sender + " said: " + message + " at " + time);

                    Messages m = new Messages(sender, message, to, time);
                    messages.add(m);
                    return result;

            } catch (UnsupportedEncodingException e) {
                //do something here
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            MessageCardAdapter thisOne = new MessageCardAdapter(getApplicationContext(),messages);
            mAdapter = thisOne;
            mRecyclerView.setAdapter(mAdapter);

        }
    }
}
