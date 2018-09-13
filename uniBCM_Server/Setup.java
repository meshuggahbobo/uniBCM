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

import server.central.*;
import server.login.*;
import server.proxy.*;
import server.proxy.Proxy;
import server.reg.*;
import lib.*;

import java.io.*;
import java.rmi.*;
import java.rmi.activation.*;
import java.rmi.RMISecurityManager;
import java.rmi.server.*;
import java.util.*;

import javax.naming.*;
import javax.rmi.PortableRemoteObject;
import javax.rmi.CORBA.*;
import org.omg.CORBA.*;

/**
 * Lancia il sistema uniBCM team PARTE SERVER.
 * Questa classe si preoccupa di:
 * - Installare un SecurityManager<br>
 * - Creare un nuovo Server Centrale ed esportarlo su JRMP e su IIOP<br>
 * - Creare un nuovo utente Amministratore con i settaggi di deafult (user:admin,pass=1234)<br>
 * - Creare i server attivabili di Login e Registrazione con relativi bind sui loro gruppi di attivazione.<br>
 * - Creare il Server Proxy in dual export<br>
 * - Registrare il Proxy su rmiregistry e cosnaming
 * @author uniBCM team
 *
 */
public final class Setup{
	
	public static void main(String[] args) throws RemoteException {
		
		System.out.println("\n\033[90m***\033[0m\033[36m "+new Date()+" \033[0m\033[90m*** \033[0m Lancio sistema uniBCM in corso...");
		//Settiamo il Security Manager
		System.setSecurityManager(new RMISecurityManager());
		
		try{
			Central centralServer = new CentralServer();
			//Esporto il server centrale sia su JRMP che IIOP (siccome abbiamo client con agenti
			//jrmp che iiop)
			
			UnicastRemoteObject.exportObject(centralServer);
			//verifica questo metodo
			Remote stubJRMP = UnicastRemoteObject.toStub(centralServer);
			
			PortableRemoteObject.exportObject(centralServer);
			Stub stubIIOP = (Stub) PortableRemoteObject.toStub(centralServer);
			ORB myORB = ORB.init(new String[0],null);
			stubIIOP.connect(myORB);

			System.out.println("\nCreata istanza di \033[37m\033[44m--- CENTRAL SERVER ---\033[0m");

			
			//Creo utente Admin
			User admin = new Admin();
			admin.setId("admin");
			admin.setPassword("1234");
			
			//Salviamo l'utente del db
			String objToSave = "dbUni/admin.usr";
			FileOutputStream f = new FileOutputStream(objToSave);
			ObjectOutputStream out = new ObjectOutputStream(f);
			out.writeObject(admin);
			out.close();
			
			//Setto proprietà di sistema, leggendo quelle passate da terminale
			//String policyGroupLogin  = System.getProperty("login.policy");
			//String implCodebaseLogin = System.getProperty("login.impl.codebase");
			String classeserverLogin = System.getProperty("login.classeserver");
			
			//cambiare il lancio da terminale con reg.
			String policyGroup  = System.getProperty("reg.policy");
			String implCodebaseReg = System.getProperty("reg.impl.codebase");
			String classeserverReg = System.getProperty("reg.classeserver");
	
			String leasePeriod = System.getProperty("java.rmi.dgc.leaseValue");

			String keystore = System.getProperty("javax.net.ssl.keyStore");
			String keystorepsw = System.getProperty("javax.net.ssl.keyStorePassword");
			String trustStore = System.getProperty("javax.net.ssl.trustStore");
			String trustStorepsw = System.getProperty("javax.net.ssl.trustStorePassword");
			
			Properties prop = new Properties();
			prop.put("java.security.policy",policyGroup);
			//se non funzica mettere server.reg....
           	 	//propReg.put("reg.impl.codebase", implCodebaseReg);
            		prop.put("java.class.path", "no_classpath");
			/// A T T E N Z I O N E
			prop.put("java.rmi.dgc.leaseValue", "1000");
			prop.put("javax.net.ssl.keyStore", keystore);
			prop.put("javax.net.ssl.keyStorePassword", keystorepsw);
			prop.put("javax.net.ssl.trustStore", trustStore);
			prop.put("javax.net.ssl.trustStorePassword", trustStorepsw);

			// INIZIALIZZAZIONE DELLE PROPERTIES PER DESCRIVERE LE CARATTERISTICHE DEI SERVER
			//Properties propLogin = new Properties();
			
			//propLogin.put("java.security.policy",policyGroupLogin);
            		//propLogin.put("login.impl.codebase", implCodebaseLogin);
           	 	//propLogin.put("java.class.path", "no_classpath");
			//propLogin.put("java.rmi.dgc.leaseValue", 1000);
			//propLogin.put("javax.net.ssl.keyStore", keystore);
			//propLogin.put("javax.net.ssl.keyStorePassword", keystorepsw);
			//propLogin.put("javax.net.ssl.trustStore", trustStore);
			//propLogin.put("javax.net.ssl.trustStorePassword", trustStorepsw);
			
			//Creo gruppi di attivazione
			ActivationGroupDesc regGroup = new ActivationGroupDesc(prop,null);
			ActivationGroupDesc loginGroup = new ActivationGroupDesc(prop,null);

			
			//Registro i gruppi di attivazione
			ActivationGroupID regIdGroup = ActivationGroup.getSystem().registerGroup(regGroup);
			ActivationGroupID loginIdGroup = ActivationGroup.getSystem().registerGroup(loginGroup);

			
			System.out.println("\nCreato gruppo di \033[30m\033[43m--- LOGIN ---\033[0m");
			System.out.println("Creato gruppo di \033[30m\033[46m--- REGISTRATION ---\033[0m");
			
			// CREAZIONE DELL'ACTIVATION DESCRIPTOR DEL SERVER ATTIVABILE

			ActivationDesc regDesc = new ActivationDesc(regIdGroup, classeserverReg, implCodebaseReg, null);
			
			//Registrazione dei  server di Login e Registration con il sistema di attivazione

			Registration regStub = (Registration)Activatable.register(regDesc);
			
			//Creo la struttura con i due stub
			Data_Structure struct = new Data_Structure((Central)stubJRMP,(Central)stubIIOP, (Registration)regStub);

			ActivationDesc loginDesc = new ActivationDesc(loginIdGroup, classeserverLogin, implCodebaseReg, new MarshalledObject (struct));
			
			Login loginStub = (Login)Activatable.register(loginDesc);
			
			System.out.println("\nCreato descrittore \033[30m\033[43m--- LOGIN SERVER ---\033[0m");
			System.out.println("Creato descrittore \033[30m\033[46m--- REGISTRATION SERVER ---\033[0m");

			//Creazione istanza del proxy
			Proxy proxyServer = new ProxyServer(loginStub,regStub);

			System.out.println("\nCreata istanza di \033[30m\033[42m--- PROXY SERVER ---\033[0m");
			
			//Dual export per proxy
			PortableRemoteObject.exportObject(proxyServer);
			UnicastRemoteObject.exportObject(proxyServer);
			
			// AL CONTRARIO DEL SERVER CENTRALE LA REFERENZA DEL PROXY VIENE SALVATA SUL COSNAMING
			Properties propProxyCOS = new Properties();
			propProxyCOS.put("java.naming.factory.initial","com.sun.jndi.cosnaming.CNCtxFactory");
			propProxyCOS.put("java.naming.provider.url","iiop://localhost:5555");
			//InitialContext ic = new InitialContext(propProxyCOS);
			javax.naming.Context ic = new InitialContext(propProxyCOS);
			ic.rebind("Proxy_Server",proxyServer);

			System.out.println("\nLa referenza del \033[30m\033[42m--- PROXY SERVER ---\033[0m è stata salvata sul COSNaming (port 5555)");

			// E SU RMIREGISTRY
			Properties propProxyRMI = new Properties();
			propProxyRMI.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
			propProxyRMI.put(javax.naming.Context.PROVIDER_URL, "rmi://localhost:2222");
			//InitialContext ic1 = new InitialContext(propProxyRMI);
			InitialContext ic1 = new InitialContext(propProxyRMI);
						
			ic1.rebind("Proxy_Server", proxyServer);
            		System.out.println("La referenza del \033[30m\033[42m--- PROXY SERVER ---\033[0m è stata salvata su RMIregisty (port 2222)");		

			System.out.println("\n\033[90m*** *** *** *** ***\033[0m");
		}catch(Exception e){
			e.printStackTrace();
		}
	}//chiudo main
	
}//chiudo classe
