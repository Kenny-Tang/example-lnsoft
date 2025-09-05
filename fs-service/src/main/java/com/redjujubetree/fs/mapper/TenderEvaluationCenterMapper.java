package com.redjujubetree.fs.mapper;

import com.redjujubetree.fs.domain.entity.TenderEvaluationCenter;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TenderEvaluationCenterMapper {

	TenderEvaluationCenter selectByClientId(String clientId);

	int insertTenderEvaluationCenter(TenderEvaluationCenter tenderEvaluationCenter);

	int updateTenderEvaluationCenter(TenderEvaluationCenter tenderEvaluationCenter);
}
