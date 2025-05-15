package com.redjujubetree.users.service;

import com.redjujubetree.users.domain.entity.ColumnInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 专栏信息 服务类
 * </p>
 *
 * @author tanjianwei
 * @since 2025-05-15
 */
public interface ColumnInfoService extends IService<ColumnInfo> {

    void saveColumnInfo(ColumnInfo columnInfo);

    void updateColumnInfoById(ColumnInfo columnInfo);
}
