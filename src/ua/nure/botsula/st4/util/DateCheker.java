package ua.nure.botsula.st4.util;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ua.nure.botsula.st4.db.dao.DAOFactory;
import ua.nure.botsula.st4.db.dao.sql.MysqlDAOFactory;
import ua.nure.botsula.st4.db.entity.Course;
import ua.nure.botsula.st4.db.entity.CourseState;
import ua.nure.botsula.st4.exception.DBException;


public class DateCheker implements Runnable{
	private static final Logger LOG = Logger.getLogger(MysqlDAOFactory.class);
	@Override
	public void run() {
		DAOFactory mysqlFactory = null;
		mysqlFactory=DAOFactory.getDAOFactory(DAOFactory.MYSQL);
		while (true) {
			List<Course> courses = new ArrayList<Course>();
			try {
				courses = mysqlFactory.getCourseDAO().findAllCourses();
			} catch (DBException e1) {
				LOG.error(
						"Courses dateChekerError, manager#findAllCourses",
						e1);
			}
			for (Course courseUnit : courses) {
				if (DateUtils.fullDaysBetweenDates(DateUtils.getCurrentDate(), courseUnit.getStartDate()) > 0) {
					System.out.println(DateUtils.fullDaysBetweenDates(DateUtils.getCurrentDate(), courseUnit.getStartDate()) > 0);
					System.out.println(DateUtils.getCurrentDate());
					System.out.println((courseUnit.getFinishDate()));
					System.out.println(courseUnit.getName());
					try {
						mysqlFactory.getCourseDAO().updateCourseState(CourseState.RECRUITED.ordinal(), courseUnit.getId());
						System.out.println("recrited");
					} catch (DBException e1) {
						LOG.error(
								"Courses dateChekerError, manager#findAllCourses",
								e1);
					}
					LOG.trace("Course= "
							+ courseUnit.getId()
							+ " - was updated");
				}else if(DateUtils.fullDaysBetweenDates(DateUtils.getCurrentDate(), courseUnit.getFinishDate())>0&&(DateUtils.fullDaysBetweenDates(courseUnit.getStartDate(),DateUtils.getCurrentDate())>0)){
					try {
						mysqlFactory.getCourseDAO().updateCourseState(CourseState.DURING.ordinal(), courseUnit.getId());
						System.out.println("during");
		
						System.out.println(courseUnit.getName());
					} catch (DBException e1) {
						LOG.error(
								"Courses dateChekerError, manager#findAllCourses",
								e1);
					}
					LOG.trace("Course= "
							+ courseUnit.getId()
							+ " - was updated");
				
					
				}else if(DateUtils.fullDaysBetweenDates(DateUtils.getCurrentDate(), courseUnit.getFinishDate())<0){
					try {
						mysqlFactory.getCourseDAO().updateCourseState(CourseState.FINISHED.ordinal(), courseUnit.getId());
						System.out.println("finished");
						System.out.println("during");
						
						System.out.println(courseUnit.getName());
					} catch (DBException e1) {
						LOG.error(
								"Courses dateChekerError, manager#findAllCourses",
								e1);
					}
					LOG.trace("Course= "
							+ courseUnit.getId()
							+ " - was updated");
				
				}
			}
			try {
				Thread.sleep(60*60*1000);
			} catch (InterruptedException e) {
				LOG.error("CourseDateChecker error, InterruptedException", e);
			}
		}
	}
}
