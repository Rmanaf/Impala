package com.github.rmanaf.myfirstform.Models;

import com.github.rmanaf.impala.core.Model;
import com.github.rmanaf.impala.forms.Bind;
import com.github.rmanaf.myfirstform.R;

// Define base model to access "title" field in all models
public class BaseModel extends Model {

    @Bind(value = R.id.title , set = "setText")
    public CharSequence title;

}
