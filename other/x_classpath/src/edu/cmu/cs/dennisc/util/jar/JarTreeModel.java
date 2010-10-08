package edu.cmu.cs.dennisc.util.jar;

class TreeNode extends javax.swing.tree.DefaultMutableTreeNode  {
	private String filePath;
	public TreeNode( String filePath ) {
		this.filePath = filePath;
	}
	protected String getFilePath() {
		return this.filePath;
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		//sb.append( this.getClass().getName() );
		sb.append( this.getClass().getSimpleName() );
		sb.append( "[" );
		sb.append( this.filePath );
		sb.append( "]" );
		return sb.toString();
	}
}

class PackageTreeNode extends TreeNode  {
	public PackageTreeNode( String filePath ) {
		super( filePath );
	}
	@Override
	public boolean getAllowsChildren() {
		return true;
	}
	public Package getPackage() {
		String packageName = this.getFilePath().replace( '/', '.' );
		return Package.getPackage( packageName );
	}
}

abstract class FileTreeNode extends TreeNode  {
	public FileTreeNode( String filePath ) {
		super( filePath );
	}
	@Override
	public boolean getAllowsChildren() {
		return false;
	}
}

class ClassTreeNode extends FileTreeNode  {
	public ClassTreeNode( String filePath ) {
		super( filePath );
	}
	public Class<?> getCls() {
		String s = this.getFilePath().replace( '/', '.' );
		String className = s.substring( 0, s.length()- 6 );
		try {
			return edu.cmu.cs.dennisc.lang.ClassUtilities.forName( className );
		} catch( ClassNotFoundException cnfe ) {
			return null;
		}
	}
}

class ResourceTreeNode extends TreeNode  {
	public ResourceTreeNode( String filePath ) {
		super( filePath );
	}
}

public class JarTreeModel extends javax.swing.tree.DefaultTreeModel {
	private static final String ROOT_NAME = "";
	private static String getParentKey( String childKey ) {
		String rv;
		int index = childKey.lastIndexOf( "/", childKey.length()-2 );
		if( index != -1 ) {
			rv = childKey.substring(0,index+1);
		} else { 
			rv = ROOT_NAME;
		}
		return rv;
	}
	public JarTreeModel( java.util.jar.JarFile jarFile ) {
		super( new PackageTreeNode( ROOT_NAME ) );
		java.util.Map<String, TreeNode> map = new java.util.HashMap<String, TreeNode>();
		map.put( ROOT_NAME, (PackageTreeNode)this.getRoot());
		java.util.Enumeration< java.util.jar.JarEntry > e = jarFile.entries();
		while( e.hasMoreElements() ) {
			java.util.jar.JarEntry jarEntry = e.nextElement();
			String entryName = jarEntry.getName();
			if( entryName.startsWith( "META-INF" ) ) {
				//pass
			} else {
				TreeNode node;
				if( jarEntry.isDirectory() ) {
					node = new PackageTreeNode( entryName );
				} else {
					if( entryName.endsWith( ".class" ) ) {
						node = new ClassTreeNode( entryName );
					} else {
						node = new ResourceTreeNode( entryName );
					}
				}
				map.put(entryName, node);
			}
		}
		for( String key : map.keySet() ) {
			if( key.equals( ROOT_NAME ) ) {
				//pass
			} else {
				TreeNode child = map.get( key );
				assert child != null;
				String parentKey = getParentKey( key );
				TreeNode parent = map.get( getParentKey( key ) );
				if( parent != null ) {
					parent.add(child);
				} else {
					System.err.println( "cannot find parent key: " + parentKey );
				}
			}
		}
	}
	public static void main(String[] args) throws Exception {
		final java.util.jar.JarFile jarFile = JarUtilities.getJarFileOnClassPathNamed("mail.jar");
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				javax.swing.JTree tree = new javax.swing.JTree( new JarTreeModel(jarFile) );
				tree.setRootVisible( false );
				javax.swing.JFrame frame = new javax.swing.JFrame();
				frame.setDefaultCloseOperation( javax.swing.WindowConstants.EXIT_ON_CLOSE );
				frame.getContentPane().add( new javax.swing.JScrollPane( tree ) );
				frame.pack();
				frame.setVisible(true);
			}
		} );
	}
}
