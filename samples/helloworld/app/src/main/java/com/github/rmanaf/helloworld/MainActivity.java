package com.github.rmanaf.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.rmanaf.helloworld.Controllers.HomeController;
import com.github.rmanaf.impala.Impala;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart(){

        super.onStart();

        // Initialize Impala
        Impala.init(this , R.id.wrapper );
        Impala.run(HomeController.class);

    }

}
