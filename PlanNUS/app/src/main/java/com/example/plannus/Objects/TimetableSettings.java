package com.example.plannus.Objects;

import com.google.firebase.database.PropertyName;

import java.util.ArrayList;

public class TimetableSettings {

//    @PropertyName("module1")
//    private String module1;
//
//    @PropertyName("module2")
//    private String module2;
//
//    @PropertyName("module3")
//    private String module3;

    @PropertyName("moduleList")
    private ArrayList<String> moduleList;

    @PropertyName("size")
    private int size;
    
//    public TimetableSettings(String module1, String module2, String module3) {
//        this.module1 = module1;
//        this.module2 = module2;
//        this.module3 = module3;
//    }

    public TimetableSettings() {

    }

    public TimetableSettings(ArrayList<String> moduleList) {
        this.moduleList = moduleList;
        this.size = moduleList.size();
    }

    public ArrayList<String> getModuleList() {
        return moduleList;
    }

    public int getSize() {
        return size;
    }
}
