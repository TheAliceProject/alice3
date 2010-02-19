package org.alice.ide.operations.project;

public class ManageResourcesOperation extends org.alice.ide.operations.InconsequentialActionOperation {
	public ManageResourcesOperation() {
		this.putValue( javax.swing.Action.NAME, "Manage Resources..." );
	}
	@Override
	protected void performInternal( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		org.alice.ide.resource.ResourcesPane resourcesPane = new org.alice.ide.resource.ResourcesPane();
		javax.swing.JOptionPane.showMessageDialog( this.getIDE(), resourcesPane, "Project Resources", javax.swing.JOptionPane.PLAIN_MESSAGE, null );
	}	
}
