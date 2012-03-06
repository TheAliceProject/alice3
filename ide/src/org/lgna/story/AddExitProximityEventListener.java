package org.lgna.story;

import edu.cmu.cs.dennisc.matt.ProximityDistance;

public class AddExitProximityEventListener {

	public interface Detail{}

	public static Double getDist(Detail[] details){
		for(Detail detail: details){
			if(detail instanceof ProximityDistance){
				return ((ProximityDistance)detail).getDist();
			}
		}
		return 0.0;
	}
}
