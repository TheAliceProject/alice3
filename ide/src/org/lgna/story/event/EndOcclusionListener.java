package org.lgna.story.event;

import edu.cmu.cs.dennisc.matt.EndOcclusionEvent;

public interface EndOcclusionListener {
	
	public void theseNoLongerOcclude( EndOcclusionEvent e );

}
