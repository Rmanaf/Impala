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
package com.github.rmanaf.impala.security.crypto;

import com.github.rmanaf.impala.core.ModelModifier;
import com.github.rmanaf.impala.generic.PropertyInfo;
import com.github.rmanaf.impala.net.Serializer;

public class Base64Modifier extends ModelModifier<Base64> {

    public Base64Modifier(Base64 modifier) {
        super(modifier);
    }

    @Override
    public Object modify(PropertyInfo property) {

        Object value = null;

        try {

            value = property.getValue();

            if (value == null)
                return value;

            int flag = getModifier().value();

            Base64.Type type = getModifier().type();

            switch (type) {

                case String:

                    value = android.util.Base64.encodeToString(Serializer.serialize(value), flag);

                    break;

                case ByteArray:

                    value = android.util.Base64.encode(Serializer.serialize(value), flag);

                    break;

            }


        } catch (Exception e) {

            e.printStackTrace();

        }

        return value;
    }

    @Override
    public Object restore(PropertyInfo property) {

        Object value = null;

        try {

            value = property.getValue();

            if (value == null)
                return value;

            int flag = getModifier().value();

            Base64.Type type = getModifier().type();

            switch (type) {

                case String:

                    value = android.util.Base64.decode((String) value, flag);

                    break;

                case ByteArray:

                    value = android.util.Base64.encode(Serializer.serialize(value), flag);

                    break;

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return value;

    }

}
