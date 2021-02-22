package com.sinoparasoft.storage.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinoparasoft.storage.model.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 元数据管理服务类
 *
 * @author 袁涛
 * @date 2017.10.27
 */
@Component
public class MetadataService {
	private static final Logger logger = LoggerFactory.getLogger(MetadataService.class);

	/**
	 * 将JSON字符串转换为元数据类型
	 *
	 * @param json
	 *            JSON字符串
	 * @return 元数据类型
	 */
	public Metadata toMetadata(String json) {
		Metadata retValue = null;

		ObjectMapper mapper = new ObjectMapper();
		try {
			retValue = mapper.readValue(json, Metadata.class);
		} catch (JsonParseException e) {
			String msg = "json string parse failed, json = " + json;
			logger.error(msg, e);
		} catch (JsonMappingException e) {
			String msg = "json string transform to metadata failed, json = " + json;
			logger.error(msg, e);
		} catch (IOException e) {
			String msg = "json string transform to metadata failed, json = " + json;
			logger.error(msg, e);
		}

		return retValue;
	}

	/**
	 * 将元数据类型转换为JSON字符串
	 *
	 * @param metadata
	 *            元数据类型
	 * @return JSON字符串
	 */
	public String toJson(Metadata metadata) {
		String retValue = "{}";

		ObjectMapper mapper = new ObjectMapper();
		try {
			retValue = mapper.writeValueAsString(metadata);
		} catch (JsonProcessingException e) {
			String msg = "metadata transform to json string failed, metadata = " + metadata.toString();
			logger.error(msg, e);
		}

		return retValue;
	}
}
