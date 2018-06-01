package model;

import java.sql.Timestamp;

import org.json.JSONObject;

public class Message extends Model {

	private Timestamp sentAt = new Timestamp(System.currentTimeMillis());
	private String body;
	private int memberId;
	private int channelId;
	
	@Override
	public JSONObject serializeJSON() {
		JSONObject json = super.serializeJSON();
		json.put("sent_at", sentAt.getTime());
		json.put("body", body);
		json.put("member_id", memberId);
		json.put("channel_id", channelId);
		return json;
	}

	@Override
	public void deserializeJSON(JSONObject json) {
		super.deserializeJSON(json);
		sentAt = new Timestamp(json.getLong("sent_at"));
		body = json.getString("body");
		memberId = json.getInt("member_id");
		channelId = json.getInt("channel_id");
	}

	public Timestamp getSentAt() {
		return sentAt;
	}

	public void setSentAt(Timestamp sentAt) {
		this.sentAt = sentAt;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	@Override
	public String toString() {
		return body;
	}
	
}
