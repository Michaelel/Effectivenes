package model;

import org.json.JSONObject;

public abstract class Model {
	protected int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public JSONObject serializeJSON() {
		JSONObject json = new JSONObject();
		json.put("id", id);
		return json;
	}

	public void deserializeJSON(JSONObject json) {
		id = json.getInt("id");
	}
}
