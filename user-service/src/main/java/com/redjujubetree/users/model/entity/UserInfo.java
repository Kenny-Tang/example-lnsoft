package com.redjujubetree.users.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author tanjianwei
 * @since 2025-02-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserInfo implements Serializable {

    private Long id;
    private String username;

    @TableField("username_zh")
    private String usernameZh;

    private String password;

    @TableField("del_flag")
    private Boolean delFlag;

    private Integer version;

    @TableField("create_time")
    private Long createTime;

    @TableField("update_time")
    private Long updateTime;


}
