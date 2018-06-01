package repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import domain.Channel;

public class ChannelRepository implements Repository<Channel> {

	@Override
	public void create(Channel entry) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Channel readById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Channel entry) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Channel> readAll() throws SQLException {
		List<Channel> list = new ArrayList<>();
		Connection connection = SqliteDB.getConnection();

		try (Statement stmt = connection.createStatement()) {
			try (ResultSet rs = stmt.executeQuery("SELECT * FROM 'channel'")) {
				while (rs.next()) {
					Channel channel = new Channel();
					channel.setId(rs.getInt("id"));
					channel.setName(rs.getString("name"));
					list.add(channel);
				}
			}
		}
		return list;
	}

}
