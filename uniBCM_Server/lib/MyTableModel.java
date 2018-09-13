/*
 * uniBCM Project: an exams's management system for university, developed with Java RMI
 * 
 * Copyleft 2009  uniBCM Team(Alberto Cordioli, Andrea Oboe)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/.
 *
 * E-Mail:	cordioli [dot] alberto [at] gmail [dot] com
 * 			oboe [dot] andrea [at] gmail [dot] com
 */

package lib;

import javax.swing.table.AbstractTableModel;

/**
 * Classe che definisce un modello per la tebella utilizzata nel MobileServerAdmin
 * @author uniBCMTeam
 */
public class MyTableModel extends AbstractTableModel{
    /**
	 * Descrittore della classe
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Intestazioni delle colonne
	 */
	private String[] columnNames;
	/**
	 * Dati contenuti nella tabella
	 */
    private Object[][] data;
    /**
     * Variabile di supporto per controllare quante righe della tabella sono selezionate
     */
    private int count;

    /**
     * Costruisce un nuovo modello di tabella
     * @param columnNames Intestazioni delle colonne
     * @param data Dati contenuti nella tabella
     */
    public MyTableModel(String[] columnNames, Object[][] data){
        this.columnNames=columnNames;
        this.data=data;
        this.count=0;
    }
    
    /**
     * @see AbstractTableModel#getColumnCount()
     */
    public int getColumnCount() {
        return columnNames.length;
    }
    
    /**
     * @see AbstractTableModel#getRowCount()
     */
    public int getRowCount() {
        return data.length;
    }
    
    /**
     * @see AbstractTableModel#getColumnName(int)
     */
    public String getColumnName(int col) {
        return columnNames[col];
    }

    /**
     * @see AbstractTableModel#getValueAt(int,int)
     */
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }
    
    /**
     * @see AbstractTableModel#setValueAt(Object,int,int)
     */
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
        if(col==4){
                if((Boolean)this.getValueAt(row, col))
                    count++;
                else
                    count--;
        }

    }
    
    /**
     * JTable utilizza questo metodo per determinare l'aspetto
     * di ogni cella. Se questo metodo non e' implementato, allora
     * la colonna di tipo booleano conterrebbe stringhe true/false
     * invece che dei check box.
     * 
     * @see AbstractTableModel#getColumnClass(int)
     */
    @SuppressWarnings("unchecked")
	public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
    */
    /**
     * Non e' necessario implementare questo metodo se la tabella
     * e' editabile. Nel nostro caso e' editabile solo l'ultima
     * colonna.
     * 
     * @see AbstractTableModel#isCellEditable(int,int)
     */
    public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if(col == 4 ) return true;
            return false;
    }

    /**
     * Restituisce il numero di righe selezionate nella tabella
     * @return Numero di righe selezionate nella tabella
     */
    public int getCount(){
        return this.count;
    }
}
