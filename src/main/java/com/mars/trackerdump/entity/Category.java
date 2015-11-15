package com.mars.trackerdump.entity;

public class Category {

    //"ID категории";"Название категории";"Файл с раздачами"
    long cat_id;
    String cat_name;
    String cat_filename;

    public Category() {
    }

    public Category(long cat_id, String cat_name, String cat_filename) {
        this.cat_id = cat_id;
        this.cat_name = cat_name;
        this.cat_filename = cat_filename;
    }

    public long getCat_id() {
        return cat_id;
    }

    public void setCat_id(long cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCat_filename() {
        return cat_filename;
    }

    public void setCat_filename(String cat_filename) {
        this.cat_filename = cat_filename;
    }

}
