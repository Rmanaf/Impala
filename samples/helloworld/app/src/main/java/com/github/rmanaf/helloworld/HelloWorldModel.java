package com.github.rmanaf.helloworld;

import com.github.rmanaf.impala.core.Model;
import com.github.rmanaf.impala.forms.Bind;
import com.github.rmanaf.impala.forms.View;

/**
 * attach view to model
 */
@View(R.layout.helloworld_layout)
public class HelloWorldModel extends Model {

    /**
     * bind property to TextView component
     * value is component id
     * set is setter method name
     */

    @Bind(value = R.id.greeting , set = "setText")
    public CharSequence greeting = "Hello World!";

}
