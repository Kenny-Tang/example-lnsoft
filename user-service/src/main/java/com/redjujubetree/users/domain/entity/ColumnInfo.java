package com.redjujubetree.users.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 专栏信息
 * </p>
 *
 * @author tanjianwei
 * @since 2025-05-15
 */
@TableName("column_info")
public class ColumnInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Date updateTime;

    private Date createTime;

    private Integer version;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 文章显示图标
     */
    private String icon;

    /**
     * 专栏名称
     */
    private String title;

    /**
     * 展示次序
     */
    private Integer displayOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    @Override
    public String toString() {
        return "ColumnInfo{" +
            "id = " + id +
            ", updateTime = " + updateTime +
            ", createTime = " + createTime +
            ", version = " + version +
            ", path = " + path +
            ", icon = " + icon +
            ", title = " + title +
            ", displayOrder = " + displayOrder +
        "}";
    }
}
