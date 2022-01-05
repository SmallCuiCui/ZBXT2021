package org.lxzx.boot.controller;

import org.lxzx.boot.Utils.Festival;
import org.lxzx.boot.dto.Result;
import org.lxzx.boot.enums.DepartmentEnum;
import org.lxzx.boot.enums.LimitedEnum;
import org.lxzx.boot.enums.PositionEnum;
import org.lxzx.boot.enums.ResultCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/common")
public class CommonController {

    @RequestMapping(value = "/getDepartment", method = RequestMethod.GET)
    public Result getDepartMentList() {
        return Result.ok().message("请求成功").data(DepartmentEnum.getAllEnum());
    }

    @RequestMapping(value = "/getLimited", method = RequestMethod.GET)
    public Result getLimitedList() {
        return Result.ok().message("请求成功").data(LimitedEnum.getAllEnum());
    }

    @RequestMapping(value = "/getPosition", method = RequestMethod.GET)
    public Result getPositionList() {
        return Result.ok().message("请求成功").data(PositionEnum.getAllEnum());
    }

//    文件上传
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            return Result.error().message("上传失败，请选择文件");
        }
        String fileName = file.getOriginalFilename();
        String fileLocation = "/" + Festival.getDate(new Date()) +"/";  //  图片资源访问路径,取当天日期
        File myFilePath  = new File(fileLocation);
        if(!myFilePath .exists()) {
            myFilePath .mkdir();
        }
        String fileFullName = fileLocation + fileName;
        File dest = new File(fileFullName);
//        String filePath = request.getScheme() + "://" + request.getServerName()
//                + ":" + request.getServerPort() //端口 https443端口无需添加
//                + fileLocation + fileName;
        try {
            file.transferTo(dest);
            Map map = new HashMap();
            map.put("fileName", fileName);
            map.put("filePath", fileFullName);
            return Result.ok().data(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.error(ResultCode.CREDENTIALS_EXPIRED);
    }

//    文件下载
    @GetMapping("/download")
    public Result downloadFile(HttpServletResponse response, @RequestParam("fileFullName") String fileFullName) throws UnsupportedEncodingException {
//        String rootPath = propertiesconfig.getUploadpacketPath();//这里是我在配置文件里面配置的根路径，各位可以更换成自己的路径之后再使用（例如：D：/test）
        String FullPath = "c:/uploadFiles" + fileFullName;//将文件的统一储存路径和文件名拼接得到文件全路径
        File packetFile = new File(FullPath);
        String fileName = packetFile.getName(); //下载的文件名
        File file = new File(FullPath);
        // 如果文件名存在，则进行下载
        if (file.exists()) {
            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            // 实现文件下载
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {//对应文件不存在
            try {
                //设置响应的数据类型是html文本，并且告知浏览器，使用UTF-8 来编码。
                response.setContentType("text/html;charset=UTF-8");
                //String这个类里面， getBytes()方法使用的码表，是UTF-8，  跟tomcat的默认码表没关系。 tomcat iso-8859-1
                String csn = Charset.defaultCharset().name();
                //1. 指定浏览器看这份数据使用的码表
                response.setHeader("Content-Type", "text/html;charset=UTF-8");
                OutputStream os = response.getOutputStream();
                os.write("对应文件不存在".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
        }
    }
