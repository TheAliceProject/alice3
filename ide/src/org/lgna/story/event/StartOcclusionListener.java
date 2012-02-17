package org.lgna.story.event;

import edu.cmu.cs.dennisc.matt.OcclusionListener;

public interface StartOcclusionListener extends OcclusionListener {
	
	public void whenTheseOcclude( StartOcclusionEvent e );

}
