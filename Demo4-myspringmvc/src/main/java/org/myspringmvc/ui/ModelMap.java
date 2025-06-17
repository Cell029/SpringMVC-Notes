package org.myspringmvc.ui;

import java.util.LinkedHashMap;

public class ModelMap extends LinkedHashMap<String, Object> {
    public ModelMap() {
    }

    // 向域中存数据
    public ModelMap addAttribute(String name, String value){
        this.put(name, value);
        return this;
    }
}
