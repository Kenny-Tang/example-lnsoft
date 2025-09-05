package com.example.client.service;

import cn.hutool.json.JSONUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Objects;

@Setter
@Slf4j
@Service
public class FileDownloadService {

    private final int BUFFER_SIZE = 32 * 1024; // 32KB缓冲区

    private final RestTemplate restTemplate;

    @Autowired
    public FileDownloadService(RestTemplateBuilder restTemplateBuilder) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout((int) Duration.ofSeconds(60).toMillis())  // 连接超时
                .setSocketTimeout((int) Duration.ofMinutes(60).toMillis())   // 读超时
                .setConnectionRequestTimeout((int) Duration.ofSeconds(30).toMillis()) // 连接池取连接超时
                .build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(200);           // 最大连接数
        connectionManager.setDefaultMaxPerRoute(50);  // 单路由最大连接数

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .disableContentCompression()  // 禁用 GZIP，避免大文件中途断流
                .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setBufferRequestBody(false); // 下载/上传大文件必须禁用缓存

        this.restTemplate = new RestTemplate(factory);
    }


    public boolean downloadFileStreaming(BidDownloadParam request) {
        Path downloadPath = Paths.get(request.getDocumentStoragePath(), request.getReferencePath());
        Path tempPath = Paths.get(request.getDocumentStoragePath(), request.getReferencePath() + ".tmp");

        try {
            Files.createDirectories(downloadPath.getParent());
            
            long downloadedSize = 0;
            if (Files.exists(tempPath)) {
                downloadedSize = Files.size(tempPath);
                log.info("Resuming download from byte: {}", downloadedSize);
            }

            boolean success = streamDownload(request, tempPath, downloadedSize);
            
            if (success) {
                // 验证文件完整性
                if (validateDownloadedFile(tempPath, request)) {
                    Files.move(tempPath, downloadPath);
                    log.info("File download completed successfully: {}", request.getReferencePath());
                } else {
                    log.error("File validation failed after download: {}", request.getReferencePath());
                    //Files.deleteIfExists(tempPath);
                    return false;
                }
            }
            return success;
        } catch (Exception e) {
            log.error("downloadFileStreaming error: {}", e.getMessage(), e);
            try {
                Files.deleteIfExists(tempPath);
            } catch (Exception cleanupException) {
                log.warn("Failed to cleanup temp file: {}", cleanupException.getMessage());
            }
        }
        return false;
    }

    private boolean streamDownload(BidDownloadParam parasm, Path tempPath, long downloadedSize0) {
        // log.info("streamDownload: {}, resuming from byte: {}", JSONUtil.toJsonStr(param), downloadedSize);
        final BidDownloadParam param = JSONUtil.toBean(" {\"syncRecordId\":65,\"documentStoragePath\":\"E:\\\\250303\",\"referencePath\":\"中原招标代理有限公司/江东公司2025年第三次物资类公开招标采购/技术/分标1/包11/为庄信息技术有限公司/分标1-包11-为庄信息技术有限公司-技术.pdf.enc\",\"checksum\":\"41f6a52bb3555d81e4fcf74a72781a24\",\"fileSize\":1082046978}" , BidDownloadParam.class);
        String url = buildUrl("http://172.16.33.72:28084/prod-api/bid/download", param);
        long downloadedSize = 0;
        HttpHeaders headers = new HttpHeaders();
        String checksum = "checksum-placeholder";
        if (downloadedSize > 0) {
            headers.set("Range", "bytes=" + downloadedSize + "-");
            headers.set("If-Range", checksum);
        }

        return restTemplate.execute(url, HttpMethod.GET,
                request -> request.getHeaders().addAll(headers),
                response -> {
                    HttpStatus status = response.getStatusCode();
                    log.info("Response status: {}, content-length: {}", 
                            status, response.getHeaders().getFirst("Content-Length"));
                    
                    try (InputStream inputStream = response.getBody();
                         RandomAccessFile randomAccessFile = new RandomAccessFile(tempPath.toFile(), "rw")) {
                        
                        long writePos = downloadedSize;
                        
                        // 如果服务器返回200而不是206，说明不支持断点续传，从头开始
                        if (HttpStatus.OK.equals(status)) {
                            writePos = 0L;
                            randomAccessFile.setLength(0);
                            log.info("Server doesn't support resume, starting from beginning");
                        } else if (HttpStatus.PARTIAL_CONTENT.equals(status)) {
                            log.info("Resuming download from position: {}", writePos);
                        }
                        
                        randomAccessFile.seek(writePos);
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int bytesRead;
                        long totalBytesRead = writePos;
                        long lastLogTime = System.currentTimeMillis();
                        final long logInterval = 10000; // 每10秒记录一次进度

                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            randomAccessFile.write(buffer, 0, bytesRead);
                            totalBytesRead += bytesRead;
                            
                            // 定期记录下载进度
                            long currentTime = System.currentTimeMillis();
                            if (currentTime - lastLogTime > logInterval) {
                                long expectedSize = param.getFileSize();
                                double progress = expectedSize > 0 ? (double) totalBytesRead / expectedSize * 100 : 0;
                                log.info("Download progress: {} bytes ({:.1f}%), file: {}", 
                                        totalBytesRead, progress, param.getReferencePath());
                                lastLogTime = currentTime;
                            }
                        }
                        
                        log.info("Download stream completed: {} bytes written for file: {}", 
                                totalBytesRead, param.getReferencePath());
                        return true;
                        
                    } catch (Exception e) {
                        log.error("Error during file streaming: {}", e.getMessage(), e);
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    /**
     * 验证下载的文件
     */
    private boolean validateDownloadedFile(Path filePath, BidDownloadParam param) {
        try {
            if (!Files.exists(filePath)) {
                log.error("Downloaded file does not exist: {}", filePath);
                return false;
            }
            
            long actualSize = Files.size(filePath);
            long expectedSize = param.getFileSize();
            
            if (expectedSize > 0 && actualSize != expectedSize) {
                log.error("File size mismatch: expected={}, actual={}, file={}", 
                         expectedSize, actualSize, param.getReferencePath());
                return false;
            }
            
            return true;
        } catch (Exception e) {
            log.error("File validation error: {}", e.getMessage(), e);
            return false;
        }
    }

    public String buildUrl(String baseUrl, Object obj) {
        if (obj == null) {
            return baseUrl;
        }
        StringBuilder url = new StringBuilder(baseUrl);
        StringBuilder query = new StringBuilder();
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object val = field.get(obj);
                if (!Objects.isNull(val)) {
                    if (query.length() > 0) {
                        query.append("&");
                    }
                    query.append(URLEncoder.encode(field.getName(), StandardCharsets.UTF_8.name()))
                            .append("=")
                            .append(URLEncoder.encode(val.toString(), StandardCharsets.UTF_8.name()));
                }
            }
        } catch (IllegalAccessException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        if (query.length() > 0) {
            url.append("?").append(query);
        }
        return url.toString();
    }
}