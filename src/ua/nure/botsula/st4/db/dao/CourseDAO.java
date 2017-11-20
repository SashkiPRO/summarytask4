package ua.nure.botsula.st4.db.dao;

import java.util.List;

import ua.nure.botsula.st4.db.entity.Course;
import ua.nure.botsula.st4.exception.DBException;

/**
 * DAO-Interface for the different CourseDAO-implementations. Defines the
 * CRUD-operations.
 */
public interface CourseDAO {
	/** Find all teachers by id. */
	List<Course> findAllTeachersCoursesById(int teacherId) throws DBException;

	/** Update student count of course. */
	void updateStudentCount(int courseId) throws DBException;

	/** Update course state of course. */
	void updateCourseState(int courseStateId, int courseId) throws DBException;

	/** Find course unit by id. */
	Course findCourseById(int id) throws DBException;

	/** Find all courses. */
	List<Course> findAllCourses() throws DBException;

	/** Find all free courses. */
	List<Course> findAllFreeCourses() throws DBException;

	/** Find all student courses. */
	List<Course> findCoursesByStudentIdAndState(int studentId, int stateId) throws DBException;

	/** Delete course by id */
	void deleteCourseById(int course_id) throws DBException;

	/** Insert course into database. */
	void insertCourseIntoDataBase(Course course) throws DBException;

	/** Update course. */
	void updateCourse(Course newCourse, int courseId) throws DBException;
}
