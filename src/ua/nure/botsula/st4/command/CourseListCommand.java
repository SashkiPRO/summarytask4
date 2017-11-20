package ua.nure.botsula.st4.command;

import java.io.IOException;



import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.botsula.st.path.Path;
import ua.nure.botsula.st4.db.dao.DAOFactory;
import ua.nure.botsula.st4.db.entity.Course;
import ua.nure.botsula.st4.exception.AppException;



public class CourseListCommand extends Command{

	/**
	 * 
	 */
	private static final long serialVersionUID = 625793218072200760L;

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		DAOFactory 	mysqlFactory=DAOFactory.getDAOFactory(DAOFactory.MYSQL);
		List <Course> b = new ArrayList<Course>();
	
			b=mysqlFactory.getCourseDAO().findAllCourses();
			System.out.println(b);
			
			
		
	request.setAttribute("course_list", b);
		return Path.ADMIN_CABINET;
	}

}
