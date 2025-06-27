package com.lnsoft.cloud.ztbgl.utils;

import cn.hutool.core.collection.ListUtil;
import com.lnsoft.cloud.ztbgl.utils.pdf.MergeConfig;
import com.lnsoft.cloud.ztbgl.utils.pdf.PageNumberFormatter;
import com.lnsoft.cloud.ztbgl.utils.pdf.PdfFileEntry;
import com.lnsoft.cloud.ztbgl.utils.pdf.TocItem;
import lombok.Data;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDBorderStyleDictionary;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageFitDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class ZtbPdfMergeService {

	private MergeConfig config;
	private PageNumberFormatter formatter = pageIndex -> String.format("第 %s 页", pageIndex);
	public ZtbPdfMergeService(String fontPath) {
		this.config = new ZtbPdfMergeConfig(fontPath);
	}

	public void wordToPdf(File docx) {

	}

	public void merge(List<PdfFileEntry> entries, File file) {
		this.merge(entries, file, true);
	}

	public void merge(List<PdfFileEntry> entries, File file, Boolean overwrite) {
		if (file.exists() && !overwrite) {
			throw new RuntimeException("The file already exists. Please set overwrite to true or delete the existing file manually. File: " + file.getAbsolutePath());
		}
		if (file.exists()) {
			if (!file.delete()) {
				throw new RuntimeException("Unable to delete the file. Please ensure it is not currently in use. File: " + file.getAbsolutePath());
			}
		}
		try (ByteArrayOutputStream destStream = new ByteArrayOutputStream()) {
			entries.sort(Comparator.naturalOrder());
			PDFMergerUtility mergerUtility = new PDFMergerUtility();
			mergerUtility.setDestinationStream(destStream);

			for (PdfFileEntry entry : entries) {
				mergerUtility.addSource(entry.getFile());
			}
			mergerUtility.mergeDocuments();
			try (PDDocument outputDocument = PDDocument.load(new ByteArrayInputStream(destStream.toByteArray()))) {
				PDDocumentOutline outline = outputDocument.getDocumentCatalog().getDocumentOutline();
				if (outline == null) {
					throw new RuntimeException("No outline/bookmarks found in PDF.");
				}

				List<TocItem> tocItems = generateTocItems(outline.getFirstChild().getNextSibling(), outputDocument, null);
				List<List<TocItem>> partition = ListUtil.partition(tocItems, config.getTocCountPerPage());
				int tocOffset = 0;
				partition.forEach(t -> {
					PDPage prevPage = outputDocument.getPages().get(tocOffset);
					PDRectangle targetSize = prevPage.getMediaBox();
					outputDocument.getPages().insertAfter(new PDPage(targetSize), prevPage);
				});
				PDType0Font font = PDType0Font.load(outputDocument, new File(config.getFontPath()));
				for (int i = 0; i < partition.size(); i++) {
					PDPage tocPage = outputDocument.getPages().get(tocOffset + i + 1);
					addTableOfContents(outputDocument, tocPage, partition.get(i), font,i == 0);
				}

				outline = new PDDocumentOutline();
				outputDocument.getDocumentCatalog().setDocumentOutline(outline);

				Map<TocItem, PDOutlineItem> itemMap = new HashMap<>();
				PDOutlineItem coverItem = new PDOutlineItem();
				coverItem.setTitle("封面");

				PDPageFitDestination destCover = new PDPageFitDestination();
				destCover.setPage(outputDocument.getPage(0));
				coverItem.setDestination(destCover);
				outline.addFirst(coverItem);
				for (TocItem item : tocItems) {
					PDOutlineItem bookmark = new PDOutlineItem();
					bookmark.setTitle(item.getTitle());

					PDPageFitDestination dest = new PDPageFitDestination();
					dest.setPage(item.getPage());
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
				addPageNumber(outputDocument, font);
				outputDocument.save(file);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void addPageNumber(PDDocument outputDocument, PDType0Font font) throws IOException {
		int totalPages = outputDocument.getNumberOfPages();
		for (int i = 1; i < totalPages; i++) {
			PDPage page = outputDocument.getPage(i);
			try (PDPageContentStream contentStream = new PDPageContentStream(outputDocument, page,
					PDPageContentStream.AppendMode.APPEND, true)) {
				contentStream.beginText();
				contentStream.setFont(font, 10);
				float pageWidth = page.getMediaBox().getWidth();
				contentStream.newLineAtOffset(pageWidth / 2 - 20, 20);
				contentStream.showText(formatter.format(i + 1));
				contentStream.endText();
			}
		}
	}

	private static List<TocItem> generateTocItems(PDOutlineItem outline, PDDocument outputDocument, TocItem parent) throws IOException {
		List<TocItem> tocItems = new ArrayList<>();
		Map<String, TocItem> tocItemMap = new HashMap<>();
		Deque<Object[]> stack = new ArrayDeque<>();
		Object[] current = new Object[]{outline, parent};
		stack.push(current);
		while (!stack.isEmpty()) {
			Object[] element = stack.pop();
			PDOutlineItem currOutlineItem = (PDOutlineItem) element[0];
			TocItem parentItem = (TocItem) element[1];

			PDPageDestination dest = null;
			if (currOutlineItem.getDestination() instanceof PDDestination) {
				dest = (PDPageDestination) currOutlineItem.getDestination();
			} else if (currOutlineItem.getAction() instanceof PDActionGoTo) {
				dest = (PDPageDestination) ((PDActionGoTo) currOutlineItem.getAction()).getDestination();
			}
			int pageIndex = -1;
			TocItem tocItem = new TocItem(currOutlineItem.getTitle(), parentItem);
			if (dest != null) {
				PDPage page = dest.getPage();
				// 如果 getPage 返回非 null，可以直接用
				if (page != null) {
					pageIndex = outputDocument.getPages().indexOf(page) + 1;
					tocItem.setPage(page);
				} else {
					// 否则 fallback：尝试 getPageNumber()
					int fallbackPageNum = dest.getPageNumber();
					if (fallbackPageNum >= 0 && fallbackPageNum < outputDocument.getNumberOfPages()) {
						pageIndex = fallbackPageNum + 1;
					}
				}
				tocItem.setPageIndex(pageIndex);
			}

			if (tocItemMap.containsKey(tocItem.key())) {
				tocItem = tocItemMap.get(tocItem.key());
			} else {
				tocItems.add(tocItem);
				tocItemMap.put(tocItem.key(), tocItem);
			}

			if (currOutlineItem.getNextSibling() != null) {
				stack.push(new Object[]{currOutlineItem.getNextSibling(), parentItem});
			}

			if (currOutlineItem.getFirstChild() != null) {
				stack.push(new Object[]{currOutlineItem.getFirstChild(), tocItem});
			}
		}
		return tocItems;
	}

	private void addTableOfContents(PDDocument outputDoc, PDPage tocPage, List<TocItem> tocItems, PDType0Font font, Boolean headToc) throws IOException {
		float pageWidth = PDRectangle.A4.getWidth();
		float startY = 770;
		float y = startY;
		float minY = 60;
		float lineHeight = 25;
		float leftMargin = 50;
		float rightMargin = pageWidth - 50;
		float indentSize = 20;

		PDPageContentStream stream = new PDPageContentStream(outputDoc, tocPage);
		if (headToc) {
			y = drawCatalogTitle(stream, font, pageWidth, y);
		}

		for (TocItem item : tocItems) {
			if (y < minY) {
				stream.close();
				tocPage = new PDPage(PDRectangle.A4);
				outputDoc.addPage(tocPage);
				stream = new PDPageContentStream(outputDoc, tocPage);
				y = startY;
			}

			int targetPage = item.pageIndex;
			if (Objects.nonNull(item.getPage())) {
				targetPage = outputDoc.getPages().indexOf(item.getPage()) + 1;
			}
			String pageStr = formatter.format(targetPage);
			String text = item.title;
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
			dest.setPage(outputDoc.getPage(targetPage - 1));
			PDActionGoTo action = new PDActionGoTo();
			action.setDestination(dest);
			link.setAction(action);
			tocPage.getAnnotations().add(link);

			y -= lineHeight;
		}
		stream.close();
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

	@Data
	public class ZtbPdfMergeConfig implements MergeConfig {
		private String fontPath ;
		private int tocCountPerPage = 26;

		public ZtbPdfMergeConfig(String fontPath) {
			this.fontPath = fontPath;
		}

	}

	public MergeConfig getConfig() {
		return config;
	}

	public void setConfig(MergeConfig config) {
		this.config = config;
	}

	public PageNumberFormatter getFormatter() {
		return formatter;
	}

	public void setFormatter(PageNumberFormatter formatter) {
		this.formatter = formatter;
	}
}
