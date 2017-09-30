package net.itgoo.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.itgoo.countdowntextview.CountDownTextView;

public class MainActivity extends AppCompatActivity implements CountDownTextView.OnCountDownListener {

    private CountDownTextView countDownTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countDownTextView = (CountDownTextView) findViewById(R.id.tv);
        countDownTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTextView.start();
            }
        });
    }

    @Override
    public boolean onCountDownCustomFormat(long minute, long second) {
        return false;
    }

    @Override
    public void onCountDownFinish() {
    }
}
