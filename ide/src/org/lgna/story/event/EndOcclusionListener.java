package org.lgna.story.event;

import edu.cmu.cs.dennisc.matt.EndOcclusionEvent;
import edu.cmu.cs.dennisc.matt.OcclusionListener;

public interface EndOcclusionListener extends OcclusionListener {
	
	public void theseNoLongerOcclude( EndOcclusionEvent e );

}
