package org.alice.ide.declarationseditor.events;

import java.util.List;

import org.alice.ide.ast.draganddrop.BlockStatementIndexPair;
import org.alice.ide.croquet.edits.ast.InsertStatementEdit;
import org.alice.stageide.StageIDE;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeWithInternalBlank;
import org.lgna.croquet.imp.cascade.BlankNode;
import org.lgna.project.ast.BlockStatement;
import org.lgna.project.ast.ExpressionStatement;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserMethod;

public class AddEventListenerCascade extends CascadeWithInternalBlank<MethodInvocation> {
	private static class SingletonHolder {
		private static AddEventListenerCascade instance = new AddEventListenerCascade();
	}

	public static AddEventListenerCascade getInstance() {
		return SingletonHolder.instance;
	}

	private AddEventListenerCascade() {
		super( org.lgna.croquet.Application.PROJECT_GROUP, java.util.UUID.fromString( "dc90da69-a11f-4de4-8923-e410058762a3" ), MethodInvocation.class );
	}

	@Override
	protected org.lgna.croquet.edits.Edit createEdit( org.lgna.croquet.history.CompletionStep<org.lgna.croquet.Cascade<org.lgna.project.ast.MethodInvocation>> completionStep, org.lgna.project.ast.MethodInvocation[] values ) {
		NamedUserType sceneType = StageIDE.getActiveInstance().getSceneType();
		UserMethod method = sceneType.getDeclaredMethod( StageIDE.INITIALIZE_EVENT_LISTENERS_METHOD_NAME );
		BlockStatement body = method.body.getValue();
		BlockStatementIndexPair blockStatementIndexPair = new BlockStatementIndexPair(
				body,
				body.statements.size()
				);
		return new InsertStatementEdit( completionStep, blockStatementIndexPair, new ExpressionStatement( values[ 0 ] ) );
	}

	@Override
	protected List<CascadeBlankChild> updateBlankChildren( List<CascadeBlankChild> rv, BlankNode<MethodInvocation> blankNode ) {
		rv.add( TimeEventListenerMenu.getInstance() );
		rv.add( KeyboardEventListenerMenu.getInstance() );
		rv.add( MouseEventListenerMenu.getInstance() );
		rv.add( TransformationEventListenerMenu.getInstance() );
		return rv;
	}
}
