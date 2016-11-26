package com.maoshen.component.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BaseDto implements Serializable {
	private Integer page;
	private Integer count;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}
