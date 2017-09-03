package com.samorgs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.saumya.wakeup.R;

import java.util.Random;

/**
 * Created by saumya on 25-11-2016.
 */
public class Mathproblems extends AppCompatActivity {
    public  String ans="";
    public  static int answer=5;
    Uri uri;
    protected void onCreate(Bundle savedInstanceState){
        Intent i = getIntent();
        uri = i.getParcelableExtra("Uri");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mathactivity);
        Button button = (Button)findViewById(R.id.inp1);
        Button button1=(Button)findViewById(R.id.inp3);
        Button button2 =(Button)findViewById(R.id.inp2);
        final EditText tv = (EditText)findViewById(R.id.answeedit);
        int answer = 5;
        Random rand = new Random();
        int randomnum =rand.nextInt(5);
        int randomnum1 =rand.nextInt(5);
        int randoprs=rand.nextInt(4);
        if(randomnum==0||randomnum1==0||randoprs==0)
        {randomnum=2;
            randomnum1=2;
            randoprs=1;}
        button.setText(String.valueOf(randomnum));
        button2.setText(String.valueOf(randomnum1));
        switch(randoprs){
            case 1:
                answer=randomnum+randomnum1;
                if(answer>20)
                    answer=20;
                if(answer==0)
                    answer=5;
                button1.setText("+");
                break;
            case 2:
                answer=Math.abs(randomnum-randomnum1);
                if(answer>20)
                    answer=20;
                if(answer==0)
                    answer=5;
                Log.e("answers",""+answer);
                button1.setText("-");
                break;
            case 3:
                answer=randomnum*randomnum1;
                if(answer>20)
                    answer=20;
                if(answer==0)
                    answer=5;
                button1.setText("*");
                break;
            case 4:
                answer=randomnum/randomnum1;
                if(answer>20)
                    answer=20;
                if(answer==0)
                    answer=5;
                button1.setText("/");
                break;
        }

         ans= String.valueOf(answer);
        Button submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = tv.getText().toString();
                int x = Integer.parseInt(str);
                if(x==0)
                    x=5;
                str= String.valueOf(x);
                if(str.equals(ans)){
                    Intent intent = new Intent(Mathproblems.this,MathActivity.class);
                    intent.putExtra("answer",str);
                    intent.putExtra("Uri",uri);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });


}

    public void onStop(){
        super.onStop();
    }
    public void onBackPressed() {
        // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // super.onBackPressed();

    }

}
