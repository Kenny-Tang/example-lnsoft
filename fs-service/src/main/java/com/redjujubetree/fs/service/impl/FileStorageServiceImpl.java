package com.redjujubetree.fs.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redjujubetree.fs.domain.entity.FileStorage;
import com.redjujubetree.fs.mapper.FileStorageMapper;
import com.redjujubetree.fs.service.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tanjianwei
 * @since 2025-06-06
 */
@Service
public class FileStorageServiceImpl extends ServiceImpl<FileStorageMapper, FileStorage> implements FileStorageService {

    @Transactional(rollbackFor = Throwable.class)
	public void saveFileStorage(FileStorage fileStorage) {

		FileStorage entity = new FileStorage();
		BeanUtil.copyProperties(fileStorage, entity);
		Date date = new Date();
		entity.setLastDate(DateUtil.formatDate(date));
		save(entity);
	}

    public void updateFileStorageById(FileStorage fileStorage) {
        updateById(fileStorage);
    }
}
