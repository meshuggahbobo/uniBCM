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

package client;

import mobile.Mobile;
import mobile.magent.MobileAgentStud;
import server.proxy.*;
import lib.*;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.*;
import java.rmi.*;

import javax.naming.*;
import javax.rmi.PortableRemoteObject;
import java.lang.Error;

/**
 * Implementazione di un client di tipo IIOP
 * @author uniBCM team
 *
 */
public class ClientIIOP extends javax.swing.JFrame implements Client{
	
	/**
	 * Descrittore della classe
	 */
	private static final long serialVersionUID = 1L;
	/** Indirizzo del server */
	private String hostname = "";
	/** Protocollo di esecuzione */
	private String protocol="";
	/** Referenza al proxy */
	private Proxy obj = null;
	/** Oggetto di tipo mobile */
    private Mobile mob=null;
    /** Splash Screen */
    private static Logo logo=null;
    /** Oggetto tipo User per il login */
    private User sendUser=null;
    /** JFrame di registrazione nuovo utente */
    private PendingReg pendGui = null;
    /** Indirizzo IP del client */
	private String ipadd = "";
	
	/** Crea un nuovo ClientIIOP */
    public ClientIIOP() {
    	
        initComponents();

    }
	
    /**
     * @see Client#setServer(String)
     */
	public void setServer(String server){
		
		this.hostname = server;
		
	}
	
	/**
	 * @see Client#setProtocol(String)
	 */
	public void setProtocol(String protocol){
		
		this.protocol = protocol;
		
	}

	
	/**
	 * @see Client#setIPAddress(String)
	 */
	public void setIPAddress(String address){
		
		this.ipadd = address;
		
	}
	
	/**
	 * @see Runnable#run()
	 */
	public void run() throws RuntimeException{
	
		try{
			//Setto appropriatamente l'InitialContext per fare la lookup sul COSNaming JNDI.
	    	Properties propsC = new Properties();
	    	propsC.put("java.naming.factory.initial", "com.sun.jndi.cosnaming.CNCtxFactory");
	    	propsC.put("java.naming.provider.url", "iiop://" + hostname + ":5555");
	
	    	javax.naming.Context ic = new InitialContext(propsC);
	
		
			java.lang.Object objRef = ic.lookup("Proxy_Server");
			System.out.println("Effettuata lookup sul registro COSNaming.");
				
			// faccio la narrow sull'inerrfaccia remota Proxy
			obj = (Proxy)PortableRemoteObject.narrow(objRef, Proxy.class);
				
		}catch(Exception e){ System.out.println("Impossibile effettuare la lookup sul registro COSNaming"); }
	
		setVisible(true);	
	}
	
	/**
	 * Routine privata di login
	 */
	private void login(){
			
        String username="";
    	String password;

        username = jTextField1.getText();
        password = new String(jPasswordField1.getPassword());

        sendUser = new Stud();
   		sendUser.setId(username);
   		sendUser.setPassword(password);
		

        logo = new Logo(this.hostname,this.protocol);
        javax.swing.SwingUtilities.invokeLater(new Runnable(){public void run(){
    		if(execMobile() == false)
          	 	errorMessage();
   			}
        });
	}
	
	/**
	 * Stampa un messaggio di errore in caso di fallimento inserimento credenziali
	 */
	private void errorMessage(){
    	javax.swing.JOptionPane.showMessageDialog(this,
          	  	"Nome Utente/Password errata. Riprovare.",
          	  	"Error Message",
           	 	javax.swing.JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Routine di lancio del MobileAgentStud
     * @return true se il login è andato a buon fine, false altrimenti
     */
    private boolean execMobile(){

		try{
	            MarshalledObject<User> sendMarshUser = new MarshalledObject<User>(sendUser);
	            mob = (Mobile)((obj.login(sendMarshUser)).get());
		}catch(Exception e){
				if(e instanceof RemoteException){
					javax.swing.JOptionPane.showMessageDialog(this,
		          	  		"Il Server non è al momento disponibile. L'applicazione verrà terminata",
		          	  		"Error Message",
		           	 		javax.swing.JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
				else
	                	javax.swing.JOptionPane.showMessageDialog(this,
	                      	  	"Errore interno al sistema.",
	                      	  	"Error Message",
	                       	 	javax.swing.JOptionPane.ERROR_MESSAGE);
		}catch(Error err){System.out.println("ERRRRRRRRRRORE CATTURATO");}
	
	
		if(mob == null){
		 	logo.splashHide();
	        return false;
		}
		
		try{
			this.setVisible(false);
			((MobileAgentStud)mob).setIPAddress(this.ipadd);
			mob.runSession();
		}catch(Exception e){
			this.setVisible(true);
			javax.swing.JOptionPane.showMessageDialog(this,
              	  	"Errore esecuzione del MobileAgentStud.",
              	  	"Error Message",
               	 	javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	
		logo.splashHide();
		return true; 
    }

	/**
	 * Routine di registrazione nuovo utente
	 */
	private void reg(){

		pendGui = new PendingReg(this,obj);
        this.setVisible(false);
        pendGui.setVisible(true);
	
	}	
	
	/** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        int width = 510;
        int height = 270;
        
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        
        int x = (screen.width-width)/2;
        int y = (screen.height-height)/2;

        setTitle("Login - Client IIOP");
        setMinimumSize(new java.awt.Dimension(width, height));
        setBounds(x,y,width,height);
        setResizable(false);
        getContentPane().setLayout(null);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(null);

        jLabel1.setText("Password");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(20, 70, 90, 16);

        jLabel2.setText("Nome Utente");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(20, 30, 90, 16);
        jPanel1.add(jTextField1);
        jTextField1.setBounds(110, 20, 130, 28);

        jPanel1.add(jPasswordField1);
        jPasswordField1.setBounds(110, 60, 130, 28);

        jButton1.setText("Entra");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(10, 110, 90, 29);
	jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logo.gif"))); // NOI18N
        jPanel1.add(jLabel3);
        jLabel3.setBounds(300, 10, 180, 190);

        jButton2.setText("Registrati");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);
        jButton2.setBounds(10, 150, 110, 29);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(10, 10, 490, 210);

        jLabel4.setText("powered by uniBCM team");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(170, 230, 160, 16);

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
		login();
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
    	 reg();
    }
    
	// Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
	
}//chiudo classe
