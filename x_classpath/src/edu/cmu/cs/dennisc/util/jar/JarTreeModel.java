package edu.cmu.cs.dennisc.util.jar;

class TreeNode extends javax.swing.tree.DefaultMutableTreeNode  {
	private String[] parts;
	public TreeNode( String[] parts ) {
		this.parts = parts;
	}
	protected String[] getParts() {
		return this.parts;
	}
	@Override
	public java.lang.String toString() {
		return java.util.Arrays.toString( this.parts );
	}
}

class PackageTreeNode extends TreeNode  {
	public PackageTreeNode( String[] parts ) {
		super( parts );
	}
	@Override
	public boolean getAllowsChildren() {
		return true;
	}
	public String getPackageName() {
		StringBuffer sb = new StringBuffer();
		for( String part : this.getParts() ) {
			sb.append( part );
			sb.append( "." );
		}
		if( sb.length() > 0 ) {
			sb.deleteCharAt( sb.length() - 1 );
		}
		return sb.toString();
	}
}

abstract class FileTreeNode extends TreeNode  {
	public FileTreeNode( String[] parts ) {
		super( parts );
	}
	public String getName() {
		StringBuffer sb = new StringBuffer();
		for( String part : this.getParts() ) {
			sb.append( part );
			sb.append( "." );
		}
		sb.delete( sb.length() - ".class.".length(), sb.length() );
		return sb.toString();
	}
	
}

class ClassTreeNode extends FileTreeNode  {
	public ClassTreeNode( String[] parts ) {
		super( parts );
	}
}

class ResourceTreeNode extends TreeNode  {
	public ResourceTreeNode( String[] parts ) {
		super( parts );
	}
}

public class JarTreeModel extends javax.swing.tree.DefaultTreeModel {
	public JarTreeModel( java.util.jar.JarFile jarFile ) {
		super( new PackageTreeNode( new String[] {} ) );
		java.util.Map<String, TreeNode> map = new java.util.HashMap<String, TreeNode>();
		map.put( "", (PackageTreeNode)this.getRoot());
		java.util.Enumeration< java.util.jar.JarEntry > e = jarFile.entries();
		while( e.hasMoreElements() ) {
			java.util.jar.JarEntry jarEntry = e.nextElement();
			String entryName = jarEntry.getName();
			edu.cmu.cs.dennisc.print.PrintUtilities.printlns( entryName );
			String[] parts = entryName.split( "/" );
			assert parts.length > 0;
			
			edu.cmu.cs.dennisc.print.PrintUtilities.println( parts[ 0 ] );
			if( parts[ 0 ].equals( "META-INF" ) ) {
				//pass

			
			//todo: remove
			} else if( entryName.startsWith( "com" ) ) {
				//pass
			
			
			
			} else {
				String last = parts[ parts.length - 1 ];
				TreeNode node;
				if( jarEntry.isDirectory() ) {
					node = new PackageTreeNode( parts );
				} else {
					if( last.endsWith( ".class" ) ) {
						node = new ClassTreeNode( parts );
					} else {
						node = new ResourceTreeNode( parts );
					}
				}
				map.put(entryName, node);
			}
		}
		for( String key : map.keySet() ) {
			if( key.length() == 0 ) {
				//pass
			} else {
				TreeNode child = map.get( key );
				assert child != null;
				String parentKey;
				int index = key.lastIndexOf( "/" );
				if( index != -1 ) {
					parentKey = key.substring(0,index+1);
				} else { 
					parentKey = "";
				}
				TreeNode parent = map.get( parentKey );
				if( parent != null ) {
					try {
						parent.add(child);
					} catch( IllegalArgumentException iae ) {
						System.err.println( parent );
						System.err.println( child );
						iae.printStackTrace();
					}
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
				javax.swing.JFrame frame = new javax.swing.JFrame();
				frame.setDefaultCloseOperation( javax.swing.WindowConstants.EXIT_ON_CLOSE );
				frame.getContentPane().add( new javax.swing.JTree( new JarTreeModel(jarFile)) );
				frame.pack();
				frame.setVisible(true);
			}
		} );
	}
}
