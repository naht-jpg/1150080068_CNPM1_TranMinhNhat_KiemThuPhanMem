package com.shopvn.form;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class RegistrationFormUI extends JFrame {

    private JTextField    txtHoVaTen, txtTenDangNhap, txtEmail, txtSoDienThoai;
    private JTextField    txtNgaySinh, txtMaGioiThieu;
    private JPasswordField txtMatKhau, txtXacNhanMatKhau;
    private JRadioButton  rdoNam, rdoNu, rdoKhac;
    private JCheckBox     chkDongY;
    private JTextArea     txtResult;
    private JButton       btnDangKy, btnXoa;

    private static final Color RED        = new Color(192, 0, 0);
    private static final Color RED_HOVER  = new Color(220, 40, 40);
    private static final Color GRAY_BTN   = new Color(90, 90, 90);
    private static final Color BG_PAGE    = new Color(240, 242, 245);
    private static final Color BG_CARD    = Color.WHITE;
    private static final int   LBL_W      = 155;  // fixed label column width
    private static final int   FIELD_W    = 280;  // fixed field width
    private static final Font  FONT_LBL   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONT_FIELD = new Font("Segoe UI", Font.PLAIN, 13);

    private final RegistrationFormValidator validator = new RegistrationFormValidator();

    public RegistrationFormUI() {
        setTitle("ShopVN - Form Đăng Ký Tài Khoản");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        initComponents();
        pack();
        setMinimumSize(new Dimension(540, 500));
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_PAGE);

        // ── Header ──────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(RED);
        header.setBorder(new EmptyBorder(14, 24, 12, 24));
        JLabel lblTitle = new JLabel("ĐĂNG KÝ TÀI KHOẢN SHOPVN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        JLabel lblSub = new JLabel("Tạo tài khoản miễn phí để mua sắm không giới hạn", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(new Color(255, 200, 200));
        header.add(lblTitle, BorderLayout.CENTER);
        header.add(lblSub, BorderLayout.SOUTH);
        root.add(header, BorderLayout.NORTH);

        // ── Card (form fields) ───────────────────────────────────────────────
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(215, 215, 215)),
            new EmptyBorder(14, 20, 14, 20)
        ));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        int row = 0;

        txtHoVaTen     = new JTextField();
        txtTenDangNhap = new JTextField();
        txtEmail       = new JTextField();
        txtSoDienThoai = new JTextField();
        txtMatKhau        = new JPasswordField();
        txtXacNhanMatKhau = new JPasswordField();
        txtNgaySinh    = new JTextField();
        txtMaGioiThieu = new JTextField();

        // Tooltips thay cho hint column
        txtTenDangNhap.setToolTipText("5–20 ký tự, chữ thường/số/gạch dưới, bắt đầu bằng chữ cái");
        txtSoDienThoai.setToolTipText("10 chữ số, bắt đầu bằng 0");
        txtMatKhau.setToolTipText("8–32 ký tự, có ít nhất 1 hoa, 1 thường, 1 số, 1 ký tự đặc biệt");
        txtNgaySinh.setToolTipText("Định dạng dd/MM/yyyy, tuổi từ 16 đến dưới 100");
        txtMaGioiThieu.setToolTipText("Không bắt buộc — 8 ký tự [A-Z0-9], ví dụ: ABC12345");

        row = addSectionSep(card, c, row, "Thông tin cá nhân");
        row = addRow(card, c, row, "Họ và tên",       txtHoVaTen,     true);
        row = addRow(card, c, row, "Tên đăng nhập",   txtTenDangNhap, true);
        row = addRow(card, c, row, "Email",            txtEmail,       true);
        row = addRow(card, c, row, "Số điện thoại",   txtSoDienThoai, true);

        row = addSectionSep(card, c, row, "Bảo mật");
        row = addRow(card, c, row, "Mật khẩu",        txtMatKhau,        true);
        row = addRow(card, c, row, "Xác nhận mật khẩu", txtXacNhanMatKhau, true);

        row = addSectionSep(card, c, row, "Thông tin bổ sung");
        row = addRow(card, c, row, "Ngày sinh",        txtNgaySinh,    true);

        // Giới tính
        rdoNam = new JRadioButton("Nam");  rdoNam.setBackground(BG_CARD);  rdoNam.setFont(FONT_LBL);
        rdoNu  = new JRadioButton("Nữ");   rdoNu.setBackground(BG_CARD);   rdoNu.setFont(FONT_LBL);
        rdoKhac= new JRadioButton("Khác"); rdoKhac.setBackground(BG_CARD); rdoKhac.setFont(FONT_LBL);
        ButtonGroup bg = new ButtonGroup();
        bg.add(rdoNam); bg.add(rdoNu); bg.add(rdoKhac);
        rdoNam.setSelected(true);
        JPanel pnlGT = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlGT.setBackground(BG_CARD);
        pnlGT.add(rdoNam); pnlGT.add(rdoNu); pnlGT.add(rdoKhac);
        row = addRow(card, c, row, "Giới tính", pnlGT, true);

        row = addRow(card, c, row, "Mã giới thiệu",   txtMaGioiThieu, false);

        // Checkbox
        chkDongY = new JCheckBox("Tôi đồng ý với Điều khoản sử dụng và Chính sách bảo mật của ShopVN");
        chkDongY.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkDongY.setBackground(BG_CARD);
        c.gridx = 0; c.gridy = row; c.gridwidth = 2;
        c.insets = new Insets(10, 0, 4, 0);
        card.add(chkDongY, c);
        c.gridwidth = 1;

        // Bọc card trong scroll
        JScrollPane cardScroll = new JScrollPane(card,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        cardScroll.setBorder(null);
        cardScroll.getVerticalScrollBar().setUnitIncrement(16);

        // ── Buttons ──────────────────────────────────────────────────────────
        btnDangKy = makeButton("Đăng Ký",  RED,      RED_HOVER,                  Color.WHITE, true);
        btnXoa    = makeButton("Xóa Form", GRAY_BTN, new Color(120, 120, 120),   Color.WHITE, false);
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 8));
        pnlBtn.setBackground(BG_PAGE);
        pnlBtn.add(btnDangKy);
        pnlBtn.add(btnXoa);

        // ── Result ────────────────────────────────────────────────────────────
        txtResult = new JTextArea(5, 50);
        txtResult.setEditable(false);
        txtResult.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtResult.setLineWrap(true);
        txtResult.setWrapStyleWord(true);
        txtResult.setMargin(new Insets(8, 10, 8, 10));
        txtResult.setBackground(new Color(252, 252, 252));
        JScrollPane resultScroll = new JScrollPane(txtResult);
        resultScroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), " Kết quả kiểm tra ",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 11), new Color(80, 80, 80)
        ));

        // ── Body assembly ────────────────────────────────────────────────────
        JPanel body = new JPanel(new BorderLayout(0, 10));
        body.setBackground(BG_PAGE);
        body.setBorder(new EmptyBorder(14, 16, 12, 16));
        body.add(cardScroll, BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout(0, 6));
        south.setBackground(BG_PAGE);
        south.add(pnlBtn,       BorderLayout.NORTH);
        south.add(resultScroll, BorderLayout.CENTER);
        body.add(south, BorderLayout.SOUTH);

        root.add(body, BorderLayout.CENTER);

        btnDangKy.addActionListener(e -> onDangKy());
        btnXoa.addActionListener(e -> onXoa());

        add(root);
    }

    // ── Layout helpers ───────────────────────────────────────────────────────

    private int addSectionSep(JPanel panel, GridBagConstraints c, int row, String title) {
        c.gridx = 0; c.gridy = row; c.gridwidth = 2;
        c.insets = new Insets(12, 0, 4, 0);
        JPanel sep = new JPanel(new BorderLayout(6, 0));
        sep.setBackground(BG_CARD);
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(new Color(140, 140, 140));
        JSeparator line = new JSeparator();
        sep.add(lbl,  BorderLayout.WEST);
        sep.add(line, BorderLayout.CENTER);
        panel.add(sep, c);
        c.gridwidth = 1;
        c.insets = new Insets(0, 0, 0, 0);
        return row + 1;
    }

    private int addRow(JPanel panel, GridBagConstraints c,
                       int row, String labelText, JComponent field, boolean required) {
        // Label — fixed width, no-wrap via HTML nobr
        String req = required ? "&nbsp;<font color='#cc0000'>*</font>" : "";
        JLabel lbl = new JLabel("<html><nobr>" + labelText + req + "</nobr></html>");
        lbl.setFont(FONT_LBL);
        lbl.setPreferredSize(new Dimension(LBL_W, 26));
        lbl.setMinimumSize(new Dimension(LBL_W, 26));
        c.gridx = 0; c.gridy = row; c.weightx = 0;
        c.insets = new Insets(5, 4, 5, 10);
        panel.add(lbl, c);

        // Field — stretches horizontally
        field.setFont(FONT_FIELD);
        if (field instanceof JTextField || field instanceof JPasswordField) {
            field.setPreferredSize(new Dimension(FIELD_W, 28));
        }
        c.gridx = 1; c.gridy = row; c.weightx = 1.0;
        c.insets = new Insets(5, 0, 5, 4);
        panel.add(field, c);

        return row + 1;
    }

    private JButton makeButton(String text, Color bg, Color hover, Color fg, boolean bold) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color col = getModel().isRollover() ? hover : bg;
                if (getModel().isPressed()) col = col.darker();
                g2.setColor(col);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, 13));
        btn.setForeground(fg);
        btn.setPreferredSize(new Dimension(130, 36));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ── Events ───────────────────────────────────────────────────────────────

    private String getGioiTinh() {
        if (rdoNu.isSelected())  return "Nữ";
        if (rdoKhac.isSelected()) return "Khác";
        return "Nam";
    }

    private void onDangKy() {
        RegistrationForm form = new RegistrationForm(
            txtHoVaTen.getText().trim(),
            txtTenDangNhap.getText().trim(),
            txtEmail.getText().trim(),
            txtSoDienThoai.getText().trim(),
            new String(txtMatKhau.getPassword()),
            new String(txtXacNhanMatKhau.getPassword()),
            chkDongY.isSelected(),
            txtNgaySinh.getText().trim(),
            getGioiTinh(),
            txtMaGioiThieu.getText().trim()
        );
        RegistrationFormValidator.ValidationResult result = validator.validateForm(form);
        if (result.isValid()) {
            txtResult.setForeground(new Color(0, 130, 0));
            txtResult.setText("✔  ĐĂNG KÝ THÀNH CÔNG!\n\nTài khoản \""
                + form.getTenDangNhap() + "\" đã được tạo thành công.\nChào mừng bạn đến với ShopVN!");
        } else {
            txtResult.setForeground(new Color(180, 0, 0));
            List<String> errors = result.getErrors();
            StringBuilder sb = new StringBuilder();
            sb.append("✘  ĐĂNG KÝ THẤT BẠI  —  Phát hiện ").append(errors.size()).append(" lỗi:\n\n");
            for (int i = 0; i < errors.size(); i++)
                sb.append("  ").append(i + 1).append(".  ").append(errors.get(i)).append("\n");
            txtResult.setText(sb.toString());
        }
        txtResult.setCaretPosition(0);
    }

    private void onXoa() {
        txtHoVaTen.setText(""); txtTenDangNhap.setText(""); txtEmail.setText("");
        txtSoDienThoai.setText(""); txtMatKhau.setText(""); txtXacNhanMatKhau.setText("");
        txtNgaySinh.setText(""); txtMaGioiThieu.setText("");
        chkDongY.setSelected(false); rdoNam.setSelected(true);
        txtResult.setText(""); txtHoVaTen.requestFocus();
    }
}
