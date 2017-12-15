package com.maoshen.component.inter.test;

public class InterfaceTestImpl implements InterfaceTest{

	@Override
	public String say() {
		// TODO Auto-generated method stub
		return "gogo";
	}

	public static void main(String []args){
		InterfaceTest t = new InterfaceTestImpl();
		System.out.println(t.get());
	}
}
