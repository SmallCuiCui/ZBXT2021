package org.lxzx.boot.Utils;

import java.sql.Timestamp;
import java.util.Calendar;
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

//    计算相差天数
    public static int differentDays(Date date1,Date date2){
        int days = (int) Math.ceil((date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000));
        return days + 1;
    }

    public static Date getZeroTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        return zero;
    }

    public static Date getTodayEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date zero = calendar.getTime();
        return zero;
    }

    //获取本周的开始时间
    public static Date getBeginDayOfWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        return getDayStartTime(cal.getTime());
    }

    //获取本周的结束时间
    public static Date getEndDayOfWeek(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }

    //获取某个日期的开始时间
    public static Timestamp getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if(null != d) calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),    calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    //获取某个日期的结束时间
    public static Timestamp getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if(null != d) calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),    calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public static int Days(Date date) {
        Calendar cal = Calendar.getInstance();
//      将日期设置给Calendat
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
//      定义累加器储存天数
        int num = 0;
//      遍历月份，求每个月份的天数和
        for (int i = 1; i < month; i++) {
            switch (i) {
//          当月为大月时累加31
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    num += 31;
                    break;
//          当月为二月时闰年累加29，平年累加28
                case 2:
                    num += (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) ? 29 : 28;
                    break;
                default:
                    num += 30;
                    break;
            }
        }
//      加上日
        num += day;
        return num;
    }

}
