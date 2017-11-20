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

public class ViewJournalByStudent extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6542131245610657789L;

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		DAOFactory mysqlFactory = null;
		mysqlFactory=DAOFactory.getDAOFactory(DAOFactory.MYSQL);
		double total=0.0;
		int courseId=Integer.parseInt(request.getParameter("id"));
		Course course = mysqlFactory.getCourseDAO().findCourseById(courseId);
		List<JournalBean> journal;
	journal=mysqlFactory.getJournalDAO().findJournalByCourseId(courseId);
	System.out.println(journal);
	System.out.println(course);
		request.setAttribute("type", "courses");
		
		if(journal.size()>0){
			request.setAttribute("journal", journal);
			for(int i =0;i<journal.size();i++){
				total+=journal.get(i).getMark();
			}
			total/=journal.size();
			request.setAttribute("middle_mark", total);
			
		}else{
			request.setAttribute("no_students", true);
		}

		request.setAttribute("find_course", course);
			return Path.STUDENT_CABINET;
	}

}
