package ua.nure.botsula.st4.db.dao.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ua.nure.botsula.st4.db.Fields;
import ua.nure.botsula.st4.db.dao.UserDAO;
import ua.nure.botsula.st4.db.entity.User;
import ua.nure.botsula.st4.exception.DBException;
import ua.nure.botsula.st4.exception.Messages;

public class SQLUserDAO implements UserDAO {
	private static final String SQL_FIND_USER_BY_LOGIN = "SELECT * FROM users WHERE login=?";
	private static final String SQL_FIND_USER_BY_ID = "SELECT * FROM users WHERE id=?";
	private static final String SQL_FIND_USERS_BY_ROLE_ID = "SELECT * FROM users WHERE role_id=?";
	private static final String SQL_INSERT_STUDENT = "INSERT INTO users(fname,lname,login,password,role_id,email) VALUES(?,?,?,?,?,?)";

	private static final String SQL_UPDATE_USER = "UPDATE users SET fname=?, lname=?, login=?, password=?, email=? WHERE id=?";

	private static final String SQL_UPDATE_USER_ROLE = "UPDATE users SET role_id=? WHERE id=?";
	private static final String SQL_DELETE_TEACHER = "DELETE FROM users WHERE id=?";
	private static final Logger LOG = Logger.getLogger(MysqlDAOFactory.class);

	@Override
	public User findUserByLogin(String login) throws DBException {
		User user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			LOG.trace("findUserByLogin ==> " + login);
			con = MysqlDAOFactory.createConnection();
			pstmt = con.prepareStatement(SQL_FIND_USER_BY_LOGIN);
			pstmt.setString(1, login);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				user = extractUser(rs);
			}
			con.commit();

		} catch (SQLException ex) {
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			;
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
		} finally {
			try {
				con.close();
				pstmt.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return user;
	}

	@Override
	public List<User> findUserGroupByRoleId(int roleId) throws DBException {

		List<User> userList = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			LOG.trace("findGroupByRoleId ==> " + roleId);
			con = MysqlDAOFactory.createConnection();
			pstmt = con.prepareStatement(SQL_FIND_USERS_BY_ROLE_ID);
			pstmt.setInt(1, roleId);
			rs = pstmt.executeQuery();

			while (rs.next()) {

				userList.add(extractUser(rs));
			}
			con.commit();
		} catch (SQLException ex) {
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
		} finally {

			try {
				con.close();
				pstmt.close();
				rs.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
		return userList;
	}

	@Override
	public void changeUserRole(int userId, int roleId) throws DBException {
		PreparedStatement pstmt = null;
		Connection con = null;

		try {
			con = MysqlDAOFactory.createConnection();
			pstmt = con.prepareStatement(SQL_UPDATE_USER_ROLE);
			int i = 1;

			pstmt.setInt(i++, roleId);
			pstmt.setInt(i++, userId);
			pstmt.executeUpdate();
			con.commit();
			LOG.trace("User role was changed");
		} catch (SQLException ex) {
			try {
				con.rollback();
			} catch (SQLException e) {

				e.printStackTrace();
			}
			LOG.error(Messages.ERR_CANNOT_UPDATE_USER, ex);
			throw new DBException(Messages.ERR_CANNOT_UPADATE_COURSE, ex);
		} finally {
			try {
				con.close();
				pstmt.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

	}

	@Override
	public User findUserById(int id) throws DBException {
		User user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			LOG.trace("findUserById ==> " + id);
			con = MysqlDAOFactory.createConnection();
			pstmt = con.prepareStatement(SQL_FIND_USER_BY_ID);
			pstmt.setLong(1, id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				user = extractUser(rs);
			}
			con.commit();
		} catch (SQLException ex) {
			try {
				con.rollback();
			} catch (SQLException e) {

				e.printStackTrace();
			}
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
		} finally {
			try {
				con.close();
				pstmt.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return user;
	}

	@Override
	public void updateUser(User user, int id) throws DBException {
		PreparedStatement pstmt = null;
		Connection con = null;
		User newUser = user;
		try {
			con = MysqlDAOFactory.createConnection();
			pstmt = con.prepareStatement(SQL_UPDATE_USER);
			int i = 1;
			pstmt.setString(i++, newUser.getFname());
			pstmt.setString(i++, newUser.getLname());
			pstmt.setString(i++, newUser.getLogin());
			pstmt.setString(i++, newUser.getPassword());
			pstmt.setString(i++, newUser.getEmail());
			pstmt.setInt(i++, newUser.getId());
			pstmt.executeUpdate();
			commit(con);
			LOG.trace("User with id" + id + "was update");
		} catch (SQLException ex) {
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			LOG.error(Messages.ERR_CANNOT_UPDATE_USER, ex);
			throw new DBException(Messages.ERR_CANNOT_UPDATE_USER, ex);
		} finally {
			try {
				con.close();
				pstmt.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

	}

	@Override
	public void insertUserIntoDatabase(User user) throws DBException {
		User u = user;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = MysqlDAOFactory.createConnection();
			pstmt = con.prepareStatement(SQL_INSERT_STUDENT);

			int i = 1;
			pstmt.setString(i++, u.getFname());
			pstmt.setString(i++, u.getLname());
			pstmt.setString(i++, u.getLogin());
			pstmt.setString(i++, u.getPassword());
			pstmt.setInt(i++, u.getRoleid());
			pstmt.setString(i++, u.getEmail());
			System.out.println("OK");
			pstmt.executeUpdate();
			commit(con);
		} catch (SQLException ex) {
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			LOG.error("Cannot insert user into database ", ex);
		} finally {
			try {
				con.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	private User extractUser(ResultSet rs) throws SQLException {
		LOG.trace("Try to extract user:");
		User user = new User();
		user.setId((rs.getInt(Fields.ENTITY_ID)));
		user.setLogin(rs.getString(Fields.USER_LOGIN));
		user.setPassword(rs.getString(Fields.USER_PASSWORD));
		user.setFname(rs.getString(Fields.USER_FIRST_NAME));
		user.setLname(rs.getString(Fields.USER_LAST_NAME));
		user.setRoleid(rs.getInt(Fields.USER_ROLE_ID));
		user.setEmail(rs.getString(Fields.USER_EMAIL));
		return user;
	}

	@Override
	public void deleteTeacher(int teacherId) throws DBException {

		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = MysqlDAOFactory.createConnection();
			new SQLTeacherRequestDAO().deleteAllTeacherSignCourse(teacherId);
			pstmt = con.prepareStatement(SQL_DELETE_TEACHER);
			int i = 1;
			pstmt.setInt(i++, teacherId);
			pstmt.executeUpdate();

			commit(con);
			LOG.trace("Teacher with id " + teacherId + " was delete");
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_UPDATE_USER, ex);
			throw new DBException(Messages.ERR_CANNOT_UPDATE_USER, ex);
		} finally {
			close(con, pstmt, null);
		}

	}

	/**
	 * 
	 * Closes a connection.
	 * 
	 * @param con
	 *            Connection to be closed.
	 */
	private void close(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException ex) {
				LOG.error(Messages.ERR_CANNOT_CLOSE_CONNECTION, ex);
			}
		}
	}

	/**
	 * Closes a statement object.
	 */
	private void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException ex) {
				LOG.error(Messages.ERR_CANNOT_CLOSE_STATEMENT, ex);
			}
		}
	}

	/**
	 * Closes a result set object.
	 */
	private void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException ex) {
				LOG.error(Messages.ERR_CANNOT_CLOSE_RESULTSET, ex);
			}
		}
	}

	/**
	 * Closes resources.
	 */
	private void close(Connection con, Statement stmt, ResultSet rs) {
		close(rs);
		close(stmt);
		close(con);
	}

	/**
	 * Commit the given connection.
	 * 
	 * @param con
	 *            Connection to be commited.
	 */
	private void commit(Connection con) {
		if (con != null) {
			try {
				con.commit();
			} catch (SQLException ex) {
				LOG.error("Cannot rollback transaction", ex);
			}
		}
	}

	/**
	 * Rollbacks a connection.
	 * 
	 * @param con
	 *            Connection to be rollbacked.
	 */
	private void rollback(Connection con) {
		if (con != null) {
			try {
				con.rollback();
			} catch (SQLException ex) {
				LOG.error("Cannot rollback transaction", ex);
			}
		}
	}

}
