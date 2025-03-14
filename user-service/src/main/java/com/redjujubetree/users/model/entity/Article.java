package com.redjujubetree.users.model.entity;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author tanjianwei
 * @since 2025-02-27
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
    private String name;

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

    private Long createTime;

    private Long updateTime;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Article{" +
            "id = " + id +
            ", path = " + path +
            ", name = " + name +
            ", url = " + url +
            ", icon = " + icon +
            ", component = " + component +
            ", createTime = " + createTime +
            ", updateTime = " + updateTime +
        "}";
    }
}
