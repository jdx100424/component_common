package com.maoshen.component.task;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.maoshen.component.kafka.util.KafkaUtil;
import com.maoshen.component.other.ResourceUtils;
import com.maoshen.component.task.util.TaskUtil;

/**
 * 使用REDIS控制负载均衡，只允许一台机操作
 * https://my.oschina.net/lese/blog/308709
 * @author daxian.jianglifesense.com
 *
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
	
	<!-- task -->
	<task:annotation-driven /> <!-- 定时器开关-->  
    <bean id="echoTask" class="com.maoshen.echo.task.EchoTask"></bean>  
    <task:scheduled-tasks>  
        <!--  
            这里表示的是每隔五秒执行一次  
        -->  
        <task:scheduled ref="echoTask" method="doJob" cron="0 0/1 * * * ?" />  
    </task:scheduled-tasks>  
 */
public abstract class BaseRedisTask extends BaseTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseRedisTask.class);
	@SuppressWarnings("rawtypes")
	@Autowired
	private RedisTemplate jedisTemplate;

	@SuppressWarnings("unchecked") 
	public void doJob() {
		String taskGray = ResourceUtils.get("task.gray","true");
		boolean isGray = TaskUtil.isTaskGray(taskGray);
		LOGGER.info("isGray:" + isGray+ ","+getName() + "_"+Thread.currentThread().getName() + "_" + this.getClass() + "_" + this.getName() + " this thread is running,date is:"+new Date());
		
		if(isGray){
			LOGGER.warn("name:{} is gray,not allow run the task",getName());
			return;
		}
		
		if (StringUtils.isNotBlank(getName())) {			
			if (jedisTemplate.opsForValue().setIfAbsent(getName(), "true")) {
				//如果用户没有定义超时时间或者值为0，会使用默认值
				if(getExpireTime()!=null && getExpireTime()>0){
					jedisTemplate.expire(getName(), getExpireTime(), TIME_UNIT);
				}else{
					jedisTemplate.expire(getName(), DEFAULT_EXPIRE_TIME, TIME_UNIT);
				}
				
				try {
					LOGGER.info(Thread.currentThread().getName() + "_" + this.getClass() + "_" + this.getName() + " run start,date is:"+new Date());
					timeTaskRun();
					LOGGER.info(Thread.currentThread().getName() + "_" + this.getClass() + "_" + this.getName() + " run end,date is:"+new Date());
				} catch (Exception e) {
					LOGGER.error(this.getClass() + "_" + this.getName() + " run timeTaskError", e);
				} finally {
					//jedisTemplate.delete(getName());
				}
			}else{
				LOGGER.warn("其他服务器的定时器正在运行。");
			}
		}else{
			LOGGER.error("不定义定时器在REDIS的KEY名字，定时器不给运行");
		}
	}
}
