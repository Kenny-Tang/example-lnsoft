package com.lnsoft.cloud.ztbgl;

import com.lnsoft.cloud.ztbgl.common.constant.FileTypes;
import com.lnsoft.cloud.ztbgl.utils.ZtbPdfMergeService;
import com.lnsoft.cloud.ztbgl.utils.pdf.FileEntry;
import com.lnsoft.cloud.ztbgl.utils.pdf.Word2PdfParam;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

public class ZtbPdfMergeServiceTest {

	@Test
	public void testGenerateOutline(){
		ZtbPdfMergeService ztbPdfMergeService = new ZtbPdfMergeService("/Users/kenny/IdeaProjects/example/lnsoft-cloud-ztbgl/src/main/resources/font/NotoSansSC.ttf");

		Word2PdfParam param = new Word2PdfParam();
		List<FileEntry> catalogEntries = CatalogBuilder.buildCatalog(CatalogBuilder.files, FileTypes.DOCX);
		for (FileEntry catalogEntry : catalogEntries) {
			param.setInputFile(catalogEntry.getFile().getAbsolutePath());
			File outputFile = new File(param.getInputFile().substring(0, param.getInputFile().length() - 5) + ".pdf");
			if (outputFile.exists()) {
				outputFile.delete();
			}
			param.setOutputFile(outputFile.getAbsolutePath());

			long l = System.currentTimeMillis() % 10;
			if (l > 5) {
				param.setBookmarkPath("一级目录");
			} else if (l > 3) {
				param.setBookmarkPath("一级目录/二级目录");
			} else {
				param.setBookmarkPath("一级目录/二级目录/三级目录");
			}
			ztbPdfMergeService.word2pf(param);
		}

	}

	@Test
	public void testMergePdf() {
		List<FileEntry> catalogEntries = CatalogBuilder.buildCatalog(CatalogBuilder.files, FileTypes.PDF);
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
