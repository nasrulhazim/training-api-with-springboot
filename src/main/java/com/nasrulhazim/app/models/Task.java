package com.nasrulhazim.app.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tasks")
public class Task implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;

	private boolean is_done;

	public Task() {
		this.is_done = false;
	}

	public long getId() {
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