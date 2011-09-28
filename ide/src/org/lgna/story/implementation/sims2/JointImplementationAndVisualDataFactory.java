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

package org.lgna.story.implementation.sims2;

/**
 * @author Dennis Cosgrove
 */
public class JointImplementationAndVisualDataFactory implements org.lgna.story.implementation.JointedModelImp.JointImplementationAndVisualDataFactory {
	private static java.util.Map< org.lgna.story.resources.JointedModelResource, JointImplementationAndVisualDataFactory > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

	public static JointImplementationAndVisualDataFactory getInstance( org.lgna.story.resources.JointedModelResource resource ) {
		synchronized( map ) {
			JointImplementationAndVisualDataFactory rv = map.get( resource );
			if( rv != null ) {
				//pass
			} else {
				rv = new JointImplementationAndVisualDataFactory( resource );
				map.put( resource, rv );
			}
			return rv;
		}
	}

	private final org.lgna.story.resources.JointedModelResource resource;
	private JointImplementationAndVisualDataFactory( org.lgna.story.resources.JointedModelResource resource ) {
		this.resource = resource;
	}
	public org.lgna.story.resources.JointedModelResource getResource() {
		return this.resource;
	}
	public org.lgna.story.implementation.JointImp createJointImplementation( org.lgna.story.implementation.JointedModelImp jointedModelImplementation, org.lgna.story.resources.JointId jointId ) {
		assert jointedModelImplementation.getVisualData() instanceof NebulousVisualData;
		edu.cmu.cs.dennisc.nebulous.Model nebModel = ((NebulousVisualData<edu.cmu.cs.dennisc.nebulous.Model>)jointedModelImplementation.getVisualData()).getNebModel();
		return new JointImplementation( jointedModelImplementation, new NebulousJoint( nebModel, jointId ) );
	}
	public org.lgna.story.implementation.JointedModelImp.VisualData createVisualData( org.lgna.story.implementation.JointedModelImp jointedModelImplementation ) {
		try {
			if( this.resource instanceof org.lgna.story.resources.sims2.PersonResource ) {
				org.lgna.story.resources.sims2.PersonResource personResource = (org.lgna.story.resources.sims2.PersonResource)this.resource;
				return NebulousPersonVisualData.createInstance( personResource );
			} else {
				return new NebulousVisualData< edu.cmu.cs.dennisc.nebulous.Model >( new edu.cmu.cs.dennisc.nebulous.Thing( this.resource, this.resource ) );
			}
		} catch( edu.cmu.cs.dennisc.eula.LicenseRejectedException lre ) {
			throw new RuntimeException( lre );
		}
	}
}
