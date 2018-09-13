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

/**
 * 
 * Nota per colori:
 * PROXY : verde background \033[42m
 * REGISTRATION: cyan background \033[46m
 * LOGIN: giallo background \033[43m
 * CENTRAL : blu background \033[44m bianco foreground \033[37m
 * 	Avvisi normali : colore standar shell corrente in uso
 *  Avvisi livello medio : giallo foreground \033[33m
 *  Avvisi livello alto(sicurezza) : rosso foreground \033[31m
 *  
 *  Per resettare ai colori di default di sistema : \033[0m
 *  
 *  per ulteriori informazioni : http://en.wikipedia.org/wiki/ANSI_escape_code
 *  
 */

//verificare se bisogna importare le eccezzioni
package server.login;

import mobile.Mobile;
import mobile.magent.*;
import mobile.mserver.*;
import server.central.*;

import lib.*;

import java.io.*;

import java.lang.ClassNotFoundException;
import java.rmi.*;
import java.rmi.activation.*;
import java.rmi.server.*;

import java.util.Date;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import javax.rmi.PortableRemoteObject;

/**
 * Implementazione di un Login server attivabile che implementa l'intefaccia Login Unreferenced
 * ed estende Activatable
 * @see Activatable
 * @see Login
 * @see Unreferenced
 * @author uniBCM team
 *
 */
public class LoginServer extends Activatable implements Login, Unreferenced{
	
	Data_Structure stubs = null;

	/**
	 * Costruttore del server attivabile, si esporta sulla porta 35000 su SSL
	 * @param id identificativo del server
	 * @param data parametri di inizializzazione del server
	 * @see SslRMIClientSocketFactory
	 * @see SslRMIServerSocketFactory
	 * @throws ActivationException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public LoginServer(ActivationID id, MarshalledObject data) throws ActivationException, ClassNotFoundException, IOException {
		
		super(id,35000, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory());
		// OTTENGO LA REFERENZA AL SISTEMA DI ATTIVAZIONE
		ActivationSystem actS = ActivationGroup.getSystem();
		// getID È UN METODO DI ACTIVATABLE CHE TORNA L'ID ASSOCIATO AL SERVER
		//ActivationID id = getID();
		// RICAVO L'ACTIVATION DESCRIPTOR DEL SERVER
		ActivationDesc actD = actS.getActivationDesc(id);
		// RICAVO L'OGGETTO DI TIPO DATA_STRUCTURE CON CUI IL SERVER È SETTATO
		//Da Vedere!!!!!!!!!!!!!
		Object object = ((MarshalledObject)actD.getData()).get();
		// CASTO L'OGGETTO GENERALE AL TIPO SPECIFICO
		stubs = (Data_Structure)object;

	}
	
	/**
	 * @see Login#login(User)
	 */
	public Mobile login(User checkUser) throws RemoteException{
		
		System.out.println("\n\033[90m***\033[0m\033[36m "+new Date()+" \033[0m\033[90m*** \033[0m  Richiesta di login in corso...");
		int control = -1;
		Mobile mob = null;
		
		String username = checkUser.getId();
		String fileName =  "dbUni/" + username + ".usr";
		User userFromFile = null;
		boolean exists = (new File(fileName)).exists();

		System.out.println("\033[30m\033[43m--- LOGIN SERVER ---\033[0m  Tentativo di accesso da parte dell'utente "+username);
		
		if(exists){
			try{
				FileInputStream f = new FileInputStream (fileName);
				ObjectInputStream in = new ObjectInputStream(f);
				userFromFile = (User)(in.readObject());
				in.close();
			}catch (Exception e) {
				System.out.println("\033[30m\033[43m--- LOGIN SERVER ---\033[0m\033[31m  W A R N I N G : Recupero dati utente "+username+"fallito!\033[0m");
				e.printStackTrace();
				return null;
			}
		}
		
		else{
			System.out.println("\033[30m\033[43m--- LOGIN SERVER ---\033[0m\033[31m  W A R N I N G : Tentativo recupero dati utente inesistente! Possibile hacking...\033[0m");
			return null;
		}
		
		System.out.println("\033[30m\033[43m--- LOGIN SERVER ---\033[0m  Utente "+username+" estratto da dbUni.");
		
		//per evitare che utenti studenti si logghino come JRMP???
		if(!(checkUser.getType().equals(userFromFile.getType()))){
			System.out.println("\033[30m\033[43m--- LOGIN SERVER ---\033[0m\033[31m  W A R N I N G : Il tipo di utente inviato non corrisponde al protocollo ad esso associato! Possibile hacking...\033[0m");
			return null;
		}
												
		if (checkUser.verifyId(userFromFile) && !(userFromFile.getSuspend())){
			if(userFromFile instanceof Stud){
				System.out.println("\033[30m\033[43m--- LOGIN SERVER ---\033[0m  Utente "+username+" autenticato. Tipo utente: \033[4mStudente\033[0m");
				control = 2;
			}
			else if(userFromFile instanceof Prof){
				System.out.println("\033[30m\033[43m--- LOGIN SERVER ---\033[0m  Utente "+username+" autenticato. Tipo utente: \033[4mProfessore\033[0m");
				control = 1;
			}
			else{
				System.out.println("\033[30m\033[43m--- LOGIN SERVER ---\033[0m\033[31m  A M M I N I S T R A T O R E autenticato\033[0m");
				control = 0;	
			}
		}
		else{
			System.out.println("\033[30m\033[43m--- LOGIN SERVER ---\033[0m\033[31m  W A R N I N G :  Autenticazione errata!\033[0m");
			return null;
		}
					
		//facciamo dei try per il mob?
		switch(control){
						
			case 0:
					mob = new MobileServerAdmin(stubs.getCentralDataJRMP(), stubs.getRegDataJRMP(), this);
					System.out.println("\033[30m\033[43m--- LOGIN SERVER ---\033[0m  Admin mobile server JRMP inviato");
					break;
					
			case 1:
					mob = new MobileAgentProf(stubs.getCentralDataJRMP(), username);
					System.out.println("\033[30m\033[43m--- LOGIN SERVER ---\033[0m  Professore mobile agent JRMP inviato");
					break;
					
			case 2:
					//vedere bene!
					Central obj = (Central)PortableRemoteObject.narrow(stubs.getCentralDataIIOP(), Central.class);
					mob = new MobileAgentStud(obj,username);
					System.out.println("\033[30m\033[43m--- LOGIN SERVER ---\033[0m  Studente mobile agent IIOP inviato");
					break;
					
		}

		System.out.println("\n\033[90m*** *** *** *** ***\033[0m");
		return mob;

	}

	/**
	 * @see Login#setMSRef(MobileServerItf)
	 */
	public void setMSRef(MobileServerItf stubMS) throws RemoteException {       
		System.out.println("\033[30m\033[43m--- LOGIN SERVER ---\033[0m  Registrata referenza al mobile server. Informazioni sull'host: "+stubMS.ping());
	}

	/**
	 * @see Unreferenced#unreferenced()
	 */
	public void unreferenced(){
	
		System.out.println("\n\033[90m***\033[0m\033[36m "+new Date()+" \033[0m\033[90m*** \033[0m  Invocazione metodo unreferenced() in corso...");
		
		try {
			//Activatable.unexportObject(this,true);
			Activatable.inactive(this.getID());
		} catch (RemoteException re) {
			re.printStackTrace();
		} catch (ActivationException ae) {
			ae.printStackTrace();
		}
	//cambia i colori e i nomi
	    System.out.println("\033[30m\033[43m--- LOGIN SERVER ---\033[0m  De-esportazione del server dal sistema RMI effettuata");
	    System.out.println("\033[30m\033[43m--- LOGIN SERVER ---\033[0m  Sistema RMID in attesa di nuove invocazioni");
	    System.out.println("\033[30m\033[43m--- LOGIN SERVER ---\033[0m  Lancio della gc() locale");
		//lancio garbage collector
		System.gc();
        System.out.println("\n\033[90m*** *** *** *** ***\033[0m");
		
	}
	
}
