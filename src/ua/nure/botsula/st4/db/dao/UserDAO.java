package ua.nure.botsula.st4.db.dao;

import java.util.List;

import ua.nure.botsula.st4.db.entity.User;
import ua.nure.botsula.st4.exception.DBException;

public interface UserDAO {
	/** Find user by login. */
	User findUserByLogin(String login) throws DBException;

	/** Find group of users. */
	List<User> findUserGroupByRoleId(int roleId) throws DBException;

	/** Update user role. */
	void changeUserRole(int userId, int roleId) throws DBException;

	/** Find user by id. */
	User findUserById(int id) throws DBException;

	/** Update user. */
	void updateUser(User user, int id) throws DBException;

	/** Insert user into database. */
	void insertUserIntoDatabase(User user) throws DBException;

	/** Delete teacher. */
	void deleteTeacher(int teacherId) throws DBException;

}
