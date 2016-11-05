package com.maoshen.component.task;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 使用REDIS控制负载均衡，只允许一台机操作
 * https://my.oschina.net/lese/blog/308709
 * @author daxian.jianglifesense.com
 *
 */
public abstract class BaseRedisTask extends BaseTask {
	private static final Logger LOGGER = Logger.getLogger(BaseRedisTask.class);
	@SuppressWarnings("rawtypes")
	@Autowired
	private RedisTemplate jedisTemplate;

	@Autowired
	@Qualifier("jedisFactory")
	private JedisConnectionFactory jedisConnectionFactory;

	@SuppressWarnings("unchecked") 
	public void doJob() {
		if (StringUtils.isNotBlank(getName())) {
			if (jedisTemplate.opsForValue().setIfAbsent(getName(), "true")) {
				try {
					LOGGER.info(Thread.currentThread().getName() + "_" + this.getClass() + "_" + this.getName() + " run start,date is:"+new Date());
					timeTaskRun();
					LOGGER.info(Thread.currentThread().getName() + "_" + this.getClass() + "_" + this.getName() + " run end,date is:"+new Date());
				} catch (Exception e) {
					LOGGER.error(this.getClass() + "_" + this.getName() + " run timeTaskError", e);
				} finally {
					jedisTemplate.delete(getName());
				}
			}
		}
	}
}
