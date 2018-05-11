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
package com.github.rmanaf.impala.generic;

import com.github.rmanaf.impala.core.Model;
import com.github.rmanaf.impala.core.Utilities;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class PropertyInfo {

    private Field field;
    private Object owner;

    public <T> PropertyInfo(T owner, Field field) {

        this.field = field;
        this.owner = owner;

    }

    public String getDisplayName(){

        if(field.isAnnotationPresent(DisplayName.class))
            return field.getAnnotation(DisplayName.class).value();

        return getName();

    }

    public String getName() {

        return field.getName();

    }

    public Object getValue() throws IllegalAccessException {

        Object data;

        if (field.isAccessible()) {

            data = field.get(owner);

        } else {

            field.setAccessible(true);

            data = field.get(owner);

            field.setAccessible(false);

        }

        return data;

    }

    public PropertyInfo setValue(Object value) throws NoSuchFieldException, IllegalAccessException {

        if (field.isAccessible()) {

            field.set(owner, value);

        } else {

            field.setAccessible(true);

            field.set(owner, value);

            field.setAccessible(false);

        }

        return this;

    }

    public Object getOwner() {

        return this.owner;

    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {

        return field.getAnnotation(annotationType);

    }

    public Annotation[] getDeclaredAnnotations() {

        return field.getDeclaredAnnotations();

    }

    public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {

        return field.isAnnotationPresent(annotationType);

    }

    public Class<?> getType() {

        return field.getType();

    }

    public Field toField() {

        return this.field;

    }

    public boolean isCollection() {

        return Iterable.class.isAssignableFrom(field.getType()) || isArray();

    }

    public boolean isArray() {

        return field.getType().isArray();

    }

    public boolean isModel() {

        return Model.class.isAssignableFrom(field.getType());

    }

    public boolean isSynthetic() {

        return field.isSynthetic() || Utilities.isInSystemFields(field.getName());

    }

    public boolean equals(PropertyInfo propertyInfo){

        return this.getOwner() == propertyInfo.getOwner() &&
                this.getName() == propertyInfo.getName();

    }

}
