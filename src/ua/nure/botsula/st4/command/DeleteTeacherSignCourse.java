package ua.nure.botsula.st4.command;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.botsula.st4.db.dao.DAOFactory;
import ua.nure.botsula.st4.exception.AppException;

public class DeleteTeacherSignCourse extends Command{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7382550172329090082L;

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		DAOFactory mysqlFactory = null;
		mysqlFactory=DAOFactory.getDAOFactory(DAOFactory.MYSQL);
	int teacherId=Integer.parseInt(request.getParameter("teacher_id"));
	int courseId=Integer.parseInt(request.getParameter("course_id"));
	mysqlFactory.getTeacherRequestDAO().deleteTeacherSignCourse(teacherId, courseId);
		return "controller?command=teacher_list&type=teachers&cat=teachers_course";
	}

}
