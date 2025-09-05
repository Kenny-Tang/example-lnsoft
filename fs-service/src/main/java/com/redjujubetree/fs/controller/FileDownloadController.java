package com.redjujubetree.fs.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/download")
public class FileDownloadController {

    private static final String FILE_DIRECTORY = "/Users/kenny/IdeaProjects/example/uploads"; // 修改为实际文件目录
    private static final int BUFFER_SIZE = 4096;

    @GetMapping("/{fileName}")
    public ResponseEntity<StreamingResponseBody> downloadFile(
            @PathVariable String fileName,
            @RequestHeader(value = "Range", required = false) String rangeHeader,
            HttpServletRequest request,
            HttpServletResponse response) {

        // 构建文件路径
        Path filePath = Paths.get(FILE_DIRECTORY, fileName);
        
        // 检查文件是否存在
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        File file = filePath.toFile();
        long fileLength = file.length();
        
        // 解析Range请求头
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
                if (ranges.length > 1 && !ranges[1].isEmpty()) {
                    rangeEnd = Long.parseLong(ranges[1]);
                }
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).build();
            }
        }

        // 验证范围
        if (rangeStart < 0 || rangeEnd >= fileLength || rangeStart > rangeEnd) {
            return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).build();
        }

        long contentLength = rangeEnd - rangeStart + 1;
        
        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, getContentType(fileName));
        headers.add(HttpHeaders.ACCEPT_RANGES, "bytes");
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength));
        
        if (partialContent) {
            headers.add(HttpHeaders.CONTENT_RANGE, 
                String.format("bytes %d-%d/%d", rangeStart, rangeEnd, fileLength));
        }
        
        headers.add(HttpHeaders.CONTENT_DISPOSITION, 
            "attachment; filename=\"" + fileName + "\"");

        // 添加ETag支持
        String eTag = generateETag(file);
        headers.add(HttpHeaders.ETAG, eTag);

        // 检查If-Range头
        String ifRange = request.getHeader("If-Range");
        if (ifRange != null && !ifRange.equals(eTag)) {
            // ETag不匹配，返回完整文件
            rangeStart = 0;
            rangeEnd = fileLength - 1;
            contentLength = fileLength;
            partialContent = false;
            headers.remove(HttpHeaders.CONTENT_RANGE);
            headers.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength));
        }

        final long finalRangeStart = rangeStart;
        final long finalContentLength = contentLength;
        
        StreamingResponseBody responseBody = outputStream -> {
            try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
                randomAccessFile.seek(finalRangeStart);
                
                byte[] buffer = new byte[BUFFER_SIZE];
                long bytesRead = 0;
                int len;
                
                while (bytesRead < finalContentLength) {
                    len = randomAccessFile.read(buffer, 0, 
                        (int) Math.min(BUFFER_SIZE, finalContentLength - bytesRead));
                    
                    if (len == -1) {
                        break;
                    }
                    
                    outputStream.write(buffer, 0, len);
                    bytesRead += len;
                }
                
                outputStream.flush();
            } catch (IOException e) {
                throw new RuntimeException("文件读取失败", e);
            }
        };

        HttpStatus status = partialContent ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(status).headers(headers).body(responseBody);
    }

    @GetMapping("/info/{fileName}")
    public ResponseEntity<FileInfo> getFileInfo(@PathVariable String fileName) {
        Path filePath = Paths.get(FILE_DIRECTORY, fileName);
        
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        File file = filePath.toFile();
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(fileName);
        fileInfo.setFileSize(file.length());
        fileInfo.setETag(generateETag(file));
        fileInfo.setLastModified(file.lastModified());
        fileInfo.setSupportsResume(true);

        return ResponseEntity.ok(fileInfo);
    }

    private String generateETag(File file) {
        return "\"" + file.lastModified() + "-" + file.length() + "\"";
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
            case "zip":
                return "application/zip";
            case "doc":
            case "docx":
                return "application/msword";
            case "xls":
            case "xlsx":
                return "application/vnd.ms-excel";
            case "png":
                return "image/png";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "gif":
                return "image/gif";
            case "mp4":
                return "video/mp4";
            case "mp3":
                return "audio/mpeg";
            case "txt":
                return "text/plain";
            default:
                return "application/octet-stream";
        }
    }

    // 文件信息DTO
    public static class FileInfo {
        private String fileName;
        private long fileSize;
        private String eTag;
        private long lastModified;
        private boolean supportsResume;

        // Getters and Setters
        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public long getFileSize() {
            return fileSize;
        }

        public void setFileSize(long fileSize) {
            this.fileSize = fileSize;
        }

        public String getETag() {
            return eTag;
        }

        public void setETag(String eTag) {
            this.eTag = eTag;
        }

        public long getLastModified() {
            return lastModified;
        }

        public void setLastModified(long lastModified) {
            this.lastModified = lastModified;
        }

        public boolean isSupportsResume() {
            return supportsResume;
        }

        public void setSupportsResume(boolean supportsResume) {
            this.supportsResume = supportsResume;
        }
    }
}