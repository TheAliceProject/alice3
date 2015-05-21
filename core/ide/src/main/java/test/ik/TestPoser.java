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

package test.ik;

import org.lgna.ik.poser.croquet.AbstractPoserOrAnimatorComposite;
import org.lgna.ik.poser.croquet.BipedAnimator;
import org.lgna.ik.poser.croquet.BipedPoser;
import org.lgna.story.SBiped;
import org.lgna.story.SProgram;

//import test.ik.IkTestApplication;

/**
 * @author Matt May
 */
public class TestPoser extends SProgram {
	private static final boolean SHOULD_I_ANIMATE = true;

	public static void main( String[] args ) {
		IkTestApplication app = new IkTestApplication();
		app.initialize( args );

		org.lgna.project.virtualmachine.ReleaseVirtualMachine vm = new org.lgna.project.virtualmachine.ReleaseVirtualMachine();

		org.lgna.story.resources.BipedResource bipedResource = org.lgna.story.resources.biped.OgreResource.BROWN;
		//org.lgna.story.resources.BipedResource bipedResource = org.lgna.story.resources.biped.AlienResource.DEFAULT;

		org.lgna.project.ast.JavaType ancestorType = org.lgna.project.ast.JavaType.getInstance( SBiped.class );
		org.lgna.project.ast.JavaField argumentField = org.lgna.project.ast.JavaField.getInstance( bipedResource.getClass(), bipedResource.toString() );
		org.lgna.project.ast.NamedUserType type = org.alice.ide.typemanager.TypeManager.getNamedUserTypeFromArgumentField( ancestorType, argumentField );

		org.lgna.project.ast.NamedUserConstructor userConstructor = type.constructors.get( 0 );
		final int N = userConstructor.requiredParameters.size();
		Object[] arguments = new Object[ N ];
		switch( N ) {
		case 0:
			break;
		case 1:
			arguments[ 0 ] = bipedResource;
			break;
		case 2:
			assert false : N;
		}
		org.lgna.project.virtualmachine.UserInstance userInstance = vm.ENTRY_POINT_createInstance( type, arguments );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( userInstance );
		userInstance.getJavaInstance( SBiped.class );
		AbstractPoserOrAnimatorComposite composite;

		if( SHOULD_I_ANIMATE ) {
			composite = new BipedAnimator( type );
		} else {
			composite = new BipedPoser( type );
		}
		app.getDocumentFrame().getFrame().setMainComposite( composite );

		app.getDocumentFrame().getFrame().setSize( 1200, 800 );
		app.getDocumentFrame().getFrame().setVisible( true );
	}
}
