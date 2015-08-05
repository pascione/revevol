package com.revevol.simulation.Servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;


/**
 * Servlet implementation class Worker
 */
public class Worker extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger( Worker.class.getName() );

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Worker() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			useDataStore(request.getParameter("simulationId"));
		} catch (Exception e) {
			log.severe(e.getMessage());
		}
		response.getWriter().append("Simulation launched in background");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	private void useDataStore(String simulationId) {		
		List<Long> list = new ArrayList<Long>();
		try{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();		
		Entity simulation = new Entity("simulation");
	    simulation.setProperty("startTime", new Date());
	    calculateRandoms(list);
	    simulation.setProperty("stopTime", new Date());
		simulation.setProperty("listRandom", list);
		simulation.setProperty("name", simulationId);
		datastore.put(simulation);
		}catch(Exception e){
			log.severe(e.getMessage());
		}
	}

	
	private void calculateRandoms(List<Long> list) {
		Random randomGenerator = new Random();
	    for (int idx = 0; idx < 1000; idx++){
	      list.add(new Long(randomGenerator.nextInt(51)));
	    }	    
	}

}
