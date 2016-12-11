package com.example.alexhan.codeword;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class SendMessageActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTargetUser;
    private EditText editMessage;
    private Button sendMessage;
    private String jwt;
    private String email;
    private AES aes;
    private RSA rsa;
    private Button btn;
    private TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);


        editMessage = (EditText) findViewById(R.id.message);
        editTargetUser = (EditText) findViewById(R.id.targetUser);
        sendMessage = (Button) findViewById(R.id.sendMessage2);

        txtView = (TextView) findViewById(R.id.txtContent);


        Bundle bundle = getIntent().getExtras();
        jwt = bundle.getString("jwt");
        email = bundle.getString("email");
        sendMessage.setOnClickListener(this);

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(this);


        aes = new AES();
        rsa = new RSA(email);
    }

    public void runTask(){
            new SendMessagesOperation().execute(editMessage.getText().toString(),editTargetUser.getText().toString());

    }

    @Override
    public void onClick(View v) {
        if(v == sendMessage){
            runTask();
        }
        if(v==btn){
            ImageView myImageView = (ImageView) findViewById(R.id.imgview);
            Bitmap myBitmap = BitmapFactory.decodeResource(
                    getApplicationContext().getResources(),
                    R.drawable.puppy);
            myImageView.setImageBitmap(myBitmap);

            BarcodeDetector detector =
                    new BarcodeDetector.Builder(getApplicationContext())
                            .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                            .build();
            if(!detector.isOperational()){
                txtView.setText("Could not set up the detector!");
                return;
            }

            Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
            SparseArray<Barcode> barcodes = detector.detect(frame);

            Barcode thisCode = barcodes.valueAt(0);
            TextView txtView = (TextView) findViewById(R.id.txtContent);
            txtView.setText(thisCode.rawValue);
        }
    }

    private class SendMessagesOperation extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            //Encode post data
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("https://www.codeword.tech/SendMessage.php");

            aes.generateKey(email);
            byte[] encMessage = aes.encrypt(params[0].getBytes(),params[1]);
            String encryptedMessage = Base64.encodeToString(encMessage,Base64.DEFAULT);

            List<NameValuePair> nameValuePair = new ArrayList<>(3);
            nameValuePair.add(new BasicNameValuePair("authorization", jwt));
            nameValuePair.add(new BasicNameValuePair("message", encryptedMessage));
            nameValuePair.add(new BasicNameValuePair("targetUser", params[1]));


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

                Toast.makeText(getApplicationContext(),"Incorrect Email/Password.", Toast.LENGTH_LONG).show();

            }
            if (response.contains("true")){

                Toast.makeText(getApplicationContext(),"Message Sent", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
