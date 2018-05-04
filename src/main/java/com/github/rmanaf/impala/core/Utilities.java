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

import android.view.ViewGroup;

import com.github.rmanaf.impala.forms.Bind;
import com.github.rmanaf.impala.forms.Component;
import com.github.rmanaf.impala.forms.Form;
import com.github.rmanaf.impala.forms.Multibind;
import com.github.rmanaf.impala.generic.PropertyInfo;
import com.github.rmanaf.impala.generic.Tuple;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Utilities {

    private static final ArrayList<String> systemFields;

    static {
        systemFields = new ArrayList<>();
        systemFields.add("serialVersionUID");
        systemFields.add("serialversionuid");
    }

    private static final Map<Class, Class> primitives;

    static {

        primitives = new HashMap<>();
        primitives.put(Boolean.class, Boolean.TYPE);
        primitives.put(Byte.class, Byte.TYPE);
        primitives.put(Character.class, Character.TYPE);
        primitives.put(Float.class, Float.TYPE);
        primitives.put(Integer.class, Integer.TYPE);
        primitives.put(Long.class, Long.TYPE);
        primitives.put(Short.class, Short.TYPE);
        primitives.put(Double.class, Double.TYPE);

    }

    public static String[] getNumbers(String array, String separator) {

        return array.replace("\"", "")
                .replaceAll("\\[", "")
                .replaceAll("\\]", "")
                .split(separator);

    }

    public static boolean isInSystemFields(String param){

        return systemFields.contains(param);

    }

    public static long[] toLongArray(String array, String separator) {

        String[] items = getNumbers(array, separator);

        long[] results = new long[items.length];

        for (int i = 0; i < items.length; i++) {

            results[i] = Long.parseLong(items[i]);

        }

        return results;

    }



    public static float[] toFloatArray(String array, String separator) {

        String[] items = getNumbers(array, separator);

        float[] results = new float[items.length];

        for (int i = 0; i < items.length; i++) {

            results[i] = Float.parseFloat(items[i]);

        }

        return results;

    }

    public static double[] toDoubleArray(String array, String separator) {

        String[] items = getNumbers(array, separator);

        double[] results = new double[items.length];

        for (int i = 0; i < items.length; i++) {

            results[i] = Double.parseDouble(items[i]);

        }

        return results;

    }

    public static int[] toIntegerArray(String array, String separator) {

        String[] items = getNumbers(array, separator);

        int[] results = new int[items.length];

        for (int i = 0; i < items.length; i++) {

            results[i] = Integer.parseInt(items[i]);

        }

        return results;

    }

    public static <E> E[] toPrimitive(E type, Object[] array) {

        E[] retVal = (E[]) new Object[array.length];

        for (int i = 0; i < array.length; i++) {

            retVal[i] = (E) array[i];

        }


        return retVal;

    }

    public static Class getPrimitive(Class fieldType) {

        return primitives.get(fieldType);

    }

    public static boolean hasPrimitive(Object value) {

        if (value == null) return false;

        Class clazz = value.getClass();

        return primitives.containsKey(clazz);

    }

    public static boolean isString(Object value) {

        return value.getClass() == String.class;

    }

    public static Tuple<Class , Class> getConverterTypes(ValueConverter converter) {

        Class t1 = null, t2 = null;

        for (Method m : converter.getClass().getDeclaredMethods()) {

            if (m.getName().equals("convert")) {

                t1 = m.getParameterTypes()[0];
                t2 = m.getReturnType();

            }

        }

        return new Tuple<>(t1, t2);

    }

    public static ArrayList<BoundData> getBoundData(PropertyInfo propertyInfo , Form form)
            throws InstantiationException, IllegalAccessException {

        return getBoundData(propertyInfo , form , (ViewGroup) form.getView());

    }

    public static ArrayList<BoundData> getBoundData(PropertyInfo propertyInfo , Form form , ViewGroup root)
            throws InstantiationException, IllegalAccessException {

        ArrayList<BoundData> data = new ArrayList<>();

        Bind bind = propertyInfo.getAnnotation(Bind.class);

        Multibind multibind = propertyInfo.getAnnotation(Multibind.class);

        if (bind != null) {

            data.add(annotationToBoundData(bind, propertyInfo , form , root));

        }

        if (multibind == null){

            return data;

        }

        for (Bind b : multibind.value()) {

            data.add(annotationToBoundData(b, propertyInfo , form , root));

        }

        return data;

    }

    private static BoundData annotationToBoundData(Bind bind, PropertyInfo propertyInfo , Form form , ViewGroup root)
            throws IllegalAccessException, InstantiationException {

        ValueConverter converter = bind.converter().getSuperclass() == ValueConverter.class ?
                    (ValueConverter) bind.converter().newInstance() : null;

        Component comp = form.getComponent(propertyInfo);

        if(comp == null) {

            comp = new Component(form, propertyInfo, bind.value() , root ,  bind.get(), bind.set(),
                    bind.component() , bind.index());

            form.registerComponent(comp);

        }

        return new BoundData(comp , bind.event() , converter , bind.autoUpdate());

    }
    
}
