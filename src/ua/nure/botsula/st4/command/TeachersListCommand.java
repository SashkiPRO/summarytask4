package ua.nure.botsula.st4.command;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.botsula.st.path.Path;
import ua.nure.botsula.st4.db.bean.TeacherRequestBean;
import ua.nure.botsula.st4.db.dao.DAOFactory;
import ua.nure.botsula.st4.db.entity.Course;
import ua.nure.botsula.st4.db.entity.Role;
import ua.nure.botsula.st4.db.entity.User;
import ua.nure.botsula.st4.exception.AppException;

public class TeachersListCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6609222266164179194L;

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		DAOFactory mysqlFactory = null;
		mysqlFactory=DAOFactory.getDAOFactory(DAOFactory.MYSQL);
		List<TeacherRequestBean> teacher=new ArrayList<>();
		List<User> allTeacher;
		List<Course> courses;
		teacher =mysqlFactory.getTeacherRequestDAO().findAllTeachersCourses();
		allTeacher=mysqlFactory.getUserDAO().findUserGroupByRoleId(Role.TEACHER.ordinal());
		courses=mysqlFactory.getCourseDAO().findAllFreeCourses();
		
		
		
	request.setAttribute("teachers_list", teacher);

	request.setAttribute("all_teacher_list", allTeacher);
	request.setAttribute("all_courses", courses);
		return Path.ADMIN_CABINET;
	}

}
