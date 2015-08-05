package com.revevol.simulation.Pojo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Statistics {
	private long duration;
	private int distinctNumbers;
	private Map<Long,Double> occurrences;
	private List<Long> listKeys;
	
	public Statistics(){
		this.occurrences = new HashMap<Long,Double>();
		this.listKeys = new ArrayList<Long>();
		for(int i = 0;i<=50;i++){
			this.listKeys.add(new Long(i));
		}
	}
	
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public int getDistinctNumbers() {
		return distinctNumbers;
	}
	public void setDistinctNumbers(int distinctNumbers) {
		this.distinctNumbers = distinctNumbers;
	}
	public Map<Long, Double> getOccurrences() {
		return occurrences;
	}
	public void setOccurrences(Map<Long, Double> occurrences) {
		this.occurrences = occurrences;
	}
	public List<Long> getListKeys() {
		return listKeys;
	}
	public void setListKeys(List<Long> listKeys) {
		this.listKeys = listKeys;
	}
	
	public String getMostOccurred(){
		Entry<Long,Double> maxValue = null;		
		for(Entry<Long,Double> temp : this.occurrences.entrySet()){
			if((maxValue ==null)||(temp.getValue()>maxValue.getValue()))
				maxValue = temp;
		}
		if(maxValue != null)
			return maxValue.getKey().toString();
		else 
			return ""; 
	}
	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat("##.##");	    
		String str = 
				"Duration: " + this.duration + "\n" +
				"Distinct Numbers: " + this.distinctNumbers + "\n" +
				"Occurrences: \n";
		Iterator<Long> it = this.listKeys.iterator(); 
		while(it.hasNext()){
			Long i = it.next();
			str += i + " " + df.format(this.occurrences.get(i))+ "% " + "\n";
		}
		str += "Most Occurred: " + this.getMostOccurred();
		return str;
	}
	

}

