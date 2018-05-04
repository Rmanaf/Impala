/**
 *    Copyright (C) 2018 Arman Afzal <arman.afzal@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.github.rmanaf.impala.core;


import android.app.FragmentTransaction;

public class ControllerOptions {

    private String mStartupPage = "Index|index|Home|home";
    private boolean mCacheForms;
    private boolean mDefaultOperations = false;
    private IControllerLifecycleHooks mLifecycleHooks;
    private Controller mController;

    public ControllerOptions $for(Controller b){

        mController = b;

        return this;

    }

    public void onInit(){

        getLifecycleHooks().onInit(mController);

    }

    public void onViewDestroy(){

        getLifecycleHooks().onViewDestroy(mController);

    }

    public void onStart() {

        getLifecycleHooks().onStart(mController);

    }

    public void onCreateView(){

        getLifecycleHooks().onCreateView(mController);

    }

    public void onBeginTransaction(FragmentTransaction ft) {

        getLifecycleHooks().onBeginTransaction(ft);

    }

    public boolean canCacheForms(){

        return mCacheForms;

    }

    public boolean isFormDefaultOperationsAllowed(){

        return mDefaultOperations;

    }

    public ControllerOptions cacheForms(){

        mCacheForms = true;

        return this;

    }

    public ControllerOptions useDefaultFormOperations(){

        mDefaultOperations = true;

        return this;

    }

    public ControllerOptions setStartupPage(String page){

        mStartupPage = page;

        return this;

    }

    public String getStartupPage(){

        return mStartupPage;

    }

    public ControllerOptions setLifecycleHooks(IControllerLifecycleHooks lifecycle){

        mLifecycleHooks = lifecycle;

        return this;

    }

    private IControllerLifecycleHooks getLifecycleHooks(){

        if(mLifecycleHooks == null){

            mLifecycleHooks = new IControllerLifecycleHooks() {
                @Override public void onInit(IController controller) {}
                @Override public void onCreateView(IController controller) {}
                @Override public void onStart(IController controller) {}
                @Override public void onViewDestroy(IController controller) {}
                @Override public void onBeginTransaction(FragmentTransaction ft) { }
            };

        }

        return mLifecycleHooks;

    }

}
