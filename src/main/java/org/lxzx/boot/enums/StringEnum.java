package org.lxzx.boot.enums;

public enum StringEnum {
    ChaoJiYongHu(1, "admin"),
    ChaoJiYongHuMiMa(2, "admin"),
    ChaoJiYongHuMingChen(3, "超级管理员"),
    DEFAULTPASSWORD(4, "123456"),
    DRAFT(5, "DRAFT"),
    DRAFTNAME(6, "草稿"),
    PUBLISH(7, "PUBLISH"),
    PUBLISHNAME(8, "提交"),
    ONE(9, "ONE"),
    ONENAME(10, "防疫执勤"),
    EIGHT(11, "EIGHT"),
    EIGHTNAME(12, "后台值班"),
    DEFAULT(1000, "空");

    private int id;
    private String info;

    public int getId() {
        return id;
    }

    public String getInfo() {
        return info;
    }

    StringEnum(int id, String info) {
        this.id = id;
        this.info = info;
    }

    public static StringEnum stateOf(int id) {
        for(StringEnum e: values()) {
            if(e.getId() == id) {
                return e;
            }
        }
        return null;
    }
}
