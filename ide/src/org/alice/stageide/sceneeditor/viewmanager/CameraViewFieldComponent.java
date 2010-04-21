package org.alice.stageide.sceneeditor.viewmanager;

import org.alice.ide.memberseditor.templates.GetterTemplate;

import edu.cmu.cs.dennisc.alice.ast.AbstractField;
import edu.cmu.cs.dennisc.alice.ast.ThisExpression;

public class CameraViewFieldComponent extends GetterTemplate {
	
	protected AbstractField field;
	
	public CameraViewFieldComponent(edu.cmu.cs.dennisc.alice.ast.AbstractField field) {
		super(field);
		this.field = field;
	}
	
	public AbstractField getField()
	{
		return field;
	}
	
	@Override
	protected boolean isFieldInScope() 
	{
		return true;
	}
	
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.Expression createIncompleteExpression() {
		return org.alice.ide.ast.NodeUtilities.createFieldAccess(new ThisExpression(), this.field );
	}

}
