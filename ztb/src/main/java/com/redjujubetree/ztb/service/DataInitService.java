package com.redjujubetree.ztb.service;

import com.redjujubetree.ztb.mapper.DataInitMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
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
