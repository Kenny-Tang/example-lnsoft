package com.lnsoft.cloud.ztbgl;

import com.lnsoft.cloud.ztbgl.common.constant.FileTypes;
import com.lnsoft.cloud.ztbgl.utils.pdf.FileEntry;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.Section;
import com.spire.doc.documents.BreakType;
import com.spire.doc.documents.Paragraph;

import java.util.Arrays;
import java.util.List;

public class WordMergeWithBookmarks {

    public static void main(String[] args) {
        List<String> files = Arrays.asList(
                "/Users/kenny/Documents/鲁软/技术/技术/封面.docx",
                "/Users/kenny/Documents/鲁软/技术/技术/设备设施.docx",
                "/Users/kenny/Documents/鲁软/技术/技术/公司厂房.docx",
                "/Users/kenny/Documents/鲁软/技术/技术/项目团队情况.docx");

        List<FileEntry> catalogEntries = CatalogBuilder.buildCatalog(CatalogBuilder.files, FileTypes.PDF);

        // 创建一个主文档
        Document mainDoc = new Document();
        mainDoc.loadFromFile(files.get(0));

        // 合并后续文档
        for (int i = 1; i < files.size(); i++) {
            Document partDoc = new Document();
            partDoc.loadFromFile(files.get(i));
            // 在合并前插入分页符（可选）
            Section lastSection = mainDoc.getLastSection();
            Paragraph para = lastSection.addParagraph();
            para.appendBreak(BreakType.Page_Break);
            // 合并内容（包括格式、段落等）
            for (Section section : (Iterable<Section>) partDoc.getSections()) {
                mainDoc.getSections().add(section.deepClone());
            }
        }

        // 自动识别标题作为 PDF 书签（必须是 Word 标题样式，如 Heading 1/2）

        // 导出 PDF 并附带结构书签
        mainDoc.saveToFile(System.currentTimeMillis()+"merged_with_outline.docx", FileFormat.Docx);
    }
}
