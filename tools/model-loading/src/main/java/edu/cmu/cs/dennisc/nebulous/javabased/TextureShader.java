package edu.cmu.cs.dennisc.nebulous.javabased;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TextureShader extends Shader {
	
	@Override
    public int getClassID() 
    {
        return Element.TEXTURE_SHADER; 
    }
	
	@Override
	protected void readInternal(InputStream iStream, int nVersion) throws IOException 
	{
		super.readInternal(iStream, nVersion);
        this.textureId = Utilities.readInt(iStream);
	}

	@Override
	protected void writeInternal(OutputStream oStream) throws IOException 
	{
		super.writeInternal(oStream);
        Utilities.writeInt(this.textureId, oStream);
	}
	
	public int getTextureId() {
		return this.textureId;
	}
	
	private int textureId;

}
