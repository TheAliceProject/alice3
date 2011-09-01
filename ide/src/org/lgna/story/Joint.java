/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.lgna.story;

/**
 * @author Dennis Cosgrove
 */
public class Joint extends Turnable {

	private static final edu.cmu.cs.dennisc.map.MapToMap< JointedModel, org.lgna.story.resources.JointId, Joint > mapToMap = edu.cmu.cs.dennisc.map.MapToMap.newInstance();

	/*package-private*/static Joint getJoint( JointedModel jointedModel, org.lgna.story.resources.JointId jointId ) {
		synchronized( mapToMap ) {
			Joint rv = mapToMap.get( jointedModel, jointId );
			if( rv != null ) {
				//pass
			} else {
				org.lgna.story.implementation.JointedModelImplementation jointedModelImplementation = ImplementationAccessor.getImplementation( jointedModel );
				rv = org.lgna.story.Joint.getInstance( jointedModelImplementation, jointId );
				mapToMap.put( jointedModel, jointId, rv );
			}
			return rv;
		}
	}
	/*package-private*/static Joint getInstance( org.lgna.story.implementation.JointedModelImplementation jointedModelImplementation, org.lgna.story.resources.JointId jointId ) {
		org.lgna.story.implementation.JointImplementation implementation = jointedModelImplementation.getJointImplementation( jointId );
		Joint rv = implementation.getAbstraction();
		if( rv != null ) {
			//pass
		} else {
			rv = new Joint( implementation );
			implementation.setAbstraction( rv );
		}
		return rv;
	}

	private final org.lgna.story.implementation.JointImplementation implementation;

	private Joint( org.lgna.story.implementation.JointImplementation implementation ) {
		this.implementation = implementation;
	}
	@Override
	/*package-private*/org.lgna.story.implementation.JointImplementation getImplementation() {
		return this.implementation;
	}
}
