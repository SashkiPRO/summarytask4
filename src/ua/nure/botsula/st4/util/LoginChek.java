package ua.nure.botsula.st4.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ua.nure.botsula.st4.db.dao.sql.MysqlDAOFactory;
import ua.nure.botsula.st4.exception.DBException;

/**
 * Servlet implementation class RegistrationLoginChek
 */
@WebServlet("/LoginChek")
public class LoginChek extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Statement st;
		ResultSet rs;
		String availLogin = request.getParameter("login");
		String availPassword = request.getParameter("password");

		System.out.println(availLogin + "log");
		System.out.println(availPassword+ "pass");

		String SQL = "SELECT login, password FROM users WHERE login='" + availLogin + "' AND password='"+availPassword+"'";
		try {
			new MysqlDAOFactory();
			Connection con = MysqlDAOFactory.createConnection();
			try {
				st = con.createStatement();
				rs = st.executeQuery(SQL);
				if (rs.next()) {

					out.print("true");
					
					System.out.println("true");

				} else {

					out.print("false");
					
					System.out.println("false");

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			out.close();
		}

	}

}
