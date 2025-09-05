package com.redjujubetree.fs.controller;

import cn.hutool.json.JSONUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Setter
@RequestMapping("/bid/download")
@RestController
public class BidFileDownloadController {


    @GetMapping("")
    public ResponseEntity<StreamingResponseBody> downloadBidFile(
            @ModelAttribute com.redjujubetree.fs.controller.ClientDownloadBidFileResponse downloadRequest,
            @RequestHeader(value = "Range", required = false) String rangeHeader,
            HttpServletRequest request) {
        log.info("request param {}", JSONUtil.toJsonStr(downloadRequest));
        return processDownload(downloadRequest, rangeHeader, request);
    }

    private ResponseEntity<StreamingResponseBody> processDownload(ClientDownloadBidFileResponse downloadRequest, String rangeHeader, HttpServletRequest request) {
        Path filePath = Paths.get("/Users/kenny/Documents/教程/Frozen","/Frozen.2013.2160p.4K.BluRay.x265.10bit.AAC5.1-[YTS.MX].mkv");

        if (!Files.exists(filePath)) {
            log.info("file not exists {}", filePath.toFile().getAbsolutePath());
            return ResponseEntity.notFound().build();
        }

        File file = filePath.toFile();
        long fileLength = file.length();
        long rangeStart = 0;
        long rangeEnd = fileLength - 1;
        boolean partialContent = false;

        if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
            partialContent = true;
            String range = rangeHeader.substring(6);
            String[] ranges = range.split("-");
            try {
                if (ranges.length > 0 && !ranges[0].isEmpty()) {
                    rangeStart = Long.parseLong(ranges[0]);
                }
                // 修复bug: 应该是ranges[1]而不是ranges[0]
                if (ranges.length > 1 && !ranges[1].isEmpty()) {
                    rangeEnd = Long.parseLong(ranges[1]);
                }
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).build();
            }
        }

        if (rangeStart < 0 || rangeEnd >= fileLength || rangeStart > rangeEnd) {
            return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).build();
        }

        long contentLength = rangeEnd - rangeStart + 1;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, getContentType(file.getName()));
        headers.add(HttpHeaders.ACCEPT_RANGES, "bytes");
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength));

        if (partialContent) {
            headers.add(HttpHeaders.CONTENT_RANGE, String.format("bytes %d-%d/%d", rangeStart, rangeEnd, fileLength));
        }
        String checksum = "checksum-placeholder";
        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(file.getName(), StandardCharsets.UTF_8).build();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, disposition.toString());
        headers.add(HttpHeaders.ETAG, checksum);

        String ifRange = request.getHeader("If-Range");
        if (ifRange != null && !ifRange.equals(checksum)) {
            rangeStart = 0;
            rangeEnd = fileLength - 1;
            contentLength = fileLength;
            partialContent = false;
            headers.remove(HttpHeaders.CONTENT_RANGE);
            headers.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength));
        }

        StreamingResponseBody responseBody = getStreamingResponseBody(rangeStart, contentLength, file);

        HttpStatus status = partialContent ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(status).headers(headers).body(responseBody);
    }

    private StreamingResponseBody getStreamingResponseBody(long rangeStart, long contentLength, File file) {
        return outputStream -> {
            try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
                randomAccessFile.seek(rangeStart);
                
                // 优化缓冲区大小：大文件使用更大的缓冲区
                final int bufferSize = determineBufferSize(contentLength);
                byte[] buffer = new byte[bufferSize];
                long bytesRead = 0;
                int len;
                int flushCounter = 0;
                final int flushInterval = calculateFlushInterval(bufferSize);
                
                log.debug("Starting file stream: file={}, rangeStart={}, contentLength={}, bufferSize={}", 
                         file.getName(), rangeStart, contentLength, bufferSize);
                int time = 0;
                while (bytesRead < contentLength) {
                    long remaining = contentLength - bytesRead;
                    int readSize = (int) Math.min(bufferSize, remaining);
                    
                    len = randomAccessFile.read(buffer, 0, readSize);
                    if (len == -1) {
                        break;
                    }
                    
                    try {
                        outputStream.write(buffer, 0, len);
                        bytesRead += len;
                        flushCounter++;
                        
                        // 减少flush频率以提高性能
                        if (flushCounter >= flushInterval) {
                            outputStream.flush();
                            flushCounter = 0;
                        }
                        if (time++ %10 == 0) {
                            System.out.println(time);
                        }
                        Thread.sleep(1000);
                    } catch (ClientAbortException e) {
                        // 客户端主动断开连接，这是正常情况
                        log.info("Client disconnected during download: file={}, bytesTransferred={}/{}", 
                                file.getName(), bytesRead, contentLength);
                        return; // 优雅退出，不抛异常
                    } catch (IOException e) {
                        // 检查是否是客户端断开连接相关的异常
                        if (isClientDisconnectionException(e)) {
                            log.info("Client connection lost during download: file={}, bytesTransferred={}/{}, error={}", 
                                    file.getName(), bytesRead, contentLength, e.getMessage());
                            return; // 优雅退出
                        } else {
                            // 其他IO异常仍然抛出
                            throw e;
                        }
                    } catch (InterruptedException e) {
					}
				}
                
                // 确保最后的数据被发送
                try {
                    outputStream.flush();
                    log.debug("File download completed successfully: file={}, bytesTransferred={}", 
                             file.getName(), bytesRead);
                } catch (IOException e) {
                    if (isClientDisconnectionException(e)) {
                        log.info("Client disconnected at end of download: file={}, bytesTransferred={}", 
                                file.getName(), bytesRead);
                    } else {
                        throw e;
                    }
                }
                
            } catch (IOException e) {
                log.error("File read failed: file={}, error={}", file.getName(), e.getMessage());
                throw new RuntimeException("File read failed: " + file.getName(), e);
            }
        };
    }

    /**
     * 根据内容长度确定最优缓冲区大小
     */
    private int determineBufferSize(long contentLength) {
        if (contentLength > 1024 * 1024 * 1024) { // > 1GB
            return 64 * 1024; // 64KB
        } else if (contentLength > 100 * 1024 * 1024) { // > 100MB
            return 32 * 1024; // 32KB
        } else if (contentLength > 10 * 1024 * 1024) { // > 10MB
            return 16 * 1024; // 16KB
        } else {
            return 8 * 1024; // 8KB
        }
    }

    /**
     * 计算flush间隔以平衡性能和内存使用
     */
    private int calculateFlushInterval(int bufferSize) {
        // 每传输约256KB数据flush一次
        return Math.max(1, (256 * 1024) / bufferSize);
    }

    /**
     * 判断是否是客户端断开连接相关的异常
     */
    private boolean isClientDisconnectionException(Throwable e) {
        if (e instanceof ClientAbortException) {
            return true;
        }
        
        String message = e.getMessage();
        if (message != null) {
            String lowerMessage = message.toLowerCase();
            return lowerMessage.contains("connection reset") ||
                   lowerMessage.contains("broken pipe") ||
                   lowerMessage.contains("connection abort") ||
                   lowerMessage.contains("socket closed") ||
                   lowerMessage.contains("connection closed");
        }
        
        return false;
    }

    private String getContentType(String fileName) {
        String extension = "";
        int i = fileName.lastIndexOf('.');

        if (i > 0) {
            extension = fileName.substring(i + 1).toLowerCase();
        }

        switch (extension) {
            case "pdf":
                return "application/pdf";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls":
                return "application/vnd.ms-excel";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "zip":
                return "application/zip";
            case "rar":
                return "application/x-rar-compressed";
            default:
                return "application/octet-stream";
        }
    }
}