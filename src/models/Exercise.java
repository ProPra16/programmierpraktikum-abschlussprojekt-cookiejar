package models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Exercise {
    String name;

    @XmlElement
    public String getEx(){
        return name;
    }
}
