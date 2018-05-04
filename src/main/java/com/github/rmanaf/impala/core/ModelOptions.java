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

import com.github.rmanaf.impala.forms.View;
import com.github.rmanaf.impala.validation.Validator;

public class ModelOptions {

    private boolean mModifiersEnabled = true;
    private int mFormLayoutId;
    private int mFormSubmitId;
    private int mFormClearId;
    private Model mOwner;
    private boolean mAutoGenerateComponents = false;

    public ModelOptions a(Model b){

        mOwner = b;

        if(mOwner.getClass().isAnnotationPresent(View.class)){

            View params =  mOwner.getClass().getAnnotation(View.class);

            mFormClearId = params.clear();
            mFormLayoutId = params.value();
            mFormSubmitId = params.submit();

        }

        return this;

    }

    public ModelOptions disableModifiers(){

        mModifiersEnabled = false;

        return this;

    }

    public boolean isModifiersEnabled(){

        return mModifiersEnabled;

    }

    public <T extends Validator> ModelOptions  addErrorPage(Class<T> validator , int layout){

        return this;

    }

    public <T extends Validator> ModelOptions addErrorPage(Class<T> validator){

        return this;

    }

    public ModelOptions setFromLayoutId(int id) {

        mFormLayoutId = id;

        return this;

    }

    public int getFormLayoutId(){

        return mFormLayoutId;

    }

    public ModelOptions setFormSubmitId(int id){

        mFormSubmitId = id;

        return this;

    }

    public int getFormSubmitId(){

        return mFormSubmitId;

    }

    public ModelOptions setFormClearId(int id){

        mFormClearId = id;

        return this;

    }

    public int getFormClearId(){

        return mFormClearId;

    }

    public boolean canGenerateComponents(){

        return mAutoGenerateComponents;

    }

    public ModelOptions allowAutoGenerateComponents(){

        mAutoGenerateComponents = true;

        return this;

    }


}
