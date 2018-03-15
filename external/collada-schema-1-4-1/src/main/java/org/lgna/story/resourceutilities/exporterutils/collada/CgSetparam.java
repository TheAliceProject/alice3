
package org.lgna.story.resourceutilities.exporterutils.collada;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 * 				Assigns a new value to a previously defined parameter.
 * 			
 * 
 * <p>Java class for cg_setparam complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cg_setparam">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;group ref="{http://www.collada.org/2005/11/COLLADASchema}cg_param_type"/>
 *         &lt;element name="usertype" type="{http://www.collada.org/2005/11/COLLADASchema}cg_setuser_type"/>
 *         &lt;element name="array" type="{http://www.collada.org/2005/11/COLLADASchema}cg_setarray_type"/>
 *         &lt;element name="connect_param" type="{http://www.collada.org/2005/11/COLLADASchema}cg_connect_param"/>
 *       &lt;/choice>
 *       &lt;attribute name="ref" use="required" type="{http://www.collada.org/2005/11/COLLADASchema}cg_identifier" />
 *       &lt;attribute name="program" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cg_setparam", namespace = "http://www.collada.org/2005/11/COLLADASchema", propOrder = {
    "bool",
    "bool1",
    "bool2",
    "bool3",
    "bool4",
    "bool1X1",
    "bool1X2",
    "bool1X3",
    "bool1X4",
    "bool2X1",
    "bool2X2",
    "bool2X3",
    "bool2X4",
    "bool3X1",
    "bool3X2",
    "bool3X3",
    "bool3X4",
    "bool4X1",
    "bool4X2",
    "bool4X3",
    "bool4X4",
    "_float",
    "float1",
    "float2",
    "float3",
    "float4",
    "float1X1",
    "float1X2",
    "float1X3",
    "float1X4",
    "float2X1",
    "float2X2",
    "float2X3",
    "float2X4",
    "float3X1",
    "float3X2",
    "float3X3",
    "float3X4",
    "float4X1",
    "float4X2",
    "float4X3",
    "float4X4",
    "_int",
    "int1",
    "int2",
    "int3",
    "int4",
    "int1X1",
    "int1X2",
    "int1X3",
    "int1X4",
    "int2X1",
    "int2X2",
    "int2X3",
    "int2X4",
    "int3X1",
    "int3X2",
    "int3X3",
    "int3X4",
    "int4X1",
    "int4X2",
    "int4X3",
    "int4X4",
    "half",
    "half1",
    "half2",
    "half3",
    "half4",
    "half1X1",
    "half1X2",
    "half1X3",
    "half1X4",
    "half2X1",
    "half2X2",
    "half2X3",
    "half2X4",
    "half3X1",
    "half3X2",
    "half3X3",
    "half3X4",
    "half4X1",
    "half4X2",
    "half4X3",
    "half4X4",
    "fixed",
    "fixed1",
    "fixed2",
    "fixed3",
    "fixed4",
    "fixed1X1",
    "fixed1X2",
    "fixed1X3",
    "fixed1X4",
    "fixed2X1",
    "fixed2X2",
    "fixed2X3",
    "fixed2X4",
    "fixed3X1",
    "fixed3X2",
    "fixed3X3",
    "fixed3X4",
    "fixed4X1",
    "fixed4X2",
    "fixed4X3",
    "fixed4X4",
    "surface",
    "sampler1D",
    "sampler2D",
    "sampler3D",
    "samplerRECT",
    "samplerCUBE",
    "samplerDEPTH",
    "string",
    "_enum",
    "usertype",
    "array",
    "connectParam"
})
public class CgSetparam {

    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected Boolean bool;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected Boolean bool1;
    @XmlList
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Boolean.class)
    protected List<Boolean> bool2;
    @XmlList
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Boolean.class)
    protected List<Boolean> bool3;
    @XmlList
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Boolean.class)
    protected List<Boolean> bool4;
    @XmlList
    @XmlElement(name = "bool1x1", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Boolean.class)
    protected List<Boolean> bool1X1;
    @XmlList
    @XmlElement(name = "bool1x2", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Boolean.class)
    protected List<Boolean> bool1X2;
    @XmlList
    @XmlElement(name = "bool1x3", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Boolean.class)
    protected List<Boolean> bool1X3;
    @XmlList
    @XmlElement(name = "bool1x4", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Boolean.class)
    protected List<Boolean> bool1X4;
    @XmlList
    @XmlElement(name = "bool2x1", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Boolean.class)
    protected List<Boolean> bool2X1;
    @XmlList
    @XmlElement(name = "bool2x2", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Boolean.class)
    protected List<Boolean> bool2X2;
    @XmlList
    @XmlElement(name = "bool2x3", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Boolean.class)
    protected List<Boolean> bool2X3;
    @XmlList
    @XmlElement(name = "bool2x4", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Boolean.class)
    protected List<Boolean> bool2X4;
    @XmlList
    @XmlElement(name = "bool3x1", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Boolean.class)
    protected List<Boolean> bool3X1;
    @XmlList
    @XmlElement(name = "bool3x2", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Boolean.class)
    protected List<Boolean> bool3X2;
    @XmlList
    @XmlElement(name = "bool3x3", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Boolean.class)
    protected List<Boolean> bool3X3;
    @XmlList
    @XmlElement(name = "bool3x4", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Boolean.class)
    protected List<Boolean> bool3X4;
    @XmlList
    @XmlElement(name = "bool4x1", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Boolean.class)
    protected List<Boolean> bool4X1;
    @XmlList
    @XmlElement(name = "bool4x2", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Boolean.class)
    protected List<Boolean> bool4X2;
    @XmlList
    @XmlElement(name = "bool4x3", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Boolean.class)
    protected List<Boolean> bool4X3;
    @XmlList
    @XmlElement(name = "bool4x4", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Boolean.class)
    protected List<Boolean> bool4X4;
    @XmlElement(name = "float", namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected Float _float;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected Float float1;
    @XmlList
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> float2;
    @XmlList
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> float3;
    @XmlList
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> float4;
    @XmlList
    @XmlElement(name = "float1x1", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> float1X1;
    @XmlList
    @XmlElement(name = "float1x2", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> float1X2;
    @XmlList
    @XmlElement(name = "float1x3", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> float1X3;
    @XmlList
    @XmlElement(name = "float1x4", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> float1X4;
    @XmlList
    @XmlElement(name = "float2x1", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> float2X1;
    @XmlList
    @XmlElement(name = "float2x2", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> float2X2;
    @XmlList
    @XmlElement(name = "float2x3", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> float2X3;
    @XmlList
    @XmlElement(name = "float2x4", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> float2X4;
    @XmlList
    @XmlElement(name = "float3x1", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> float3X1;
    @XmlList
    @XmlElement(name = "float3x2", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> float3X2;
    @XmlList
    @XmlElement(name = "float3x3", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> float3X3;
    @XmlList
    @XmlElement(name = "float3x4", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> float3X4;
    @XmlList
    @XmlElement(name = "float4x1", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> float4X1;
    @XmlList
    @XmlElement(name = "float4x2", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> float4X2;
    @XmlList
    @XmlElement(name = "float4x3", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> float4X3;
    @XmlList
    @XmlElement(name = "float4x4", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> float4X4;
    @XmlElement(name = "int", namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected Integer _int;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected Integer int1;
    @XmlList
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Integer.class)
    protected List<Integer> int2;
    @XmlList
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Integer.class)
    protected List<Integer> int3;
    @XmlList
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Integer.class)
    protected List<Integer> int4;
    @XmlList
    @XmlElement(name = "int1x1", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Integer.class)
    protected List<Integer> int1X1;
    @XmlList
    @XmlElement(name = "int1x2", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Integer.class)
    protected List<Integer> int1X2;
    @XmlList
    @XmlElement(name = "int1x3", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Integer.class)
    protected List<Integer> int1X3;
    @XmlList
    @XmlElement(name = "int1x4", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Integer.class)
    protected List<Integer> int1X4;
    @XmlList
    @XmlElement(name = "int2x1", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Integer.class)
    protected List<Integer> int2X1;
    @XmlList
    @XmlElement(name = "int2x2", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Integer.class)
    protected List<Integer> int2X2;
    @XmlList
    @XmlElement(name = "int2x3", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Integer.class)
    protected List<Integer> int2X3;
    @XmlList
    @XmlElement(name = "int2x4", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Integer.class)
    protected List<Integer> int2X4;
    @XmlList
    @XmlElement(name = "int3x1", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Integer.class)
    protected List<Integer> int3X1;
    @XmlList
    @XmlElement(name = "int3x2", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Integer.class)
    protected List<Integer> int3X2;
    @XmlList
    @XmlElement(name = "int3x3", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Integer.class)
    protected List<Integer> int3X3;
    @XmlList
    @XmlElement(name = "int3x4", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Integer.class)
    protected List<Integer> int3X4;
    @XmlList
    @XmlElement(name = "int4x1", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Integer.class)
    protected List<Integer> int4X1;
    @XmlList
    @XmlElement(name = "int4x2", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Integer.class)
    protected List<Integer> int4X2;
    @XmlList
    @XmlElement(name = "int4x3", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Integer.class)
    protected List<Integer> int4X3;
    @XmlList
    @XmlElement(name = "int4x4", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Integer.class)
    protected List<Integer> int4X4;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected Float half;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected Float half1;
    @XmlList
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> half2;
    @XmlList
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> half3;
    @XmlList
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> half4;
    @XmlList
    @XmlElement(name = "half1x1", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> half1X1;
    @XmlList
    @XmlElement(name = "half1x2", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> half1X2;
    @XmlList
    @XmlElement(name = "half1x3", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> half1X3;
    @XmlList
    @XmlElement(name = "half1x4", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> half1X4;
    @XmlList
    @XmlElement(name = "half2x1", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> half2X1;
    @XmlList
    @XmlElement(name = "half2x2", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> half2X2;
    @XmlList
    @XmlElement(name = "half2x3", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> half2X3;
    @XmlList
    @XmlElement(name = "half2x4", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> half2X4;
    @XmlList
    @XmlElement(name = "half3x1", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> half3X1;
    @XmlList
    @XmlElement(name = "half3x2", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> half3X2;
    @XmlList
    @XmlElement(name = "half3x3", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> half3X3;
    @XmlList
    @XmlElement(name = "half3x4", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> half3X4;
    @XmlList
    @XmlElement(name = "half4x1", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> half4X1;
    @XmlList
    @XmlElement(name = "half4x2", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> half4X2;
    @XmlList
    @XmlElement(name = "half4x3", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> half4X3;
    @XmlList
    @XmlElement(name = "half4x4", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> half4X4;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected Float fixed;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected Float fixed1;
    @XmlList
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> fixed2;
    @XmlList
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> fixed3;
    @XmlList
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> fixed4;
    @XmlList
    @XmlElement(name = "fixed1x1", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> fixed1X1;
    @XmlList
    @XmlElement(name = "fixed1x2", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> fixed1X2;
    @XmlList
    @XmlElement(name = "fixed1x3", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> fixed1X3;
    @XmlList
    @XmlElement(name = "fixed1x4", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> fixed1X4;
    @XmlList
    @XmlElement(name = "fixed2x1", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> fixed2X1;
    @XmlList
    @XmlElement(name = "fixed2x2", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> fixed2X2;
    @XmlList
    @XmlElement(name = "fixed2x3", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> fixed2X3;
    @XmlList
    @XmlElement(name = "fixed2x4", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> fixed2X4;
    @XmlList
    @XmlElement(name = "fixed3x1", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> fixed3X1;
    @XmlList
    @XmlElement(name = "fixed3x2", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> fixed3X2;
    @XmlList
    @XmlElement(name = "fixed3x3", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> fixed3X3;
    @XmlList
    @XmlElement(name = "fixed3x4", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> fixed3X4;
    @XmlList
    @XmlElement(name = "fixed4x1", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> fixed4X1;
    @XmlList
    @XmlElement(name = "fixed4x2", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> fixed4X2;
    @XmlList
    @XmlElement(name = "fixed4x3", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> fixed4X3;
    @XmlList
    @XmlElement(name = "fixed4x4", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
    protected List<Float> fixed4X4;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected CgSurfaceType surface;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected CgSampler1D sampler1D;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected CgSampler2D sampler2D;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected CgSampler3D sampler3D;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected CgSamplerRECT samplerRECT;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected CgSamplerCUBE samplerCUBE;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected CgSamplerDEPTH samplerDEPTH;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected String string;
    @XmlElement(name = "enum", namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected String _enum;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected CgSetuserType usertype;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected CgSetarrayType array;
    @XmlElement(name = "connect_param", namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected CgConnectParam connectParam;
    @XmlAttribute(name = "ref", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String ref;
    @XmlAttribute(name = "program")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String program;

    /**
     * Gets the value of the bool property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBool() {
        return bool;
    }

    /**
     * Sets the value of the bool property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBool(Boolean value) {
        this.bool = value;
    }

    /**
     * Gets the value of the bool1 property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBool1() {
        return bool1;
    }

    /**
     * Sets the value of the bool1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBool1(Boolean value) {
        this.bool1 = value;
    }

    /**
     * Gets the value of the bool2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bool2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBool2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Boolean }
     * 
     * 
     */
    public List<Boolean> getBool2() {
        if (bool2 == null) {
            bool2 = new ArrayList<Boolean>();
        }
        return this.bool2;
    }

    /**
     * Gets the value of the bool3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bool3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBool3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Boolean }
     * 
     * 
     */
    public List<Boolean> getBool3() {
        if (bool3 == null) {
            bool3 = new ArrayList<Boolean>();
        }
        return this.bool3;
    }

    /**
     * Gets the value of the bool4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bool4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBool4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Boolean }
     * 
     * 
     */
    public List<Boolean> getBool4() {
        if (bool4 == null) {
            bool4 = new ArrayList<Boolean>();
        }
        return this.bool4;
    }

    /**
     * Gets the value of the bool1X1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bool1X1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBool1X1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Boolean }
     * 
     * 
     */
    public List<Boolean> getBool1X1() {
        if (bool1X1 == null) {
            bool1X1 = new ArrayList<Boolean>();
        }
        return this.bool1X1;
    }

    /**
     * Gets the value of the bool1X2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bool1X2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBool1X2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Boolean }
     * 
     * 
     */
    public List<Boolean> getBool1X2() {
        if (bool1X2 == null) {
            bool1X2 = new ArrayList<Boolean>();
        }
        return this.bool1X2;
    }

    /**
     * Gets the value of the bool1X3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bool1X3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBool1X3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Boolean }
     * 
     * 
     */
    public List<Boolean> getBool1X3() {
        if (bool1X3 == null) {
            bool1X3 = new ArrayList<Boolean>();
        }
        return this.bool1X3;
    }

    /**
     * Gets the value of the bool1X4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bool1X4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBool1X4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Boolean }
     * 
     * 
     */
    public List<Boolean> getBool1X4() {
        if (bool1X4 == null) {
            bool1X4 = new ArrayList<Boolean>();
        }
        return this.bool1X4;
    }

    /**
     * Gets the value of the bool2X1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bool2X1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBool2X1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Boolean }
     * 
     * 
     */
    public List<Boolean> getBool2X1() {
        if (bool2X1 == null) {
            bool2X1 = new ArrayList<Boolean>();
        }
        return this.bool2X1;
    }

    /**
     * Gets the value of the bool2X2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bool2X2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBool2X2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Boolean }
     * 
     * 
     */
    public List<Boolean> getBool2X2() {
        if (bool2X2 == null) {
            bool2X2 = new ArrayList<Boolean>();
        }
        return this.bool2X2;
    }

    /**
     * Gets the value of the bool2X3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bool2X3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBool2X3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Boolean }
     * 
     * 
     */
    public List<Boolean> getBool2X3() {
        if (bool2X3 == null) {
            bool2X3 = new ArrayList<Boolean>();
        }
        return this.bool2X3;
    }

    /**
     * Gets the value of the bool2X4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bool2X4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBool2X4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Boolean }
     * 
     * 
     */
    public List<Boolean> getBool2X4() {
        if (bool2X4 == null) {
            bool2X4 = new ArrayList<Boolean>();
        }
        return this.bool2X4;
    }

    /**
     * Gets the value of the bool3X1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bool3X1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBool3X1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Boolean }
     * 
     * 
     */
    public List<Boolean> getBool3X1() {
        if (bool3X1 == null) {
            bool3X1 = new ArrayList<Boolean>();
        }
        return this.bool3X1;
    }

    /**
     * Gets the value of the bool3X2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bool3X2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBool3X2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Boolean }
     * 
     * 
     */
    public List<Boolean> getBool3X2() {
        if (bool3X2 == null) {
            bool3X2 = new ArrayList<Boolean>();
        }
        return this.bool3X2;
    }

    /**
     * Gets the value of the bool3X3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bool3X3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBool3X3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Boolean }
     * 
     * 
     */
    public List<Boolean> getBool3X3() {
        if (bool3X3 == null) {
            bool3X3 = new ArrayList<Boolean>();
        }
        return this.bool3X3;
    }

    /**
     * Gets the value of the bool3X4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bool3X4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBool3X4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Boolean }
     * 
     * 
     */
    public List<Boolean> getBool3X4() {
        if (bool3X4 == null) {
            bool3X4 = new ArrayList<Boolean>();
        }
        return this.bool3X4;
    }

    /**
     * Gets the value of the bool4X1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bool4X1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBool4X1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Boolean }
     * 
     * 
     */
    public List<Boolean> getBool4X1() {
        if (bool4X1 == null) {
            bool4X1 = new ArrayList<Boolean>();
        }
        return this.bool4X1;
    }

    /**
     * Gets the value of the bool4X2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bool4X2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBool4X2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Boolean }
     * 
     * 
     */
    public List<Boolean> getBool4X2() {
        if (bool4X2 == null) {
            bool4X2 = new ArrayList<Boolean>();
        }
        return this.bool4X2;
    }

    /**
     * Gets the value of the bool4X3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bool4X3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBool4X3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Boolean }
     * 
     * 
     */
    public List<Boolean> getBool4X3() {
        if (bool4X3 == null) {
            bool4X3 = new ArrayList<Boolean>();
        }
        return this.bool4X3;
    }

    /**
     * Gets the value of the bool4X4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bool4X4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBool4X4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Boolean }
     * 
     * 
     */
    public List<Boolean> getBool4X4() {
        if (bool4X4 == null) {
            bool4X4 = new ArrayList<Boolean>();
        }
        return this.bool4X4;
    }

    /**
     * Gets the value of the float property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getFloat() {
        return _float;
    }

    /**
     * Sets the value of the float property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setFloat(Float value) {
        this._float = value;
    }

    /**
     * Gets the value of the float1 property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getFloat1() {
        return float1;
    }

    /**
     * Sets the value of the float1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setFloat1(Float value) {
        this.float1 = value;
    }

    /**
     * Gets the value of the float2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFloat2() {
        if (float2 == null) {
            float2 = new ArrayList<Float>();
        }
        return this.float2;
    }

    /**
     * Gets the value of the float3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFloat3() {
        if (float3 == null) {
            float3 = new ArrayList<Float>();
        }
        return this.float3;
    }

    /**
     * Gets the value of the float4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFloat4() {
        if (float4 == null) {
            float4 = new ArrayList<Float>();
        }
        return this.float4;
    }

    /**
     * Gets the value of the float1X1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float1X1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat1X1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFloat1X1() {
        if (float1X1 == null) {
            float1X1 = new ArrayList<Float>();
        }
        return this.float1X1;
    }

    /**
     * Gets the value of the float1X2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float1X2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat1X2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFloat1X2() {
        if (float1X2 == null) {
            float1X2 = new ArrayList<Float>();
        }
        return this.float1X2;
    }

    /**
     * Gets the value of the float1X3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float1X3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat1X3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFloat1X3() {
        if (float1X3 == null) {
            float1X3 = new ArrayList<Float>();
        }
        return this.float1X3;
    }

    /**
     * Gets the value of the float1X4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float1X4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat1X4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFloat1X4() {
        if (float1X4 == null) {
            float1X4 = new ArrayList<Float>();
        }
        return this.float1X4;
    }

    /**
     * Gets the value of the float2X1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float2X1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat2X1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFloat2X1() {
        if (float2X1 == null) {
            float2X1 = new ArrayList<Float>();
        }
        return this.float2X1;
    }

    /**
     * Gets the value of the float2X2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float2X2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat2X2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFloat2X2() {
        if (float2X2 == null) {
            float2X2 = new ArrayList<Float>();
        }
        return this.float2X2;
    }

    /**
     * Gets the value of the float2X3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float2X3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat2X3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFloat2X3() {
        if (float2X3 == null) {
            float2X3 = new ArrayList<Float>();
        }
        return this.float2X3;
    }

    /**
     * Gets the value of the float2X4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float2X4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat2X4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFloat2X4() {
        if (float2X4 == null) {
            float2X4 = new ArrayList<Float>();
        }
        return this.float2X4;
    }

    /**
     * Gets the value of the float3X1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float3X1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat3X1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFloat3X1() {
        if (float3X1 == null) {
            float3X1 = new ArrayList<Float>();
        }
        return this.float3X1;
    }

    /**
     * Gets the value of the float3X2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float3X2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat3X2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFloat3X2() {
        if (float3X2 == null) {
            float3X2 = new ArrayList<Float>();
        }
        return this.float3X2;
    }

    /**
     * Gets the value of the float3X3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float3X3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat3X3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFloat3X3() {
        if (float3X3 == null) {
            float3X3 = new ArrayList<Float>();
        }
        return this.float3X3;
    }

    /**
     * Gets the value of the float3X4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float3X4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat3X4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFloat3X4() {
        if (float3X4 == null) {
            float3X4 = new ArrayList<Float>();
        }
        return this.float3X4;
    }

    /**
     * Gets the value of the float4X1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float4X1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat4X1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFloat4X1() {
        if (float4X1 == null) {
            float4X1 = new ArrayList<Float>();
        }
        return this.float4X1;
    }

    /**
     * Gets the value of the float4X2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float4X2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat4X2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFloat4X2() {
        if (float4X2 == null) {
            float4X2 = new ArrayList<Float>();
        }
        return this.float4X2;
    }

    /**
     * Gets the value of the float4X3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float4X3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat4X3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFloat4X3() {
        if (float4X3 == null) {
            float4X3 = new ArrayList<Float>();
        }
        return this.float4X3;
    }

    /**
     * Gets the value of the float4X4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float4X4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat4X4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFloat4X4() {
        if (float4X4 == null) {
            float4X4 = new ArrayList<Float>();
        }
        return this.float4X4;
    }

    /**
     * Gets the value of the int property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInt() {
        return _int;
    }

    /**
     * Sets the value of the int property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInt(Integer value) {
        this._int = value;
    }

    /**
     * Gets the value of the int1 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInt1() {
        return int1;
    }

    /**
     * Sets the value of the int1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInt1(Integer value) {
        this.int1 = value;
    }

    /**
     * Gets the value of the int2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the int2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInt2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getInt2() {
        if (int2 == null) {
            int2 = new ArrayList<Integer>();
        }
        return this.int2;
    }

    /**
     * Gets the value of the int3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the int3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInt3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getInt3() {
        if (int3 == null) {
            int3 = new ArrayList<Integer>();
        }
        return this.int3;
    }

    /**
     * Gets the value of the int4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the int4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInt4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getInt4() {
        if (int4 == null) {
            int4 = new ArrayList<Integer>();
        }
        return this.int4;
    }

    /**
     * Gets the value of the int1X1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the int1X1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInt1X1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getInt1X1() {
        if (int1X1 == null) {
            int1X1 = new ArrayList<Integer>();
        }
        return this.int1X1;
    }

    /**
     * Gets the value of the int1X2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the int1X2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInt1X2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getInt1X2() {
        if (int1X2 == null) {
            int1X2 = new ArrayList<Integer>();
        }
        return this.int1X2;
    }

    /**
     * Gets the value of the int1X3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the int1X3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInt1X3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getInt1X3() {
        if (int1X3 == null) {
            int1X3 = new ArrayList<Integer>();
        }
        return this.int1X3;
    }

    /**
     * Gets the value of the int1X4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the int1X4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInt1X4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getInt1X4() {
        if (int1X4 == null) {
            int1X4 = new ArrayList<Integer>();
        }
        return this.int1X4;
    }

    /**
     * Gets the value of the int2X1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the int2X1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInt2X1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getInt2X1() {
        if (int2X1 == null) {
            int2X1 = new ArrayList<Integer>();
        }
        return this.int2X1;
    }

    /**
     * Gets the value of the int2X2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the int2X2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInt2X2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getInt2X2() {
        if (int2X2 == null) {
            int2X2 = new ArrayList<Integer>();
        }
        return this.int2X2;
    }

    /**
     * Gets the value of the int2X3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the int2X3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInt2X3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getInt2X3() {
        if (int2X3 == null) {
            int2X3 = new ArrayList<Integer>();
        }
        return this.int2X3;
    }

    /**
     * Gets the value of the int2X4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the int2X4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInt2X4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getInt2X4() {
        if (int2X4 == null) {
            int2X4 = new ArrayList<Integer>();
        }
        return this.int2X4;
    }

    /**
     * Gets the value of the int3X1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the int3X1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInt3X1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getInt3X1() {
        if (int3X1 == null) {
            int3X1 = new ArrayList<Integer>();
        }
        return this.int3X1;
    }

    /**
     * Gets the value of the int3X2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the int3X2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInt3X2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getInt3X2() {
        if (int3X2 == null) {
            int3X2 = new ArrayList<Integer>();
        }
        return this.int3X2;
    }

    /**
     * Gets the value of the int3X3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the int3X3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInt3X3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getInt3X3() {
        if (int3X3 == null) {
            int3X3 = new ArrayList<Integer>();
        }
        return this.int3X3;
    }

    /**
     * Gets the value of the int3X4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the int3X4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInt3X4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getInt3X4() {
        if (int3X4 == null) {
            int3X4 = new ArrayList<Integer>();
        }
        return this.int3X4;
    }

    /**
     * Gets the value of the int4X1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the int4X1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInt4X1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getInt4X1() {
        if (int4X1 == null) {
            int4X1 = new ArrayList<Integer>();
        }
        return this.int4X1;
    }

    /**
     * Gets the value of the int4X2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the int4X2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInt4X2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getInt4X2() {
        if (int4X2 == null) {
            int4X2 = new ArrayList<Integer>();
        }
        return this.int4X2;
    }

    /**
     * Gets the value of the int4X3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the int4X3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInt4X3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getInt4X3() {
        if (int4X3 == null) {
            int4X3 = new ArrayList<Integer>();
        }
        return this.int4X3;
    }

    /**
     * Gets the value of the int4X4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the int4X4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInt4X4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getInt4X4() {
        if (int4X4 == null) {
            int4X4 = new ArrayList<Integer>();
        }
        return this.int4X4;
    }

    /**
     * Gets the value of the half property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getHalf() {
        return half;
    }

    /**
     * Sets the value of the half property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setHalf(Float value) {
        this.half = value;
    }

    /**
     * Gets the value of the half1 property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getHalf1() {
        return half1;
    }

    /**
     * Sets the value of the half1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setHalf1(Float value) {
        this.half1 = value;
    }

    /**
     * Gets the value of the half2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the half2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHalf2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getHalf2() {
        if (half2 == null) {
            half2 = new ArrayList<Float>();
        }
        return this.half2;
    }

    /**
     * Gets the value of the half3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the half3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHalf3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getHalf3() {
        if (half3 == null) {
            half3 = new ArrayList<Float>();
        }
        return this.half3;
    }

    /**
     * Gets the value of the half4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the half4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHalf4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getHalf4() {
        if (half4 == null) {
            half4 = new ArrayList<Float>();
        }
        return this.half4;
    }

    /**
     * Gets the value of the half1X1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the half1X1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHalf1X1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getHalf1X1() {
        if (half1X1 == null) {
            half1X1 = new ArrayList<Float>();
        }
        return this.half1X1;
    }

    /**
     * Gets the value of the half1X2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the half1X2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHalf1X2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getHalf1X2() {
        if (half1X2 == null) {
            half1X2 = new ArrayList<Float>();
        }
        return this.half1X2;
    }

    /**
     * Gets the value of the half1X3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the half1X3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHalf1X3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getHalf1X3() {
        if (half1X3 == null) {
            half1X3 = new ArrayList<Float>();
        }
        return this.half1X3;
    }

    /**
     * Gets the value of the half1X4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the half1X4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHalf1X4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getHalf1X4() {
        if (half1X4 == null) {
            half1X4 = new ArrayList<Float>();
        }
        return this.half1X4;
    }

    /**
     * Gets the value of the half2X1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the half2X1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHalf2X1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getHalf2X1() {
        if (half2X1 == null) {
            half2X1 = new ArrayList<Float>();
        }
        return this.half2X1;
    }

    /**
     * Gets the value of the half2X2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the half2X2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHalf2X2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getHalf2X2() {
        if (half2X2 == null) {
            half2X2 = new ArrayList<Float>();
        }
        return this.half2X2;
    }

    /**
     * Gets the value of the half2X3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the half2X3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHalf2X3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getHalf2X3() {
        if (half2X3 == null) {
            half2X3 = new ArrayList<Float>();
        }
        return this.half2X3;
    }

    /**
     * Gets the value of the half2X4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the half2X4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHalf2X4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getHalf2X4() {
        if (half2X4 == null) {
            half2X4 = new ArrayList<Float>();
        }
        return this.half2X4;
    }

    /**
     * Gets the value of the half3X1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the half3X1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHalf3X1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getHalf3X1() {
        if (half3X1 == null) {
            half3X1 = new ArrayList<Float>();
        }
        return this.half3X1;
    }

    /**
     * Gets the value of the half3X2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the half3X2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHalf3X2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getHalf3X2() {
        if (half3X2 == null) {
            half3X2 = new ArrayList<Float>();
        }
        return this.half3X2;
    }

    /**
     * Gets the value of the half3X3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the half3X3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHalf3X3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getHalf3X3() {
        if (half3X3 == null) {
            half3X3 = new ArrayList<Float>();
        }
        return this.half3X3;
    }

    /**
     * Gets the value of the half3X4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the half3X4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHalf3X4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getHalf3X4() {
        if (half3X4 == null) {
            half3X4 = new ArrayList<Float>();
        }
        return this.half3X4;
    }

    /**
     * Gets the value of the half4X1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the half4X1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHalf4X1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getHalf4X1() {
        if (half4X1 == null) {
            half4X1 = new ArrayList<Float>();
        }
        return this.half4X1;
    }

    /**
     * Gets the value of the half4X2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the half4X2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHalf4X2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getHalf4X2() {
        if (half4X2 == null) {
            half4X2 = new ArrayList<Float>();
        }
        return this.half4X2;
    }

    /**
     * Gets the value of the half4X3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the half4X3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHalf4X3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getHalf4X3() {
        if (half4X3 == null) {
            half4X3 = new ArrayList<Float>();
        }
        return this.half4X3;
    }

    /**
     * Gets the value of the half4X4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the half4X4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHalf4X4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getHalf4X4() {
        if (half4X4 == null) {
            half4X4 = new ArrayList<Float>();
        }
        return this.half4X4;
    }

    /**
     * Gets the value of the fixed property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getFixed() {
        return fixed;
    }

    /**
     * Sets the value of the fixed property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setFixed(Float value) {
        this.fixed = value;
    }

    /**
     * Gets the value of the fixed1 property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getFixed1() {
        return fixed1;
    }

    /**
     * Sets the value of the fixed1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setFixed1(Float value) {
        this.fixed1 = value;
    }

    /**
     * Gets the value of the fixed2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fixed2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFixed2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFixed2() {
        if (fixed2 == null) {
            fixed2 = new ArrayList<Float>();
        }
        return this.fixed2;
    }

    /**
     * Gets the value of the fixed3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fixed3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFixed3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFixed3() {
        if (fixed3 == null) {
            fixed3 = new ArrayList<Float>();
        }
        return this.fixed3;
    }

    /**
     * Gets the value of the fixed4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fixed4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFixed4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFixed4() {
        if (fixed4 == null) {
            fixed4 = new ArrayList<Float>();
        }
        return this.fixed4;
    }

    /**
     * Gets the value of the fixed1X1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fixed1X1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFixed1X1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFixed1X1() {
        if (fixed1X1 == null) {
            fixed1X1 = new ArrayList<Float>();
        }
        return this.fixed1X1;
    }

    /**
     * Gets the value of the fixed1X2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fixed1X2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFixed1X2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFixed1X2() {
        if (fixed1X2 == null) {
            fixed1X2 = new ArrayList<Float>();
        }
        return this.fixed1X2;
    }

    /**
     * Gets the value of the fixed1X3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fixed1X3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFixed1X3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFixed1X3() {
        if (fixed1X3 == null) {
            fixed1X3 = new ArrayList<Float>();
        }
        return this.fixed1X3;
    }

    /**
     * Gets the value of the fixed1X4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fixed1X4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFixed1X4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFixed1X4() {
        if (fixed1X4 == null) {
            fixed1X4 = new ArrayList<Float>();
        }
        return this.fixed1X4;
    }

    /**
     * Gets the value of the fixed2X1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fixed2X1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFixed2X1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFixed2X1() {
        if (fixed2X1 == null) {
            fixed2X1 = new ArrayList<Float>();
        }
        return this.fixed2X1;
    }

    /**
     * Gets the value of the fixed2X2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fixed2X2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFixed2X2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFixed2X2() {
        if (fixed2X2 == null) {
            fixed2X2 = new ArrayList<Float>();
        }
        return this.fixed2X2;
    }

    /**
     * Gets the value of the fixed2X3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fixed2X3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFixed2X3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFixed2X3() {
        if (fixed2X3 == null) {
            fixed2X3 = new ArrayList<Float>();
        }
        return this.fixed2X3;
    }

    /**
     * Gets the value of the fixed2X4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fixed2X4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFixed2X4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFixed2X4() {
        if (fixed2X4 == null) {
            fixed2X4 = new ArrayList<Float>();
        }
        return this.fixed2X4;
    }

    /**
     * Gets the value of the fixed3X1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fixed3X1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFixed3X1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFixed3X1() {
        if (fixed3X1 == null) {
            fixed3X1 = new ArrayList<Float>();
        }
        return this.fixed3X1;
    }

    /**
     * Gets the value of the fixed3X2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fixed3X2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFixed3X2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFixed3X2() {
        if (fixed3X2 == null) {
            fixed3X2 = new ArrayList<Float>();
        }
        return this.fixed3X2;
    }

    /**
     * Gets the value of the fixed3X3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fixed3X3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFixed3X3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFixed3X3() {
        if (fixed3X3 == null) {
            fixed3X3 = new ArrayList<Float>();
        }
        return this.fixed3X3;
    }

    /**
     * Gets the value of the fixed3X4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fixed3X4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFixed3X4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFixed3X4() {
        if (fixed3X4 == null) {
            fixed3X4 = new ArrayList<Float>();
        }
        return this.fixed3X4;
    }

    /**
     * Gets the value of the fixed4X1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fixed4X1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFixed4X1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFixed4X1() {
        if (fixed4X1 == null) {
            fixed4X1 = new ArrayList<Float>();
        }
        return this.fixed4X1;
    }

    /**
     * Gets the value of the fixed4X2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fixed4X2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFixed4X2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFixed4X2() {
        if (fixed4X2 == null) {
            fixed4X2 = new ArrayList<Float>();
        }
        return this.fixed4X2;
    }

    /**
     * Gets the value of the fixed4X3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fixed4X3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFixed4X3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFixed4X3() {
        if (fixed4X3 == null) {
            fixed4X3 = new ArrayList<Float>();
        }
        return this.fixed4X3;
    }

    /**
     * Gets the value of the fixed4X4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fixed4X4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFixed4X4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     * 
     * 
     */
    public List<Float> getFixed4X4() {
        if (fixed4X4 == null) {
            fixed4X4 = new ArrayList<Float>();
        }
        return this.fixed4X4;
    }

    /**
     * Gets the value of the surface property.
     * 
     * @return
     *     possible object is
     *     {@link CgSurfaceType }
     *     
     */
    public CgSurfaceType getSurface() {
        return surface;
    }

    /**
     * Sets the value of the surface property.
     * 
     * @param value
     *     allowed object is
     *     {@link CgSurfaceType }
     *     
     */
    public void setSurface(CgSurfaceType value) {
        this.surface = value;
    }

    /**
     * Gets the value of the sampler1D property.
     * 
     * @return
     *     possible object is
     *     {@link CgSampler1D }
     *     
     */
    public CgSampler1D getSampler1D() {
        return sampler1D;
    }

    /**
     * Sets the value of the sampler1D property.
     * 
     * @param value
     *     allowed object is
     *     {@link CgSampler1D }
     *     
     */
    public void setSampler1D(CgSampler1D value) {
        this.sampler1D = value;
    }

    /**
     * Gets the value of the sampler2D property.
     * 
     * @return
     *     possible object is
     *     {@link CgSampler2D }
     *     
     */
    public CgSampler2D getSampler2D() {
        return sampler2D;
    }

    /**
     * Sets the value of the sampler2D property.
     * 
     * @param value
     *     allowed object is
     *     {@link CgSampler2D }
     *     
     */
    public void setSampler2D(CgSampler2D value) {
        this.sampler2D = value;
    }

    /**
     * Gets the value of the sampler3D property.
     * 
     * @return
     *     possible object is
     *     {@link CgSampler3D }
     *     
     */
    public CgSampler3D getSampler3D() {
        return sampler3D;
    }

    /**
     * Sets the value of the sampler3D property.
     * 
     * @param value
     *     allowed object is
     *     {@link CgSampler3D }
     *     
     */
    public void setSampler3D(CgSampler3D value) {
        this.sampler3D = value;
    }

    /**
     * Gets the value of the samplerRECT property.
     * 
     * @return
     *     possible object is
     *     {@link CgSamplerRECT }
     *     
     */
    public CgSamplerRECT getSamplerRECT() {
        return samplerRECT;
    }

    /**
     * Sets the value of the samplerRECT property.
     * 
     * @param value
     *     allowed object is
     *     {@link CgSamplerRECT }
     *     
     */
    public void setSamplerRECT(CgSamplerRECT value) {
        this.samplerRECT = value;
    }

    /**
     * Gets the value of the samplerCUBE property.
     * 
     * @return
     *     possible object is
     *     {@link CgSamplerCUBE }
     *     
     */
    public CgSamplerCUBE getSamplerCUBE() {
        return samplerCUBE;
    }

    /**
     * Sets the value of the samplerCUBE property.
     * 
     * @param value
     *     allowed object is
     *     {@link CgSamplerCUBE }
     *     
     */
    public void setSamplerCUBE(CgSamplerCUBE value) {
        this.samplerCUBE = value;
    }

    /**
     * Gets the value of the samplerDEPTH property.
     * 
     * @return
     *     possible object is
     *     {@link CgSamplerDEPTH }
     *     
     */
    public CgSamplerDEPTH getSamplerDEPTH() {
        return samplerDEPTH;
    }

    /**
     * Sets the value of the samplerDEPTH property.
     * 
     * @param value
     *     allowed object is
     *     {@link CgSamplerDEPTH }
     *     
     */
    public void setSamplerDEPTH(CgSamplerDEPTH value) {
        this.samplerDEPTH = value;
    }

    /**
     * Gets the value of the string property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getString() {
        return string;
    }

    /**
     * Sets the value of the string property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setString(String value) {
        this.string = value;
    }

    /**
     * Gets the value of the enum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnum() {
        return _enum;
    }

    /**
     * Sets the value of the enum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnum(String value) {
        this._enum = value;
    }

    /**
     * Gets the value of the usertype property.
     * 
     * @return
     *     possible object is
     *     {@link CgSetuserType }
     *     
     */
    public CgSetuserType getUsertype() {
        return usertype;
    }

    /**
     * Sets the value of the usertype property.
     * 
     * @param value
     *     allowed object is
     *     {@link CgSetuserType }
     *     
     */
    public void setUsertype(CgSetuserType value) {
        this.usertype = value;
    }

    /**
     * Gets the value of the array property.
     * 
     * @return
     *     possible object is
     *     {@link CgSetarrayType }
     *     
     */
    public CgSetarrayType getArray() {
        return array;
    }

    /**
     * Sets the value of the array property.
     * 
     * @param value
     *     allowed object is
     *     {@link CgSetarrayType }
     *     
     */
    public void setArray(CgSetarrayType value) {
        this.array = value;
    }

    /**
     * Gets the value of the connectParam property.
     * 
     * @return
     *     possible object is
     *     {@link CgConnectParam }
     *     
     */
    public CgConnectParam getConnectParam() {
        return connectParam;
    }

    /**
     * Sets the value of the connectParam property.
     * 
     * @param value
     *     allowed object is
     *     {@link CgConnectParam }
     *     
     */
    public void setConnectParam(CgConnectParam value) {
        this.connectParam = value;
    }

    /**
     * Gets the value of the ref property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRef() {
        return ref;
    }

    /**
     * Sets the value of the ref property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRef(String value) {
        this.ref = value;
    }

    /**
     * Gets the value of the program property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProgram() {
        return program;
    }

    /**
     * Sets the value of the program property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProgram(String value) {
        this.program = value;
    }

}
