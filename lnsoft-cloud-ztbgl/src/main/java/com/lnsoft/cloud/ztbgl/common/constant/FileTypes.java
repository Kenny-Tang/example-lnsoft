package com.lnsoft.cloud.ztbgl.common.constant;

import lombok.Getter;

@Getter
public enum FileTypes {

    PDF("PDF", ".pdf"),
    DOCX("Word_2007", ".docx"),
    DOC("Word_2003", ".doc"),
    ;

    private final String type;
    private final String suffix;
    FileTypes(String type, String fileSuffix) {
        this.type = type;
        this.suffix = fileSuffix;
    }
}
