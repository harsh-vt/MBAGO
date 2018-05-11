package com.example.mb_ago;

 import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.widget.TextView;
        import java.lang.Math;



public class Result extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i=getIntent();
        setContentView(R.layout.result);
        double percent=0;
        int m=i.getExtras().getInt("marks");
        int tot=i.getExtras().getInt("total");
        percent=((double) m/tot)*100;
        TextView t1=(TextView) findViewById(R.id.textView2);
        t1.setText("Marks Obtained: "+m);
        TextView t2=(TextView) findViewById(R.id.textView3);
        t2.setText("Percentage: "+percent+"%");
    }

}
