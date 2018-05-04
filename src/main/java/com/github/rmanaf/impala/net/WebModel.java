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
package com.github.rmanaf.impala.net;

import com.github.rmanaf.impala.core.Model;
import com.github.rmanaf.impala.net.converters.WebModelToJSONBuilderConverter;
import com.github.rmanaf.impala.net.json.JSONBuilder;


public class WebModel extends Model implements IWebModel {

    private WebModelToJSONBuilderConverter converter = new WebModelToJSONBuilderConverter();

    public String toJSON(){

        return converter.convert(this).toString();

    }

    public WebModel fromJSON(String json)
    {

        JSONBuilder builder = new JSONBuilder(json);

        return converter.convertBack(builder);

    }

}
