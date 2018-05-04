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
import android.util.Patterns;

import com.github.rmanaf.impala.generic.PropertyInfo;
import com.github.rmanaf.impala.validation.Validator;

import java.lang.annotation.Annotation;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataTypeValidator extends Validator {


    public DataTypeValidator(Resources resources) {
        super(resources);
    }

    public static boolean isEmailAdress(String value) {

        return Patterns.EMAIL_ADDRESS.matcher(value).matches();

    }

    public static boolean isPhoneNumber(String value) {

        return Patterns.PHONE.matcher(value).matches();

    }

    public static boolean isIPAddress(String value) {

        return Patterns.IP_ADDRESS.matcher(value).matches();

    }

    public static boolean isWebURL(String value) {

        return Patterns.WEB_URL.matcher(value).matches();

    }

    public static boolean isDateTime(String value) {

        Date date = null;

        String format = "dd/MM/yyyy";

        try {

            DateFormat sdf = new SimpleDateFormat(format);

            date = sdf.parse(value);

            if (!value.equals(sdf.format(date)))
                date = null;

        } catch (ParseException ex) {

            ex.printStackTrace();

        }

        return date != null;
    }


    @Override
    public int getErrorCode() {

        return 0;

    }

    @Override
    public boolean isValid(PropertyInfo property, Annotation modifier) {

        try {

            return !hasTypeError((DataType) modifier, property);

        } catch (Exception e) {

            e.printStackTrace();

            return false;

        }

    }

    @Override
    public String getErrorDefaultMessage(PropertyInfo property , Annotation modifier) {

        return ("Field \"")
                .concat(property.getName())
                .concat("\" is not valid ")
                .concat(String.valueOf(((DataType)modifier) .value().toString()));

    }

    private boolean hasTypeError(DataType dt, PropertyInfo prop)
            throws NoSuchFieldException, IllegalAccessException {

        String value = (String) prop.getValue();

        switch (dt.value()) {
            case EmailAddress:
                return !isEmailAdress(value);
            case PhoneNumber:
                return !isPhoneNumber(value);
            case IPAddress:
                return !isIPAddress(value);
            case WebURL:
                return !isWebURL(value);
            case DateTime:
                return !isDateTime(value);
            default:
                return false;
        }

    }


}
