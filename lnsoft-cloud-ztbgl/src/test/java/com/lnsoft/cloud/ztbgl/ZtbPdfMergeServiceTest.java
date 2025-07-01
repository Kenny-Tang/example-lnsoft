package com.lnsoft.cloud.ztbgl;

import com.lnsoft.cloud.ztbgl.utils.ZtbPdfMergeService;
import com.lnsoft.cloud.ztbgl.utils.pdf.PdfFileEntry;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

public class ZtbPdfMergeServiceTest {

	@Test
	public void testWord2Pdf() {
		ZtbPdfMergeService ztbPdfMergeService = new ZtbPdfMergeService("/Users/kenny/IdeaProjects/example/lnsoft-cloud-ztbgl/src/main/resources/font/NotoSansSC.ttf");
		File catagory = new File("E:\\山东方振新材料有限公司\\");
		File[] files = catagory.listFiles();
		for (File file : files) {
			String absolutePath = file.getAbsolutePath();
			if (!absolutePath.endsWith(".docx")) {
				continue;
			}
			System.out.println(file.getAbsolutePath());
			File outputFile = new File(absolutePath.substring(0, absolutePath.length() - 5) + ".pdf");
			if (outputFile.exists()) {
				outputFile.delete();
			}
			ztbPdfMergeService.word2pf(absolutePath,outputFile.getAbsolutePath());
		}
	}

	@Test
	public void testMergePdf() {
		List<PdfFileEntry> catalogEntries = CatalogBuilder.buildCatalog(CatalogBuilder.files);
		ZtbPdfMergeService ztbPdfMergeService = new ZtbPdfMergeService("/Users/kenny/IdeaProjects/example/lnsoft-cloud-ztbgl/src/main/resources/font/NotoSansSC.ttf");
		File file = new File(System.currentTimeMillis()+"-merged.pdf");
		long start = System.currentTimeMillis();
		ztbPdfMergeService.merge(catalogEntries, file);
		long end = System.currentTimeMillis();
		System.out.println("PDF合并完成，耗时：" + (end - start) + "毫秒");
	}


	@Test
	public void test() {
		String pageStr = String.format("第 %s 页", 1000);
		System.out.println(pageStr);
	}
}
