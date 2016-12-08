package zeec.codeword;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView welcome;
    private Button sendMessage;
    private Button seeMessages;

    private String response;
    private String email;
    private String jwt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        welcome = (TextView) findViewById(R.id.welcome);
        sendMessage = (Button) findViewById(R.id.sendMessage);
        seeMessages = (Button) findViewById(R.id.seeMessages);


        Bundle bundle = getIntent().getExtras();
        response = bundle.getString("jwt");
        email = bundle.getString("email");
        jwt = parseString(response);

        sendMessage.setOnClickListener(this);
        seeMessages.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {


        if(v == sendMessage){
            Intent intent = new Intent(ProfileActivity.this,SendMessageActivity.class);
            intent.putExtra("jwt",jwt);
            intent.putExtra("email",email);
            startActivity(intent);
        }
        if(v == seeMessages){

            Intent intent = new Intent(ProfileActivity.this,GetMessagesActivity.class);
            intent.putExtra("jwt",jwt);
            intent.putExtra("email",email);
            startActivity(intent);
        }

    }

    public String parseString(String token){
        String namepass[] = token.split("jwt");
        String secondPart [] = namepass[1].split("\"");
        return secondPart[2];
    }
}
