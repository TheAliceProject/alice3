
package org.lgna.story.resourceutilities.exporterutils.collada;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fx_pipeline_stage_common.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="fx_pipeline_stage_common">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="VERTEXPROGRAM"/>
 *     &lt;enumeration value="FRAGMENTPROGRAM"/>
 *     &lt;enumeration value="VERTEXSHADER"/>
 *     &lt;enumeration value="PIXELSHADER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "fx_pipeline_stage_common", namespace = "http://www.collada.org/2005/11/COLLADASchema")
@XmlEnum
public enum FxPipelineStageCommon {

    VERTEXPROGRAM,
    FRAGMENTPROGRAM,
    VERTEXSHADER,
    PIXELSHADER;

    public String value() {
        return name();
    }

    public static FxPipelineStageCommon fromValue(String v) {
        return valueOf(v);
    }

}
