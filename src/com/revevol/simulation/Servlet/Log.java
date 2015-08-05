package com.revevol.simulation.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.log.AppLogLine;
import com.google.appengine.api.log.LogQuery;
import com.google.appengine.api.log.LogServiceFactory;
import com.google.appengine.api.log.RequestLogs;

/**
 * Servlet implementation class Log
 */
public class Log extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Log() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setContentType("text/html");
		PrintWriter writer = resp.getWriter();

		LogQuery query = LogQuery.Builder.withDefaults();
		query.includeAppLogs(true);

		int i = 0;
		int limit = 1500;

		for (RequestLogs record : LogServiceFactory.getLogService().fetch(query)) {
			if (i++ < limit) {
				for (AppLogLine appLog : record.getAppLogLines()) {
					Calendar appCal = Calendar.getInstance();
					appCal.setTimeInMillis(appLog.getTimeUsec() / 1000);
					writer.println(String.format("<br />Date: %s", appCal.getTime().toString()));
					writer.println("Message: " + appLog.getLogMessage() + "<br /> <br />");
				}
			}
		}
	}
}
