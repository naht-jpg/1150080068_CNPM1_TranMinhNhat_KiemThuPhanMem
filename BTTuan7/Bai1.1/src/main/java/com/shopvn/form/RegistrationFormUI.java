package com.shopvn.form;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class RegistrationFormUI extends JFrame {

    // Fields
    private JTextField txtHoVaTen;
    private JTextField txtTenDangNhap;
    private JTextField txtEmail;
    private JTextField txtSoDienThoai;
    private JPasswordField txtMatKhau;
    private JPasswordField txtXacNhanMatKhau;
    private JTextField txtNgaySinh;
    private JRadioButton rdoNam;
    private JRadioButton rdoNu;
    private JRadioButton rdoKhac;
    private JTextField txtMaGioiThieu;
    private JCheckBox chkDongY;
    private JTextArea txtResult;
    private JButton btnDangKy;
    private JButton btnXoa;

    private final RegistrationFormValidator validator = new RegistrationFormValidator();

    public RegistrationFormUI() {
        setTitle("ShopVN - Form Đăng Ký Tài Khoản");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        initComponents();
        pack();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        // Main panel with padding
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(new EmptyBorder(16, 20, 16, 20));
        main.setBackground(new Color(245, 247, 250));

        // Title
        JLabel title = new JLabel("ĐĂNG KÝ TÀI KHOẢN SHOPVN", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(220, 50, 50));
        title.setBorder(new EmptyBorder(0, 0, 10, 0));
        main.add(title, BorderLayout.NORTH);

        // Form panel
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 210, 210)),
            new EmptyBorder(14, 18, 14, 18)
        ));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 4, 5, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        // Row helper
        int row = 0;

        // Họ và tên
        txtHoVaTen = new JTextField(22);
        row = addRow(form, c, row, "Họ và tên *", txtHoVaTen, false);

        // Tên đăng nhập
        txtTenDangNhap = new JTextField(22);
        row = addRow(form, c, row, "Tên đăng nhập *", txtTenDangNhap, false);

        // Email
        txtEmail = new JTextField(22);
        row = addRow(form, c, row, "Email *", txtEmail, false);

        // Số điện thoại
        txtSoDienThoai = new JTextField(22);
        row = addRow(form, c, row, "Số điện thoại *", txtSoDienThoai, false);

        // Mật khẩu
        txtMatKhau = new JPasswordField(22);
        row = addRow(form, c, row, "Mật khẩu *", txtMatKhau, false);

        // Xác nhận mật khẩu
        txtXacNhanMatKhau = new JPasswordField(22);
        row = addRow(form, c, row, "Xác nhận mật khẩu *", txtXacNhanMatKhau, false);

        // Ngày sinh
        txtNgaySinh = new JTextField(22);
        txtNgaySinh.setToolTipText("Định dạng: dd/MM/yyyy");
        row = addRow(form, c, row, "Ngày sinh * (dd/MM/yyyy)", txtNgaySinh, false);

        // Giới tính
        rdoNam = new JRadioButton("Nam");
        rdoNu = new JRadioButton("Nữ");
        rdoKhac = new JRadioButton("Khác");
        rdoNam.setBackground(Color.WHITE);
        rdoNu.setBackground(Color.WHITE);
        rdoKhac.setBackground(Color.WHITE);
        ButtonGroup bgGioiTinh = new ButtonGroup();
        bgGioiTinh.add(rdoNam);
        bgGioiTinh.add(rdoNu);
        bgGioiTinh.add(rdoKhac);
        rdoNam.setSelected(true);
        JPanel pnlGioiTinh = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlGioiTinh.setBackground(Color.WHITE);
        pnlGioiTinh.add(rdoNam);
        pnlGioiTinh.add(Box.createHorizontalStrut(10));
        pnlGioiTinh.add(rdoNu);
        pnlGioiTinh.add(Box.createHorizontalStrut(10));
        pnlGioiTinh.add(rdoKhac);
        row = addRow(form, c, row, "Giới tính *", pnlGioiTinh, false);

        // Mã giới thiệu
        txtMaGioiThieu = new JTextField(22);
        txtMaGioiThieu.setToolTipText("Không bắt buộc - 8 ký tự [A-Z0-9]");
        row = addRow(form, c, row, "Mã giới thiệu", txtMaGioiThieu, false);

        // Checkbox
        chkDongY = new JCheckBox("Tôi đồng ý với các Điều khoản sử dụng của ShopVN");
        chkDongY.setBackground(Color.WHITE);
        chkDongY.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        c.gridx = 0; c.gridy = row; c.gridwidth = 2;
        form.add(chkDongY, c);
        c.gridwidth = 1;
        row++;

        // Hint
        JLabel hint = new JLabel("* Trường bắt buộc");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(Color.GRAY);
        c.gridx = 0; c.gridy = row; c.gridwidth = 2;
        form.add(hint, c);
        c.gridwidth = 1;

        main.add(form, BorderLayout.CENTER);

        // Bottom panel: buttons + result
        JPanel bottom = new JPanel(new BorderLayout(8, 8));
        bottom.setBackground(new Color(245, 247, 250));

        // Buttons
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 4));
        pnlBtn.setBackground(new Color(245, 247, 250));

        btnDangKy = new JButton("  Đăng Ký  ");
        btnDangKy.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnDangKy.setBackground(new Color(220, 50, 50));
        btnDangKy.setForeground(Color.WHITE);
        btnDangKy.setFocusPainted(false);
        btnDangKy.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnXoa = new JButton("  Xóa Form  ");
        btnXoa.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnXoa.setBackground(new Color(180, 180, 180));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setFocusPainted(false);
        btnXoa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        pnlBtn.add(btnDangKy);
        pnlBtn.add(btnXoa);
        bottom.add(pnlBtn, BorderLayout.NORTH);

        // Result area
        txtResult = new JTextArea(6, 46);
        txtResult.setEditable(false);
        txtResult.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtResult.setLineWrap(true);
        txtResult.setWrapStyleWord(true);
        txtResult.setBorder(new EmptyBorder(6, 8, 6, 8));
        JScrollPane scroll = new JScrollPane(txtResult);
        scroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(210, 210, 210)), "Kết quả kiểm tra",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 11), new Color(60, 60, 60)
        ));
        bottom.add(scroll, BorderLayout.CENTER);

        main.add(bottom, BorderLayout.SOUTH);

        // Events
        btnDangKy.addActionListener(e -> onDangKy());
        btnXoa.addActionListener(e -> onXoa());

        add(main);
    }

    private int addRow(JPanel panel, GridBagConstraints c, int row, String label, JComponent field, boolean span) {
        JLabel lbl = new JLabel(label + ":");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setPreferredSize(new Dimension(185, 24));
        c.gridx = 0; c.gridy = row; c.weightx = 0;
        panel.add(lbl, c);

        c.gridx = 1; c.gridy = row; c.weightx = 1;
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        if (field instanceof JTextField || field instanceof JPasswordField) {
            field.setPreferredSize(new Dimension(200, 26));
        }
        panel.add(field, c);
        return row + 1;
    }

    private String getGioiTinh() {
        if (rdoNam.isSelected()) return "Nam";
        if (rdoNu.isSelected()) return "Nữ";
        return "Khác";
    }

    private void onDangKy() {
        String hoVaTen = txtHoVaTen.getText().trim();
        String tenDangNhap = txtTenDangNhap.getText().trim();
        String email = txtEmail.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword());
        String xacNhan = new String(txtXacNhanMatKhau.getPassword());
        String ngaySinh = txtNgaySinh.getText().trim();
        String gioiTinh = getGioiTinh();
        String maGT = txtMaGioiThieu.getText().trim();
        boolean dongY = chkDongY.isSelected();

        RegistrationForm form = new RegistrationForm(
            hoVaTen, tenDangNhap, email, sdt,
            matKhau, xacNhan, dongY, ngaySinh, gioiTinh, maGT
        );

        RegistrationFormValidator.ValidationResult result = validator.validateForm(form);

        if (result.isValid()) {
            txtResult.setForeground(new Color(0, 140, 0));
            txtResult.setText("✔ ĐĂNG KÝ THÀNH CÔNG!\n\nTài khoản \"" + tenDangNhap + "\" đã được tạo thành công.");
        } else {
            txtResult.setForeground(new Color(200, 0, 0));
            List<String> errors = result.getErrors();
            StringBuilder sb = new StringBuilder();
            sb.append("✘ ĐĂNG KÝ THẤT BẠI - Có ").append(errors.size()).append(" lỗi:\n\n");
            for (int i = 0; i < errors.size(); i++) {
                sb.append("  ").append(i + 1).append(". ").append(errors.get(i)).append("\n");
            }
            txtResult.setText(sb.toString());
        }
    }

    private void onXoa() {
        txtHoVaTen.setText("");
        txtTenDangNhap.setText("");
        txtEmail.setText("");
        txtSoDienThoai.setText("");
        txtMatKhau.setText("");
        txtXacNhanMatKhau.setText("");
        txtNgaySinh.setText("");
        txtMaGioiThieu.setText("");
        chkDongY.setSelected(false);
        rdoNam.setSelected(true);
        txtResult.setText("");
        txtHoVaTen.requestFocus();
    }
}
