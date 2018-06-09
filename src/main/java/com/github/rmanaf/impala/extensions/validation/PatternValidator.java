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

public class PatternValidator extends Validator {

    public PatternValidator(Resources resources) {
        super(resources);
    }

    @Override
    public int getErrorCode() {
        return 0;
    }

    @Override
    public boolean isValid(PropertyInfo field, Annotation modifier) throws IllegalAccessException {
        return java.util.regex.Pattern.matches((field.getAnnotation(Pattern.class)).value(),
                (CharSequence) field.getValue());
    }

    @Override
    public String getErrorDefaultMessage(PropertyInfo field, Annotation modifier) {
        return String.format("Field \"%s\" format is incorrect" , field.getDisplayName());
    }

}
