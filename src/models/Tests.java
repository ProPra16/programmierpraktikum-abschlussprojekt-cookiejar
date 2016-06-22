package models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public class Tests {
    List<Test> tests;
    @XmlElementWrapper
    @XmlElement
    public List<Test> getTests(){
        return tests;
    }
}
