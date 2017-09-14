/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */

package edu.cmu.cs.dennisc.nebulous.javabased;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;



public class Utilities {
	
    public static boolean BIG_ENDIAN = false;
 
    private static final int BUFFER = 2048;
    private static byte data[] = new byte[BUFFER];


    public static void writeStreamToZip(ZipOutputStream zip, String entryName, InputStream in) throws IOException
    {
        zip.putNextEntry(new ZipEntry(entryName));
        int c;
        while ((c = in.read(data, 0, BUFFER)) != -1){ zip.write(data, 0, c); }
        zip.closeEntry();
    }

    public static void writeFileToZip(ZipOutputStream zip, File file) throws IOException
    {
        FileInputStream in = new FileInputStream( file );
        writeStreamToZip(zip, file.getName(), in);
        in.close();
    }
    
    public static void writeStringToZip(ZipOutputStream zip, String name, String data) throws IOException
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(data.getBytes());
        writeStreamToZip(zip, name, bais);
        bais.close();
    }

    
    public static byte[] extractBytes( java.util.zip.ZipInputStream zis, java.util.zip.ZipEntry zipEntry ) throws java.io.IOException {
        final int BUFFER_SIZE = 2048;
        byte[] buffer = new byte[BUFFER_SIZE];
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream( BUFFER_SIZE );
        int count;
        while ((count = zis.read( buffer, 0, BUFFER_SIZE )) != -1) {
            baos.write( buffer, 0, count );
        }
        zis.closeEntry();
        return baos.toByteArray();
    }
    
    public static void printBytes(byte[] bytes)
    {
        System.out.println("("+bytes.length+")");
        for (int i=0; i<bytes.length; i++)
        {
            byte b = bytes[i];
            System.out.printf("%02X", b);
            if (i < bytes.length - 1)
            {
                System.out.print(":");
            }
            if (i > 0 && i % 26 == 0)
            {
                System.out.println();
            }
        }
    }
    
	public static final byte[] intToByteArray(int value)
	{
	    if (BIG_ENDIAN)
	    {
    		return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value
            };
	    }
	    else
	    {
	        return new byte[] {
                (byte)(value),
    	        (byte)(value >>> 8),
    	        (byte)(value >>> 16),
    	        (byte)(value >>> 24) 
	        };
	    }
	}
	
	public static final int byteArrayToInt(byte[] bytes)
	{
		int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (BIG_ENDIAN) ? (4 - 1 - i) * 8 : i*8;
            value += (bytes[i] & 0x000000FF) << shift;
        }
        return value;
	}
	
	public static final byte[] shortToByteArray(short value)
	{
	    if (BIG_ENDIAN)
        {
    		return new byte[] {
                (byte)(value >>> 8),
                (byte)value
            };
        }
	    else
	    {
	        return new byte[] {
                (byte)value,
                (byte)(value >>> 8)
            };
	    }
	}
	
	public static final short byteArrayToShort(byte[] bytes)
	{
		short value = 0;
        for (int i = 0; i < 2; i++) {
            int shift = (BIG_ENDIAN) ? (2 - 1 - i) * 8 : i*8;
            value += (bytes[i] & 0x000000FF) << shift;
        }
        return value;
	}
	
	public static final byte[] floatToByteArray(float value)
	{
		int intBits = Float.floatToRawIntBits(value);
		return intToByteArray(intBits);
	}
	
	public static final float byteArrayToFloat(byte[] bytes)
	{
		int intBits = byteArrayToInt(bytes);
		float value = Float.intBitsToFloat(intBits);
        return value;
	}
	
	public static void writeBool(boolean value, OutputStream oStream) throws IOException
	{
	    oStream.write( (value) ? 1 : 0 );
	}
	
	public static boolean readBool(InputStream iStream) throws IOException
    {
        int boolVal = iStream.read();
        if (boolVal == 0)
        {
            return false;
        }
        return true;
    }
	
	public static void writeInt(int value, OutputStream oStream) throws IOException
	{
		byte[] bytes = intToByteArray(value);
		oStream.write(bytes);
	}
	
	public static int readInt(InputStream iStream) throws IOException
	{
		byte[] bytes = new byte[Integer.SIZE / 8];
		int bytesRead = iStream.read(bytes);
		assert bytesRead == (Integer.SIZE / 8);
		int toReturn = Utilities.byteArrayToInt(bytes);
		return toReturn;
	}
	
	public static void writeShort(short value, OutputStream oStream) throws IOException
	{
		byte[] bytes = shortToByteArray(value);
		oStream.write(bytes);
	}
	
	public static short readShort(InputStream iStream) throws IOException
	{
		byte[] bytes = new byte[Short.SIZE/8];
		int bytesRead = iStream.read(bytes);
		assert bytesRead == (Short.SIZE/8);
		return Utilities.byteArrayToShort(bytes);
	}
	
	public static void writeFloat(float value, OutputStream oStream) throws IOException
	{
		byte[] bytes = floatToByteArray(value);
		oStream.write(bytes);
	}
	
	public static float readFloat(InputStream iStream) throws IOException
	{
		byte[] bytes = new byte[Float.SIZE/8];
		int bytesRead = iStream.read(bytes);
		assert bytesRead == (Float.SIZE/8);
		return Utilities.byteArrayToFloat(bytes);
	}
	
	public static int[] readRawIntArray(InputStream iStream, int numElements) throws IOException
    {
        int[] intArray = new int[numElements];
        for (int i=0; i<numElements; i++)
        {
            intArray[i] = readInt(iStream);
        }
        return intArray;
    }
	
	public static int[] readIntArray(InputStream iStream) throws IOException
	{
		int numElements = Utilities.readInt(iStream);
		return readRawIntArray(iStream, numElements);
	}
	
	public static void writeRawIntArray(OutputStream oStream, int[] intArray) throws IOException
    {
        for (int i=0; i<intArray.length; i++)
        {
            writeInt(intArray[i], oStream);
        }
    }
	
	public static void writeIntArray(OutputStream oStream, int[] intArray) throws IOException
	{
		writeInt(intArray.length, oStream);
		writeRawIntArray(oStream, intArray);
	}
	
	public static short[] readRawShortArray(InputStream iStream, int numElements) throws IOException
    {
        short[] shortArray = new short[numElements];
        for (int i=0; i<numElements; i++)
        {
            shortArray[i] = readShort(iStream);
        }
        return shortArray;
    }
	
	public static short[] readShortArray(InputStream iStream) throws IOException
	{
		int numElements = Utilities.readInt(iStream);
		return readRawShortArray(iStream, numElements);
	}
	
	public static void writeRawShortArray(OutputStream oStream, short[] shortArray) throws IOException
    {
        for (int i=0; i<shortArray.length; i++)
        {
            writeShort(shortArray[i], oStream);
        }
    }
	
	public static void writeShortArray(OutputStream oStream, short[] shortArray) throws IOException
	{
		writeInt(shortArray.length, oStream);
		writeRawShortArray(oStream, shortArray);
	}
	
	public static float[] readRawFloatArray(InputStream iStream, int numElements) throws IOException
    {
        float[] floatArray = new float[numElements];
        for (int i=0; i<numElements; i++)
        {
            floatArray[i] = readFloat(iStream);
        }
        return floatArray;
    }
	
	public static float[] readFloatArray(InputStream iStream) throws IOException
	{
		int numElements = Utilities.readInt(iStream);
		return readRawFloatArray(iStream, numElements);
	}
	
	public static void writeRawFloatArray(OutputStream oStream, float[] floatArray) throws IOException
    {
        for (int i=0; i<floatArray.length; i++)
        {
            writeFloat(floatArray[i], oStream);
        }
    }
	
	public static void writeFloatArray(OutputStream oStream, float[] floatArray) throws IOException
	{
		writeInt(floatArray.length, oStream);
		writeRawFloatArray(oStream, floatArray);
	}
	
	public static List<Short> convertShortArrayToList(short[] array)
    {
        List<Short> list = new LinkedList<Short>();
        for (short i : array)
        {
            list.add(i);
        }
        return list;
    }
	
	public static List<Integer> convertIntArrayToList(int[] array)
	{
		List<Integer> list = new LinkedList<Integer>();
		for (int i : array)
		{
			list.add(i);
		}
		return list;
	}
	
	public static List<Float> convertFloatArrayToList(float[] array)
	{
		List<Float> list = new LinkedList<Float>();
		for (float i : array)
		{
			list.add(i);
		}
		return list;
	}
	
	public static List<Double> convertDoubleArrayToList(double[] array)
	{
		List<Double> list = new LinkedList<Double>();
		for (double i : array)
		{
			list.add(i);
		}
		return list;
	}
	
	public static short[] convertListToShortArray(List<Short> list)
    {
        short[] array = new short[list.size()];
        for (int i=0; i<list.size(); i++)
        {
            array[i] = list.get(i);
        }
        return array;
    }
	
	public static int[] convertShortListToIntArray(List<Short> list)
    {
        int[] array = new int[list.size()];
        for (int i=0; i<list.size(); i++)
        {
            array[i] = list.get(i).intValue();
        }
        return array;
    }
	
	public static int[] convertListToIntArray(List<Integer> list)
	{
		int[] array = new int[list.size()];
		for (int i=0; i<list.size(); i++)
		{
			array[i] = list.get(i);
		}
		return array;
	}
	
	public static float[] convertListToFloatArray(List<Float> list)
	{
		float[] array = new float[list.size()];
		for (int i=0; i<list.size(); i++)
		{
			array[i] = list.get(i);
		}
		return array;
	}
	
	public static double[] convertListToDoubleArray(List<Double> list)
	{
		double[] array = new double[list.size()];
		for (int i=0; i<list.size(); i++)
		{
			array[i] = list.get(i);
		}
		return array;
	}
	
	public static short[] addShortToArray(short[] array, short value)
    {
	    short[] newArray = new short[array.length+1];
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[newArray.length-1] = value;
        return newArray;
    }
	
	public static int[] addIntToArray(int[] array, int value)
	{
		int[] newArray = new int[array.length+1];
		System.arraycopy(array, 0, newArray, 0, array.length);
		newArray[newArray.length-1] = value;
		return newArray;
	}
	
	public static float[] addFloatToArray(float[] array, float value)
	{
		float[] newArray = new float[array.length+1];
		System.arraycopy(array, 0, newArray, 0, array.length);
		newArray[newArray.length-1] = value;
		return newArray;
	}
	
	public static double[] addDoubleToArray(double[] array, double value)
	{
		double[] newArray = new double[array.length+1];
		System.arraycopy(array, 0, newArray, 0, array.length);
		newArray[newArray.length-1] = value;
		return newArray;
	}
	
	public static String getNameFromFile(File file)
	{
	    String fileName = file.getName();
        int extensionIndex = fileName.indexOf(".");
        fileName = fileName.substring(0, extensionIndex);
        return fileName;
	}
	
	public static String getNameFromFile(String fileName)
    {
	    return getNameFromFile(new File(fileName));
    }
	
	public static String normalizeSeparators(String path)
	{
	    return path.replace("/", File.separator);
	}
	
	public static int[] convertIntBufferToArray( IntBuffer buf )
	{
	    if (buf.hasArray())
	    {
	        return buf.array();
	    }
	    else
	    {
	        buf.rewind();
	        int[] intArray = new int[buf.remaining()];
	        int index = 0;
	        while (buf.hasRemaining())
	        {
	            intArray[index++] = buf.get();
	        }
	        return intArray;
	    }
	}
	
	public static double[] convertFloatBufferToDoubleArray( FloatBuffer buf )
    {
        buf.rewind();
        double[] floatArray = new double[buf.remaining()];
        int index = 0;
        while (buf.hasRemaining())
        {
            floatArray[index++] = buf.get();
        }
        return floatArray;
    }
	
	public static float[] convertFloatBufferToArray( FloatBuffer buf )
    {
        if (buf.hasArray())
        {
            return buf.array();
        }
        else
        {
            buf.rewind();
            float[] floatArray = new float[buf.remaining()];
            int index = 0;
            while (buf.hasRemaining())
            {
                floatArray[index++] = buf.get();
            }
            return floatArray;
        }
    }
    
	
	public static FloatBuffer createFloatBuffer( float[] data )
	{
	    if (data == null)
	    {
	        return null;
	    }
        FloatBuffer buf = ByteBuffer.allocateDirect((Float.SIZE / 8)  * data.length).order(ByteOrder.nativeOrder()).asFloatBuffer();
        buf.clear();
        buf.put(data);
        buf.flip();
        return buf;
	}
	
	public static DoubleBuffer createDoubleBuffer( double[] data )
    {
        if (data == null)
        {
            return null;
        }
        DoubleBuffer buf = ByteBuffer.allocateDirect((Double.SIZE / 8) * data.length).order(ByteOrder.nativeOrder()).asDoubleBuffer();
        buf.clear();
        buf.put(data);
        buf.flip();
        return buf;
    }
	
	public static DoubleBuffer createDoubleBuffer( FloatBuffer data )
    {
        if (data == null)
        {
            return null;
        }
        DoubleBuffer buf = createDoubleBuffer( convertFloatBufferToDoubleArray(data) );
        return buf;
    }
	
	public static double[] convertDoubleBufferToArray( DoubleBuffer buf )
    {
        if (buf.hasArray())
        {
            return buf.array();
        }
        else
        {
            buf.rewind();
            double[] doubleArray = new double[buf.remaining()];
            int index = 0;
            while (buf.hasRemaining())
            {
            	doubleArray[index++] = buf.get();
            }
            return doubleArray;
        }
    }
	
	public static DoubleBuffer copyDoubleBuffer( DoubleBuffer data )
    {
        if (data == null)
        {
            return null;
        }
        DoubleBuffer buf = createDoubleBuffer( convertDoubleBufferToArray(data) );
        return buf;
    }
	
	public static FloatBuffer copyFloatBuffer( FloatBuffer data )
    {
        if (data == null)
        {
            return null;
        }
        FloatBuffer buf = createFloatBuffer( convertFloatBufferToArray(data) );
        return buf;
    }
	
	public static IntBuffer createIntBuffer( int[] data )
    {
        if (data == null)
        {
            return null;
        }
        IntBuffer buf = ByteBuffer.allocateDirect((Integer.SIZE / 8)  * data.length).order(ByteOrder.nativeOrder()).asIntBuffer();
        buf.clear();
        buf.put(data);
        buf.flip();
        return buf;
    }
	
	public static IntBuffer copyIntBuffer( IntBuffer data )
    {
        if (data == null)
        {
            return null;
        }
        IntBuffer buf = createIntBuffer( convertIntBufferToArray(data) );
        return buf;
    }
	
	public static edu.cmu.cs.dennisc.scenegraph.WeightedMesh copySgWeightedMesh( edu.cmu.cs.dennisc.scenegraph.WeightedMesh toCopy )
	{
		edu.cmu.cs.dennisc.scenegraph.WeightedMesh rv = new edu.cmu.cs.dennisc.scenegraph.WeightedMesh();
		rv.cullBackfaces.setValue(toCopy.cullBackfaces.getValue());
		rv.textureId.setValue(toCopy.textureId.getValue());
		rv.useAlphaTest.setValue(toCopy.useAlphaTest.getValue());
		rv.indexBuffer.setValue(Utilities.copyIntBuffer(toCopy.indexBuffer.getValue()));
		rv.normalBuffer.setValue(Utilities.copyFloatBuffer(toCopy.normalBuffer.getValue()));
		rv.textCoordBuffer.setValue(Utilities.copyFloatBuffer(toCopy.textCoordBuffer.getValue()));
		rv.vertexBuffer.setValue(Utilities.copyDoubleBuffer(toCopy.vertexBuffer.getValue()));
		rv.setName(toCopy.getName());

		//None copied values
		rv.weightInfo.setValue(toCopy.weightInfo.getValue());
		rv.skeleton.setValue(toCopy.skeleton.getValue());
		
		
		return rv;
	}
	
	public static edu.cmu.cs.dennisc.scenegraph.WeightedMesh createSgWeightedMeshFromControl( edu.cmu.cs.dennisc.nebulous.javabased.UtilityWeightedMeshControl control )
	{
		edu.cmu.cs.dennisc.scenegraph.WeightedMesh rv = new edu.cmu.cs.dennisc.scenegraph.WeightedMesh();
		rv.cullBackfaces.setValue(control.getSgWeightedMesh().cullBackfaces.getValue());
		rv.textureId.setValue(control.getSgWeightedMesh().textureId.getValue());
		rv.useAlphaTest.setValue(control.getSgWeightedMesh().useAlphaTest.getValue());
		rv.indexBuffer.setValue(Utilities.copyIntBuffer(control.getIndexBuffer()));
		rv.normalBuffer.setValue(Utilities.copyFloatBuffer(control.getTransformedNormals()));
		rv.textCoordBuffer.setValue(Utilities.copyFloatBuffer(control.getTextCoordBuffer()));
		rv.vertexBuffer.setValue(Utilities.copyDoubleBuffer(control.getTransformedVertices()));
		rv.setName(control.getSgWeightedMesh().getName());

		//None copied values
		rv.weightInfo.setValue(control.getSgWeightedMesh().weightInfo.getValue());
		rv.skeleton.setValue(control.getSgWeightedMesh().skeleton.getValue());
		
		
		return rv;
	}
}
