package com.shopvn.test.report;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExcelReportGenerator {
    public static final int COL_TC_ID=0,COL_MO_TA=1,COL_PRECOND=2,COL_INPUT=3;
    public static final int COL_KY_THUAT=4,COL_EXPECTED=5,COL_ACTUAL=6,COL_STATUS=7,COL_NOTE=8;
    private final String outputPath;
    private final XSSFWorkbook workbook;
    private CellStyle headerStyle,dataStyle,passStyle,failStyle,naStyle,titleStyle,sectionStyle;

    public ExcelReportGenerator(String outputPath) {
        this.outputPath=outputPath; this.workbook=new XSSFWorkbook(); initStyles();
    }
    private void initStyles(){
        titleStyle=workbook.createCellStyle(); Font tf=workbook.createFont(); tf.setBold(true); tf.setFontHeightInPoints((short)13); titleStyle.setFont(tf); titleStyle.setAlignment(HorizontalAlignment.CENTER); titleStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex()); titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); border(titleStyle);
        headerStyle=workbook.createCellStyle(); Font hf=workbook.createFont(); hf.setBold(true); hf.setColor(IndexedColors.WHITE.getIndex()); headerStyle.setFont(hf); headerStyle.setAlignment(HorizontalAlignment.CENTER); headerStyle.setVerticalAlignment(VerticalAlignment.CENTER); headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex()); headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); headerStyle.setWrapText(true); border(headerStyle);
        dataStyle=workbook.createCellStyle(); dataStyle.setWrapText(true); dataStyle.setVerticalAlignment(VerticalAlignment.TOP); border(dataStyle);
        passStyle=workbook.createCellStyle(); Font pf=workbook.createFont(); pf.setBold(true); pf.setColor(IndexedColors.WHITE.getIndex()); passStyle.setFont(pf); passStyle.setAlignment(HorizontalAlignment.CENTER); passStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex()); passStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); border(passStyle);
        failStyle=workbook.createCellStyle(); Font ff=workbook.createFont(); ff.setBold(true); ff.setColor(IndexedColors.WHITE.getIndex()); failStyle.setFont(ff); failStyle.setAlignment(HorizontalAlignment.CENTER); failStyle.setFillForegroundColor(IndexedColors.RED.getIndex()); failStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); border(failStyle);
        naStyle=workbook.createCellStyle(); naStyle.setAlignment(HorizontalAlignment.CENTER); naStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex()); naStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); border(naStyle);
        sectionStyle=workbook.createCellStyle(); Font sf=workbook.createFont(); sf.setBold(true); sf.setColor(IndexedColors.WHITE.getIndex()); sectionStyle.setFont(sf); sectionStyle.setFillForegroundColor(IndexedColors.DARK_TEAL.getIndex()); sectionStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); border(sectionStyle);
    }
    private void border(CellStyle s){ s.setBorderTop(BorderStyle.THIN); s.setBorderBottom(BorderStyle.THIN); s.setBorderLeft(BorderStyle.THIN); s.setBorderRight(BorderStyle.THIN); }

    public void createTestCaseSheet(List<TestCaseRow> tcs) {
        XSSFSheet sh=workbook.createSheet("TC_DangKy_ShopVN");
        Row tr=sh.createRow(0); tr.setHeightInPoints(28);
        Cell tc=tr.createCell(0); tc.setCellValue("BAO CAO TEST CASE - FORM DANG KY TAI KHOAN SHOPVN"); tc.setCellStyle(titleStyle);
        sh.addMergedRegion(new CellRangeAddress(0,0,0,8));
        Row ir=sh.createRow(1); Cell ic=ir.createCell(0);
        ic.setCellValue("Mon: Kiem Thu Phan Mem | Bai 1.1: EP & BVA | Ngay: "+LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        sh.addMergedRegion(new CellRangeAddress(1,1,0,8));
        Row hr=sh.createRow(2); hr.setHeightInPoints(36);
        String[] headers={"TC ID","Mo ta muc dich","Dieu kien tien quyet","Du lieu dau vao","Ky thuat\n(EP/BVA/SPEC)","Ket qua mong doi","Ket qua thuc te","Trang thai\n(PASS/FAIL)","Ghi chu"};
        for(int i=0;i<headers.length;i++){Cell c=hr.createCell(i);c.setCellValue(headers[i]);c.setCellStyle(headerStyle);}
        String last=""; int ri=3;
        for(TestCaseRow row:tcs){
            if(!row.getGroup().equals(last)){
                Row sr=sh.createRow(ri++); sr.setHeightInPoints(18);
                Cell sc=sr.createCell(0); sc.setCellValue(">> "+row.getGroup()); sc.setCellStyle(sectionStyle);
                sh.addMergedRegion(new CellRangeAddress(ri-1,ri-1,0,8)); last=row.getGroup();
            }
            Row r=sh.createRow(ri++); r.setHeightInPoints(45);
            Cell idc=r.createCell(0); idc.setCellValue(row.getTcId()); idc.setCellStyle(headerStyle);
            dc(r,1,row.getMoTa()); dc(r,2,row.getPrecondition()); dc(r,3,row.getInputData());
            dc(r,4,row.getKyThuat()); dc(r,5,row.getExpected());
            Cell ac=r.createCell(6); String a=row.getActualResult();
            if(a!=null&&!a.isEmpty()){ac.setCellValue(a);ac.setCellStyle(dataStyle);}else{ac.setCellValue("(chua chay)");ac.setCellStyle(naStyle);}
            Cell sc2=r.createCell(7); String st=row.getStatus();
            if("PASS".equalsIgnoreCase(st)){sc2.setCellValue("PASS");sc2.setCellStyle(passStyle);}
            else if("FAIL".equalsIgnoreCase(st)){sc2.setCellValue("FAIL");sc2.setCellStyle(failStyle);}
            else{sc2.setCellValue(st!=null?st:"");sc2.setCellStyle(naStyle);}
            dc(r,8,row.getNote()!=null?row.getNote():"");
        }
        int[] cw={5000,9000,7000,10000,4500,8000,8000,4000,6000};
        for(int i=0;i<cw.length;i++) sh.setColumnWidth(i,cw[i]);
        sh.createFreezePane(0,3);
    }
    private void dc(Row r,int col,String v){Cell c=r.createCell(col);c.setCellValue(v!=null?v:"");c.setCellStyle(dataStyle);}

    public void createEPSheet(){
        XSSFSheet sh=workbook.createSheet("EP_PhanHoachTuongDuong");
        Row tr=sh.createRow(0); Cell tc=tr.createCell(0); tc.setCellValue("PHAN HOACH TUONG DUONG - FORM DANG KY SHOPVN"); tc.setCellStyle(titleStyle); sh.addMergedRegion(new CellRangeAddress(0,0,0,3));
        Row hr=sh.createRow(1); hr.setHeightInPoints(30);
        String[] h={"Truong","Lop HOP LE (gia tri dai dien)","Lop KHONG HOP LE (gia tri dai dien)","Ly do / Quy tac phan lop"};
        for(int i=0;i<h.length;i++){Cell c=hr.createCell(i);c.setCellValue(h[i]);c.setCellStyle(headerStyle);}
        String[][] d={
            {"Ho va ten (*)","EP1_V1: Chi chu cai Unicode va dau cach, 2-50 ky tu (VD: \"Nguyen Van A\")","EP1_I1: Rong (\"\")\nEP1_I2: Chua chu so (\"Nguyen123\")\nEP1_I3: Chua ky tu dac biet (\"Nguyen@\")","Bat buoc; chi chu cai Unicode + dau cach; do dai 2-50"},
            {"Ten dang nhap (*)","EP2_V1: Bat dau chu thuong, chua a-z/0-9/_, 5-20 ky tu","EP2_I1: Rong\nEP2_I2: Bat dau bang so (\"1user\")\nEP2_I3: Chu hoa (\"User01\")\nEP2_I4: Ky tu dac biet (\"user@01\")\nEP2_I5: <5 ky tu\nEP2_I6: >20 ky tu\nEP2_I7: Da ton tai CSDL","Bat buoc; [a-z][a-z0-9_]*; dai 5-20; duy nhat"},
            {"Email (*)","EP3_V1: Dung dinh dang RFC 5322, chua dang ky (VD: newuser@gmail.com)","EP3_I1: Rong\nEP3_I2: Khong co @ (usermail.com)\nEP3_I3: Khong co domain (user@)\nEP3_I4: Email da dang ky","Bat buoc; RFC 5322; chua ton tai CSDL"},
            {"So dien thoai (*)","EP4_V1: Bat dau bang 0, dung 10 chu so (VD: 0987654321)","EP4_I1: Rong\nEP4_I2: Khong bat dau 0 (1987654321)\nEP4_I3: Chua chu cai (098765432a)\nEP4_I4: Thieu so - 9 ky tu\nEP4_I5: Thua so - 11 ky tu","Bat buoc; bat dau 0; dung 10 chu so"},
            {"Mat khau (*)","EP5_V1: 8-32 ky tu, du hoa+thuong+so+dac biet (VD: Pass@1234)","EP5_I1: Rong\nEP5_I2: Thieu chu hoa (pass@1234)\nEP5_I3: Thieu chu thuong (PASS@1234)\nEP5_I4: Thieu chu so (Pass@word)\nEP5_I5: Thieu ky tu dac biet (Password1)\nEP5_I6: <8 ky tu","Bat buoc; 8-32 ky tu; >=1 hoa, >=1 thuong, >=1 so, >=1 dac biet"},
            {"Xac nhan mat khau (*)","EP6_V1: Trung khop voi mat khau da nhap","EP6_I1: Rong\nEP6_I2: Khong khop voi mat khau","Bat buoc; phai trung khop hoan toan"},
            {"Ngay sinh (tuy chon)","EP7_V1: dd/MM/yyyy, tuoi 16-99 (VD: 15/03/1990)\nEP7_V2: Rong (khong dien - hop le)","EP7_I1: Sai dinh dang (1990-03-15)\nEP7_I2: Duoi 16 tuoi (01/01/2015)\nEP7_I3: Tu 100 tuoi tro len (01/01/1900)","Khong bat buoc; neu nhap: dd/MM/yyyy, tu 16 den duoi 100 tuoi"},
            {"Ma gioi thieu (tuy chon)","EP8_V1: 8 ky tu [A-Z0-9], co trong CSDL (VD: ABC12345)\nEP8_V2: Rong - hop le","EP8_I1: Chu thuong (abc12345)\nEP8_I2: <8 ky tu (ABC1234)\nEP8_I3: >8 ky tu\nEP8_I4: Dung format nhung khong ton tai (ZZZZZZZZ)","Khong bat buoc; neu nhap: dung 8 ky tu [A-Z0-9]; phai ton tai CSDL"},
            {"Dong y Dieu khoan (*)","EP9_V1: Checkbox duoc tich (true)","EP9_I1: Checkbox khong duoc tich (false)","Bat buoc phai tich; nut Dang ky bi disabled khi chua tich"}
        };
        for(int i=0;i<d.length;i++){Row rr=sh.createRow(i+2); rr.setHeightInPoints(70); for(int j=0;j<d[i].length;j++){Cell c=rr.createCell(j);c.setCellValue(d[i][j]);c.setCellStyle(dataStyle);}}
        sh.setColumnWidth(0,6500); sh.setColumnWidth(1,13000); sh.setColumnWidth(2,16000); sh.setColumnWidth(3,12000);
        sh.createFreezePane(0,2);
    }

    public void createBVASheet(){
        XSSFSheet sh=workbook.createSheet("BVA_GiaTriBien");
        Row tr=sh.createRow(0); Cell tc=tr.createCell(0); tc.setCellValue("PHAN TICH GIA TRI BIEN - FORM DANG KY SHOPVN (Ref date: 03/03/2026)"); tc.setCellStyle(titleStyle); sh.addMergedRegion(new CellRangeAddress(0,0,0,4));
        Row hr=sh.createRow(1); hr.setHeightInPoints(30);
        String[] h={"Truong","Bien (boundary)","Gia tri test tai bien (min-1, min, min+1, max-1, max, max+1)","Loai (valid/invalid)","Ket qua mong doi"};
        for(int i=0;i<h.length;i++){Cell c=hr.createCell(i);c.setCellValue(h[i]);c.setCellStyle(headerStyle);}
        String[][] d={
            {"Ho va ten (*)","Bien duoi - do dai toi thieu = 2","min-1 = 1 ky tu: \"A\"","INVALID","Loi: Qua ngan"},
            {"","","min = 2 ky tu: \"An\"","VALID","Dang ky thanh cong"},
            {"","","min+1 = 3 ky tu: \"Ana\"","VALID","Dang ky thanh cong"},
            {"Ho va ten (*)","Bien tren - do dai toi da = 50","max-1 = 49 ky tu: chui 49 ky tu hop le","VALID","Dang ky thanh cong"},
            {"","","max = 50 ky tu: chui 50 ky tu hop le","VALID","Dang ky thanh cong"},
            {"","","max+1 = 51 ky tu: chui 51 ky tu","INVALID","Loi: Qua dai"},
            {"Ten dang nhap (*)","Bien duoi - do dai toi thieu = 5","min-1 = 4 ky tu: \"user\"","INVALID","Loi: Qua ngan"},
            {"","","min = 5 ky tu: \"user1\"","VALID","Dang ky thanh cong"},
            {"","","min+1 = 6 ky tu: \"user01\"","VALID","Dang ky thanh cong"},
            {"Ten dang nhap (*)","Bien tren - do dai toi da = 20","max-1 = 19 ky tu","VALID","Dang ky thanh cong"},
            {"","","max = 20 ky tu","VALID","Dang ky thanh cong"},
            {"","","max+1 = 21 ky tu","INVALID","Loi: Qua dai"},
            {"Mat khau (*)","Bien duoi - do dai toi thieu = 8","min-1 = 7 ky tu: \"Pa@1234\"","INVALID","Loi: Qua ngan"},
            {"","","min = 8 ky tu: \"Pa@12345\"","VALID","Dang ky thanh cong"},
            {"","","min+1 = 9 ky tu: \"Pa@123456\"","VALID","Dang ky thanh cong"},
            {"Mat khau (*)","Bien tren - do dai toi da = 32","max-1 = 31 ky tu","VALID","Dang ky thanh cong"},
            {"","","max = 32 ky tu","VALID","Dang ky thanh cong"},
            {"","","max+1 = 33 ky tu","INVALID","Loi: Qua dai"},
            {"Ngay sinh (tuy chon)","Bien duoi - tuoi toi thieu = 16 (ref: 03/03/2026)","min-1: 04/03/2010 (15 tuoi 364 ngay)","INVALID","Loi: Chua du 16 tuoi"},
            {"","","min: 03/03/2010 (dung 16 tuoi)","VALID","Dang ky thanh cong"},
            {"","","min+1: 02/03/2010 (16 tuoi 1 ngay)","VALID","Dang ky thanh cong"},
            {"Ngay sinh (tuy chon)","Bien tren - tuoi toi da < 100 (ref: 03/03/2026)","max-1: 04/03/1926 (99 tuoi 364 ngay)","VALID","Dang ky thanh cong"},
            {"","","max: 03/03/1926 (dung 100 tuoi)","INVALID","Loi: Tu 100 tuoi tro len"},
            {"","","max+1: 02/03/1926 (100 tuoi 1 ngay)","INVALID","Loi: Tu 100 tuoi tro len"}
        };
        for(int i=0;i<d.length;i++){Row rr=sh.createRow(i+2); rr.setHeightInPoints(35); for(int j=0;j<d[i].length;j++){Cell c=rr.createCell(j);c.setCellValue(d[i][j]);c.setCellStyle(dataStyle);}}
        sh.setColumnWidth(0,6500); sh.setColumnWidth(1,9000); sh.setColumnWidth(2,14000); sh.setColumnWidth(3,5000); sh.setColumnWidth(4,9000);
        sh.createFreezePane(0,2);
    }

    public void write() throws IOException {
        new java.io.File(outputPath).getParentFile().mkdirs();
        try(FileOutputStream fos=new FileOutputStream(outputPath)){workbook.write(fos);}
        workbook.close(); System.out.println("[ExcelReportGenerator] Saved: "+outputPath);
    }
}
