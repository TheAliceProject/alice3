/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */

package edu.cmu.cs.dennisc.scenegraph.io;

/**
 * @author Dennis Cosgrove
 */
public class OBJ {
	private static double getNextNumber( java.io.StreamTokenizer streamTokenizer ) {
		try {
			streamTokenizer.nextToken();
			if( streamTokenizer.ttype==java.io.StreamTokenizer.TT_NUMBER ) {
				double f = streamTokenizer.nval;
				streamTokenizer.nextToken();
				if (streamTokenizer.ttype==java.io.StreamTokenizer.TT_WORD ) {
					if( streamTokenizer.sval.startsWith( "E" ) ) {
						int exponent = Integer.parseInt( streamTokenizer.sval.substring( 1 ) );
						return f*Math.pow( 10, exponent );
					}
				}
				streamTokenizer.pushBack();
				return f;
			}
		} catch( java.io.IOException ioe ) {
			ioe.printStackTrace();
		}
		return Double.NaN;
	}

	public static edu.cmu.cs.dennisc.scenegraph.IndexedTriangleArray decode( java.io.InputStream is ) throws java.io.IOException {
		java.io.BufferedReader r = new java.io.BufferedReader( new java.io.InputStreamReader( is ) );
		java.io.StreamTokenizer st = new java.io.StreamTokenizer(r);
		st.commentChar( '#' );
		st.slashSlashComments( false );
		st.slashStarComments( false );
		st.whitespaceChars( '/', '/' );
		st.parseNumbers();
		java.util.Vector<double[]> xyzs = new java.util.Vector<double[]>();
		java.util.Vector<double[]> ijks = new java.util.Vector<double[]>();
		java.util.Vector<double[]> uvs = new java.util.Vector<double[]>();
		java.util.Vector<java.util.Vector<Integer>> fs = new java.util.Vector<java.util.Vector<Integer>>();
		while (st.nextToken()==java.io.StreamTokenizer.TT_WORD) {
			if( st.sval.startsWith( "vt" ) ) {
				double uv[] = new double[3];
				uv[0] = getNextNumber( st );
				uv[1] = getNextNumber( st );
				uvs.addElement( uv );
			} else if( st.sval.startsWith( "vn" ) ) {
				double ijk[] = new double[3];
				ijk[0] = getNextNumber( st );
				ijk[1] = getNextNumber( st );
				ijk[2] = getNextNumber( st );
				ijks.addElement( ijk );
			} else if( st.sval.startsWith( "v" ) ) {
				double xyz[] = new double[3];
				xyz[0] = getNextNumber( st );
				xyz[1] = getNextNumber( st );
				xyz[2] = getNextNumber( st );
				xyzs.addElement( xyz );
			} else if( st.sval.startsWith( "f" ) ) {
				java.util.Vector<Integer> f = new java.util.Vector<Integer>();
				while( st.nextToken()==java.io.StreamTokenizer.TT_NUMBER ) {
					int index = (int)st.nval;
					if( index < 0 ) {
						//todo: account for different lengthed ijks and uvs
						index += xyzs.size();
					} else {
						index -= 1;
					}
					f.addElement( index );
				}
				st.pushBack();
				fs.addElement( f );
			} else {
				break;
			}
		}
		int nVertexCount = xyzs.size();
		edu.cmu.cs.dennisc.scenegraph.Vertex[] vertices = new edu.cmu.cs.dennisc.scenegraph.Vertex[nVertexCount];
		double ijkDefault[] = new double[3];
		ijkDefault[0] = 0;
		ijkDefault[1] = 1;
		ijkDefault[2] = 0;
		double uvDefault[] = new double[2];
		uvDefault[0] = 0;
		uvDefault[1] = 0;
		for (int v=0; v<nVertexCount; v++) {
			double xyz[] = xyzs.elementAt(v);
			double ijk[];
			double uv[];
			try {
				ijk = ijks.elementAt(v);
			} catch( ArrayIndexOutOfBoundsException e ) {
				ijk = ijkDefault;
			}
			try {
				uv = uvs.elementAt(v);
			} catch( ArrayIndexOutOfBoundsException e ) {
				uv = uvDefault;
			}
			vertices[v] = edu.cmu.cs.dennisc.scenegraph.Vertex.createXYZIJKUV( xyz[0], xyz[1], xyz[2], (float)ijk[0], (float)ijk[1], (float)ijk[2], (float)uv[0], (float)uv[1] );
			//System.err.println( v + " " + vertices[v] );
		}
		//todo
		int[] indices = new int[fs.size()*3];
		int i = 0;
		for( int f=0; f<fs.size(); f++ ) {
			java.util.Vector<Integer> face = fs.elementAt(f);
			switch( face.size() ) {
			case 3:
				indices[i++] = face.elementAt( 0 ).intValue();
				indices[i++] = face.elementAt( 1 ).intValue();
				indices[i++] = face.elementAt( 2 ).intValue();
				break;
			case 6:
				indices[i++] = face.elementAt( 0 ).intValue();
				indices[i++] = face.elementAt( 2 ).intValue();
				indices[i++] = face.elementAt( 4 ).intValue();
				break;
			case 9:
				indices[i++] = face.elementAt( 0 ).intValue();
				indices[i++] = face.elementAt( 3 ).intValue();
				indices[i++] = face.elementAt( 6 ).intValue();
				break;
			default:
				throw new RuntimeException( "unhandled face index size" );
			}
		}
		edu.cmu.cs.dennisc.scenegraph.IndexedTriangleArray ita = new edu.cmu.cs.dennisc.scenegraph.IndexedTriangleArray();
		ita.vertices.setValue( vertices );
		ita.polygonData.setValue( indices );
		
		for( int index : indices ) {
			assert index >= 0; 
			assert index < vertices.length;
		}
		
		return ita;
	}
	public static void encode( java.io.OutputStream os, edu.cmu.cs.dennisc.scenegraph.IndexedTriangleArray ita, edu.cmu.cs.dennisc.math.AffineMatrix4x4 m, String groupName ) {
		edu.cmu.cs.dennisc.scenegraph.Vertex[] vertices = ita.vertices.getValue();
		int[] indices = ita.polygonData.getValue();
		if( vertices!=null && indices!=null ) {
			java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream( os );
			java.io.PrintWriter pw = new java.io.PrintWriter( bos );
			if( groupName!=null ) {
				pw.println( "g " + groupName );
			}
			edu.cmu.cs.dennisc.math.Point3 p = new edu.cmu.cs.dennisc.math.Point3();
			edu.cmu.cs.dennisc.math.Vector3f n = new edu.cmu.cs.dennisc.math.Vector3f();
			for( int lcv=0; lcv<vertices.length; lcv++ ) {
				p.set( vertices[lcv].position );
				n.set( vertices[lcv].normal );
				double u = vertices[lcv].textureCoordinate0.u;
				double v = vertices[lcv].textureCoordinate0.v;
				if( m!=null ) {
					m.transform( p );
					m.transform( n );
				}
				pw.print( "v " );
				pw.print( p.x );
				pw.print( " " );
				pw.print( p.y );
				pw.print( " " );
				pw.print( p.z );
				pw.println();
				pw.print( "vt " );
				pw.print( u );
				pw.print( " " );
				pw.print( v );
				pw.println();
				pw.print( "vn " );
				pw.print( n.x );
				pw.print( " " );
				pw.print( n.y );
				pw.print( " " );
				pw.print( n.z );
				pw.println();
			}
			for( int i=0; i<indices.length; i+=3 ) {
				pw.print( "f " );
				for( int j=0; j<3; j++ ) {
					int a = indices[i+j]-vertices.length;
					pw.print( a+"/"+a+"/"+a+" " );
				}
				pw.println();
			}
			pw.flush();
		}
	}
}
