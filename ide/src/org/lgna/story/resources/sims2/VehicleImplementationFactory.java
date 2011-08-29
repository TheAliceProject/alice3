package org.lgna.story.resources.sims2;

import org.lgna.story.resources.VehicleResource;

public class VehicleImplementationFactory {
private static java.util.Map< VehicleResource, VehicleImplementationFactory > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	
    public static VehicleImplementationFactory getInstance( VehicleResource resource ) {
    	synchronized( map ) {
    		VehicleImplementationFactory rv = map.get( resource );
    		if( rv != null ) {
    			//pass
    		} else {
    			rv = new VehicleImplementationFactory( resource );
    			map.put( resource, rv );
    		}
    		return rv;
		}
    }
	
    private final VehicleResource resource;
    
    private VehicleImplementationFactory( VehicleResource resource ) {
    	this.resource = resource;
	}
    
	public org.lgna.story.implementation.VehicleImplementation createImplementation( org.lgna.story.Vehicle abstraction ) {
	    String modelName = SimsResourceUtilities.getModelName( this.resource );
	    String textureName = SimsResourceUtilities.getTextureName( this.resource );
		return new org.lgna.story.implementation.sims2.SimsVehicleImplementation( abstraction, modelName, textureName );
	}
}
