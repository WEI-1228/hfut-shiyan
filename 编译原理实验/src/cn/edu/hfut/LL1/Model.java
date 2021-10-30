package cn.edu.hfut.LL1;

import javafx.util.Pair;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;

public class Model implements TableModel {
    Map<Pair<Character, Character>, String> table;
    String[][]mess = new String[0][];
    public Model setVal(String text) throws IOException {
        LL1 ll1 = new LL1();
        LinkedList<String[]> analysis = ll1.analysis(text);
        mess = new String[analysis.size()][];
        int count = 0;
        for(String[]strings:analysis){
            mess[count++]=strings;
        }
        table = ll1.getTable();
        return this;
    }
    public Map<Pair<Character, Character>, String> getTable(){
        return table;
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
        String[]name = {"步骤", "分析栈", "剩余输入符号串", "所用产生式", "动作"};
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
