package com.lnsoft.cloud.ztbgl.utils.pdf;

import com.lnsoft.cloud.ztbgl.common.constant.Constants;
import lombok.Data;
import org.apache.pdfbox.pdmodel.PDPage;

import java.util.Objects;

@Data
public  class TocItem {
    public String title;
    public int pageIndex; // 页码
    public TocItem parent;
    private PDPage page;

    public TocItem() {}

    public TocItem(String title, TocItem parent) {
        this.title = title;
        this.parent = parent;
    }

    public Integer getLevel() {
        if (Objects.isNull(parent)) {
            return 0; // 如果没有父级，则返回0
        }
        return 1 + parent.getLevel(); // 默认返回0，实际应用中可以根据需要调整
    }

    public String key() {
        if (Objects.isNull(parent)) {
            return title;
        }
        return parent.key() + Constants.FILE_SEPARATOR_UNIX + title; // 返回完整路径
    }
}