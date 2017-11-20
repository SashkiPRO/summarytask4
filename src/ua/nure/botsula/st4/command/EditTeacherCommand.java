package ua.nure.botsula.st4.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.botsula.st.path.Path;
import ua.nure.botsula.st4.db.dao.DAOFactory;
import ua.nure.botsula.st4.db.entity.User;
import ua.nure.botsula.st4.exception.AppException;

public class EditTeacherCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6043139337557052976L;

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		DAOFactory mysqlFactory = null;
		mysqlFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
		request.setAttribute("edit", true);
		int id = Integer.parseInt(request.getParameter("id"));
		User teacher;
		teacher = mysqlFactory.getUserDAO().findUserById(id);
		request.setAttribute("teacher_ed", teacher);
		return Path.ADMIN_CABINET;
	}

}
