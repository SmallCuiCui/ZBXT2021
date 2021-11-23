package org.lxzx.boot.Utils;

import java.util.UUID;

public class CommonUtil {

    public static String getUUId() {
        String id =null;
        UUID uuid = UUID.randomUUID();
        id = uuid.toString();
        return id;
    }
}
