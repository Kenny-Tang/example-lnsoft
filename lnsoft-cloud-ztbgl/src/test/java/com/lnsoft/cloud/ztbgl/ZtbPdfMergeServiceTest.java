package com.lnsoft.cloud.ztbgl;

import com.lnsoft.cloud.ztbgl.utils.ZtbPdfMergeService;
import com.lnsoft.cloud.ztbgl.utils.pdf.PdfFileEntry;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

public class ZtbPdfMergeServiceTest {

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
