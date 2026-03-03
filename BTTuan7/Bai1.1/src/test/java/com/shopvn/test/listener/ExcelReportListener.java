package com.shopvn.test.listener;
import com.shopvn.test.report.ExcelReportGenerator;
import com.shopvn.test.report.TestCaseRow;
import com.shopvn.test.data.RegistrationTestCaseFactory;
import org.testng.*;
import java.io.*; import java.time.LocalDateTime; import java.time.format.DateTimeFormatter;
import java.util.*; import java.util.concurrent.ConcurrentHashMap;

public class ExcelReportListener implements ISuiteListener, ITestListener {
    private static final String REPORTS_DIR = "reports";
    private static final Map<String,String> testResults = new ConcurrentHashMap<>();

    public void onStart(ISuite s) { System.out.println("[Listener] Suite start: "+s.getName()); new File(REPORTS_DIR).mkdirs(); }
    public void onFinish(ISuite s) { System.out.println("[Listener] Suite finish. Generating Excel..."); generateReport(); }
    public void onTestSuccess(ITestResult r) { testResults.put(r.getMethod().getMethodName(),"PASS"); }
    public void onTestFailure(ITestResult r) { testResults.put(r.getMethod().getMethodName(),"FAIL"); }
    public void onTestSkipped(ITestResult r) { testResults.put(r.getMethod().getMethodName(),"SKIP"); }

    private void generateReport() {
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = REPORTS_DIR+"/BaoCao_TestCase_DangKy_"+ts+".xlsx";
        try {
            ExcelReportGenerator gen = new ExcelReportGenerator(filename);
            List<TestCaseRow> all = RegistrationTestCaseFactory.getAllTestCases();
            for(TestCaseRow row:all){
                String r=testResults.get("test_"+row.getTcId().replace("-","_"));
                if(r!=null){row.setStatus(r); row.setActualResult("PASS".equals(r)?"He thong xu ly dung.":"Ket qua khong khop. Xem log.");}
            }
            gen.createTestCaseSheet(all);
            gen.createEPSheet();
            gen.createBVASheet();
            gen.write();
            System.out.println("[Listener] Excel report: "+filename);
        } catch(IOException e) { System.err.println("[Listener] Error: "+e.getMessage()); }
    }
}
