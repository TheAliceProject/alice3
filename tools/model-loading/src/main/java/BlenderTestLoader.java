import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.xml.sax.SAXException;

import com.dddviewr.collada.Collada;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.asset.ModelKey;
import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.asset.plugins.UrlAssetInfo;
import com.jme3.material.plugins.J3MLoader;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.plugins.OBJLoader;
import com.jme3.scene.plugins.blender.BlenderLoader;
import com.jme3.shader.plugins.GLSLLoader;
import com.jme3.system.JmeSystem;
import com.jme3.system.MockJmeSystemDelegate;

public class BlenderTestLoader {
	

	public static void printChildren( Spatial s, String indent ){
		if (s != null) {
			System.out.println( indent + s );
			if (s instanceof Node) {
				Node n = (Node)s;
				for (Spatial child : n.getChildren()) {
//					if (child instanceof Geometry) {
//						Geometry g = (Geometry)child;
//						System.out.println( g );
//					}
					printChildren( child, indent+"  " );
				}
			}
		}
	}
	
	public static void main( String[] args ) {
		
		File colladaFile  = new File( "C:/Users/dculyba/Documents/Alice/Collada/chicken.dae" );
		
		AliceModelLoader.loadAliceModelFromCollada( colladaFile, "chicken");

		
//		UrlAssetInfo uai;
//		try {
////			Object o = objLoader.load( UrlAssetInfo.create( dam, new ModelKey( "Chicken" ), objFile.toURL() ) );
//			Object o = blenderLoader.load( UrlAssetInfo.create( dam, new ModelKey( "Alfred" ), blenderFile.toURL() ) );
//			if (o instanceof Spatial) {
//				com.jme3.scene.Spatial s = (Spatial)o;
//				printChildren(s, "");
//			}
//			System.out.println( "loaded!" );
//		} catch( MalformedURLException e ) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch( IOException e ) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		

	}

}
