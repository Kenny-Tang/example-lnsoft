package com.redjujubetree.example.model.dto;

import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {

	private String id;
	private String username;
	private String birthday;
	private String sex;
	private String createTime;

}
