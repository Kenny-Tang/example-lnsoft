package com.redjujubetree.fs.domain.entity;

import lombok.Data;
import java.util.Date;

@Data
public class TenderEvaluationCenter {
	private Long id;
	private String clientId;
	private String centerName;
	private String serverName;
	private String centerIp;
	private Date onlineTime;
	private Boolean onlineStatus;
	private Boolean isDeleted;
	private Date createTime;
	private Date updateTime;
	private String createUser;
}