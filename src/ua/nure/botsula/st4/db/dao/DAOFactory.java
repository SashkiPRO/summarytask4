package ua.nure.botsula.st4.db.dao;

import ua.nure.botsula.st4.db.dao.sql.MysqlDAOFactory;
import ua.nure.botsula.st4.exception.DBException;

public abstract class DAOFactory {
	/** Static member for mysql-factory. */
	public static final int MYSQL = 0;
	/** Static member for derby-factory. */
	public static final int DERBY = 1;

	/**
	 * Factory-method
	 * 
	 * @param database
	 *            the database to choose
	 * @return a matching factory
	 * @author Alexandr Botsula;
	 */
	public static DAOFactory getDAOFactory(int database) {
		switch (database) {
		case MYSQL:
			return new MysqlDAOFactory();
		default:
			return null;
		}
	}

	/** Abstract method for the UserDAO. */
	public abstract UserDAO getUserDAO() throws DBException;

	/** Abstract method for the TeacherOrderRequestDAO. */
	public abstract TeacherRequestDAO getTeacherRequestDAO() throws DBException;

	/** Abstract method for the StudentOrderRequestDAO. */
	public abstract StudentOrderRequestDAO getStudentOrderRequestDAO() throws DBException;

	/** Abstract method for the journalDAO. */
	public abstract JournalDAO getJournalDAO() throws DBException;

	/** Abstract method for the CourseDAO. */
	public abstract CourseDAO getCourseDAO() throws DBException;

	/** Abstract method for the CourseRequestBeanDAO. */
	public abstract CourseRequestBeanDAO getCourseRequestBeanDAO() throws DBException;

}
