package models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class Tests {
    List<Test> tests;
    @XmlElementWrapper
    @XmlElement
    public List<Test> getTests(){
        return tests;
    }
}
