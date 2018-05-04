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
package com.github.rmanaf.impala.net.converters;

import com.github.rmanaf.impala.core.ValueConverter;
import com.github.rmanaf.impala.net.WebModel;
import com.github.rmanaf.impala.net.json.JSONBuilder;
import com.github.rmanaf.impala.net.json.JSONParser;

import org.json.JSONException;

public class WebModelToJSONBuilderConverter extends ValueConverter<WebModel, JSONBuilder> {

    @Override
    public JSONBuilder convert(WebModel value) {

        JSONBuilder builder = new JSONBuilder();

        try{

            builder.object(value);

        }catch (IllegalAccessException ex){

            ex.printStackTrace();

        }

        return builder;

    }

    @Override
    public WebModel convertBack(JSONBuilder value)  {

        WebModel result = null;

        try {

            result = new JSONParser(WebModel.class , value).parse();

        } catch (JSONException e) {

             e.printStackTrace();

        }

       return result;


    }

}
