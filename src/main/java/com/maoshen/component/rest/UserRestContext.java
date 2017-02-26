package com.maoshen.component.rest;

public class UserRestContext extends BaseRestContext{
	public UserRestContext(){
		
	}
	
	private static final ThreadLocal<UserRestContext> local = new ThreadLocal<UserRestContext>(){
		protected UserRestContext initialValue(){
			return new UserRestContext();
		};
	};
	
	public static UserRestContext get(){
		return local.get();
	}
	
	public static void clear(){
		local.remove();
	}
	
	private Long userId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
