package domain;

import org.json.JSONObject;

public class Member extends Model {
	private String name;
	private String surname;
	private String secondname;

	private String email;

	private String login;
	private String password;

	@Override
	public JSONObject serializeJSON() {
		JSONObject json = super.serializeJSON();
		json.put("name", name);
		json.put("surname", surname);
		json.put("secondname", secondname);
		json.put("email", email);
		json.put("login", login);
		json.put("password", password);
		return json;
	}

	@Override
	public void deserializeJSON(JSONObject json) {
		super.deserializeJSON(json);
		name = json.getString("name");
		surname = json.getString("surname");
		secondname = json.getString("secondname");
		email = json.getString("email");
		login = json.getString("login");
		password  = json.getString("password");
	}
	
	@Override
	public String toString() {
		return login;
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

	public String getSecondname() {
		return secondname;
	}

	public void setSecondname(String secondname) {
		this.secondname = secondname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
