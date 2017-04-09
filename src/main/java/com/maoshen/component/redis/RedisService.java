/**   
 * @Description:(用一句话描述该类做什么)
 * @author Daxian.jiang
 * @Email  Daxian.jiang@vipshop.com
 * @Date 2015年7月29日 上午10:06:31
 * @Version V1.0   
 */
package com.maoshen.component.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis 
 * @author daxian.jianglifesense.com
 
 <!-- jedis pool配置 -->
	<bean id="jedisPoolConfig" class="redis.clients.jedis.JedisPoolConfig">
	</bean>
	<bean id="jedisFactory"
		class="org.springframework.data.redis.connection.jedis.JedisConnectionFactory">
		<property name="usePool" value="true" />
		<property name="hostName" value="127.0.0.1" />
		<property name="password" value="" />
		<property name="port" value="6379" />
		<constructor-arg index="0" ref="jedisPoolConfig" />
	</bean>
	<!-- spring data redis -->
	<bean id="jedisTemplate" class="org.springframework.data.redis.core.RedisTemplate">
		<property name="connectionFactory" ref="jedisFactory" />
		<property name="keySerializer">
			<bean
				class="org.springframework.data.redis.serializer.StringRedisSerializer" />
		</property>
		<property name="hashKeySerializer">
			<bean
				class="org.springframework.data.redis.serializer.StringRedisSerializer" />
		</property>
	</bean>
 */
@SuppressWarnings("rawtypes")
public class RedisService {

	@Autowired
	private RedisTemplate jedisTemplate;

	public RedisTemplate getJedisTemplate() {
		return jedisTemplate;
	}
	
	public void setJedisTemplate(RedisTemplate jedisTemplate) {
		this.jedisTemplate = jedisTemplate;
	}

	@SuppressWarnings("unchecked")
	public void insertByValue(Object key, Object value, long timeOut, TimeUnit timeUnit) throws Exception {
		jedisTemplate.opsForValue().set(key, value, timeOut, timeUnit);
	}

	@SuppressWarnings("unchecked")
	public void insertByHash(Object key, Object hashKey, Object value) throws Exception {
		jedisTemplate.opsForHash().put(key, hashKey, value);
	}

	public Object getByValue(Object key) throws Exception {
		return jedisTemplate.opsForValue().get(key);
	}

	@SuppressWarnings("unchecked")
	public Object getByHash(Object key, Object hashKey) throws Exception {
		return jedisTemplate.opsForHash().get(key, hashKey);
	}
	
	@SuppressWarnings("unchecked")
	public void remove(Object key){
		jedisTemplate.delete(key);
	}
	
	@SuppressWarnings("unchecked")
	public void incr(Object key){
		jedisTemplate.opsForValue().increment(key, 1);
	}
}