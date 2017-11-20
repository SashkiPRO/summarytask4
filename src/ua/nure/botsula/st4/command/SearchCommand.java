package ua.nure.botsula.st4.command;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.botsula.st4.exception.AppException;

public class SearchCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7930694889235124452L;

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
	
		String searchParamentr = request.getParameter("search");
		StringBuilder builder = new StringBuilder();		
		builder.append("/controller?command=show_result&search=");
		builder.append(URLEncoder.encode(searchParamentr, "UTF8"));
		System.err.println(builder);
		return builder.toString();
	}

}
