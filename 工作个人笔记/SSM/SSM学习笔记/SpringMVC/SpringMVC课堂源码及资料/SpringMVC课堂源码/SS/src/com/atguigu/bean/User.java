package com.atguigu.bean;

import org.springframework.stereotype.Component;

@Component
public class User {

	private Integer id;
	
	private String username;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + "]";
	}

	public User() {
		System.out.println("User===>构造方法");
	}
	
	
}
