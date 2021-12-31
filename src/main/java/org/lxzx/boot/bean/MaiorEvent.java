package org.lxzx.boot.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MaiorEvent {

    private String eventId;
    private Date eventDate;
    private String adress;
    private String content;
    private String title;
    private boolean isLock; // 是否锁定
    private String fileURL; // 文件路径
}
