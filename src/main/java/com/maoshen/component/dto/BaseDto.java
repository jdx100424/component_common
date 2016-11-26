package com.maoshen.component.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BaseDto implements Serializable {
	private int page;
	private int count;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
