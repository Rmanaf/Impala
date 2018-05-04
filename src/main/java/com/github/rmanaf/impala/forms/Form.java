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
package com.github.rmanaf.impala.forms;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.rmanaf.impala.core.Model;
import com.github.rmanaf.impala.core.ModelRenderer;
import com.github.rmanaf.impala.generic.PropertyInfo;

import java.util.ArrayList;
import java.util.List;

public class Form {


    private ModelRenderer mRenderer;
    private android.view.View mView;
    private Model mModel;
    private List<IFormOperation> mOperations;
    private List<Component> mComponents;


    public Form(ModelRenderer renderer){

        mRenderer = renderer;

    }

    public Form setModel(Model model){

        mModel = model;

        return this;

    }

    public Form create(LayoutInflater inflater, ViewGroup container) {

        mView = inflater.inflate( getModel().getOptions().getFormLayoutId() , container, false);

        getComponents().clear();

        return this;

    }

    public Form registerOperation(IFormOperation operation){

        if(getOperations().indexOf(operation)  <= -1) {

            getOperations().add(operation);

        }

        return this;

    }

    public Form registerComponent(Component component){

        if(!getComponents().contains(component)) {

            getComponents().add(component);

        }

        return this;

    }

    public Form initOperations(){

        for (IFormOperation operation:getOperations()) {

            operation.onInit();

        }

        return this;

    }


    public ModelRenderer getRenderer(){

        return mRenderer;

    }

    public List<Component> getComponents() {

        if(mComponents == null){

            mComponents = new ArrayList<>();

        }

        return mComponents;

    }

    public Model getModel(){

        return mModel;

    }

    public android.view.View getView() {

        return mView;

    }

    public List<IFormOperation> getOperations(){

        if(mOperations == null){

            mOperations = new ArrayList<>();

        }

        return mOperations;

    }

    public Component getComponent(PropertyInfo property){

        for (Component comp: getComponents()) {

            if(comp.getPropertyInfo().equals(property)) {

                return comp;

            }

        }

        return null;

    }

}
