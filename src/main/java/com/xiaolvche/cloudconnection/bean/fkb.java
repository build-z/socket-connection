package com.xiaolvche.cloudconnection.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author liaoxh
 * @create 2021-07-24-8:20
 */
@TableName("fkb")
@Data
public class fkb {
    Integer id;
    String username;
    String password;
    String zid;
    Integer accpetnum;
    Integer status;
}
