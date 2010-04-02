import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.Array;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.alice.apis.moveandturn.Model;


import edu.cmu.cs.dennisc.alice.Project;
import edu.cmu.cs.dennisc.alice.ast.AbstractType;
import edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice;
import edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice;
import edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava;
import edu.cmu.cs.dennisc.alice.io.FileUtilities;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.cmu.cs.dennisc.zoot.ZFrame;

class TreeNodeUtilities {
	private static Map<Object, TreeNode> map = new HashMap<Object, TreeNode>();
	public static TreeNode get( Object key ) {
		TreeNode rv = map.get( key );
		if( rv != null ) {
			//pass
		} else {
			if( key instanceof FieldDeclaredInAlice ) {
				rv = new FieldTreeNode((FieldDeclaredInAlice)key);
			} else if( key instanceof Enum<?> ) {
				rv = new EnumConstantTreeNode( (Enum<?>)key );
			}
			map.put( key, rv );
		}
		return rv;
	}
}

abstract class AbstractTreeNode implements TreeNode {
	protected abstract List<Object> updateListsOrArraysForChildren( List<Object> rv );
	private Object[] children;
	protected void handleChange() {
		List<Object> listsOrArrays = new LinkedList<Object>();
		this.updateListsOrArraysForChildren( listsOrArrays );
		int n = 0;
		for( Object listOrArray : listsOrArrays ) {
			final int M;
			if( listOrArray.getClass().isArray() ) {
				M = Array.getLength( listOrArray );
			} else {
				List<?> list = ((List<?>)listOrArray);
				M = list.size();
			}
			n += M;
		}
		this.children = new Object[ n ];
		int i = 0;
		for( Object listOrArray : listsOrArrays ) {
			if( listOrArray.getClass().isArray() ) {
				final int M = Array.getLength( listOrArray );
				for( int j=0; j<M; j++ ) {
					this.children[ i++ ] = Array.get( listOrArray, j);
				}
			} else {
				List<?> list = ((List<?>)listOrArray);
				for( Object o : list ) {
					this.children[ i++ ] = o;
				}
			}
		}
	}
	public boolean getAllowsChildren() {
		return true;
	}

	public Enumeration<?> children() {
		return null;
	}

	public TreeNode getChildAt(int childIndex) {
		return TreeNodeUtilities.get( this.children[ childIndex ] );
	}

	public int getChildCount() {
		return this.children.length;
	}

	public int getIndex(TreeNode node) {
		return 0;
	}

	public TreeNode getParent() {
		return null;
	}

	public boolean isLeaf() {
		return getChildCount()==0;
	}
}

class FieldTreeNode extends AbstractTreeNode {
	private FieldDeclaredInAlice field;
	FieldTreeNode( FieldDeclaredInAlice field ) {
		this.field = field;
		this.handleChange();
	}
	@Override
	protected List<Object> updateListsOrArraysForChildren(List<Object> rv ) {
		AbstractType type = this.field.getValueType();
		rv.add( type.getDeclaredFields() );
		TypeDeclaredInJava typeJava = type.getFirstTypeEncounteredDeclaredInJava();
		Class<?> cls = typeJava.getClassReflectionProxy().getReification();
		assert cls != null;
		if( Model.class.isAssignableFrom(cls)) {
			Class<?>[] innerClses = cls.getDeclaredClasses();
			for( Class<?> innerCls : innerClses ) {
				//PrintUtilities.println(innerCls);
				if( innerCls.getSimpleName().equals("Part")) {
					Object[] constants = innerCls.getEnumConstants();
//					edu.cmu.cs.dennisc.lang.ArrayUtilities.reverseInPlace( constants );
					rv.add( constants );
				}
			}
		}
		return rv;
	}
	
	@Override
	public String toString() {
//		if( this.field != null ) {
			return this.field.getName();
//		} else {
//			return "<not set>";
//		}
	}
}

class EnumConstantTreeNode extends AbstractTreeNode {
	private Enum<?> enumConstant;
	public EnumConstantTreeNode( Enum<?> enumConstant ) {
		this.enumConstant = enumConstant;
		this.handleChange();
	}
	@Override
	protected List<Object> updateListsOrArraysForChildren(List<Object> rv) {
		return rv;
	}
	@Override
	public String toString() {
		return this.enumConstant.name();
	}
}

public class TreeTest {
	private static void showTreeFor( FieldDeclaredInAlice field ) {
		ZFrame frame = new ZFrame() {
			@Override
			protected void handleWindowOpened(WindowEvent e) {
			}
			@Override
			protected void handleQuit(EventObject e) {
				System.exit( 0 );
			}
		};
		JTree tree = new JTree( new DefaultTreeModel( TreeNodeUtilities.get(field)));
		
		tree.addTreeSelectionListener( new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				TreePath treePath = e.getPath();
				int N = treePath.getPathCount();
				if( N > 0 ) {
					Object last = treePath.getPathComponent( N-1 );
					PrintUtilities.println( last, last.getClass().getName() );
				}
			}
		} );
		
		frame.getContentPane().add( tree, BorderLayout.CENTER );
		frame.setSize( 320, 640 );
		
		frame.setVisible(true);
		
		
	}
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater( new Runnable(){
			public void run() {
				File root = FileUtilities.getMyProjectsDirectory( "Alice3" );
				File file = new File( root, "a.a3p" );
				Project project = FileUtilities.readProject( file );
				TypeDeclaredInAlice programType = (TypeDeclaredInAlice)project.getProgramType();
				showTreeFor( (FieldDeclaredInAlice)programType.fields.get( 0 ) );
			}
		} );
	}
}
