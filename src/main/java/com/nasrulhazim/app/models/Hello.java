package com.nasrulhazim.app.models;

public class Hello {
	private String name;
	public Hello(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public String sayHello() {
		return "Hello: " + this.name;
	}
}