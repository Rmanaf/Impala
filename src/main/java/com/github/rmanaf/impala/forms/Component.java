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


import android.content.Context;
import android.view.ViewGroup;

import com.github.rmanaf.impala.generic.PropertyInfo;

import java.lang.reflect.InvocationTargetException;

public class Component<T extends android.view.View> {

    private int mViewId;
    private PropertyInfo mProperty;
    private String mSetter;
    private String mGetter;
    private Class mViewType;
    private Form mForm;
    private int mIndex;
    private T mView;
    private ViewGroup mRootView;

    public Component(Form form, PropertyInfo property, int id, ViewGroup root , String getter, String setter,
                     Class<T> viewType, int index) {

        mForm = form;
        mProperty = property;
        mSetter = setter;
        mGetter = getter;
        mViewType = viewType;
        mViewId = id;
        mRootView = root;
        mIndex = index;

    }

    public int getId() {

        return mViewId;

    }

    public Class<T> getType() {

        return mViewType;

    }

    public String getSetterName() {

        return mSetter;

    }

    public String getGetterName() {

        return mGetter;

    }

    public PropertyInfo getPropertyInfo() {

        return mProperty;

    }

    public Form getForm() {

        return mForm;

    }

    public android.view.View getView() throws
            NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException,
            InstantiationException {

        // generate component view or find it in root

        if (mView == null && mViewType != Object.class &&

                mForm.getModel().getOptions().canGenerateComponents()) {

            mView = getType().getConstructor(Context.class)
                    .newInstance(mForm.getRenderer().getWrapper().getActivity());

            mViewId = (int) (Math.random() * Integer.MAX_VALUE);

            mView.setId(mViewId);

            mRootView.addView(mView, mIndex);


        } else if (mView == null) {

            mView = (T) mRootView.findViewById(mViewId);

        }

        return mView;

    }


}
