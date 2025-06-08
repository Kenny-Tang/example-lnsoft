package com.redjujubetree.fs.service;

import com.redjujubetree.fs.domain.entity.FileStorage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tanjianwei
 * @since 2025-06-06
 */
public interface FileStorageService extends IService<FileStorage> {

    void saveFileStorage(FileStorage fileStorage);

    void updateFileStorageById(FileStorage fileStorage);
}
