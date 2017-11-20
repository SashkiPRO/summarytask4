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
import ua.nure.botsula.st4.db.bean.TeacherRequestBean;

import ua.nure.botsula.st4.db.dao.TeacherRequestDAO;
import ua.nure.botsula.st4.exception.DBException;
import ua.nure.botsula.st4.exception.Messages;

public class SQLTeacherRequestDAO implements TeacherRequestDAO {

	private static final String SQL_FIND_TEACERS_COURSES = "SELECT t.id, t.fname, t.lname,t.email, c.name, d.course_id FROM users t INNER JOIN teachers_course d ON d.teacher_id=t.id INNER JOIN courses c ON c.id=d.course_id";
	private static final String SQL_INSERT_COURSE_TEACHER_SIGN = "INSERT INTO teachers_course (teacher_id, course_id) VALUES(?,?)";
	private static final String SQL_DELETE_COURSE_TEACHER_SIGN = "DELETE FROM teachers_course WHERE course_id=? AND teacher_id=?";
	private static final String SQL_UPDATE_COURSE_TEACHER_SIGN = "UPDATE teachers_course SET course_id=? WHERE course_id=? AND teacher_id=?";
	private static final String SQL_DELETE_ALL_TEACHER_COURSES = "DELETE FROM teachers_course WHERE teacher_id=?";
	private static final String SQL_DELETE_TEACHER_COURSE_BY_COURSE_ID = "DELETE FROM teachers_course WHERE course_id = ?";
	private static final Logger LOG = Logger.getLogger(MysqlDAOFactory.class);

	@Override
	public void deleteTeacherCourseByCourseId(int course_id) throws DBException {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = MysqlDAOFactory.createConnection();
			pstmt = con.prepareStatement(SQL_DELETE_TEACHER_COURSE_BY_COURSE_ID);
			pstmt.setInt(1, course_id);
			pstmt.executeUpdate();
			commit(con);
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_DELETE_SIGN_COURSE, ex);
			throw new DBException(Messages.ERR_CANNOT_DELETE_SIGN_COURSE, ex);
		} finally {
			close(con, pstmt, null);
		}

	}

	@Override
	public List<TeacherRequestBean> findAllTeachersCourses() throws DBException {
		List<TeacherRequestBean> teacherList = new ArrayList<TeacherRequestBean>();
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = null;

		try {
			con = MysqlDAOFactory.createConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL_FIND_TEACERS_COURSES);
			while (rs.next()) {
				teacherList.add(extractTacherRequestBean(rs));
			}
			commit(con);
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_COURSES, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_COURSES, ex);
		} finally {
			close(con, stmt, rs);
		}
		return teacherList;
	}

	@Override
	public void insertCourseToTeacherSign(int teacher_id, int course_id) throws DBException {
		int c_id = course_id;
		int t_id = teacher_id;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = MysqlDAOFactory.createConnection();
			pstmt = con.prepareStatement(SQL_INSERT_COURSE_TEACHER_SIGN);

			int i = 1;

			pstmt.setInt(i++, t_id);
			pstmt.setInt(i++, c_id);
			System.out.println("OK");
			pstmt.executeUpdate();
			commit(con);
		} catch (SQLException ex) {
			rollback(con);
			LOG.error("Cannot insert a sign course ", ex);
		} finally {
			close(pstmt);
			close(con);
		}

	}

	@Override
	public void updateTeacherSignCourse(int teacherId, int courseId, int newCourseId) throws DBException {
		LOG.trace("Update teacher sign ==> " + courseId);
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = MysqlDAOFactory.createConnection();
			pstmt = con.prepareStatement(SQL_UPDATE_COURSE_TEACHER_SIGN);
			int i = 1;
			pstmt.setInt(i++, newCourseId);
			pstmt.setInt(i++, courseId);
			pstmt.setInt(i++, teacherId);
			pstmt.executeUpdate();

			commit(con);
			LOG.trace("Teacher, with id= " + teacherId + " start teacher of course by id=" + newCourseId);
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_UPADATE_COURSE, ex);
			throw new DBException(Messages.ERR_CANNOT_UPADATE_COURSE, ex);
		} finally {
			close(con, pstmt, null);
		}
	}

	@Override
	public void deleteTeacherSignCourse(int teacherId, int courseId) throws DBException {
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = MysqlDAOFactory.createConnection();
			pstmt = con.prepareStatement(SQL_DELETE_COURSE_TEACHER_SIGN);
			int i = 1;
			pstmt.setInt(i++, courseId);
			pstmt.setInt(i++, teacherId);
			pstmt.executeUpdate();

			commit(con);
			LOG.trace("Teacher, with id= " + teacherId + " not teacher of course=" + courseId);
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_UPADATE_COURSE, ex);
			throw new DBException(Messages.ERR_CANNOT_UPADATE_COURSE, ex);
		} finally {
			close(con, pstmt, null);
		}

	}

	@Override
	public void deleteAllTeacherSignCourse(int teacherId) throws DBException {
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = MysqlDAOFactory.createConnection();
			pstmt = con.prepareStatement(SQL_DELETE_ALL_TEACHER_COURSES);
			int i = 1;
			pstmt.setInt(i++, teacherId);
			pstmt.executeUpdate();

			commit(con);
			LOG.trace("All signed order by teacher id= " + teacherId + " was deleted");
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_UPADATE_COURSE, ex);
			throw new DBException(Messages.ERR_CANNOT_UPADATE_COURSE, ex);
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

	private TeacherRequestBean extractTacherRequestBean(ResultSet rs) throws SQLException {
		TeacherRequestBean teacher = new TeacherRequestBean();
		teacher.setId(rs.getInt(Fields.ENTITY_ID));
		teacher.setCourseName(rs.getString(Fields.COURSE_NAME));
		teacher.setEmail(rs.getString(Fields.USER_EMAIL));
		teacher.setFirstName(rs.getString(Fields.USER_FIRST_NAME));
		teacher.setLastName(rs.getString(Fields.USER_LAST_NAME));
		teacher.setCourseId(rs.getInt(Fields.COURSE_ID));

		return teacher;

	}

}
