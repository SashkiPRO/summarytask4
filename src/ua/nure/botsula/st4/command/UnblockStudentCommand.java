package ua.nure.botsula.st4.command;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.botsula.st4.db.dao.DAOFactory;
import ua.nure.botsula.st4.db.entity.Role;
import ua.nure.botsula.st4.exception.AppException;

public class UnblockStudentCommand extends Command{

	/**
	 * 
	 */
	private static final long serialVersionUID = 877453048378055655L;

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		DAOFactory mysqlFactory = null;
		mysqlFactory=DAOFactory.getDAOFactory(DAOFactory.MYSQL);
int id = Integer.parseInt(request.getParameter("id"));
mysqlFactory.getUserDAO().changeUserRole(id, Role.STUDENT.ordinal());
return "controller?command=students_list&type=students";
	}

}
