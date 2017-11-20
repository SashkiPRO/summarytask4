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
import ua.nure.botsula.st4.db.dao.CourseDAO;
import ua.nure.botsula.st4.db.entity.Course;
import ua.nure.botsula.st4.exception.DBException;
import ua.nure.botsula.st4.exception.Messages;

public class SQLCourseDAO implements CourseDAO {
	private static final String SQL_FIND_ALL_COURSES = "SELECT * FROM courses";
	private static final String SQL_FIND_COURSE_BY_ID = "SELECT * FROM courses WHERE id=?";
	private static final String SQL_DELETE_COURSE_BY_ID = "DELETE FROM courses WHERE id=?";
	private static final String SQL_INSERT_COURSE = "INSERT INTO courses (name,theme, start_date, finish_date, status_id, student_count) VALUES(?,?,?,?,?,?)";
	private static final String SQL_UPDATE_STUDENT_COUNT = "UPDATE courses SET student_count=? WHERE id=?";
	private static final String SQL_FIND_COURSE_BY_TEACHER_ID = "SELECT  c.id, c.name, c.theme, c.start_date, c.finish_date, c.status_id, c.student_count FROM courses c INNER JOIN teachers_course t ON c.id=t.course_id AND t.teacher_id=?";
	private static final String SQL_FIND_ALL_FREE_COURSES = "SELECT courses.id, courses.name, courses.theme, courses.start_date, courses.finish_date, courses.status_id, courses.student_count FROM courses WHERE courses.id NOT IN (SELECT teachers_course.course_id FROM teachers_course)";
	private static final String SQL_FIND_COURSE_STUDENTS_AND_STATUS = "SELECT * FROM courses c INNER JOIN sign_course s ON c.id=s.course_id WHERE s.user_id=? AND c.status_id=?";
	private static final String SQL_UPDATE_COURSE_STATE = "UPDATE courses SET status_id=? WHERE id=?";
	private static final String SQL_UPDATE_COURSE = "UPDATE courses SET name=?, theme=?, start_date=?, finish_date=?, status_id=? WHERE id=?";
	/** The logger. */
	private static final Logger LOG = Logger.getLogger(MysqlDAOFactory.class);

	@Override
	public List<Course> findAllTeachersCoursesById(int teacherId) throws DBException {
		List<Course> courseList = new ArrayList<Course>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = MysqlDAOFactory.createConnection();
		;
		try {
			LOG.trace("findCoursesById ==> " + teacherId);

			pstmt = con.prepareStatement(SQL_FIND_COURSE_BY_TEACHER_ID);
			pstmt.setInt(1, teacherId);
			rs = pstmt.executeQuery();

			while (rs.next()) {

				courseList.add(extractCourse(rs));
			}
			commit(con);
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return courseList;
	}

	@Override
	public void updateStudentCount(int courseId) throws DBException {
		PreparedStatement pstmt = null;
		Connection con = null;
		Course course;

		try {

			con = MysqlDAOFactory.createConnection();
			course = findCourseById(courseId);
			int oldCount = course.getCountStudent();
			System.out.println(oldCount + "oldCount");

			pstmt = con.prepareStatement(SQL_UPDATE_STUDENT_COUNT);
			int i = 1;

			pstmt.setInt(i++, ++oldCount);
			pstmt.setInt(i++, courseId);
			pstmt.executeUpdate();
			commit(con);

		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_UPDATE_STUDENT_COUNT, ex);
			throw new DBException(Messages.ERR_CANNOT_UPADATE_COURSE, ex);
		} finally {
			close(con, pstmt, null);
		}

	}

	@Override
	public void updateCourseState(int courseStateId, int courseId) throws DBException {
		PreparedStatement pstmt = null;
		Connection con = null;
		Course course = new Course();
		try {

			con = MysqlDAOFactory.createConnection();
			course = findCourseById(courseId);

			pstmt = con.prepareStatement(SQL_UPDATE_COURSE_STATE);
			int i = 1;

			pstmt.setInt(i++, courseStateId);
			pstmt.setInt(i++, courseId);
			pstmt.executeUpdate();
			commit(con);
			LOG.trace("Course state, with id= " + courseId + "was updated");
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_UPDATE_COURSE_STATE, ex);
			throw new DBException(Messages.ERR_CANNOT_UPDATE_COURSE_STATE, ex);
		} finally {
			close(con, pstmt, null);
		}

	}

	@Override
	public Course findCourseById(int id) throws DBException {
		Course course = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;

		try {
			con = MysqlDAOFactory.createConnection();
			pstmt = con.prepareStatement(SQL_FIND_COURSE_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				course = extractCourse(rs);
				System.out.println("EXTRA");

			}
			commit(con);
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_COURSES, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_COURSES, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return course;
	}

	@Override
	public List<Course> findAllCourses() throws DBException {
		List<Course> courseList = new ArrayList<Course>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con;
		con = MysqlDAOFactory.createConnection();
		try {

			pstmt = con.prepareStatement(SQL_FIND_ALL_COURSES);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				courseList.add(extractCourse(rs));
			}
			commit(con);
		} catch (SQLException ex) {
			try {
				con.rollback();
			} catch (SQLException e) {
			
				e.printStackTrace();
			}
			LOG.error(Messages.ERR_CANNOT_OBTAIN_COURSES, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_COURSES, ex);
		} finally {

			try {
				con.close();
				pstmt.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return courseList;

	}

	@Override
	public List<Course> findAllFreeCourses() throws DBException {
		List<Course> courseList = new ArrayList<Course>();
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = null;

		try {
			con = MysqlDAOFactory.createConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL_FIND_ALL_FREE_COURSES);
			while (rs.next()) {
				courseList.add(extractCourse(rs));
			}
			commit(con);
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_COURSES, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_COURSES, ex);
		} finally {
			close(con, stmt, rs);
		}
		return courseList;
	}

	@Override
	public List<Course> findCoursesByStudentIdAndState(int studentId, int stateId) throws DBException {
		List<Course> courseList = new ArrayList<Course>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;

		try {
			con = MysqlDAOFactory.createConnection();
			pstmt = con.prepareStatement(SQL_FIND_COURSE_STUDENTS_AND_STATUS);
			int i = 1;
			pstmt.setInt(i++, studentId);
			pstmt.setInt(i++, stateId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				courseList.add(extractCourse(rs));
			}
			commit(con);
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_ORDERS_BY_USER_AND_STATUS_ID, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_COURSES, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return courseList;
	}

	@Override
	public void deleteCourseById(int course_id) throws DBException {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = MysqlDAOFactory.createConnection();
			new StudentOrderDAO().deleteSignByCourseId(course_id);
			new SQLJournalDAO().deleteJournalByCourseId(course_id);
			new SQLTeacherRequestDAO().deleteTeacherCourseByCourseId(course_id);
			pstmt = con.prepareStatement(SQL_DELETE_COURSE_BY_ID);
			pstmt.setInt(1, course_id);
			pstmt.executeUpdate();
			commit(con);
		} catch (SQLException ex) {
			rollback(con);
			LOG.error(Messages.ERR_CANNOT_DELETE_COURSE_BY_ID, ex);
			throw new DBException(Messages.ERR_CANNOT_DELETE_COURSE_BY_ID, ex);
		} finally {
			close(con, pstmt, null);
		}

	}

	@Override
	public void insertCourseIntoDataBase(Course course) throws DBException {
		Course c = course;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = MysqlDAOFactory.createConnection();
			pstmt = con.prepareStatement(SQL_INSERT_COURSE);

			int i = 1;
			pstmt.setString(i++, c.getName());
			pstmt.setString(i++, c.getTheme());
			pstmt.setDate(i++, c.getStartDate());
			pstmt.setDate(i++, c.getFinishDate());
			pstmt.setInt(i++, c.getState());
			pstmt.setInt(i++, c.getCountStudent());
			System.out.println("OK");
			pstmt.executeUpdate();
			commit(con);
		} catch (SQLException ex) {
			rollback(con);
			LOG.error("Cannot insert a course request ", ex);
		} finally {
			close(pstmt);
			close(con);
		}

	}

	@Override
	public void updateCourse(Course newCourse, int courseId) throws DBException {
		LOG.trace("Update course");
		PreparedStatement pstmt = null;
		Connection con = null;
		Course course = newCourse;
		try {
			con = MysqlDAOFactory.createConnection();
			pstmt = con.prepareStatement(SQL_UPDATE_COURSE);
			int i = 1;
			pstmt.setString(i++, course.getName());
			pstmt.setString(i++, course.getTheme());
			pstmt.setDate(i++, course.getStartDate());
			pstmt.setDate(i++, course.getFinishDate());
			pstmt.setInt(i++, course.getState());
			pstmt.setInt(i++, courseId);
			pstmt.executeUpdate();
			commit(con);
			// LOG.trace("Room, with id= " + roomId + " turned in requestState="
			// + conditionId);
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
				// LOG.error(Messages.ERR_CANNOT_CLOSE_CONNECTION, ex);
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

	private Course extractCourse(ResultSet rs) throws SQLException {
		Course course = new Course();
		course.setId(rs.getInt(Fields.ENTITY_ID));
		course.setName(rs.getString(Fields.COURSE_NAME));
		course.setTheme(rs.getString(Fields.COURSE_THEME));
		course.setStartDate(rs.getDate(Fields.START_DATE));
		course.setFinishDate(rs.getDate(Fields.FINISH_DATE));

		course.setState(rs.getInt(Fields.STATUS_ID));
		course.setCountStudent(rs.getInt(Fields.STUDENT_COUNT));
		return course;
	}

}
