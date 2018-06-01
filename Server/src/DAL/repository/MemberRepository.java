package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import domain.Member;

public class MemberRepository implements Repository<Member> {

	@Override
	public void create(Member entry) throws SQLException {
		Connection connection = SqliteDB.getConnection();

		try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO 'member' "
				+ "(name, surname, secondname, email, login, password) " + "VALUES (?, ?, ?, ? ,? ,?)")) {
			stmt.setString(1, entry.getName());
			stmt.setString(2, entry.getSurname());
			stmt.setString(3, entry.getSecondname());
			stmt.setString(4, entry.getEmail());
			stmt.setString(5, entry.getLogin());
			stmt.setString(6, entry.getPassword());
			stmt.executeUpdate();
		}
	}

	@Override
	public Member readById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Member entry) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub

	}

	public Member readMember(String login, String password) throws SQLException {
		Connection connection = SqliteDB.getConnection();

		try (PreparedStatement stmt = connection
				.prepareStatement("SELECT * FROM 'member' WHERE login=? AND password=?")) {
			stmt.setString(1, login);
			stmt.setString(2, password);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					Member member = new Member();
					member.setId(rs.getInt("id"));
					member.setName(rs.getString("name"));
					member.setSurname(rs.getString("surname"));
					member.setSecondname(rs.getString("secondname"));
					member.setEmail(rs.getString("email"));
					member.setLogin(rs.getString("login"));
					member.setPassword(rs.getString("password"));
					return member;
				}
			}
		}
		return null;
	}

	@Override
	public List<Member> readAll() throws SQLException {
		List<Member> list = new ArrayList<>();
		Connection connection = SqliteDB.getConnection();

		try (Statement stmt = connection.createStatement()) {
			try (ResultSet rs = stmt.executeQuery("SELECT * FROM 'member'")) {
				while (rs.next()) {
					Member member = new Member();
					member.setId(rs.getInt("id"));
					member.setName(rs.getString("name"));
					member.setSurname(rs.getString("surname"));
					member.setSecondname(rs.getString("secondname"));
					member.setEmail(rs.getString("email"));
					member.setLogin(rs.getString("login"));
					member.setPassword(rs.getString("password"));
					list.add(member);
				}
			}
		}
		return list;
	}
}
