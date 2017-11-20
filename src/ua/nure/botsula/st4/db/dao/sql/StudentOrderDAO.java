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
import ua.nure.botsula.st4.db.bean.StudentOrderRequestBean;
import ua.nure.botsula.st4.db.dao.StudentOrderRequestDAO;
import ua.nure.botsula.st4.db.entity.CourseState;
import ua.nure.botsula.st4.exception.DBException;
import ua.nure.botsula.st4.exception.Messages;
import ua.nure.botsula.st4.util.DateUtils;

public class StudentOrderDAO implements StudentOrderRequestDAO{

	
	private static final String SQL_FIND_ALL_COURSE_BEAN = "SELECT c.id,c.name,c.theme, c.start_date,c.finish_date, c.student_count, c.status_id, t.teacher_id, u.fname, u.lname FROM courses c INNER JOIN teachers_course t ON t.course_id=c.id INNER JOIN users u ON u.id=t.teacher_id";
	private static final String SQL_FIND_STUDENT_SIGN = "SELECT *FROM sign_course WHERE user_id=? AND course_id=?";
	private static final String SQL_SIGN_TO_COURSE = "INSERT INTO sign_course SET user_id=?, course_id=?";
	private static final String SQL_DELETE_SIGN_COURSE_BY_COURSE_ID = "DELETE FROM sign_course WHERE course_id = ?";
	private static final String SQL_FIND_ALL_COURSE_BEAN_BY_SEARCH = "SELECT c.id,c.name,c.theme, c.start_date,c.finish_date, c.student_count, c.status_id, t.teacher_id, u.fname, u.lname FROM courses c INNER JOIN teachers_course t ON t.course_id=c.id INNER JOIN users u ON u.id=t.teacher_id WHERE c.name LIKE ? OR c.theme LIKE ? OR u.fname LIKE ? OR u.lname LIKE ?";
	private static final Logger LOG = Logger.getLogger(MysqlDAOFactory.class);
	@Override
	public void deleteSignByCourseId(int course_id) throws DBException {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = MysqlDAOFactory.createConnection();
			pstmt = con.prepareStatement(SQL_DELETE_SIGN_COURSE_BY_COURSE_ID);
			pstmt.setInt(1, course_id);
			pstmt.executeUpdate();
			commit(con);
		} catch (SQLException ex) {
			rollback(con);
			 LOG.error(Messages.ERR_CANNOT_DELETE_SIGN_COURSE,
			 ex);
			throw new DBException(Messages.ERR_CANNOT_DELETE_SIGN_COURSE, ex);
		} finally {
			close(con, pstmt, null);
		}
		
	}
	@Override
	public void insertIntoDatabaseSignCourse(int studentId, int courseId) throws DBException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = MysqlDAOFactory.createConnection();
			pstmt = con.prepareStatement(SQL_SIGN_TO_COURSE);

			int i = 1;
			pstmt.setInt(i++, studentId);
			pstmt.setInt(i++, courseId);
		new 	SQLCourseDAO().updateStudentCount(courseId);
		new SQLJournalDAO().insertIntoDataBaseJournal(courseId, studentId);
			pstmt.executeUpdate();

			commit(con);
		} catch (SQLException ex) {
			rollback(con);
			 LOG.error("Cannot insert sign course ", ex);
		} finally {
			close(pstmt);
			close(con);
		}
		
	}
	@Override
	public List<StudentOrderRequestBean> findStudentAllOrder(int studentId) throws DBException {
		List<StudentOrderRequestBean> courseList = new ArrayList<StudentOrderRequestBean>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;

		try {
			con = MysqlDAOFactory.createConnection();
		pstmt=con.prepareStatement(SQL_FIND_ALL_COURSE_BEAN);
			rs=pstmt.executeQuery();
			while (rs.next()) {
				courseList.add(extractRequstBean(rs, studentId));
				
			}
			commit(con);
		} catch (SQLException ex) {
			rollback(con);
			 LOG.error(Messages.ERR_CANNOT_OBTAIN_COURSES, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_COURSES, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return courseList;
	}

	@Override
	public boolean studentSignedOnCourse(int studentId, int courseId) throws DBException {
		int studId = studentId;
		int courId = courseId;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con =MysqlDAOFactory.createConnection();
			pstmt = con.prepareStatement(SQL_FIND_STUDENT_SIGN);
			int i = 1;
			pstmt.setInt(i++, studId);
			pstmt.setInt(i++, courId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			}
			commit(con);
		} catch (SQLException ex) {
			rollback(con);
		} finally {
			close(pstmt);
			close(con);
		}
		return false;
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


	private StudentOrderRequestBean extractRequstBean(ResultSet rs, int studentId) throws SQLException, DBException {
		StudentOrderRequestBean course = new StudentOrderRequestBean();
		course.setId(rs.getInt(Fields.ENTITY_ID));
		course.setCourse_name(rs.getString(Fields.COURSE_NAME));
		course.setCourse_theme(rs.getString(Fields.COURSE_THEME));
		course.setStartCourse(rs.getDate(Fields.START_DATE));
		course.setFinishCourse(rs.getDate(Fields.FINISH_DATE));
		course.setTeacher_id(rs.getInt(Fields.TEACHER_ID));
		course.setTeacherFirstName(rs.getString(Fields.USER_FIRST_NAME));
		course.setTeacherLastName(rs.getString(Fields.USER_LAST_NAME));
		course.setState(CourseState.getCourseState(new Integer(rs.getString(Fields.STATUS_ID))).toString());
		course.setDuration(
				DateUtils.fullDaysBetweenDates(rs.getDate(Fields.START_DATE), rs.getDate(Fields.FINISH_DATE)));
		course.setSigned(studentSignedOnCourse(studentId, course.getId()));
		course.setStudentCount(rs.getInt(Fields.STUDENT_COUNT));
		return course;
	}
	@Override
	public List<StudentOrderRequestBean> findCoursesBySearchParametr(String parameter, int studentId) throws DBException {
		List<StudentOrderRequestBean> courseList = new ArrayList<StudentOrderRequestBean>();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		StringBuilder findParam = new StringBuilder();
		try {
			con = MysqlDAOFactory.createConnection();
			pstmt = con.prepareStatement(SQL_FIND_ALL_COURSE_BEAN_BY_SEARCH);
			int i =1;
			findParam.append("%");
			findParam.append(parameter);
	
			pstmt.setString(i++, findParam.toString());
			pstmt.setString(i++, findParam.toString());
			pstmt.setString(i++, findParam.toString());
			pstmt.setString(i++, findParam.toString());
		
			rs = pstmt.executeQuery();
			while (rs.next()) {
				courseList.add(extractRequstBean(rs, studentId));
			}
			commit(con);
		} catch (SQLException ex) {
			rollback(con);
			 LOG.error(Messages.ERR_CANNOT_OBTAIN_COURSES, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_COURSES, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return courseList;
	}




}
