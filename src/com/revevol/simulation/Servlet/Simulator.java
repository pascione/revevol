package com.revevol.simulation.Servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultIterable;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.apphosting.api.ApiProxy.OverQuotaException;

/**
 * Servlet implementation class Simulation
 */
public class Simulator extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger( Simulator.class.getName() );

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Simulator() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("/");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			response.setContentType("text/html");
			Queue queue = QueueFactory.getDefaultQueue();
			log.info("Deleting old rows");
			deleteAllEntities(DatastoreServiceFactory.getDatastoreService());

			for (int i = 0; i < 100; i++) {
				queue.add(TaskOptions.Builder.withUrl("/worker").param("simulationId", String.valueOf(i)));
			}
			response.getWriter()
					.write("Simulation launched in background....click <a href='/'>here</a> to return home.");
			log.info("Simulation launched in background");

		} catch (OverQuotaException oe) {
			log.severe(oe.getMessage());
			response.getWriter().write("Quota exceeded...please try again later");

		} catch (Exception e) {
			log.severe(e.getMessage());
		}
	}

	private void deleteAllEntities(DatastoreService datastore) {
		Query q = new Query("simulation");
		PreparedQuery pq = datastore.prepare(q);
		List<Key> keys = new ArrayList<Key>();
		QueryResultIterable<Entity> result = pq.asQueryResultIterable();
		QueryResultIterator<Entity> iterator = result.iterator();
		while (iterator.hasNext()) {
			keys.add(iterator.next().getKey());
		}
		datastore.delete(keys);
	}

}
