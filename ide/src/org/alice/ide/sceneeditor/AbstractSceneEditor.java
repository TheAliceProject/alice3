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
	
	private java.util.Map< org.lgna.project.ast.UserField, org.lgna.project.virtualmachine.InstanceInAlice > mapSceneFieldToInstance = new java.util.HashMap< org.lgna.project.ast.UserField, org.lgna.project.virtualmachine.InstanceInAlice >();
	private java.util.Map< org.lgna.project.virtualmachine.InstanceInAlice, org.lgna.project.ast.UserField > mapSceneInstanceToField = new java.util.HashMap< org.lgna.project.virtualmachine.InstanceInAlice, org.lgna.project.ast.UserField >();
	
	private org.lgna.project.ast.NamedUserType programType;
	private org.lgna.project.virtualmachine.InstanceInAlice programInstance;
	
	private org.alice.ide.ProjectApplication.ProjectObserver projectObserver = new org.alice.ide.ProjectApplication.ProjectObserver() { 
		public void projectOpening( org.lgna.project.Project previousProject, org.lgna.project.Project nextProject ) {
		}
		public void projectOpened( org.lgna.project.Project previousProject, org.lgna.project.Project nextProject ) {
			AbstractSceneEditor.this.setProgramType( nextProject.getProgramType() );
			AbstractSceneEditor.this.revalidateAndRepaint();
		}
	};
	
	private org.lgna.croquet.ListSelectionState.ValueObserver< org.lgna.project.ast.UserField > selectedSceneObserver  = new org.lgna.croquet.ListSelectionState.ValueObserver< org.lgna.project.ast.UserField >() {
		public void changing( org.lgna.croquet.State< org.lgna.project.ast.UserField > state, org.lgna.project.ast.UserField prevValue, org.lgna.project.ast.UserField nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.lgna.project.ast.UserField > state, org.lgna.project.ast.UserField prevValue, org.lgna.project.ast.UserField nextValue, boolean isAdjusting ) {
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

	public abstract void putInstanceForInitializingPendingField( org.lgna.project.ast.UserField field, Object instance );
	public abstract Object getInstanceInJavaForUndo( org.lgna.project.ast.UserField field );
	
	public abstract void generateCodeForSetUp( org.lgna.project.ast.StatementListProperty bodyStatementsProperty );
	
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
	
	
	public org.lgna.project.ast.UserField getSceneField() {
		return SceneFieldListSelectionState.getInstance().getSelectedItem();
	}

	@Deprecated
	public Object getInstanceInAliceVMForField(
			org.lgna.project.ast.AbstractField field) {
		return null;
	}

	@Deprecated
	public org.lgna.project.ast.UserField getFieldForInstanceInAliceVM(
			Object instance) {
		return null;
	}

	public org.lgna.project.ast.UserField getFieldForInstanceInJavaVM(
			Object instanceInJava) {
		return getActiveSceneInstance().ACCEPTABLE_HACK_FOR_SCENE_EDITOR_getFieldForInstanceInJava(instanceInJava);
	}

	public Object getInstanceInJavaVMForField( org.lgna.project.ast.AbstractField field) {
		assert field instanceof org.lgna.project.ast.UserField;
		return getActiveSceneInstance().getFieldValueInstanceInJava((org.lgna.project.ast.UserField)field);
	}

	public <E> E getInstanceInJavaVMForField( org.lgna.project.ast.AbstractField field, Class<E> cls) {
		return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance(getInstanceInJavaVMForField(field), cls);
	}
	
	public  <T extends org.lgna.story.implementation.EntityImplementation> T getImplementation( org.lgna.project.ast.AbstractField field ) {
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
	
	public void addField( org.lgna.project.ast.UserField field, org.lgna.project.ast.Statement... statements ){
		this.getActiveSceneType().fields.add(field);
		this.getVM().ACCEPTABLE_HACK_FOR_SCENE_EDITOR_initializeField(this.getActiveSceneInstance(), field);
		for (org.lgna.project.ast.Statement statement : statements)
		{
			this.getVM().ACCEPTABLE_HACK_FOR_SCENE_EDITOR_executeStatement(this.getActiveSceneInstance(), statement);
		}
	}
	
	public void removeField( org.lgna.project.ast.UserField field, org.lgna.project.ast.Statement... statements ){
	
	}
	
	public org.lgna.project.ast.NamedUserType getActiveSceneType()
	{
		org.lgna.project.ast.AbstractType type = this.getActiveSceneField().getValueType();
		if (type instanceof org.lgna.project.ast.NamedUserType)
		{
			return (org.lgna.project.ast.NamedUserType)type;
		}
		return null;
	}
	
	public org.lgna.project.ast.UserField getActiveSceneField()
	{
		return SceneFieldListSelectionState.getInstance().getSelectedItem();
	}
	
	public org.lgna.project.virtualmachine.InstanceInAlice getActiveSceneInstance()
	{
		org.lgna.project.ast.UserField activeSceneField = SceneFieldListSelectionState.getInstance().getSelectedItem();
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
	protected org.lgna.project.virtualmachine.VirtualMachine getVM() {
		return this.getIDE().getVirtualMachineForSceneEditor();
	}
	
	protected void addScene( org.lgna.project.ast.UserField sceneField ) {
		org.lgna.project.ast.NamedUserType sceneType = (org.lgna.project.ast.NamedUserType) sceneField.getValueType();
		org.lgna.project.virtualmachine.InstanceInAlice rv = getVM().ACCEPTABLE_HACK_FOR_SCENE_EDITOR_createInstanceWithInverseMap(sceneType);
		mapSceneFieldToInstance.put(sceneField, rv);
		mapSceneInstanceToField.put(rv, sceneField);
		SceneFieldListSelectionState.getInstance().addItem(sceneField);
	}
	
	protected void setActiveScene( org.lgna.project.ast.UserField sceneField ) {
		SceneFieldListSelectionState.getInstance().setSelectedItem(sceneField);

		//Run the "setActiveScene" call on the program to get the active scene set in the right state
//		edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice sceneAliceInstance = getActiveSceneInstance();
//		org.lookingglassandalice.storytelling.Scene sceneJavaInstance = (org.lookingglassandalice.storytelling.Scene)sceneAliceInstance.getInstanceInJava();
//		getProgramInstanceInJava().setActiveScene(sceneJavaInstance);
	}
	
	protected org.lgna.project.virtualmachine.InstanceInAlice getProgramInstanceInAlice()
	{
		return this.programInstance;
	}
	
	protected org.lgna.story.Program getProgramInstanceInJava()
	{
		return  (org.lgna.story.Program)this.programInstance.getInstanceInJava();
	}
	
	protected void setProgramInstance(org.lgna.project.virtualmachine.InstanceInAlice programInstance)
	{
		this.programInstance = programInstance;
	}
	
	protected void setProgramType( org.lgna.project.ast.NamedUserType programType ) {
		this.programType = programType;
		SceneFieldListSelectionState.getInstance().removeValueObserver(this.selectedSceneObserver);
		SceneFieldListSelectionState.getInstance().clear();
		mapSceneFieldToInstance.clear();
		mapSceneInstanceToField.clear();
		if( this.programType != null ) {
			setProgramInstance((org.lgna.project.virtualmachine.InstanceInAlice)getVM().ENTRY_POINT_createInstance(this.programType));
			for (org.lgna.project.ast.AbstractField programField : this.programType.getDeclaredFields())
			{
				if( programField.getDesiredValueType().isAssignableTo(org.lgna.story.Scene.class)) 
				{
					this.addScene((org.lgna.project.ast.UserField)programField);
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
		org.lgna.project.Project project = org.alice.ide.ProjectApplication.getActiveInstance().getProject();
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
