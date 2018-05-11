package com.github.rmanaf.dependencyinjection.Controllers;

import android.widget.TextView;

import com.github.rmanaf.dependencyinjection.Models.LoginModel;
import com.github.rmanaf.dependencyinjection.Models.PanelModel;
import com.github.rmanaf.dependencyinjection.R;
import com.github.rmanaf.dependencyinjection.Services.AuthService;
import com.github.rmanaf.impala.core.Controller;
import com.github.rmanaf.impala.extensions.validation.ValidationResult;
import com.github.rmanaf.impala.forms.Submit;

public class AccountController extends Controller {

    private AuthService _authsvc;

    // Inject AuthService instance to AccountController
    public AccountController(AuthService authsvc){
        _authsvc = authsvc;
    }

    public void Login(){
        view("Login" , new LoginModel());
    }

    @Submit()
    public void Login(LoginModel model){

        ValidationResult state = getRenderer().getModelState();

        if(state.isValid()){

           if( _authsvc.hasPermission(model.username , model.password))
               view("Panel" , new PanelModel());

        } else {

            TextView status = getActivity().findViewById(R.id.status);

            status.setText(state.toString());

        }

    }

}
