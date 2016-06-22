package models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Test {
    String name;

    @XmlElement
    public String getTestEx(){
        return name;
    }
}
