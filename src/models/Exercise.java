package models;

import java.util.List;

public class Exercise {
    String description;
    String name;
    List<String> classes;
    List<String> tests;

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

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes;
    }

    public void addClasses(String classString){
        this.classes.add(classString);
    }

    public List<String> getTests() {
        return tests;
    }

    public void setTests(List<String> tests) {
        this.tests = tests;
    }

    public void addTests(String testString){
        this.tests.add(testString);
    }
}
