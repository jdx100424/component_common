package com.maoshen.component.kafka.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MessageDto implements Serializable{
	private Object messageInfo;

	private String requestId;
	
	public MessageDto(Object messageInfo, String requestId) {
		super();
		this.messageInfo = messageInfo;
		this.requestId = requestId;
	}

	public Object getMessageInfo() {
		return messageInfo;
	}

	public void setMessageInfo(Object messageInfo) {
		this.messageInfo = messageInfo;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

}
