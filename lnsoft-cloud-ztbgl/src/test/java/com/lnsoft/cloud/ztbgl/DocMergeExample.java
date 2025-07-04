package com.lnsoft.cloud.ztbgl;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.Section;
import com.spire.doc.documents.BreakType;
import com.spire.doc.documents.Paragraph;

import java.io.File;

public class DocMergeExample {

    public static Document mergeDocuments(File[] fileList) throws Exception {
        if (fileList == null || fileList.length == 0) {
            throw new IllegalArgumentException("文件列表不能为空");
        }

        // 用第一个文档初始化
        Document baseDoc = new Document();
        baseDoc.loadFromFile(fileList[0].getAbsolutePath());

        // 从第二个开始依次合并
        for (int i = 1; i < fileList.length; i++) {
            Document nextDoc = new Document();
            nextDoc.loadFromFile(fileList[i].getAbsolutePath());

            // 在合并前插入分页符（可选）
            Section lastSection = baseDoc.getLastSection();
            Paragraph para = lastSection.addParagraph();
            para.appendBreak(BreakType.Page_Break);

            // 合并内容（包括格式、段落等）
            for (Section section : (Iterable<Section>) nextDoc.getSections()) {
                baseDoc.getSections().add(section.deepClone());
            }
        }

        return baseDoc;
    }

    public static void main(String[] args) throws Exception {
        File[] files = new File[] {
                new File("doc1.docx"),
                new File("doc2.docx"),
                new File("doc3.docx")
        };

        Document merged = mergeDocuments(files);
        merged.saveToFile("Merged.docx", FileFormat.Docx);
        System.out.println("✅ 合并完成：Merged.docx");
    }
}
