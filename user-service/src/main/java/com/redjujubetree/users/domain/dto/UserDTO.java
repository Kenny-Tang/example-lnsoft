package com.redjujubetree.users.domain.dto;

import lombok.Data;

@Data
public class UserDTO {

	private String id;
	private String username;

	private String usernameZh;

	private String password;

	private Boolean delFlag;

	private Integer version;

	private String createTime;

	private String updateTime;
}
