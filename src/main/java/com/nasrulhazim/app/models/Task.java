package com.nasrulhazim.app.models;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Task implements Serializable {

	private static final AtomicInteger sequence = new AtomicInteger();

	private int id;

	private String name;

	private boolean is_done;

	public Task(String name) {
		this.id = sequence.incrementAndGet();
		this.name = name;
		this.is_done = false;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getIsDone() {
		return this.is_done;
	}
	
	public void setIsDone(boolean is_done) {
		this.is_done = is_done;
	}
}