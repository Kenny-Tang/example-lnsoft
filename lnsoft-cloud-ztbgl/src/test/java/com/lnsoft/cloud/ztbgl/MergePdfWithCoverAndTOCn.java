package com.lnsoft.cloud.ztbgl;

import cn.hutool.core.collection.ListUtil;
import com.lnsoft.cloud.ztbgl.common.constant.FileTypes;
import com.lnsoft.cloud.ztbgl.utils.pdf.FileEntry;
import com.lnsoft.cloud.ztbgl.utils.pdf.TocItem;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDBorderStyleDictionary;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageFitDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class MergePdfWithCoverAndTOCn {
    // 配置路径
    static String fontPath = "/Users/kenny/IdeaProjects/example/lnsoft-cloud-ztbgl/src/main/resources/font/NotoSansSC.ttf";
    static String inputDirPath = "/Users/kenny/IdeaProjects/example/lnsoft-cloud-ztbgl/merge-pdf-files/";
    static String outputPath = "merged.pdf";
    static int LINES_PER_PAGE_DEFAULT = 26; // 大概：从 y=720 到 y=50，行距25

    public static void main(String[] args) throws IOException {


        List<FileEntry> catalogEntries = CatalogBuilder.buildCatalog(CatalogBuilder.files, FileTypes.PDF);

        List<TocItem> tocItems = new ArrayList<>();
        int currentPageIndex = 0;

        PDFMergerUtility merger = new PDFMergerUtility();
        ByteArrayOutputStream contentBuffer = new ByteArrayOutputStream();
        merger.setDestinationStream(contentBuffer);
        Deque<FileEntry> stack = new ArrayDeque<>();
        for (int i = catalogEntries.size() - 1; i >= 0; i--) {
            stack.addFirst(catalogEntries.get(i));
        }
        Map<Integer, TocItem> tocItemMap = new HashMap<>();
        while (!stack.isEmpty()) {
            FileEntry entry = stack.pollFirst();
            merger.addSource(entry.getFile());
//            String markedName = entry.getIndex() + "_" + entry.getLevel() + "_" + entry.getFileName().replace(".pdf", "");
//            TocItem e = new TocItem(markedName, entry.getLevel(), currentPageIndex, entry.order);
//            tocItemMap.put(e.getOrder(), e);
//            if (entry.getParent() != null) {
//                e.setParent(tocItemMap.get(entry.getParent().getOrder()));
//            }
//            tocItems.add(e);
//            try (PDDocument tmp = PDDocument.load(entry.getFile())) {
//                currentPageIndex += tmp.getNumberOfPages();
//            }
//            List<CatalogBuilder.PdfFileEntry> subEntries = entry.getSubEntries();
//            for (int i = subEntries.size() - 1; i >= 0; i--) {
//                stack.addFirst(subEntries.get(i));
//            }
        }
        long start = System.currentTimeMillis();
        merger.mergeDocuments(null);

        generateDoc(contentBuffer, tocItems);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println("✅ 合并完成，输出路径：" + outputPath);
    }

    private static void generateDoc(ByteArrayOutputStream contentBuffer, List<TocItem> tocItems) throws IOException {
        // 加载正文
        ByteArrayInputStream contentInput = new ByteArrayInputStream(contentBuffer.toByteArray());
        PDDocument contentDoc = PDDocument.load(contentInput);

        // 2. 创建最终PDF，加载字体
        PDDocument finalDoc = new PDDocument();
        PDType0Font finalFont = PDType0Font.load(finalDoc, new File(fontPath));


        // 3. 添加封面页
        File coverFile = new File(inputDirPath + "封面" + ".pdf");
        PDDocument coverDoc = PDDocument.load(coverFile); // ← 替换路径
        finalDoc.importPage(coverDoc.getPage(0));

        // 4. 添加目录页
        int contentOffSet = 1;
        int catalogPageCount = getCatalogPageCount(tocItems);// 计算目录页数
        contentOffSet += catalogPageCount; // 偏移封面和目录页

        List<PDPage> tocPages = new ArrayList<>();
        for (int i = 0; i < catalogPageCount; i++) {
            PDPage tocPage = new PDPage(PDRectangle.A4);
            tocPages.add(tocPage);
        }

        // 5. 添加正文页
        for (PDPage page : contentDoc.getPages()) {
            finalDoc.addPage(page);
        }

        // 7. 美化目录页
        List<List<TocItem>> partition = ListUtil.partition(tocItems, LINES_PER_PAGE_DEFAULT);

        for (int i = 0; i < tocPages.size(); i++) {
            PDPage pdPage = tocPages.get(i);
            addTableOfContents(finalDoc, pdPage, partition.get(i), i+1, finalFont);
        }

        // 8. 添加书签
        PDDocumentOutline outline = new PDDocumentOutline();
        finalDoc.getDocumentCatalog().setDocumentOutline(outline);

        Map<TocItem, PDOutlineItem> itemMap = new HashMap<>();
        PDOutlineItem coverItem = new PDOutlineItem();
        coverItem.setTitle("封面");

        PDPageFitDestination destCover = new PDPageFitDestination();
        destCover.setPage(finalDoc.getPage(0));
        coverItem.setDestination(destCover);
        outline.addFirst(coverItem);
        for (TocItem item : tocItems) {
            String name = item.title;
            int pageIdx = item.pageIndex + contentOffSet;

            PDOutlineItem bookmark = new PDOutlineItem();
            bookmark.setTitle(name);

            PDPageFitDestination dest = new PDPageFitDestination();
            dest.setPage(finalDoc.getPage(pageIdx));
            bookmark.setDestination(dest);

            itemMap.put(item, bookmark);

            if (item.parent == null) {
                // 一级目录，添加到根 outline
                outline.addLast(bookmark);
            } else {
                // 非一级目录，添加到对应父目录下
                PDOutlineItem parentBookmark = itemMap.get(item.parent);
                if (parentBookmark != null) {
                    parentBookmark.addLast(bookmark);
                } else {
                    // 如果未找到 parentBookmark（理论上不应该发生）
                    outline.addLast(bookmark);
                }
            }
        }

        outline.openNode();


        // 9. 为每页添加页码
        int totalPages = finalDoc.getNumberOfPages();
        for (int i = 1; i < totalPages; i++) {
            PDPage page = finalDoc.getPage(i);
            try (PDPageContentStream contentStream = new PDPageContentStream(finalDoc, page,
                    PDPageContentStream.AppendMode.APPEND, true)) {
                contentStream.beginText();
                contentStream.setFont(finalFont, 10);
                float pageWidth = page.getMediaBox().getWidth();
                contentStream.newLineAtOffset(pageWidth / 2 - 20, 20);
                contentStream.showText("第 " + (i + 1) + " 页");
                contentStream.endText();
            }
        }

        // 10. 保存关闭
        finalDoc.save(outputPath);
        finalDoc.close();
        contentDoc.close();
    }

    private static void addTableOfContents(PDDocument outputDoc, PDPage tocPage, List<TocItem> tocItems, int contentOffSet, PDType0Font font) throws IOException {
        float pageWidth = PDRectangle.A4.getWidth();
        float startY = 720;
        float y = startY;
        float minY = 50;
        float lineHeight = 25;
        float leftMargin = 50;
        float rightMargin = pageWidth - 50;
        float indentSize = 20;

        PDPageContentStream stream = new PDPageContentStream(outputDoc, tocPage);
        y = drawCatalogTitle(stream, font, pageWidth, y);

        for (TocItem item : tocItems) {
            if (y < minY) {
                stream.close();
                tocPage = new PDPage(PDRectangle.A4);
                outputDoc.addPage(tocPage);
                stream = new PDPageContentStream(outputDoc, tocPage);
                y = startY;
            }

            String text = item.title;
            int targetPage = item.pageIndex + 1 + getCatalogPageCount(tocItems); // 封面 + 目录页数量
            String pageStr = "第 " + (targetPage + 1) + " 页";

            float titleWidth = font.getStringWidth(text) / 1000 * 14;
            float pageNumWidth = font.getStringWidth(pageStr) / 1000 * 14;
            float indentX = leftMargin + indentSize * item.getLevel();

            // 显示目录标题
            stream.setFont(font, 14);
            stream.setNonStrokingColor(0, 0, 0);
            stream.beginText();
            stream.newLineAtOffset(indentX, y);
            stream.showText(text);
            stream.endText();

            // 显示页码
            stream.beginText();
            stream.newLineAtOffset(rightMargin - pageNumWidth, y);
            stream.showText(pageStr);
            stream.endText();

            // 点线
            float dotStartX = indentX + titleWidth + 5;
            float dotEndX = rightMargin - pageNumWidth - 5;
            float dotY = y + 4;
            stream.setStrokingColor(100, 100, 100);
            float dotGap = 4;
            for (float x = dotStartX; x < dotEndX; x += dotGap) {
                stream.moveTo(x, dotY);
                stream.lineTo(x + 1.5f, dotY);
            }
            stream.stroke();

            // 链接
            PDAnnotationLink link = new PDAnnotationLink();
            PDRectangle linkBounds = new PDRectangle(indentX, y - 4, rightMargin - indentX, 14);
            link.setRectangle(linkBounds);
            link.setBorderStyle(new PDBorderStyleDictionary());
            PDPageFitDestination dest = new PDPageFitDestination();
            dest.setPage(outputDoc.getPage(targetPage));
            PDActionGoTo action = new PDActionGoTo();
            action.setDestination(dest);
            link.setAction(action);
            tocPage.getAnnotations().add(link);

            y -= lineHeight;
        }
        stream.close();
    }


    static int getCatalogPageCount(List<TocItem> tocItems) {
        return (tocItems.size() + LINES_PER_PAGE_DEFAULT - 1) / LINES_PER_PAGE_DEFAULT;
    }

    private static float drawCatalogTitle(PDPageContentStream stream, PDType0Font font, float pageWidth, float y) throws IOException {
        stream.setNonStrokingColor(0, 51, 102);
        stream.setFont(font, 24);
        stream.beginText();
        String tocTitle = "目录";
        float titleWidth = font.getStringWidth(tocTitle) / 1000 * 24;
        stream.newLineAtOffset((pageWidth - titleWidth) / 2, y);
        stream.showText(tocTitle);
        stream.endText();
        return y - 40;
    }
}
