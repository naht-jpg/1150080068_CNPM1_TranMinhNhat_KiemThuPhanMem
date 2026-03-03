package com.shopvn.form;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class RegistrationFormUI extends JFrame {

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

    private static final Color RED       = new Color(192, 0, 0);
    private static final Color RED_HOVER = new Color(220, 40, 40);
    private static final Color GRAY_BTN  = new Color(100, 100, 100);
    private static final Color BG_PAGE   = new Color(240, 242, 245);
    private static final Color BG_CARD   = Color.WHITE;
    private static final Font  FONT_LBL  = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONT_FIELD = new Font("Segoe UI", Font.PLAIN, 13);

    private final RegistrationFormValidator validator = new RegistrationFormValidator();

    public RegistrationFormUI() {
        setTitle("ShopVN - Form Đăng Ký Tài Khoản");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        initComponents();
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BG_PAGE);

        // ── Header ──────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(RED);
        header.setBorder(new EmptyBorder(14, 24, 14, 24));

        JLabel lblTitle = new JLabel("ĐĂNG KÝ TÀI KHOẢN SHOPVN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        header.add(lblTitle, BorderLayout.CENTER);

        JLabel lblSub = new JLabel("Tạo tài khoản miễn phí để mua sắm không giới hạn", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(new Color(255, 200, 200));
        header.add(lblSub, BorderLayout.SOUTH);

        root.add(header, BorderLayout.NORTH);

        // ── Body ─────────────────────────────────────────────────────────────
        JPanel body = new JPanel(new BorderLayout(0, 12));
        body.setBackground(BG_PAGE);
        body.setBorder(new EmptyBorder(16, 20, 12, 20));

        // Card form
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(215, 215, 215)),
            new EmptyBorder(16, 20, 16, 20)
        ));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 4, 5, 6);

        int row = 0;

        // Separator: Thông tin cá nhân
        row = addSeparator(card, c, row, "Thông tin cá nhân");

        txtHoVaTen     = new JTextField(24);
        txtTenDangNhap = new JTextField(24);
        txtEmail       = new JTextField(24);
        txtSoDienThoai = new JTextField(24);

        row = addField(card, c, row, "Họ và tên", txtHoVaTen,     true,  null);
        row = addField(card, c, row, "Tên đăng nhập", txtTenDangNhap, true, "5–20 ký tự, chữ thường/số/gạch dưới");
        row = addField(card, c, row, "Email", txtEmail,           true,  null);
        row = addField(card, c, row, "Số điện thoại", txtSoDienThoai, true, "10 chữ số, bắt đầu bằng 0");

        // Separator: Bảo mật
        row = addSeparator(card, c, row, "Bảo mật");

        txtMatKhau        = new JPasswordField(24);
        txtXacNhanMatKhau = new JPasswordField(24);

        row = addField(card, c, row, "Mật khẩu", txtMatKhau,           true, "8–32 ký tự, có hoa, thường, số, ký tự đặc biệt");
        row = addField(card, c, row, "Xác nhận mật khẩu", txtXacNhanMatKhau, true, null);

        // Separator: Thông tin bổ sung
        row = addSeparator(card, c, row, "Thông tin bổ sung");

        txtNgaySinh   = new JTextField(24);
        txtMaGioiThieu = new JTextField(24);

        row = addField(card, c, row, "Ngày sinh", txtNgaySinh,     true,  "Định dạng dd/MM/yyyy  •  Từ 16 đến dưới 100 tuổi");

        // Giới tính row
        rdoNam  = new JRadioButton("Nam");
        rdoNu   = new JRadioButton("Nữ");
        rdoKhac = new JRadioButton("Khác");
        for (JRadioButton r : new JRadioButton[]{rdoNam, rdoNu, rdoKhac}) {
            r.setFont(FONT_LBL);
            r.setBackground(BG_CARD);
        }
        ButtonGroup bg = new ButtonGroup();
        bg.add(rdoNam); bg.add(rdoNu); bg.add(rdoKhac);
        rdoNam.setSelected(true);

        JPanel pnlGT = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlGT.setBackground(BG_CARD);
        pnlGT.add(rdoNam); pnlGT.add(rdoNu); pnlGT.add(rdoKhac);
        row = addField(card, c, row, "Giới tính", pnlGT, true, null);

        row = addField(card, c, row, "Mã giới thiệu", txtMaGioiThieu, false, "Không bắt buộc  •  8 ký tự [A-Z0-9]");

        // Checkbox điều khoản
        chkDongY = new JCheckBox("Tôi đồng ý với Điều khoản sử dụng và Chính sách bảo mật của ShopVN");
        chkDongY.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkDongY.setBackground(BG_CARD);
        c.gridx = 0; c.gridy = row; c.gridwidth = 3;
        card.add(chkDongY, c);
        c.gridwidth = 1;
        row++;

        body.add(card, BorderLayout.CENTER);

        // ── Button Bar ────────────────────────────────────────────────────────
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        pnlBtn.setBackground(BG_PAGE);
        pnlBtn.setBorder(new EmptyBorder(2, 0, 6, 0));

        btnDangKy = makeButton("Đăng Ký", RED, RED_HOVER, Color.WHITE, true);
        btnXoa    = makeButton("Xóa Form", GRAY_BTN, new Color(130, 130, 130), Color.WHITE, false);

        pnlBtn.add(btnDangKy);
        pnlBtn.add(btnXoa);

        // ── Result panel ─────────────────────────────────────────────────────
        txtResult = new JTextArea(5, 52);
        txtResult.setEditable(false);
        txtResult.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtResult.setLineWrap(true);
        txtResult.setWrapStyleWord(true);
        txtResult.setMargin(new Insets(8, 10, 8, 10));
        txtResult.setBackground(new Color(252, 252, 252));

        JScrollPane scroll = new JScrollPane(txtResult);
        scroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), " Kết quả kiểm tra ",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 11), new Color(80, 80, 80)
        ));

        JPanel south = new JPanel(new BorderLayout(0, 8));
        south.setBackground(BG_PAGE);
        south.add(pnlBtn, BorderLayout.NORTH);
        south.add(scroll, BorderLayout.CENTER);

        body.add(south, BorderLayout.SOUTH);
        root.add(body, BorderLayout.CENTER);

        // ── Events ──────────────────────────────────────────────────────────
        btnDangKy.addActionListener(e -> onDangKy());
        btnXoa.addActionListener(e -> onXoa());

        add(root);
    }

    // ── Helpers ─────────────────────────────────────────────────────────────

    private int addSeparator(JPanel panel, GridBagConstraints c, int row, String text) {
        c.gridx = 0; c.gridy = row; c.gridwidth = 3; c.insets = new Insets(10, 4, 2, 4);
        JPanel sep = new JPanel(new BorderLayout(6, 0));
        sep.setBackground(BG_CARD);
        JLabel lbl = new JLabel(" " + text + " ");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(new Color(150, 150, 150));
        JSeparator line = new JSeparator();
        line.setForeground(new Color(220, 220, 220));
        sep.add(lbl, BorderLayout.WEST);
        sep.add(line, BorderLayout.CENTER);
        panel.add(sep, c);
        c.gridwidth = 1; c.insets = new Insets(5, 4, 5, 6);
        return row + 1;
    }

    private int addField(JPanel panel, GridBagConstraints c, int row,
                         String label, JComponent field, boolean required, String hint) {
        // Label column
        String req = required ? " <font color='#cc0000'>*</font>" : "";
        JLabel lbl = new JLabel("<html>" + label + req + "</html>");
        lbl.setFont(FONT_LBL);
        lbl.setPreferredSize(new Dimension(160, 24));
        c.gridx = 0; c.gridy = row; c.weightx = 0;
        panel.add(lbl, c);

        // Field column
        field.setFont(FONT_FIELD);
        if (field instanceof JTextField || field instanceof JPasswordField) {
            field.setPreferredSize(new Dimension(230, 28));
        }
        c.gridx = 1; c.gridy = row; c.weightx = 1;
        panel.add(field, c);

        // Hint column
        if (hint != null) {
            JLabel hintLbl = new JLabel(hint);
            hintLbl.setFont(new Font("Segoe UI", Font.ITALIC, 10));
            hintLbl.setForeground(new Color(160, 160, 160));
            hintLbl.setPreferredSize(new Dimension(260, 20));
            c.gridx = 2; c.gridy = row; c.weightx = 0;
            panel.add(hintLbl, c);
        }

        return row + 1;
    }

    private JButton makeButton(String text, Color bg, Color hover, Color fg, boolean bold) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isRollover() ? hover : bg;
                if (getModel().isPressed()) c = c.darker();
                g2.setColor(c);
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

    private String getGioiTinh() {
        if (rdoNam.isSelected()) return "Nam";
        if (rdoNu.isSelected())  return "Nữ";
        return "Khác";
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
            txtResult.setText("✔  ĐĂNG KÝ THÀNH CÔNG!\n\nTài khoản \"" + form.getTenDangNhap()
                + "\" đã được tạo thành công. Chào mừng bạn đến với ShopVN!");
        } else {
            txtResult.setForeground(new Color(180, 0, 0));
            List<String> errors = result.getErrors();
            StringBuilder sb = new StringBuilder();
            sb.append("✘  ĐĂNG KÝ THẤT BẠI  —  Phát hiện ").append(errors.size()).append(" lỗi:\n\n");
            for (int i = 0; i < errors.size(); i++) {
                sb.append("  ").append(i + 1).append(".  ").append(errors.get(i)).append("\n");
            }
            txtResult.setText(sb.toString());
        }
        txtResult.setCaretPosition(0);
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
