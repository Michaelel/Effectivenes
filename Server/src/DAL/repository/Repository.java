package repository;

import java.util.List;

import domain.Model;

public interface Repository<T extends Model> {
	void create(T entry) throws Exception;
	T readById(int id);
	List<T> readAll() throws Exception;
	void update(T entry) throws Exception;
	void delete(int id);
}
