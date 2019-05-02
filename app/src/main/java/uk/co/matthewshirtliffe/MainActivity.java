package uk.co.matthewshirtliffe;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button button;

    Quote quote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.quoteButton);


        getQuote();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setEnabled(false);
                getQuote();
            }
        });

    }

    private void getQuote() {
        OkHttpClient client = new OkHttpClient();
        // GET request
        Request request = new Request.Builder()
                .url("https://api.kanye.rest")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                button.setEnabled(true);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Moshi moshi = new Moshi.Builder().build();
                JsonAdapter<Quote> jsonAdapter = moshi.adapter(Quote.class);
                try {
                    quote = jsonAdapter.fromJson(response.body().string());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateUI();
                            button.setEnabled(true);
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                    button.setEnabled(true);
                }

            }
        });
    }

    private void updateUI() {

        textView.setText(quote.quote);
    }



}
