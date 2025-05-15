package com.redjujubetree.users.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author tanjianwei
 * @since 2025-05-07
 */
@TableName("user_info")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;

    private String usernameZh;

    private Long id;

    private String password;

    private Boolean delFlag;

    private Integer version;

    private Date createTime;

    private Date updateTime;

    private String virMac;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsernameZh() {
        return usernameZh;
    }

    public void setUsernameZh(String usernameZh) {
        this.usernameZh = usernameZh;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getVirMac() {
        return virMac;
    }

    public void setVirMac(String virMac) {
        this.virMac = virMac;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
            "username = " + username +
            ", usernameZh = " + usernameZh +
            ", id = " + id +
            ", password = " + password +
            ", delFlag = " + delFlag +
            ", version = " + version +
            ", createTime = " + createTime +
            ", updateTime = " + updateTime +
            ", virMac = " + virMac +
        "}";
    }
}
