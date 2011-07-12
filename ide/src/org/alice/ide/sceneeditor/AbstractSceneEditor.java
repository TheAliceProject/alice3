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
package org.alice.ide.sceneeditor;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractSceneEditor extends org.lgna.croquet.components.BorderPanel implements edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice >, org.lgna.croquet.DropReceptor, FieldAndInstanceMapper {
	private edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField = null;
	private org.alice.ide.ProjectApplication.ProjectObserver projectObserver = new org.alice.ide.ProjectApplication.ProjectObserver() { 
		public void projectOpening( edu.cmu.cs.dennisc.alice.Project previousProject, edu.cmu.cs.dennisc.alice.Project nextProject ) {
		}
		public void projectOpened( edu.cmu.cs.dennisc.alice.Project previousProject, edu.cmu.cs.dennisc.alice.Project nextProject ) {
			AbstractSceneEditor.this.setProgramType( nextProject.getProgramType() );
			AbstractSceneEditor.this.revalidateAndRepaint();
		}
	};
	public abstract void disableRendering( org.alice.ide.ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering );
	public abstract void enableRendering( org.alice.ide.ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering );
	public abstract void setOmittingThisFieldAccesses( boolean isOmittingThisFieldAccesses );
	
//	private java.util.Map< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, Object > mapFieldToInstance = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
//	protected Object getAndRemoveInstanceForInitializingPendingField( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field ) {
//		if( this.mapFieldToInstance.containsKey( field ) ) {
//			Object rv = this.mapFieldToInstance.get( field );
//			this.mapFieldToInstance.remove( field );
//			return rv;
//		} else {
//			return null;
//		}
//	}
	public abstract void putInstanceForInitializingPendingField( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field, Object instance );
//		this.mapFieldToInstance.put( field, instance );
//	}
	
	public String getTutorialNoteText( org.lgna.croquet.Model model, org.lgna.croquet.edits.Edit< ? > edit, org.lgna.croquet.UserInformation userInformation ) {
		return "Drop...";
	}
	
//	@Deprecated
//	public abstract void handleFieldCreation( edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice<?> declaringType, edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field, Object instance, boolean isAnimationDesired );
	public abstract Object getInstanceInJavaForUndo( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field );
	
	public abstract void generateCodeForSetUp( edu.cmu.cs.dennisc.alice.ast.StatementListProperty bodyStatementsProperty, FieldAndInstanceMapper fieldAndInstanceMapper );
	
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getActiveInstance();
	}
	protected edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine getVM() {
		return this.getIDE().getVirtualMachineForSceneEditor();
	}
	public edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice getSceneField() {
		return this.sceneField;
	}
	protected void setSceneField( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField ) {
		if( this.sceneField != null ) {
			this.sceneField.removeListPropertyListener( this );
		}
		this.sceneField = sceneField;
		if( this.sceneField != null ) {
			this.sceneField.addListPropertyListener( this );
		}
	}
	protected void setProgramType( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> programType ) {
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice fieldInAlice;
		if( programType != null ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractField field = programType.getDeclaredFields().get( 0 );
			if( field instanceof edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice ) {
				fieldInAlice = (edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice)field;
			} else {
				fieldInAlice = null;
			}
		} else {
			fieldInAlice = null;
		}
		setSceneField( fieldInAlice );
	}

	public void added( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
	}
	public void adding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
	}
	public void cleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
	}
	public void clearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
	}
	public void removed( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
	}
	public void removing( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
	}
	public void set( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
	}
	public void setting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
	}

	@Override
	protected void handleAddedTo( org.lgna.croquet.components.Component< ? > parent ) {
		edu.cmu.cs.dennisc.alice.Project project = org.alice.ide.ProjectApplication.getActiveInstance().getProject();
		if( project != null ) {
			this.projectObserver.projectOpened(null, project);
		}
		org.alice.ide.ProjectApplication.getActiveInstance().addProjectObserver( this.projectObserver );
		super.handleAddedTo( parent );
	}
	@Override
	protected void handleRemovedFrom( org.lgna.croquet.components.Component< ? > parent ) {
		super.handleRemovedFrom( parent );
		org.alice.ide.ProjectApplication.getActiveInstance().removeProjectObserver( this.projectObserver );
	}
}
