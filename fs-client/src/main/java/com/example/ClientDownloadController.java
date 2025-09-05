package com.example;

import com.example.client.service.BidDownloadParam;
import com.example.client.service.FileDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client")
public class ClientDownloadController {

    @Autowired
    private FileDownloadService downloadService;

    /**
     * 流式下载文件（适合大文件）
     */
    @PostMapping("/download/stream")
    public boolean downloadFileStreaming(@RequestBody BidDownloadParam request) {

        return downloadService.downloadFileStreaming(request);
    }
}
