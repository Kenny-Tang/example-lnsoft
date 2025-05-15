package com.redjujubetree.ztb.service;

import com.redjujubetree.ztb.mapper.DataInitMapper;

import javax.annotation.Resource;

public class DataInitService{
	@Resource
	private DataInitMapper dataInitMapper;
	public void initData() {
		String tableName = dataInitMapper.isTableExists("user_info");
		if (tableName == null) {
			dataInitMapper.createTable();
		}
	}

	public DataInitMapper getDataInitMapper() {
		return dataInitMapper;
	}

	public void setDataInitMapper(DataInitMapper dataInitMapper) {
		this.dataInitMapper = dataInitMapper;
	}
}
