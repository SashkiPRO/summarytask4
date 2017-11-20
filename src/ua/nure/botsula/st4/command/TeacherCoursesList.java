package ua.nure.botsula.st4.command;

import java.io.IOException;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ua.nure.botsula.st.path.Path;
import ua.nure.botsula.st4.db.dao.DAOFactory;
import ua.nure.botsula.st4.db.entity.Course;
import ua.nure.botsula.st4.db.entity.User;
import ua.nure.botsula.st4.exception.AppException;

public class TeacherCoursesList extends Command{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6535489780672331760L;

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		DAOFactory mysqlFactory = null;
		mysqlFactory=DAOFactory.getDAOFactory(DAOFactory.MYSQL);
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");

		List<Course> coursesList = mysqlFactory.getCourseDAO().findAllTeachersCoursesById(user.getId());
		request.setAttribute("course_list", coursesList);
		request.setAttribute("type", "courses");
		return Path.TEACHER_CABINET;
	}

}
