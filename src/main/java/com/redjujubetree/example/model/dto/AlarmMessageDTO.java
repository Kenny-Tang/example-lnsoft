package com.redjujubetree.example.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class AlarmMessageDTO {

	private String scopeId;
	private String scope;
	private String name;
	private String id0;
	private String id1;
	private String ruleName;
	private String alarmMessage;
	private String startTime;
	private List<AlarmTagDTO> tags;
}
