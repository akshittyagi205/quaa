package com.quanutrition.app.selectiondialogs;

public class CountryModel {
    String name;
    int id,phonecode,flag;


    public CountryModel(String name, int id, int phonecode, int flag) {
        this.name = name;
        this.id = id;
        this.phonecode = phonecode;
        this.flag=flag;
    }
    public CountryModel(String name, int id){
        this.name = name;
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPhonecode() {
        return phonecode;
    }

    public void setPhonecode(int phonecode) {
        this.phonecode = phonecode;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
