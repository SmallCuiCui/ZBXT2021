package org.lxzx.boot.Utils;

import java.util.Date;
import java.util.UUID;

public class CommonUtil {

    public static String getUUId() {
        String id =null;
        UUID uuid = UUID.randomUUID();
        id = uuid.toString();
        return id;
    }

//    判断两个时间是否存在重叠
    public static Boolean compareTwoTime(Date startOne, Date endOne, Date startTwo, Date endTwo){
        //after 当start1小于等于end2时返回flase  before end1大于等于start2返回flase
        if ((!startOne.after(endTwo))&&(!endOne.before(startTwo))){
            //时间重叠
            return true;
        }
        //时间不重叠
        return false;
    }

    public static int differentDays(Date date1,Date date2){
        int days = (int) Math.ceil((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days + 1;
    }

}
