/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package number;

/**
 * @author Dennis Cosgrove
 */
public final class NumberDemoComposite extends org.lgna.croquet.SimpleOperationInputDialogCoreComposite<number.views.NumberDemoView> {
	private final org.lgna.croquet.BoundedIntegerState waterTempFahrenheitState = this.createBoundedIntegerState( "waterTempFahrenheitState", new BoundedIntegerDetails().minimum( 32 ).maximum( 212 ).initialValue( 70 ) );

	private final org.lgna.croquet.event.ValueListener<Integer> listener = new org.lgna.croquet.event.ValueListener<Integer>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<Integer> e ) {
			System.out.println( e.getNextValue() );
		}
	};

	public NumberDemoComposite() {
		super( java.util.UUID.fromString( "f2d46859-44a9-4b38-9cce-65c8b8dfaef1" ), org.lgna.croquet.Application.DOCUMENT_UI_GROUP );
	}

	public org.lgna.croquet.BoundedIntegerState getWaterTempFahrenheitState() {
		return this.waterTempFahrenheitState;
	}

	@Override
	protected number.views.NumberDemoView createView() {
		return new number.views.NumberDemoView( this );
	}

	@Override
	protected Status getStatusPreRejectorCheck( org.lgna.croquet.history.CompletionStep<?> step ) {
		return IS_GOOD_TO_GO_STATUS;
	}

	@Override
	protected org.lgna.croquet.edits.Edit createEdit( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
		return null;
	}

	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		this.waterTempFahrenheitState.addNewSchoolValueListener( this.listener );
	}

	@Override
	public void handlePostDeactivation() {
		this.waterTempFahrenheitState.removeNewSchoolValueListener( this.listener );
		super.handlePostDeactivation();
	}

	public static void main( String[] args ) throws Exception {
		edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities.setLookAndFeel( "Nimbus" );
		org.lgna.croquet.simple.SimpleApplication app = new org.lgna.croquet.simple.SimpleApplication();
		new NumberDemoComposite().getLaunchOperation().fire();
		System.exit( 0 );
	}
}
