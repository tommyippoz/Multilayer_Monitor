/**
 * 
 */
package ippoz.multilayer.monitor.master.observation;

import ippoz.multilayer.monitor.layers.LayerType;

/**
 * @author Tommy
 *
 */
public class Indicator implements Comparable<Indicator> {
	
	private String indicatorName;
	private LayerType indicatorLayer;
	private Class<?> indicatorType;
	
	public Indicator(String indicatorName, LayerType indicatorLayer, Class<?> indicatorType) {
		this.indicatorName = indicatorName;
		this.indicatorLayer = indicatorLayer;
		this.indicatorType = indicatorType;
	}

	public Object getValue(String rawValue){
		return indicatorType.cast(rawValue);
	}
	
	public String getName(){
		return indicatorName;
	}
	
	public LayerType getLayer(){
		return indicatorLayer;
	}

	@Override
	public int compareTo(Indicator other) {
		if(other.getName().equals(getName()))
			return 0;
		else return 1;
	}

}
