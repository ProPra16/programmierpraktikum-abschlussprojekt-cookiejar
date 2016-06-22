package models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Class {
    String name;

    @XmlElement
    public String getClassEx(){
        return name;
    }
}
