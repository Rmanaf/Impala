package com.github.rmanaf.myfirstform.Models;

import com.github.rmanaf.impala.forms.Bind;
import com.github.rmanaf.impala.forms.View;
import com.github.rmanaf.impala.extensions.validation.Required;
import com.github.rmanaf.myfirstform.R;

// Binding model to first layout and assign submit button id using @View
@View(value = R.layout.first_form_view , submit = R.id.submit)
public class FirstFormModel extends BaseModel {

    /**
     * Using "Required" annotation to force client to enter value
     */

    @Required()

    /**
     * "converter" will convert TextView value to String
     */

    @Bind(value = R.id.name , set = "setText" , get = "getText")
    public CharSequence name;

   public FirstFormModel(){
       title = "First Form";
   }

}
