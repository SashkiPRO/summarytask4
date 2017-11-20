package ua.nure.botsula.st4.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

import ua.nure.botsula.st.path.Path;
import ua.nure.botsula.st4.db.dao.DAOFactory;
import ua.nure.botsula.st4.db.entity.Role;
import ua.nure.botsula.st4.db.entity.User;
import ua.nure.botsula.st4.exception.AppException;
import ua.nure.botsula.st4.util.ForwardPage;

/**
 * Login command.
 * 
 * @author D.Kolesnikov
 * 
 */
public class LoginCommand extends Command {

	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(LoginCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		LOG.debug("Command starts");

		HttpSession session = request.getSession();
		String referrer = request.getHeader("referer");

		// obtain login and password from a request
		DAOFactory mysqlFactory = null;
		mysqlFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
		String login = request.getParameter("login");

		LOG.trace("Request parameter: loging --> " + login);
		System.out.println(login);
		String password = request.getParameter("password");
		if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
			throw new AppException("Login/password cannot be empty");
		}

		User user = mysqlFactory.getUserDAO().findUserByLogin(login);
		LOG.trace("Found in DB: user --> " + user);
		String forward = ForwardPage.getForward(referrer);
		if (user == null || !password.equals(user.getPassword())) {

		if(ForwardPage.isJsp(forward)){
			forward  =forward +"?error=error";
		}else{
			forward  =forward +"&error=error";
		}
	
		//	 throw new AppException("Cannot find user with such login/password");
		} else {

			Role userRole = Role.getRole(user);
			System.out.println(user.getRoleid());
			// LOG.trace("userRole --> " + userRole);

			if (userRole == Role.ADMIN) {
				forward = "/controller?command=user_info&type=admin_info";
			}
			if (userRole == Role.TEACHER) {
				forward = "/controller?command=user_info&type=info";
			}
			if (userRole == Role.STUDENT) {
				forward = "/controller?command=user_info&type=student_info";
			}

			session.setAttribute("user", user);
			// LOG.trace("Set the session attribute: user --> " + user);

			session.setAttribute("userRole", userRole);
		}
		// LOG.trace("Set the session attribute: userRole --> " + userRole);

		// LOG.info("User " + user + " logged as " +
		// userRole.toString().toLowerCase());

		// LOG.debug("Command finished");
		return forward;
	}

}
