package com.redjujubetree.fs.handler;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson2.JSON;
import com.redjujubetree.fs.domain.entity.TenderEvaluationCenter;
import com.redjujubetree.fs.mapper.TenderEvaluationCenterMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.redjujubetree.grpc.tunnel.proto.ResponsePayload;
import top.redjujubetree.grpc.tunnel.proto.TunnelMessage;
import top.redjujubetree.grpc.tunnel.server.handler.DefaultConnectedHandler;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class ConnectedHandler extends DefaultConnectedHandler {
	private Snowflake snowflake = new Snowflake();

	@Resource
	private TenderEvaluationCenterMapper tenderEvaluationCenterMapper;

	protected ResponsePayload handleRequest(TunnelMessage request) {
		TenderEvaluationCenter center = new TenderEvaluationCenter();
		center.setId(snowflake.nextId());
		center.setCreateTime(new Date());
		center.setClientId(request.getClientId());
		center.setOnlineTime(new Date());
		center.setOnlineStatus(Boolean.TRUE);
		center.setIsDeleted(Boolean.FALSE);
		String mapStr = request.getRequest().getData().toStringUtf8();
		Map<String, String> map = JSON.parseObject(mapStr, Map.class);
		center.setServerName(map.get("serverMachineName"));
		center.setCenterIp(map.get("clientIp"));
		log.info(JSON.toJSONString(center));
		TenderEvaluationCenter tenderEvaluationCenter = tenderEvaluationCenterMapper.selectByClientId(center.getClientId());
		if (tenderEvaluationCenter != null) {
			center.setId(tenderEvaluationCenter.getId());
			tenderEvaluationCenterMapper.updateTenderEvaluationCenter(center);
			return null;
		} else {
			tenderEvaluationCenterMapper.insertTenderEvaluationCenter(center);
		}
		return null;
	}

}
