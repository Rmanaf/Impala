package com.github.rmanaf.myfirstform.Models;

import com.github.rmanaf.impala.forms.Bind;
import com.github.rmanaf.impala.forms.View;
import com.github.rmanaf.myfirstform.R;


@View(value = R.layout.second_form_view)
public class SecondFormModel extends BaseModel {

    @Bind(value = R.id.message , set = "setText")
    public CharSequence message;

    public SecondFormModel(){
        title = "Second Form";
    }

    public SecondFormModel(CharSequence name){
        message = String.format("First model submitted \"%s\"" , name);
    }

}
