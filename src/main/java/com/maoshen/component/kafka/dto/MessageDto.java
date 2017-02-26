package com.maoshen.component.kafka.dto;

import java.io.Serializable;

import com.maoshen.component.rest.UserRestContext;

@SuppressWarnings("serial")
public class MessageDto implements Serializable{
	private Object messageInfo;

	private UserRestContext userRestContext;
	
	public MessageDto(){
		
	}
	
	public MessageDto(Object messageInfo) {
		super();
		this.messageInfo = messageInfo;
	}

	public Object getMessageInfo() {
		return messageInfo;
	}

	public void setMessageInfo(Object messageInfo) {
		this.messageInfo = messageInfo;
	}

	public UserRestContext getUserRestContext() {
		return userRestContext;
	}

	public void setUserRestContext(UserRestContext userRestContext) {
		this.userRestContext = userRestContext;
	}
}
