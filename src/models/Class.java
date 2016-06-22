package models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Class {
    String name;
    String classString;

    @XmlElement
    public String getClassName(){
        return name;
    }

    @XmlElement
    public String getClassString(){
        return classString;
    }
}
