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
package com.github.rmanaf.impala.validation;

import com.github.rmanaf.impala.generic.PropertyInfo;

public class ValidationError {

    private int code;
    private int priority;
    private String message;
    private PropertyInfo property;

    public ValidationError(PropertyInfo property , int code, int priority, String message) {

        this.code = code;
        this.message = message;
        this.property = property;
        this.priority = priority;

    }

    public String getMessage() {

        return message;

    }

    public int getCode() {

        return code;

    }

    public int getPriority() {

        return priority;

    }

    public PropertyInfo getField() {

        return property;

    }

}
