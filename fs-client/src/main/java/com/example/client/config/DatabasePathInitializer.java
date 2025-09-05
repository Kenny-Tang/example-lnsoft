package com.example.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
/**
 * 应用启动监听器
 */
@Component
public class DatabasePathInitializer {
    
    @Value("${spring.datasource.url}")
    private String datasourceUrl;
    
    @PostConstruct
    public void initializeDatabasePath() {
        System.out.println("🚀 检查数据库路径...");
        
        if (datasourceUrl != null && datasourceUrl.startsWith("jdbc:sqlite:")) {
            createDirectoryForSqlite(datasourceUrl);
        }
    }
    
    private void createDirectoryForSqlite(String jdbcUrl) {
        try {
            String filePath = jdbcUrl.replace("jdbc:sqlite:", "");
            File dbFile = new File(filePath);
            File parentDir = dbFile.getParentFile();
            
            if (parentDir != null && !parentDir.exists()) {
                boolean created = parentDir.mkdirs();
                if (created) {
                    System.out.println("✅ 已创建数据库目录: " + parentDir.getAbsolutePath());
                } else {
                    System.err.println("❌ 无法创建数据库目录: " + parentDir.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            System.err.println("❌ 初始化数据库路径失败: " + e.getMessage());
        }
    }
}