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
 
package mobile.magent;

import java.util.Date;
import server.central.*;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.lang.ClassNotFoundException;

import lib.*;

import java.rmi.RemoteException;

import javax.rmi.CORBA.*;
import javax.swing.ListSelectionModel;
import org.omg.CORBA.*;

/**
 * Implemetazione di un MobileAgent per gli studenti
 * @author uniBCM team
 */
public class MobileAgentStud extends javax.swing.JFrame implements MobileAgentItf, Serializable{

	/** Referenza al server centrale */
    private Central centralsrv;
    /** Utente */
	private String user="";
	/** Referenza al client sul quale e' in esecuzione il MobileAgent */
	private Reference ref=null;
	/** Indirizzo IP */
	private String ipadd="";

	/**
	 * Costrisce un nuovo MobileAgentStud
	 * @param centralsrv referenza server centrale
	 * @param user utente
	 */
    public MobileAgentStud(Central centralsrv, String user){

		this.centralsrv = centralsrv;
		this.user = user;

	}
    
    /** Creates new form MobileAgentProf */
    public MobileAgentStud() {
        initComponents();
    }

    /**
     * setta l'indirizzo IP. Questo server perche' IIOP non permette di trovare l'indirizzo
     * IP tramite i metodi di InetAddress
     * @param address indirizzo IP
     */
    public void setIPAddress(String address){

	this.ipadd = address;
	
	}

    /**
     * @see MobileAgentItf#runSession()
     */
    public void runSession() throws ClassNotFoundException, IOException, RemoteException{
    	
    initComponents();
    setVisible(true);
        
	Central obj=null;
	try{
	   obj = (Central)centralsrv;
	   ORB myORB = ORB.init(new String[0], null);
	   ((Stub)obj).connect(myORB);
	}catch(Exception e){
            e.printStackTrace();
    	}
	
	try{
		
        	ref = new Reference(user, this.ipadd, new Date());
        	centralsrv.addRef(ref);
        	
	}catch(RemoteException re){
    	javax.swing.JOptionPane.showMessageDialog(this,
          	  	"Impossibile contattare il server. L'applicazione verra' terminata.",
          	  	"Error Message",
           	 	javax.swing.JOptionPane.ERROR_MESSAGE);
    	
    	System.exit(0);
	}
        refreshList();

	}

    /**
     * Ricarica la lista di esami sostenuti dallo studente
     */
    private void refreshList(){

        String[] colNames = {"Nome Corso", "Voto"};
        Esame[] courses = null;

        try{
            courses = centralsrv.getExamsList(user);
	
        }catch(Exception e){
            if(e instanceof RemoteException){
            	javax.swing.JOptionPane.showMessageDialog(this,
                  	  	"Impossibile contattare il server. L'applicazione verrà terminata.",
                  	  	"Error Message",
                   	 	javax.swing.JOptionPane.ERROR_MESSAGE);
            
            	System.exit(0);
            }
            else
                	javax.swing.JOptionPane.showMessageDialog(this,
                      	  	"Impossibile ritornare la lista degli esami dello studente",
                      	  	"Error Message",
                       	 	javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        java.lang.Object[][] values = new java.lang.Object[courses.length][2];
        
        for(int i=0; i<courses.length; i++){
            values[i][0] = courses[i].getNomeEsame();
            values[i][1] = courses[i].getVoto();
        }

        coursesTable.setModel(new javax.swing.table.DefaultTableModel(
            values,
            colNames
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        coursesTable = new javax.swing.JTable();        
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        int width = 363;
        int height = 451;
        
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        
        int x = (screen.width-width)/2;
        int y = (screen.height-height)/2;
        
        setTitle("Mobile Agent Studente - IIOP");
        setMinimumSize(new java.awt.Dimension(width, height));
        setBounds(x,y,width,height);
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(null);

        jLabel1.setText("Benvenuto "+user);
        jPanel1.add(jLabel1);
        jLabel1.setBounds(10, 10, 230, 16);

        jLabel2.setText("Lista esami svolti");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(10, 50, 120, 16);

        jSeparator1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.add(jSeparator1);
        jSeparator1.setBounds(10, 30, 140, 12);

        coursesTable.setModel(new javax.swing.table.DefaultTableModel(
            new java.lang.Object [][] {

            },
            new String [] {
                "Titolo Esame", "Voto"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        coursesTable.setColumnSelectionAllowed(true);
        coursesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                coursesTableMouseClicked(evt);
            }
        });
        
        this.addWindowListener(new WindowAdapter(){

    		public void windowClosing(WindowEvent we){
    			try{
    			centralsrv.removeRef(ref);
    			}catch(RemoteException e){
                	javax.swing.JOptionPane.showMessageDialog(null,
                      	  	"Impossibile contattare il server. L'applicazione verrà terminata.",
                      	  	"Error Message",
                       	 	javax.swing.JOptionPane.ERROR_MESSAGE);
                	
                	System.exit(0);
    			}
    			System.exit(0);	
    		}
    	});
        
        jScrollPane1.setViewportView(coursesTable);
        coursesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(10, 70, 240, 270);

        
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logo.gif"))); // NOI18N
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/stud.png"))); // NOI18N

        jPanel1.add(jLabel3);
        jLabel3.setBounds(160, 280, 200, 290);


        jPanel1.add(jLabel4);
        jLabel4.setBounds(280, 20, 60, 70);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void coursesTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_coursesTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_coursesTableMouseClicked

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MobileAgentStud().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable coursesTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables

}
