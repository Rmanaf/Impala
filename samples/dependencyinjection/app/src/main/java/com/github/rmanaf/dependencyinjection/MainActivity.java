package com.github.rmanaf.dependencyinjection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.rmanaf.dependencyinjection.Controllers.AccountController;
import com.github.rmanaf.dependencyinjection.Services.AuthService;
import com.github.rmanaf.impala.Impala;
import com.github.rmanaf.impala.core.ControllerOptions;

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

        // Use AuthService as injectable dependency
        Impala.use(AuthService.class);

        ControllerOptions options = new ControllerOptions();

        // Use submit and clear methods in forms
        options.useDefaultFormOperations();

        // Use one of the following names as startup method
        options.setStartupPage("Index|index|Login|login");

        // Start controller
        Impala.run(AccountController.class , options);

    }
}
