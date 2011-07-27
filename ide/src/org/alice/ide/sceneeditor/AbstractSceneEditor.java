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

import org.alice.ide.croquet.models.ui.IsSceneEditorExpandedState;
import org.lgna.croquet.components.BorderPanel.Constraint;
import org.lgna.story.ImplementationAccessor;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractSceneEditor extends org.lgna.croquet.components.BorderPanel {
	
	private java.util.Map< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice > mapSceneFieldToInstance = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice >();
	private java.util.Map< edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice, edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > mapSceneInstanceToField = new java.util.HashMap< edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice, edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice >();
	
	private edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> programType;
	private edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice programInstance;
	
	private org.alice.ide.ProjectApplication.ProjectObserver projectObserver = new org.alice.ide.ProjectApplication.ProjectObserver() { 
		public void projectOpening( edu.cmu.cs.dennisc.alice.Project previousProject, edu.cmu.cs.dennisc.alice.Project nextProject ) {
		}
		public void projectOpened( edu.cmu.cs.dennisc.alice.Project previousProject, edu.cmu.cs.dennisc.alice.Project nextProject ) {
			AbstractSceneEditor.this.setProgramType( nextProject.getProgramType() );
			AbstractSceneEditor.this.revalidateAndRepaint();
		}
	};
	
	private org.lgna.croquet.ListSelectionState.ValueObserver< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > selectedSceneObserver  = new org.lgna.croquet.ListSelectionState.ValueObserver< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice >() {
		public void changing( org.lgna.croquet.State< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > state, edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice prevValue, edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > state, edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice prevValue, edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice nextValue, boolean isAdjusting ) {
			AbstractSceneEditor.this.setActiveScene(nextValue);
		}
	};
	
	private org.lgna.croquet.State.ValueObserver< Boolean > editorExpandedObserver = new org.lgna.croquet.State.ValueObserver< Boolean >() {
		public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			handleExpandContractChange( nextValue );
		}
	};
	
	public abstract void disableRendering( org.alice.ide.ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering );
	public abstract void enableRendering( org.alice.ide.ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering );

	public abstract void putInstanceForInitializingPendingField( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field, Object instance );
	public abstract Object getInstanceInJavaForUndo( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field );
	
	public abstract void generateCodeForSetUp( edu.cmu.cs.dennisc.alice.ast.StatementListProperty bodyStatementsProperty );
	
	protected abstract void handleExpandContractChange( boolean isExpanded );
	
	
	//Initialization
	private boolean isInitialized = false;
	private void initializeIfNecessary()
	{
		if (!this.isInitialized)
		{
			initializeComponents();
			initializeObservers();
			this.isInitialized = true;
		}
	}
	
	protected void initializeComponents(){
		
	}
	protected void initializeObservers(){
		IsSceneEditorExpandedState.getInstance().addAndInvokeValueObserver(this.editorExpandedObserver);
	}
	
	
	public edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice getSceneField() {
		return SceneFieldListSelectionState.getInstance().getSelectedItem();
	}

	@Deprecated
	public Object getInstanceInAliceVMForField(
			edu.cmu.cs.dennisc.alice.ast.AbstractField field) {
		return null;
	}

	@Deprecated
	public edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice getFieldForInstanceInAliceVM(
			Object instance) {
		return null;
	}

	public edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice getFieldForInstanceInJavaVM(
			Object instanceInJava) {
		return getActiveSceneInstance().ACCEPTABLE_HACK_FOR_SCENE_EDITOR_getFieldForInstanceInJava(instanceInJava);
	}

	public Object getInstanceInJavaVMForField( edu.cmu.cs.dennisc.alice.ast.AbstractField field) {
		assert field instanceof edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice;
		return getActiveSceneInstance().getFieldValueInstanceInJava((edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice)field);
	}

	public <E> E getInstanceInJavaVMForField( edu.cmu.cs.dennisc.alice.ast.AbstractField field, Class<E> cls) {
		return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance(getInstanceInJavaVMForField(field), cls);
	}
	
	public  <T extends org.lgna.story.implementation.EntityImplementation> T getImplementation( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		org.lgna.story.Entity entity = getInstanceInJavaVMForField(field, org.lgna.story.Entity.class);
		if (entity != null)
		{
			return ImplementationAccessor.getImplementation(entity);
		}
		else
		{
			return null;
		}
	}
	
	public void addField( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field, edu.cmu.cs.dennisc.alice.ast.Statement... statements ){
		this.getActiveSceneType().fields.add(field);
		this.getVM().ACCEPTABLE_HACK_FOR_SCENE_EDITOR_initializeField(this.getActiveSceneInstance(), field);
		for (edu.cmu.cs.dennisc.alice.ast.Statement statement : statements)
		{
			this.getVM().ACCEPTABLE_HACK_FOR_SCENE_EDITOR_executeStatement(this.getActiveSceneInstance(), statement);
		}
	}
	
	public void removeField( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field, edu.cmu.cs.dennisc.alice.ast.Statement... statements ){
	
	}
	
	public edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getActiveSceneType()
	{
		edu.cmu.cs.dennisc.alice.ast.AbstractType type = this.getActiveSceneField().getValueType();
		if (type instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)
		{
			return (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)type;
		}
		return null;
	}
	
	public edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice getActiveSceneField()
	{
		return SceneFieldListSelectionState.getInstance().getSelectedItem();
	}
	
	public edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice getActiveSceneInstance()
	{
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice activeSceneField = SceneFieldListSelectionState.getInstance().getSelectedItem();
		return this.mapSceneFieldToInstance.get(activeSceneField);
	}
	
	public  <T extends org.lgna.story.implementation.EntityImplementation> T getActiveSceneImplementation() {
		org.lgna.story.Entity entity = getInstanceInJavaVMForField(getActiveSceneField(), org.lgna.story.Entity.class);
		if (entity != null)
		{
			return ImplementationAccessor.getImplementation(entity);
		}
		else
		{
			return null;
		}
	} 
	
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getActiveInstance();
	}
	protected edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine getVM() {
		return this.getIDE().getVirtualMachineForSceneEditor();
	}
	
	protected void addScene( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField ) {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice sceneType = (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice) sceneField.getValueType();
		edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice rv = getVM().ACCEPTABLE_HACK_FOR_SCENE_EDITOR_createInstanceWithInverseMapWithoutExcutingConstructorBody(sceneType);
		mapSceneFieldToInstance.put(sceneField, rv);
		mapSceneInstanceToField.put(rv, sceneField);
		SceneFieldListSelectionState.getInstance().addItem(sceneField);
	}
	
	protected void setActiveScene( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField ) {
		SceneFieldListSelectionState.getInstance().setSelectedItem(sceneField);

		//Run the "setActiveScene" call on the program to get the active scene set in the right state
//		edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice sceneAliceInstance = getActiveSceneInstance();
//		org.lookingglassandalice.storytelling.Scene sceneJavaInstance = (org.lookingglassandalice.storytelling.Scene)sceneAliceInstance.getInstanceInJava();
//		getProgramInstanceInJava().setActiveScene(sceneJavaInstance);
	}
	
	protected edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice getProgramInstanceInAlice()
	{
		return this.programInstance;
	}
	
	protected org.lgna.story.Program getProgramInstanceInJava()
	{
		return  (org.lgna.story.Program)this.programInstance.getInstanceInJava();
	}
	
	protected void setProgramInstance(edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice programInstance)
	{
		this.programInstance = programInstance;
	}
	
	protected void setProgramType( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> programType ) {
		this.programType = programType;
		SceneFieldListSelectionState.getInstance().removeValueObserver(this.selectedSceneObserver);
		SceneFieldListSelectionState.getInstance().clear();
		mapSceneFieldToInstance.clear();
		mapSceneInstanceToField.clear();
		if( this.programType != null ) {
			setProgramInstance((edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice)getVM().createInstanceEntryPoint(this.programType));
			for (edu.cmu.cs.dennisc.alice.ast.AbstractField programField : this.programType.getDeclaredFields())
			{
				if( programField.getDesiredValueType().isAssignableTo(org.lgna.story.Scene.class)) 
				{
					this.addScene((edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice)programField);
				}
			}
		} else {
			setProgramInstance( null );
		}
		if (SceneFieldListSelectionState.getInstance().getItemCount() > 0)
		{
			SceneFieldListSelectionState.getInstance().setSelectedIndex(0);
		}
		SceneFieldListSelectionState.getInstance().addAndInvokeValueObserver(this.selectedSceneObserver);
	}
	
	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		initializeIfNecessary();
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
