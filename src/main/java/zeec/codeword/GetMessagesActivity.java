package zeec.codeword;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import static zeec.codeword.R.id.messagesView;

public class GetMessagesActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button refresh;

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
        refresh.setOnClickListener(this);

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
                String result ="";
                httpGet.addHeader("Authorization","Bearer "+token);
                HttpResponse response = httpClient.execute(httpGet);
                Log.d("response code", response.getStatusLine().toString());

                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        response.getEntity().getContent()));

                String line="";
                int counter =1;
                ArrayList<String> messageInfo = new ArrayList<String>();
                while((line = rd.readLine()) != null){
                    messageInfo.add(line);

                    if(counter == 4){
                        Messages m = new Messages(messageInfo.get(0),messageInfo.get(1),messageInfo.get(2),messageInfo.get(3));
                        messages.add(m);
                        messageInfo.clear();
                        counter = 1;
                    }else{
                        counter++;
                    }
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
            MessageCardAdapter thisOne = new MessageCardAdapter(getApplicationContext(),messages);
            mAdapter = thisOne;
            mRecyclerView.setAdapter(mAdapter);

        }
    }
}
