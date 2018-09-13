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

package mobile.mserver;

import server.central.*;
import server.reg.*;
import server.login.*;
import lib.*;

import java.io.IOException;
import java.io.Serializable;
import java.lang.ClassNotFoundException;
import java.rmi.*;
import java.rmi.server.*;

import java.net.InetAddress;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import java.util.*;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
/**
 * Implemetazione di un Mobile Server per l'amministratore del sistema
 * @author uniBCM team
 */
public class MobileServerAdmin extends javax.swing.JFrame implements MobileServerItf,Serializable {

    static final long serialVersionUID = 42L;

    /** Referenza al server centrale */
	private Central centralsrv;
	/** Stub al server di registrazione */
	private Registration regsrv;
	/** Stub al server di Login */
    private Login loginsrv;

    /** array di utenti pendenti */
	private User[] pendUserList, 
	/** e di utenti registrati */
					userList;

	private Icon update;

	private String result="";
    private int count;

    private AbstractTableModel modelPending;
    private Reference ref=null;

    /** Creates new form MobileServerAdmin */
    public MobileServerAdmin() {
        initComponents();
    }

    /**
     * Costruisce un nuovo MobileServer. N.B. il server non e' esportato alla creazione
     * @param centralsrv stub server centrale
     * @param regsrv stub server registrazione
     * @param loginsrv stub server login
     */
    public MobileServerAdmin(Central centralsrv, Registration regsrv, Login loginsrv){

        this.centralsrv = centralsrv;
        this.regsrv = regsrv;
        this.loginsrv = loginsrv;
        this.count=0;
    }

    /**
     * @see MobileServerItf#runSession()
     */
	public void runSession() throws ClassNotFoundException, IOException, RemoteException{
        initComponents();
        updateProf.setVisible(false);
        updateStud.setVisible(false);
        setVisible(true);

        try{
            InetAddress ia = InetAddress.getLocalHost();
            String ip = ia.getHostAddress();
            ref = new Reference("admin", ip, new Date());
            centralsrv.addRef(ref);
        }catch(UnknownHostException uhe){
        	javax.swing.JOptionPane.showMessageDialog(this,
              	  	"Impossibile determinare l'indirizzo ip di questo host.",
              	  	"Error Message",
               	 	javax.swing.JOptionPane.ERROR_MESSAGE);
    	}catch(RemoteException re){
        	javax.swing.JOptionPane.showMessageDialog(this,
              	  	"Impossibile contattare il server. L'applicazione verrà terminata.",
              	  	"Error Message",
               	 	javax.swing.JOptionPane.ERROR_MESSAGE);
        	
        	System.exit(0);
    	}

	java.util.Timer timer = new java.util.Timer();
	TimerTask task = new MyTask();

	timer.schedule(task, 1000, 30000);

        pendingGest();
        tabUtenti.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                int row = tabUtenti.getSelectedRow();
                viewInfo.setEnabled(true);
                removeUser.setEnabled(true);
                suspendUser.setEnabled(true);
                enableUser.setEnabled(true);
                if(tabUtenti.getValueAt(row,3).equals("Professore")){
                    addCourse.setEnabled(true);
                }else{
                    addCourse.setEnabled(false);
                }
            }
        });
        
        MobileServerItf stubMS = null;
        try {
            stubMS = (MobileServerItf)UnicastRemoteObject.exportObject(this, 36000);
            loginsrv.setMSRef(stubMS);
            //centralsrv.setMSRef(stubMS);
        }catch (RemoteException re){
                	javax.swing.JOptionPane.showMessageDialog(null,
                      	  	"Impossibile contattare il server",
                      	  	"Error Message",
                       	 	javax.swing.JOptionPane.ERROR_MESSAGE);
        		
        }
    }

	/**
	 * @see MobileServerItf#ping()
	 */
   	public String ping(){
		String fullInfo = "";
		InetAddress thisIp = null;

		try{
			thisIp = InetAddress.getLocalHost();
		}catch(Exception e){
			System.out.println("Impossibile determinare l'indirizzo IP "+e);
		}

		fullInfo += "\n\n --- Username:\t\t"+System.getProperty("user.name")+"\n";
		fullInfo += " --- Sistema Operativo:\t"+System.getProperty("os.name")+" "+System.getProperty("os.version")+"\n";
		fullInfo += " --- Hostname/IP:\t\t"+thisIp.toString()+"\n";
		fullInfo += " --- Paese:\t\t\t"+System.getProperty("user.country")+"\n\n";

		return fullInfo;
	}

   	/**
   	 * Gestisce le richieste pendenti, ricaricando la lista
   	 */
    private void pendingGest(){
        try{
    		pendUserList = regsrv.pendingUserList();
    		refreshList(pendUserList,0);

    	}catch(Exception e){
    		if(e instanceof RemoteException){
				javax.swing.JOptionPane.showMessageDialog(null,
                  	  	"Impossibile contattare il server",
                  	  	"Error Message",
                   	 	javax.swing.JOptionPane.ERROR_MESSAGE);
		}
            	
    		else
            	javax.swing.JOptionPane.showMessageDialog(null,
                  	  	"Errore interno al sistema. Si prega di contattare uniBCM team!",
                  	  	"Error Message",
                   	 	javax.swing.JOptionPane.ERROR_MESSAGE);
    	}
	
    }

    /**
     * Gestisce gli utenti regiistrati al sistema, ricaricando la lista
     */
    private void dbGest(){
        try{
    		userList = regsrv.userList();
    		refreshList(userList,1);
    	}catch(Exception e){
    		if(e instanceof RemoteException)
            	javax.swing.JOptionPane.showMessageDialog(null,
                  	  	"Impossibile contattare il server",
                  	  	"Error Message",
                   	 	javax.swing.JOptionPane.ERROR_MESSAGE);
    		else
            	javax.swing.JOptionPane.showMessageDialog(null,
                  	  	"Errore interno al sistema. Si prega di contattare uniBCM team!",
                  	  	"Error Message",
                   	 	javax.swing.JOptionPane.ERROR_MESSAGE);
    	}
    }

    /**
     * Ricarica le liste di richieste pendenti o di utenti registrati a seconda del parametro
     * component
     * @param userList array di utenti
     * @param component lista da caricare (0 = richieste pendenti,1 = utenti)
     */
    private void refreshList(User[] userList, int component){
        if (component == 0){	
            confirm.setEnabled(false);
            refused.setEnabled(false);
            confirmAll.setEnabled(false);
            refusedAll.setEnabled(false);
            
            if(userList.length > 0){
                confirmAll.setEnabled(true);
                refusedAll.setEnabled(true);
            }

            String[] colNames = { "ID" , "Nome" , "Cognome" , "Account" , "Accetta" };
            Object[][] values = new Object[userList.length][5];
            for(int i=0; i<userList.length; i++){
                values[i][0] = userList[i].getId();
                values[i][1] = userList[i].getName();
                values[i][2] = userList[i].getSurname();
                values[i][3] = userList[i].getType();
                values[i][4] = new Boolean(false);
            }
            modelPending = new MyTableModel(colNames,values);
            
            tabPendenti.setModel(modelPending);

    }else{
            viewInfo.setEnabled(false);
            addCourse.setEnabled(false);
            removeUser.setEnabled(false);
            suspendUser.setEnabled(false);
            enableUser.setEnabled(false);
            if(userList.length>0){
                viewInfo.setEnabled(true);
                removeUser.setEnabled(true);
                suspendUser.setEnabled(true);
                enableUser.setEnabled(true);
            }
            String[] colNames = { "ID" , "Nome" , "Cognome" , "Account" ,"Sospeso" };
            Object[][] values = new Object[userList.length][5];
            for(int i=0; i<userList.length; i++){
                values[i][0] = userList[i].getId();
                values[i][1] = userList[i].getName();
                values[i][2] = userList[i].getSurname();
                values[i][3] = userList[i].getType();
                values[i][4] = (userList[i].getSuspend()) ? "Si" : "No";
            }

            AbstractTableModel modelUser = new DefaultTableModel(values,colNames);
            tabUtenti.setModel(modelUser);
    	}
   	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        label1 = new javax.swing.JLabel();
        tab = new javax.swing.JTabbedPane();
        scheda1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabPendenti = new javax.swing.JTable();
        confirmAll = new javax.swing.JButton();
        refusedAll = new javax.swing.JButton();
        confirm = new javax.swing.JButton();
        refused = new javax.swing.JButton();
        scheda2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabUtenti = new javax.swing.JTable();
        suspendUser = new javax.swing.JButton();
        enableUser = new javax.swing.JButton();
        removeUser = new javax.swing.JButton();
        addCourse = new javax.swing.JButton();
        viewInfo = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        updateProf = new javax.swing.JLabel();
        updateStud = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        connUsers = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        lastUser = new javax.swing.JLabel();

        int width = 900;
        int height = 670;
        
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        
        int x = (screen.width-width)/2;
        int y = (screen.height-height)/2;
        
        setTitle("Mobile Server Amministratore - JRMP");
        setMinimumSize(new java.awt.Dimension(width, height));
        setBounds(x,y,width,height);
        setResizable(false);
        getContentPane().setLayout(null);

        label1.setText("Benvenuto Amministratore!");
        getContentPane().add(label1);
        label1.setBounds(50, 30, 180, 16);

        tab.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabStateChanged(evt);
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

        tabPendenti.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome", "Cognome", "Accetta"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabPendenti.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabPendentiMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabPendenti);

        confirmAll.setText("Accetta tutte le richieste");
        
        confirmAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmAllActionPerformed(evt);
            }
        });

        refusedAll.setText("Rifiuta tutte le richieste");

        refusedAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refusedAllActionPerformed(evt);
            }
        });

        confirm.setText("Accetta utenti selezionati");

        confirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmActionPerformed(evt);
            }
        });

        refused.setText("Rifiuta utenti selezionati");

        refused.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refusedActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout scheda1Layout = new javax.swing.GroupLayout(scheda1);
        scheda1.setLayout(scheda1Layout);
        scheda1Layout.setHorizontalGroup(
            scheda1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scheda1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 619, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(scheda1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(confirmAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(refused, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(refusedAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(confirm))
                .addGap(65, 65, 65))
        );
        scheda1Layout.setVerticalGroup(
            scheda1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scheda1Layout.createSequentialGroup()
                .addGroup(scheda1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(scheda1Layout.createSequentialGroup()
                        .addGap(99, 99, 99)
                        .addComponent(confirmAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(refusedAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(confirm)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(refused))
                    .addGroup(scheda1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)))
                .addContainerGap())
        );

        tab.addTab("Gestione richieste pendenti", scheda1);

        tabUtenti.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "Cognome", "Sospeso"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabUtenti.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabUtentiMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabUtenti);

        suspendUser.setText("Sospendi");

        suspendUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suspendUserActionPerformed(evt);
            }
        });

        enableUser.setText("Abilita");

        enableUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableUserActionPerformed(evt);
            }
        });

        removeUser.setText("Elimina");

        removeUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeUserActionPerformed(evt);
            }
        });

        addCourse.setText("Aggiungi corso");

        addCourse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCourseActionPerformed(evt);
            }
        });

        viewInfo.setText("Visualizza informazioni");

        viewInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewInfoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout scheda2Layout = new javax.swing.GroupLayout(scheda2);
        scheda2.setLayout(scheda2Layout);
        scheda2Layout.setHorizontalGroup(
            scheda2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scheda2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(74, 74, 74)
                .addGroup(scheda2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(enableUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addCourse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(viewInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(removeUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(suspendUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(71, 71, 71))
        );
        scheda2Layout.setVerticalGroup(
            scheda2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scheda2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(scheda2Layout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(viewInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addCourse)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(suspendUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(enableUser))
        );

        tab.addTab("Gestione utenti del sistema", scheda2);

        getContentPane().add(tab);
        tab.setBounds(0, 80, 890, 398);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logo.gif"))); // NOI18N
        getContentPane().add(jLabel1);
        jLabel1.setBounds(690, 450, 230, 290);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/admin.png"))); // NOI18N
        getContentPane().add(jLabel2);
        jLabel2.setBounds(10, 10, 40, 40);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setToolTipText("null");
        jPanel1.setName("null"); // NOI18N

        updateProf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/updateProf.png"))); // NOI18N

        updateStud.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/updateStud.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(updateStud, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                    .addComponent(updateProf, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(updateProf)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(updateStud)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1);
        jPanel1.setBounds(650, 10, 270, 100);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        connUsers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/connUsers.png"))); // NOI18N

        jSeparator1.setForeground(new java.awt.Color(51, 51, 51));

        lastUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lastUser.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lastUser, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(connUsers, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE))
                .addContainerGap(451, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(connUsers)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lastUser, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2);
        jPanel2.setBounds(10, 480, 670, 150);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void refusedAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refusedAllActionPerformed
        try{
            result = regsrv.delAllPendUser();
            
            JOptionPane.showMessageDialog(this, result, "Attenzione", JOptionPane.INFORMATION_MESSAGE, null);
    		result = "";
    		
    		pendUserList = regsrv.pendingUserList();
			refreshList(pendUserList,0);
		}catch(Exception e){
    		if(e instanceof RemoteException)
            	javax.swing.JOptionPane.showMessageDialog(null,
                  	  	"Impossibile contattare il server",
                  	  	"Error Message",
                   	 	javax.swing.JOptionPane.ERROR_MESSAGE);
    		else
            	javax.swing.JOptionPane.showMessageDialog(null,
                  	  	"Errore interno al sistema. Si prega di contattare uniBCM team!",
                  	  	"Error Message",
                   	 	javax.swing.JOptionPane.ERROR_MESSAGE);
    	}

    }//GEN-LAST:event_refusedAllActionPerformed

    private void suspendUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suspendUserActionPerformed
        String user=(String)tabUtenti.getValueAt(tabUtenti.getSelectedRow(),0);

        try{
            result = regsrv.setUserState(user, true);
            JOptionPane.showMessageDialog(this, result, "Attenzione", JOptionPane.INFORMATION_MESSAGE, null);
            result = "";
            userList = regsrv.userList();
            refreshList(userList,1);
        }catch(Exception e){
    		if(e instanceof RemoteException)
            	javax.swing.JOptionPane.showMessageDialog(null,
                  	  	"Impossibile contattare il server",
                  	  	"Error Message",
                   	 	javax.swing.JOptionPane.ERROR_MESSAGE);
    		else
            	javax.swing.JOptionPane.showMessageDialog(null,
                  	  	"Errore interno al sistema. Si prega di contattare uniBCM team!",
                  	  	"Error Message",
                   	 	javax.swing.JOptionPane.ERROR_MESSAGE);
        }

}//GEN-LAST:event_suspendUserActionPerformed

    private void removeUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeUserActionPerformed
        String user=(String)tabUtenti.getValueAt(tabUtenti.getSelectedRow(),0);
        try{
            result = regsrv.delUser(user);
            JOptionPane.showMessageDialog(this, result, "Attenzione", JOptionPane.INFORMATION_MESSAGE, null);
            result = "";
            userList = regsrv.userList();
            refreshList(userList,1);
        }catch(Exception e){
    		if(e instanceof RemoteException)
            	javax.swing.JOptionPane.showMessageDialog(null,
                  	  	"Impossibile contattare il server",
                  	  	"Error Message",
                   	 	javax.swing.JOptionPane.ERROR_MESSAGE);
    		else
            	javax.swing.JOptionPane.showMessageDialog(null,
                  	  	"Errore interno al sistema. Si prega di contattare uniBCM team!",
                  	  	"Error Message",
                   	 	javax.swing.JOptionPane.ERROR_MESSAGE);
    	}

}//GEN-LAST:event_removeUserActionPerformed

    private void viewInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewInfoActionPerformed
        String user=(String)tabUtenti.getValueAt(tabUtenti.getSelectedRow(),0);
        try{
			result = centralsrv.getInfo(user);
			JOptionPane.showMessageDialog(this, result, "Informazioni", JOptionPane.INFORMATION_MESSAGE, null);
			result = "";
		}catch (RemoteException re){
        	javax.swing.JOptionPane.showMessageDialog(null,
              	  	"Impossibile contattare il server. pippossssssssssssssssssssssssss",
              	  	"Error Message",
               	 	javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		
}//GEN-LAST:event_viewInfoActionPerformed

private void addCourseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCourseActionPerformed
    String courseName = JOptionPane.showInputDialog("Inserire nome del corso");
    String year = JOptionPane.showInputDialog("Inserire anno accademico");
    String user=(String)tabUtenti.getValueAt(tabUtenti.getSelectedRow(),0);
	try{
		result = centralsrv.addCourse(user, courseName, year);
		JOptionPane.showMessageDialog(this, result, "Informazioni", JOptionPane.INFORMATION_MESSAGE, null);
		result = "";
	}catch (RemoteException re){
    	javax.swing.JOptionPane.showMessageDialog(null,
          	  	"Impossibile contattare il server",
          	  	"Error Message",
           	 	javax.swing.JOptionPane.ERROR_MESSAGE);
	}
	
}//GEN-LAST:event_addCourseActionPerformed

private void enableUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableUserActionPerformed
    String user=(String)tabUtenti.getValueAt(tabUtenti.getSelectedRow(),0);

    try{
		result = regsrv.setUserState(user, false);
		JOptionPane.showMessageDialog(this, result, "Attenzione", JOptionPane.INFORMATION_MESSAGE, null);
		result = "";
		userList = regsrv.userList();
		refreshList(userList,1);
	}catch(Exception e){
		if(e instanceof RemoteException)
        	javax.swing.JOptionPane.showMessageDialog(null,
              	  	"Impossibile contattare il server",
              	  	"Error Message",
               	 	javax.swing.JOptionPane.ERROR_MESSAGE);
		else
        	javax.swing.JOptionPane.showMessageDialog(null,
              	  	"Errore interno al sistema. Si prega di contattare uniBCM team!",
              	  	"Error Message",
               	 	javax.swing.JOptionPane.ERROR_MESSAGE);
	}
}//GEN-LAST:event_enableUserActionPerformed

private void confirmAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmAllActionPerformed
    try{
        result = regsrv.confAllUser();
        JOptionPane.showMessageDialog(this, result, "Attenzione", JOptionPane.INFORMATION_MESSAGE, null);
    	result = "";
    	pendUserList = regsrv.pendingUserList();
		refreshList(pendUserList,0);
	}catch(Exception e){
		if(e instanceof RemoteException)
        	javax.swing.JOptionPane.showMessageDialog(null,
              	  	"Impossibile contattare il server",
              	  	"Error Message",
               	 	javax.swing.JOptionPane.ERROR_MESSAGE);
		else
        	javax.swing.JOptionPane.showMessageDialog(null,
              	  	"Errore interno al sistema. Si prega di contattare uniBCM team!",
              	  	"Error Message",
               	 	javax.swing.JOptionPane.ERROR_MESSAGE);
	}
}//GEN-LAST:event_confirmAllActionPerformed

private void confirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmActionPerformed
	try{
	       for(int i=0; i<tabPendenti.getRowCount();i++){
	            if((java.lang.Boolean)tabPendenti.getValueAt(i, 4)){
	                
						result += regsrv.confUser((String)tabPendenti.getValueAt(i, 0), true) + "\n";
					
				}
	        }
			JOptionPane.showMessageDialog(this, result, "Attenzione", JOptionPane.INFORMATION_MESSAGE, null);
			result = "";
			pendUserList = regsrv.pendingUserList();
			refreshList(pendUserList,0);
	}catch(Exception e){
		if(e instanceof RemoteException)
        	javax.swing.JOptionPane.showMessageDialog(null,
              	  	"Impossibile contattare il server",
              	  	"Error Message",
               	 	javax.swing.JOptionPane.ERROR_MESSAGE);
		else
        	javax.swing.JOptionPane.showMessageDialog(null,
              	  	"Errore interno al sistema. Si prega di contattare uniBCM team!",
              	  	"Error Message",
               	 	javax.swing.JOptionPane.ERROR_MESSAGE);
	}
}//GEN-LAST:event_confirmActionPerformed

private void refusedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refusedActionPerformed
	try{
	    for(int i=0; i<tabPendenti.getRowCount();i++){
	        if((java.lang.Boolean)tabPendenti.getValueAt(i, 4)){
	            
	                result += regsrv.confUser((String)tabPendenti.getValueAt(i, 0), false) + "\n";
				
			}
	    }
		JOptionPane.showMessageDialog(this, result, "Attenzione", JOptionPane.INFORMATION_MESSAGE, null);
		result = "";
		pendUserList = regsrv.pendingUserList();
		refreshList(pendUserList,0);
	}catch(Exception e){
		if(e instanceof RemoteException)
        	javax.swing.JOptionPane.showMessageDialog(null,
              	  	"Impossibile contattare il server",
              	  	"Error Message",
               	 	javax.swing.JOptionPane.ERROR_MESSAGE);
		else
        	javax.swing.JOptionPane.showMessageDialog(null,
              	  	"Errore interno al sistema. Si prega di contattare uniBCM team!",
              	  	"Error Message",
               	 	javax.swing.JOptionPane.ERROR_MESSAGE);
	}
}//GEN-LAST:event_refusedActionPerformed

private void tabStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabStateChanged
    if(tab.getSelectedIndex()==0){
        try{
            pendUserList = regsrv.pendingUserList();
            refreshList(pendUserList,0);
        }catch(Exception e){
    		if(e instanceof RemoteException){
			if(e instanceof ConnectIOException){//per ssl no
				javax.swing.JOptionPane.showMessageDialog(null,
                  	  	"Non possiedi il certificato uniBCM! Il programma verra' terminato.",
                  	  	"Error Message",
                   	 	javax.swing.JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}else
            			javax.swing.JOptionPane.showMessageDialog(null,
                  	  	"Impossibile contattare il server",
                  	  	"Error Message",
                   	 	javax.swing.JOptionPane.ERROR_MESSAGE);
		}

    		else
            	javax.swing.JOptionPane.showMessageDialog(null,
                  	  	"Errore interno al sistema. Si prega di contattare uniBCM team!",
                  	  	"Error Message",
                   	 	javax.swing.JOptionPane.ERROR_MESSAGE);
    	}
    }
    if(tab.getSelectedIndex()==1){
        try{
            userList = regsrv.userList();
            refreshList(userList,1);
        }catch(Exception e){
    		if(e instanceof RemoteException)
            	javax.swing.JOptionPane.showMessageDialog(null,
                  	  	"Impossibile contattare il server",
                  	  	"Error Message",
                   	 	javax.swing.JOptionPane.ERROR_MESSAGE);
    		else
            	javax.swing.JOptionPane.showMessageDialog(null,
                  	  	"Errore interno al sistema. Si prega di contattare uniBCM team!",
                  	  	"Error Message",
                   	 	javax.swing.JOptionPane.ERROR_MESSAGE);
    	}
    }
}//GEN-LAST:event_tabStateChanged

private void tabComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_tabComponentShown
    // TODO add your handling code here:
}//GEN-LAST:event_tabComponentShown

private void tabUtentiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabUtentiMouseClicked
    if(tabUtenti.getValueAt(tabUtenti.getSelectedRow(), 3).equals("Professore"))
        addCourse.setEnabled(true);
    else
        addCourse.setEnabled(false);
}//GEN-LAST:event_tabUtentiMouseClicked

private void tabPendentiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabPendentiMouseClicked
    if(((MyTableModel)modelPending).getCount()==0){
            confirm.setEnabled(false);
            refused.setEnabled(false);
    }else{
            confirm.setEnabled(true);
            refused.setEnabled(true);
    }
}//GEN-LAST:event_tabPendentiMouseClicked

private void tabPendentiPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tabPendentiPropertyChange
    // TODO add your handling code here:
}//GEN-LAST:event_tabPendentiPropertyChange

/**
 * Task che periodicamente richiede la lista di richieste pendenti
 * @author uniBCM team
 *
 */
private class MyTask extends TimerTask{
        public void run(){

            //parte relativa alle richieste pendenti
            try{
                pendUserList = regsrv.pendingUserList();
            }catch(Exception e){
        		if(e instanceof RemoteException)
                	javax.swing.JOptionPane.showMessageDialog(null,
                      	  	"Impossibile contattare il server per aggiornare lista utenti pendenti.",
                      	  	"Error Message",
                       	 	javax.swing.JOptionPane.ERROR_MESSAGE);
        		else
                	javax.swing.JOptionPane.showMessageDialog(null,
                      	  	"Errore interno al sistema. Si prega di contattare uniBCM team!",
                      	  	"Error Message",
                       	 	javax.swing.JOptionPane.ERROR_MESSAGE);
        	}

            int countP=0,countS=0;
            if(pendUserList.length > 0){
                for(int i=0;i<pendUserList.length;i++){
                    if(pendUserList[i] instanceof Prof)
                        countP++;
                    else
                        countS++;
                }
            }
            if(countP>0){
               updateProf.setText(countP+" richieste professori!");
               updateProf.setVisible(true);
               }else
            updateProf.setVisible(false);

            if(countS>0){
                updateStud.setText(countS+" richieste studenti!");
                updateStud.setVisible(true);
            }else
               updateStud.setVisible(false);

             try{
                int refCount = centralsrv.getReferenceCount()-1;
                if(refCount != 0)
                    connUsers.setText("Numero utenti connessi: "+refCount);
                else
                    connUsers.setText("Non ci sono utenti attualmente connessi");
                lastUser.setText(centralsrv.getLastRef());
                
                }catch(RemoteException re){
                	javax.swing.JOptionPane.showMessageDialog(null,
                      	  	"Impossibile contattare il server per aggiornare statistiche utenti connessi al sistema.",
                      	  	"Error Message",
                       	 	javax.swing.JOptionPane.ERROR_MESSAGE);
                }
           }



    }
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MobileServerAdmin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addCourse;
    private javax.swing.JButton confirm;
    private javax.swing.JButton confirmAll;
    private javax.swing.JLabel connUsers;
    private javax.swing.JButton enableUser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel label1;
    private javax.swing.JLabel lastUser;
    private javax.swing.JButton refused;
    private javax.swing.JButton refusedAll;
    private javax.swing.JButton removeUser;
    private javax.swing.JPanel scheda1;
    private javax.swing.JPanel scheda2;
    private javax.swing.JButton suspendUser;
    private javax.swing.JTabbedPane tab;
    private javax.swing.JTable tabPendenti;
    private javax.swing.JTable tabUtenti;
    private javax.swing.JLabel updateProf;
    private javax.swing.JLabel updateStud;
    private javax.swing.JButton viewInfo;
    // End of variables declaration//GEN-END:variables

}
