package com.github.rmanaf.dependencyinjection.Models;

import com.github.rmanaf.dependencyinjection.R;
import com.github.rmanaf.impala.core.Model;
import com.github.rmanaf.impala.core.converters.CharSequenceToStringValueConverter;
import com.github.rmanaf.impala.extensions.validation.MinLength;
import com.github.rmanaf.impala.extensions.validation.Required;
import com.github.rmanaf.impala.forms.Bind;
import com.github.rmanaf.impala.forms.View;
import com.github.rmanaf.impala.generic.DisplayName;

@View(value = R.layout.login_view , submit = R.id.submit)
public class LoginModel extends Model {

    @Required
    @DisplayName("User Name")
    @Bind(value = R.id.username , set = "setText" , get = "getText"
            , converter = CharSequenceToStringValueConverter.class)
    public String username;

    @Required
    @MinLength(8)
    @DisplayName("Password")
    @Bind(value = R.id.password , set = "setText" , get = "getText"
            , converter = CharSequenceToStringValueConverter.class)
    public String password;

}
