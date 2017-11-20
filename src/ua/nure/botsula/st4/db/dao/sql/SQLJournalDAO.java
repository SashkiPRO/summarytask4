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
import ua.nure.botsula.st4.db.bean.JournalBean;
import ua.nure.botsula.st4.db.dao.JournalDAO;
import ua.nure.botsula.st4.exception.DBException;
import ua.nure.botsula.st4.exception.Messages;

public class SQLJournalDAO implements JournalDAO {
	private static final String SQL_DELETE_JOURNAL_BY_COURSE_ID = "DELETE FROM journals WHERE course_id=?";
	private static final String SQL_INSERT_COURSE_JOURNAL = "INSERT INTO journals (course_id, user_id, mark) VALUES(?, ?, 0)";
	private static final String SQL_FIND_JOURNAL = "SELECT s.fname, s.lname, s.id, c.name, j.course_id, j.mark FROM users s INNER JOIN journals j ON j.user_id=s.id INNER JOIN courses c ON c.id=j.course_id AND c.id=?";
	private static final String SQL_UPDATE_JOURNAL = "UPDATE journals SET mark=? WHERE course_id=? AND user_id=?";
	private static final Logger LOG = Logger.getLogger(MysqlDAOFactory.class);
	@Override
	public List<JournalBean> findJournalByCourseId(int courseId) throws DBException {
		List<JournalBean> journal = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
		 LOG.trace("findJournaById ==> " + courseId);
			con = MysqlDAOFactory.createConnection();
			pstmt = con.prepareStatement(SQL_FIND_JOURNAL);
			pstmt.setInt(1, courseId);
			rs = pstmt.executeQuery();

			while (rs.next()) {

				journal.add(extractJournalBean(rs));
			}
			commit(con);
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return journal;
	}

	@Override
	public void updateCoursesJournal(List<JournalBean> newJournal) throws DBException {
		PreparedStatement pstmt = null;
		Connection con = null;

		try {
			int count;
			con = MysqlDAOFactory.createConnection();
			for (int i = 0; i < newJournal.size(); i++) {
				count = 1;
				pstmt = con.prepareStatement(SQL_UPDATE_JOURNAL);
				pstmt.setInt(count++, newJournal.get(i).getMark());
				pstmt.setInt(count++, newJournal.get(i).getCourseId());
				pstmt.setInt(count++, newJournal.get(i).getStudentId());
				pstmt.executeUpdate();
			}

			commit(con);
			 LOG.trace("Course journal, with was updated");
		} catch (SQLException ex) {
			rollback(con);
			 LOG.error(Messages.ERR_CANNOT_UPADATE_JOURNAL, ex);
			throw new DBException(Messages.ERR_CANNOT_UPADATE_COURSE, ex);
		} finally {
			close(con, pstmt, null);
		}

	}

	@Override
	public void insertIntoDataBaseJournal(int courseId, int studentId) throws DBException {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = MysqlDAOFactory.createConnection();
			pstmt = con.prepareStatement(SQL_INSERT_COURSE_JOURNAL);

			int i = 1;

			pstmt.setInt(i++, courseId);
			pstmt.setInt(i++, studentId);
			pstmt.executeUpdate();

			commit(con);
		} catch (SQLException ex) {
			rollback(con);
			 LOG.error("Cannot insert a journal ", ex);
		} finally {
			close(pstmt);
			close(con);
		}

	}

	@Override
	public void deleteJournalByCourseId(int course_id) throws DBException {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = MysqlDAOFactory.createConnection();
			pstmt = con.prepareStatement(SQL_DELETE_JOURNAL_BY_COURSE_ID);
			pstmt.setInt(1, course_id);
			pstmt.executeUpdate();
			commit(con);
		} catch (SQLException ex) {
			rollback(con);
			 LOG.error(Messages.ERR_CANNOT_DELETE_JOURNAL, ex);
			throw new DBException(Messages.ERR_CANNOT_DELETE_JOURNAL, ex);
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

	private JournalBean extractJournalBean(ResultSet rs) throws SQLException {
		JournalBean journal = new JournalBean();
		journal.setCourseId(rs.getInt(Fields.COURSE_ID));
		journal.setStudentFisrtName(rs.getString(Fields.USER_FIRST_NAME));
		journal.setStudentLastName(rs.getString(Fields.USER_LAST_NAME));
		journal.setStudentId(rs.getInt(Fields.ENTITY_ID));
		journal.setMark(rs.getInt(Fields.MARK));

		return journal;

	}

}
