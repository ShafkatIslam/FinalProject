package com.example.shafkatislam.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class SplashScreen extends AppCompatActivity {

    private  int progress;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //remove the Title Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //remove the Notification Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);


        progressBar = (ProgressBar) findViewById(R.id.progressBarId);

        Thread thread = new Thread(new Runnable() {  // to change the value of progress during a time intervel we use this "Thread class" and we use "Runnable interface in the Thread Class.
            @Override
            public void run() {

                doWork();  //we call a method
                startAct();  //we call a method to go to the next Activiity

            }
        });
        thread.start();    //start the Thread Class
    }

    public void doWork(){    //create a method to get the progress from start to end during a time intervel

        for (progress=20;progress<=100;progress=progress+20)  //to get the progress the loop is used
        {
            try {
                Thread.sleep(1000);           //to get the progress from start to end during a time intervel
                progressBar.setProgress(progress);  //to set the progress value in the progressBar
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

   public void startAct(){   //create a method to go to the next Activiity

        Intent intent = new Intent(SplashScreen.this,MainActivity.class);   //By using Intent class we can go one Activity to another Activity
        startActivity(intent);
        finish();

    }
}

