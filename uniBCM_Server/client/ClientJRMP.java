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
import server.proxy.*;
import lib.*;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.rmi.*;
import java.util.*;

import javax.naming.*;
import javax.rmi.PortableRemoteObject;


/**
 * Implemetazione di un client di tipo JRMP
 * @author uniBCM team
 */
public class ClientJRMP extends javax.swing.JFrame implements Client{
	
	/** Indirizzo ip del server */
    private String hostname = "";
    /** Protocollo di esecuzione */
    private String protocol = "";
	/** Stub al Proxy */
    private Proxy obj = null;
    /** Oggetto di tipo Mobile ritornato in caso di successo al Login */
    private Mobile mob=null;
    /** Logo splashscreen */
    private static Logo logo=null;
    /** Utente da inviare con le credenziali per il login */
    private User sendUser=null;
    /** GUI per la registrazione pendente */
    private PendingReg pendGui = null;

    /** Creates new form ClientJRMP */
    public ClientJRMP() {
        initComponents();
    }

    /**
     * @see Client#setServer(String)
     */
    public void setServer(String server){
        //System.setProperty("javax.net.ssl.trustStore", trustStore);
		//System.setProperty("javax.net.ssl.trustStorePassword", trustStorepasswd);
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
	 * @deprecated
	 * Non usato nel ClientJRMP poiche' l'indirizzo IP e' preso con le classi di Java InetAddress
	 * @see InetAddress
	 */
	public void setIPAddress(String address){}

	/**
	 * @see Runnable#run()
	 */
    public void run(){
    	


    	setVisible(true);

		try{

			//interfaccia verso servizio di Naming --> provider JNDI pg 191-193
			//settiamo un provider JNDI per un registro RMI dove sarà in esecuzione il nostro proxy
			//vantaggio : consente di supportare diverse forme di naming
			Properties propsA = new Properties();
			//proposA.put("java.naming.factory.initial", ""com.sun.jndi.rmi.registry.RegistryContextFactory)
			propsA.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
			propsA.put("java.naming.provider.url", "rmi://" + hostname + ":2222");
	        	InitialContext ic = new InitialContext(propsA);

			//ProxyServer stesso nome durante fase Setup
			Object objRef = ic.lookup("Proxy_Server");
			System.out.println("Effettuata lookup sul registro RMI.");
			// faccio la narrow sull'inerrfaccia remota Proxy
			obj = (Proxy)PortableRemoteObject.narrow(objRef, Proxy.class);
            
            }catch(Exception e){ e.printStackTrace(); }

        
    }

    /**
     * Routine privata di login
     */
    private void login(){

        String username="";
        String password;

        username = jTextField1.getText();
        password = new String(jPasswordField1.getPassword());
        
        if(username.equalsIgnoreCase("Admin"))
        	sendUser = new Admin();
        else
        	sendUser = new Prof();

		sendUser.setId(username);
		sendUser.setPassword(password);

        logo = new Logo(this.hostname,this.protocol);
        javax.swing.SwingUtilities.invokeLater(new Runnable(){public void run(){
			if(execMobile() == false)
				
         	  	 	errorMessage();

		}

	});
    }
   
    private void errorMessage(){
    	javax.swing.JOptionPane.showMessageDialog(this,
          	  	"Nome Utente/Password errata. Riprovare.",
          	  	"Error Message",
           	 	javax.swing.JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Esegue il Mobile
     * @return true se non occorrono problemi, false altrimenti
     */
    private boolean execMobile(){

	try{
            MarshalledObject sendMarshUser = new MarshalledObject(sendUser);
			//vedere se fare interfaccia mobile server
			//verifica parentesi
            mob = (Mobile)((obj.login(sendMarshUser)).get());
		}catch(MarshalException me){
			System.out.println(" --- LOGIN ERROR --- Errore in invio/ricezione dati: "+me);
		//aggiungere RemoteException
		} catch (RemoteException re) {
			javax.swing.JOptionPane.showMessageDialog(this,
	          	  	"Il Server non è al momento disponibile.",
	          	  	"Error Message",
	           	 	javax.swing.JOptionPane.ERROR_MESSAGE);
		}catch (Exception e){
			javax.swing.JOptionPane.showMessageDialog(this,
	          	  	"Errore durante l'elaborazione della richiesta.",
	          	  	"Error Message",
	           	 	javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	if(mob == null){
	 	logo.splashHide();
        return false;
	}

			try{
				this.setVisible(false);
				mob.runSession();
				//vedere se fare RunnableException
			}catch(Exception e){
					System.out.println(" --- ERROR --- Errore in esecuzione dell'agente mobile.");
					e.printStackTrace();
			}
			
			logo.splashHide();
			return true;
   
    }

    /**
     * Routine privata di registrazione richiesta pendente
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
    @SuppressWarnings("unchecked")
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
        
        int width = 510;
        int height = 270;
        
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        
        int x = (screen.width-width)/2;
        int y = (screen.height-height)/2;
        

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login - Client JRMP");
        setMinimumSize(new java.awt.Dimension(width, height));
        setBounds(x,y,width,height);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
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

        jPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField1ActionPerformed(evt);
            }
        });
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

    private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
	
	login();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:

    reg();

    }//GEN-LAST:event_jButton2ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
              // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_formWindowClosing
    
    
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

}
