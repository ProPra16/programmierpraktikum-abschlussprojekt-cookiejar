package models;

import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;

public class CodeTab extends Tab {
    private boolean isTest;
    private boolean isAcceptance;
    private TextArea code;

    public CodeTab() {
        super();
        code = new TextArea();
        this.setContent(code);
        isTest = false;
        isAcceptance = false;
        setClosable(false);
    }

    public CodeTab(ClassStruct cs) {
        super();
        code = new TextArea();
        this.setContent(code);
        this.setText(cs.getName());
        code.setText(cs.getCode());
        isTest = cs.isTest();
        setClosable(false);
    }

    public void setCode(String code){
        this.code = new TextArea();
        this.setContent(this.code);
    }

    public String getCode() {
        return code.getText();
    }

    public boolean isTest() {
        return isTest;
    }

    public boolean isAcceptance() {return isAcceptance;}

    public void setEditable(boolean editable) {
        code.setEditable(editable);
    }

    public void setAcceptance(boolean acceptance) {
        isAcceptance = acceptance;
    }
}
