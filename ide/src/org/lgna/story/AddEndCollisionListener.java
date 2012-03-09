package org.lgna.story;

import java.util.ArrayList;

public class AddEndCollisionListener {

	public interface Detail {
	}

	public static <T extends MovableTurnable> ArrayList<T> getGroupOne( Detail[] details, Class<T> cls ) {
		for( Detail detail : details ) {
			if( detail instanceof EventCollection ) {
				EventCollection eCollection = (EventCollection)detail;
				if( eCollection.getInternalClass().equals( cls ) ) {
					return eCollection.getValue();
				}
			}
		}
		return null;
	}
	public static <T extends MovableTurnable> ArrayList<T> getGroupTwo( Detail[] details, Class<T> cls ) {
		EventCollection firstCollection = null;
		for( Detail detail : details ) {
			if( detail instanceof EventCollection ) {
				EventCollection eCollection = (EventCollection)detail;
				if( eCollection.getInternalClass().equals( cls ) && firstCollection != null ) {
					return eCollection.getValue();
				} else {
					firstCollection = eCollection;
				}
			}
		}
		if( firstCollection != null ) {
			return firstCollection.getValue();
		}
		return null;
	}
}
