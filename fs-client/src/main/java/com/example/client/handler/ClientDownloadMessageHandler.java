package com.example.client.handler;

import top.redjujubetree.grpc.tunnel.client.handler.AbstractServerRequestMessageHandler;
import top.redjujubetree.grpc.tunnel.proto.RequestPayload;
import top.redjujubetree.grpc.tunnel.proto.ResponsePayload;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;

// 客户端消息处理器接口
public class ClientDownloadMessageHandler extends AbstractServerRequestMessageHandler {
	@Resource(name = "defaultExecutorService")
	private ExecutorService executorService;
	@Override
	protected boolean supportRequestType(String request) {
		return false;
	}

	@Override
	protected ResponsePayload handleServerCommand(RequestPayload request) {
		
		return null;
	}
}