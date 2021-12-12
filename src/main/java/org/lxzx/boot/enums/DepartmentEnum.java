package org.lxzx.boot.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//部门常量
public enum DepartmentEnum implements BaseEnum {
    DEPT1("ZHONGXIN","中心办"),DEPT2("dept2","单位2"),DEPT3("dept3","单位3"),DEPT4("dept4","单位四");

    private DepartmentEnum(String value, String name){
        this.value = value;
        this.name = name;
    }

    private final String value;
    private final String name;

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    /**
     * 根据value值获取枚举
     * @param value
     * @return
     */
    public static DepartmentEnum stateOf(String value) {
        for (DepartmentEnum e : values()) {
            if(e.getValue() == value) {
                return e;
            }
        }
        return null;
    }

    public static List<DepartmentEnum> getAllEnum() {
        List<DepartmentEnum> list = new ArrayList<>();
        list.addAll(Arrays.asList(values()));
        return list;
    }
}
