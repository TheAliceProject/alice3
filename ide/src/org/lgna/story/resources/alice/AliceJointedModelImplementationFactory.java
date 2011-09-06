package org.lgna.story.resources.alice;

import org.lgna.story.resources.JointedModelResource;

public class AliceJointedModelImplementationFactory {
private static java.util.Map< JointedModelResource, AliceJointedModelImplementationFactory > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	
    public static AliceJointedModelImplementationFactory getInstance( JointedModelResource resource ) {
    	synchronized( map ) {
    		AliceJointedModelImplementationFactory rv = map.get( resource );
    		if( rv != null ) {
    			//pass
    		} else {
    			rv = new AliceJointedModelImplementationFactory( resource );
    			map.put( resource, rv );
    		}
    		return rv;
		}
    }
	
    private final JointedModelResource resource;
    
    private AliceJointedModelImplementationFactory( JointedModelResource resource ) {
    	this.resource = resource;
	}
    
	public org.lgna.story.implementation.JointedModelImplementation createImplementation( org.lgna.story.Model abstraction, org.lgna.story.resources.JointId[] rootJointIds) {
	    edu.cmu.cs.dennisc.scenegraph.TexturedAppearance[] texturedAppearances = AliceResourceUtilties.getTexturedAppearance( this.resource );
	    edu.cmu.cs.dennisc.scenegraph.SkeletonVisual sgSkeletonVisual = AliceResourceUtilties.getVisualCopy( this.resource );
		return new org.lgna.story.implementation.alice.AliceJointedModelImplementation( abstraction, sgSkeletonVisual, texturedAppearances, rootJointIds );
	}

}
