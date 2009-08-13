package edu.cmu.cs.dennisc.util.jar;

class PackageTreeNode extends javax.swing.tree.DefaultMutableTreeNode  {
	private String packageName;
	public PackageTreeNode( String[] parts ) {
		StringBuffer sb = new StringBuffer();
		for( String part : parts ) {
			sb.append( part );
			sb.append( "." );
		}
		if( sb.length() > 0 ) {
			sb.deleteCharAt( sb.length() - 1 );
		}
		this.packageName = sb.toString();
	}
}
class ClassTreeNode extends javax.swing.tree.DefaultMutableTreeNode  {
	private String className;
	public ClassTreeNode( String[] parts ) {
		StringBuffer sb = new StringBuffer();
		for( String part : parts ) {
			sb.append( part );
			sb.append( "." );
		}
		sb.delete( sb.length() - ".class.".length(), sb.length() );
		this.className = sb.toString();
	}
}

public class JarTreeModel extends javax.swing.tree.DefaultTreeModel {
	public JarTreeModel( java.util.jar.JarFile jarFile ) {
		super( new PackageTreeNode( new String[] {} ) );
		PackageTreeNode root = (PackageTreeNode)this.getRoot();
		for( String entryName : JarUtilities.getEntryNames( jarFile ) ) {
			String[] parts = entryName.split( "/" );
			assert parts.length > 0;
			if( parts[ 0 ].equals( "META-INF" ) ) {
				//pass
			} else {
				String last = parts[ parts.length - 1 ];
				javax.swing.tree.TreeNode node;
				if( last.endsWith( ".class" ) ) {
					node = new ClassTreeNode( parts );
				} else {
					node = new PackageTreeNode( parts );
				}
				edu.cmu.cs.dennisc.print.PrintUtilities.println( parts );
			}
		}
	}
}
