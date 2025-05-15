package com.redjujubetree;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PdfInvoiceParser {
	Logger logger = LoggerFactory.getLogger(this.getClass());
//	public static void main(String[] args) {
//		PdfInvoiceParser fapiao1 = new PdfInvoiceParser();
//		fapiao1.getInvoiceQrcodeInfo("/Users/kenny/Documents/鲁软/差旅/0422/海朵拉酒店发票.pdf");
//	}

	public InvoiceInfo getInvoiceQrcodeInfo(String filePath) {
		return getInvoiceQrcodeInfo(new File(filePath));
	}

	/**
	 * 获取电子发票pdf文件中的发票信息
	 *
	 * @param file 电子发票路径
	 * @return 发票信息
	 */
	public InvoiceInfo getInvoiceQrcodeInfo(File file) {
		try {
			List<BufferedImage> imageList = extractImage(file);
			if (imageList.isEmpty()) {
				logger.info("pdf中未解析出图片，返回空");
				return null;
			}
			for (BufferedImage bufferedImage : imageList) {
				MultiFormatReader formatReader = new MultiFormatReader();
				//正常解析出来有3张图片，第一张是二维码，其他两张图片是发票上盖的章
				BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
				Map hints = new HashMap<>();
				hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
				Result result = formatReader.decode(binaryBitmap);
				if (result == null) {
					logger.info("pdf中的第一张图片没有解析了字符串信息，直接返回空");
					return null;
				}

				logger.info("从电子发票中识别出的信息为：{}", result.getText());

				// 读取到的信息为 ： 01，发票类型，发票代码，发票号码，发票金额，开票日期，校验码，随机产生的摘要信息
				String[] infos = result.getText().split(",");
				if (infos.length != 8) {
					logger.info("pdf中的第一张图片解析出的字符串数组长度不为8，返回空。");
					return null;
				}

				InvoiceQrcodeInfo invoice = new InvoiceQrcodeInfo();
				invoice.setInvoiceType(infos[1]); //发票类型
				invoice.setInvoiceCode(infos[2]); //发票代码
				invoice.setInvoiceNo(infos[3]); // 发票号码
				invoice.setInvoiceAmount(infos[4]); // 发票金额
				invoice.setInvoiceDate(infos[5]); //开票日期
				invoice.setCheckCode(infos[6]); // 校验码*/
				return invoice;
			}
		} catch (Exception e) {
			logger.warn("解析pdf中的二维码出现异常", e);
		}
		return null;
	}


	/**
	 * 提取电子发票里面的图片
	 *
	 * @param pdfFile 电子发票文件对象
	 * @return pdf中解析出的图片列表
	 * @throws Exception
	 */
	private List<BufferedImage> extractImage(File pdfFile) throws Exception {
		List<BufferedImage> imageList = new ArrayList<BufferedImage>();

		PDDocument document = PDDocument.load(pdfFile);
		PDPage page = document.getPage(0); //电子发票只有一页
		PDResources resources = page.getResources();

		for (COSName name : resources.getXObjectNames()) {
			if (resources.isImageXObject(name)) {
				PDImageXObject obj = (PDImageXObject) resources.getXObject(name);
				imageList.add(obj.getImage());
			}
		}
		document.close();
		return imageList;
	}
}
