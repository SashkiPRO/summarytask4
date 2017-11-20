package ua.nure.botsula.st4.command;

import java.io.IOException;



import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.botsula.st4.db.dao.DAOFactory;
import ua.nure.botsula.st4.db.entity.Course;
import ua.nure.botsula.st4.exception.AppException;
import ua.nure.botsula.st4.util.DateUtils;

public class UpdateCourseCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8675654074822811141L;

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		DAOFactory mysqlFactory = null;
		mysqlFactory=DAOFactory.getDAOFactory(DAOFactory.MYSQL);
	Course course = new Course();
	course.setName(request.getParameter("course_name").toString());
	course.setTheme(request.getParameter("theme").toString());
	course.setStartDate(DateUtils.getDateFromString(request.getParameter("start_date").toString(), DateUtils.DATE_FORMAT));	
	course.setFinishDate(DateUtils.getDateFromString(request.getParameter("finish_date").toString(), DateUtils.DATE_FORMAT));	
	int id = Integer.parseInt(request.getParameter("id"));
	System.out.println("UPDATE");
	mysqlFactory.getCourseDAO().updateCourse(course,id);
	
		return "/controller?command=course_list&type=courses";
	}

}
