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
public class Dynamic {
    private String dynamicId;
    private Date changeTime;
    private String discription;
    private String originStatus;
    private String newStatus;
    private String targetUser;
    private String handleUser;
    private int recordType;
    private Date createTime;//生成时间
}
