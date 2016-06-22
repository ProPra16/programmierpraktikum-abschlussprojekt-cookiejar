package models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Test {
    String name;
    String textString;

    @XmlElement
    public String getClassName(){
        return name;
    }

    @XmlElement
    public String getTextString(){
        return textString;
    }
}
