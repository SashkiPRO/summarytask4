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
import ua.nure.botsula.st4.db.bean.CourseRequestBean;
import ua.nure.botsula.st4.db.dao.CourseRequestBeanDAO;
import ua.nure.botsula.st4.db.entity.CourseState;
import ua.nure.botsula.st4.exception.DBException;
import ua.nure.botsula.st4.exception.Messages;
import ua.nure.botsula.st4.util.DateUtils;

public class CourseRequestDAO implements CourseRequestBeanDAO {
	private static final String SQL_FIND_ALL_COURSE_BEAN = "SELECT c.id,c.name,c.theme, c.start_date,c.finish_date, c.student_count, c.status_id, t.teacher_id, u.fname, u.lname FROM courses c INNER JOIN teachers_course t ON t.course_id=c.id INNER JOIN users u ON u.id=t.teacher_id";
	private static final String SQL_FIND_ALL_COURSE_BEAN_BY_SEARCH = "SELECT c.id,c.name,c.theme, c.start_date,c.finish_date, c.student_count, c.status_id, t.teacher_id, u.fname, u.lname FROM courses c INNER JOIN teachers_course t ON t.course_id=c.id INNER JOIN users u ON u.id=t.teacher_id WHERE c.name LIKE ? OR c.theme LIKE ? OR u.fname LIKE ? OR u.lname LIKE ?";
	/** The logger. */
	private static final Logger LOG = Logger.getLogger(MysqlDAOFactory.class);
	@Override
	public List<CourseRequestBean> findAllCoursesBean() throws DBException {
		List<CourseRequestBean> courseList = new ArrayList<CourseRequestBean>();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = MysqlDAOFactory.createConnection();
			pstmt = con.prepareStatement(SQL_FIND_ALL_COURSE_BEAN);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				courseList.add(extractCourseRequestBean(rs));
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
	public List<CourseRequestBean> findCoursesBySearchParametr(String parameter) throws DBException {
		List<CourseRequestBean> courseList = new ArrayList<CourseRequestBean>();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		StringBuilder findParam = new StringBuilder();
		try {
			con = MysqlDAOFactory.createConnection();
			pstmt = con.prepareStatement(SQL_FIND_ALL_COURSE_BEAN_BY_SEARCH);
			int i = 1;
			findParam.append("%");
			findParam.append(parameter);

			pstmt.setString(i++, findParam.toString());
			pstmt.setString(i++, findParam.toString());
			pstmt.setString(i++, findParam.toString());
			pstmt.setString(i++, findParam.toString());

			rs = pstmt.executeQuery();
			while (rs.next()) {
				courseList.add(extractCourseRequestBean(rs));
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

	private CourseRequestBean extractCourseRequestBean(ResultSet rs) throws SQLException {
		CourseRequestBean course = new CourseRequestBean();
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
		course.setStudentCount(rs.getInt(Fields.STUDENT_COUNT));
		return course;
	}

}
