package com.lnsoft.cloud.ztbgl.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lnsoft.cloud.ztbgl.common.constant.DelFlag;
import com.lnsoft.cloud.ztbgl.domain.dto.InvoiceFetchProjectParam;
import com.lnsoft.cloud.ztbgl.domain.entity.ZtbInvoiceFetchProject;
import com.lnsoft.cloud.ztbgl.mapper.ZtbInvoiceFetchProjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class ZtbInvoiceFetchProjectService extends ServiceImpl<ZtbInvoiceFetchProjectMapper, ZtbInvoiceFetchProject> {

	@Transactional(rollbackFor = Throwable.class)
	public void saveProject(InvoiceFetchProjectParam project) {
		getOneOpt(new LambdaQueryWrapper<ZtbInvoiceFetchProject>()
				.eq(ZtbInvoiceFetchProject::getProjectName, project.getProjectName())
				.eq(ZtbInvoiceFetchProject::getProjectYear, project.getProjectYear())
				.eq(ZtbInvoiceFetchProject::getDelFlag, DelFlag.DATA_IS_VALID)
		).ifPresent(p -> {
			throw new IllegalArgumentException("项目已存在");
		});

		ZtbInvoiceFetchProject entity = new ZtbInvoiceFetchProject();
		BeanUtil.copyProperties(project, entity);
		entity.setDelFlag(DelFlag.DATA_IS_VALID);
		entity.setCreateBy("tanjianwei");
		Date date = new Date();
		entity.setUpdateTime(date);
		entity.setCreateTime(date);
		save(entity);
	}
}
