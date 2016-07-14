package models;

import java.util.List;

public class Exercise {
    String description;
    String name;
    List<ClassStruct> classes;
    List<ClassStruct> tests;
    String identifier;

    public Exercise(){
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ClassStruct> getClasses() {
        return classes;
    }

    public void setClasses(List<ClassStruct> classes) {
        this.classes = classes;
    }

    public void addClasses(ClassStruct classString){
        this.classes.add(classString);
    }

    public List<ClassStruct> getTests() {
        return tests;
    }

    public void setTests(List<ClassStruct> tests) {
        this.tests = tests;
    }

    public void addTests(ClassStruct testString){
        this.tests.add(testString);
    }

    public void setIdentifier(String identifier) {this.identifier = identifier;}

    public String getIdentifier() {return identifier;}
}
