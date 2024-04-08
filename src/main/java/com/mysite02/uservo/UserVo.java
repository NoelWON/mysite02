package com.mysite02.uservo;

public class UserVo {
	// 객체 안에 데이터를 보호하기 위해 private 로 한다.
	// 다른 객체에서 접근을 막기 위해서 즉 get method 로만 부르기 위해서
	private long no;
	private String name;
	private String email;
	private String password;
	private String gender;

	public long getNo() {
		return no;
	}

	public void setNo(long no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

}
