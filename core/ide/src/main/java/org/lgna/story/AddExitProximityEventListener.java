package org.lgna.story;

import org.alice.stageide.apis.story.event.ProximityDistance;

public class AddExitProximityEventListener {

	public interface Detail {
	}

	public static Double getDist( Detail[] details ) {
		for( Detail detail : details ) {
			if( detail instanceof ProximityDistance ) {
				return ( (ProximityDistance)detail ).getDist();
			}
		}
		return 0.0;
	}
}
