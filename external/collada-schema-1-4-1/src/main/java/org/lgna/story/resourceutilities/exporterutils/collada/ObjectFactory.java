
package org.lgna.story.resourceutilities.exporterutils.collada;

import java.math.BigInteger;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.lgna.story.resourceutilities.exporterutils.collada package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Translate_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "translate");
    private final static QName _Scale_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "scale");
    private final static QName _ProfileGLSL_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "profile_GLSL");
    private final static QName _InstancePhysicsMaterial_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "instance_physics_material");
    private final static QName _ProfileGLES_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "profile_GLES");
    private final static QName _P_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "p");
    private final static QName _FxProfileAbstract_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "fx_profile_abstract");
    private final static QName _InstanceForceField_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "instance_force_field");
    private final static QName _InstanceCamera_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "instance_camera");
    private final static QName _ProfileCG_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "profile_CG");
    private final static QName _GlHookAbstract_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "gl_hook_abstract");
    private final static QName _ProfileCOMMON_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "profile_COMMON");
    private final static QName _InstanceNode_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "instance_node");
    private final static QName _InstanceLight_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "instance_light");
    private final static QName _CgSetarrayTypeInt3X1_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "int3x1");
    private final static QName _CgSetarrayTypeInt3X2_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "int3x2");
    private final static QName _CgSetarrayTypeInt3X3_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "int3x3");
    private final static QName _CgSetarrayTypeUsertype_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "usertype");
    private final static QName _CgSetarrayTypeHalf1X4_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "half1x4");
    private final static QName _CgSetarrayTypeHalf1X3_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "half1x3");
    private final static QName _CgSetarrayTypeHalf1X2_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "half1x2");
    private final static QName _CgSetarrayTypeHalf2_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "half2");
    private final static QName _CgSetarrayTypeHalf1X1_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "half1x1");
    private final static QName _CgSetarrayTypeHalf1_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "half1");
    private final static QName _CgSetarrayTypeFixed_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "fixed");
    private final static QName _CgSetarrayTypeHalf4_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "half4");
    private final static QName _CgSetarrayTypeHalf3_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "half3");
    private final static QName _CgSetarrayTypeFloat_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "float");
    private final static QName _CgSetarrayTypeInt3X4_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "int3x4");
    private final static QName _CgSetarrayTypeFixed3X2_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "fixed3x2");
    private final static QName _CgSetarrayTypeFixed3X1_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "fixed3x1");
    private final static QName _CgSetarrayTypeFixed3X4_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "fixed3x4");
    private final static QName _CgSetarrayTypeFixed3X3_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "fixed3x3");
    private final static QName _CgSetarrayTypeFloat3X4_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "float3x4");
    private final static QName _CgSetarrayTypeFloat3X3_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "float3x3");
    private final static QName _CgSetarrayTypeBool2X4_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "bool2x4");
    private final static QName _CgSetarrayTypeFloat3X2_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "float3x2");
    private final static QName _CgSetarrayTypeFloat3X1_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "float3x1");
    private final static QName _CgSetarrayTypeBool2X2_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "bool2x2");
    private final static QName _CgSetarrayTypeBool2X3_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "bool2x3");
    private final static QName _CgSetarrayTypeBool2X1_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "bool2x1");
    private final static QName _CgSetarrayTypeInt_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "int");
    private final static QName _CgSetarrayTypeInt4X1_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "int4x1");
    private final static QName _CgSetarrayTypeInt4X2_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "int4x2");
    private final static QName _CgSetarrayTypeHalf4X2_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "half4x2");
    private final static QName _CgSetarrayTypeHalf4X1_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "half4x1");
    private final static QName _CgSetarrayTypeHalf4X4_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "half4x4");
    private final static QName _CgSetarrayTypeSamplerRECT_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "samplerRECT");
    private final static QName _CgSetarrayTypeHalf4X3_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "half4x3");
    private final static QName _CgSetarrayTypeBool_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "bool");
    private final static QName _CgSetarrayTypeString_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "string");
    private final static QName _CgSetarrayTypeArray_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "array");
    private final static QName _CgSetarrayTypeInt4X3_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "int4x3");
    private final static QName _CgSetarrayTypeFixed2X3_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "fixed2x3");
    private final static QName _CgSetarrayTypeInt4X4_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "int4x4");
    private final static QName _CgSetarrayTypeFixed2X2_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "fixed2x2");
    private final static QName _CgSetarrayTypeFixed2X4_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "fixed2x4");
    private final static QName _CgSetarrayTypeFloat4X3_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "float4x3");
    private final static QName _CgSetarrayTypeFloat4X2_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "float4x2");
    private final static QName _CgSetarrayTypeBool3X3_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "bool3x3");
    private final static QName _CgSetarrayTypeFloat4X1_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "float4x1");
    private final static QName _CgSetarrayTypeFixed2X1_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "fixed2x1");
    private final static QName _CgSetarrayTypeBool3X4_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "bool3x4");
    private final static QName _CgSetarrayTypeBool3X1_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "bool3x1");
    private final static QName _CgSetarrayTypeBool3X2_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "bool3x2");
    private final static QName _CgSetarrayTypeFloat4X4_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "float4x4");
    private final static QName _CgSetarrayTypeSurface_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "surface");
    private final static QName _CgSetarrayTypeInt1X2_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "int1x2");
    private final static QName _CgSetarrayTypeInt1X3_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "int1x3");
    private final static QName _CgSetarrayTypeInt1X4_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "int1x4");
    private final static QName _CgSetarrayTypeSampler3D_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "sampler3D");
    private final static QName _CgSetarrayTypeEnum_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "enum");
    private final static QName _CgSetarrayTypeSamplerCUBE_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "samplerCUBE");
    private final static QName _CgSetarrayTypeInt1X1_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "int1x1");
    private final static QName _CgSetarrayTypeHalf3X3_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "half3x3");
    private final static QName _CgSetarrayTypeHalf3X2_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "half3x2");
    private final static QName _CgSetarrayTypeHalf3X1_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "half3x1");
    private final static QName _CgSetarrayTypeFixed2_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "fixed2");
    private final static QName _CgSetarrayTypeFixed3_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "fixed3");
    private final static QName _CgSetarrayTypeHalf3X4_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "half3x4");
    private final static QName _CgSetarrayTypeFixed1_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "fixed1");
    private final static QName _CgSetarrayTypeFloat1X2_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "float1x2");
    private final static QName _CgSetarrayTypeFloat1X1_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "float1x1");
    private final static QName _CgSetarrayTypeHalf_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "half");
    private final static QName _CgSetarrayTypeSampler2D_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "sampler2D");
    private final static QName _CgSetarrayTypeInt4_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "int4");
    private final static QName _CgSetarrayTypeInt3_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "int3");
    private final static QName _CgSetarrayTypeBool3_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "bool3");
    private final static QName _CgSetarrayTypeFixed1X4_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "fixed1x4");
    private final static QName _CgSetarrayTypeBool2_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "bool2");
    private final static QName _CgSetarrayTypeFixed1X3_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "fixed1x3");
    private final static QName _CgSetarrayTypeInt2_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "int2");
    private final static QName _CgSetarrayTypeBool4_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "bool4");
    private final static QName _CgSetarrayTypeInt1_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "int1");
    private final static QName _CgSetarrayTypeBool4X4_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "bool4x4");
    private final static QName _CgSetarrayTypeBool1_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "bool1");
    private final static QName _CgSetarrayTypeBool4X2_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "bool4x2");
    private final static QName _CgSetarrayTypeFloat1X4_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "float1x4");
    private final static QName _CgSetarrayTypeFixed1X2_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "fixed1x2");
    private final static QName _CgSetarrayTypeBool4X3_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "bool4x3");
    private final static QName _CgSetarrayTypeFloat1X3_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "float1x3");
    private final static QName _CgSetarrayTypeFixed1X1_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "fixed1x1");
    private final static QName _CgSetarrayTypeBool4X1_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "bool4x1");
    private final static QName _CgSetarrayTypeFloat1_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "float1");
    private final static QName _CgSetarrayTypeFloat2_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "float2");
    private final static QName _CgSetarrayTypeFloat3_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "float3");
    private final static QName _CgSetarrayTypeFloat4_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "float4");
    private final static QName _CgSetarrayTypeInt2X1_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "int2x1");
    private final static QName _CgSetarrayTypeSampler1D_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "sampler1D");
    private final static QName _CgSetarrayTypeInt2X2_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "int2x2");
    private final static QName _CgSetarrayTypeInt2X3_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "int2x3");
    private final static QName _CgSetarrayTypeInt2X4_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "int2x4");
    private final static QName _CgSetarrayTypeHalf2X4_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "half2x4");
    private final static QName _CgSetarrayTypeHalf2X3_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "half2x3");
    private final static QName _CgSetarrayTypeHalf2X2_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "half2x2");
    private final static QName _CgSetarrayTypeHalf2X1_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "half2x1");
    private final static QName _CgSetarrayTypeSamplerDEPTH_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "samplerDEPTH");
    private final static QName _CgSetarrayTypeFloat2X1_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "float2x1");
    private final static QName _CgSetarrayTypeFixed4_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "fixed4");
    private final static QName _CgSetarrayTypeFixed4X4_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "fixed4x4");
    private final static QName _CgSetarrayTypeFixed4X1_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "fixed4x1");
    private final static QName _CgSetarrayTypeFixed4X3_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "fixed4x3");
    private final static QName _CgSetarrayTypeFixed4X2_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "fixed4x2");
    private final static QName _CgSetarrayTypeFloat2X4_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "float2x4");
    private final static QName _CgSetarrayTypeFloat2X3_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "float2x3");
    private final static QName _CgSetarrayTypeFloat2X2_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "float2x2");
    private final static QName _CgSetarrayTypeBool1X3_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "bool1x3");
    private final static QName _CgSetarrayTypeBool1X4_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "bool1x4");
    private final static QName _CgSetarrayTypeBool1X1_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "bool1x1");
    private final static QName _CgSetarrayTypeBool1X2_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "bool1x2");
    private final static QName _PolygonsPh_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "ph");
    private final static QName _CgSetuserTypeConnectParam_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "connect_param");
    private final static QName _CameraOpticsTechniqueCommonOrthographicZfar_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "zfar");
    private final static QName _CameraOpticsTechniqueCommonOrthographicAspectRatio_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "aspect_ratio");
    private final static QName _CameraOpticsTechniqueCommonOrthographicYmag_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "ymag");
    private final static QName _CameraOpticsTechniqueCommonOrthographicXmag_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "xmag");
    private final static QName _CameraOpticsTechniqueCommonOrthographicZnear_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "znear");
    private final static QName _CameraOpticsTechniqueCommonPerspectiveYfov_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "yfov");
    private final static QName _CameraOpticsTechniqueCommonPerspectiveXfov_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "xfov");
    private final static QName _PolygonsPhH_QNAME = new QName("http://www.collada.org/2005/11/COLLADASchema", "h");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.lgna.story.resourceutilities.exporterutils.collada
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BindMaterial }
     * 
     */
    public BindMaterial createBindMaterial() {
        return new BindMaterial();
    }

    /**
     * Create an instance of {@link Asset }
     * 
     */
    public Asset createAsset() {
        return new Asset();
    }

    /**
     * Create an instance of {@link PhysicsMaterial }
     * 
     */
    public PhysicsMaterial createPhysicsMaterial() {
        return new PhysicsMaterial();
    }

    /**
     * Create an instance of {@link Polygons }
     * 
     */
    public Polygons createPolygons() {
        return new Polygons();
    }

    /**
     * Create an instance of {@link Source }
     * 
     */
    public Source createSource() {
        return new Source();
    }

    /**
     * Create an instance of {@link Skin }
     * 
     */
    public Skin createSkin() {
        return new Skin();
    }

    /**
     * Create an instance of {@link Morph }
     * 
     */
    public Morph createMorph() {
        return new Morph();
    }

    /**
     * Create an instance of {@link InstanceMaterial }
     * 
     */
    public InstanceMaterial createInstanceMaterial() {
        return new InstanceMaterial();
    }

    /**
     * Create an instance of {@link Camera }
     * 
     */
    public Camera createCamera() {
        return new Camera();
    }

    /**
     * Create an instance of {@link RigidConstraint }
     * 
     */
    public RigidConstraint createRigidConstraint() {
        return new RigidConstraint();
    }

    /**
     * Create an instance of {@link InstanceRigidBody }
     * 
     */
    public InstanceRigidBody createInstanceRigidBody() {
        return new InstanceRigidBody();
    }

    /**
     * Create an instance of {@link PhysicsScene }
     * 
     */
    public PhysicsScene createPhysicsScene() {
        return new PhysicsScene();
    }

    /**
     * Create an instance of {@link Light }
     * 
     */
    public Light createLight() {
        return new Light();
    }

    /**
     * Create an instance of {@link Spline }
     * 
     */
    public Spline createSpline() {
        return new Spline();
    }

    /**
     * Create an instance of {@link InstanceEffect }
     * 
     */
    public InstanceEffect createInstanceEffect() {
        return new InstanceEffect();
    }

    /**
     * Create an instance of {@link RigidBody }
     * 
     */
    public RigidBody createRigidBody() {
        return new RigidBody();
    }

    /**
     * Create an instance of {@link VisualScene }
     * 
     */
    public VisualScene createVisualScene() {
        return new VisualScene();
    }

    /**
     * Create an instance of {@link COLLADA }
     * 
     */
    public COLLADA createCOLLADA() {
        return new COLLADA();
    }

    /**
     * Create an instance of {@link GlesTextureUnit }
     * 
     */
    public GlesTextureUnit createGlesTextureUnit() {
        return new GlesTextureUnit();
    }

    /**
     * Create an instance of {@link FxSurfaceInitCubeCommon }
     * 
     */
    public FxSurfaceInitCubeCommon createFxSurfaceInitCubeCommon() {
        return new FxSurfaceInitCubeCommon();
    }

    /**
     * Create an instance of {@link FxSurfaceInitVolumeCommon }
     * 
     */
    public FxSurfaceInitVolumeCommon createFxSurfaceInitVolumeCommon() {
        return new FxSurfaceInitVolumeCommon();
    }

    /**
     * Create an instance of {@link CommonFloatOrParamType }
     * 
     */
    public CommonFloatOrParamType createCommonFloatOrParamType() {
        return new CommonFloatOrParamType();
    }

    /**
     * Create an instance of {@link CgSurfaceType }
     * 
     */
    public CgSurfaceType createCgSurfaceType() {
        return new CgSurfaceType();
    }

    /**
     * Create an instance of {@link CgSurfaceType.Generator }
     * 
     */
    public CgSurfaceType.Generator createCgSurfaceTypeGenerator() {
        return new CgSurfaceType.Generator();
    }

    /**
     * Create an instance of {@link CommonColorOrTextureType }
     * 
     */
    public CommonColorOrTextureType createCommonColorOrTextureType() {
        return new CommonColorOrTextureType();
    }

    /**
     * Create an instance of {@link FxSurfaceInitPlanarCommon }
     * 
     */
    public FxSurfaceInitPlanarCommon createFxSurfaceInitPlanarCommon() {
        return new FxSurfaceInitPlanarCommon();
    }

    /**
     * Create an instance of {@link GlslSurfaceType }
     * 
     */
    public GlslSurfaceType createGlslSurfaceType() {
        return new GlslSurfaceType();
    }

    /**
     * Create an instance of {@link GlslSurfaceType.Generator }
     * 
     */
    public GlslSurfaceType.Generator createGlslSurfaceTypeGenerator() {
        return new GlslSurfaceType.Generator();
    }

    /**
     * Create an instance of {@link ProfileCG }
     * 
     */
    public ProfileCG createProfileCG() {
        return new ProfileCG();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique }
     * 
     */
    public ProfileCG.Technique createProfileCGTechnique() {
        return new ProfileCG.Technique();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass }
     * 
     */
    public ProfileCG.Technique.Pass createProfileCGTechniquePass() {
        return new ProfileCG.Technique.Pass();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.Shader }
     * 
     */
    public ProfileCG.Technique.Pass.Shader createProfileCGTechniquePassShader() {
        return new ProfileCG.Technique.Pass.Shader();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.Shader.Bind }
     * 
     */
    public ProfileCG.Technique.Pass.Shader.Bind createProfileCGTechniquePassShaderBind() {
        return new ProfileCG.Technique.Pass.Shader.Bind();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.StencilMaskSeparate }
     * 
     */
    public ProfileCG.Technique.Pass.StencilMaskSeparate createProfileCGTechniquePassStencilMaskSeparate() {
        return new ProfileCG.Technique.Pass.StencilMaskSeparate();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.StencilOpSeparate }
     * 
     */
    public ProfileCG.Technique.Pass.StencilOpSeparate createProfileCGTechniquePassStencilOpSeparate() {
        return new ProfileCG.Technique.Pass.StencilOpSeparate();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.StencilFuncSeparate }
     * 
     */
    public ProfileCG.Technique.Pass.StencilFuncSeparate createProfileCGTechniquePassStencilFuncSeparate() {
        return new ProfileCG.Technique.Pass.StencilFuncSeparate();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.StencilOp }
     * 
     */
    public ProfileCG.Technique.Pass.StencilOp createProfileCGTechniquePassStencilOp() {
        return new ProfileCG.Technique.Pass.StencilOp();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.StencilFunc }
     * 
     */
    public ProfileCG.Technique.Pass.StencilFunc createProfileCGTechniquePassStencilFunc() {
        return new ProfileCG.Technique.Pass.StencilFunc();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.PolygonMode }
     * 
     */
    public ProfileCG.Technique.Pass.PolygonMode createProfileCGTechniquePassPolygonMode() {
        return new ProfileCG.Technique.Pass.PolygonMode();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.ColorMaterial }
     * 
     */
    public ProfileCG.Technique.Pass.ColorMaterial createProfileCGTechniquePassColorMaterial() {
        return new ProfileCG.Technique.Pass.ColorMaterial();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.BlendEquationSeparate }
     * 
     */
    public ProfileCG.Technique.Pass.BlendEquationSeparate createProfileCGTechniquePassBlendEquationSeparate() {
        return new ProfileCG.Technique.Pass.BlendEquationSeparate();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.BlendFuncSeparate }
     * 
     */
    public ProfileCG.Technique.Pass.BlendFuncSeparate createProfileCGTechniquePassBlendFuncSeparate() {
        return new ProfileCG.Technique.Pass.BlendFuncSeparate();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.BlendFunc }
     * 
     */
    public ProfileCG.Technique.Pass.BlendFunc createProfileCGTechniquePassBlendFunc() {
        return new ProfileCG.Technique.Pass.BlendFunc();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.AlphaFunc }
     * 
     */
    public ProfileCG.Technique.Pass.AlphaFunc createProfileCGTechniquePassAlphaFunc() {
        return new ProfileCG.Technique.Pass.AlphaFunc();
    }

    /**
     * Create an instance of {@link ProfileGLES }
     * 
     */
    public ProfileGLES createProfileGLES() {
        return new ProfileGLES();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique }
     * 
     */
    public ProfileGLES.Technique createProfileGLESTechnique() {
        return new ProfileGLES.Technique();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass }
     * 
     */
    public ProfileGLES.Technique.Pass createProfileGLESTechniquePass() {
        return new ProfileGLES.Technique.Pass();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.StencilOp }
     * 
     */
    public ProfileGLES.Technique.Pass.StencilOp createProfileGLESTechniquePassStencilOp() {
        return new ProfileGLES.Technique.Pass.StencilOp();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.StencilFunc }
     * 
     */
    public ProfileGLES.Technique.Pass.StencilFunc createProfileGLESTechniquePassStencilFunc() {
        return new ProfileGLES.Technique.Pass.StencilFunc();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.BlendFunc }
     * 
     */
    public ProfileGLES.Technique.Pass.BlendFunc createProfileGLESTechniquePassBlendFunc() {
        return new ProfileGLES.Technique.Pass.BlendFunc();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.AlphaFunc }
     * 
     */
    public ProfileGLES.Technique.Pass.AlphaFunc createProfileGLESTechniquePassAlphaFunc() {
        return new ProfileGLES.Technique.Pass.AlphaFunc();
    }

    /**
     * Create an instance of {@link ProfileCOMMON }
     * 
     */
    public ProfileCOMMON createProfileCOMMON() {
        return new ProfileCOMMON();
    }

    /**
     * Create an instance of {@link ProfileCOMMON.Technique }
     * 
     */
    public ProfileCOMMON.Technique createProfileCOMMONTechnique() {
        return new ProfileCOMMON.Technique();
    }

    /**
     * Create an instance of {@link VisualScene.EvaluateScene }
     * 
     */
    public VisualScene.EvaluateScene createVisualSceneEvaluateScene() {
        return new VisualScene.EvaluateScene();
    }

    /**
     * Create an instance of {@link RigidBody.TechniqueCommon }
     * 
     */
    public RigidBody.TechniqueCommon createRigidBodyTechniqueCommon() {
        return new RigidBody.TechniqueCommon();
    }

    /**
     * Create an instance of {@link RigidBody.TechniqueCommon.Shape }
     * 
     */
    public RigidBody.TechniqueCommon.Shape createRigidBodyTechniqueCommonShape() {
        return new RigidBody.TechniqueCommon.Shape();
    }

    /**
     * Create an instance of {@link Light.TechniqueCommon }
     * 
     */
    public Light.TechniqueCommon createLightTechniqueCommon() {
        return new Light.TechniqueCommon();
    }

    /**
     * Create an instance of {@link InstanceRigidBody.TechniqueCommon }
     * 
     */
    public InstanceRigidBody.TechniqueCommon createInstanceRigidBodyTechniqueCommon() {
        return new InstanceRigidBody.TechniqueCommon();
    }

    /**
     * Create an instance of {@link InstanceRigidBody.TechniqueCommon.Shape }
     * 
     */
    public InstanceRigidBody.TechniqueCommon.Shape createInstanceRigidBodyTechniqueCommonShape() {
        return new InstanceRigidBody.TechniqueCommon.Shape();
    }

    /**
     * Create an instance of {@link RigidConstraint.TechniqueCommon }
     * 
     */
    public RigidConstraint.TechniqueCommon createRigidConstraintTechniqueCommon() {
        return new RigidConstraint.TechniqueCommon();
    }

    /**
     * Create an instance of {@link RigidConstraint.TechniqueCommon.Spring }
     * 
     */
    public RigidConstraint.TechniqueCommon.Spring createRigidConstraintTechniqueCommonSpring() {
        return new RigidConstraint.TechniqueCommon.Spring();
    }

    /**
     * Create an instance of {@link RigidConstraint.TechniqueCommon.Limits }
     * 
     */
    public RigidConstraint.TechniqueCommon.Limits createRigidConstraintTechniqueCommonLimits() {
        return new RigidConstraint.TechniqueCommon.Limits();
    }

    /**
     * Create an instance of {@link Camera.Optics }
     * 
     */
    public Camera.Optics createCameraOptics() {
        return new Camera.Optics();
    }

    /**
     * Create an instance of {@link Camera.Optics.TechniqueCommon }
     * 
     */
    public Camera.Optics.TechniqueCommon createCameraOpticsTechniqueCommon() {
        return new Camera.Optics.TechniqueCommon();
    }

    /**
     * Create an instance of {@link ProfileGLSL }
     * 
     */
    public ProfileGLSL createProfileGLSL() {
        return new ProfileGLSL();
    }

    /**
     * Create an instance of {@link ProfileGLSL.Technique }
     * 
     */
    public ProfileGLSL.Technique createProfileGLSLTechnique() {
        return new ProfileGLSL.Technique();
    }

    /**
     * Create an instance of {@link ProfileGLSL.Technique.Pass }
     * 
     */
    public ProfileGLSL.Technique.Pass createProfileGLSLTechniquePass() {
        return new ProfileGLSL.Technique.Pass();
    }

    /**
     * Create an instance of {@link ProfileGLSL.Technique.Pass.Shader }
     * 
     */
    public ProfileGLSL.Technique.Pass.Shader createProfileGLSLTechniquePassShader() {
        return new ProfileGLSL.Technique.Pass.Shader();
    }

    /**
     * Create an instance of {@link ProfileGLSL.Technique.Pass.Shader.Bind }
     * 
     */
    public ProfileGLSL.Technique.Pass.Shader.Bind createProfileGLSLTechniquePassShaderBind() {
        return new ProfileGLSL.Technique.Pass.Shader.Bind();
    }

    /**
     * Create an instance of {@link InstanceController }
     * 
     */
    public InstanceController createInstanceController() {
        return new InstanceController();
    }

    /**
     * Create an instance of {@link org.lgna.story.resourceutilities.exporterutils.collada.Param }
     * 
     */
    public org.lgna.story.resourceutilities.exporterutils.collada.Param createParam() {
        return new org.lgna.story.resourceutilities.exporterutils.collada.Param();
    }

    /**
     * Create an instance of {@link BindMaterial.TechniqueCommon }
     * 
     */
    public BindMaterial.TechniqueCommon createBindMaterialTechniqueCommon() {
        return new BindMaterial.TechniqueCommon();
    }

    /**
     * Create an instance of {@link org.lgna.story.resourceutilities.exporterutils.collada.Technique }
     * 
     */
    public org.lgna.story.resourceutilities.exporterutils.collada.Technique createTechnique() {
        return new org.lgna.story.resourceutilities.exporterutils.collada.Technique();
    }

    /**
     * Create an instance of {@link Extra }
     * 
     */
    public Extra createExtra() {
        return new Extra();
    }

    /**
     * Create an instance of {@link Asset.Contributor }
     * 
     */
    public Asset.Contributor createAssetContributor() {
        return new Asset.Contributor();
    }

    /**
     * Create an instance of {@link Asset.Unit }
     * 
     */
    public Asset.Unit createAssetUnit() {
        return new Asset.Unit();
    }

    /**
     * Create an instance of {@link LibraryPhysicsMaterials }
     * 
     */
    public LibraryPhysicsMaterials createLibraryPhysicsMaterials() {
        return new LibraryPhysicsMaterials();
    }

    /**
     * Create an instance of {@link PhysicsMaterial.TechniqueCommon }
     * 
     */
    public PhysicsMaterial.TechniqueCommon createPhysicsMaterialTechniqueCommon() {
        return new PhysicsMaterial.TechniqueCommon();
    }

    /**
     * Create an instance of {@link Plane }
     * 
     */
    public Plane createPlane() {
        return new Plane();
    }

    /**
     * Create an instance of {@link Rotate }
     * 
     */
    public Rotate createRotate() {
        return new Rotate();
    }

    /**
     * Create an instance of {@link InputLocalOffset }
     * 
     */
    public InputLocalOffset createInputLocalOffset() {
        return new InputLocalOffset();
    }

    /**
     * Create an instance of {@link Polygons.Ph }
     * 
     */
    public Polygons.Ph createPolygonsPh() {
        return new Polygons.Ph();
    }

    /**
     * Create an instance of {@link Channel }
     * 
     */
    public Channel createChannel() {
        return new Channel();
    }

    /**
     * Create an instance of {@link Lookat }
     * 
     */
    public Lookat createLookat() {
        return new Lookat();
    }

    /**
     * Create an instance of {@link IDREFArray }
     * 
     */
    public IDREFArray createIDREFArray() {
        return new IDREFArray();
    }

    /**
     * Create an instance of {@link NameArray }
     * 
     */
    public NameArray createNameArray() {
        return new NameArray();
    }

    /**
     * Create an instance of {@link BoolArray }
     * 
     */
    public BoolArray createBoolArray() {
        return new BoolArray();
    }

    /**
     * Create an instance of {@link FloatArray }
     * 
     */
    public FloatArray createFloatArray() {
        return new FloatArray();
    }

    /**
     * Create an instance of {@link IntArray }
     * 
     */
    public IntArray createIntArray() {
        return new IntArray();
    }

    /**
     * Create an instance of {@link Source.TechniqueCommon }
     * 
     */
    public Source.TechniqueCommon createSourceTechniqueCommon() {
        return new Source.TechniqueCommon();
    }

    /**
     * Create an instance of {@link TargetableFloat3 }
     * 
     */
    public TargetableFloat3 createTargetableFloat3() {
        return new TargetableFloat3();
    }

    /**
     * Create an instance of {@link Triangles }
     * 
     */
    public Triangles createTriangles() {
        return new Triangles();
    }

    /**
     * Create an instance of {@link LibraryControllers }
     * 
     */
    public LibraryControllers createLibraryControllers() {
        return new LibraryControllers();
    }

    /**
     * Create an instance of {@link Controller }
     * 
     */
    public Controller createController() {
        return new Controller();
    }

    /**
     * Create an instance of {@link Skin.Joints }
     * 
     */
    public Skin.Joints createSkinJoints() {
        return new Skin.Joints();
    }

    /**
     * Create an instance of {@link Skin.VertexWeights }
     * 
     */
    public Skin.VertexWeights createSkinVertexWeights() {
        return new Skin.VertexWeights();
    }

    /**
     * Create an instance of {@link Morph.Targets }
     * 
     */
    public Morph.Targets createMorphTargets() {
        return new Morph.Targets();
    }

    /**
     * Create an instance of {@link Polylist }
     * 
     */
    public Polylist createPolylist() {
        return new Polylist();
    }

    /**
     * Create an instance of {@link InstanceMaterial.Bind }
     * 
     */
    public InstanceMaterial.Bind createInstanceMaterialBind() {
        return new InstanceMaterial.Bind();
    }

    /**
     * Create an instance of {@link InstanceMaterial.BindVertexInput }
     * 
     */
    public InstanceMaterial.BindVertexInput createInstanceMaterialBindVertexInput() {
        return new InstanceMaterial.BindVertexInput();
    }

    /**
     * Create an instance of {@link InstanceWithExtra }
     * 
     */
    public InstanceWithExtra createInstanceWithExtra() {
        return new InstanceWithExtra();
    }

    /**
     * Create an instance of {@link AnimationClip }
     * 
     */
    public AnimationClip createAnimationClip() {
        return new AnimationClip();
    }

    /**
     * Create an instance of {@link Image }
     * 
     */
    public Image createImage() {
        return new Image();
    }

    /**
     * Create an instance of {@link LibraryCameras }
     * 
     */
    public LibraryCameras createLibraryCameras() {
        return new LibraryCameras();
    }

    /**
     * Create an instance of {@link Camera.Imager }
     * 
     */
    public Camera.Imager createCameraImager() {
        return new Camera.Imager();
    }

    /**
     * Create an instance of {@link RigidConstraint.RefAttachment }
     * 
     */
    public RigidConstraint.RefAttachment createRigidConstraintRefAttachment() {
        return new RigidConstraint.RefAttachment();
    }

    /**
     * Create an instance of {@link RigidConstraint.Attachment }
     * 
     */
    public RigidConstraint.Attachment createRigidConstraintAttachment() {
        return new RigidConstraint.Attachment();
    }

    /**
     * Create an instance of {@link LibraryPhysicsScenes }
     * 
     */
    public LibraryPhysicsScenes createLibraryPhysicsScenes() {
        return new LibraryPhysicsScenes();
    }

    /**
     * Create an instance of {@link InstancePhysicsModel }
     * 
     */
    public InstancePhysicsModel createInstancePhysicsModel() {
        return new InstancePhysicsModel();
    }

    /**
     * Create an instance of {@link InstanceRigidConstraint }
     * 
     */
    public InstanceRigidConstraint createInstanceRigidConstraint() {
        return new InstanceRigidConstraint();
    }

    /**
     * Create an instance of {@link PhysicsScene.TechniqueCommon }
     * 
     */
    public PhysicsScene.TechniqueCommon createPhysicsSceneTechniqueCommon() {
        return new PhysicsScene.TechniqueCommon();
    }

    /**
     * Create an instance of {@link Animation }
     * 
     */
    public Animation createAnimation() {
        return new Animation();
    }

    /**
     * Create an instance of {@link Sampler }
     * 
     */
    public Sampler createSampler() {
        return new Sampler();
    }

    /**
     * Create an instance of {@link InputLocal }
     * 
     */
    public InputLocal createInputLocal() {
        return new InputLocal();
    }

    /**
     * Create an instance of {@link Ellipsoid }
     * 
     */
    public Ellipsoid createEllipsoid() {
        return new Ellipsoid();
    }

    /**
     * Create an instance of {@link Node }
     * 
     */
    public Node createNode() {
        return new Node();
    }

    /**
     * Create an instance of {@link Matrix }
     * 
     */
    public Matrix createMatrix() {
        return new Matrix();
    }

    /**
     * Create an instance of {@link Skew }
     * 
     */
    public Skew createSkew() {
        return new Skew();
    }

    /**
     * Create an instance of {@link InstanceGeometry }
     * 
     */
    public InstanceGeometry createInstanceGeometry() {
        return new InstanceGeometry();
    }

    /**
     * Create an instance of {@link Linestrips }
     * 
     */
    public Linestrips createLinestrips() {
        return new Linestrips();
    }

    /**
     * Create an instance of {@link LibraryGeometries }
     * 
     */
    public LibraryGeometries createLibraryGeometries() {
        return new LibraryGeometries();
    }

    /**
     * Create an instance of {@link Geometry }
     * 
     */
    public Geometry createGeometry() {
        return new Geometry();
    }

    /**
     * Create an instance of {@link ConvexMesh }
     * 
     */
    public ConvexMesh createConvexMesh() {
        return new ConvexMesh();
    }

    /**
     * Create an instance of {@link Vertices }
     * 
     */
    public Vertices createVertices() {
        return new Vertices();
    }

    /**
     * Create an instance of {@link Lines }
     * 
     */
    public Lines createLines() {
        return new Lines();
    }

    /**
     * Create an instance of {@link Trifans }
     * 
     */
    public Trifans createTrifans() {
        return new Trifans();
    }

    /**
     * Create an instance of {@link Tristrips }
     * 
     */
    public Tristrips createTristrips() {
        return new Tristrips();
    }

    /**
     * Create an instance of {@link Mesh }
     * 
     */
    public Mesh createMesh() {
        return new Mesh();
    }

    /**
     * Create an instance of {@link Spline.ControlVertices }
     * 
     */
    public Spline.ControlVertices createSplineControlVertices() {
        return new Spline.ControlVertices();
    }

    /**
     * Create an instance of {@link LibraryLights }
     * 
     */
    public LibraryLights createLibraryLights() {
        return new LibraryLights();
    }

    /**
     * Create an instance of {@link Effect }
     * 
     */
    public Effect createEffect() {
        return new Effect();
    }

    /**
     * Create an instance of {@link FxAnnotateCommon }
     * 
     */
    public FxAnnotateCommon createFxAnnotateCommon() {
        return new FxAnnotateCommon();
    }

    /**
     * Create an instance of {@link FxNewparamCommon }
     * 
     */
    public FxNewparamCommon createFxNewparamCommon() {
        return new FxNewparamCommon();
    }

    /**
     * Create an instance of {@link InstanceEffect.TechniqueHint }
     * 
     */
    public InstanceEffect.TechniqueHint createInstanceEffectTechniqueHint() {
        return new InstanceEffect.TechniqueHint();
    }

    /**
     * Create an instance of {@link InstanceEffect.Setparam }
     * 
     */
    public InstanceEffect.Setparam createInstanceEffectSetparam() {
        return new InstanceEffect.Setparam();
    }

    /**
     * Create an instance of {@link LibraryEffects }
     * 
     */
    public LibraryEffects createLibraryEffects() {
        return new LibraryEffects();
    }

    /**
     * Create an instance of {@link LibraryAnimations }
     * 
     */
    public LibraryAnimations createLibraryAnimations() {
        return new LibraryAnimations();
    }

    /**
     * Create an instance of {@link Capsule }
     * 
     */
    public Capsule createCapsule() {
        return new Capsule();
    }

    /**
     * Create an instance of {@link PhysicsModel }
     * 
     */
    public PhysicsModel createPhysicsModel() {
        return new PhysicsModel();
    }

    /**
     * Create an instance of {@link Accessor }
     * 
     */
    public Accessor createAccessor() {
        return new Accessor();
    }

    /**
     * Create an instance of {@link Box }
     * 
     */
    public Box createBox() {
        return new Box();
    }

    /**
     * Create an instance of {@link LibraryImages }
     * 
     */
    public LibraryImages createLibraryImages() {
        return new LibraryImages();
    }

    /**
     * Create an instance of {@link LibraryPhysicsModels }
     * 
     */
    public LibraryPhysicsModels createLibraryPhysicsModels() {
        return new LibraryPhysicsModels();
    }

    /**
     * Create an instance of {@link Sphere }
     * 
     */
    public Sphere createSphere() {
        return new Sphere();
    }

    /**
     * Create an instance of {@link LibraryMaterials }
     * 
     */
    public LibraryMaterials createLibraryMaterials() {
        return new LibraryMaterials();
    }

    /**
     * Create an instance of {@link Material }
     * 
     */
    public Material createMaterial() {
        return new Material();
    }

    /**
     * Create an instance of {@link LibraryAnimationClips }
     * 
     */
    public LibraryAnimationClips createLibraryAnimationClips() {
        return new LibraryAnimationClips();
    }

    /**
     * Create an instance of {@link ForceField }
     * 
     */
    public ForceField createForceField() {
        return new ForceField();
    }

    /**
     * Create an instance of {@link LibraryNodes }
     * 
     */
    public LibraryNodes createLibraryNodes() {
        return new LibraryNodes();
    }

    /**
     * Create an instance of {@link TaperedCylinder }
     * 
     */
    public TaperedCylinder createTaperedCylinder() {
        return new TaperedCylinder();
    }

    /**
     * Create an instance of {@link LibraryForceFields }
     * 
     */
    public LibraryForceFields createLibraryForceFields() {
        return new LibraryForceFields();
    }

    /**
     * Create an instance of {@link LibraryVisualScenes }
     * 
     */
    public LibraryVisualScenes createLibraryVisualScenes() {
        return new LibraryVisualScenes();
    }

    /**
     * Create an instance of {@link COLLADA.Scene }
     * 
     */
    public COLLADA.Scene createCOLLADAScene() {
        return new COLLADA.Scene();
    }

    /**
     * Create an instance of {@link TaperedCapsule }
     * 
     */
    public TaperedCapsule createTaperedCapsule() {
        return new TaperedCapsule();
    }

    /**
     * Create an instance of {@link Cylinder }
     * 
     */
    public Cylinder createCylinder() {
        return new Cylinder();
    }

    /**
     * Create an instance of {@link FxStenciltargetCommon }
     * 
     */
    public FxStenciltargetCommon createFxStenciltargetCommon() {
        return new FxStenciltargetCommon();
    }

    /**
     * Create an instance of {@link CgConnectParam }
     * 
     */
    public CgConnectParam createCgConnectParam() {
        return new CgConnectParam();
    }

    /**
     * Create an instance of {@link CgNewparam }
     * 
     */
    public CgNewparam createCgNewparam() {
        return new CgNewparam();
    }

    /**
     * Create an instance of {@link CgSamplerDEPTH }
     * 
     */
    public CgSamplerDEPTH createCgSamplerDEPTH() {
        return new CgSamplerDEPTH();
    }

    /**
     * Create an instance of {@link FxSampler1DCommon }
     * 
     */
    public FxSampler1DCommon createFxSampler1DCommon() {
        return new FxSampler1DCommon();
    }

    /**
     * Create an instance of {@link FxCleardepthCommon }
     * 
     */
    public FxCleardepthCommon createFxCleardepthCommon() {
        return new FxCleardepthCommon();
    }

    /**
     * Create an instance of {@link FxSamplerCUBECommon }
     * 
     */
    public FxSamplerCUBECommon createFxSamplerCUBECommon() {
        return new FxSamplerCUBECommon();
    }

    /**
     * Create an instance of {@link CgSamplerRECT }
     * 
     */
    public CgSamplerRECT createCgSamplerRECT() {
        return new CgSamplerRECT();
    }

    /**
     * Create an instance of {@link GlesNewparam }
     * 
     */
    public GlesNewparam createGlesNewparam() {
        return new GlesNewparam();
    }

    /**
     * Create an instance of {@link FxSurfaceFormatHintCommon }
     * 
     */
    public FxSurfaceFormatHintCommon createFxSurfaceFormatHintCommon() {
        return new FxSurfaceFormatHintCommon();
    }

    /**
     * Create an instance of {@link FxDepthtargetCommon }
     * 
     */
    public FxDepthtargetCommon createFxDepthtargetCommon() {
        return new FxDepthtargetCommon();
    }

    /**
     * Create an instance of {@link FxColortargetCommon }
     * 
     */
    public FxColortargetCommon createFxColortargetCommon() {
        return new FxColortargetCommon();
    }

    /**
     * Create an instance of {@link CgSetuserType }
     * 
     */
    public CgSetuserType createCgSetuserType() {
        return new CgSetuserType();
    }

    /**
     * Create an instance of {@link CgSetparam }
     * 
     */
    public CgSetparam createCgSetparam() {
        return new CgSetparam();
    }

    /**
     * Create an instance of {@link GlslNewarrayType }
     * 
     */
    public GlslNewarrayType createGlslNewarrayType() {
        return new GlslNewarrayType();
    }

    /**
     * Create an instance of {@link CgSamplerCUBE }
     * 
     */
    public CgSamplerCUBE createCgSamplerCUBE() {
        return new CgSamplerCUBE();
    }

    /**
     * Create an instance of {@link GlesTexturePipeline }
     * 
     */
    public GlesTexturePipeline createGlesTexturePipeline() {
        return new GlesTexturePipeline();
    }

    /**
     * Create an instance of {@link GlslSetparam }
     * 
     */
    public GlslSetparam createGlslSetparam() {
        return new GlslSetparam();
    }

    /**
     * Create an instance of {@link FxSampler3DCommon }
     * 
     */
    public FxSampler3DCommon createFxSampler3DCommon() {
        return new FxSampler3DCommon();
    }

    /**
     * Create an instance of {@link CommonNewparamType }
     * 
     */
    public CommonNewparamType createCommonNewparamType() {
        return new CommonNewparamType();
    }

    /**
     * Create an instance of {@link FxSamplerDEPTHCommon }
     * 
     */
    public FxSamplerDEPTHCommon createFxSamplerDEPTHCommon() {
        return new FxSamplerDEPTHCommon();
    }

    /**
     * Create an instance of {@link GlesTexcombinerCommandType }
     * 
     */
    public GlesTexcombinerCommandType createGlesTexcombinerCommandType() {
        return new GlesTexcombinerCommandType();
    }

    /**
     * Create an instance of {@link FxClearstencilCommon }
     * 
     */
    public FxClearstencilCommon createFxClearstencilCommon() {
        return new FxClearstencilCommon();
    }

    /**
     * Create an instance of {@link CommonTransparentType }
     * 
     */
    public CommonTransparentType createCommonTransparentType() {
        return new CommonTransparentType();
    }

    /**
     * Create an instance of {@link GlesTexcombinerArgumentRGBType }
     * 
     */
    public GlesTexcombinerArgumentRGBType createGlesTexcombinerArgumentRGBType() {
        return new GlesTexcombinerArgumentRGBType();
    }

    /**
     * Create an instance of {@link GlesTexcombinerCommandRGBType }
     * 
     */
    public GlesTexcombinerCommandRGBType createGlesTexcombinerCommandRGBType() {
        return new GlesTexcombinerCommandRGBType();
    }

    /**
     * Create an instance of {@link GlesTexcombinerCommandAlphaType }
     * 
     */
    public GlesTexcombinerCommandAlphaType createGlesTexcombinerCommandAlphaType() {
        return new GlesTexcombinerCommandAlphaType();
    }

    /**
     * Create an instance of {@link FxSurfaceCommon }
     * 
     */
    public FxSurfaceCommon createFxSurfaceCommon() {
        return new FxSurfaceCommon();
    }

    /**
     * Create an instance of {@link GlslSetarrayType }
     * 
     */
    public GlslSetarrayType createGlslSetarrayType() {
        return new GlslSetarrayType();
    }

    /**
     * Create an instance of {@link GlSamplerDEPTH }
     * 
     */
    public GlSamplerDEPTH createGlSamplerDEPTH() {
        return new GlSamplerDEPTH();
    }

    /**
     * Create an instance of {@link GlesSamplerState }
     * 
     */
    public GlesSamplerState createGlesSamplerState() {
        return new GlesSamplerState();
    }

    /**
     * Create an instance of {@link FxCodeProfile }
     * 
     */
    public FxCodeProfile createFxCodeProfile() {
        return new FxCodeProfile();
    }

    /**
     * Create an instance of {@link GlSampler2D }
     * 
     */
    public GlSampler2D createGlSampler2D() {
        return new GlSampler2D();
    }

    /**
     * Create an instance of {@link CgNewarrayType }
     * 
     */
    public CgNewarrayType createCgNewarrayType() {
        return new CgNewarrayType();
    }

    /**
     * Create an instance of {@link CgSampler3D }
     * 
     */
    public CgSampler3D createCgSampler3D() {
        return new CgSampler3D();
    }

    /**
     * Create an instance of {@link GlSampler3D }
     * 
     */
    public GlSampler3D createGlSampler3D() {
        return new GlSampler3D();
    }

    /**
     * Create an instance of {@link GlSamplerRECT }
     * 
     */
    public GlSamplerRECT createGlSamplerRECT() {
        return new GlSamplerRECT();
    }

    /**
     * Create an instance of {@link GlesTexenvCommandType }
     * 
     */
    public GlesTexenvCommandType createGlesTexenvCommandType() {
        return new GlesTexenvCommandType();
    }

    /**
     * Create an instance of {@link FxSampler2DCommon }
     * 
     */
    public FxSampler2DCommon createFxSampler2DCommon() {
        return new FxSampler2DCommon();
    }

    /**
     * Create an instance of {@link GlesTextureConstantType }
     * 
     */
    public GlesTextureConstantType createGlesTextureConstantType() {
        return new GlesTextureConstantType();
    }

    /**
     * Create an instance of {@link GlSamplerCUBE }
     * 
     */
    public GlSamplerCUBE createGlSamplerCUBE() {
        return new GlSamplerCUBE();
    }

    /**
     * Create an instance of {@link CgSampler1D }
     * 
     */
    public CgSampler1D createCgSampler1D() {
        return new CgSampler1D();
    }

    /**
     * Create an instance of {@link CgSampler2D }
     * 
     */
    public CgSampler2D createCgSampler2D() {
        return new CgSampler2D();
    }

    /**
     * Create an instance of {@link GlslSetparamSimple }
     * 
     */
    public GlslSetparamSimple createGlslSetparamSimple() {
        return new GlslSetparamSimple();
    }

    /**
     * Create an instance of {@link GlesTexcombinerArgumentAlphaType }
     * 
     */
    public GlesTexcombinerArgumentAlphaType createGlesTexcombinerArgumentAlphaType() {
        return new GlesTexcombinerArgumentAlphaType();
    }

    /**
     * Create an instance of {@link TargetableFloat }
     * 
     */
    public TargetableFloat createTargetableFloat() {
        return new TargetableFloat();
    }

    /**
     * Create an instance of {@link FxSurfaceInitFromCommon }
     * 
     */
    public FxSurfaceInitFromCommon createFxSurfaceInitFromCommon() {
        return new FxSurfaceInitFromCommon();
    }

    /**
     * Create an instance of {@link InputGlobal }
     * 
     */
    public InputGlobal createInputGlobal() {
        return new InputGlobal();
    }

    /**
     * Create an instance of {@link FxClearcolorCommon }
     * 
     */
    public FxClearcolorCommon createFxClearcolorCommon() {
        return new FxClearcolorCommon();
    }

    /**
     * Create an instance of {@link FxSamplerRECTCommon }
     * 
     */
    public FxSamplerRECTCommon createFxSamplerRECTCommon() {
        return new FxSamplerRECTCommon();
    }

    /**
     * Create an instance of {@link GlslNewparam }
     * 
     */
    public GlslNewparam createGlslNewparam() {
        return new GlslNewparam();
    }

    /**
     * Create an instance of {@link FxIncludeCommon }
     * 
     */
    public FxIncludeCommon createFxIncludeCommon() {
        return new FxIncludeCommon();
    }

    /**
     * Create an instance of {@link CgSetarrayType }
     * 
     */
    public CgSetarrayType createCgSetarrayType() {
        return new CgSetarrayType();
    }

    /**
     * Create an instance of {@link GlSampler1D }
     * 
     */
    public GlSampler1D createGlSampler1D() {
        return new GlSampler1D();
    }

    /**
     * Create an instance of {@link CgSetparamSimple }
     * 
     */
    public CgSetparamSimple createCgSetparamSimple() {
        return new CgSetparamSimple();
    }

    /**
     * Create an instance of {@link GlesTextureUnit.Texcoord }
     * 
     */
    public GlesTextureUnit.Texcoord createGlesTextureUnitTexcoord() {
        return new GlesTextureUnit.Texcoord();
    }

    /**
     * Create an instance of {@link FxSurfaceInitCubeCommon.All }
     * 
     */
    public FxSurfaceInitCubeCommon.All createFxSurfaceInitCubeCommonAll() {
        return new FxSurfaceInitCubeCommon.All();
    }

    /**
     * Create an instance of {@link FxSurfaceInitCubeCommon.Primary }
     * 
     */
    public FxSurfaceInitCubeCommon.Primary createFxSurfaceInitCubeCommonPrimary() {
        return new FxSurfaceInitCubeCommon.Primary();
    }

    /**
     * Create an instance of {@link FxSurfaceInitCubeCommon.Face }
     * 
     */
    public FxSurfaceInitCubeCommon.Face createFxSurfaceInitCubeCommonFace() {
        return new FxSurfaceInitCubeCommon.Face();
    }

    /**
     * Create an instance of {@link FxSurfaceInitVolumeCommon.All }
     * 
     */
    public FxSurfaceInitVolumeCommon.All createFxSurfaceInitVolumeCommonAll() {
        return new FxSurfaceInitVolumeCommon.All();
    }

    /**
     * Create an instance of {@link FxSurfaceInitVolumeCommon.Primary }
     * 
     */
    public FxSurfaceInitVolumeCommon.Primary createFxSurfaceInitVolumeCommonPrimary() {
        return new FxSurfaceInitVolumeCommon.Primary();
    }

    /**
     * Create an instance of {@link CommonFloatOrParamType.Float }
     * 
     */
    public CommonFloatOrParamType.Float createCommonFloatOrParamTypeFloat() {
        return new CommonFloatOrParamType.Float();
    }

    /**
     * Create an instance of {@link CommonFloatOrParamType.Param }
     * 
     */
    public CommonFloatOrParamType.Param createCommonFloatOrParamTypeParam() {
        return new CommonFloatOrParamType.Param();
    }

    /**
     * Create an instance of {@link CgSurfaceType.Generator.Name }
     * 
     */
    public CgSurfaceType.Generator.Name createCgSurfaceTypeGeneratorName() {
        return new CgSurfaceType.Generator.Name();
    }

    /**
     * Create an instance of {@link CommonColorOrTextureType.Color }
     * 
     */
    public CommonColorOrTextureType.Color createCommonColorOrTextureTypeColor() {
        return new CommonColorOrTextureType.Color();
    }

    /**
     * Create an instance of {@link CommonColorOrTextureType.Param }
     * 
     */
    public CommonColorOrTextureType.Param createCommonColorOrTextureTypeParam() {
        return new CommonColorOrTextureType.Param();
    }

    /**
     * Create an instance of {@link CommonColorOrTextureType.Texture }
     * 
     */
    public CommonColorOrTextureType.Texture createCommonColorOrTextureTypeTexture() {
        return new CommonColorOrTextureType.Texture();
    }

    /**
     * Create an instance of {@link FxSurfaceInitPlanarCommon.All }
     * 
     */
    public FxSurfaceInitPlanarCommon.All createFxSurfaceInitPlanarCommonAll() {
        return new FxSurfaceInitPlanarCommon.All();
    }

    /**
     * Create an instance of {@link GlslSurfaceType.Generator.Name }
     * 
     */
    public GlslSurfaceType.Generator.Name createGlslSurfaceTypeGeneratorName() {
        return new GlslSurfaceType.Generator.Name();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.BlendEquation }
     * 
     */
    public ProfileCG.Technique.Pass.BlendEquation createProfileCGTechniquePassBlendEquation() {
        return new ProfileCG.Technique.Pass.BlendEquation();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.CullFace }
     * 
     */
    public ProfileCG.Technique.Pass.CullFace createProfileCGTechniquePassCullFace() {
        return new ProfileCG.Technique.Pass.CullFace();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.DepthFunc }
     * 
     */
    public ProfileCG.Technique.Pass.DepthFunc createProfileCGTechniquePassDepthFunc() {
        return new ProfileCG.Technique.Pass.DepthFunc();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.FogMode }
     * 
     */
    public ProfileCG.Technique.Pass.FogMode createProfileCGTechniquePassFogMode() {
        return new ProfileCG.Technique.Pass.FogMode();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.FogCoordSrc }
     * 
     */
    public ProfileCG.Technique.Pass.FogCoordSrc createProfileCGTechniquePassFogCoordSrc() {
        return new ProfileCG.Technique.Pass.FogCoordSrc();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.FrontFace }
     * 
     */
    public ProfileCG.Technique.Pass.FrontFace createProfileCGTechniquePassFrontFace() {
        return new ProfileCG.Technique.Pass.FrontFace();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.LightModelColorControl }
     * 
     */
    public ProfileCG.Technique.Pass.LightModelColorControl createProfileCGTechniquePassLightModelColorControl() {
        return new ProfileCG.Technique.Pass.LightModelColorControl();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.LogicOp }
     * 
     */
    public ProfileCG.Technique.Pass.LogicOp createProfileCGTechniquePassLogicOp() {
        return new ProfileCG.Technique.Pass.LogicOp();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.ShadeModel }
     * 
     */
    public ProfileCG.Technique.Pass.ShadeModel createProfileCGTechniquePassShadeModel() {
        return new ProfileCG.Technique.Pass.ShadeModel();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.LightEnable }
     * 
     */
    public ProfileCG.Technique.Pass.LightEnable createProfileCGTechniquePassLightEnable() {
        return new ProfileCG.Technique.Pass.LightEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.LightAmbient }
     * 
     */
    public ProfileCG.Technique.Pass.LightAmbient createProfileCGTechniquePassLightAmbient() {
        return new ProfileCG.Technique.Pass.LightAmbient();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.LightDiffuse }
     * 
     */
    public ProfileCG.Technique.Pass.LightDiffuse createProfileCGTechniquePassLightDiffuse() {
        return new ProfileCG.Technique.Pass.LightDiffuse();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.LightSpecular }
     * 
     */
    public ProfileCG.Technique.Pass.LightSpecular createProfileCGTechniquePassLightSpecular() {
        return new ProfileCG.Technique.Pass.LightSpecular();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.LightPosition }
     * 
     */
    public ProfileCG.Technique.Pass.LightPosition createProfileCGTechniquePassLightPosition() {
        return new ProfileCG.Technique.Pass.LightPosition();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.LightConstantAttenuation }
     * 
     */
    public ProfileCG.Technique.Pass.LightConstantAttenuation createProfileCGTechniquePassLightConstantAttenuation() {
        return new ProfileCG.Technique.Pass.LightConstantAttenuation();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.LightLinearAttenuation }
     * 
     */
    public ProfileCG.Technique.Pass.LightLinearAttenuation createProfileCGTechniquePassLightLinearAttenuation() {
        return new ProfileCG.Technique.Pass.LightLinearAttenuation();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.LightQuadraticAttenuation }
     * 
     */
    public ProfileCG.Technique.Pass.LightQuadraticAttenuation createProfileCGTechniquePassLightQuadraticAttenuation() {
        return new ProfileCG.Technique.Pass.LightQuadraticAttenuation();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.LightSpotCutoff }
     * 
     */
    public ProfileCG.Technique.Pass.LightSpotCutoff createProfileCGTechniquePassLightSpotCutoff() {
        return new ProfileCG.Technique.Pass.LightSpotCutoff();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.LightSpotDirection }
     * 
     */
    public ProfileCG.Technique.Pass.LightSpotDirection createProfileCGTechniquePassLightSpotDirection() {
        return new ProfileCG.Technique.Pass.LightSpotDirection();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.LightSpotExponent }
     * 
     */
    public ProfileCG.Technique.Pass.LightSpotExponent createProfileCGTechniquePassLightSpotExponent() {
        return new ProfileCG.Technique.Pass.LightSpotExponent();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.Texture1D }
     * 
     */
    public ProfileCG.Technique.Pass.Texture1D createProfileCGTechniquePassTexture1D() {
        return new ProfileCG.Technique.Pass.Texture1D();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.Texture2D }
     * 
     */
    public ProfileCG.Technique.Pass.Texture2D createProfileCGTechniquePassTexture2D() {
        return new ProfileCG.Technique.Pass.Texture2D();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.Texture3D }
     * 
     */
    public ProfileCG.Technique.Pass.Texture3D createProfileCGTechniquePassTexture3D() {
        return new ProfileCG.Technique.Pass.Texture3D();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.TextureCUBE }
     * 
     */
    public ProfileCG.Technique.Pass.TextureCUBE createProfileCGTechniquePassTextureCUBE() {
        return new ProfileCG.Technique.Pass.TextureCUBE();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.TextureRECT }
     * 
     */
    public ProfileCG.Technique.Pass.TextureRECT createProfileCGTechniquePassTextureRECT() {
        return new ProfileCG.Technique.Pass.TextureRECT();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.TextureDEPTH }
     * 
     */
    public ProfileCG.Technique.Pass.TextureDEPTH createProfileCGTechniquePassTextureDEPTH() {
        return new ProfileCG.Technique.Pass.TextureDEPTH();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.Texture1DEnable }
     * 
     */
    public ProfileCG.Technique.Pass.Texture1DEnable createProfileCGTechniquePassTexture1DEnable() {
        return new ProfileCG.Technique.Pass.Texture1DEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.Texture2DEnable }
     * 
     */
    public ProfileCG.Technique.Pass.Texture2DEnable createProfileCGTechniquePassTexture2DEnable() {
        return new ProfileCG.Technique.Pass.Texture2DEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.Texture3DEnable }
     * 
     */
    public ProfileCG.Technique.Pass.Texture3DEnable createProfileCGTechniquePassTexture3DEnable() {
        return new ProfileCG.Technique.Pass.Texture3DEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.TextureCUBEEnable }
     * 
     */
    public ProfileCG.Technique.Pass.TextureCUBEEnable createProfileCGTechniquePassTextureCUBEEnable() {
        return new ProfileCG.Technique.Pass.TextureCUBEEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.TextureRECTEnable }
     * 
     */
    public ProfileCG.Technique.Pass.TextureRECTEnable createProfileCGTechniquePassTextureRECTEnable() {
        return new ProfileCG.Technique.Pass.TextureRECTEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.TextureDEPTHEnable }
     * 
     */
    public ProfileCG.Technique.Pass.TextureDEPTHEnable createProfileCGTechniquePassTextureDEPTHEnable() {
        return new ProfileCG.Technique.Pass.TextureDEPTHEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.TextureEnvColor }
     * 
     */
    public ProfileCG.Technique.Pass.TextureEnvColor createProfileCGTechniquePassTextureEnvColor() {
        return new ProfileCG.Technique.Pass.TextureEnvColor();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.TextureEnvMode }
     * 
     */
    public ProfileCG.Technique.Pass.TextureEnvMode createProfileCGTechniquePassTextureEnvMode() {
        return new ProfileCG.Technique.Pass.TextureEnvMode();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.ClipPlane }
     * 
     */
    public ProfileCG.Technique.Pass.ClipPlane createProfileCGTechniquePassClipPlane() {
        return new ProfileCG.Technique.Pass.ClipPlane();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.ClipPlaneEnable }
     * 
     */
    public ProfileCG.Technique.Pass.ClipPlaneEnable createProfileCGTechniquePassClipPlaneEnable() {
        return new ProfileCG.Technique.Pass.ClipPlaneEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.BlendColor }
     * 
     */
    public ProfileCG.Technique.Pass.BlendColor createProfileCGTechniquePassBlendColor() {
        return new ProfileCG.Technique.Pass.BlendColor();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.ClearColor }
     * 
     */
    public ProfileCG.Technique.Pass.ClearColor createProfileCGTechniquePassClearColor() {
        return new ProfileCG.Technique.Pass.ClearColor();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.ClearStencil }
     * 
     */
    public ProfileCG.Technique.Pass.ClearStencil createProfileCGTechniquePassClearStencil() {
        return new ProfileCG.Technique.Pass.ClearStencil();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.ClearDepth }
     * 
     */
    public ProfileCG.Technique.Pass.ClearDepth createProfileCGTechniquePassClearDepth() {
        return new ProfileCG.Technique.Pass.ClearDepth();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.ColorMask }
     * 
     */
    public ProfileCG.Technique.Pass.ColorMask createProfileCGTechniquePassColorMask() {
        return new ProfileCG.Technique.Pass.ColorMask();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.DepthBounds }
     * 
     */
    public ProfileCG.Technique.Pass.DepthBounds createProfileCGTechniquePassDepthBounds() {
        return new ProfileCG.Technique.Pass.DepthBounds();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.DepthMask }
     * 
     */
    public ProfileCG.Technique.Pass.DepthMask createProfileCGTechniquePassDepthMask() {
        return new ProfileCG.Technique.Pass.DepthMask();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.DepthRange }
     * 
     */
    public ProfileCG.Technique.Pass.DepthRange createProfileCGTechniquePassDepthRange() {
        return new ProfileCG.Technique.Pass.DepthRange();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.FogDensity }
     * 
     */
    public ProfileCG.Technique.Pass.FogDensity createProfileCGTechniquePassFogDensity() {
        return new ProfileCG.Technique.Pass.FogDensity();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.FogStart }
     * 
     */
    public ProfileCG.Technique.Pass.FogStart createProfileCGTechniquePassFogStart() {
        return new ProfileCG.Technique.Pass.FogStart();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.FogEnd }
     * 
     */
    public ProfileCG.Technique.Pass.FogEnd createProfileCGTechniquePassFogEnd() {
        return new ProfileCG.Technique.Pass.FogEnd();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.FogColor }
     * 
     */
    public ProfileCG.Technique.Pass.FogColor createProfileCGTechniquePassFogColor() {
        return new ProfileCG.Technique.Pass.FogColor();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.LightModelAmbient }
     * 
     */
    public ProfileCG.Technique.Pass.LightModelAmbient createProfileCGTechniquePassLightModelAmbient() {
        return new ProfileCG.Technique.Pass.LightModelAmbient();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.LightingEnable }
     * 
     */
    public ProfileCG.Technique.Pass.LightingEnable createProfileCGTechniquePassLightingEnable() {
        return new ProfileCG.Technique.Pass.LightingEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.LineStipple }
     * 
     */
    public ProfileCG.Technique.Pass.LineStipple createProfileCGTechniquePassLineStipple() {
        return new ProfileCG.Technique.Pass.LineStipple();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.LineWidth }
     * 
     */
    public ProfileCG.Technique.Pass.LineWidth createProfileCGTechniquePassLineWidth() {
        return new ProfileCG.Technique.Pass.LineWidth();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.MaterialAmbient }
     * 
     */
    public ProfileCG.Technique.Pass.MaterialAmbient createProfileCGTechniquePassMaterialAmbient() {
        return new ProfileCG.Technique.Pass.MaterialAmbient();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.MaterialDiffuse }
     * 
     */
    public ProfileCG.Technique.Pass.MaterialDiffuse createProfileCGTechniquePassMaterialDiffuse() {
        return new ProfileCG.Technique.Pass.MaterialDiffuse();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.MaterialEmission }
     * 
     */
    public ProfileCG.Technique.Pass.MaterialEmission createProfileCGTechniquePassMaterialEmission() {
        return new ProfileCG.Technique.Pass.MaterialEmission();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.MaterialShininess }
     * 
     */
    public ProfileCG.Technique.Pass.MaterialShininess createProfileCGTechniquePassMaterialShininess() {
        return new ProfileCG.Technique.Pass.MaterialShininess();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.MaterialSpecular }
     * 
     */
    public ProfileCG.Technique.Pass.MaterialSpecular createProfileCGTechniquePassMaterialSpecular() {
        return new ProfileCG.Technique.Pass.MaterialSpecular();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.ModelViewMatrix }
     * 
     */
    public ProfileCG.Technique.Pass.ModelViewMatrix createProfileCGTechniquePassModelViewMatrix() {
        return new ProfileCG.Technique.Pass.ModelViewMatrix();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.PointDistanceAttenuation }
     * 
     */
    public ProfileCG.Technique.Pass.PointDistanceAttenuation createProfileCGTechniquePassPointDistanceAttenuation() {
        return new ProfileCG.Technique.Pass.PointDistanceAttenuation();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.PointFadeThresholdSize }
     * 
     */
    public ProfileCG.Technique.Pass.PointFadeThresholdSize createProfileCGTechniquePassPointFadeThresholdSize() {
        return new ProfileCG.Technique.Pass.PointFadeThresholdSize();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.PointSize }
     * 
     */
    public ProfileCG.Technique.Pass.PointSize createProfileCGTechniquePassPointSize() {
        return new ProfileCG.Technique.Pass.PointSize();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.PointSizeMin }
     * 
     */
    public ProfileCG.Technique.Pass.PointSizeMin createProfileCGTechniquePassPointSizeMin() {
        return new ProfileCG.Technique.Pass.PointSizeMin();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.PointSizeMax }
     * 
     */
    public ProfileCG.Technique.Pass.PointSizeMax createProfileCGTechniquePassPointSizeMax() {
        return new ProfileCG.Technique.Pass.PointSizeMax();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.PolygonOffset }
     * 
     */
    public ProfileCG.Technique.Pass.PolygonOffset createProfileCGTechniquePassPolygonOffset() {
        return new ProfileCG.Technique.Pass.PolygonOffset();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.ProjectionMatrix }
     * 
     */
    public ProfileCG.Technique.Pass.ProjectionMatrix createProfileCGTechniquePassProjectionMatrix() {
        return new ProfileCG.Technique.Pass.ProjectionMatrix();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.Scissor }
     * 
     */
    public ProfileCG.Technique.Pass.Scissor createProfileCGTechniquePassScissor() {
        return new ProfileCG.Technique.Pass.Scissor();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.StencilMask }
     * 
     */
    public ProfileCG.Technique.Pass.StencilMask createProfileCGTechniquePassStencilMask() {
        return new ProfileCG.Technique.Pass.StencilMask();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.AlphaTestEnable }
     * 
     */
    public ProfileCG.Technique.Pass.AlphaTestEnable createProfileCGTechniquePassAlphaTestEnable() {
        return new ProfileCG.Technique.Pass.AlphaTestEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.AutoNormalEnable }
     * 
     */
    public ProfileCG.Technique.Pass.AutoNormalEnable createProfileCGTechniquePassAutoNormalEnable() {
        return new ProfileCG.Technique.Pass.AutoNormalEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.BlendEnable }
     * 
     */
    public ProfileCG.Technique.Pass.BlendEnable createProfileCGTechniquePassBlendEnable() {
        return new ProfileCG.Technique.Pass.BlendEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.ColorLogicOpEnable }
     * 
     */
    public ProfileCG.Technique.Pass.ColorLogicOpEnable createProfileCGTechniquePassColorLogicOpEnable() {
        return new ProfileCG.Technique.Pass.ColorLogicOpEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.ColorMaterialEnable }
     * 
     */
    public ProfileCG.Technique.Pass.ColorMaterialEnable createProfileCGTechniquePassColorMaterialEnable() {
        return new ProfileCG.Technique.Pass.ColorMaterialEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.CullFaceEnable }
     * 
     */
    public ProfileCG.Technique.Pass.CullFaceEnable createProfileCGTechniquePassCullFaceEnable() {
        return new ProfileCG.Technique.Pass.CullFaceEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.DepthBoundsEnable }
     * 
     */
    public ProfileCG.Technique.Pass.DepthBoundsEnable createProfileCGTechniquePassDepthBoundsEnable() {
        return new ProfileCG.Technique.Pass.DepthBoundsEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.DepthClampEnable }
     * 
     */
    public ProfileCG.Technique.Pass.DepthClampEnable createProfileCGTechniquePassDepthClampEnable() {
        return new ProfileCG.Technique.Pass.DepthClampEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.DepthTestEnable }
     * 
     */
    public ProfileCG.Technique.Pass.DepthTestEnable createProfileCGTechniquePassDepthTestEnable() {
        return new ProfileCG.Technique.Pass.DepthTestEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.DitherEnable }
     * 
     */
    public ProfileCG.Technique.Pass.DitherEnable createProfileCGTechniquePassDitherEnable() {
        return new ProfileCG.Technique.Pass.DitherEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.FogEnable }
     * 
     */
    public ProfileCG.Technique.Pass.FogEnable createProfileCGTechniquePassFogEnable() {
        return new ProfileCG.Technique.Pass.FogEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.LightModelLocalViewerEnable }
     * 
     */
    public ProfileCG.Technique.Pass.LightModelLocalViewerEnable createProfileCGTechniquePassLightModelLocalViewerEnable() {
        return new ProfileCG.Technique.Pass.LightModelLocalViewerEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.LightModelTwoSideEnable }
     * 
     */
    public ProfileCG.Technique.Pass.LightModelTwoSideEnable createProfileCGTechniquePassLightModelTwoSideEnable() {
        return new ProfileCG.Technique.Pass.LightModelTwoSideEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.LineSmoothEnable }
     * 
     */
    public ProfileCG.Technique.Pass.LineSmoothEnable createProfileCGTechniquePassLineSmoothEnable() {
        return new ProfileCG.Technique.Pass.LineSmoothEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.LineStippleEnable }
     * 
     */
    public ProfileCG.Technique.Pass.LineStippleEnable createProfileCGTechniquePassLineStippleEnable() {
        return new ProfileCG.Technique.Pass.LineStippleEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.LogicOpEnable }
     * 
     */
    public ProfileCG.Technique.Pass.LogicOpEnable createProfileCGTechniquePassLogicOpEnable() {
        return new ProfileCG.Technique.Pass.LogicOpEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.MultisampleEnable }
     * 
     */
    public ProfileCG.Technique.Pass.MultisampleEnable createProfileCGTechniquePassMultisampleEnable() {
        return new ProfileCG.Technique.Pass.MultisampleEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.NormalizeEnable }
     * 
     */
    public ProfileCG.Technique.Pass.NormalizeEnable createProfileCGTechniquePassNormalizeEnable() {
        return new ProfileCG.Technique.Pass.NormalizeEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.PointSmoothEnable }
     * 
     */
    public ProfileCG.Technique.Pass.PointSmoothEnable createProfileCGTechniquePassPointSmoothEnable() {
        return new ProfileCG.Technique.Pass.PointSmoothEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.PolygonOffsetFillEnable }
     * 
     */
    public ProfileCG.Technique.Pass.PolygonOffsetFillEnable createProfileCGTechniquePassPolygonOffsetFillEnable() {
        return new ProfileCG.Technique.Pass.PolygonOffsetFillEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.PolygonOffsetLineEnable }
     * 
     */
    public ProfileCG.Technique.Pass.PolygonOffsetLineEnable createProfileCGTechniquePassPolygonOffsetLineEnable() {
        return new ProfileCG.Technique.Pass.PolygonOffsetLineEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.PolygonOffsetPointEnable }
     * 
     */
    public ProfileCG.Technique.Pass.PolygonOffsetPointEnable createProfileCGTechniquePassPolygonOffsetPointEnable() {
        return new ProfileCG.Technique.Pass.PolygonOffsetPointEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.PolygonSmoothEnable }
     * 
     */
    public ProfileCG.Technique.Pass.PolygonSmoothEnable createProfileCGTechniquePassPolygonSmoothEnable() {
        return new ProfileCG.Technique.Pass.PolygonSmoothEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.PolygonStippleEnable }
     * 
     */
    public ProfileCG.Technique.Pass.PolygonStippleEnable createProfileCGTechniquePassPolygonStippleEnable() {
        return new ProfileCG.Technique.Pass.PolygonStippleEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.RescaleNormalEnable }
     * 
     */
    public ProfileCG.Technique.Pass.RescaleNormalEnable createProfileCGTechniquePassRescaleNormalEnable() {
        return new ProfileCG.Technique.Pass.RescaleNormalEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.SampleAlphaToCoverageEnable }
     * 
     */
    public ProfileCG.Technique.Pass.SampleAlphaToCoverageEnable createProfileCGTechniquePassSampleAlphaToCoverageEnable() {
        return new ProfileCG.Technique.Pass.SampleAlphaToCoverageEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.SampleAlphaToOneEnable }
     * 
     */
    public ProfileCG.Technique.Pass.SampleAlphaToOneEnable createProfileCGTechniquePassSampleAlphaToOneEnable() {
        return new ProfileCG.Technique.Pass.SampleAlphaToOneEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.SampleCoverageEnable }
     * 
     */
    public ProfileCG.Technique.Pass.SampleCoverageEnable createProfileCGTechniquePassSampleCoverageEnable() {
        return new ProfileCG.Technique.Pass.SampleCoverageEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.ScissorTestEnable }
     * 
     */
    public ProfileCG.Technique.Pass.ScissorTestEnable createProfileCGTechniquePassScissorTestEnable() {
        return new ProfileCG.Technique.Pass.ScissorTestEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.StencilTestEnable }
     * 
     */
    public ProfileCG.Technique.Pass.StencilTestEnable createProfileCGTechniquePassStencilTestEnable() {
        return new ProfileCG.Technique.Pass.StencilTestEnable();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.Shader.CompilerTarget }
     * 
     */
    public ProfileCG.Technique.Pass.Shader.CompilerTarget createProfileCGTechniquePassShaderCompilerTarget() {
        return new ProfileCG.Technique.Pass.Shader.CompilerTarget();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.Shader.Name }
     * 
     */
    public ProfileCG.Technique.Pass.Shader.Name createProfileCGTechniquePassShaderName() {
        return new ProfileCG.Technique.Pass.Shader.Name();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.Shader.Bind.Param }
     * 
     */
    public ProfileCG.Technique.Pass.Shader.Bind.Param createProfileCGTechniquePassShaderBindParam() {
        return new ProfileCG.Technique.Pass.Shader.Bind.Param();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.StencilMaskSeparate.Face }
     * 
     */
    public ProfileCG.Technique.Pass.StencilMaskSeparate.Face createProfileCGTechniquePassStencilMaskSeparateFace() {
        return new ProfileCG.Technique.Pass.StencilMaskSeparate.Face();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.StencilMaskSeparate.Mask }
     * 
     */
    public ProfileCG.Technique.Pass.StencilMaskSeparate.Mask createProfileCGTechniquePassStencilMaskSeparateMask() {
        return new ProfileCG.Technique.Pass.StencilMaskSeparate.Mask();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.StencilOpSeparate.Face }
     * 
     */
    public ProfileCG.Technique.Pass.StencilOpSeparate.Face createProfileCGTechniquePassStencilOpSeparateFace() {
        return new ProfileCG.Technique.Pass.StencilOpSeparate.Face();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.StencilOpSeparate.Fail }
     * 
     */
    public ProfileCG.Technique.Pass.StencilOpSeparate.Fail createProfileCGTechniquePassStencilOpSeparateFail() {
        return new ProfileCG.Technique.Pass.StencilOpSeparate.Fail();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.StencilOpSeparate.Zfail }
     * 
     */
    public ProfileCG.Technique.Pass.StencilOpSeparate.Zfail createProfileCGTechniquePassStencilOpSeparateZfail() {
        return new ProfileCG.Technique.Pass.StencilOpSeparate.Zfail();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.StencilOpSeparate.Zpass }
     * 
     */
    public ProfileCG.Technique.Pass.StencilOpSeparate.Zpass createProfileCGTechniquePassStencilOpSeparateZpass() {
        return new ProfileCG.Technique.Pass.StencilOpSeparate.Zpass();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.StencilFuncSeparate.Front }
     * 
     */
    public ProfileCG.Technique.Pass.StencilFuncSeparate.Front createProfileCGTechniquePassStencilFuncSeparateFront() {
        return new ProfileCG.Technique.Pass.StencilFuncSeparate.Front();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.StencilFuncSeparate.Back }
     * 
     */
    public ProfileCG.Technique.Pass.StencilFuncSeparate.Back createProfileCGTechniquePassStencilFuncSeparateBack() {
        return new ProfileCG.Technique.Pass.StencilFuncSeparate.Back();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.StencilFuncSeparate.Ref }
     * 
     */
    public ProfileCG.Technique.Pass.StencilFuncSeparate.Ref createProfileCGTechniquePassStencilFuncSeparateRef() {
        return new ProfileCG.Technique.Pass.StencilFuncSeparate.Ref();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.StencilFuncSeparate.Mask }
     * 
     */
    public ProfileCG.Technique.Pass.StencilFuncSeparate.Mask createProfileCGTechniquePassStencilFuncSeparateMask() {
        return new ProfileCG.Technique.Pass.StencilFuncSeparate.Mask();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.StencilOp.Fail }
     * 
     */
    public ProfileCG.Technique.Pass.StencilOp.Fail createProfileCGTechniquePassStencilOpFail() {
        return new ProfileCG.Technique.Pass.StencilOp.Fail();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.StencilOp.Zfail }
     * 
     */
    public ProfileCG.Technique.Pass.StencilOp.Zfail createProfileCGTechniquePassStencilOpZfail() {
        return new ProfileCG.Technique.Pass.StencilOp.Zfail();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.StencilOp.Zpass }
     * 
     */
    public ProfileCG.Technique.Pass.StencilOp.Zpass createProfileCGTechniquePassStencilOpZpass() {
        return new ProfileCG.Technique.Pass.StencilOp.Zpass();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.StencilFunc.Func }
     * 
     */
    public ProfileCG.Technique.Pass.StencilFunc.Func createProfileCGTechniquePassStencilFuncFunc() {
        return new ProfileCG.Technique.Pass.StencilFunc.Func();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.StencilFunc.Ref }
     * 
     */
    public ProfileCG.Technique.Pass.StencilFunc.Ref createProfileCGTechniquePassStencilFuncRef() {
        return new ProfileCG.Technique.Pass.StencilFunc.Ref();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.StencilFunc.Mask }
     * 
     */
    public ProfileCG.Technique.Pass.StencilFunc.Mask createProfileCGTechniquePassStencilFuncMask() {
        return new ProfileCG.Technique.Pass.StencilFunc.Mask();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.PolygonMode.Face }
     * 
     */
    public ProfileCG.Technique.Pass.PolygonMode.Face createProfileCGTechniquePassPolygonModeFace() {
        return new ProfileCG.Technique.Pass.PolygonMode.Face();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.PolygonMode.Mode }
     * 
     */
    public ProfileCG.Technique.Pass.PolygonMode.Mode createProfileCGTechniquePassPolygonModeMode() {
        return new ProfileCG.Technique.Pass.PolygonMode.Mode();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.ColorMaterial.Face }
     * 
     */
    public ProfileCG.Technique.Pass.ColorMaterial.Face createProfileCGTechniquePassColorMaterialFace() {
        return new ProfileCG.Technique.Pass.ColorMaterial.Face();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.ColorMaterial.Mode }
     * 
     */
    public ProfileCG.Technique.Pass.ColorMaterial.Mode createProfileCGTechniquePassColorMaterialMode() {
        return new ProfileCG.Technique.Pass.ColorMaterial.Mode();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.BlendEquationSeparate.Rgb }
     * 
     */
    public ProfileCG.Technique.Pass.BlendEquationSeparate.Rgb createProfileCGTechniquePassBlendEquationSeparateRgb() {
        return new ProfileCG.Technique.Pass.BlendEquationSeparate.Rgb();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.BlendEquationSeparate.Alpha }
     * 
     */
    public ProfileCG.Technique.Pass.BlendEquationSeparate.Alpha createProfileCGTechniquePassBlendEquationSeparateAlpha() {
        return new ProfileCG.Technique.Pass.BlendEquationSeparate.Alpha();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.BlendFuncSeparate.SrcRgb }
     * 
     */
    public ProfileCG.Technique.Pass.BlendFuncSeparate.SrcRgb createProfileCGTechniquePassBlendFuncSeparateSrcRgb() {
        return new ProfileCG.Technique.Pass.BlendFuncSeparate.SrcRgb();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.BlendFuncSeparate.DestRgb }
     * 
     */
    public ProfileCG.Technique.Pass.BlendFuncSeparate.DestRgb createProfileCGTechniquePassBlendFuncSeparateDestRgb() {
        return new ProfileCG.Technique.Pass.BlendFuncSeparate.DestRgb();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.BlendFuncSeparate.SrcAlpha }
     * 
     */
    public ProfileCG.Technique.Pass.BlendFuncSeparate.SrcAlpha createProfileCGTechniquePassBlendFuncSeparateSrcAlpha() {
        return new ProfileCG.Technique.Pass.BlendFuncSeparate.SrcAlpha();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.BlendFuncSeparate.DestAlpha }
     * 
     */
    public ProfileCG.Technique.Pass.BlendFuncSeparate.DestAlpha createProfileCGTechniquePassBlendFuncSeparateDestAlpha() {
        return new ProfileCG.Technique.Pass.BlendFuncSeparate.DestAlpha();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.BlendFunc.Src }
     * 
     */
    public ProfileCG.Technique.Pass.BlendFunc.Src createProfileCGTechniquePassBlendFuncSrc() {
        return new ProfileCG.Technique.Pass.BlendFunc.Src();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.BlendFunc.Dest }
     * 
     */
    public ProfileCG.Technique.Pass.BlendFunc.Dest createProfileCGTechniquePassBlendFuncDest() {
        return new ProfileCG.Technique.Pass.BlendFunc.Dest();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.AlphaFunc.Func }
     * 
     */
    public ProfileCG.Technique.Pass.AlphaFunc.Func createProfileCGTechniquePassAlphaFuncFunc() {
        return new ProfileCG.Technique.Pass.AlphaFunc.Func();
    }

    /**
     * Create an instance of {@link ProfileCG.Technique.Pass.AlphaFunc.Value }
     * 
     */
    public ProfileCG.Technique.Pass.AlphaFunc.Value createProfileCGTechniquePassAlphaFuncValue() {
        return new ProfileCG.Technique.Pass.AlphaFunc.Value();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Setparam }
     * 
     */
    public ProfileGLES.Technique.Setparam createProfileGLESTechniqueSetparam() {
        return new ProfileGLES.Technique.Setparam();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.ClearColor }
     * 
     */
    public ProfileGLES.Technique.Pass.ClearColor createProfileGLESTechniquePassClearColor() {
        return new ProfileGLES.Technique.Pass.ClearColor();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.ClearStencil }
     * 
     */
    public ProfileGLES.Technique.Pass.ClearStencil createProfileGLESTechniquePassClearStencil() {
        return new ProfileGLES.Technique.Pass.ClearStencil();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.ClearDepth }
     * 
     */
    public ProfileGLES.Technique.Pass.ClearDepth createProfileGLESTechniquePassClearDepth() {
        return new ProfileGLES.Technique.Pass.ClearDepth();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.ClipPlane }
     * 
     */
    public ProfileGLES.Technique.Pass.ClipPlane createProfileGLESTechniquePassClipPlane() {
        return new ProfileGLES.Technique.Pass.ClipPlane();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.ColorMask }
     * 
     */
    public ProfileGLES.Technique.Pass.ColorMask createProfileGLESTechniquePassColorMask() {
        return new ProfileGLES.Technique.Pass.ColorMask();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.CullFace }
     * 
     */
    public ProfileGLES.Technique.Pass.CullFace createProfileGLESTechniquePassCullFace() {
        return new ProfileGLES.Technique.Pass.CullFace();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.DepthFunc }
     * 
     */
    public ProfileGLES.Technique.Pass.DepthFunc createProfileGLESTechniquePassDepthFunc() {
        return new ProfileGLES.Technique.Pass.DepthFunc();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.DepthMask }
     * 
     */
    public ProfileGLES.Technique.Pass.DepthMask createProfileGLESTechniquePassDepthMask() {
        return new ProfileGLES.Technique.Pass.DepthMask();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.DepthRange }
     * 
     */
    public ProfileGLES.Technique.Pass.DepthRange createProfileGLESTechniquePassDepthRange() {
        return new ProfileGLES.Technique.Pass.DepthRange();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.FogColor }
     * 
     */
    public ProfileGLES.Technique.Pass.FogColor createProfileGLESTechniquePassFogColor() {
        return new ProfileGLES.Technique.Pass.FogColor();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.FogDensity }
     * 
     */
    public ProfileGLES.Technique.Pass.FogDensity createProfileGLESTechniquePassFogDensity() {
        return new ProfileGLES.Technique.Pass.FogDensity();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.FogMode }
     * 
     */
    public ProfileGLES.Technique.Pass.FogMode createProfileGLESTechniquePassFogMode() {
        return new ProfileGLES.Technique.Pass.FogMode();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.FogStart }
     * 
     */
    public ProfileGLES.Technique.Pass.FogStart createProfileGLESTechniquePassFogStart() {
        return new ProfileGLES.Technique.Pass.FogStart();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.FogEnd }
     * 
     */
    public ProfileGLES.Technique.Pass.FogEnd createProfileGLESTechniquePassFogEnd() {
        return new ProfileGLES.Technique.Pass.FogEnd();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.FrontFace }
     * 
     */
    public ProfileGLES.Technique.Pass.FrontFace createProfileGLESTechniquePassFrontFace() {
        return new ProfileGLES.Technique.Pass.FrontFace();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.TexturePipeline }
     * 
     */
    public ProfileGLES.Technique.Pass.TexturePipeline createProfileGLESTechniquePassTexturePipeline() {
        return new ProfileGLES.Technique.Pass.TexturePipeline();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.LogicOp }
     * 
     */
    public ProfileGLES.Technique.Pass.LogicOp createProfileGLESTechniquePassLogicOp() {
        return new ProfileGLES.Technique.Pass.LogicOp();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.LightAmbient }
     * 
     */
    public ProfileGLES.Technique.Pass.LightAmbient createProfileGLESTechniquePassLightAmbient() {
        return new ProfileGLES.Technique.Pass.LightAmbient();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.LightDiffuse }
     * 
     */
    public ProfileGLES.Technique.Pass.LightDiffuse createProfileGLESTechniquePassLightDiffuse() {
        return new ProfileGLES.Technique.Pass.LightDiffuse();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.LightSpecular }
     * 
     */
    public ProfileGLES.Technique.Pass.LightSpecular createProfileGLESTechniquePassLightSpecular() {
        return new ProfileGLES.Technique.Pass.LightSpecular();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.LightPosition }
     * 
     */
    public ProfileGLES.Technique.Pass.LightPosition createProfileGLESTechniquePassLightPosition() {
        return new ProfileGLES.Technique.Pass.LightPosition();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.LightConstantAttenuation }
     * 
     */
    public ProfileGLES.Technique.Pass.LightConstantAttenuation createProfileGLESTechniquePassLightConstantAttenuation() {
        return new ProfileGLES.Technique.Pass.LightConstantAttenuation();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.LightLinearAttenutation }
     * 
     */
    public ProfileGLES.Technique.Pass.LightLinearAttenutation createProfileGLESTechniquePassLightLinearAttenutation() {
        return new ProfileGLES.Technique.Pass.LightLinearAttenutation();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.LightQuadraticAttenuation }
     * 
     */
    public ProfileGLES.Technique.Pass.LightQuadraticAttenuation createProfileGLESTechniquePassLightQuadraticAttenuation() {
        return new ProfileGLES.Technique.Pass.LightQuadraticAttenuation();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.LightSpotCutoff }
     * 
     */
    public ProfileGLES.Technique.Pass.LightSpotCutoff createProfileGLESTechniquePassLightSpotCutoff() {
        return new ProfileGLES.Technique.Pass.LightSpotCutoff();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.LightSpotDirection }
     * 
     */
    public ProfileGLES.Technique.Pass.LightSpotDirection createProfileGLESTechniquePassLightSpotDirection() {
        return new ProfileGLES.Technique.Pass.LightSpotDirection();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.LightSpotExponent }
     * 
     */
    public ProfileGLES.Technique.Pass.LightSpotExponent createProfileGLESTechniquePassLightSpotExponent() {
        return new ProfileGLES.Technique.Pass.LightSpotExponent();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.LightModelAmbient }
     * 
     */
    public ProfileGLES.Technique.Pass.LightModelAmbient createProfileGLESTechniquePassLightModelAmbient() {
        return new ProfileGLES.Technique.Pass.LightModelAmbient();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.LineWidth }
     * 
     */
    public ProfileGLES.Technique.Pass.LineWidth createProfileGLESTechniquePassLineWidth() {
        return new ProfileGLES.Technique.Pass.LineWidth();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.MaterialAmbient }
     * 
     */
    public ProfileGLES.Technique.Pass.MaterialAmbient createProfileGLESTechniquePassMaterialAmbient() {
        return new ProfileGLES.Technique.Pass.MaterialAmbient();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.MaterialDiffuse }
     * 
     */
    public ProfileGLES.Technique.Pass.MaterialDiffuse createProfileGLESTechniquePassMaterialDiffuse() {
        return new ProfileGLES.Technique.Pass.MaterialDiffuse();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.MaterialEmission }
     * 
     */
    public ProfileGLES.Technique.Pass.MaterialEmission createProfileGLESTechniquePassMaterialEmission() {
        return new ProfileGLES.Technique.Pass.MaterialEmission();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.MaterialShininess }
     * 
     */
    public ProfileGLES.Technique.Pass.MaterialShininess createProfileGLESTechniquePassMaterialShininess() {
        return new ProfileGLES.Technique.Pass.MaterialShininess();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.MaterialSpecular }
     * 
     */
    public ProfileGLES.Technique.Pass.MaterialSpecular createProfileGLESTechniquePassMaterialSpecular() {
        return new ProfileGLES.Technique.Pass.MaterialSpecular();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.ModelViewMatrix }
     * 
     */
    public ProfileGLES.Technique.Pass.ModelViewMatrix createProfileGLESTechniquePassModelViewMatrix() {
        return new ProfileGLES.Technique.Pass.ModelViewMatrix();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.PointDistanceAttenuation }
     * 
     */
    public ProfileGLES.Technique.Pass.PointDistanceAttenuation createProfileGLESTechniquePassPointDistanceAttenuation() {
        return new ProfileGLES.Technique.Pass.PointDistanceAttenuation();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.PointFadeThresholdSize }
     * 
     */
    public ProfileGLES.Technique.Pass.PointFadeThresholdSize createProfileGLESTechniquePassPointFadeThresholdSize() {
        return new ProfileGLES.Technique.Pass.PointFadeThresholdSize();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.PointSize }
     * 
     */
    public ProfileGLES.Technique.Pass.PointSize createProfileGLESTechniquePassPointSize() {
        return new ProfileGLES.Technique.Pass.PointSize();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.PointSizeMin }
     * 
     */
    public ProfileGLES.Technique.Pass.PointSizeMin createProfileGLESTechniquePassPointSizeMin() {
        return new ProfileGLES.Technique.Pass.PointSizeMin();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.PointSizeMax }
     * 
     */
    public ProfileGLES.Technique.Pass.PointSizeMax createProfileGLESTechniquePassPointSizeMax() {
        return new ProfileGLES.Technique.Pass.PointSizeMax();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.PolygonOffset }
     * 
     */
    public ProfileGLES.Technique.Pass.PolygonOffset createProfileGLESTechniquePassPolygonOffset() {
        return new ProfileGLES.Technique.Pass.PolygonOffset();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.ProjectionMatrix }
     * 
     */
    public ProfileGLES.Technique.Pass.ProjectionMatrix createProfileGLESTechniquePassProjectionMatrix() {
        return new ProfileGLES.Technique.Pass.ProjectionMatrix();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.Scissor }
     * 
     */
    public ProfileGLES.Technique.Pass.Scissor createProfileGLESTechniquePassScissor() {
        return new ProfileGLES.Technique.Pass.Scissor();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.ShadeModel }
     * 
     */
    public ProfileGLES.Technique.Pass.ShadeModel createProfileGLESTechniquePassShadeModel() {
        return new ProfileGLES.Technique.Pass.ShadeModel();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.StencilMask }
     * 
     */
    public ProfileGLES.Technique.Pass.StencilMask createProfileGLESTechniquePassStencilMask() {
        return new ProfileGLES.Technique.Pass.StencilMask();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.AlphaTestEnable }
     * 
     */
    public ProfileGLES.Technique.Pass.AlphaTestEnable createProfileGLESTechniquePassAlphaTestEnable() {
        return new ProfileGLES.Technique.Pass.AlphaTestEnable();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.BlendEnable }
     * 
     */
    public ProfileGLES.Technique.Pass.BlendEnable createProfileGLESTechniquePassBlendEnable() {
        return new ProfileGLES.Technique.Pass.BlendEnable();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.ClipPlaneEnable }
     * 
     */
    public ProfileGLES.Technique.Pass.ClipPlaneEnable createProfileGLESTechniquePassClipPlaneEnable() {
        return new ProfileGLES.Technique.Pass.ClipPlaneEnable();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.ColorLogicOpEnable }
     * 
     */
    public ProfileGLES.Technique.Pass.ColorLogicOpEnable createProfileGLESTechniquePassColorLogicOpEnable() {
        return new ProfileGLES.Technique.Pass.ColorLogicOpEnable();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.ColorMaterialEnable }
     * 
     */
    public ProfileGLES.Technique.Pass.ColorMaterialEnable createProfileGLESTechniquePassColorMaterialEnable() {
        return new ProfileGLES.Technique.Pass.ColorMaterialEnable();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.CullFaceEnable }
     * 
     */
    public ProfileGLES.Technique.Pass.CullFaceEnable createProfileGLESTechniquePassCullFaceEnable() {
        return new ProfileGLES.Technique.Pass.CullFaceEnable();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.DepthTestEnable }
     * 
     */
    public ProfileGLES.Technique.Pass.DepthTestEnable createProfileGLESTechniquePassDepthTestEnable() {
        return new ProfileGLES.Technique.Pass.DepthTestEnable();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.DitherEnable }
     * 
     */
    public ProfileGLES.Technique.Pass.DitherEnable createProfileGLESTechniquePassDitherEnable() {
        return new ProfileGLES.Technique.Pass.DitherEnable();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.FogEnable }
     * 
     */
    public ProfileGLES.Technique.Pass.FogEnable createProfileGLESTechniquePassFogEnable() {
        return new ProfileGLES.Technique.Pass.FogEnable();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.TexturePipelineEnable }
     * 
     */
    public ProfileGLES.Technique.Pass.TexturePipelineEnable createProfileGLESTechniquePassTexturePipelineEnable() {
        return new ProfileGLES.Technique.Pass.TexturePipelineEnable();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.LightEnable }
     * 
     */
    public ProfileGLES.Technique.Pass.LightEnable createProfileGLESTechniquePassLightEnable() {
        return new ProfileGLES.Technique.Pass.LightEnable();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.LightingEnable }
     * 
     */
    public ProfileGLES.Technique.Pass.LightingEnable createProfileGLESTechniquePassLightingEnable() {
        return new ProfileGLES.Technique.Pass.LightingEnable();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.LightModelTwoSideEnable }
     * 
     */
    public ProfileGLES.Technique.Pass.LightModelTwoSideEnable createProfileGLESTechniquePassLightModelTwoSideEnable() {
        return new ProfileGLES.Technique.Pass.LightModelTwoSideEnable();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.LineSmoothEnable }
     * 
     */
    public ProfileGLES.Technique.Pass.LineSmoothEnable createProfileGLESTechniquePassLineSmoothEnable() {
        return new ProfileGLES.Technique.Pass.LineSmoothEnable();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.MultisampleEnable }
     * 
     */
    public ProfileGLES.Technique.Pass.MultisampleEnable createProfileGLESTechniquePassMultisampleEnable() {
        return new ProfileGLES.Technique.Pass.MultisampleEnable();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.NormalizeEnable }
     * 
     */
    public ProfileGLES.Technique.Pass.NormalizeEnable createProfileGLESTechniquePassNormalizeEnable() {
        return new ProfileGLES.Technique.Pass.NormalizeEnable();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.PointSmoothEnable }
     * 
     */
    public ProfileGLES.Technique.Pass.PointSmoothEnable createProfileGLESTechniquePassPointSmoothEnable() {
        return new ProfileGLES.Technique.Pass.PointSmoothEnable();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.PolygonOffsetFillEnable }
     * 
     */
    public ProfileGLES.Technique.Pass.PolygonOffsetFillEnable createProfileGLESTechniquePassPolygonOffsetFillEnable() {
        return new ProfileGLES.Technique.Pass.PolygonOffsetFillEnable();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.RescaleNormalEnable }
     * 
     */
    public ProfileGLES.Technique.Pass.RescaleNormalEnable createProfileGLESTechniquePassRescaleNormalEnable() {
        return new ProfileGLES.Technique.Pass.RescaleNormalEnable();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.SampleAlphaToCoverageEnable }
     * 
     */
    public ProfileGLES.Technique.Pass.SampleAlphaToCoverageEnable createProfileGLESTechniquePassSampleAlphaToCoverageEnable() {
        return new ProfileGLES.Technique.Pass.SampleAlphaToCoverageEnable();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.SampleAlphaToOneEnable }
     * 
     */
    public ProfileGLES.Technique.Pass.SampleAlphaToOneEnable createProfileGLESTechniquePassSampleAlphaToOneEnable() {
        return new ProfileGLES.Technique.Pass.SampleAlphaToOneEnable();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.SampleCoverageEnable }
     * 
     */
    public ProfileGLES.Technique.Pass.SampleCoverageEnable createProfileGLESTechniquePassSampleCoverageEnable() {
        return new ProfileGLES.Technique.Pass.SampleCoverageEnable();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.ScissorTestEnable }
     * 
     */
    public ProfileGLES.Technique.Pass.ScissorTestEnable createProfileGLESTechniquePassScissorTestEnable() {
        return new ProfileGLES.Technique.Pass.ScissorTestEnable();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.StencilTestEnable }
     * 
     */
    public ProfileGLES.Technique.Pass.StencilTestEnable createProfileGLESTechniquePassStencilTestEnable() {
        return new ProfileGLES.Technique.Pass.StencilTestEnable();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.StencilOp.Fail }
     * 
     */
    public ProfileGLES.Technique.Pass.StencilOp.Fail createProfileGLESTechniquePassStencilOpFail() {
        return new ProfileGLES.Technique.Pass.StencilOp.Fail();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.StencilOp.Zfail }
     * 
     */
    public ProfileGLES.Technique.Pass.StencilOp.Zfail createProfileGLESTechniquePassStencilOpZfail() {
        return new ProfileGLES.Technique.Pass.StencilOp.Zfail();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.StencilOp.Zpass }
     * 
     */
    public ProfileGLES.Technique.Pass.StencilOp.Zpass createProfileGLESTechniquePassStencilOpZpass() {
        return new ProfileGLES.Technique.Pass.StencilOp.Zpass();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.StencilFunc.Func }
     * 
     */
    public ProfileGLES.Technique.Pass.StencilFunc.Func createProfileGLESTechniquePassStencilFuncFunc() {
        return new ProfileGLES.Technique.Pass.StencilFunc.Func();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.StencilFunc.Ref }
     * 
     */
    public ProfileGLES.Technique.Pass.StencilFunc.Ref createProfileGLESTechniquePassStencilFuncRef() {
        return new ProfileGLES.Technique.Pass.StencilFunc.Ref();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.StencilFunc.Mask }
     * 
     */
    public ProfileGLES.Technique.Pass.StencilFunc.Mask createProfileGLESTechniquePassStencilFuncMask() {
        return new ProfileGLES.Technique.Pass.StencilFunc.Mask();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.BlendFunc.Src }
     * 
     */
    public ProfileGLES.Technique.Pass.BlendFunc.Src createProfileGLESTechniquePassBlendFuncSrc() {
        return new ProfileGLES.Technique.Pass.BlendFunc.Src();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.BlendFunc.Dest }
     * 
     */
    public ProfileGLES.Technique.Pass.BlendFunc.Dest createProfileGLESTechniquePassBlendFuncDest() {
        return new ProfileGLES.Technique.Pass.BlendFunc.Dest();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.AlphaFunc.Func }
     * 
     */
    public ProfileGLES.Technique.Pass.AlphaFunc.Func createProfileGLESTechniquePassAlphaFuncFunc() {
        return new ProfileGLES.Technique.Pass.AlphaFunc.Func();
    }

    /**
     * Create an instance of {@link ProfileGLES.Technique.Pass.AlphaFunc.Value }
     * 
     */
    public ProfileGLES.Technique.Pass.AlphaFunc.Value createProfileGLESTechniquePassAlphaFuncValue() {
        return new ProfileGLES.Technique.Pass.AlphaFunc.Value();
    }

    /**
     * Create an instance of {@link ProfileCOMMON.Technique.Constant }
     * 
     */
    public ProfileCOMMON.Technique.Constant createProfileCOMMONTechniqueConstant() {
        return new ProfileCOMMON.Technique.Constant();
    }

    /**
     * Create an instance of {@link ProfileCOMMON.Technique.Lambert }
     * 
     */
    public ProfileCOMMON.Technique.Lambert createProfileCOMMONTechniqueLambert() {
        return new ProfileCOMMON.Technique.Lambert();
    }

    /**
     * Create an instance of {@link ProfileCOMMON.Technique.Phong }
     * 
     */
    public ProfileCOMMON.Technique.Phong createProfileCOMMONTechniquePhong() {
        return new ProfileCOMMON.Technique.Phong();
    }

    /**
     * Create an instance of {@link ProfileCOMMON.Technique.Blinn }
     * 
     */
    public ProfileCOMMON.Technique.Blinn createProfileCOMMONTechniqueBlinn() {
        return new ProfileCOMMON.Technique.Blinn();
    }

    /**
     * Create an instance of {@link VisualScene.EvaluateScene.Render }
     * 
     */
    public VisualScene.EvaluateScene.Render createVisualSceneEvaluateSceneRender() {
        return new VisualScene.EvaluateScene.Render();
    }

    /**
     * Create an instance of {@link RigidBody.TechniqueCommon.Dynamic }
     * 
     */
    public RigidBody.TechniqueCommon.Dynamic createRigidBodyTechniqueCommonDynamic() {
        return new RigidBody.TechniqueCommon.Dynamic();
    }

    /**
     * Create an instance of {@link RigidBody.TechniqueCommon.MassFrame }
     * 
     */
    public RigidBody.TechniqueCommon.MassFrame createRigidBodyTechniqueCommonMassFrame() {
        return new RigidBody.TechniqueCommon.MassFrame();
    }

    /**
     * Create an instance of {@link RigidBody.TechniqueCommon.Shape.Hollow }
     * 
     */
    public RigidBody.TechniqueCommon.Shape.Hollow createRigidBodyTechniqueCommonShapeHollow() {
        return new RigidBody.TechniqueCommon.Shape.Hollow();
    }

    /**
     * Create an instance of {@link Light.TechniqueCommon.Ambient }
     * 
     */
    public Light.TechniqueCommon.Ambient createLightTechniqueCommonAmbient() {
        return new Light.TechniqueCommon.Ambient();
    }

    /**
     * Create an instance of {@link Light.TechniqueCommon.Directional }
     * 
     */
    public Light.TechniqueCommon.Directional createLightTechniqueCommonDirectional() {
        return new Light.TechniqueCommon.Directional();
    }

    /**
     * Create an instance of {@link Light.TechniqueCommon.Point }
     * 
     */
    public Light.TechniqueCommon.Point createLightTechniqueCommonPoint() {
        return new Light.TechniqueCommon.Point();
    }

    /**
     * Create an instance of {@link Light.TechniqueCommon.Spot }
     * 
     */
    public Light.TechniqueCommon.Spot createLightTechniqueCommonSpot() {
        return new Light.TechniqueCommon.Spot();
    }

    /**
     * Create an instance of {@link InstanceRigidBody.TechniqueCommon.Dynamic }
     * 
     */
    public InstanceRigidBody.TechniqueCommon.Dynamic createInstanceRigidBodyTechniqueCommonDynamic() {
        return new InstanceRigidBody.TechniqueCommon.Dynamic();
    }

    /**
     * Create an instance of {@link InstanceRigidBody.TechniqueCommon.MassFrame }
     * 
     */
    public InstanceRigidBody.TechniqueCommon.MassFrame createInstanceRigidBodyTechniqueCommonMassFrame() {
        return new InstanceRigidBody.TechniqueCommon.MassFrame();
    }

    /**
     * Create an instance of {@link InstanceRigidBody.TechniqueCommon.Shape.Hollow }
     * 
     */
    public InstanceRigidBody.TechniqueCommon.Shape.Hollow createInstanceRigidBodyTechniqueCommonShapeHollow() {
        return new InstanceRigidBody.TechniqueCommon.Shape.Hollow();
    }

    /**
     * Create an instance of {@link RigidConstraint.TechniqueCommon.Enabled }
     * 
     */
    public RigidConstraint.TechniqueCommon.Enabled createRigidConstraintTechniqueCommonEnabled() {
        return new RigidConstraint.TechniqueCommon.Enabled();
    }

    /**
     * Create an instance of {@link RigidConstraint.TechniqueCommon.Interpenetrate }
     * 
     */
    public RigidConstraint.TechniqueCommon.Interpenetrate createRigidConstraintTechniqueCommonInterpenetrate() {
        return new RigidConstraint.TechniqueCommon.Interpenetrate();
    }

    /**
     * Create an instance of {@link RigidConstraint.TechniqueCommon.Spring.Angular }
     * 
     */
    public RigidConstraint.TechniqueCommon.Spring.Angular createRigidConstraintTechniqueCommonSpringAngular() {
        return new RigidConstraint.TechniqueCommon.Spring.Angular();
    }

    /**
     * Create an instance of {@link RigidConstraint.TechniqueCommon.Spring.Linear }
     * 
     */
    public RigidConstraint.TechniqueCommon.Spring.Linear createRigidConstraintTechniqueCommonSpringLinear() {
        return new RigidConstraint.TechniqueCommon.Spring.Linear();
    }

    /**
     * Create an instance of {@link RigidConstraint.TechniqueCommon.Limits.SwingConeAndTwist }
     * 
     */
    public RigidConstraint.TechniqueCommon.Limits.SwingConeAndTwist createRigidConstraintTechniqueCommonLimitsSwingConeAndTwist() {
        return new RigidConstraint.TechniqueCommon.Limits.SwingConeAndTwist();
    }

    /**
     * Create an instance of {@link RigidConstraint.TechniqueCommon.Limits.Linear }
     * 
     */
    public RigidConstraint.TechniqueCommon.Limits.Linear createRigidConstraintTechniqueCommonLimitsLinear() {
        return new RigidConstraint.TechniqueCommon.Limits.Linear();
    }

    /**
     * Create an instance of {@link Camera.Optics.TechniqueCommon.Orthographic }
     * 
     */
    public Camera.Optics.TechniqueCommon.Orthographic createCameraOpticsTechniqueCommonOrthographic() {
        return new Camera.Optics.TechniqueCommon.Orthographic();
    }

    /**
     * Create an instance of {@link Camera.Optics.TechniqueCommon.Perspective }
     * 
     */
    public Camera.Optics.TechniqueCommon.Perspective createCameraOpticsTechniqueCommonPerspective() {
        return new Camera.Optics.TechniqueCommon.Perspective();
    }

    /**
     * Create an instance of {@link ProfileGLSL.Technique.Pass.Shader.CompilerTarget }
     * 
     */
    public ProfileGLSL.Technique.Pass.Shader.CompilerTarget createProfileGLSLTechniquePassShaderCompilerTarget() {
        return new ProfileGLSL.Technique.Pass.Shader.CompilerTarget();
    }

    /**
     * Create an instance of {@link ProfileGLSL.Technique.Pass.Shader.Name }
     * 
     */
    public ProfileGLSL.Technique.Pass.Shader.Name createProfileGLSLTechniquePassShaderName() {
        return new ProfileGLSL.Technique.Pass.Shader.Name();
    }

    /**
     * Create an instance of {@link ProfileGLSL.Technique.Pass.Shader.Bind.Param }
     * 
     */
    public ProfileGLSL.Technique.Pass.Shader.Bind.Param createProfileGLSLTechniquePassShaderBindParam() {
        return new ProfileGLSL.Technique.Pass.Shader.Bind.Param();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TargetableFloat3 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "translate")
    public JAXBElement<TargetableFloat3> createTranslate(TargetableFloat3 value) {
        return new JAXBElement<TargetableFloat3>(_Translate_QNAME, TargetableFloat3 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TargetableFloat3 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "scale")
    public JAXBElement<TargetableFloat3> createScale(TargetableFloat3 value) {
        return new JAXBElement<TargetableFloat3>(_Scale_QNAME, TargetableFloat3 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProfileGLSL }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "profile_GLSL", substitutionHeadNamespace = "http://www.collada.org/2005/11/COLLADASchema", substitutionHeadName = "fx_profile_abstract")
    public JAXBElement<ProfileGLSL> createProfileGLSL(ProfileGLSL value) {
        return new JAXBElement<ProfileGLSL>(_ProfileGLSL_QNAME, ProfileGLSL.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InstanceWithExtra }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "instance_physics_material")
    public JAXBElement<InstanceWithExtra> createInstancePhysicsMaterial(InstanceWithExtra value) {
        return new JAXBElement<InstanceWithExtra>(_InstancePhysicsMaterial_QNAME, InstanceWithExtra.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProfileGLES }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "profile_GLES", substitutionHeadNamespace = "http://www.collada.org/2005/11/COLLADASchema", substitutionHeadName = "fx_profile_abstract")
    public JAXBElement<ProfileGLES> createProfileGLES(ProfileGLES value) {
        return new JAXBElement<ProfileGLES>(_ProfileGLES_QNAME, ProfileGLES.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link BigInteger }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "p")
    public JAXBElement<List<BigInteger>> createP(List<BigInteger> value) {
        return new JAXBElement<List<BigInteger>>(_P_QNAME, ((Class) List.class), null, ((List<BigInteger> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fx_profile_abstract")
    public JAXBElement<Object> createFxProfileAbstract(Object value) {
        return new JAXBElement<Object>(_FxProfileAbstract_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InstanceWithExtra }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "instance_force_field")
    public JAXBElement<InstanceWithExtra> createInstanceForceField(InstanceWithExtra value) {
        return new JAXBElement<InstanceWithExtra>(_InstanceForceField_QNAME, InstanceWithExtra.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InstanceWithExtra }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "instance_camera")
    public JAXBElement<InstanceWithExtra> createInstanceCamera(InstanceWithExtra value) {
        return new JAXBElement<InstanceWithExtra>(_InstanceCamera_QNAME, InstanceWithExtra.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProfileCG }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "profile_CG", substitutionHeadNamespace = "http://www.collada.org/2005/11/COLLADASchema", substitutionHeadName = "fx_profile_abstract")
    public JAXBElement<ProfileCG> createProfileCG(ProfileCG value) {
        return new JAXBElement<ProfileCG>(_ProfileCG_QNAME, ProfileCG.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "gl_hook_abstract")
    public JAXBElement<Object> createGlHookAbstract(Object value) {
        return new JAXBElement<Object>(_GlHookAbstract_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProfileCOMMON }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "profile_COMMON", substitutionHeadNamespace = "http://www.collada.org/2005/11/COLLADASchema", substitutionHeadName = "fx_profile_abstract")
    public JAXBElement<ProfileCOMMON> createProfileCOMMON(ProfileCOMMON value) {
        return new JAXBElement<ProfileCOMMON>(_ProfileCOMMON_QNAME, ProfileCOMMON.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InstanceWithExtra }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "instance_node")
    public JAXBElement<InstanceWithExtra> createInstanceNode(InstanceWithExtra value) {
        return new JAXBElement<InstanceWithExtra>(_InstanceNode_QNAME, InstanceWithExtra.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InstanceWithExtra }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "instance_light")
    public JAXBElement<InstanceWithExtra> createInstanceLight(InstanceWithExtra value) {
        return new JAXBElement<InstanceWithExtra>(_InstanceLight_QNAME, InstanceWithExtra.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int3x1", scope = CgSetarrayType.class)
    public JAXBElement<List<Integer>> createCgSetarrayTypeInt3X1(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt3X1_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int3x2", scope = CgSetarrayType.class)
    public JAXBElement<List<Integer>> createCgSetarrayTypeInt3X2(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt3X2_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int3x3", scope = CgSetarrayType.class)
    public JAXBElement<List<Integer>> createCgSetarrayTypeInt3X3(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt3X3_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgSetuserType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "usertype", scope = CgSetarrayType.class)
    public JAXBElement<CgSetuserType> createCgSetarrayTypeUsertype(CgSetuserType value) {
        return new JAXBElement<CgSetuserType>(_CgSetarrayTypeUsertype_QNAME, CgSetuserType.class, CgSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half1x4", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeHalf1X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf1X4_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half1x3", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeHalf1X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf1X3_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half1x2", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeHalf1X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf1X2_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half2", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeHalf2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf2_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half1x1", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeHalf1X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf1X1_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link java.lang.Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half1", scope = CgSetarrayType.class)
    public JAXBElement<java.lang.Float> createCgSetarrayTypeHalf1(java.lang.Float value) {
        return new JAXBElement<java.lang.Float>(_CgSetarrayTypeHalf1_QNAME, java.lang.Float.class, CgSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link java.lang.Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed", scope = CgSetarrayType.class)
    public JAXBElement<java.lang.Float> createCgSetarrayTypeFixed(java.lang.Float value) {
        return new JAXBElement<java.lang.Float>(_CgSetarrayTypeFixed_QNAME, java.lang.Float.class, CgSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half4", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeHalf4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf4_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half3", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeHalf3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf3_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link java.lang.Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float", scope = CgSetarrayType.class)
    public JAXBElement<java.lang.Float> createCgSetarrayTypeFloat(java.lang.Float value) {
        return new JAXBElement<java.lang.Float>(_CgSetarrayTypeFloat_QNAME, java.lang.Float.class, CgSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int3x4", scope = CgSetarrayType.class)
    public JAXBElement<List<Integer>> createCgSetarrayTypeInt3X4(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt3X4_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed3x2", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFixed3X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed3X2_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed3x1", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFixed3X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed3X1_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed3x4", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFixed3X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed3X4_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed3x3", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFixed3X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed3X3_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float3x4", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFloat3X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat3X4_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float3x3", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFloat3X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat3X3_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool2x4", scope = CgSetarrayType.class)
    public JAXBElement<List<Boolean>> createCgSetarrayTypeBool2X4(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool2X4_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float3x2", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFloat3X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat3X2_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float3x1", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFloat3X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat3X1_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool2x2", scope = CgSetarrayType.class)
    public JAXBElement<List<Boolean>> createCgSetarrayTypeBool2X2(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool2X2_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool2x3", scope = CgSetarrayType.class)
    public JAXBElement<List<Boolean>> createCgSetarrayTypeBool2X3(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool2X3_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool2x1", scope = CgSetarrayType.class)
    public JAXBElement<List<Boolean>> createCgSetarrayTypeBool2X1(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool2X1_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int", scope = CgSetarrayType.class)
    public JAXBElement<Integer> createCgSetarrayTypeInt(Integer value) {
        return new JAXBElement<Integer>(_CgSetarrayTypeInt_QNAME, Integer.class, CgSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int4x1", scope = CgSetarrayType.class)
    public JAXBElement<List<Integer>> createCgSetarrayTypeInt4X1(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt4X1_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int4x2", scope = CgSetarrayType.class)
    public JAXBElement<List<Integer>> createCgSetarrayTypeInt4X2(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt4X2_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half4x2", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeHalf4X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf4X2_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half4x1", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeHalf4X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf4X1_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half4x4", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeHalf4X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf4X4_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgSamplerRECT }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "samplerRECT", scope = CgSetarrayType.class)
    public JAXBElement<CgSamplerRECT> createCgSetarrayTypeSamplerRECT(CgSamplerRECT value) {
        return new JAXBElement<CgSamplerRECT>(_CgSetarrayTypeSamplerRECT_QNAME, CgSamplerRECT.class, CgSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half4x3", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeHalf4X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf4X3_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool", scope = CgSetarrayType.class)
    public JAXBElement<Boolean> createCgSetarrayTypeBool(Boolean value) {
        return new JAXBElement<Boolean>(_CgSetarrayTypeBool_QNAME, Boolean.class, CgSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "string", scope = CgSetarrayType.class)
    public JAXBElement<String> createCgSetarrayTypeString(String value) {
        return new JAXBElement<String>(_CgSetarrayTypeString_QNAME, String.class, CgSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgSetarrayType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "array", scope = CgSetarrayType.class)
    public JAXBElement<CgSetarrayType> createCgSetarrayTypeArray(CgSetarrayType value) {
        return new JAXBElement<CgSetarrayType>(_CgSetarrayTypeArray_QNAME, CgSetarrayType.class, CgSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int4x3", scope = CgSetarrayType.class)
    public JAXBElement<List<Integer>> createCgSetarrayTypeInt4X3(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt4X3_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed2x3", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFixed2X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed2X3_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int4x4", scope = CgSetarrayType.class)
    public JAXBElement<List<Integer>> createCgSetarrayTypeInt4X4(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt4X4_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed2x2", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFixed2X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed2X2_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed2x4", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFixed2X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed2X4_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float4x3", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFloat4X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat4X3_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float4x2", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFloat4X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat4X2_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool3x3", scope = CgSetarrayType.class)
    public JAXBElement<List<Boolean>> createCgSetarrayTypeBool3X3(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool3X3_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float4x1", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFloat4X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat4X1_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed2x1", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFixed2X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed2X1_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool3x4", scope = CgSetarrayType.class)
    public JAXBElement<List<Boolean>> createCgSetarrayTypeBool3X4(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool3X4_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool3x1", scope = CgSetarrayType.class)
    public JAXBElement<List<Boolean>> createCgSetarrayTypeBool3X1(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool3X1_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool3x2", scope = CgSetarrayType.class)
    public JAXBElement<List<Boolean>> createCgSetarrayTypeBool3X2(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool3X2_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float4x4", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFloat4X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat4X4_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgSurfaceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "surface", scope = CgSetarrayType.class)
    public JAXBElement<CgSurfaceType> createCgSetarrayTypeSurface(CgSurfaceType value) {
        return new JAXBElement<CgSurfaceType>(_CgSetarrayTypeSurface_QNAME, CgSurfaceType.class, CgSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int1x2", scope = CgSetarrayType.class)
    public JAXBElement<List<Integer>> createCgSetarrayTypeInt1X2(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt1X2_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int1x3", scope = CgSetarrayType.class)
    public JAXBElement<List<Integer>> createCgSetarrayTypeInt1X3(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt1X3_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int1x4", scope = CgSetarrayType.class)
    public JAXBElement<List<Integer>> createCgSetarrayTypeInt1X4(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt1X4_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgSampler3D }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "sampler3D", scope = CgSetarrayType.class)
    public JAXBElement<CgSampler3D> createCgSetarrayTypeSampler3D(CgSampler3D value) {
        return new JAXBElement<CgSampler3D>(_CgSetarrayTypeSampler3D_QNAME, CgSampler3D.class, CgSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "enum", scope = CgSetarrayType.class)
    public JAXBElement<String> createCgSetarrayTypeEnum(String value) {
        return new JAXBElement<String>(_CgSetarrayTypeEnum_QNAME, String.class, CgSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgSamplerCUBE }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "samplerCUBE", scope = CgSetarrayType.class)
    public JAXBElement<CgSamplerCUBE> createCgSetarrayTypeSamplerCUBE(CgSamplerCUBE value) {
        return new JAXBElement<CgSamplerCUBE>(_CgSetarrayTypeSamplerCUBE_QNAME, CgSamplerCUBE.class, CgSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int1x1", scope = CgSetarrayType.class)
    public JAXBElement<List<Integer>> createCgSetarrayTypeInt1X1(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt1X1_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half3x3", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeHalf3X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf3X3_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half3x2", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeHalf3X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf3X2_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half3x1", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeHalf3X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf3X1_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed2", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFixed2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed2_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed3", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFixed3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed3_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half3x4", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeHalf3X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf3X4_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link java.lang.Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed1", scope = CgSetarrayType.class)
    public JAXBElement<java.lang.Float> createCgSetarrayTypeFixed1(java.lang.Float value) {
        return new JAXBElement<java.lang.Float>(_CgSetarrayTypeFixed1_QNAME, java.lang.Float.class, CgSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float1x2", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFloat1X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat1X2_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float1x1", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFloat1X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat1X1_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link java.lang.Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half", scope = CgSetarrayType.class)
    public JAXBElement<java.lang.Float> createCgSetarrayTypeHalf(java.lang.Float value) {
        return new JAXBElement<java.lang.Float>(_CgSetarrayTypeHalf_QNAME, java.lang.Float.class, CgSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgSampler2D }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "sampler2D", scope = CgSetarrayType.class)
    public JAXBElement<CgSampler2D> createCgSetarrayTypeSampler2D(CgSampler2D value) {
        return new JAXBElement<CgSampler2D>(_CgSetarrayTypeSampler2D_QNAME, CgSampler2D.class, CgSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int4", scope = CgSetarrayType.class)
    public JAXBElement<List<Integer>> createCgSetarrayTypeInt4(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt4_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int3", scope = CgSetarrayType.class)
    public JAXBElement<List<Integer>> createCgSetarrayTypeInt3(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt3_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool3", scope = CgSetarrayType.class)
    public JAXBElement<List<Boolean>> createCgSetarrayTypeBool3(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool3_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed1x4", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFixed1X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed1X4_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool2", scope = CgSetarrayType.class)
    public JAXBElement<List<Boolean>> createCgSetarrayTypeBool2(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool2_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed1x3", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFixed1X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed1X3_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int2", scope = CgSetarrayType.class)
    public JAXBElement<List<Integer>> createCgSetarrayTypeInt2(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt2_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool4", scope = CgSetarrayType.class)
    public JAXBElement<List<Boolean>> createCgSetarrayTypeBool4(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool4_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int1", scope = CgSetarrayType.class)
    public JAXBElement<Integer> createCgSetarrayTypeInt1(Integer value) {
        return new JAXBElement<Integer>(_CgSetarrayTypeInt1_QNAME, Integer.class, CgSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool4x4", scope = CgSetarrayType.class)
    public JAXBElement<List<Boolean>> createCgSetarrayTypeBool4X4(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool4X4_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool1", scope = CgSetarrayType.class)
    public JAXBElement<Boolean> createCgSetarrayTypeBool1(Boolean value) {
        return new JAXBElement<Boolean>(_CgSetarrayTypeBool1_QNAME, Boolean.class, CgSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool4x2", scope = CgSetarrayType.class)
    public JAXBElement<List<Boolean>> createCgSetarrayTypeBool4X2(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool4X2_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float1x4", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFloat1X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat1X4_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed1x2", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFixed1X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed1X2_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool4x3", scope = CgSetarrayType.class)
    public JAXBElement<List<Boolean>> createCgSetarrayTypeBool4X3(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool4X3_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float1x3", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFloat1X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat1X3_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed1x1", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFixed1X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed1X1_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool4x1", scope = CgSetarrayType.class)
    public JAXBElement<List<Boolean>> createCgSetarrayTypeBool4X1(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool4X1_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link java.lang.Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float1", scope = CgSetarrayType.class)
    public JAXBElement<java.lang.Float> createCgSetarrayTypeFloat1(java.lang.Float value) {
        return new JAXBElement<java.lang.Float>(_CgSetarrayTypeFloat1_QNAME, java.lang.Float.class, CgSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float2", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFloat2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat2_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float3", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFloat3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat3_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float4", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFloat4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat4_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int2x1", scope = CgSetarrayType.class)
    public JAXBElement<List<Integer>> createCgSetarrayTypeInt2X1(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt2X1_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgSampler1D }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "sampler1D", scope = CgSetarrayType.class)
    public JAXBElement<CgSampler1D> createCgSetarrayTypeSampler1D(CgSampler1D value) {
        return new JAXBElement<CgSampler1D>(_CgSetarrayTypeSampler1D_QNAME, CgSampler1D.class, CgSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int2x2", scope = CgSetarrayType.class)
    public JAXBElement<List<Integer>> createCgSetarrayTypeInt2X2(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt2X2_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int2x3", scope = CgSetarrayType.class)
    public JAXBElement<List<Integer>> createCgSetarrayTypeInt2X3(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt2X3_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int2x4", scope = CgSetarrayType.class)
    public JAXBElement<List<Integer>> createCgSetarrayTypeInt2X4(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt2X4_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half2x4", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeHalf2X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf2X4_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half2x3", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeHalf2X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf2X3_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half2x2", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeHalf2X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf2X2_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half2x1", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeHalf2X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf2X1_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgSamplerDEPTH }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "samplerDEPTH", scope = CgSetarrayType.class)
    public JAXBElement<CgSamplerDEPTH> createCgSetarrayTypeSamplerDEPTH(CgSamplerDEPTH value) {
        return new JAXBElement<CgSamplerDEPTH>(_CgSetarrayTypeSamplerDEPTH_QNAME, CgSamplerDEPTH.class, CgSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float2x1", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFloat2X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat2X1_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed4", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFixed4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed4_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed4x4", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFixed4X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed4X4_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed4x1", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFixed4X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed4X1_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed4x3", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFixed4X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed4X3_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed4x2", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFixed4X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed4X2_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float2x4", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFloat2X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat2X4_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float2x3", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFloat2X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat2X3_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float2x2", scope = CgSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetarrayTypeFloat2X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat2X2_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool1x3", scope = CgSetarrayType.class)
    public JAXBElement<List<Boolean>> createCgSetarrayTypeBool1X3(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool1X3_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool1x4", scope = CgSetarrayType.class)
    public JAXBElement<List<Boolean>> createCgSetarrayTypeBool1X4(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool1X4_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool1x1", scope = CgSetarrayType.class)
    public JAXBElement<List<Boolean>> createCgSetarrayTypeBool1X1(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool1X1_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool1x2", scope = CgSetarrayType.class)
    public JAXBElement<List<Boolean>> createCgSetarrayTypeBool1X2(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool1X2_QNAME, ((Class) List.class), CgSetarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Polygons.Ph }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "ph", scope = Polygons.class)
    public JAXBElement<Polygons.Ph> createPolygonsPh(Polygons.Ph value) {
        return new JAXBElement<Polygons.Ph>(_PolygonsPh_QNAME, Polygons.Ph.class, Polygons.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int3x1", scope = CgSetuserType.class)
    public JAXBElement<List<Integer>> createCgSetuserTypeInt3X1(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt3X1_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int3x2", scope = CgSetuserType.class)
    public JAXBElement<List<Integer>> createCgSetuserTypeInt3X2(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt3X2_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int3x3", scope = CgSetuserType.class)
    public JAXBElement<List<Integer>> createCgSetuserTypeInt3X3(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt3X3_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgSetuserType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "usertype", scope = CgSetuserType.class)
    public JAXBElement<CgSetuserType> createCgSetuserTypeUsertype(CgSetuserType value) {
        return new JAXBElement<CgSetuserType>(_CgSetarrayTypeUsertype_QNAME, CgSetuserType.class, CgSetuserType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half1x4", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeHalf1X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf1X4_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half1x3", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeHalf1X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf1X3_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half1x2", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeHalf1X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf1X2_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half2", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeHalf2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf2_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half1x1", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeHalf1X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf1X1_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link java.lang.Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half1", scope = CgSetuserType.class)
    public JAXBElement<java.lang.Float> createCgSetuserTypeHalf1(java.lang.Float value) {
        return new JAXBElement<java.lang.Float>(_CgSetarrayTypeHalf1_QNAME, java.lang.Float.class, CgSetuserType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link java.lang.Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed", scope = CgSetuserType.class)
    public JAXBElement<java.lang.Float> createCgSetuserTypeFixed(java.lang.Float value) {
        return new JAXBElement<java.lang.Float>(_CgSetarrayTypeFixed_QNAME, java.lang.Float.class, CgSetuserType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half4", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeHalf4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf4_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half3", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeHalf3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf3_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link java.lang.Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float", scope = CgSetuserType.class)
    public JAXBElement<java.lang.Float> createCgSetuserTypeFloat(java.lang.Float value) {
        return new JAXBElement<java.lang.Float>(_CgSetarrayTypeFloat_QNAME, java.lang.Float.class, CgSetuserType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int3x4", scope = CgSetuserType.class)
    public JAXBElement<List<Integer>> createCgSetuserTypeInt3X4(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt3X4_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed3x2", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFixed3X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed3X2_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed3x1", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFixed3X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed3X1_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed3x4", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFixed3X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed3X4_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed3x3", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFixed3X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed3X3_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float3x4", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFloat3X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat3X4_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float3x3", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFloat3X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat3X3_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool2x4", scope = CgSetuserType.class)
    public JAXBElement<List<Boolean>> createCgSetuserTypeBool2X4(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool2X4_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float3x2", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFloat3X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat3X2_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float3x1", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFloat3X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat3X1_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool2x2", scope = CgSetuserType.class)
    public JAXBElement<List<Boolean>> createCgSetuserTypeBool2X2(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool2X2_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool2x3", scope = CgSetuserType.class)
    public JAXBElement<List<Boolean>> createCgSetuserTypeBool2X3(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool2X3_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgConnectParam }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "connect_param", scope = CgSetuserType.class)
    public JAXBElement<CgConnectParam> createCgSetuserTypeConnectParam(CgConnectParam value) {
        return new JAXBElement<CgConnectParam>(_CgSetuserTypeConnectParam_QNAME, CgConnectParam.class, CgSetuserType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool2x1", scope = CgSetuserType.class)
    public JAXBElement<List<Boolean>> createCgSetuserTypeBool2X1(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool2X1_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int", scope = CgSetuserType.class)
    public JAXBElement<Integer> createCgSetuserTypeInt(Integer value) {
        return new JAXBElement<Integer>(_CgSetarrayTypeInt_QNAME, Integer.class, CgSetuserType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int4x1", scope = CgSetuserType.class)
    public JAXBElement<List<Integer>> createCgSetuserTypeInt4X1(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt4X1_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int4x2", scope = CgSetuserType.class)
    public JAXBElement<List<Integer>> createCgSetuserTypeInt4X2(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt4X2_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half4x2", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeHalf4X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf4X2_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half4x1", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeHalf4X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf4X1_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half4x4", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeHalf4X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf4X4_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgSamplerRECT }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "samplerRECT", scope = CgSetuserType.class)
    public JAXBElement<CgSamplerRECT> createCgSetuserTypeSamplerRECT(CgSamplerRECT value) {
        return new JAXBElement<CgSamplerRECT>(_CgSetarrayTypeSamplerRECT_QNAME, CgSamplerRECT.class, CgSetuserType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half4x3", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeHalf4X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf4X3_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool", scope = CgSetuserType.class)
    public JAXBElement<Boolean> createCgSetuserTypeBool(Boolean value) {
        return new JAXBElement<Boolean>(_CgSetarrayTypeBool_QNAME, Boolean.class, CgSetuserType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "string", scope = CgSetuserType.class)
    public JAXBElement<String> createCgSetuserTypeString(String value) {
        return new JAXBElement<String>(_CgSetarrayTypeString_QNAME, String.class, CgSetuserType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgSetarrayType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "array", scope = CgSetuserType.class)
    public JAXBElement<CgSetarrayType> createCgSetuserTypeArray(CgSetarrayType value) {
        return new JAXBElement<CgSetarrayType>(_CgSetarrayTypeArray_QNAME, CgSetarrayType.class, CgSetuserType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int4x3", scope = CgSetuserType.class)
    public JAXBElement<List<Integer>> createCgSetuserTypeInt4X3(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt4X3_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed2x3", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFixed2X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed2X3_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int4x4", scope = CgSetuserType.class)
    public JAXBElement<List<Integer>> createCgSetuserTypeInt4X4(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt4X4_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed2x2", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFixed2X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed2X2_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed2x4", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFixed2X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed2X4_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float4x3", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFloat4X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat4X3_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float4x2", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFloat4X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat4X2_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool3x3", scope = CgSetuserType.class)
    public JAXBElement<List<Boolean>> createCgSetuserTypeBool3X3(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool3X3_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float4x1", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFloat4X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat4X1_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed2x1", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFixed2X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed2X1_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool3x4", scope = CgSetuserType.class)
    public JAXBElement<List<Boolean>> createCgSetuserTypeBool3X4(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool3X4_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool3x1", scope = CgSetuserType.class)
    public JAXBElement<List<Boolean>> createCgSetuserTypeBool3X1(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool3X1_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool3x2", scope = CgSetuserType.class)
    public JAXBElement<List<Boolean>> createCgSetuserTypeBool3X2(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool3X2_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float4x4", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFloat4X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat4X4_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgSurfaceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "surface", scope = CgSetuserType.class)
    public JAXBElement<CgSurfaceType> createCgSetuserTypeSurface(CgSurfaceType value) {
        return new JAXBElement<CgSurfaceType>(_CgSetarrayTypeSurface_QNAME, CgSurfaceType.class, CgSetuserType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int1x2", scope = CgSetuserType.class)
    public JAXBElement<List<Integer>> createCgSetuserTypeInt1X2(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt1X2_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int1x3", scope = CgSetuserType.class)
    public JAXBElement<List<Integer>> createCgSetuserTypeInt1X3(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt1X3_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int1x4", scope = CgSetuserType.class)
    public JAXBElement<List<Integer>> createCgSetuserTypeInt1X4(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt1X4_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgSampler3D }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "sampler3D", scope = CgSetuserType.class)
    public JAXBElement<CgSampler3D> createCgSetuserTypeSampler3D(CgSampler3D value) {
        return new JAXBElement<CgSampler3D>(_CgSetarrayTypeSampler3D_QNAME, CgSampler3D.class, CgSetuserType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "enum", scope = CgSetuserType.class)
    public JAXBElement<String> createCgSetuserTypeEnum(String value) {
        return new JAXBElement<String>(_CgSetarrayTypeEnum_QNAME, String.class, CgSetuserType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgSamplerCUBE }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "samplerCUBE", scope = CgSetuserType.class)
    public JAXBElement<CgSamplerCUBE> createCgSetuserTypeSamplerCUBE(CgSamplerCUBE value) {
        return new JAXBElement<CgSamplerCUBE>(_CgSetarrayTypeSamplerCUBE_QNAME, CgSamplerCUBE.class, CgSetuserType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int1x1", scope = CgSetuserType.class)
    public JAXBElement<List<Integer>> createCgSetuserTypeInt1X1(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt1X1_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half3x3", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeHalf3X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf3X3_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half3x2", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeHalf3X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf3X2_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half3x1", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeHalf3X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf3X1_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed2", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFixed2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed2_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed3", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFixed3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed3_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half3x4", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeHalf3X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf3X4_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link java.lang.Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed1", scope = CgSetuserType.class)
    public JAXBElement<java.lang.Float> createCgSetuserTypeFixed1(java.lang.Float value) {
        return new JAXBElement<java.lang.Float>(_CgSetarrayTypeFixed1_QNAME, java.lang.Float.class, CgSetuserType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float1x2", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFloat1X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat1X2_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float1x1", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFloat1X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat1X1_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link java.lang.Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half", scope = CgSetuserType.class)
    public JAXBElement<java.lang.Float> createCgSetuserTypeHalf(java.lang.Float value) {
        return new JAXBElement<java.lang.Float>(_CgSetarrayTypeHalf_QNAME, java.lang.Float.class, CgSetuserType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgSampler2D }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "sampler2D", scope = CgSetuserType.class)
    public JAXBElement<CgSampler2D> createCgSetuserTypeSampler2D(CgSampler2D value) {
        return new JAXBElement<CgSampler2D>(_CgSetarrayTypeSampler2D_QNAME, CgSampler2D.class, CgSetuserType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int4", scope = CgSetuserType.class)
    public JAXBElement<List<Integer>> createCgSetuserTypeInt4(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt4_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int3", scope = CgSetuserType.class)
    public JAXBElement<List<Integer>> createCgSetuserTypeInt3(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt3_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool3", scope = CgSetuserType.class)
    public JAXBElement<List<Boolean>> createCgSetuserTypeBool3(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool3_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed1x4", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFixed1X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed1X4_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool2", scope = CgSetuserType.class)
    public JAXBElement<List<Boolean>> createCgSetuserTypeBool2(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool2_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed1x3", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFixed1X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed1X3_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int2", scope = CgSetuserType.class)
    public JAXBElement<List<Integer>> createCgSetuserTypeInt2(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt2_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool4", scope = CgSetuserType.class)
    public JAXBElement<List<Boolean>> createCgSetuserTypeBool4(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool4_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int1", scope = CgSetuserType.class)
    public JAXBElement<Integer> createCgSetuserTypeInt1(Integer value) {
        return new JAXBElement<Integer>(_CgSetarrayTypeInt1_QNAME, Integer.class, CgSetuserType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool4x4", scope = CgSetuserType.class)
    public JAXBElement<List<Boolean>> createCgSetuserTypeBool4X4(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool4X4_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool1", scope = CgSetuserType.class)
    public JAXBElement<Boolean> createCgSetuserTypeBool1(Boolean value) {
        return new JAXBElement<Boolean>(_CgSetarrayTypeBool1_QNAME, Boolean.class, CgSetuserType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool4x2", scope = CgSetuserType.class)
    public JAXBElement<List<Boolean>> createCgSetuserTypeBool4X2(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool4X2_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float1x4", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFloat1X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat1X4_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed1x2", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFixed1X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed1X2_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool4x3", scope = CgSetuserType.class)
    public JAXBElement<List<Boolean>> createCgSetuserTypeBool4X3(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool4X3_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float1x3", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFloat1X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat1X3_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed1x1", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFixed1X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed1X1_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool4x1", scope = CgSetuserType.class)
    public JAXBElement<List<Boolean>> createCgSetuserTypeBool4X1(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool4X1_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link java.lang.Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float1", scope = CgSetuserType.class)
    public JAXBElement<java.lang.Float> createCgSetuserTypeFloat1(java.lang.Float value) {
        return new JAXBElement<java.lang.Float>(_CgSetarrayTypeFloat1_QNAME, java.lang.Float.class, CgSetuserType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float2", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFloat2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat2_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float3", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFloat3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat3_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float4", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFloat4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat4_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int2x1", scope = CgSetuserType.class)
    public JAXBElement<List<Integer>> createCgSetuserTypeInt2X1(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt2X1_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgSampler1D }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "sampler1D", scope = CgSetuserType.class)
    public JAXBElement<CgSampler1D> createCgSetuserTypeSampler1D(CgSampler1D value) {
        return new JAXBElement<CgSampler1D>(_CgSetarrayTypeSampler1D_QNAME, CgSampler1D.class, CgSetuserType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int2x2", scope = CgSetuserType.class)
    public JAXBElement<List<Integer>> createCgSetuserTypeInt2X2(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt2X2_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int2x3", scope = CgSetuserType.class)
    public JAXBElement<List<Integer>> createCgSetuserTypeInt2X3(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt2X3_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int2x4", scope = CgSetuserType.class)
    public JAXBElement<List<Integer>> createCgSetuserTypeInt2X4(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt2X4_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half2x4", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeHalf2X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf2X4_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half2x3", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeHalf2X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf2X3_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half2x2", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeHalf2X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf2X2_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half2x1", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeHalf2X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf2X1_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgSamplerDEPTH }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "samplerDEPTH", scope = CgSetuserType.class)
    public JAXBElement<CgSamplerDEPTH> createCgSetuserTypeSamplerDEPTH(CgSamplerDEPTH value) {
        return new JAXBElement<CgSamplerDEPTH>(_CgSetarrayTypeSamplerDEPTH_QNAME, CgSamplerDEPTH.class, CgSetuserType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float2x1", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFloat2X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat2X1_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed4", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFixed4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed4_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed4x4", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFixed4X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed4X4_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed4x1", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFixed4X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed4X1_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed4x3", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFixed4X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed4X3_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed4x2", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFixed4X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed4X2_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float2x4", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFloat2X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat2X4_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float2x3", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFloat2X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat2X3_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float2x2", scope = CgSetuserType.class)
    public JAXBElement<List<java.lang.Float>> createCgSetuserTypeFloat2X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat2X2_QNAME, ((Class) List.class), CgSetuserType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool1x3", scope = CgSetuserType.class)
    public JAXBElement<List<Boolean>> createCgSetuserTypeBool1X3(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool1X3_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool1x4", scope = CgSetuserType.class)
    public JAXBElement<List<Boolean>> createCgSetuserTypeBool1X4(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool1X4_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool1x1", scope = CgSetuserType.class)
    public JAXBElement<List<Boolean>> createCgSetuserTypeBool1X1(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool1X1_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool1x2", scope = CgSetuserType.class)
    public JAXBElement<List<Boolean>> createCgSetuserTypeBool1X2(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool1X2_QNAME, ((Class) List.class), CgSetuserType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TargetableFloat }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "zfar", scope = Camera.Optics.TechniqueCommon.Orthographic.class)
    public JAXBElement<TargetableFloat> createCameraOpticsTechniqueCommonOrthographicZfar(TargetableFloat value) {
        return new JAXBElement<TargetableFloat>(_CameraOpticsTechniqueCommonOrthographicZfar_QNAME, TargetableFloat.class, Camera.Optics.TechniqueCommon.Orthographic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TargetableFloat }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "aspect_ratio", scope = Camera.Optics.TechniqueCommon.Orthographic.class)
    public JAXBElement<TargetableFloat> createCameraOpticsTechniqueCommonOrthographicAspectRatio(TargetableFloat value) {
        return new JAXBElement<TargetableFloat>(_CameraOpticsTechniqueCommonOrthographicAspectRatio_QNAME, TargetableFloat.class, Camera.Optics.TechniqueCommon.Orthographic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TargetableFloat }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "ymag", scope = Camera.Optics.TechniqueCommon.Orthographic.class)
    public JAXBElement<TargetableFloat> createCameraOpticsTechniqueCommonOrthographicYmag(TargetableFloat value) {
        return new JAXBElement<TargetableFloat>(_CameraOpticsTechniqueCommonOrthographicYmag_QNAME, TargetableFloat.class, Camera.Optics.TechniqueCommon.Orthographic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TargetableFloat }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "xmag", scope = Camera.Optics.TechniqueCommon.Orthographic.class)
    public JAXBElement<TargetableFloat> createCameraOpticsTechniqueCommonOrthographicXmag(TargetableFloat value) {
        return new JAXBElement<TargetableFloat>(_CameraOpticsTechniqueCommonOrthographicXmag_QNAME, TargetableFloat.class, Camera.Optics.TechniqueCommon.Orthographic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TargetableFloat }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "znear", scope = Camera.Optics.TechniqueCommon.Orthographic.class)
    public JAXBElement<TargetableFloat> createCameraOpticsTechniqueCommonOrthographicZnear(TargetableFloat value) {
        return new JAXBElement<TargetableFloat>(_CameraOpticsTechniqueCommonOrthographicZnear_QNAME, TargetableFloat.class, Camera.Optics.TechniqueCommon.Orthographic.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool", scope = GlslNewarrayType.class)
    public JAXBElement<Boolean> createGlslNewarrayTypeBool(Boolean value) {
        return new JAXBElement<Boolean>(_CgSetarrayTypeBool_QNAME, Boolean.class, GlslNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GlslSurfaceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "surface", scope = GlslNewarrayType.class)
    public JAXBElement<GlslSurfaceType> createGlslNewarrayTypeSurface(GlslSurfaceType value) {
        return new JAXBElement<GlslSurfaceType>(_CgSetarrayTypeSurface_QNAME, GlslSurfaceType.class, GlslNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float2", scope = GlslNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createGlslNewarrayTypeFloat2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat2_QNAME, ((Class) List.class), GlslNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float3", scope = GlslNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createGlslNewarrayTypeFloat3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat3_QNAME, ((Class) List.class), GlslNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link java.lang.Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float", scope = GlslNewarrayType.class)
    public JAXBElement<java.lang.Float> createGlslNewarrayTypeFloat(java.lang.Float value) {
        return new JAXBElement<java.lang.Float>(_CgSetarrayTypeFloat_QNAME, java.lang.Float.class, GlslNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float4", scope = GlslNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createGlslNewarrayTypeFloat4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat4_QNAME, ((Class) List.class), GlslNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GlSampler1D }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "sampler1D", scope = GlslNewarrayType.class)
    public JAXBElement<GlSampler1D> createGlslNewarrayTypeSampler1D(GlSampler1D value) {
        return new JAXBElement<GlSampler1D>(_CgSetarrayTypeSampler1D_QNAME, GlSampler1D.class, GlslNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GlSampler2D }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "sampler2D", scope = GlslNewarrayType.class)
    public JAXBElement<GlSampler2D> createGlslNewarrayTypeSampler2D(GlSampler2D value) {
        return new JAXBElement<GlSampler2D>(_CgSetarrayTypeSampler2D_QNAME, GlSampler2D.class, GlslNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int", scope = GlslNewarrayType.class)
    public JAXBElement<Integer> createGlslNewarrayTypeInt(Integer value) {
        return new JAXBElement<Integer>(_CgSetarrayTypeInt_QNAME, Integer.class, GlslNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GlSampler3D }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "sampler3D", scope = GlslNewarrayType.class)
    public JAXBElement<GlSampler3D> createGlslNewarrayTypeSampler3D(GlSampler3D value) {
        return new JAXBElement<GlSampler3D>(_CgSetarrayTypeSampler3D_QNAME, GlSampler3D.class, GlslNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "enum", scope = GlslNewarrayType.class)
    public JAXBElement<String> createGlslNewarrayTypeEnum(String value) {
        return new JAXBElement<String>(_CgSetarrayTypeEnum_QNAME, String.class, GlslNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GlSamplerCUBE }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "samplerCUBE", scope = GlslNewarrayType.class)
    public JAXBElement<GlSamplerCUBE> createGlslNewarrayTypeSamplerCUBE(GlSamplerCUBE value) {
        return new JAXBElement<GlSamplerCUBE>(_CgSetarrayTypeSamplerCUBE_QNAME, GlSamplerCUBE.class, GlslNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int4", scope = GlslNewarrayType.class)
    public JAXBElement<List<Integer>> createGlslNewarrayTypeInt4(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt4_QNAME, ((Class) List.class), GlslNewarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int3", scope = GlslNewarrayType.class)
    public JAXBElement<List<Integer>> createGlslNewarrayTypeInt3(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt3_QNAME, ((Class) List.class), GlslNewarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GlslNewarrayType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "array", scope = GlslNewarrayType.class)
    public JAXBElement<GlslNewarrayType> createGlslNewarrayTypeArray(GlslNewarrayType value) {
        return new JAXBElement<GlslNewarrayType>(_CgSetarrayTypeArray_QNAME, GlslNewarrayType.class, GlslNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool3", scope = GlslNewarrayType.class)
    public JAXBElement<List<Boolean>> createGlslNewarrayTypeBool3(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool3_QNAME, ((Class) List.class), GlslNewarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool2", scope = GlslNewarrayType.class)
    public JAXBElement<List<Boolean>> createGlslNewarrayTypeBool2(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool2_QNAME, ((Class) List.class), GlslNewarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int2", scope = GlslNewarrayType.class)
    public JAXBElement<List<Integer>> createGlslNewarrayTypeInt2(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt2_QNAME, ((Class) List.class), GlslNewarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GlSamplerRECT }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "samplerRECT", scope = GlslNewarrayType.class)
    public JAXBElement<GlSamplerRECT> createGlslNewarrayTypeSamplerRECT(GlSamplerRECT value) {
        return new JAXBElement<GlSamplerRECT>(_CgSetarrayTypeSamplerRECT_QNAME, GlSamplerRECT.class, GlslNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool4", scope = GlslNewarrayType.class)
    public JAXBElement<List<Boolean>> createGlslNewarrayTypeBool4(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool4_QNAME, ((Class) List.class), GlslNewarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float3x3", scope = GlslNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createGlslNewarrayTypeFloat3X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat3X3_QNAME, ((Class) List.class), GlslNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GlSamplerDEPTH }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "samplerDEPTH", scope = GlslNewarrayType.class)
    public JAXBElement<GlSamplerDEPTH> createGlslNewarrayTypeSamplerDEPTH(GlSamplerDEPTH value) {
        return new JAXBElement<GlSamplerDEPTH>(_CgSetarrayTypeSamplerDEPTH_QNAME, GlSamplerDEPTH.class, GlslNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float2x2", scope = GlslNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createGlslNewarrayTypeFloat2X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat2X2_QNAME, ((Class) List.class), GlslNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float4x4", scope = GlslNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createGlslNewarrayTypeFloat4X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat4X4_QNAME, ((Class) List.class), GlslNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TargetableFloat }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "zfar", scope = Camera.Optics.TechniqueCommon.Perspective.class)
    public JAXBElement<TargetableFloat> createCameraOpticsTechniqueCommonPerspectiveZfar(TargetableFloat value) {
        return new JAXBElement<TargetableFloat>(_CameraOpticsTechniqueCommonOrthographicZfar_QNAME, TargetableFloat.class, Camera.Optics.TechniqueCommon.Perspective.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TargetableFloat }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "aspect_ratio", scope = Camera.Optics.TechniqueCommon.Perspective.class)
    public JAXBElement<TargetableFloat> createCameraOpticsTechniqueCommonPerspectiveAspectRatio(TargetableFloat value) {
        return new JAXBElement<TargetableFloat>(_CameraOpticsTechniqueCommonOrthographicAspectRatio_QNAME, TargetableFloat.class, Camera.Optics.TechniqueCommon.Perspective.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TargetableFloat }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "yfov", scope = Camera.Optics.TechniqueCommon.Perspective.class)
    public JAXBElement<TargetableFloat> createCameraOpticsTechniqueCommonPerspectiveYfov(TargetableFloat value) {
        return new JAXBElement<TargetableFloat>(_CameraOpticsTechniqueCommonPerspectiveYfov_QNAME, TargetableFloat.class, Camera.Optics.TechniqueCommon.Perspective.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TargetableFloat }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "xfov", scope = Camera.Optics.TechniqueCommon.Perspective.class)
    public JAXBElement<TargetableFloat> createCameraOpticsTechniqueCommonPerspectiveXfov(TargetableFloat value) {
        return new JAXBElement<TargetableFloat>(_CameraOpticsTechniqueCommonPerspectiveXfov_QNAME, TargetableFloat.class, Camera.Optics.TechniqueCommon.Perspective.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TargetableFloat }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "znear", scope = Camera.Optics.TechniqueCommon.Perspective.class)
    public JAXBElement<TargetableFloat> createCameraOpticsTechniqueCommonPerspectiveZnear(TargetableFloat value) {
        return new JAXBElement<TargetableFloat>(_CameraOpticsTechniqueCommonOrthographicZnear_QNAME, TargetableFloat.class, Camera.Optics.TechniqueCommon.Perspective.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool", scope = GlslSetarrayType.class)
    public JAXBElement<Boolean> createGlslSetarrayTypeBool(Boolean value) {
        return new JAXBElement<Boolean>(_CgSetarrayTypeBool_QNAME, Boolean.class, GlslSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GlslSurfaceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "surface", scope = GlslSetarrayType.class)
    public JAXBElement<GlslSurfaceType> createGlslSetarrayTypeSurface(GlslSurfaceType value) {
        return new JAXBElement<GlslSurfaceType>(_CgSetarrayTypeSurface_QNAME, GlslSurfaceType.class, GlslSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float2", scope = GlslSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createGlslSetarrayTypeFloat2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat2_QNAME, ((Class) List.class), GlslSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float3", scope = GlslSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createGlslSetarrayTypeFloat3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat3_QNAME, ((Class) List.class), GlslSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link java.lang.Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float", scope = GlslSetarrayType.class)
    public JAXBElement<java.lang.Float> createGlslSetarrayTypeFloat(java.lang.Float value) {
        return new JAXBElement<java.lang.Float>(_CgSetarrayTypeFloat_QNAME, java.lang.Float.class, GlslSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float4", scope = GlslSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createGlslSetarrayTypeFloat4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat4_QNAME, ((Class) List.class), GlslSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GlSampler1D }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "sampler1D", scope = GlslSetarrayType.class)
    public JAXBElement<GlSampler1D> createGlslSetarrayTypeSampler1D(GlSampler1D value) {
        return new JAXBElement<GlSampler1D>(_CgSetarrayTypeSampler1D_QNAME, GlSampler1D.class, GlslSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GlSampler2D }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "sampler2D", scope = GlslSetarrayType.class)
    public JAXBElement<GlSampler2D> createGlslSetarrayTypeSampler2D(GlSampler2D value) {
        return new JAXBElement<GlSampler2D>(_CgSetarrayTypeSampler2D_QNAME, GlSampler2D.class, GlslSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int", scope = GlslSetarrayType.class)
    public JAXBElement<Integer> createGlslSetarrayTypeInt(Integer value) {
        return new JAXBElement<Integer>(_CgSetarrayTypeInt_QNAME, Integer.class, GlslSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GlSampler3D }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "sampler3D", scope = GlslSetarrayType.class)
    public JAXBElement<GlSampler3D> createGlslSetarrayTypeSampler3D(GlSampler3D value) {
        return new JAXBElement<GlSampler3D>(_CgSetarrayTypeSampler3D_QNAME, GlSampler3D.class, GlslSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "enum", scope = GlslSetarrayType.class)
    public JAXBElement<String> createGlslSetarrayTypeEnum(String value) {
        return new JAXBElement<String>(_CgSetarrayTypeEnum_QNAME, String.class, GlslSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GlSamplerCUBE }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "samplerCUBE", scope = GlslSetarrayType.class)
    public JAXBElement<GlSamplerCUBE> createGlslSetarrayTypeSamplerCUBE(GlSamplerCUBE value) {
        return new JAXBElement<GlSamplerCUBE>(_CgSetarrayTypeSamplerCUBE_QNAME, GlSamplerCUBE.class, GlslSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int4", scope = GlslSetarrayType.class)
    public JAXBElement<List<Integer>> createGlslSetarrayTypeInt4(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt4_QNAME, ((Class) List.class), GlslSetarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int3", scope = GlslSetarrayType.class)
    public JAXBElement<List<Integer>> createGlslSetarrayTypeInt3(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt3_QNAME, ((Class) List.class), GlslSetarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GlslSetarrayType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "array", scope = GlslSetarrayType.class)
    public JAXBElement<GlslSetarrayType> createGlslSetarrayTypeArray(GlslSetarrayType value) {
        return new JAXBElement<GlslSetarrayType>(_CgSetarrayTypeArray_QNAME, GlslSetarrayType.class, GlslSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool3", scope = GlslSetarrayType.class)
    public JAXBElement<List<Boolean>> createGlslSetarrayTypeBool3(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool3_QNAME, ((Class) List.class), GlslSetarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool2", scope = GlslSetarrayType.class)
    public JAXBElement<List<Boolean>> createGlslSetarrayTypeBool2(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool2_QNAME, ((Class) List.class), GlslSetarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int2", scope = GlslSetarrayType.class)
    public JAXBElement<List<Integer>> createGlslSetarrayTypeInt2(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt2_QNAME, ((Class) List.class), GlslSetarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GlSamplerRECT }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "samplerRECT", scope = GlslSetarrayType.class)
    public JAXBElement<GlSamplerRECT> createGlslSetarrayTypeSamplerRECT(GlSamplerRECT value) {
        return new JAXBElement<GlSamplerRECT>(_CgSetarrayTypeSamplerRECT_QNAME, GlSamplerRECT.class, GlslSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool4", scope = GlslSetarrayType.class)
    public JAXBElement<List<Boolean>> createGlslSetarrayTypeBool4(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool4_QNAME, ((Class) List.class), GlslSetarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float3x3", scope = GlslSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createGlslSetarrayTypeFloat3X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat3X3_QNAME, ((Class) List.class), GlslSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GlSamplerDEPTH }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "samplerDEPTH", scope = GlslSetarrayType.class)
    public JAXBElement<GlSamplerDEPTH> createGlslSetarrayTypeSamplerDEPTH(GlSamplerDEPTH value) {
        return new JAXBElement<GlSamplerDEPTH>(_CgSetarrayTypeSamplerDEPTH_QNAME, GlSamplerDEPTH.class, GlslSetarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float2x2", scope = GlslSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createGlslSetarrayTypeFloat2X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat2X2_QNAME, ((Class) List.class), GlslSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float4x4", scope = GlslSetarrayType.class)
    public JAXBElement<List<java.lang.Float>> createGlslSetarrayTypeFloat4X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat4X4_QNAME, ((Class) List.class), GlslSetarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int3x1", scope = CgNewarrayType.class)
    public JAXBElement<List<Integer>> createCgNewarrayTypeInt3X1(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt3X1_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int3x2", scope = CgNewarrayType.class)
    public JAXBElement<List<Integer>> createCgNewarrayTypeInt3X2(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt3X2_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int3x3", scope = CgNewarrayType.class)
    public JAXBElement<List<Integer>> createCgNewarrayTypeInt3X3(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt3X3_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgSetuserType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "usertype", scope = CgNewarrayType.class)
    public JAXBElement<CgSetuserType> createCgNewarrayTypeUsertype(CgSetuserType value) {
        return new JAXBElement<CgSetuserType>(_CgSetarrayTypeUsertype_QNAME, CgSetuserType.class, CgNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half1x4", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeHalf1X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf1X4_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half1x3", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeHalf1X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf1X3_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half1x2", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeHalf1X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf1X2_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half2", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeHalf2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf2_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half1x1", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeHalf1X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf1X1_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link java.lang.Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half1", scope = CgNewarrayType.class)
    public JAXBElement<java.lang.Float> createCgNewarrayTypeHalf1(java.lang.Float value) {
        return new JAXBElement<java.lang.Float>(_CgSetarrayTypeHalf1_QNAME, java.lang.Float.class, CgNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link java.lang.Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed", scope = CgNewarrayType.class)
    public JAXBElement<java.lang.Float> createCgNewarrayTypeFixed(java.lang.Float value) {
        return new JAXBElement<java.lang.Float>(_CgSetarrayTypeFixed_QNAME, java.lang.Float.class, CgNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half4", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeHalf4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf4_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half3", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeHalf3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf3_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link java.lang.Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float", scope = CgNewarrayType.class)
    public JAXBElement<java.lang.Float> createCgNewarrayTypeFloat(java.lang.Float value) {
        return new JAXBElement<java.lang.Float>(_CgSetarrayTypeFloat_QNAME, java.lang.Float.class, CgNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int3x4", scope = CgNewarrayType.class)
    public JAXBElement<List<Integer>> createCgNewarrayTypeInt3X4(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt3X4_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed3x2", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFixed3X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed3X2_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed3x1", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFixed3X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed3X1_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed3x4", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFixed3X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed3X4_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed3x3", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFixed3X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed3X3_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float3x4", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFloat3X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat3X4_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float3x3", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFloat3X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat3X3_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool2x4", scope = CgNewarrayType.class)
    public JAXBElement<List<Boolean>> createCgNewarrayTypeBool2X4(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool2X4_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float3x2", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFloat3X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat3X2_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float3x1", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFloat3X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat3X1_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool2x2", scope = CgNewarrayType.class)
    public JAXBElement<List<Boolean>> createCgNewarrayTypeBool2X2(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool2X2_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool2x3", scope = CgNewarrayType.class)
    public JAXBElement<List<Boolean>> createCgNewarrayTypeBool2X3(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool2X3_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgConnectParam }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "connect_param", scope = CgNewarrayType.class)
    public JAXBElement<CgConnectParam> createCgNewarrayTypeConnectParam(CgConnectParam value) {
        return new JAXBElement<CgConnectParam>(_CgSetuserTypeConnectParam_QNAME, CgConnectParam.class, CgNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool2x1", scope = CgNewarrayType.class)
    public JAXBElement<List<Boolean>> createCgNewarrayTypeBool2X1(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool2X1_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int", scope = CgNewarrayType.class)
    public JAXBElement<Integer> createCgNewarrayTypeInt(Integer value) {
        return new JAXBElement<Integer>(_CgSetarrayTypeInt_QNAME, Integer.class, CgNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int4x1", scope = CgNewarrayType.class)
    public JAXBElement<List<Integer>> createCgNewarrayTypeInt4X1(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt4X1_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int4x2", scope = CgNewarrayType.class)
    public JAXBElement<List<Integer>> createCgNewarrayTypeInt4X2(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt4X2_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half4x2", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeHalf4X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf4X2_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half4x1", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeHalf4X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf4X1_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half4x4", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeHalf4X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf4X4_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgSamplerRECT }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "samplerRECT", scope = CgNewarrayType.class)
    public JAXBElement<CgSamplerRECT> createCgNewarrayTypeSamplerRECT(CgSamplerRECT value) {
        return new JAXBElement<CgSamplerRECT>(_CgSetarrayTypeSamplerRECT_QNAME, CgSamplerRECT.class, CgNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half4x3", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeHalf4X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf4X3_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool", scope = CgNewarrayType.class)
    public JAXBElement<Boolean> createCgNewarrayTypeBool(Boolean value) {
        return new JAXBElement<Boolean>(_CgSetarrayTypeBool_QNAME, Boolean.class, CgNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "string", scope = CgNewarrayType.class)
    public JAXBElement<String> createCgNewarrayTypeString(String value) {
        return new JAXBElement<String>(_CgSetarrayTypeString_QNAME, String.class, CgNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgNewarrayType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "array", scope = CgNewarrayType.class)
    public JAXBElement<CgNewarrayType> createCgNewarrayTypeArray(CgNewarrayType value) {
        return new JAXBElement<CgNewarrayType>(_CgSetarrayTypeArray_QNAME, CgNewarrayType.class, CgNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int4x3", scope = CgNewarrayType.class)
    public JAXBElement<List<Integer>> createCgNewarrayTypeInt4X3(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt4X3_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed2x3", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFixed2X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed2X3_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int4x4", scope = CgNewarrayType.class)
    public JAXBElement<List<Integer>> createCgNewarrayTypeInt4X4(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt4X4_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed2x2", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFixed2X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed2X2_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed2x4", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFixed2X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed2X4_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float4x3", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFloat4X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat4X3_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float4x2", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFloat4X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat4X2_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool3x3", scope = CgNewarrayType.class)
    public JAXBElement<List<Boolean>> createCgNewarrayTypeBool3X3(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool3X3_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float4x1", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFloat4X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat4X1_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed2x1", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFixed2X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed2X1_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool3x4", scope = CgNewarrayType.class)
    public JAXBElement<List<Boolean>> createCgNewarrayTypeBool3X4(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool3X4_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool3x1", scope = CgNewarrayType.class)
    public JAXBElement<List<Boolean>> createCgNewarrayTypeBool3X1(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool3X1_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool3x2", scope = CgNewarrayType.class)
    public JAXBElement<List<Boolean>> createCgNewarrayTypeBool3X2(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool3X2_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float4x4", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFloat4X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat4X4_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgSurfaceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "surface", scope = CgNewarrayType.class)
    public JAXBElement<CgSurfaceType> createCgNewarrayTypeSurface(CgSurfaceType value) {
        return new JAXBElement<CgSurfaceType>(_CgSetarrayTypeSurface_QNAME, CgSurfaceType.class, CgNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int1x2", scope = CgNewarrayType.class)
    public JAXBElement<List<Integer>> createCgNewarrayTypeInt1X2(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt1X2_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int1x3", scope = CgNewarrayType.class)
    public JAXBElement<List<Integer>> createCgNewarrayTypeInt1X3(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt1X3_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int1x4", scope = CgNewarrayType.class)
    public JAXBElement<List<Integer>> createCgNewarrayTypeInt1X4(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt1X4_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgSampler3D }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "sampler3D", scope = CgNewarrayType.class)
    public JAXBElement<CgSampler3D> createCgNewarrayTypeSampler3D(CgSampler3D value) {
        return new JAXBElement<CgSampler3D>(_CgSetarrayTypeSampler3D_QNAME, CgSampler3D.class, CgNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "enum", scope = CgNewarrayType.class)
    public JAXBElement<String> createCgNewarrayTypeEnum(String value) {
        return new JAXBElement<String>(_CgSetarrayTypeEnum_QNAME, String.class, CgNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgSamplerCUBE }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "samplerCUBE", scope = CgNewarrayType.class)
    public JAXBElement<CgSamplerCUBE> createCgNewarrayTypeSamplerCUBE(CgSamplerCUBE value) {
        return new JAXBElement<CgSamplerCUBE>(_CgSetarrayTypeSamplerCUBE_QNAME, CgSamplerCUBE.class, CgNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int1x1", scope = CgNewarrayType.class)
    public JAXBElement<List<Integer>> createCgNewarrayTypeInt1X1(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt1X1_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half3x3", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeHalf3X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf3X3_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half3x2", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeHalf3X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf3X2_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half3x1", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeHalf3X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf3X1_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed2", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFixed2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed2_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed3", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFixed3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed3_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half3x4", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeHalf3X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf3X4_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link java.lang.Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed1", scope = CgNewarrayType.class)
    public JAXBElement<java.lang.Float> createCgNewarrayTypeFixed1(java.lang.Float value) {
        return new JAXBElement<java.lang.Float>(_CgSetarrayTypeFixed1_QNAME, java.lang.Float.class, CgNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float1x2", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFloat1X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat1X2_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float1x1", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFloat1X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat1X1_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link java.lang.Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half", scope = CgNewarrayType.class)
    public JAXBElement<java.lang.Float> createCgNewarrayTypeHalf(java.lang.Float value) {
        return new JAXBElement<java.lang.Float>(_CgSetarrayTypeHalf_QNAME, java.lang.Float.class, CgNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgSampler2D }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "sampler2D", scope = CgNewarrayType.class)
    public JAXBElement<CgSampler2D> createCgNewarrayTypeSampler2D(CgSampler2D value) {
        return new JAXBElement<CgSampler2D>(_CgSetarrayTypeSampler2D_QNAME, CgSampler2D.class, CgNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int4", scope = CgNewarrayType.class)
    public JAXBElement<List<Integer>> createCgNewarrayTypeInt4(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt4_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int3", scope = CgNewarrayType.class)
    public JAXBElement<List<Integer>> createCgNewarrayTypeInt3(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt3_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool3", scope = CgNewarrayType.class)
    public JAXBElement<List<Boolean>> createCgNewarrayTypeBool3(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool3_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed1x4", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFixed1X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed1X4_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool2", scope = CgNewarrayType.class)
    public JAXBElement<List<Boolean>> createCgNewarrayTypeBool2(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool2_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed1x3", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFixed1X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed1X3_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int2", scope = CgNewarrayType.class)
    public JAXBElement<List<Integer>> createCgNewarrayTypeInt2(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt2_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool4", scope = CgNewarrayType.class)
    public JAXBElement<List<Boolean>> createCgNewarrayTypeBool4(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool4_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int1", scope = CgNewarrayType.class)
    public JAXBElement<Integer> createCgNewarrayTypeInt1(Integer value) {
        return new JAXBElement<Integer>(_CgSetarrayTypeInt1_QNAME, Integer.class, CgNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool4x4", scope = CgNewarrayType.class)
    public JAXBElement<List<Boolean>> createCgNewarrayTypeBool4X4(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool4X4_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool1", scope = CgNewarrayType.class)
    public JAXBElement<Boolean> createCgNewarrayTypeBool1(Boolean value) {
        return new JAXBElement<Boolean>(_CgSetarrayTypeBool1_QNAME, Boolean.class, CgNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool4x2", scope = CgNewarrayType.class)
    public JAXBElement<List<Boolean>> createCgNewarrayTypeBool4X2(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool4X2_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float1x4", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFloat1X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat1X4_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed1x2", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFixed1X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed1X2_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool4x3", scope = CgNewarrayType.class)
    public JAXBElement<List<Boolean>> createCgNewarrayTypeBool4X3(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool4X3_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float1x3", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFloat1X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat1X3_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed1x1", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFixed1X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed1X1_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool4x1", scope = CgNewarrayType.class)
    public JAXBElement<List<Boolean>> createCgNewarrayTypeBool4X1(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool4X1_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link java.lang.Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float1", scope = CgNewarrayType.class)
    public JAXBElement<java.lang.Float> createCgNewarrayTypeFloat1(java.lang.Float value) {
        return new JAXBElement<java.lang.Float>(_CgSetarrayTypeFloat1_QNAME, java.lang.Float.class, CgNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float2", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFloat2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat2_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float3", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFloat3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat3_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float4", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFloat4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat4_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int2x1", scope = CgNewarrayType.class)
    public JAXBElement<List<Integer>> createCgNewarrayTypeInt2X1(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt2X1_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgSampler1D }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "sampler1D", scope = CgNewarrayType.class)
    public JAXBElement<CgSampler1D> createCgNewarrayTypeSampler1D(CgSampler1D value) {
        return new JAXBElement<CgSampler1D>(_CgSetarrayTypeSampler1D_QNAME, CgSampler1D.class, CgNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int2x2", scope = CgNewarrayType.class)
    public JAXBElement<List<Integer>> createCgNewarrayTypeInt2X2(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt2X2_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int2x3", scope = CgNewarrayType.class)
    public JAXBElement<List<Integer>> createCgNewarrayTypeInt2X3(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt2X3_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Integer }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "int2x4", scope = CgNewarrayType.class)
    public JAXBElement<List<Integer>> createCgNewarrayTypeInt2X4(List<Integer> value) {
        return new JAXBElement<List<Integer>>(_CgSetarrayTypeInt2X4_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Integer> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half2x4", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeHalf2X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf2X4_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half2x3", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeHalf2X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf2X3_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half2x2", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeHalf2X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf2X2_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "half2x1", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeHalf2X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeHalf2X1_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CgSamplerDEPTH }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "samplerDEPTH", scope = CgNewarrayType.class)
    public JAXBElement<CgSamplerDEPTH> createCgNewarrayTypeSamplerDEPTH(CgSamplerDEPTH value) {
        return new JAXBElement<CgSamplerDEPTH>(_CgSetarrayTypeSamplerDEPTH_QNAME, CgSamplerDEPTH.class, CgNewarrayType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float2x1", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFloat2X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat2X1_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed4", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFixed4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed4_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed4x4", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFixed4X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed4X4_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed4x1", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFixed4X1(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed4X1_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed4x3", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFixed4X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed4X3_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "fixed4x2", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFixed4X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFixed4X2_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float2x4", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFloat2X4(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat2X4_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float2x3", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFloat2X3(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat2X3_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link java.lang.Float }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "float2x2", scope = CgNewarrayType.class)
    public JAXBElement<List<java.lang.Float>> createCgNewarrayTypeFloat2X2(List<java.lang.Float> value) {
        return new JAXBElement<List<java.lang.Float>>(_CgSetarrayTypeFloat2X2_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<java.lang.Float> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool1x3", scope = CgNewarrayType.class)
    public JAXBElement<List<Boolean>> createCgNewarrayTypeBool1X3(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool1X3_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool1x4", scope = CgNewarrayType.class)
    public JAXBElement<List<Boolean>> createCgNewarrayTypeBool1X4(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool1X4_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool1x1", scope = CgNewarrayType.class)
    public JAXBElement<List<Boolean>> createCgNewarrayTypeBool1X1(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool1X1_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link Boolean }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "bool1x2", scope = CgNewarrayType.class)
    public JAXBElement<List<Boolean>> createCgNewarrayTypeBool1X2(List<Boolean> value) {
        return new JAXBElement<List<Boolean>>(_CgSetarrayTypeBool1X2_QNAME, ((Class) List.class), CgNewarrayType.class, ((List<Boolean> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link BigInteger }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.collada.org/2005/11/COLLADASchema", name = "h", scope = Polygons.Ph.class)
    public JAXBElement<List<BigInteger>> createPolygonsPhH(List<BigInteger> value) {
        return new JAXBElement<List<BigInteger>>(_PolygonsPhH_QNAME, ((Class) List.class), Polygons.Ph.class, ((List<BigInteger> ) value));
    }

}
