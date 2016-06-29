package models;

import java.util.List;

public class Exercise {
    String description;
    String name;
    List<Class> classes;
    List<Test> tests;

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

    public List<Class> getClasses() {
        return classes;
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
    }

    public void addClasses(Class classString){
        this.classes.add(classString);
    }

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }

    public void addTests(Test testString){
        this.tests.add(testString);
    }
}
