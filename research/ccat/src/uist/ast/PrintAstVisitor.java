package uist.ast;

public class PrintAstVisitor implements edu.cmu.cs.dennisc.pattern.Crawler {

	private java.util.ArrayList<org.lgna.project.ast.Node> depthMap = new java.util.ArrayList<org.lgna.project.ast.Node>();

	public PrintAstVisitor( org.lgna.project.ast.Node root ) {
		depthMap.add(0, root);
	}

	private int getDepth( org.lgna.project.ast.Node node ) {
		org.lgna.project.ast.Node parent = node.getParent();
		int depth = 0;
		if ( parent == null ) {
			this.depthMap.clear();
			this.depthMap.add( 0, node );
			return depth;
		}
		for ( int i = this.depthMap.size() - 1; i >= 0; i-- ) {
			org.lgna.project.ast.Node n = this.depthMap.get(i);
			if ( n == node ) {
				depth = i;
				break;
			} else if ( n == parent ) {
				depth = i + 1;
				this.depthMap.add(depth, node);
				break;
			}
		}
		return depth;
	}

	private String getPadding( int indent ) {
		StringBuilder sb = new StringBuilder();
		for ( int i = 0; i < indent; i++ ) {
			sb.append(" ");
		}
		return sb.toString();
	}

	private String getIndent( org.lgna.project.ast.Node node ) {
		return getPadding( getDepth( node ) );
	}

	private void print( org.lgna.project.ast.Node node ) {
		System.out.println( getIndent(node) + node );
	}

	public void visit(edu.cmu.cs.dennisc.pattern.Crawlable crawlable) {
		print( (org.lgna.project.ast.Node)crawlable );
	}
}
