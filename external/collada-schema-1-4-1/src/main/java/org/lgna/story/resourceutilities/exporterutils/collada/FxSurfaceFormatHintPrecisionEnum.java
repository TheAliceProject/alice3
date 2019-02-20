
package org.lgna.story.resourceutilities.exporterutils.collada;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fx_surface_format_hint_precision_enum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="fx_surface_format_hint_precision_enum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="LOW"/>
 *     &lt;enumeration value="MID"/>
 *     &lt;enumeration value="HIGH"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "fx_surface_format_hint_precision_enum", namespace = "http://www.collada.org/2005/11/COLLADASchema")
@XmlEnum
public enum FxSurfaceFormatHintPrecisionEnum {


    /**
     * For integers this typically represents 8 bits.  For floats typically 16 bits.
     * 
     */
    LOW,

    /**
     * For integers this typically represents 8 to 24 bits.  For floats typically 16 to 32 bits.
     * 
     */
    MID,

    /**
     * For integers this typically represents 16 to 32 bits.  For floats typically 24 to 32 bits.
     * 
     */
    HIGH;

    public String value() {
        return name();
    }

    public static FxSurfaceFormatHintPrecisionEnum fromValue(String v) {
        return valueOf(v);
    }

}
