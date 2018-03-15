
package org.lgna.story.resourceutilities.exporterutils.collada;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fx_sampler_wrap_common.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="fx_sampler_wrap_common">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *     &lt;enumeration value="NONE"/>
 *     &lt;enumeration value="WRAP"/>
 *     &lt;enumeration value="MIRROR"/>
 *     &lt;enumeration value="CLAMP"/>
 *     &lt;enumeration value="BORDER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "fx_sampler_wrap_common", namespace = "http://www.collada.org/2005/11/COLLADASchema")
@XmlEnum
public enum FxSamplerWrapCommon {

    NONE,
    WRAP,
    MIRROR,
    CLAMP,
    BORDER;

    public String value() {
        return name();
    }

    public static FxSamplerWrapCommon fromValue(String v) {
        return valueOf(v);
    }

}
