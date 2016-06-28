package models;

public class ClassStruct {
    private String className;
    private String code;
    private boolean isTest;

    public ClassStruct(String className, String code, boolean isTest) {
        this.className = className;
        this.code = code;
        this.isTest = isTest;
    }

    public String getClassName() {
        return className;
    }

    public String getCode() {
        return code;
    }

    public boolean isTest() {
        return isTest;
    }
}
