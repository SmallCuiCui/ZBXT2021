package org.lxzx.boot.enums;

public enum StatusEnum {

    ZAIWEI("ZAIWEI", "在位"),
    XUEXI("XUEXI", "学习"),
    CHUCHAI("CHUCHAI", "出差"),
    QINGJIA("QINGJIA", "请假"),
    LUNXIU("LUNXIU", "轮休"),
    JIEDIAO("JIEDIAO", "借调"),
    ZHUYUAN("ZHUYUAN", "住院"),
    PEIHU("PEIHU", "陪护"),
    XIUJIA("XIUJIA", "休假");

    private final String value;
    private final String name;

    StatusEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
