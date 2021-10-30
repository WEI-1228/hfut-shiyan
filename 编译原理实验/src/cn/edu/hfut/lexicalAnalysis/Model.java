package cn.edu.hfut.lexicalAnalysis;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.io.IOException;

public class Model implements TableModel {
    // 创建数组用于单元格中显示的内容
    String[][] mess = new String[0][];
    public Model setVal(){
        Main main = new Main();
        try {
            main.analysis();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mess = main.createTable();
        return this;
    }

    /**
     * 初始化数组内容
     */

    @Override
    public int getRowCount() {
        // 设置总行数
        return mess.length;
    }

    @Override
    public int getColumnCount() {
        // 设置总列数
        return 4;
    }

    @Override
    public String getColumnName(int columnIndex) {
        // 设置单元格列属性（表头名）
        if(columnIndex==0)return "单词";
        else if(columnIndex==1)return "二元序列";
        else if(columnIndex==2)return "类型";
        else return "位置（行，列）";
    }

    /**
     * 设置每列数据类型
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    /**
     * 设置单元格是否允许编辑
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    /**
     * 设置单元格数据内容
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return mess[rowIndex][columnIndex];
    }

    /**
     * 设置单元格的值
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        // 此方法可给表格添加监听
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        // 此方法可给表格移除监听
    }
}
