package ua.nure.botsula.st4.db.dao.sql;

import java.sql.Connection;

import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import ua.nure.botsula.st4.db.dao.CourseDAO;
import ua.nure.botsula.st4.db.dao.CourseRequestBeanDAO;
import ua.nure.botsula.st4.db.dao.DAOFactory;
import ua.nure.botsula.st4.db.dao.JournalDAO;
import ua.nure.botsula.st4.db.dao.StudentOrderRequestDAO;
import ua.nure.botsula.st4.db.dao.TeacherRequestDAO;
import ua.nure.botsula.st4.db.dao.UserDAO;
import ua.nure.botsula.st4.exception.DBException;
import ua.nure.botsula.st4.exception.Messages;

/**
 * Extension of the abstract DAO-Factory-class for the MYSQL-Database.
 */
public class MysqlDAOFactory extends DAOFactory {
	/**
	 * Constructor MysqlDAOFactory.
	 */
	public MysqlDAOFactory() {
		super();
	}

	/** The logger. */
	private static final Logger LOG = Logger.getLogger(MysqlDAOFactory.class);

	/** Data sourse for connection. */
	private static DataSource ds;

	/**
	 * Method to create a Connection on the mysql-database.
	 * 
	 * @return the Connection.
	 */
	public static Connection createConnection() throws DBException {
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			// facultet_db - the name of data source
			ds = (DataSource) envContext.lookup("jdbc/facultet_db");
			LOG.trace("Data source ==> " + ds);
		} catch (NamingException ex) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_DATA_SOURCE, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_DATA_SOURCE, ex);
		}
		Connection con = null;
		try {
			con = ds.getConnection();
			LOG.trace("getConnection ==> " + con);
		} catch (SQLException ex) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, ex);
		}
		return con;
	}

	@Override
	public UserDAO getUserDAO() {
		return new SQLUserDAO();
	}

	@Override
	public TeacherRequestDAO getTeacherRequestDAO() {
	
		return new SQLTeacherRequestDAO();
	}

	@Override
	public StudentOrderRequestDAO getStudentOrderRequestDAO() {

		return new StudentOrderDAO();
	}

	@Override
	public JournalDAO getJournalDAO() {
		return new SQLJournalDAO();
	}

	@Override
	public CourseDAO getCourseDAO() {
		return new SQLCourseDAO();
	}

	@Override
	public CourseRequestBeanDAO getCourseRequestBeanDAO() {
		return new CourseRequestDAO();
	}
}
