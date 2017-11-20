package ua.nure.botsula.st4.db.dao;

import java.util.List;

import ua.nure.botsula.st4.db.bean.JournalBean;
import ua.nure.botsula.st4.exception.DBException;

public interface JournalDAO {
	/** Find journal of course by id. */
	List<JournalBean> findJournalByCourseId(int courseId) throws DBException;

	/** Update journal of courses. */
	void updateCoursesJournal(List<JournalBean> newJournal) throws DBException;

	/** Insert journal into database. */
	void insertIntoDataBaseJournal(int courseId, int studentId) throws DBException;

	/** Delete journal of course. */
	void deleteJournalByCourseId(int course_id) throws DBException;

}
