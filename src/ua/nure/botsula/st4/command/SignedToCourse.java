package ua.nure.botsula.st4.command;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ua.nure.botsula.st4.db.dao.DAOFactory;
import ua.nure.botsula.st4.db.entity.User;
import ua.nure.botsula.st4.exception.AppException;

public class SignedToCourse extends Command{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3523186582871281672L;

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		DAOFactory mysqlFactory = null;
		mysqlFactory=DAOFactory.getDAOFactory(DAOFactory.MYSQL);
		int courseId = Integer.parseInt(request.getParameter("id"));
		HttpSession session = request.getSession(false);
		User student =(User)session.getAttribute("user");
		int studentId=student.getId();
		mysqlFactory.getStudentOrderRequestDAO().insertIntoDatabaseSignCourse(studentId, courseId);
		return "/controller?command=about_courses";
	}

}
