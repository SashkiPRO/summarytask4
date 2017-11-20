package ua.nure.botsula.st4.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.botsula.st4.db.dao.DAOFactory;
import ua.nure.botsula.st4.db.entity.Role;
import ua.nure.botsula.st4.db.entity.User;
import ua.nure.botsula.st4.exception.AppException;

public class AddTeacherCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5667399766732011468L;

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		DAOFactory mysqlFactory = null;
		mysqlFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
		User teacher = new User();
		teacher.setFname(request.getParameter("fname"));
		teacher.setLname(request.getParameter("lname"));
		teacher.setLogin(request.getParameter("login"));
		teacher.setPassword(request.getParameter("password"));
		teacher.setEmail(request.getParameter("email"));
		teacher.setRoleid(Role.TEACHER.ordinal());
		mysqlFactory.getUserDAO().insertUserIntoDatabase(teacher);
		return "/controller?command=all_teachers";
	}

}
