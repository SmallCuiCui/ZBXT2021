package org.lxzx.boot.Utils;

import org.springframework.util.DigestUtils;

public class MD5Util {

    private final static String slat = "hyc&lxzx&xqxz999";

    private MD5Util() {
    }

    public static String getMD5(String passwordString) {
        String base = passwordString + "/" + slat;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }
}
