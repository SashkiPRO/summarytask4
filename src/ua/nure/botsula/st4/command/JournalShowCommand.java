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

public class JournalShowCommand extends Command{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4787511067635225096L;

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		DAOFactory mysqlFactory = null;
		mysqlFactory=DAOFactory.getDAOFactory(DAOFactory.MYSQL);
	int courseId=Integer.parseInt(request.getParameter("id"));
	Course course = mysqlFactory.getCourseDAO().findCourseById(courseId);
	List<JournalBean> journal;
journal=mysqlFactory.getJournalDAO().findJournalByCourseId(courseId);
System.out.println(journal);
System.out.println(course);
	request.setAttribute("type", "courses");
	if(journal.size()>0){
		request.setAttribute("journal", journal);
	}else{
		request.setAttribute("no_students", true);
	}

	request.setAttribute("find_course", course);
		return Path.TEACHER_CABINET;
	}

}
