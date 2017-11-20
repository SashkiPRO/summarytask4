package ua.nure.botsula.st4.db.dao;

import java.util.List;

import ua.nure.botsula.st4.db.bean.CourseRequestBean;
import ua.nure.botsula.st4.exception.DBException;

public interface CourseRequestBeanDAO {
	/** Find list of courses with full information. */
	List<CourseRequestBean> findAllCoursesBean() throws DBException ;
	/** Find courses by search parameters. */
	List<CourseRequestBean> findCoursesBySearchParametr(String parameter) throws DBException;
}
