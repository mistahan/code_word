package com.example.alexhan.codeword;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.security.Key;


public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView welcome;
    private Button sendMessage;
    private Button seeMessages;
    private Button addUser;

    private String response;
    private String email;
    private String jwt;
    private RSA rsaKeyPair;

    private Key friendPublicKey;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        welcome = (TextView) findViewById(R.id.welcome);
        sendMessage = (Button) findViewById(R.id.sendMessage);
        seeMessages = (Button) findViewById(R.id.seeMessages);
        addUser = (Button) findViewById(R.id.addUser);


        Bundle bundle = getIntent().getExtras();
        response = bundle.getString("jwt");
        email = bundle.getString("email");
        friendPublicKey =  (Key) bundle.get("friendKey");



        rsaKeyPair = new RSA(email, getApplicationContext());
        rsaKeyPair.run();


        jwt = parseString(response);



        sendMessage.setOnClickListener(onClickListener);
        seeMessages.setOnClickListener(onClickListener);
        addUser.setOnClickListener(onClickListener);




    }
    @Override
    public void onClick(View v) {

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            System.out.println(v.getId());

            switch (v.getId()) {

                case R.id.sendMessage:
                    // do your code
                    Intent intent = new Intent(ProfileActivity.this, SendMessageActivity.class);
                    intent.putExtra("jwt", jwt);
                    intent.putExtra("email", email);
                    intent.putExtra("friendKey",friendPublicKey);
                    startActivity(intent);
                    break;

                case R.id.seeMessages:
                    // do your code
                    Intent intent2 = new Intent(ProfileActivity.this,GetMessagesActivity.class);
                    //Intent intent2 = new Intent(ProfileActivity.this, AddUserActivity.class);
                    intent2.putExtra("jwt",jwt);
                    intent2.putExtra("email",email);
                    startActivity(intent2);
                    break;

                case R.id.addUser:
                    // do your code
                    System.out.println("addd+++++++++++++++++++++++++++++++++++++++++++++ user ");
                    Intent intent3 = new Intent(ProfileActivity.this, AddUserActivity.class);
                    intent3.putExtra("email",email);
                    startActivity(intent3);
                    break;

                default:
                    break;
            }
        }

    };
    public String parseString(String token){
        String namepass[] = token.split("jwt");
        String secondPart [] = namepass[1].split("\"");
        return secondPart[2];
    }
}
