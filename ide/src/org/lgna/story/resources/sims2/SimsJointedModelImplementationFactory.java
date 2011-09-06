package org.lgna.story.resources.sims2;

import org.lgna.story.resources.VehicleResource;

public class SimsJointedModelImplementationFactory {
private static java.util.Map< VehicleResource, SimsJointedModelImplementationFactory > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	
    public static SimsJointedModelImplementationFactory getInstance( VehicleResource resource ) {
    	synchronized( map ) {
    		SimsJointedModelImplementationFactory rv = map.get( resource );
    		if( rv != null ) {
    			//pass
    		} else {
    			rv = new SimsJointedModelImplementationFactory( resource );
    			map.put( resource, rv );
    		}
    		return rv;
		}
    }
	
    private final VehicleResource resource;
    
    private SimsJointedModelImplementationFactory( VehicleResource resource ) {
    	this.resource = resource;
	}
    
	public org.lgna.story.implementation.JointedModelImplementation createImplementation( org.lgna.story.Vehicle abstraction, org.lgna.story.resources.JointId[] rootJointIds ) {
	    String modelName = SimsResourceUtilities.getModelName( this.resource );
	    String textureName = SimsResourceUtilities.getTextureName( this.resource );
		return new org.lgna.story.implementation.sims2.SimsJointedModelImplementation( abstraction, modelName, textureName, rootJointIds );
	}
}
