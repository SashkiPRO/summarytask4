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
@WebServlet("/RegistrationLoginChek")
public class RegistrationLoginChek extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Statement st;
		ResultSet rs;
		String availLogin = request.getParameter("login");
		System.out.println(availLogin);
		String SQL = "SELECT login FROM users WHERE login='" + availLogin + "'";
		try {
			new MysqlDAOFactory();
			Connection con = MysqlDAOFactory.createConnection();
			try {
				st = con.createStatement();
				rs = st.executeQuery(SQL);
				if (rs.next()) {

					out.print("false");

					System.out.println("false");

				} else {

					out.print("true");

					System.out.println("true");

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
