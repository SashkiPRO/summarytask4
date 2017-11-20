package ua.nure.botsula.st4.command;

import java.io.IOException;


import java.util.ArrayList;
import java.util.List;

import ua.nure.botsula.st.path.Path;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.botsula.st4.db.dao.DAOFactory;
import ua.nure.botsula.st4.db.entity.Role;
import ua.nure.botsula.st4.db.entity.User;
import ua.nure.botsula.st4.exception.AppException;

public class AllTeacherCommand extends Command{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3402055329298646624L;

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		DAOFactory mysqlFactory = null;
		mysqlFactory=DAOFactory.getDAOFactory(DAOFactory.MYSQL);
		List <User> teacher = new ArrayList<User>();
		teacher=mysqlFactory.getUserDAO().findUserGroupByRoleId(Role.TEACHER.ordinal());
		request.setAttribute("teacher_list", teacher);
		request.setAttribute("type", "teachers");
		request.setAttribute("cat", "teachers_all");
		return Path.ADMIN_CABINET;
		
	}

}
