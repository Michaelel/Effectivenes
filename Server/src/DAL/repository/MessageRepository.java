package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import domain.Message;

public class MessageRepository implements Repository<Message> {

	@Override
	public void create(Message entry) throws SQLException {
		Connection connection = SqliteDB.getConnection();

		try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO 'message' "
				+ "(body, member_id, channel_id) " + "VALUES (?, ?, ?)")) {
			stmt.setString(1, entry.getBody());
			stmt.setInt(2, entry.getMemberId());
			stmt.setInt(3, entry.getChannelId());
			stmt.executeUpdate();
		}
	}

	@Override
	public Message readById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Message entry) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Message> readAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<domain.Message> readAllByChannelId(int id) throws SQLException {
		List<domain.Message> list = new ArrayList<>();
		Connection connection = SqliteDB.getConnection();

		try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM 'message' "
				+ "WHERE channel_id=? ORDER BY sent_at ASC")) {
			stmt.setInt(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					domain.Message message = new domain.Message();
					message.setId(rs.getInt("id"));
					message.setSentAt(new Timestamp(rs.getLong("sent_at")));
					message.setBody(rs.getString("body"));
					message.setChannelId(rs.getInt("channel_id"));
					message.setMemberId(rs.getInt("member_id"));
					list.add(message);
				}
			}
		}
		return list;
	}

}
