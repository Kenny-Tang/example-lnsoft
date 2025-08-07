package com.redjujubetree.fs.controller;

import com.redjujubetree.SystemClock;
import com.redjujubetree.fs.domain.entity.FileStorage;
import com.redjujubetree.fs.service.FileStorageService;
import com.redjujubetree.response.BaseResponse;
import com.redjujubetree.response.RespCodeEnum;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tanjianwei
 * @since 2025-06-06
 */
@Slf4j
@Setter
@RestController
@RequestMapping("/fs/fileStorage")
public class FileStorageController {

    @Resource
    private FileStorageService fileStorageService;

	public BaseResponse save(FileStorage fileStorage) {
		try {
			//fileStorageService.saveFileStorage(fileStorage);
			return new BaseResponse(RespCodeEnum.SUCCESS.getCode(), RespCodeEnum.SUCCESS.getMessage());
		} catch(IllegalArgumentException e){
			log.warn(e.getMessage(), e);
			return new BaseResponse(RespCodeEnum.PARAM_ERROR.getCode(), RespCodeEnum.PARAM_ERROR.getMessage());
		} catch (Exception e) {
			log.error("系统异常请联系管理员处理", e);
			return new BaseResponse(RespCodeEnum.FAIL.getCode(), "系统异常请联系管理员处理");
		}
	}

	@PostMapping
	public BaseResponse upload(MultipartFile file , @ModelAttribute FileStorage fileStorage) {
		try {
			String originalFilename = file.getOriginalFilename();
			fileStorage.setFileName(originalFilename);
			fileStorage.setData(file.getBytes());
			if (fileStorage.getKey() == null || fileStorage.getKey().isEmpty()) {
				fileStorage.setKey(SystemClock.currentTimeMillis()+"");
			}
//			fileStorageService.saveFileStorage(fileStorage);
			return new BaseResponse(RespCodeEnum.SUCCESS.getCode(), RespCodeEnum.SUCCESS.getMessage());
		} catch(IllegalArgumentException e){
			log.warn(e.getMessage(), e);
			return new BaseResponse(RespCodeEnum.PARAM_ERROR.getCode(), RespCodeEnum.PARAM_ERROR.getMessage());
		} catch (Exception e) {
			log.error("系统异常请联系管理员处理", e);
			return new BaseResponse(RespCodeEnum.FAIL.getCode(), "系统异常请联系管理员处理");
		}
	}
}
