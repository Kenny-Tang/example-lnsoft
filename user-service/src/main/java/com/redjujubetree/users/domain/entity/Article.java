package com.redjujubetree.users.domain.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author tanjianwei
 * @since 2025-05-31
 */
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 文章名称
     */
    private String title;

    /**
     * 文章存放位置
     */
    private String url;

    /**
     * 文章显示图标
     */
    private String icon;

    /**
     * 文章显示组件
     */
    private String component;

    private Date createTime;

    private Date updateTime;

    private Integer version;

    /**
     * 展示次序
     */
    private Integer displayOrder;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 删除标志 0 未删除 1 已删除
     */
    private Boolean delFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Boolean getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return "Article{" +
            "id = " + id +
            ", path = " + path +
            ", title = " + title +
            ", url = " + url +
            ", icon = " + icon +
            ", component = " + component +
            ", createTime = " + createTime +
            ", updateTime = " + updateTime +
            ", version = " + version +
            ", displayOrder = " + displayOrder +
            ", summary = " + summary +
            ", delFlag = " + delFlag +
        "}";
    }
}
