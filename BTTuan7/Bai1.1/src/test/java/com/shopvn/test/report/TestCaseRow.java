package com.shopvn.test.report;
public class TestCaseRow {
    private String group; private String tcId; private String moTa;
    private String precondition; private String inputData; private String kyThuat;
    private String expected; private String actualResult; private String status; private String note;
    public TestCaseRow(String group,String tcId,String moTa,String precondition,
                       String inputData,String kyThuat,String expected) {
        this.group=group; this.tcId=tcId; this.moTa=moTa; this.precondition=precondition;
        this.inputData=inputData; this.kyThuat=kyThuat; this.expected=expected;
        this.actualResult=""; this.status=""; this.note="";
    }
    public String getGroup(){return group;} public void setGroup(String v){group=v;}
    public String getTcId(){return tcId;} public void setTcId(String v){tcId=v;}
    public String getMoTa(){return moTa;} public void setMoTa(String v){moTa=v;}
    public String getPrecondition(){return precondition;} public void setPrecondition(String v){precondition=v;}
    public String getInputData(){return inputData;} public void setInputData(String v){inputData=v;}
    public String getKyThuat(){return kyThuat;} public void setKyThuat(String v){kyThuat=v;}
    public String getExpected(){return expected;} public void setExpected(String v){expected=v;}
    public String getActualResult(){return actualResult;} public void setActualResult(String v){actualResult=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public String getNote(){return note;} public void setNote(String v){note=v;}
}
