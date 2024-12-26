package com.redjujubetree.example.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.redjujubetree.example.common.BaseResponse;
import com.redjujubetree.example.model.GenderEnum;
import com.redjujubetree.example.model.dto.UserDTO;
import com.redjujubetree.example.model.entity.Users;
import com.redjujubetree.example.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kenny
 * @since 2024-12-23
 */
@Slf4j
@Controller
@RequestMapping("/users")
public class UsersController {

	@Resource
	private UsersService usersService;

	@GetMapping("{id}")
	@ResponseBody
	public BaseResponse getUserById(@PathVariable Long id) throws InterruptedException {
		BaseResponse baseResponse = BaseResponse.ofSuccess();
		LambdaQueryWrapper<Users> eq = Wrappers.lambdaQuery();
		eq.eq(Users::getId, id);
		try {
			Users user = usersService.getOne(eq);
			log.info(JSON.toJSONString(user));
			Thread.sleep(1000);
			if (user == null) {
				throw new RuntimeException("user not found exception");
			}
			UserDTO target = new UserDTO();
			BeanUtils.copyProperties(user, target);
			target.setId(user.getId()+"");
			target.setSex(user.getSex()+"");
			target.setBirthday(DateUtil.format(user.getBirthday(), "yyyy-MM-dd"));
			target.setCreateTime(DateUtil.format(user.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			Optional.ofNullable(GenderEnum.fromGender(user.getSex())).ifPresent(gender -> target.setSex(gender.getDescription()));
			baseResponse.setData(target);
		} catch (RuntimeException e) {
			log.error(e.getMessage(), e);
		}

		return baseResponse;
	}
}
