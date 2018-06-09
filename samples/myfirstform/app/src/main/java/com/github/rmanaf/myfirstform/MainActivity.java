package com.github.rmanaf.myfirstform;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.rmanaf.impala.Impala;
import com.github.rmanaf.impala.core.ControllerOptions;
import com.github.rmanaf.myfirstform.Controllers.HomeController;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart(){

        super.onStart();

        // Using ControllerOptions to active default operations like "submit" and "clear" forms

        ControllerOptions options = new ControllerOptions();
        options.useDefaultFormOperations();

        // Initialize Impala

        Impala.init(this , R.id.wrapper);
        Impala.run(HomeController.class, options);

    }

}
