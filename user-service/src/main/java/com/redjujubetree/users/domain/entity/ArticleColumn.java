package com.redjujubetree.users.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 文章与专栏的关联关系
 * </p>
 *
 * @author tanjianwei
 * @since 2025-05-15
 */
@TableName("article_column")
public class ArticleColumn implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 专栏ID
     */
    private Long columnId;

    private Date createTime;

    private Date updateTime;

    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
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

    @Override
    public String toString() {
        return "ArticleColumn{" +
            "id = " + id +
            ", articleId = " + articleId +
            ", columnId = " + columnId +
            ", createTime = " + createTime +
            ", updateTime = " + updateTime +
            ", version = " + version +
        "}";
    }
}
