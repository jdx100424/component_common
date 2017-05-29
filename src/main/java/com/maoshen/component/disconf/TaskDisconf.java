package com.maoshen.component.disconf;

import org.springframework.context.annotation.Configuration;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import com.baidu.disconf.client.common.update.IDisconfUpdate;

@Configuration
@DisconfFile(filename = "task.properties")
// @DisconfUpdateService(classes = {RedisDisconf.class})
public class TaskDisconf implements IDisconfUpdate {
	private String taskGray;

	@Override
	public void reload() throws Exception {
		// TODO Auto-generated method stub

	}

	
	@DisconfFileItem(name = "task.gray", associateField = "taskGray")
	public String getTaskGray() {
		return taskGray;
	}

	public void setTaskGray(String taskGray) {
		this.taskGray = taskGray;
	}
}
