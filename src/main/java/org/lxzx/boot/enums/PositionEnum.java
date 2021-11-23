package org.lxzx.boot.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//职务常量
public enum PositionEnum implements BaseEnum {
    CANMOU("CANMOU", "参谋"),
    SHIGUAN("SHIGUAN", "士官"),
    WENZHI("WENZHI", "文职"),
    FUZHUREN("FUZHUREN", "副主任"),
    XIELI("XIELI", "协理"),
    ZHUREN("zhuren", "主任");

    PositionEnum(String value, String name) {
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

    public static PositionEnum stateOf(String value) {
        for (PositionEnum e : values()) {
            if(e.getValue() == value) {
                return e;
            }
        }
        return null;
    }

    public static List<PositionEnum> getAllEnum() {
        List<PositionEnum> list = new ArrayList<>();
        list.addAll(Arrays.asList(values()));
        return list;
    }

}
