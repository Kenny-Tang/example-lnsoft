package com.redjujubetree.qmt.service;

import com.redjujubetree.qmt.domain.entity.Top500Stock;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 沪深A股实时行情（含top500） 服务类
 * </p>
 *
 * @author tanjianwei
 * @since 2025-07-07
 */
public interface Top500StockService extends IService<Top500Stock> {

    void saveTop500Stock(Top500Stock top500Stock);

    void updateTop500StockById(Top500Stock top500Stock);
}
