package it.unisa.metric.web.utils.login;

import java.io.Serializable;

public class Account implements Serializable{
	private String name;
	private String surname;
	private String company;
	private String username;
	private String password;

	public Account(String name, String surname, String company, String username, String password) {

		this.name = name;
		this.surname = surname;
		this.company = company;
		this.username = username;
		this.password = password;
	}
	public Account(String username, String password) {
		this.username = username;
		this.password = password;
	}
	public Account() {

	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean equals(Object newAccount) {
		Account u= (Account) newAccount;
		return (
				u.username.equals(this.username) &&
				u.password.equals(this.password)
			);
		
	}

}
