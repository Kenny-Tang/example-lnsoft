package com.redjujubetree;

import com.alibaba.fastjson2.JSON;

public class Main {
	public static void main(String[] args) {
		System.out.println("Hello world!");
		CatalogReuseRelDTO catalogReuseRelDTO = new CatalogReuseRelDTO();
		catalogReuseRelDTO.setRelItemName("关联分标名称");
		catalogReuseRelDTO.setItemName("分标名称");
		catalogReuseRelDTO.setBidType("1");
		catalogReuseRelDTO.setProjectName("项目名称");
		catalogReuseRelDTO.setRelProjectName("关联项目名称");
		catalogReuseRelDTO.setVirBidName("虚拟分标名称");
		catalogReuseRelDTO.setRelVirBidName("关联虚拟分标名称");
		catalogReuseRelDTO.setVirBidId(111L);
		catalogReuseRelDTO.setRelVirBidId(222L);
		catalogReuseRelDTO.setRelItemName("关联分标名称");
		catalogReuseRelDTO.setVendorName("供应商名称");
		System.out.println(JSON.toJSONString(catalogReuseRelDTO));
	}
}