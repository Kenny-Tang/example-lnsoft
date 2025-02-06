package com.redjujubetree.users.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author tanjianwei
 * @since 2025-01-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserInfo implements Serializable {


    private String username;

    @TableField("username_zh")
    private String usernameZh;

    private String id;

    private String password;

    @TableField("del_flag")
    private Boolean delFlag;

    private Integer version;

    @TableField("create_time")
    private LocalDate createTime;

    @TableField("update_time")
    private LocalDate updateTime;


}
