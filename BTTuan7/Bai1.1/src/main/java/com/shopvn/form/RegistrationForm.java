package com.shopvn.form;

public class RegistrationForm {
    private String hoVaTen;
    private String tenDangNhap;
    private String email;
    private String soDienThoai;
    private String matKhau;
    private String xacNhanMatKhau;
    private boolean dongYDieuKhoan;
    private String ngaySinh;
    private String gioiTinh;
    private String maGioiThieu;

    public RegistrationForm() {}

    public RegistrationForm(String hoVaTen, String tenDangNhap, String email,
            String soDienThoai, String matKhau, String xacNhanMatKhau,
            boolean dongYDieuKhoan, String ngaySinh, String gioiTinh, String maGioiThieu) {
        this.hoVaTen = hoVaTen;
        this.tenDangNhap = tenDangNhap;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.matKhau = matKhau;
        this.xacNhanMatKhau = xacNhanMatKhau;
        this.dongYDieuKhoan = dongYDieuKhoan;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.maGioiThieu = maGioiThieu;
    }

    public String getHoVaTen() { return hoVaTen; }
    public void setHoVaTen(String v) { this.hoVaTen = v; }
    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String v) { this.tenDangNhap = v; }
    public String getEmail() { return email; }
    public void setEmail(String v) { this.email = v; }
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String v) { this.soDienThoai = v; }
    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String v) { this.matKhau = v; }
    public String getXacNhanMatKhau() { return xacNhanMatKhau; }
    public void setXacNhanMatKhau(String v) { this.xacNhanMatKhau = v; }
    public boolean isDongYDieuKhoan() { return dongYDieuKhoan; }
    public void setDongYDieuKhoan(boolean v) { this.dongYDieuKhoan = v; }
    public String getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(String v) { this.ngaySinh = v; }
    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String v) { this.gioiTinh = v; }
    public String getMaGioiThieu() { return maGioiThieu; }
    public void setMaGioiThieu(String v) { this.maGioiThieu = v; }
}