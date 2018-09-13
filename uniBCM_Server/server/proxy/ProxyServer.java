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

package server.proxy;

//ricordarsi di import mobile.* se esiste Mobile.java
import mobile.Mobile;
import server.login.*;
import server.reg.*;
import lib.*;

import java.io.*;
import java.rmi.*;

//import java.rmi.dgc.*;
import java.util.Date;
import java.net.URL;

/**
 * Implementazione di un server Proxy che implementa l'interfaccia Proxy
 * @author uniBCM team
 *
 */
public class ProxyServer implements Proxy{
	
	/** Protocollo sul quale salvare le referenze */
    private static final String saveProt="file://";
    /** Percorso nel quale salvare la referenza remota(stub) al Login server */
	private static final String loginRef=System.getProperty("user.dir")+"/.LoginRef";
	/** Percorso nel quale salvare la referenza remota(stub) al Registration Server*/
    private static final String regRef=System.getProperty("user.dir")+"/.RegRef";

    /**
     * Costruisce il server Proxy con gli stub ai server di Login e Registrazione passati 
     * come parametri.  Il costruttore salva le referenze su 2 file che verranno utilizzati all'occorenza.
     * Questo per permettere l'invocazione della dgc per i server attivabili.
     * @param lstub stub server di login
     * @param rstub stub server di registrazione
     * @throws RemoteException
     */
	public ProxyServer(Login lstub, Registration rstub) throws RemoteException{

		try{
			OutputStream out = new FileOutputStream(loginRef);
	        	ObjectOutputStream os = new ObjectOutputStream(out);
			os.writeObject(new MarshalledObject(lstub));
			os.close();
			out = new FileOutputStream(regRef);
			os = new ObjectOutputStream(out);
	        	os.writeObject(new MarshalledObject(rstub));
	        	os.close();
			}
			catch(IOException e){
				e.printStackTrace();
		}
	}
	
	/**
	 * @see Proxy#login(MarshalledObject)
	 */
	public MarshalledObject login(MarshalledObject user) throws RemoteException{

		Login loginStub = null;
		MarshalledObject result = null;
	
		try{
			InputStream in = new URL(saveProt+loginRef).openStream();
			ObjectInputStream oin = new ObjectInputStream(in);
        		loginStub= (Login)((MarshalledObject)oin.readObject()).get();
			oin.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			
			User userProxy = (User)user.get();
			
			System.out.println("\n\033[90m***\033[0m\033[36m "+new Date()+" \033[0m\033[90m*** \033[0m  Richiesta in entrata, login, da parte di "+userProxy.getId());

			System.out.println("\033[30m\033[42m--- PROXY SERVER ---\033[0m  Utente connesso presso il proxy: "+userProxy.getId());
			System.out.println("\033[30m\033[42m--- PROXY SERVER ---\033[0m  Invocazione metodo di login presso \033[30m\033[43m LOGIN SERVER \033[0m in corso...");
			
			Mobile mob = loginStub.login(userProxy);
			//vedere se mettere tutti gli if per instance of
			
			if(mob == null)
	
				System.out.println("\033[30m\033[42m--- PROXY SERVER ---\033[0m\033[33m  Autenticazione fallita da parte di "+userProxy.getId()+"!\033[0m");
			else
				System.out.println("\033[30m\033[42m--- PROXY SERVER ---\033[0m  Marshallizzazione dell'agente mobile da ritornare al client...");

			result = new MarshalledObject(mob);
			
		}catch(Exception e){
			if(e instanceof RemoteException){
				throw new RemoteException();
			}
			else
				e.printStackTrace();
		}
		
		System.out.println("\033[30m\033[42m--- PROXY SERVER ---\033[0m  Disconnessione in corso...");
		System.out.println("\n\033[90m*** *** *** *** ***\033[0m");
		
		return result;

	}
	
	/**
	 * @see Proxy#submitUser(MarshalledObject)
	 */
	public String submitUser(MarshalledObject user) throws RemoteException{
		
		Registration regStub = null;

		String result = null;

		try{
			InputStream in = new URL(saveProt+regRef).openStream();
			ObjectInputStream oin = new ObjectInputStream(in);
        		regStub= (Registration)((MarshalledObject)oin.readObject()).get();
			oin.close();
		}catch(Exception e){
			e.printStackTrace();
		}

		try{
			User userProxy = (User)user.get();
			
			System.out.println("\n\033[90m***\033[0m\033[36m "+new Date()+" \033[0m\033[90m*** \033[0m  Richiesta in entrata, submitUser, da parte di "+userProxy.getId());

			System.out.println("\033[30m\033[42m--- PROXY SERVER ---\033[0m  Tentativo di registrazione da parte di: "+userProxy.getId());
			System.out.println("\033[30m\033[42m--- PROXY SERVER ---\033[0m  Invocazione metodo di submitUser presso \033[30m\033[46m REGISTRATION SERVER \033[0m in corso...");
			
			try{
				result = regStub.submitUser(user);
			}catch(Exception e){
				System.out.println("\033[30m\033[42m--- PROXY SERVER ---\033[0m\033[33m  Tentativo di registrazione fallito per l'utente "+userProxy.getId()+"\033[0m\nDettagli: "+e);
				//da verificare!!
				e.printStackTrace();
				result = "Tentativo di registrazione fallito!";
			}
			
			}catch(Exception e){
				if(e instanceof RemoteException){
					throw new RemoteException();
				}
				else
					//mettiamo avvisi qui a livello proxy?
					e.printStackTrace();
			}
		
		System.out.println("\033[30m\033[42m--- PROXY SERVER ---\033[0m  Disconnessione in corso...");
		System.out.println("\n\033[90m*** *** *** *** ***\033[0m");
		return result;

	}

}
