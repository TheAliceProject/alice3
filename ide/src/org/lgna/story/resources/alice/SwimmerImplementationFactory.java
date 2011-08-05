package org.lgna.story.resources.alice;

import org.lgna.story.resources.SwimmerResource;

public class SwimmerImplementationFactory {
	private static java.util.Map< SwimmerResource, SwimmerImplementationFactory > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	
    public static SwimmerImplementationFactory getInstance( SwimmerResource resource ) {
    	synchronized( map ) {
    		SwimmerImplementationFactory rv = map.get( resource );
    		if( rv != null ) {
    			//pass
    		} else {
    			rv = new SwimmerImplementationFactory( resource );
    			map.put( resource, rv );
    		}
    		return rv;
		}
    }
	
    private final SwimmerResource resource;
    
    private SwimmerImplementationFactory( SwimmerResource resource ) {
    	this.resource = resource;
	}
    
	public org.lgna.story.implementation.SwimmerImplementation createImplementation( org.lgna.story.Swimmer abstraction ) {
	    edu.cmu.cs.dennisc.scenegraph.TexturedAppearance[] texturedAppearances = AliceResourceUtilties.getTexturedAppearance( this.resource );
	    edu.cmu.cs.dennisc.scenegraph.SkeletonVisual sgSkeletonVisual = AliceResourceUtilties.getVisualCopy( this.resource );
		return new org.lgna.story.implementation.alice.AliceSwimmerImplementation( abstraction, sgSkeletonVisual, texturedAppearances );
	}
}
