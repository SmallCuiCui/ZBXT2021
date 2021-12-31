package org.lxzx.boot.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class Festival {
    private final String FILE_NAME = "holiday.xls";
    private List<Date> festival = new ArrayList<Date>();// 节假日
    private List<String> festivalName = new ArrayList<String>();// 节假日对应名称
    private List<Date> workDay = new ArrayList<Date>();// 工作日

    public Festival() {
        File excel = this.getExcel();
        try {
            FileInputStream fin = new FileInputStream(excel);
            HSSFWorkbook hssfworkbook = new HSSFWorkbook(fin);
            HSSFSheet sheet = hssfworkbook.getSheetAt(0);
            int last = sheet.getLastRowNum();
            int index = 2;// 从第2行开始读取
            Date dt = null;
            String dtName = "";
            while (index <= last) {
                HSSFRow row = sheet.getRow(index);

                /* 读取法定节假日、节假日对应名称 */
                HSSFCell cell = row.getCell((short) 1);
                HSSFCell cellName = row.getCell((short) 2);
                if (cell != null) {
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        dt = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
                        dtName = cellName.getStringCellValue().trim();
                        if (dt != null && dt.getTime() > 0) {
                            this.festival.add(dt);
                            this.festivalName.add(dtName);
                        }
                    }
                }

                /* 读取特殊工作日 */
                cell = row.getCell((short) 3);
                if (cell != null) {
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        dt = HSSFDateUtil.getJavaDate(cell
                                .getNumericCellValue());

                        if (dt != null && dt.getTime() > 0) {
                            this.workDay.add(dt);
                        }
                    }
                }

                index++;
            }
            fin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getExcel() {
        File excel = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource("static/" + FILE_NAME);
            excel = classPathResource.getFile();
            return excel;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return excel;
    }

    public List getFestival() {
        return this.festival;
    }
    public List getFestivalName() {
        return this.festivalName;
    }

    public List getSpecialWorkDay() {
        return this.workDay;
    }

    /**
     * 判断一个日期是否日节假日 法定节假日只判断月份和天，不判断年
     */
    public Map getFestival(Date date) {
        Map<String, Object> map = new HashMap<>();
        boolean festival = false;
        Calendar fcal = Calendar.getInstance();
        Calendar dcal = Calendar.getInstance();
        dcal.setTime(date);
        List<Date> list = this.getFestival();
        List<Date> listName = this.getFestivalName();
        for (int i = 0; i< list.size(); i++) {
            Date dt = list.get(i);
            fcal.setTime(dt);
            // 法定节假日判断
            if (fcal.get(Calendar.MONTH) == dcal.get(Calendar.MONTH)
                    && fcal.get(Calendar.DATE) == dcal.get(Calendar.DATE)) {
                festival = true;
                map.put("festivalName", listName.get(i));
                break;
            }
        }
        map.put("festival", festival);
        return map;
    }

    public boolean isFestival(Date date) {
        Map<String, Object> map = new HashMap<>();
        boolean festival = false;
        Calendar fcal = Calendar.getInstance();
        Calendar dcal = Calendar.getInstance();
        dcal.setTime(date);
        List<Date> list = this.getFestival();
        List<Date> listName = this.getFestivalName();
        for (int i = 0; i< list.size(); i++) {
            Date dt = list.get(i);
            fcal.setTime(dt);
            // 法定节假日判断
            if (fcal.get(Calendar.MONTH) == dcal.get(Calendar.MONTH)
                    && fcal.get(Calendar.DATE) == dcal.get(Calendar.DATE)) {
                festival = true;
                map.put("festival", festival);
                map.put("festivalName", listName.get(i));
            }
        }
        return festival;
    }

    /**
     * 周六周日判断
     *
     * @param date
     * @return
     */
    public boolean isWeekend(Date date) {
        boolean weekend = false;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            weekend = true;
        }
        return weekend;
    }

    /**
     * 是否是工作日（法定节假日、周末为非工作日）
     */
    public boolean isWorkDay(Date date) {
        boolean workday = true;
        if (this.isFestival(date) || this.isWeekend(date)) {
            workday = false;
        }

        /* 特殊工作日判断 */
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        Calendar cal2 = Calendar.getInstance();
        for (Date dt : this.workDay) {
            cal2.setTime(dt);
            if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                    && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
                    && cal1.get(Calendar.DATE) == cal2.get(Calendar.DATE)) { // 年月日相等为特殊工作日
                workday = true;
            }
        }
        return workday;
    }

//    字符串时间获取Date时间
    public static Date getDate(String str) {
        Date dt = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dt = df.parse(str);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dt;

    }

//    Date日期获取字符串时间
    public String getDate(Date date) {
        String dt = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        dt = df.format(date);
        return dt;

    }
//    public static void main(String[] args) {
//        // TODO Auto-generated method stub
//        Date date=new Date();//取时间
//
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        String dateString = formatter.format(date);
//
//        System.out.println(dateString);
//
//        Festival f = new Festival();
//        Date dt = f.getDate(dateString);
//    }

}
