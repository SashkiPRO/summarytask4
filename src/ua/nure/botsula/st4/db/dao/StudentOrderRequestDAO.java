package ua.nure.botsula.st4.db.dao;

import java.util.List;

import ua.nure.botsula.st4.db.bean.StudentOrderRequestBean;
import ua.nure.botsula.st4.exception.DBException;

public interface StudentOrderRequestDAO {
	/** Find all courses for student with signed mark. */
	List<StudentOrderRequestBean> findStudentAllOrder(int studentId) throws DBException;

	/** Cheking student signed. */
	boolean studentSignedOnCourse(int studentId, int courseId) throws DBException;

	/** Sign student to course. */
	void insertIntoDatabaseSignCourse(int studentId, int courseId) throws DBException;

	/** Delete sign student. */
	public void deleteSignByCourseId(int course_id) throws DBException;

	/** Find courses list by search parameters. */
	List<StudentOrderRequestBean> findCoursesBySearchParametr(String parameter, int studentid) throws DBException;
}
