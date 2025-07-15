import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.List;

/**
 * 编译打包命令：
 * <pre>
 * javac -encoding UTF-8 HostsEditor.java <br />
 * jar --create --file HostsEditor.jar --main-class HostsEditor HostsEditor.class <br />
 * jpackage `
     --input . `
     --name HostsEditor `
     --main-jar HostsEditor.jar `
     --main-class HostsEditor `
     --type exe `
     --win-shortcut `
     --win-menu `
     --win-dir-chooser `
     --win-menu-group "HostsEditor工具" `
     --icon xiaozaoshu.ico `
     --vendor "Kenny" `
     --app-version 1.0.0 `
     --description "Hosts 编辑器\n作者：Kenny\n官网：https:// www. xiaozaoshu. top\nmail:kenny-tang@hotmail.com" `
     --win-requested-execution-level requireAdministrator
 * </pre>
 */
public class HostsEditor extends JFrame {
    private static final String HOSTS_PATH;
    private static final String sysEncoding;

    static {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            sysEncoding = "GBK";
            HOSTS_PATH = "C:\\Windows\\System32\\drivers\\etc\\hosts";
        } else {
            sysEncoding = "UTF-8";
            HOSTS_PATH = "/etc/hosts";
        }
    }

    private final DefaultTableModel tableModel;
    private final JTable table;

    public HostsEditor() {
        setTitle("Hosts 配置编辑器");
        setSize(700, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 菜单栏：关于
        JMenuBar menuBar = new JMenuBar();
        JMenu helpMenu = new JMenu("帮助");
        JMenuItem aboutItem = new JMenuItem("关于");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "HostsEditor 由 Kenny（谭建伟）开发\n" +
                        "官网：https://www.xiaozaoshu.top\n" +
                        "mail: kenny-tang@hotmail.com\n" +
                        "版本：v1.0.0",
                "关于", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        // 表格
        String[] columnNames = {"IP 地址", "域名"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 输入区
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JTextField ipField = new JTextField(12);
        JTextField domainField = new JTextField(20);
        inputPanel.add(new JLabel("IP:"));
        inputPanel.add(ipField);
        inputPanel.add(new JLabel("域名:"));
        inputPanel.add(domainField);

        // 按钮区
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton addBtn = new JButton("添加");
        JButton deleteBtn = new JButton("删除选中行");
        JButton saveBtn = new JButton("保存到 hosts");
        JButton refreshBtn = new JButton("刷新");
        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(refreshBtn);

        // 整合控制区域
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.add(inputPanel);
        controlPanel.add(buttonPanel);
        add(controlPanel, BorderLayout.SOUTH);

        // 添加按钮事件
        addBtn.addActionListener(e -> {
            String ip = ipField.getText().trim();
            String domain = domainField.getText().trim();
            if (ip.isEmpty() || domain.isEmpty()) {
                JOptionPane.showMessageDialog(this, "IP 和域名不能为空");
                return;
            }
            if (!isValidIPv4(ip)) {
                JOptionPane.showMessageDialog(this, "请输入有效的 IPv4 地址");
                return;
            }
            tableModel.addRow(new Object[]{ip, domain});
            ipField.setText("");
            domainField.setText("");
        });

        deleteBtn.addActionListener(e -> {
            if (table.isEditing()) {
                table.getCellEditor().stopCellEditing();
            }
            int selected = table.getSelectedRow();
            if (selected >= 0) {
                tableModel.removeRow(selected);
            }
        });

        saveBtn.addActionListener(e -> {
            if (table.isEditing()) {
                table.getCellEditor().stopCellEditing();
            }
            saveToHosts();
        });

        refreshBtn.addActionListener(e -> {
            if (table.isEditing()) {
                table.getCellEditor().stopCellEditing();
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "刷新将丢弃未保存的更改，是否继续？", "确认刷新", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                tableModel.setRowCount(0);
                loadHosts();
            }
        });

        loadHosts();
    }

    private void loadHosts() {
        try {
            Path hostsPath = Paths.get(HOSTS_PATH);
            List<String> lines = Files.readAllLines(hostsPath, Charset.forName(sysEncoding));
            for (String line : lines) {
                line = line.trim();
                if (line.startsWith("#") || line.isEmpty()) continue;
                String[] parts = line.split("\\s+");
                if (parts.length >= 2) {
                    tableModel.addRow(new Object[]{parts[0], parts[1]});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "读取 hosts 文件失败:\n" + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void backupHosts() throws IOException {
        File original = new File(HOSTS_PATH);
        if (original.exists()) {
            File backup = new File(HOSTS_PATH + ".bak_" + System.currentTimeMillis());
            Files.copy(original.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void saveToHosts() {
        try {
            backupHosts();
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(HOSTS_PATH), sysEncoding))) {
                writer.write("# 修改于 Java HostsEditor\n");
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String ip = tableModel.getValueAt(i, 0).toString();
                    String domain = tableModel.getValueAt(i, 1).toString();
                    writer.write(String.format("%-15s %s%n", ip, domain));
                }
            }
            JOptionPane.showMessageDialog(this, "✅ Hosts 文件保存成功！");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "保存失败，请确保以管理员权限运行程序。\n" + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidIPv4(String ip) {
        String pattern = "^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.|$)){4}$";
        return ip.matches(pattern);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HostsEditor().setVisible(true));
    }
}
