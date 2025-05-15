package com.redjujubetree.ztb.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface DataInitMapper {


	@Select("SELECT name FROM sqlite_master WHERE type='table' AND name= #{tableName}")
	String isTableExists(String tableName);

	@Insert("CREATE TABLE user_info (\n" +
			"\tid          INTEGER           NOT NULL PRIMARY KEY,\n" +
			"\t username    varchar(255)      NOT NULL,\n" +
			"\t username_zh varchar(255),\n" +
			"\t password    varchar(255),\n" +
			"\t del_flag    INTEGER(1),\n" +
			"\t version     INTEGER,\n" +
			"\t create_time INTEGER NOT NULL DEFAULT (strftime('%s', 'now') * 1000),  -- Unix 时间戳（毫秒）\n" +
			"\t update_time INTEGER NOT NULL DEFAULT (strftime('%s', 'now') * 1000)   -- Unix 时间戳（毫秒）\n" +
			")")
	void createTable();

}
