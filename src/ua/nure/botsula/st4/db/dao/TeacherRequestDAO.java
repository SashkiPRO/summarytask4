package ua.nure.botsula.st4.db.dao;

import java.util.List;

import ua.nure.botsula.st4.db.bean.TeacherRequestBean;
import ua.nure.botsula.st4.exception.DBException;

public interface TeacherRequestDAO {
	/** Update teacher for course. */
	void updateTeacherSignCourse(int teacherId, int courseId, int newCourseId) throws DBException;
	/** Delete teacher of course. */
	void deleteTeacherSignCourse(int teacherId, int courseId) throws DBException;
	/** Delete all teacher signed. */
	void deleteAllTeacherSignCourse(int teacherId) throws DBException;
	/** Find all info about teacher with courses info. */
	List<TeacherRequestBean> findAllTeachersCourses() throws DBException;
	/** Sign teacher to course. */
	void insertCourseToTeacherSign(int teacher_id, int course_id) throws DBException;
	/** Unsign teacher from course. */
	void deleteTeacherCourseByCourseId(int course_id) throws DBException;
}
