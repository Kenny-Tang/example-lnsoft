package com.redjujubetree.fs;

import com.example.FsClientApplication;
import com.example.client.service.BidDownloadParam;
import com.example.client.service.FileDownloadService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest(
	classes = FsClientApplication.class)
public class FileDownloadServiceTest {

	@Resource
	private FileDownloadService downloadService;

	@Test
	public void testDownloadFile() {
		String fileName = "test_1754026565603_5677eb18.pdf";

		// 执行流式下载
		BidDownloadParam request = new BidDownloadParam();
		request.setFileSize(123456L); // 替换为实际文件大小
		request.setReferencePath("test.mov"); // 替换为实际文件路径
		request.setDocumentStoragePath("/Users/kenny/IdeaProjects/example/temp"); // 替换为实际存储路径
		downloadService.downloadFileStreaming(request);
	}
}
