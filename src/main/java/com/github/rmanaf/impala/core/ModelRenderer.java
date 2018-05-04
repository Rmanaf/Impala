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

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.github.rmanaf.impala.forms.Bind;
import com.github.rmanaf.impala.forms.Form;
import com.github.rmanaf.impala.forms.IFormOperation;
import com.github.rmanaf.impala.forms.ItemView;
import com.github.rmanaf.impala.generic.PropertyInfo;
import com.github.rmanaf.impala.generic.Tuple;
import com.github.rmanaf.impala.validation.ValidationResult;
import com.github.rmanaf.impala.validation.Validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *  TODO: add plugin layer
 *  for now just looking for modifiers and validators by their suffix's
 **/
public class ModelRenderer extends Fragment {

    /**
     *   suffix of {@link ModelModifier} plugins
    **/
    private static final String MODIFIERS_SUFFIX = "Modifier";

    /**
     *   suffix of {@link Validator} plugins
     **/
    private static final String VALIDATOR_SUFFIX = "Validator";


    private IOnModelChangedEventListener mOnChangeListener;

    private Form mForm;

    private Controller mWrapper;


    public ModelRenderer() {

        super();

    }

    public Controller getWrapper() {

        return mWrapper;

    }

    public ModelRenderer setWrapper(Controller wrapper) {

        mWrapper = wrapper;

        return this;

    }

    public void bindEvents() throws NoSuchFieldException, IllegalAccessException,
            NoSuchMethodException, java.lang.InstantiationException, InvocationTargetException {

        if (getOnModelChangedEventListener() == null) {

            return;

        }

        for (final PropertyInfo prop : getForm().getModel().getProperties()) {

            ArrayList<BoundData> boundList = Utilities.getBoundData(prop, getForm());

            if (boundList.isEmpty()) {

                continue;

            }

            for (final BoundData data : boundList) {

                android.view.View v = data.getComponent().getView();

                if (v == null) {

                    break;

                }

                switch (data.getEvent()) {

                    case None:
                        continue;

                    case Click:

                        v.setClickable(true);

                        v.setOnClickListener(new android.view.View.OnClickListener() {
                            @Override
                            public void onClick(android.view.View view) {

                                try {

                                    Object value = prop.getValue();

                                    getOnModelChangedEventListener().onFieldChanged(data, value);

                                } catch (Exception e) {

                                    e.printStackTrace();

                                }

                            }
                        });

                        break;

                    case Focus:
                    case Blur:

                        v.setFocusable(true);

                        v.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(android.view.View view, boolean b) {

                                getOnModelChangedEventListener().onFieldChanged(data, b);

                            }
                        });

                        break;

                }

            }

        }

    }

    public ModelRenderer update(boolean fromModel) {

        return update(getForm().getModel(), fromModel);

    }

    public ModelRenderer update(Model model, boolean fromModel) {

        IOnModelChangedEventListener listener = getOnModelChangedEventListener();

        for (PropertyInfo prop : getForm().getModel().getProperties()) {

            try {

                ArrayList<BoundData> boundList = Utilities.getBoundData(prop, getForm());

                if (boundList.isEmpty()) {

                    continue;

                }

                for (BoundData data : boundList) {

                    android.view.View view = data.getComponent().getView();

                    if (!data.AutoUpdate || view == null) {

                        continue;

                    }

                    Object value = prop.getValue();


                    // creates adapter and custom view for collections

                    if (prop.isCollection()) {

                        if (view instanceof AdapterView && prop.isAnnotationPresent(ItemView.class)) {

                            AdapterView target = (AdapterView) view;

                            if (target.getAdapter() == null) {

                                target.setAdapter(createCollectionAdapter(target, prop));

                                target.setOnItemClickListener(createCollectionItemClickListener());

                            }

                            continue;

                        } else {

                            // TODO: find target class and use setAdapter
                            // for now it's experimental

                            IAdapterView target = (IAdapterView) view;

                            if(target.getAdapter() == null) {

                                Log.e(getClass().getSimpleName() , "Adapter is not defined");

                                //target.setAdapter(createCollectionAdapter(target, prop));

                            }


                        }

                    }

                    Method method = findMethod(prop, data, fromModel);


                    if (method == null)
                        continue;


                    if (fromModel) {

                        if (value == null) {

                            continue;

                        }

                        if (data.hasConverter()) {

                            value = data.getConverter().convertBack(value);

                        }

                        method.invoke(view, value);

                    } else {

                        Object newValue = method.invoke(view);

                        if (data.hasConverter()) {

                            newValue = data.getConverter().convert(newValue);

                        }

                        prop.setValue(newValue);

                        if (listener != null && data.getEvent() == Bind.Event.Change) {

                            listener.onFieldChanged(data, value);

                        }

                    }

                }

            } catch (Exception ex) {

                ex.printStackTrace();

                continue;

            }

        }

        return this;

    }

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {

        getForm().create(inflater, container);

        getWrapper().getOptions().onCreateView();

        return getForm().getView();

    }

    @Override
    public void onStart() {

        super.onStart();

        try {

            modify(getForm().getModel(), true);

            bindEvents();

            update(true);

            // add default operations : submit , clear

            if (getWrapper().getOptions().isFormDefaultOperationsAllowed()) {

                getForm().registerOperation(new IFormOperation() {

                    @Override
                    public void onInit() {

                        android.view.View submit = getForm().getView().findViewById(getForm()
                                .getModel().getOptions().getFormSubmitId());

                        if (submit == null) {

                            return;

                        }

                        submit.setOnClickListener(new android.view.View.OnClickListener() {

                            @Override
                            public void onClick(android.view.View view) {

                                onTrigger();

                            }

                        });

                    }

                    @Override
                    public void onTrigger() {

                        try {

                            update(false).modify(getForm().getModel(), false);

                        } catch (IllegalAccessException e) {

                            e.printStackTrace();

                        }

                        if (mOnChangeListener != null) {

                            mOnChangeListener.onSubmit(getForm().getModel());

                        }

                    }

                })
                        .registerOperation(new IFormOperation() {

                            @Override
                            public void onInit() {

                                android.view.View clear = getForm().getView().findViewById(getForm()
                                        .getModel().getOptions().getFormClearId());


                                if (clear == null) {

                                    return;

                                }

                                clear.setOnClickListener(new android.view.View.OnClickListener() {

                                    @Override
                                    public void onClick(android.view.View view) {

                                        onTrigger();

                                    }

                                });

                            }

                            @Override
                            public void onTrigger() {

                                // clear

                            }
                        });

            }

            // initialize operations

            getForm().initOperations();

            getWrapper().getOptions().onStart();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    @Override
    public void onDestroy() {

        getWrapper().getOptions().onViewDestroy();

        super.onDestroy();

    }

    @Override
    public android.view.View getView() {

        return getForm().getView();

    }

    public Form getForm() {

        if (mForm == null) {

            mForm = new Form(this);

        }

        return mForm;

    }

    public ModelRenderer render(Model model) {

        return render(model, null);

    }

    public ModelRenderer render(Model model, ModelOptions options) {

        try {

            getForm().setModel(model.setOptions(options));

            modify(getForm().getModel(), false);

        } catch (IllegalAccessException e) {

            e.printStackTrace();

        }

        return this;

    }

    public IOnModelChangedEventListener getOnModelChangedEventListener() {

        return this.mOnChangeListener;

    }

    public ModelRenderer setOnModelChangedEventListener(IOnModelChangedEventListener listener) {

        this.mOnChangeListener = listener;

        return this;

    }

    public ModelRenderer modify(Model model, boolean restore) throws IllegalAccessException {

        List<Tuple<PropertyInfo, ModelModifier>> modifiers = new ArrayList<>();

        for (PropertyInfo prop : model.getProperties()) {

            // check if field is collection

            if (prop.isCollection()) {

                Object value = prop.getValue();

                if (value == null) {

                    continue;

                }

                Iterator collection = prop.isArray() ? Arrays.asList(prop.getValue()).iterator() :
                        Iterable.class.cast(value).iterator();

                while (collection.hasNext()) {

                    Object item = collection.next();

                    // apply item modifiers

                    if (item instanceof Model) {

                        modify((Model) item, restore);

                    }

                }

            }

            // for non-collections

            Annotation[] annotations = prop.getDeclaredAnnotations();

            for (Annotation a : annotations) {

                String name = a.annotationType().getName().concat(MODIFIERS_SUFFIX);

                try {

                    Class<?> clazz = Class.forName(name);

                    ModelModifier object = (ModelModifier) clazz.getConstructor(a.annotationType())
                            .newInstance(a);

                    modifiers.add(new Tuple(prop, object));

                } catch (Exception e) {

                    continue;

                }

            }

        }

        for (Tuple<PropertyInfo, ModelModifier> m : modifiers) {

            m.Item2.invoke(m.Item1, restore);

        }

        return this;

    }

    private ArrayAdapter<Model> createCollectionAdapter(AdapterView view, PropertyInfo property)
            throws NoSuchFieldException, IllegalAccessException, NullPointerException {

        final ItemView iw = property.getAnnotation(ItemView.class);


        // leave if item view is not defined

        if (iw == null) {

            Log.e(getClass().getSimpleName() , "Item layout is not defined, use @ItemView()");

            return null;

        }


        return new ArrayAdapter<Model>(getWrapper().getActivity(),
                getForm().getModel().getOptions().getFormLayoutId(),
                (List<Model>) property.getValue()) {

            @Override
            public android.view.View getView(int position, android.view.View convertView, ViewGroup parent) {

                android.view.View v = convertView;

                if (v == null) {

                    v = LayoutInflater.from(getContext()).inflate(iw.id(), null);

                }

                Model p = getItem(position);

                for (PropertyInfo prop : p.getProperties()) {

                    if (prop.isSynthetic()) {

                        continue;

                    }

                    try {

                        ArrayList<BoundData> dt = Utilities.getBoundData(prop, getForm() ,(ViewGroup) v);

                        Object value = prop.getValue();

                        for (BoundData b : dt) {

                            Method m = findMethod(prop, b, true);

                            android.view.View target = b.getComponent().getView();

                            if (value == null || m == null) {

                                continue;

                            }

                            if (b.hasConverter()) {

                                value = b.getConverter().convertBack(value);

                            }

                            m.invoke(target, value);

                        }

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                }

                return v;

            }


        };

    }

    private AdapterView.OnItemClickListener createCollectionItemClickListener() {

        return new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, android.view.View view, int i, long l) {

                if (mOnChangeListener != null)
                    mOnChangeListener.onCollectionItemSelected(getForm().getModel(), adapterView.getAdapter().getItem(i));

            }

        };

    }


    private Method findMethod(PropertyInfo property, BoundData data, boolean setter)
            throws NoSuchMethodException, NullPointerException, IllegalAccessException,
            java.lang.InstantiationException, InvocationTargetException {

        Class compType, fieldType, fieldPrimitiveType;

        if (data.getComponent().getView() == null) {

            return null;

        }

        fieldType = property.getType();

        compType = data.getComponent().getView().getClass();

        Method method = null;

        boolean primitivesChecked = false;


        if (setter && data.getConverter() instanceof ValueConverter) {

            fieldType = Utilities.getConverterTypes(data.getConverter()).Item1;

        }


        fieldPrimitiveType = Utilities.getPrimitive(fieldType);


        while (method == null) {

            try {

                if (setter) {

                    method = compType.getMethod(data.getComponent().getSetterName(), fieldType);

                } else {

                    method = compType.getMethod(data.getComponent().getGetterName());

                }


            } catch (NoSuchMethodException e) {

                if (!primitivesChecked) {

                    primitivesChecked = true;

                    if (fieldPrimitiveType != null)
                        fieldType = fieldPrimitiveType;

                } else {

                    break;

                }

            }

        }

        return method;

    }

    public ValidationResult getModelState() {

        Resources res = getActivity().getResources();

        List<Tuple<Validator, Annotation>> validators = new ArrayList<>();

        List<String> names = new ArrayList<>();

        for (PropertyInfo prop : getForm().getModel().getProperties()) {

            Annotation[] annotations = prop.getDeclaredAnnotations();

            for (Annotation annotation : annotations) {

                String name = annotation.annotationType().getName().concat(VALIDATOR_SUFFIX);

                Class<?> clazz;

                try {

                    if (names.contains(name))
                        continue;

                    clazz = Class.forName(name);

                    Constructor<?> ctor = clazz.getConstructor(Resources.class);

                    Validator object = (Validator) ctor.newInstance(res);

                    names.add(name);

                    validators.add(new Tuple(object, annotation));

                } catch (Exception e) {

                    // ignore all exceptions

                }

            }

        }

        return validate(validators.toArray(new Tuple[validators.size()]));

    }

    private ValidationResult validate(Tuple<Validator, Annotation>... pairs) {

        ValidationResult result = new ValidationResult();

        for (Tuple<Validator, Annotation> p : pairs) {

            result.concat(p.Item1.validate(p.Item2.annotationType(), this));

        }

        return result;

    }

    interface IAdapterView {
        void setAdapter();
        Object getAdapter();
    }


}
