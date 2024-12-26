package com.redjujubetree.example.model.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author kenny
 * @since 2024-12-23
 */
@Getter
@Setter
public class Users {

    private Long id;

    private String username;

    @Version
    private Integer version;

    private Date birthday;

    private Integer sex;

    private Date createTime;

    private Date updateTime;

    @TableLogic
    private Boolean deleted;
}
