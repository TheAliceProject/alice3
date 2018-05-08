
package org.lgna.story.resourceutilities.exporterutils.collada;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fx_sampler_filter_common.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="fx_sampler_filter_common">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *     &lt;enumeration value="NONE"/>
 *     &lt;enumeration value="NEAREST"/>
 *     &lt;enumeration value="LINEAR"/>
 *     &lt;enumeration value="NEAREST_MIPMAP_NEAREST"/>
 *     &lt;enumeration value="LINEAR_MIPMAP_NEAREST"/>
 *     &lt;enumeration value="NEAREST_MIPMAP_LINEAR"/>
 *     &lt;enumeration value="LINEAR_MIPMAP_LINEAR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "fx_sampler_filter_common", namespace = "http://www.collada.org/2005/11/COLLADASchema")
@XmlEnum
public enum FxSamplerFilterCommon {

    NONE,
    NEAREST,
    LINEAR,
    NEAREST_MIPMAP_NEAREST,
    LINEAR_MIPMAP_NEAREST,
    NEAREST_MIPMAP_LINEAR,
    LINEAR_MIPMAP_LINEAR;

    public String value() {
        return name();
    }

    public static FxSamplerFilterCommon fromValue(String v) {
        return valueOf(v);
    }

}
