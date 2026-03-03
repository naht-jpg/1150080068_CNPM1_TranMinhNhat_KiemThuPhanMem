package com.shopvn.test.data;
import com.shopvn.form.RegistrationForm;
import com.shopvn.test.report.TestCaseRow;
import java.util.*;

public class RegistrationTestCaseFactory {
    private static final String PRE = "He thong ShopVN hoat dong.\nTrang dang ky da mo.\nEmail 'existing@shopvn.com' va username 'user_exist' da ton tai CSDL.\nMa gioi thieu 'ABC12345' ton tai CSDL.";

    public static RegistrationForm baseValidForm(){
        return new RegistrationForm("Nguyen Van An","newuser01","newuser01@gmail.com","0987654321","Pass@1234","Pass@1234",true,"15/03/1990","Nam","");
    }

    public static List<TestCaseRow> getAllTestCases(){
        List<TestCaseRow> list=new ArrayList<>();
        // EP
        list.add(tc("EP - Phan Hoach Tuong Duong","TC_REG_EP_001","Valid: Form day du hop le",PRE,"Ho va ten:'Nguyen Van An', TenDN:'newuser01', Email:'newuser01@gmail.com', SDT:'0987654321', MK:'Pass@1234', Xac nhan:'Pass@1234', DongY:true","EP","Dang ky thanh cong"));
        list.add(tc("EP - Phan Hoach Tuong Duong","TC_REG_EP_002","Ho va ten bi bo trong",PRE,"HO VA TEN: '' (rong) | Cac truong khac: hop le","EP - EP1_I1","Loi: Ho va ten khong duoc de trong"));
        list.add(tc("EP - Phan Hoach Tuong Duong","TC_REG_EP_003","Ho va ten chua chu so",PRE,"HO VA TEN: 'Nguyen123'","EP - EP1_I2","Loi: Ho va ten chi duoc chua chu cai va dau cach"));
        list.add(tc("EP - Phan Hoach Tuong Duong","TC_REG_EP_004","Ho va ten chua ky tu dac biet",PRE,"HO VA TEN: 'Nguyen@An'","EP - EP1_I3","Loi: Ho va ten chi duoc chua chu cai va dau cach"));
        list.add(tc("EP - Phan Hoach Tuong Duong","TC_REG_EP_005","Ten dang nhap bi bo trong",PRE,"TEN DN: '' (rong)","EP - EP2_I1","Loi: Ten dang nhap khong duoc de trong"));
        list.add(tc("EP - Phan Hoach Tuong Duong","TC_REG_EP_006","Ten dang nhap bat dau bang so",PRE,"TEN DN: '1user2'","EP - EP2_I2","Loi: Ten dang nhap phai bat dau bang chu cai"));
        list.add(tc("EP - Phan Hoach Tuong Duong","TC_REG_EP_007","Ten dang nhap chua chu hoa",PRE,"TEN DN: 'UserABC'","EP - EP2_I3","Loi: Ten dang nhap chi gom chu thuong, so, gach duoi"));
        list.add(tc("EP - Phan Hoach Tuong Duong","TC_REG_EP_008","Ten dang nhap chua ky tu dac biet (@)",PRE,"TEN DN: 'user@01'","EP - EP2_I4","Loi: Ten dang nhap chi gom chu thuong, so, gach duoi"));
        list.add(tc("EP - Phan Hoach Tuong Duong","TC_REG_EP_009","Email bi bo trong",PRE,"EMAIL: '' (rong)","EP - EP3_I1","Loi: Email khong duoc de trong"));
        list.add(tc("EP - Phan Hoach Tuong Duong","TC_REG_EP_010","Email khong co ky tu @",PRE,"EMAIL: 'usermail.com'","EP - EP3_I2","Loi: Email khong dung dinh dang (RFC 5322)"));
        list.add(tc("EP - Phan Hoach Tuong Duong","TC_REG_EP_011","Email khong co domain",PRE,"EMAIL: 'user@'","EP - EP3_I3","Loi: Email khong dung dinh dang (RFC 5322)"));
        list.add(tc("EP - Phan Hoach Tuong Duong","TC_REG_EP_012","So dien thoai bi bo trong",PRE,"SDT: '' (rong)","EP - EP4_I1","Loi: So dien thoai khong duoc de trong"));
        list.add(tc("EP - Phan Hoach Tuong Duong","TC_REG_EP_013","SDT khong bat dau bang 0",PRE,"SDT: '1987654321'","EP - EP4_I2","Loi: So dien thoai phai bat dau bang 0 va gom dung 10 chu so"));
        list.add(tc("EP - Phan Hoach Tuong Duong","TC_REG_EP_014","Mat khau khong co chu hoa",PRE,"MAT KHAU: 'pass@1234'","EP - EP5_I2","Loi: Mat khau phai co it nhat 1 chu hoa"));
        list.add(tc("EP - Phan Hoach Tuong Duong","TC_REG_EP_015","Mat khau khong co chu thuong",PRE,"MAT KHAU: 'PASS@1234'","EP - EP5_I3","Loi: Mat khau phai co it nhat 1 chu thuong"));
        // BVA
        list.add(tc("BVA - Gia Tri Bien","TC_REG_BVA_001","Ho va ten 1 ky tu (min-1)",PRE,"HO VA TEN: 'A' (1 ky tu)","BVA - min-1","Loi: Qua ngan (toi thieu 2 ky tu)"));
        list.add(tc("BVA - Gia Tri Bien","TC_REG_BVA_002","Ho va ten 2 ky tu (min)",PRE,"HO VA TEN: 'An' (2 ky tu)","BVA - min","Dang ky thanh cong"));
        list.add(tc("BVA - Gia Tri Bien","TC_REG_BVA_003","Ho va ten 3 ky tu (min+1)",PRE,"HO VA TEN: 'Ana' (3 ky tu)","BVA - min+1","Dang ky thanh cong"));
        list.add(tc("BVA - Gia Tri Bien","TC_REG_BVA_004","Ho va ten 49 ky tu (max-1)",PRE,"HO VA TEN: 49 ky tu hop le","BVA - max-1","Dang ky thanh cong"));
        list.add(tc("BVA - Gia Tri Bien","TC_REG_BVA_005","Ho va ten 50 ky tu (max)",PRE,"HO VA TEN: 50 ky tu hop le","BVA - max","Dang ky thanh cong"));
        list.add(tc("BVA - Gia Tri Bien","TC_REG_BVA_006","Ho va ten 51 ky tu (max+1)",PRE,"HO VA TEN: 51 ky tu","BVA - max+1","Loi: Qua dai (toi da 50 ky tu)"));
        list.add(tc("BVA - Gia Tri Bien","TC_REG_BVA_007","Ten dang nhap 4 ky tu (min-1)",PRE,"TEN DN: 'user' (4 ky tu)","BVA - min-1","Loi: Qua ngan (toi thieu 5 ky tu)"));
        list.add(tc("BVA - Gia Tri Bien","TC_REG_BVA_008","Ten dang nhap 5 ky tu (min)",PRE,"TEN DN: 'user1' (5 ky tu)","BVA - min","Dang ky thanh cong"));
        list.add(tc("BVA - Gia Tri Bien","TC_REG_BVA_009","Ten dang nhap 6 ky tu (min+1)",PRE,"TEN DN: 'user01' (6 ky tu)","BVA - min+1","Dang ky thanh cong"));
        list.add(tc("BVA - Gia Tri Bien","TC_REG_BVA_010","Ten dang nhap 19 ky tu (max-1)",PRE,"TEN DN: 19 ky tu hop le","BVA - max-1","Dang ky thanh cong"));
        list.add(tc("BVA - Gia Tri Bien","TC_REG_BVA_011","Ten dang nhap 20 ky tu (max)",PRE,"TEN DN: 20 ky tu hop le","BVA - max","Dang ky thanh cong"));
        list.add(tc("BVA - Gia Tri Bien","TC_REG_BVA_012","Ten dang nhap 21 ky tu (max+1)",PRE,"TEN DN: 21 ky tu","BVA - max+1","Loi: Qua dai (toi da 20 ky tu)"));
        list.add(tc("BVA - Gia Tri Bien","TC_REG_BVA_013","Mat khau 7 ky tu (min-1)",PRE,"MAT KHAU: 'Pa@1234' (7 ky tu)","BVA - min-1","Loi: Qua ngan (toi thieu 8 ky tu)"));
        list.add(tc("BVA - Gia Tri Bien","TC_REG_BVA_014","Mat khau 8 ky tu (min)",PRE,"MAT KHAU: 'Pa@12345' (8 ky tu, du dieu kien)","BVA - min","Dang ky thanh cong"));
        list.add(tc("BVA - Gia Tri Bien","TC_REG_BVA_015","Mat khau 9 ky tu (min+1)",PRE,"MAT KHAU: 'Pa@123456' (9 ky tu)","BVA - min+1","Dang ky thanh cong"));
        list.add(tc("BVA - Gia Tri Bien","TC_REG_BVA_016","Mat khau 31 ky tu (max-1)",PRE,"MAT KHAU: 31 ky tu hop le","BVA - max-1","Dang ky thanh cong"));
        list.add(tc("BVA - Gia Tri Bien","TC_REG_BVA_017","Mat khau 32 ky tu (max)",PRE,"MAT KHAU: 32 ky tu hop le","BVA - max","Dang ky thanh cong"));
        list.add(tc("BVA - Gia Tri Bien","TC_REG_BVA_018","Mat khau 33 ky tu (max+1)",PRE,"MAT KHAU: 33 ky tu","BVA - max+1","Loi: Qua dai (toi da 32 ky tu)"));
        list.add(tc("BVA - Gia Tri Bien","TC_REG_BVA_019","Ngay sinh 04/03/2010 - 15 tuoi 364 ngay (min-1)",PRE,"NGAY SINH: '04/03/2010' -> 15 tuoi 364 ngay vao 03/03/2026","BVA - min-1","Loi: Phai tu 16 tuoi tro len"));
        list.add(tc("BVA - Gia Tri Bien","TC_REG_BVA_020","Ngay sinh 03/03/2010 - dung 16 tuoi (min)",PRE,"NGAY SINH: '03/03/2010' -> dung 16 tuoi vao 03/03/2026","BVA - min","Dang ky thanh cong"));
        list.add(tc("BVA - Gia Tri Bien","TC_REG_BVA_021","Ngay sinh 02/03/2010 - 16 tuoi 1 ngay (min+1)",PRE,"NGAY SINH: '02/03/2010' -> 16 tuoi 1 ngay vao 03/03/2026","BVA - min+1","Dang ky thanh cong"));
        list.add(tc("BVA - Gia Tri Bien","TC_REG_BVA_022","Ngay sinh 04/03/1926 - 99 tuoi 364 ngay (max-1)",PRE,"NGAY SINH: '04/03/1926' -> 99 tuoi 364 ngay vao 03/03/2026","BVA - max-1","Dang ky thanh cong"));
        list.add(tc("BVA - Gia Tri Bien","TC_REG_BVA_023","Ngay sinh 03/03/1926 - dung 100 tuoi (max)",PRE,"NGAY SINH: '03/03/1926' -> dung 100 tuoi vao 03/03/2026","BVA - max","Loi: Phai duoi 100 tuoi"));
        list.add(tc("BVA - Gia Tri Bien","TC_REG_BVA_024","Ngay sinh 02/03/1926 - 100 tuoi 1 ngay (max+1)",PRE,"NGAY SINH: '02/03/1926' -> 100 tuoi 1 ngay vao 03/03/2026","BVA - max+1","Loi: Phai duoi 100 tuoi"));
        // SPEC
        list.add(tc("SPEC - Dac Biet / Server-side","TC_REG_SPEC_001","Email da ton tai trong he thong (duplicate)",PRE,"EMAIL: 'existing@shopvn.com' (da co trong CSDL)","SPEC","Loi: Email da duoc dang ky"));
        list.add(tc("SPEC - Dac Biet / Server-side","TC_REG_SPEC_002","Ten dang nhap da ton tai trong he thong",PRE,"TEN DN: 'user_exist' (da co trong CSDL)","SPEC","Loi: Ten dang nhap da ton tai trong he thong"));
        list.add(tc("SPEC - Dac Biet / Server-side","TC_REG_SPEC_003","Xac nhan mat khau khong khop",PRE,"MK: 'Pass@1234' | XAC NHAN: 'DifferentPass@99'","SPEC","Loi: Xac nhan mat khau khong khop"));
        list.add(tc("SPEC - Dac Biet / Server-side","TC_REG_SPEC_004","Ma gioi thieu hop le va ton tai CSDL",PRE,"MA GIOI THIEU: 'ABC12345' (ton tai trong CSDL)","SPEC","Dang ky thanh cong. Ap dung uu dai."));
        list.add(tc("SPEC - Dac Biet / Server-side","TC_REG_SPEC_005","Ma gioi thieu dung format nhung KHONG ton tai CSDL",PRE,"MA GIOI THIEU: 'ZZZZZZZZ' (khong co trong CSDL)","SPEC","Loi: Ma khong ton tai trong he thong"));
        list.add(tc("SPEC - Dac Biet / Server-side","TC_REG_SPEC_006","Ma gioi thieu chua chu thuong (sai format)",PRE,"MA GIOI THIEU: 'abc12345' (chu thuong)","SPEC","Loi: Phai gom dung 8 ky tu chu hoa va so [A-Z0-9]"));
        list.add(tc("SPEC - Dac Biet / Server-side","TC_REG_SPEC_007","Khong tich Dong y Dieu khoan - nut bi disabled",PRE,"DONG Y DIEU KHOAN: false (chua tich) | Cac truong khac: hop le","SPEC","Nut [Dang ky] bi disabled. Loi: Bat buoc phai tich dong y."));
        list.add(tc("SPEC - Dac Biet / Server-side","TC_REG_SPEC_008","Mat khau thieu ky tu dac biet",PRE,"MAT KHAU: 'Password1' (khong co ky tu dac biet)","SPEC","Loi: Mat khau phai co it nhat 1 ky tu dac biet (!@#$%...)"));
        return list;
    }

    private static TestCaseRow tc(String g,String id,String mo,String pre,String inp,String kt,String exp){
        return new TestCaseRow(g,id,mo,pre,inp,kt,exp);
    }
}
