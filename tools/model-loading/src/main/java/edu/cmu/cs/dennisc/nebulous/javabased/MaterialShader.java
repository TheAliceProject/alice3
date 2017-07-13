package edu.cmu.cs.dennisc.nebulous.javabased;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MaterialShader extends Shader {

	@Override
    public int getClassID() 
    {
        return Element.MATERIAL_SHADER; 
    }
	
	@Override
	protected void readInternal(InputStream iStream, int nVersion) throws IOException 
	{
		super.readInternal(iStream, nVersion);
        this.materialId = Utilities.readInt(iStream);
	}

	@Override
	protected void writeInternal(OutputStream oStream) throws IOException 
	{
		super.writeInternal(oStream);
        Utilities.writeInt(this.materialId, oStream);
	}
	
	public int getMaterialId() {
		return this.materialId;
	}
	
	private int materialId;
}
