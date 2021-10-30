package cn.edu.hfut.LR1;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.io.IOException;
import java.util.LinkedList;

public class Model implements TableModel {
    String[][] mess = new String[0][];

    public Model setVal(String text) throws IOException {
        LR1 lr1 = new LR1();
        LinkedList<String[]> analysis = lr1.analysis(text);
        mess = new String[analysis.size()][];
        int count = 0;
        for (String[] strings : analysis) {
            mess[count++] = strings;
        }
        return this;
    }

    @Override
    public int getRowCount() {
        return mess.length;
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int columnIndex) {
        String[] name = {"步骤", "状态栈", "符号栈", "输入串", "动作说明"};
        return name[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return mess[rowIndex][columnIndex];
    }


    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }
}
