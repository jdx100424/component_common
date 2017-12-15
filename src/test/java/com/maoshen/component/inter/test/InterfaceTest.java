package com.maoshen.component.inter.test;

public interface InterfaceTest {
	public String say();
	
	default public String get(){
		return say() + ":jdx";
	}
}
