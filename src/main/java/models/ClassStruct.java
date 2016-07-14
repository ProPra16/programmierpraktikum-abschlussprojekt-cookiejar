package models;

public class ClassStruct {
    private String className;
    private String code;
    private boolean isTest;     //used to differ between class and test instead of using an interface

    public ClassStruct() {
    }

    public ClassStruct(String className, String code, boolean isTest) {
        this.className = className;
        this.code = code;
        this.isTest = isTest;
    }

    public void setName(String className) {
        this.className = className;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setTest(boolean test) {
        isTest = test;
    }

    public String getName() {
        return className;
    }

    public String getCode() {
        return code;
    }

    public boolean isTest() {
        return isTest;
    }
}
