package com.lnsoft.cloud.ztbgl;

import com.lnsoft.cloud.ztbgl.common.constant.FileTypes;
import com.lnsoft.cloud.ztbgl.utils.pdf.FileEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CatalogBuilder {
    // static String inputDirPath = "/Users/kenny/Documents/鲁软/技术/山东方振新材料有限公司/";
    static String inputDirPath = "E:/山东方振新材料有限公司/";

    static String[] files = (
            "封面\n" +
            "更名说明\n" +
            "技术偏差表\n" +
            "\t业绩文件\n" +
            "\t\t项目经理\n" +
            "\t\t项目成员\n" +
            "项目理解\n" +
            "项目规划\n" +
            "\t公司履约能力\n" +
            "\t质量保障体系\n" +
            "\t质量保险措施\n" +
            "\t项目进度保障\n" +
            "\t项目安全保障\n" +
            "\t应急措施\n" +
            "\t项目组织形式\n" +
            "\t项目团队及管理措施\n" +
            "服务承诺\n" +
            "\t技术方案\n" +
            "\t\t公司机房\n" +
            "\t\t实验室\n" +
            "\t\t公司厂房\n" +
            "\t资质证书材料\n" +
            "\t业主单位绩效评价\n" +
            "\t服务质量评价\n" +
            "\t业主单位感谢信\n" +
            "\t项目获奖情况\n" +
            "技术规范\n" +
            "其他").split("\n");

    public static List<FileEntry> buildCatalog(String[] lines, FileTypes type) {
        List<FileEntry> roots = new ArrayList<>();

        int order = 0;
        for (String rawLine : lines) {
            FileEntry entry = new FileEntry(new File(inputDirPath + rawLine.trim() + type.getSuffix()), order++);
            entry.setRawLine(rawLine);
            roots.add(entry);
        }

        return roots;
    }


}
