package model;

import org.json.JSONObject;

public class Channel extends Model {

	private String name;
	
	@Override
	public JSONObject serializeJSON() {
		JSONObject json = super.serializeJSON();
		json.put("name", name);
		return json;
	}

	@Override
	public void deserializeJSON(JSONObject json) {
		super.deserializeJSON(json);
		name = json.getString("name");
	}

	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
