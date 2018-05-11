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
package com.github.rmanaf.impala.extensions.validation;


import android.content.res.Resources;

import com.github.rmanaf.impala.core.Model;
import com.github.rmanaf.impala.core.BoundData;
import com.github.rmanaf.impala.core.ModelRenderer;
import com.github.rmanaf.impala.core.Utilities;
import com.github.rmanaf.impala.generic.PropertyInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;

public abstract class Validator {

    private Resources resources;

    public Validator(Resources resources) {

        this.resources = resources;

    }

    public static <A extends Annotation> int getPriority(Class<A> validator, PropertyInfo property) {

        int value = 0;

        if (!property.isAnnotationPresent(validator))
            return value;

        A modifier = property.getAnnotation(validator);

        try {

            value = (int) modifier.getClass().getMethod("priority").invoke(modifier);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return value;

    }

    public <A extends Annotation> String getErrorMessage(Class<A> validator, PropertyInfo property) {

        String message = "";

        A annotation = property.getAnnotation(validator);

        try {

            Method error = annotation.getClass().getDeclaredMethod("error");

            int value = (int) error.invoke(annotation);

            if (value != Integer.MIN_VALUE)
                message = resources.getString(value);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return message;

    }

    public <A extends Annotation> ValidationResult validate(Class<A> validator, ModelRenderer renderer) {

        ValidationResult result = new ValidationResult();

        try {

            Model model = renderer.getForm().getModel();

            for (PropertyInfo prop : model.getProperties()) {

                if (!prop.isAnnotationPresent(validator))
                    continue;

                A modifier = prop.getAnnotation(validator);

                ArrayList<BoundData> data = Utilities.getBoundData(prop , renderer.getForm());

                if (data.isEmpty() || isValid(prop, modifier)) {

                    continue;

                }

                String error = getErrorMessage(validator, prop);

                if (error.isEmpty())
                    error = error.concat(getErrorDefaultMessage(prop, modifier));

                result.getErrors().add(new ValidationError(prop, getErrorCode(),
                        getPriority(validator, prop), error));


            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return result;

    }

    public abstract int getErrorCode();

    public abstract boolean isValid(PropertyInfo field, Annotation modifier) throws IllegalAccessException;

    public abstract String getErrorDefaultMessage(PropertyInfo field, Annotation modifier);

}
