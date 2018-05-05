package com.github.rmanaf.helloworld;

import com.github.rmanaf.impala.core.Controller;

public class HomeController extends Controller {

    public void Index(){
        view("Index" , new HelloWorldModel());
    }

}
