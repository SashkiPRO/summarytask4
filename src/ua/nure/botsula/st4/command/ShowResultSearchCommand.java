package ua.nure.botsula.st4.command;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

import ua.nure.botsula.st.path.Path;
import ua.nure.botsula.st4.db.bean.CourseRequestBean;
import ua.nure.botsula.st4.db.bean.StudentOrderRequestBean;
import ua.nure.botsula.st4.db.dao.DAOFactory;
import ua.nure.botsula.st4.db.entity.Role;
import ua.nure.botsula.st4.db.entity.User;
import ua.nure.botsula.st4.exception.AppException;

public class ShowResultSearchCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2749432371097501071L;

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		HttpSession session = request.getSession(false);
		User user = new User();
		user = (User) session.getAttribute("user");

		request.setCharacterEncoding("UTF8");
		DAOFactory mysqlFactory = null;
		mysqlFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
		String findParametr = request.getParameter("search");
		if (user != null && user.getRoleid() == Role.STUDENT.ordinal()) {
			List<StudentOrderRequestBean> courseList = mysqlFactory.getStudentOrderRequestDAO()
					.findCoursesBySearchParametr(findParametr, user.getId());
			request.setAttribute("course_list", courseList);
		} else {
			List<CourseRequestBean> courseList = mysqlFactory.getCourseRequestBeanDAO()
					.findCoursesBySearchParametr(findParametr);
			System.out.println(findParametr);
			System.err.println(courseList);

			request.setAttribute("course_list", courseList);
		}
		return Path.SEARCH_PAGE;
	}

}
