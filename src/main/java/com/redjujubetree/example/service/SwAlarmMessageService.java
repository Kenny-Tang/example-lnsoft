package com.redjujubetree.example.service;

import com.redjujubetree.example.model.entity.SwAlarmMessage;
import com.redjujubetree.example.mapper.SwAlarmMessageMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Table to store alarm messages and service response time rules. 服务实现类
 * </p>
 *
 * @author kenny
 * @since 2024-12-24
 */
@Service
public class SwAlarmMessageService extends ServiceImpl<SwAlarmMessageMapper, SwAlarmMessage> {

}
