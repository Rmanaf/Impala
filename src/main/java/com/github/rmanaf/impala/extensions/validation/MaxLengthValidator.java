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

import com.github.rmanaf.impala.generic.PropertyInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;

public class MaxLengthValidator extends Validator {

    public MaxLengthValidator(Resources resources) {
        super(resources);
    }

    @Override
    public int getErrorCode() {

        return 2;

    }

    @Override
    public boolean isValid(PropertyInfo property, Annotation modifier)
            throws IllegalAccessException {

        int val = ((MaxLength) modifier).value();

        if(!property.isCollection())
            return property.getValue().toString().length() <= val;

        if(property.isArray())
            return Array.getLength(property.getValue()) <= val;

        return false;

    }

    @Override
    public String getErrorDefaultMessage(PropertyInfo property, Annotation modifier) {

        boolean iscol = property.isCollection();

        return String.format("Field \"%s\"%s must be less than %s %s" ,
                property.getDisplayName() ,
                iscol ? " size" : "" ,
                String.valueOf(((MaxLength) modifier).value()),
                iscol ? "" : "characters");


    }

}
