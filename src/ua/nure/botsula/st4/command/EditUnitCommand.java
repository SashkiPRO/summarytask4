package ua.nure.botsula.st4.command;

import java.io.IOException;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.botsula.st.path.Path;
import ua.nure.botsula.st4.db.bean.JournalBean;
import ua.nure.botsula.st4.db.dao.DAOFactory;
import ua.nure.botsula.st4.db.entity.Course;
import ua.nure.botsula.st4.exception.AppException;

public class EditUnitCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7517608880986784634L;

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		DAOFactory mysqlFactory = null;
		mysqlFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
		String command = request.getParameter("edit");
		int courseId = Integer.parseInt(request.getParameter("course_id"));
		List<JournalBean> journal = mysqlFactory.getJournalDAO().findJournalByCourseId(courseId);
		Course course = mysqlFactory.getCourseDAO().findCourseById(courseId);
		request.setAttribute("edit", true);
		request.setAttribute("edit_journal", journal);
		request.setAttribute("type", "courses");
		request.setAttribute("course", course);
		System.out.println(course);
		System.out.println(command);
		return Path.TEACHER_CABINET;

	}

}
