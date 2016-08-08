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

package test;

/**
 * @author Dennis Cosgrove
 */
public class TestBootstrap {
	public static void main( String[] args ) {
		org.alice.stageide.openprojectpane.models.TemplateUriState.Template template = org.alice.stageide.openprojectpane.models.TemplateUriState.Template.GRASS;
		org.lgna.project.ast.NamedUserType programType;
		if( template.isRoom() ) {
			programType = org.alice.stageide.ast.SimsBootstrapUtilties.createProgramType(
					template.getFloorAppearance(),
					template.getWallAppearance(),
					template.getCeilingAppearance(),
					template.getAtmospherColor(),
					template.getFogDensity(),
					template.getAboveLightColor(),
					template.getBelowLightColor() );
		} else {
			programType = org.alice.stageide.ast.BootstrapUtilties.createProgramType( template.getSurfaceAppearance(), template.getAtmospherColor(), template.getFogDensity(), template.getAboveLightColor(), template.getBelowLightColor() );
		}
		//org.lgna.project.ast.NamedUserType programType = org.alice.stageide.ast.BootstrapUtilties.createProgramType( org.lgna.story.SGround.SurfaceAppearance.GRASS, null, Double.NaN, org.lgna.story.Color.WHITE, null );
		org.lgna.project.virtualmachine.VirtualMachine vm = new org.lgna.project.virtualmachine.ReleaseVirtualMachine();
		vm.registerAbstractClassAdapter( org.lgna.story.SScene.class, org.alice.stageide.ast.SceneAdapter.class );
		vm.registerAbstractClassAdapter( org.lgna.story.event.SceneActivationListener.class, org.alice.stageide.apis.story.event.SceneActivationAdapter.class );
		if( false ) {
			org.lgna.project.virtualmachine.UserInstance programInstance = vm.ENTRY_POINT_createInstance( programType );
			vm.ENTRY_POINT_invoke( programInstance, programType.findMethod( "initializeInFrame", String[].class ), (Object)args );
			System.err.println();
			System.err.println();
			System.err.println();
			System.err.println();
			vm.ENTRY_POINT_invoke( programInstance, programType.methods.get( 0 ) );
			//			vm.ENTRY_POINT_invoke( null, programType.methods.get( 1 ) );
			System.err.println();
			System.err.println();
			System.err.println();
			System.err.println();
		} else {
			vm.ENTRY_POINT_invoke( null, org.lgna.project.ProgramTypeUtilities.getMainMethod( programType ), (Object)args );
		}
	}
}
