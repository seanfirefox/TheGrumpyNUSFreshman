package com.example.plannus.Objects;

import com.google.firebase.database.PropertyName;

public class TimetableSettings {

    @PropertyName("module1")
    private String module1;

    @PropertyName("module2")
    private String module2;

    @PropertyName("module3")
    private String module3;
    
    public TimetableSettings(String module1, String module2, String module3) {
        this.module1 = module1;
        this.module2 = module2;
        this.module3 = module3;
    }

    public String getModule1() {
        return module1;
    }

    public String getModule2() {
        return module2;
    }

    public String getModule3() {
        return module3;
    }


}
