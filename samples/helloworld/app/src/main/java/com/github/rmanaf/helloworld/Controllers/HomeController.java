package com.github.rmanaf.helloworld.Controllers;

import com.github.rmanaf.helloworld.Models.HelloWorldModel;
import com.github.rmanaf.impala.core.Controller;

public class HomeController extends Controller {

    public void Index(){

        view("Index" , new HelloWorldModel());

    }

}
