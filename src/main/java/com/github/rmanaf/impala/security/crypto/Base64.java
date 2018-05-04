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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Base64 {

    int value() default Flag.Default;

    Type type() default Type.ByteArray;

    enum Type {

        ByteArray , String

    }

    class Flag  {

        public static final int Default = android.util.Base64.DEFAULT;
        public static final int NoWrap = android.util.Base64.NO_WRAP;
        public static final int NoPadding = android.util.Base64.NO_PADDING;
        public static final int NoClose = android.util.Base64.NO_CLOSE;
        public static final int UrlSafe = android.util.Base64.URL_SAFE;
        public static final int CRLF = android.util.Base64.URL_SAFE;

    }

}
