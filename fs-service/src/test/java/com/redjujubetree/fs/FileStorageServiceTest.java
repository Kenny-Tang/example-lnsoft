package com.redjujubetree.fs;

import cn.hutool.core.lang.UUID;
import com.redjujubetree.SystemClock;
import com.redjujubetree.fs.domain.entity.FileStorage;
import com.redjujubetree.fs.mapper.FileStorageMapper;
import com.redjujubetree.fs.service.FileStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.*;

@SpringBootTest
public class FileStorageServiceTest {
	@Resource
	private FileStorageService fileStorageService;
	@Resource
	private FileStorageMapper fileStorageMapper;

	@Test
	public void test() throws FileNotFoundException {
		FileStorage fileStorage = new FileStorage();
		fileStorage.setId(SystemClock.currentTimeMillis());
		fileStorage.setKey(UUID.fastUUID().toString());
		File file = new File("/Users/kenny/IdeaProjects/example/fs-service/src/main/resources/7bc13c26c025478c97f127789f6b9092.png");
		InputStream inputStream = new FileInputStream(file);
		byte[] bytes = new byte[(int) file.length()];
		try {
			inputStream.read(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		fileStorage.setData(bytes);
		fileStorageService.saveFileStorage(fileStorage);
	}

	@Test
	public void testSelect() throws FileNotFoundException {
		FileStorage fileStorage = fileStorageMapper.getById(1749193532417L);
		System.out.println(fileStorage);
		File file = new File("sos"+".png");
		if (!file.exists()) {
			FileOutputStream fs = new FileOutputStream(file);
			try {
				fs.write(fileStorage.getData());
				System.out.println(file.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
