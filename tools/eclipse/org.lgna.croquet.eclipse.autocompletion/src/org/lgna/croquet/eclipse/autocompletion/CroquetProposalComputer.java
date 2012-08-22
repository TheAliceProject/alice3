package org.lgna.croquet.eclipse.autocompletion;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.CompletionContext;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposalComputer;
import org.eclipse.jdt.ui.text.java.JavaContentAssistInvocationContext;
import org.eclipse.jface.text.contentassist.ICompletionProposal;

public class CroquetProposalComputer implements IJavaCompletionProposalComputer {

	public CroquetProposalComputer() {
	}

	@Override
	public java.util.List<ICompletionProposal> computeCompletionProposals( ContentAssistInvocationContext context, IProgressMonitor monitor ) {
		java.util.List<ICompletionProposal> completions = new java.util.ArrayList<ICompletionProposal>();

		try {
			CompletionContext coreContext = ( (JavaContentAssistInvocationContext)context ).getCoreContext();
			int offset = coreContext.getOffset();

			IJavaElement enclosingElement = ( (JavaContentAssistInvocationContext)context ).getCompilationUnit().getElementAt( offset );
			if( enclosingElement != null ) {
				IType type = (IType)enclosingElement.getAncestor( IJavaElement.TYPE );
				if( type != null ) {
					GenerateUuidProposal.evaluateProposals( type, offset, completions );
				}
			}
		} catch( Exception e ) {
		}
		return completions;
	}

	@Override
	public java.util.List<org.eclipse.jface.text.contentassist.IContextInformation> computeContextInformation(
			org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext context,
			org.eclipse.core.runtime.IProgressMonitor monitor ) {
		return null;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public void sessionEnded() {
	}

	@Override
	public void sessionStarted() {
	}
}
