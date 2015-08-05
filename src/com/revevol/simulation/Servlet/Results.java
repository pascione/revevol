package com.revevol.simulation.Servlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.revevol.simulation.Pojo.Simulation;
import com.revevol.simulation.Pojo.Statistics;

public class Results {
	private Statistics statistics;	
	private static final Logger log = Logger.getLogger( Results.class.getName() );

	public Statistics getStatistics() {
		statistics = new Statistics();
		try {

			List<Simulation> list = getSimulationFromDataStore();			

			statistics.setDuration(calculateSimulationTime(list));

			findDistinctNumbers(list, statistics);
			countOccurrence(list, statistics);

			// String json = convertToGoogleChartFormat(statistics);

		} catch (Exception e) {
			log.severe(e.getMessage());
		}
		return statistics;
	}

	public String convertToGoogleChartFormat(Statistics statistics) {
		StringBuilder json = new StringBuilder();
		String cols = "{ \"cols\": [ {\"id\":\"\",\"label\":\"Number\",\"pattern\":\"\",\"type\":\"string\"},{\"id\":\"\",\"label\":\"Occurrence %\",\"pattern\":\"\",\"type\":\"number\"}],";
		String rows = " \"rows\": [";
		json.append(cols).append(rows);
		for (Long i : statistics.getListKeys()) {
			json.append("{\"c\":[{\"v\":\"" + i.toString() + "\",\"f\":null},{\"v\":"
					+ statistics.getOccurrences().get(i) + ",\"f\":null}]},");

		}
		json.append("]}");
		return json.toString();
	}

	private List<Simulation> getSimulationFromDataStore() {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		List<Simulation> list = new ArrayList<Simulation>();
		log.info("Get simulations from datastore");
		Query q = new Query("simulation");
		PreparedQuery pq = datastore.prepare(q);
		
		for (Entity result : pq.asIterable()) {

			Simulation batch = new Simulation();
			batch.setStartSimulation((Date) result.getProperty("startTime"));
			batch.setStopSimulation((Date) result.getProperty("stopTime"));
			batch.setListRandom((List<Long>) result.getProperty("listRandom"));
			batch.setName((String) result.getProperty("name"));

			list.add(batch);
		}
		return list;
	}

	private long calculateSimulationTime(List<Simulation> list) {
		List<Date> dtStartList = new ArrayList<Date>();
		List<Date> dtStopList = new ArrayList<Date>();

		Iterator<Simulation> it = list.iterator();

		while (it.hasNext()) {
			Simulation t = it.next();
			dtStartList.add(t.getStartSimulation());
			dtStopList.add(t.getStopSimulation());
		}
		Date start = Collections.min(dtStartList);
		Date stop = Collections.max(dtStopList);
		long diff = stop.getTime() - start.getTime();
		log.info("calculate simulation time");
		return diff;
	}

	private void findDistinctNumbers(List<Simulation> list, Statistics statistics) {
		Set<Long> set = new HashSet<Long>();
		List<Long> tempList = new ArrayList<Long>();
		Iterator<Simulation> it = list.iterator();
		while (it.hasNext()) {
			tempList.addAll(it.next().getListRandom());
		}
		set.addAll(tempList);
		statistics.setDistinctNumbers(set.size());
		log.info("Find distinct numbers");
	}

	private void countOccurrence(List<Simulation> list, Statistics statistics) {
		if (list != null) {
			List<Long> tempList = new ArrayList<Long>();
			Iterator<Simulation> it = list.iterator();
			while (it.hasNext()) {
				tempList.addAll(it.next().getListRandom());
			}
			List<Long> listKeys = statistics.getListKeys();

			int numElements = tempList.size();

			if ((listKeys != null) && (tempList != null)) {
				Iterator<Long> keys = listKeys.iterator();
				while (keys.hasNext()) {
					Long i = keys.next();
					int frequency = getFrequency(tempList, i);
					float perc = this.getPercentage(frequency, numElements);

					statistics.getOccurrences().put(i, new Double(perc));
				}
			}
		}
		log.info("Calculate Occurrence");
	}

	private int getFrequency(List<Long> list, Long i) {
		int frequency = Collections.frequency(list, i);
		return frequency;
	}

	private float getPercentage(int n, int total) {
		float proportion = ((float) n) / ((float) total);
		return proportion * 100;
	}

}
