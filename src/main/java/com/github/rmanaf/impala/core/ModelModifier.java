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

import com.github.rmanaf.impala.generic.PropertyInfo;

import java.lang.annotation.Annotation;

public abstract class ModelModifier<T extends Annotation> {

    private T modifier;

    public ModelModifier(T modifier) {
        this.modifier = modifier;
    }

    public T getModifier() {

        return modifier;

    }

    public void invoke(PropertyInfo property, boolean restore) {

        try {

            if (this.getClass().isAnnotationPresent(ModifyOnce.class)) {

                Object value = property.getValue();

                if (value != null)
                    return;

                property.setValue(modify(property));

                return;

            }

            property.setValue(restore ? restore(property) : modify(property));

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public abstract Object modify(PropertyInfo property);

    public abstract Object restore(PropertyInfo property);

}
