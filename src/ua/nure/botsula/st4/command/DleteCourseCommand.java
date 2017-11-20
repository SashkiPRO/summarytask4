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
import ua.nure.botsula.st4.exception.DBException;

public class DleteCourseCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4896823484258380628L;

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		DAOFactory mysqlFactory = null;
		mysqlFactory=DAOFactory.getDAOFactory(DAOFactory.MYSQL);
		int course_id = Integer.parseInt(request.getParameter("id"));

	mysqlFactory.getCourseDAO().deleteCourseById(course_id);
		request.setAttribute("type", "courses");
		List<Course> b = new ArrayList<Course>();

		try {
			b = mysqlFactory.getCourseDAO().findAllCourses();

		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("course_list", b);

		return Path.ADMIN_CABINET;
	}

}
