package org.lgna.croquet.eclipse.autocompletion;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class GenerateUuidProposal implements IJavaCompletionProposal {

	public static void evaluateProposals( IType type, int offset, java.util.List<ICompletionProposal> result ) throws CoreException {
		// TODO: check if the type is UUID, and only add this proposal if it is
		result.add( new GenerateUuidProposal( type, offset ) );
	}

	private final IType type;
	private final int offset;
	private int relevance = 0;

	public GenerateUuidProposal( IType type, int offset ) {
		this.type = type;
		this.offset = offset;
		// TODO: Compute the relevance too... we want this to only go in croquet classes with type UUID
		this.relevance = 0;
	}

	@Override
	public void apply( IDocument document ) {
		try {
			String stub = "java.util.UUID.fromString( \"" + java.util.UUID.randomUUID().toString() + "\" )";
			String lineDelim = TextUtilities.getDefaultLineDelimiter( document );
			int indent = 0;
			String replacement = org.eclipse.jdt.internal.corext.util.CodeFormatterUtil.format( org.eclipse.jdt.core.formatter.CodeFormatter.K_EXPRESSION, stub, indent, lineDelim, this.type.getJavaProject() );
			document.replace( offset, 0, replacement );
		} catch( BadLocationException e ) {
		}
	}

	@Override
	public String getAdditionalProposalInfo() {
		return null;
	}

	@Override
	public IContextInformation getContextInformation() {
		return null;
	}

	@Override
	public String getDisplayString() {
		return "Generate UUID";
	}

	@Override
	public Image getImage() {
		return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor( ISharedImages.IMG_OBJ_ADD ).createImage();
	}

	@Override
	public Point getSelection( IDocument document ) {
		return new Point( offset, 0 );
	}

	@Override
	public int getRelevance() {
		return this.relevance;
	}
}
