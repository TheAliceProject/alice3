/*
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
package edu.cmu.cs.dennisc.croquet.tutorial;

import edu.cmu.cs.dennisc.tutorial.*;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/ class MenuSelectionNote extends HistoryNote {
	private static String getText( edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent menuSelectionEvent, int i ) {
		edu.cmu.cs.dennisc.croquet.Model modelI = menuSelectionEvent.getModelAt( i );
		StringBuilder sb = new StringBuilder();
		sb.append( "select <strong><em>" );
		if( modelI instanceof edu.cmu.cs.dennisc.croquet.MenuModel ) {
			sb.append( ((edu.cmu.cs.dennisc.croquet.MenuModel)modelI).getTutorialNoteText() );
		} else if( modelI instanceof edu.cmu.cs.dennisc.croquet.Operation< ? > ){
			sb.append( ((edu.cmu.cs.dennisc.croquet.Operation< ? >)modelI).getTutorialNoteText() );
		}
		sb.append( "</em></strong>" );
		return sb.toString();
	}
	public static MenuSelectionNote createInstance( edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.RetargetableMenuModelInitializationEvent retargetableMenuModelInitializationEvent, edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent menuSelectionEvent, int i, edu.cmu.cs.dennisc.croquet.ModelContext< ? > modelContext, int index0 ) {
		return new MenuSelectionNote( retargetableMenuModelInitializationEvent, menuSelectionEvent, i, modelContext, index0 );
	}
	
	
	edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.RetargetableMenuModelInitializationEvent originalRetargetableMenuModelInitializationEvent;
	private edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent originalMenuSelectionEvent;
	private int i;
	private edu.cmu.cs.dennisc.croquet.ModelContext< ? > modelContext;

	private static FirstComponentResolver createComponentResolver( edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent menuSelectionEvent, int index ) {
		return new FirstComponentResolver( new ModelFromMenuSelectionResolver< edu.cmu.cs.dennisc.croquet.Model >( menuSelectionEvent, index ) );
	}
	
	private MenuSelectionNote( edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.RetargetableMenuModelInitializationEvent retargetableMenuModelInitializationEvent, edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent menuSelectionEvent, int i, edu.cmu.cs.dennisc.croquet.ModelContext< ? > modelContext, int index0 ) {
		super( getText( menuSelectionEvent, i ) );
		this.originalRetargetableMenuModelInitializationEvent = retargetableMenuModelInitializationEvent;
		this.originalMenuSelectionEvent = menuSelectionEvent;
		this.modelContext = modelContext;
		this.i = i;

		boolean isBelowStencil = AutomaticTutorial.getInstance().getStencil().getMenuPolicy().isBelowStencil();
		boolean isInMenuBar = index0 == 1;
		if( isBelowStencil ) {
			for( int j=index0; j<=i; j++ ) {
				this.addFeature( new MenuHole( createComponentResolver( menuSelectionEvent, j ), Feature.ConnectionPreference.EAST_WEST, j==i, true, false ) );
			}
		} else {
			boolean isCheckMarkRenderingDesired = AutomaticTutorial.getInstance().getStencil().getMenuPolicy().isFeedbackDesired();
			if( isInMenuBar ) {
				this.addFeature( new MenuHole( createComponentResolver( menuSelectionEvent, index0 ), Feature.ConnectionPreference.EAST_WEST, this.i == index0, true, false ) );
			}
			if( isInMenuBar==false || this.i > index0 ) {
				this.addFeature( new MenuHole( createComponentResolver( menuSelectionEvent, this.i ), Feature.ConnectionPreference.EAST_WEST, true, false, isCheckMarkRenderingDesired ) );
			}
		}
	}
	
	@Override
	public boolean isEventInterceptable( java.awt.event.MouseEvent e ) {
		return isMouseEventInterceptedInAllCases( e );
	}

	public boolean isAtLeastWhatWeveBeenWaitingFor( edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent menuSelectionEvent ) {
		final int N = this.i+1;
		if( menuSelectionEvent.getModelCount() >= N ) {
			for( int i=0; i<N; i++ ) {
				edu.cmu.cs.dennisc.croquet.Model orginalModelI = this.originalMenuSelectionEvent.getModelAt( i );
				edu.cmu.cs.dennisc.croquet.Model replacementModelI = menuSelectionEvent.getModelAt( i );
				if( orginalModelI == replacementModelI ) {
					//pass
				} else {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	
	@Override
	public boolean isWhatWeveBeenWaitingFor( edu.cmu.cs.dennisc.croquet.HistoryNode child ) {
		if( child instanceof edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.RetargetableMenuModelInitializationEvent ) {
			if( this.originalRetargetableMenuModelInitializationEvent != null ) {
				edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.RetargetableMenuModelInitializationEvent retargetableMenuModelInitializationEvent = (edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.RetargetableMenuModelInitializationEvent)child;
				edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext popupMenuOperationContext = (edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext)retargetableMenuModelInitializationEvent.getParent();
				edu.cmu.cs.dennisc.croquet.MenuModel menuModel = popupMenuOperationContext.getModel().getMenuModel();
				if( menuModel instanceof edu.cmu.cs.dennisc.croquet.RetargetableMenuModel ) {
					edu.cmu.cs.dennisc.croquet.RetargetableMenuModel retargetableMenuModel = (edu.cmu.cs.dennisc.croquet.RetargetableMenuModel)menuModel;
					edu.cmu.cs.dennisc.croquet.RetargetingData originalRetargetingData = this.originalRetargetableMenuModelInitializationEvent.getRetargetingData();
					edu.cmu.cs.dennisc.croquet.Retargeter retargeter = AutomaticTutorial.getInstance().getRetargeter();
					originalRetargetingData.addKeyValuePairs( retargeter, retargetableMenuModel.getRetargetableData() );
					AutomaticTutorial.getInstance().retargetOriginalContext( retargeter );
				}
			}
		} else if ( child instanceof edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent ) {
			edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent menuSelectionEvent = (edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent)child;
			final int N = this.i;
			if( menuSelectionEvent.getModelCount() == N ) {
				return this.isAtLeastWhatWeveBeenWaitingFor( menuSelectionEvent );
			}
		} else {
			if( this.originalMenuSelectionEvent.getModelCount() == this.i+1 ) {
				if( child instanceof edu.cmu.cs.dennisc.croquet.CommitEvent ) {
					return true;
				}
			}
		}
		return false;
	}
}
