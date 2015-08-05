package com.revevol.simulation.Pojo;

import java.util.Date;
import java.util.List;



public class Simulation {
	private Date startSimulation;
	private Date stopSimulation;
	private List<Long> listRandom;
	private String name;
	
	
	public Date getStartSimulation() {
		return startSimulation;
	}
	public void setStartSimulation(Date startSimulation) {
		this.startSimulation = startSimulation;
	}
	public Date getStopSimulation() {
		return stopSimulation;
	}
	public void setStopSimulation(Date stopSimulation) {
		this.stopSimulation = stopSimulation;
	}
	public List<Long> getListRandom() {
		return listRandom;
	}
	public void setListRandom(List<Long> listRandom) {
		this.listRandom = listRandom;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}

