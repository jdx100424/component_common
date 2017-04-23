/**   
 * @Description:(用一句话描述该类做什么)
 * @author Daxian.jiang
 * @Email  Daxian.jiang@vipshop.com
 * @Date 2015年7月29日 上午10:06:31
 * @Version V1.0   
 */
package com.maoshen.component.redis;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

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
	private static final long TIMEOUT = 10000L;
	private static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
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
		String keyStr = key.toString();
		jedisTemplate.opsForValue().set(keyStr, value, timeOut, timeUnit);
	}

	@SuppressWarnings("unchecked")
	public void insertByHash(Object key, Object hashKey, Object value,long timeOut, TimeUnit timeUnit) throws Exception {
		String keyStr = key.toString();
		String hashKeyStr = hashKey.toString();
		jedisTemplate.opsForHash().put(keyStr, hashKeyStr, value);
		jedisTemplate.expire(keyStr, timeOut, timeUnit);
	}
	
	@SuppressWarnings("unchecked")
	public void insertAllList(Object key,List list,long timeOut, TimeUnit timeUnit) throws Exception {
		String keyStr = key.toString();
		jedisTemplate.opsForList().leftPushAll(keyStr, list);
	}
	
	@SuppressWarnings("unchecked")
	public Object rightPopList(Object key) throws Exception {
		String keyStr = key.toString();
		return jedisTemplate.opsForList().rightPop(keyStr);
	}
	
	@SuppressWarnings("unchecked")
	public void insertAllHash(Object key, Map map,long timeOut, TimeUnit timeUnit) throws Exception {
		String keyStr = key.toString();
		jedisTemplate.opsForHash().putAll(keyStr, map);
		jedisTemplate.expire(keyStr, timeOut, timeUnit);
	}

	public Object getByValue(Object key) throws Exception {
		String keyStr = key.toString();
		return jedisTemplate.opsForValue().get(keyStr);
	}

	@SuppressWarnings("unchecked")
	public Object getByHash(Object key, Object hashKey) throws Exception {
		String keyStr = key.toString();
		String hashKeyStr = hashKey.toString();
		return jedisTemplate.opsForHash().get(keyStr, hashKeyStr);
	}
	
	@SuppressWarnings("unchecked")
	public void remove(Object key){
		String keyStr = key.toString();
		jedisTemplate.delete(keyStr);
	}
	
	@SuppressWarnings("unchecked")
	public void incr(Object key, long timeOut, TimeUnit timeUnit){
		String keyStr = key.toString();
		jedisTemplate.opsForValue().increment(keyStr, 1);
		jedisTemplate.expire(keyStr, timeOut, timeUnit);
	}
	
	/**
	 * redis lock
	 * @param key
	 */
	public void lock(Object key){
		String keyStr = key.toString();
		boolean isLock = lockLogic(keyStr);
		long last = System.currentTimeMillis();
		while(!isLock){
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				
			}
			
			if(System.currentTimeMillis()-last>TIMEOUT){
				//锁超时
				throw new RuntimeException("multi retry lock timeout!");
			}
			//重新获取锁
			isLock=lockLogic(keyStr);
		}
	}
	
	public void unlock(Object key){
		String keyStr = key.toString();
		remove(keyStr);
	}
	
	/**
	 * 获取锁lock
	 *@author vakinge
	 * @param lockId
	 * @param timeout 毫秒
	 * @return 获得lock ＝＝ true  
	 */
	@SuppressWarnings("unchecked")
	private boolean lockLogic(Object key){
		String keyStr = key.toString();
		Boolean result = false;
		try {		
			ValueOperations valueOperations = jedisTemplate.opsForValue();
			result = valueOperations.setIfAbsent(keyStr, "true");
			if(result){
				jedisTemplate.expire(keyStr, TIMEOUT+2000, TIME_UNIT);
			}
		} finally {
			
		}
		return result;
	}
}