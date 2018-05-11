package com.github.rmanaf.dependencyinjection.Models;

import com.github.rmanaf.dependencyinjection.R;
import com.github.rmanaf.impala.core.Model;
import com.github.rmanaf.impala.forms.Bind;
import com.github.rmanaf.impala.forms.View;

@View(R.layout.panel_view)
public class PanelModel extends Model {

    @Bind(value = R.id.greeting , set = "setText" )
    public CharSequence greeting = "Welcome";

}
