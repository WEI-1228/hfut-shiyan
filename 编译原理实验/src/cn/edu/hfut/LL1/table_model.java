package cn.edu.hfut.LL1;

import javafx.util.Pair;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class table_model implements TableModel {
    String[][] mess = new String[0][];
    char[] Lchar;
    char[] Rchar;
    Map<Pair<Character, Character>, String> ptable;

    public table_model setVal(Map<Pair<Character, Character>, String> table) {
        ptable = table;
        Set<Pair<Character, Character>> pairs = table.keySet();
        Set<Character> Lset = new HashSet<>();
        Set<Character> Rset = new HashSet<>();
        for (Pair pair : pairs) {
            char key = (char) pair.getKey();
            char val = (char) pair.getValue();
            Lset.add(key);
            Rset.add(val);
        }
        mess = new String[Lset.size() + 1][Rset.size() + 1];
        Lchar = new char[Lset.size()];
        Rchar = new char[Rset.size()];
        int count = 0;
        for (char c : Lset) Lchar[count++] = c;
        count = 0;
        for (char c : Rset) Rchar[count++] = c;


        return this;
    }

    @Override
    public int getRowCount() {
        return mess.length;
    }

    @Override
    public int getColumnCount() {
        return mess[0].length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return "";
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
        if(rowIndex==0&&columnIndex==0){
            return "";
        }else if(columnIndex==0){
            return Lchar[rowIndex-1];
        }else if(rowIndex==0) {
            return Rchar[columnIndex-1];
        }else {
            return ptable.getOrDefault(new Pair<>(Lchar[rowIndex-1],Rchar[columnIndex-1]), "");
        }
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
