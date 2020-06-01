package com;

import java.net.Socket;

public class SocketClass {
	private int id;//�����id
	private int userId;//���˵�id
	private int friendId;//��ƥ��id
	private Socket socket;//socket
	
	public SocketClass(int id,int userId,Socket socket) {
		this.id=id;
		this.userId=userId;
		this.socket=socket;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getFriendId() {
		return friendId;
	}
	public void setFriendId(int friendId) {
		this.friendId = friendId;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	@Override
	public String toString() {
		return "SocketClass [id=" + id + ", userId=" + userId + ", friendId=" + friendId + "]";
	}
	
	
	
	
	
	
	

}
