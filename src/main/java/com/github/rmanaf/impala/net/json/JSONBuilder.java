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
package com.github.rmanaf.impala.net.json;


import com.github.rmanaf.impala.core.Model;
import com.github.rmanaf.impala.core.Utilities;
import com.github.rmanaf.impala.generic.PropertyInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class JSONBuilder {

    private StringBuilder json = new StringBuilder();

    public JSONBuilder() {

        // default constructor

    }

    public JSONBuilder(String value) {

        // initialize

        json.append(value);

    }

    public void openObject() {

        json.append("{");

    }

    public void openArray() {

        json.append("[");

    }

    public void prop(String param) {

        json.append("\"" + param + "\" : ");

    }

    public void raw(String value) {

        json.append(value);

    }

    public void next() {

        json.append(", ");

    }

    public void reset() {

        json = new StringBuilder();

    }

    public void closeObject() {

        json.append("}");

    }

    public void closeArray() {

        json.append("]");

    }

    public void put(String param, String value) {

        put(param, value, false);

    }

    public void put(String param, String value, boolean rawValue) {

        if (rawValue) {

            json.append("\"" + param + "\" : " + value);

        } else {

            json.append("\"" + param + "\" : \"" + value + "\"");

        }

    }

    public void put(PropertyInfo property) throws IllegalAccessException {

        String name = property.getName();

        Object value = property.getValue();

        if (value == null) {

            prop(name);

            raw("\"null\"");

            return;

        }


        if (property.isModel()) {

            prop(name);

            object(value);

        } else if (property.isCollection()) {

            array(property);

        } else {

            if (!Utilities.hasPrimitive(value) && !Utilities.isString(value)) {

                prop(name);

                object(value);

            } else {

                put(name, value.toString());

            }

        }

    }

    public void object(Object obj) throws IllegalAccessException {

        Field[] fields = obj.getClass().getFields();

        Iterator iterator = Arrays.asList( fields ).iterator();

        openObject();

        PropertyInfo property = null;

        while (iterator.hasNext()) {

            if (property == null) {

                property = new PropertyInfo(obj, (Field) iterator.next());

                if (property.isSynthetic() || property.isAnnotationPresent(Internal.class)) {

                    property = null;

                    continue;

                }

            }

            put(property);

            if (iterator.hasNext()) {

                property = new PropertyInfo(obj, (Field) iterator.next());

                if (property.isSynthetic() || property.isAnnotationPresent(Internal.class)) {

                    property = null;

                    continue;

                }

                next();

            }

        }

        closeObject();

    }

    public void array(PropertyInfo property) {

        try {

            prop(property.getName());

            Iterable items = null;

            if (property.isArray()) {

                array(property.getValue());

                return;

            } else {

                openArray();

                items = (Iterable) property.getValue();

            }

            Iterator iterator = items.iterator();

            while (iterator.hasNext()) {

                Object prop = iterator.next();

                Class type = prop.getClass();

                if (Model.class.isAssignableFrom(type)) {

                    object(prop);

                } else {

                    raw(Arrays.toString((Object[]) prop));

                    break;

                }

                if (iterator.hasNext())
                    next();

            }

            closeArray();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public void array(Object[] a) {

        json.append("[");

        for (int i = 0; i < a.length; i++) {

            if (i > 0) {

                json.append(",");

            }

            Object item = a[i];

            json.append(item);

        }

        json.append("]");

    }

    public void array(Object a) {

        array(convertObjectToObjectArray(a));

    }

    private Object[] convertObjectToObjectArray(Object array) {

        if (array == null) {

            return new Object[]{};

        }

        Class ofArray = array.getClass().getComponentType();

        if (ofArray.isPrimitive()) {

            List ar = new ArrayList();

            int length = Array.getLength(array);

            for (int i = 0; i < length; i++) {

                ar.add("\"" + String.valueOf(Array.get(array, i)) + "\"");

            }

            return ar.toArray();

        } else {

            return (Object[]) array;

        }

    }

    public JSONObject toJSONObject() throws JSONException {

        return new JSONObject(json.toString());

    }

    public String format(int intent) {

        String value = toString();

        try {

            JSONObject obj = new JSONObject(value);

            value = obj.toString(4);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return value;

    }

    @Override
    public String toString() {

        return json.toString().toLowerCase();

    }

}
