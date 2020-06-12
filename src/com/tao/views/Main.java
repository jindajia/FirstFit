package com.tao.views;

import com.tao.code.Job;
import com.tao.code.Zone;
import com.tao.code.ZoneTable;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.ArrayList;

// 继承自JFrame 实现了ActionListener接口
public class Main extends JFrame implements ActionListener {
    private JTable jt; // 表格
    private JTextField jtf1, jtf2, jtf3, jtf4; // 文本输入框
    private ZoneTable zoneTable;
    private JLabel jl1, jl2, jl3; // 文本框
    private JPanel jp1, jp3; // 面板
    private JButton jb1, jb2, jb3; // 按钮
    private JScrollPane jsp; // 滚动条
    private JComboBox jc1;
    private String alstr;
    View frame;
    private DefaultTableModel tableModel;
    // 二维数组
    private Vector<Vector> data; // 表格的数据

    Main() {
        jl1 = new JLabel("初始化大小");
        jtf1 = new JTextField(4);
        jb1 = new JButton("确认");
        jb1.setActionCommand("init");
        jb1.addActionListener(this);
        jc1 = new JComboBox();
        jc1.setModel(new DefaultComboBoxModel(new String[] {"首次适应算法", "最佳适应算法","最坏适应算法"}));
        jp1 = new JPanel();
        jp1.add(jc1);
        jp1.add(jl1);
        jp1.add(jtf1);
        jp1.add(jb1);

        jb2 = new JButton("添加");
        jb2.addActionListener(this);
        jb2.setActionCommand("add");
        jb3 = new JButton("删除");
        jb3.setActionCommand("remove");
        jb3.addActionListener(this);
        jl2 = new JLabel("作业名和大小");
        jl3 = new JLabel("区号");
        jtf2 = new JTextField(4);
        jtf3 = new JTextField(4);
        jtf4 = new JTextField(2);

        jp3 = new JPanel();
        jp3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        jp3.add(jl2);
        jp3.add(jtf2);
        jp3.add(jtf3);
        jp3.add(jb2);

        jp3.add(jl3);
        jp3.add(jtf4);
        jp3.add(jb3);
        getContentPane().add(jp1);
        this.setVisible(true);
        this.setSize(470, 500);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args) {
        Main m = new Main();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("init")) {
        	alstr = jc1.getSelectedItem().toString();
            String size = jtf1.getText();
            // 类型的强制转换
            zoneTable = new ZoneTable(Integer.valueOf(size));

            data = new Vector<>();
            zoneTable.updateData(data,alstr);
            //画图
            List<Zone> table = zoneTable.getTable();
            
            
            
            // 初始化时填充表格数据和标题
            DefaultTableModel tableModel = new DefaultTableModel(data,zoneTable.getTitleName());
            jt = new JTable(tableModel);
            
            // 不可编辑
            jt.setEnabled(false);

            jsp = new JScrollPane(jt);
            this.remove(jp1);
            // 边界布局
            getContentPane().add(jp3, BorderLayout.NORTH);
            getContentPane().add(jsp, BorderLayout.CENTER);
            // 页面刷新
            this.revalidate();
            frame = new View(zoneTable);
        } else if (e.getActionCommand().equals("add")) {
            String jobName = jtf2.getText();
            String size = jtf3.getText();
            boolean success = zoneTable.add(new Job(jobName, Integer.valueOf(size)));
            if (!success) {
                JOptionPane.showMessageDialog(null, "添加失败");
            } else {
                zoneTable.updateData(data,alstr);
                jt.revalidate();
                jsp.revalidate();
            }
        } else if (e.getActionCommand().equals("remove")) {
            String no = jtf4.getText();
            boolean success = zoneTable.free(Integer.valueOf(no));
            if (!success) {
                JOptionPane.showMessageDialog(null, "删除失败");
            } else {
                zoneTable.updateData(data,alstr);
                jt.revalidate();
                jsp.revalidate();
            }
        }
        jt.updateUI();
        
		frame.refresh(zoneTable);
		frame.setVisible(true);
    }
}
