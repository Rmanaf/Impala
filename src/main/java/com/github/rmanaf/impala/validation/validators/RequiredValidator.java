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
package com.github.rmanaf.impala.validation.validators;

import android.content.res.Resources;

import com.github.rmanaf.impala.generic.PropertyInfo;
import com.github.rmanaf.impala.validation.Validator;

import java.lang.annotation.Annotation;


public class RequiredValidator extends Validator {

    public RequiredValidator(Resources resources) {

        super(resources);

    }

    @Override
    public int getErrorCode() {

        return 0;

    }

    @Override
    public boolean isValid(PropertyInfo property, Annotation modifier)
            throws IllegalAccessException {

        Object value;

        value = property.getValue();

        if (value != null && value.getClass() == String.class) {

            return !((String) value).isEmpty();

        }

        return false;

    }

    @Override
    public String getErrorDefaultMessage(PropertyInfo property, Annotation modifier) {

        return ("Field \"").concat(property.getName()).concat("\" is required");

    }


}
