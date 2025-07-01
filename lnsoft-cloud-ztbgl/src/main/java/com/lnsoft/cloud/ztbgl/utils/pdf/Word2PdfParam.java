package com.lnsoft.cloud.ztbgl.utils.pdf;

import lombok.Data;

@Data
public class Word2PdfParam {
    private String inputFile;
    private String outputFile;
    private String bookmarkPath;
}
