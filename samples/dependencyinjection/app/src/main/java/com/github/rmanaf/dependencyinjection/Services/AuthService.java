package com.github.rmanaf.dependencyinjection.Services;

import com.github.rmanaf.impala.core.IDependency;

public class AuthService implements IDependency {

    public boolean hasPermission(String username , String password){

        // check permission and return result

        return true;

    }

}
