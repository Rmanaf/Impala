package com.github.rmanaf.myfirstform.Controllers;

import com.github.rmanaf.impala.core.Controller;
import com.github.rmanaf.impala.extensions.validation.ValidationResult;
import com.github.rmanaf.impala.forms.Submit;
import com.github.rmanaf.myfirstform.Models.*;

public class HomeController extends Controller {

    public void Index(){

        view("Index" , new FirstFormModel());

    }

    @Submit()
    public void Index(FirstFormModel model){

        ValidationResult state = getRenderer().getModelState();

        if(state.isValid()){

            view("Home" , new SecondFormModel(model.name));

        }

    }

}
