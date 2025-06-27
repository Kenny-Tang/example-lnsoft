package com.lnsoft.cloud.ztbgl.utils.pdf;

import lombok.Data;

import java.io.File;

@Data
public class PdfFileEntry implements Comparable<PdfFileEntry> {
    private File file;
    private Integer order;

    public PdfFileEntry(File pdfFile, int order) {
        file = pdfFile;
        this.order = order;
    }
    public String getFileName() {
        return file.getName();
    }
    public String getIndex() {
        return String.format("%03d", order);
    }



    @Override
    public int compareTo(PdfFileEntry o) {
        if (this.order < o.getOrder()) {
            return -1;
        }
        return 1;
    }

}