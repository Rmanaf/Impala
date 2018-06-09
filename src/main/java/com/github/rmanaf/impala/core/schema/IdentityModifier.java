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
package com.github.rmanaf.impala.core.schema;


import com.github.rmanaf.impala.extensions.modifiers.ModelModifier;
import com.github.rmanaf.impala.extensions.modifiers.ModifyOnce;
import com.github.rmanaf.impala.generic.PropertyInfo;

import java.util.HashMap;
import java.util.UUID;

@ModifyOnce
public class IdentityModifier extends ModelModifier<Identity> {

    private static HashMap<Class, Integer> counter = new HashMap<>();

    public IdentityModifier(Identity modifier) {

        super(modifier);

    }

    public static int getRandomHex(int min, int max) {

        return min + ((int) (Math.random() * (max - min)));

    }

    @Override
    public Object modify(PropertyInfo property) {

        Object value = null, newValue = null;

        try {

            value = property.getValue();

            if (value != null)
                return value;

            Identity.GenerateType type = getModifier().value();

            switch (type) {

                case Guid:

                    newValue = UUID.randomUUID().toString();
                    break;

                case RandomNumber:

                    newValue = getRandomHex(0x0, 0xffffff);
                    break;

                case Indexing:

                    Class clazz = property.getOwner().getClass();

                    if (!counter.containsKey(clazz))
                        counter.put(clazz, -1);

                    int count = counter.get(clazz).intValue();
                    counter.remove(clazz);
                    counter.put(clazz, ++count);

                    newValue = count;

                    break;

            }

            value = newValue.toString();

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

        } catch (Exception e) {

            e.printStackTrace();

        }

        return value;

    }

    public static void seed(Class entity, int begin) {

        if (!counter.containsKey(entity)) {
            counter.put(entity, begin);
            return;
        }

        counter.remove(entity);
        counter.put(entity, begin);

    }

}