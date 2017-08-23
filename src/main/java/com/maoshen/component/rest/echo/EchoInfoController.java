package com.maoshen.component.rest.echo;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/echoInfo")
public class EchoInfoController implements ApplicationContextAware {
	/**
	 * <bean id="sqlSessionTemplate" class=
	 * "org.mybatis.spring.SqlSessionTemplate">
	 * <property name="sqlSessionFactory" ref="sqlSessionFactory" />
	 * org.mybatis.spring.SqlSessionFactoryBean </bean>
	 * 
	 */

	private static final String DB = "DB";
	private static final String REDIS = "REDIS";
	private static final String IS_NEED_CHECK = "0";
	private static final String SUCCESS = "200";
	private static final Logger LOGGER = LoggerFactory.getLogger(EchoInfoController.class);

	private ApplicationContext applicationContext;

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "", method = { RequestMethod.GET })
	@ResponseBody
	protected Map<String, Object> echoInfo(HttpServletRequest request) {
		long echoStart = System.currentTimeMillis();
		// 最终检测结果
		boolean result = true;
		Map<String, Object> map = new HashMap<String, Object>();

		if (StringUtils.isNotBlank(request.getParameter(DB)) && IS_NEED_CHECK.equals(request.getParameter(DB))) {
			try {
				//SqlSessionTemplate sqlSessionTemplate = applicationContext.getBean(org.mybatis.spring.SqlSessionTemplate.class);
				Class.forName("org.springframework.jdbc.core.JdbcTemplate");
				org.springframework.jdbc.core.JdbcTemplate sqlSessionTemplate = applicationContext.getBean(org.springframework.jdbc.core.JdbcTemplate.class);
				if (sqlSessionTemplate == null) {
					map.put(DB, SUCCESS + ",xml not fount");
				} else {
					try {
						sqlSessionTemplate.execute("select 1" );
						map.put(DB, SUCCESS);
					} catch (Exception e) {
						LOGGER.error(e.getMessage(), e);
						map.put(DB, e.getMessage());
						result = false;
					}
				}
			} catch (Exception e) {
				map.put(DB, SUCCESS + ",xml not fount");
			}
		} else {
			map.put(DB, SUCCESS + ",not check");
		}

		if (StringUtils.isNotBlank(request.getParameter(REDIS)) && IS_NEED_CHECK.equals(request.getParameter(DB))) {
			try {
				RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
				if (redisTemplate == null) {
					map.put(REDIS, SUCCESS + ",xml not fount");
				} else {
					try {
						redisTemplate.opsForValue().get("test");
						map.put(REDIS, SUCCESS);
					} catch (Exception e) {
						LOGGER.error(e.getMessage(), e);
						map.put(REDIS, e.getMessage());
						result = false;
					}
				}
			} catch (Exception e) {
				map.put(REDIS, SUCCESS + ",xml not fount");
			}
		} else {
			map.put(REDIS, SUCCESS + ",not check");
		}

		// log级别
		map.put("logger_debug", LOGGER.isDebugEnabled());
		map.put("logger_info", LOGGER.isInfoEnabled());
		map.put("logger_warn", LOGGER.isWarnEnabled());
		map.put("logger_error", LOGGER.isErrorEnabled());

		map.put("runTime", System.currentTimeMillis() - echoStart);
		map.put("result", result);
		return map;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
