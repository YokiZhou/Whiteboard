package edu.unimelb.core;

import java.util.ArrayList;
import java.util.List;

public class User
{
	
	private String username;
	
	private Boolean isAdmin;
	
	public User()
	{
		
	}
	
	public User(String username)
	{
		this.username = username;
		this.isAdmin = false;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public Boolean getIsAdmin() {
		return isAdmin;
	}
	
	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public static List<User> getUsers()
	{
		List<User> onlineUsers = new ArrayList<User>();
		
		return onlineUsers;
	}
}
