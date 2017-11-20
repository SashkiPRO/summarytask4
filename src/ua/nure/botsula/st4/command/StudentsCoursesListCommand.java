package ua.nure.botsula.st4.command;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ua.nure.botsula.st.path.Path;
import ua.nure.botsula.st4.db.dao.DAOFactory;
import ua.nure.botsula.st4.db.entity.Course;
import ua.nure.botsula.st4.db.entity.CourseState;
import ua.nure.botsula.st4.db.entity.User;
import ua.nure.botsula.st4.exception.AppException;

public class StudentsCoursesListCommand extends Command{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3186580116482225335L;

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		DAOFactory mysqlFactory = null;
		mysqlFactory=DAOFactory.getDAOFactory(DAOFactory.MYSQL);
		String courseState=request.getParameter("state");
		HttpSession session = request.getSession();
		User student =(User) session.getAttribute("user");
		List <Course> courses = new ArrayList<Course>();
		switch (courseState) {
		case "nostarted": 
			courses = mysqlFactory.getCourseDAO().findCoursesByStudentIdAndState(student.getId(), CourseState.RECRUITED.ordinal());
			break;
		case "during": 
			courses = mysqlFactory.getCourseDAO().findCoursesByStudentIdAndState(student.getId(), CourseState.DURING.ordinal());
			break;
		case "finished": 
			courses = mysqlFactory.getCourseDAO().findCoursesByStudentIdAndState(student.getId(), CourseState.FINISHED.ordinal());
			break;
		default:courses = mysqlFactory.getCourseDAO().findCoursesByStudentIdAndState(student.getId(), CourseState.RECRUITED.ordinal());
			break;
		}
		
		request.setAttribute("type", "courses");
		request.setAttribute("courses_list", courses);
		return Path.STUDENT_CABINET;
	}

}
