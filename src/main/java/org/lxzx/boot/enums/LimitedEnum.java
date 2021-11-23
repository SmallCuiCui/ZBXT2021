package org.lxzx.boot.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//权限常量
public enum LimitedEnum implements BaseEnum {
    NORMAL("NORMAL", "普通"),
    ZHIBAN("ZHIBAN", "值班"),
    MANAGE("MANAGE", "管理员"),
    LEADER("LEADER", "领导"),
    SUPER("SUPER", "超级管理员");

    private final String value;
    private final String name;

    LimitedEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static LimitedEnum stateOf(String value) {
        for (LimitedEnum e : values()) {
            if(e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

    public static List<LimitedEnum> getAllEnum() {
        List<LimitedEnum> list = new ArrayList<>();
        list.addAll(Arrays.asList(values()));
        return list;
    }
}
