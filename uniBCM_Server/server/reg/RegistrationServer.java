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

package server.reg;

import lib.*;

import java.io.*;
import java.rmi.*;
import java.rmi.activation.*;
import java.rmi.server.*;
import java.util.Date;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

/**
 * Implementazione di un server di registrazione attivabile che implementa le interafccie
 * Registration e Unreferenced ed estende Activatable
 * @see Activatable
 * @see Registration
 * @see Unreferenced
 * @author uniBCM team
 *
 */
public class RegistrationServer extends Activatable implements Registration, Unreferenced{
	
	/**
	 * Costruttore del server. Si esporta sulla porta 36000 su SSL
	 * @param id ActivationId
	 * @param data parametri di inizializzazione
	 * @see SslRMIClientSocketFactory
	 * @see SslRMIServerSocketFactory
	 * @throws ActivationException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public RegistrationServer(ActivationID id, MarshalledObject data) throws ActivationException, ClassNotFoundException, IOException{
		//come mai diverso dal costruttore del login server????
		super(id,36000, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory());
	}
	
	/**
	 * @see Registration#submitUser(MarshalledObject)
	 */
	public String submitUser(MarshalledObject data) throws RemoteException{
		
			User user=null;
            try{
            	user = (User)data.get();
    			System.out.println("\n\033[90m***\033[0m\033[36m "+new Date()+" \033[0m\033[90m*** \033[0m  Richiesta registrazione pendente utente "+user.getId());
            }catch(ClassNotFoundException cnfe){
				System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m\033[31m  W A R N I N G : Impossibile determinate il path per la classe User\033[0m");
            	return "Impossibile effettuare la richiesta di registrazione. Si prega di contattare l'Amministratore.";
            }catch(IOException ioe){
            	System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m\033[31m  W A R N I N G : Errore interno al sistema\033[0m");
            	return "Impossibile effettuare la richiesta di registrazione. Si prega di contattare l'Amministratore.";
            }
            
			String pendingUser = user.getId();	
			String pendingFile =  "pending/" + pendingUser + ".tmp";
			String check = "dbUni/" + pendingUser + ".usr";
			if((new File(check)).exists()){
				System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m\033[31m  W A R N I N G : Utente "+pendingUser+" gia' presente nel dbUni\033[0m");
				return "L'utente specificato e' gia' presente nel sistema.";
			}
				
			try{
				FileOutputStream f = new FileOutputStream (pendingFile);
	      		ObjectOutputStream out = new ObjectOutputStream(f);
				out.writeObject(user);
				out.close();
			}catch(IOException ioe){
				System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m\033[31m  W A R N I N G : Utente "+pendingUser+" gia' in attesa di registrazione\033[0m");
				return "L'utente specificato e' gia' in attesa di approvazione.";
			}

			System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m  Registrazione pendente salvata in "+pendingFile);
			System.out.println("\n\033[90m*** *** *** *** ***\033[0m");
			
			return "Richiesta di registrazione inviata con successo.";
	}
	
	/**
	 * @see Registration#confUser(String, boolean)
	 */
	public String confUser(String id, boolean accept) throws RemoteException{
		
		String pendingFile = "pending/"+id+".tmp";
		User userFromFile = null;
				
		if(accept){
			System.out.println("\n\033[90m***\033[0m\033[36m "+new Date()+" \033[0m\033[90m*** \033[0m  Richiesta conferma registrazione pendente utente "+id);
			try{
					FileInputStream fis = new FileInputStream(pendingFile);
               		ObjectInputStream in = new ObjectInputStream(fis);
                	userFromFile = (User)(in.readObject());
                	in.close();
				
				if(userFromFile instanceof Prof)
					((Prof)userFromFile).setCourses();
				if(userFromFile instanceof Stud)
					((Stud)userFromFile).setExams();
				
					FileOutputStream fto = new FileOutputStream (pendingFile);
		      		ObjectOutputStream out = new ObjectOutputStream(fto);
					out.writeObject(userFromFile);
					out.close();


				File f = new File(pendingFile);
				//se non funziona guarda quive! verifica se sparisce il tmp
				boolean success = f.renameTo(new File("dbUni/"+id+".usr"));
				if(!success){
					System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m\033[31m  W A R N I N G : Registrazione pendente non registrata correttamente: "+pendingFile+"\033[0m");
					return "Errore nella registrazione dell'utente "+id+".";
				}
				else{
					System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m  Registrazione pendente registrata nel sistema uniBCM con successo!");
					System.out.println("\n\033[90m*** *** *** *** ***\033[0m");
					return "Utente "+id+" registrato con successo.";
				}
			}catch(IOException ioe){
				System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m\033[31m  W A R N I N G : Errore interno al sistema\033[0m");
				ioe.printStackTrace();
				return "Impossibile registrare la richiesta pendente di "+id;
			}catch(ClassNotFoundException cnfe){
				System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m\033[31m  W A R N I N G : Errore durante il caricamento dell'utente da registrare\033[0m");
				cnfe.printStackTrace();
				return "Impossibile registrare la richiesta pendente di "+id;
			}
		}
		
		else{	//try catch?
			System.out.println("\n\033[90m***\033[0m\033[36m "+new Date()+" \033[0m\033[90m*** \033[0m  Richiesta eliminazione registrazione pendente utente "+id);
				File f = new File(pendingFile);
				boolean success = f.delete();
					if(!success){
						System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m\033[31m  W A R N I N G : Registrazione pendente non eliminata correttamente: "+pendingFile+"\033[0m");
						return "Errore nella eliminazione della richiesta di registrazione dell'utente "+id+".";
					}
					else{
						System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m  Registrazione pendente eliminata con successo!");
						System.out.println("\n\033[90m*** *** *** *** ***\033[0m");
						return "Richiesta dell'utente "+id+" eliminata.";
					}
		}
	}
	
	/**
	 * @see Registration#confAllUser()
	 */
	public String confAllUser() throws RemoteException{
		
		System.out.println("\n\033[90m***\033[0m\033[36m "+new Date()+" \033[0m\033[90m*** \033[0m  Richiesta conferma di registrazione di tutti gli utenti pendenti");

		//verificare se dir e' vuota
        User userFromFile = null;
        String result="";
        try{
            File db = new File("pending/");
            String files[] = db.list();
            for(int i=0; i<files.length;i++){
            	File f = new File("pending/"+files[i]);
                FileInputStream fis = new FileInputStream("pending/"+files[i]);
                ObjectInputStream in = new ObjectInputStream(fis);
                userFromFile = (User)(in.readObject());
                in.close();
                //boolean success = f.renameTo(new File("dbUni/"+userFromFile.getId()+".usr"));
		result += "\n"+confUser(userFromFile.getId(),true);
            }
        }catch(Exception e){
			System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m\033[31m  W A R N I N G : Errore durante la registrazione di tutte le richieste pendenti\033[0m");
            e.printStackTrace();
            //??????
            return ".";
        }

    System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m  Tutte le registrazioni pendenti sono state accettate presso il sistema uniBCM!");
	System.out.println("\n\033[90m*** *** *** *** ***\033[0m");
	return result;	
	}//chiudo confAllUser
	
	/**
	 * @see Registration#delUser(String)
	 */
	public String delUser(String id) throws RemoteException{
		
		String userFile = "dbUni/"+id+".usr";
		String result="";
		
		System.out.println("\n\033[90m***\033[0m\033[36m "+new Date()+" \033[0m\033[90m*** \033[0m  Richiesta eliminazione dell'utente "+id);

        if(id.equals("admin")){
			System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m\033[31m  W A R N I N G : Tentativo di eliminazione dell'utente Admin!\033[0m");
            return "Impossibile eliminare l'Amministratore  -_-' ";
        }
        
		try{
			File f;
			if( !((f = new File(userFile)).exists()) ){
				System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m\033[31m  W A R N I N G : Tentativo di eliminazione di un utente inesistente! Possibile hacking...\033[0m");
				result = "Utente inesistente!";
			}
			else {
				if(f.delete()){
				    System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m  Utente "+id+" eliminato con successo dal sistema uniBCM");
					System.out.println("\n\033[90m*** *** *** *** ***\033[0m");
					result = "Utente eliminato!";
				}
				else {
					System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m\033[31m  W A R N I N G : Errore durante la cancellazione dell'utente "+id+"\033[0m");
					result = "Impossibile eliminare l'utente!";
				}
			}
		}catch(Exception e){
			System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m\033[31m  W A R N I N G : Errore interno al sistema durante la cancellazione dell'utente "+id+"\033[0m");
			e.printStackTrace();
			return "Impossibile eliminare l'utente "+id;
		}
		
		return result;
	}
	
	/**
	 * @see Registration#delAllPendUser()
	 */
	public String delAllPendUser() throws RemoteException{
		
		System.out.println("\n\033[90m***\033[0m\033[36m "+new Date()+" \033[0m\033[90m*** \033[0m  Richiesta cancellazione di tutte le registrazione pendenti");

        //verificare se dir e' vuota
        User userFromFile = null;
        String result="";
        try{
            File db = new File("pending/");
            String files[] = db.list();
            for(int i=0; i<files.length;i++){
            	File f = new File("pending/"+files[i]);
                FileInputStream fis = new FileInputStream("pending/"+files[i]);
                ObjectInputStream in = new ObjectInputStream(fis);
                userFromFile = (User)(in.readObject());
                in.close();
                boolean success = f.delete();
                if(!success){
					System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m\033[31m  W A R N I N G : Registrazione pendente non eliminata "+userFromFile.getId()+"\033[0m");
					result += "\nErrore nella eliminazione dell'utente pendente "+userFromFile.getId()+".";
				}
				else{
				    System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m  Registrazione pendente eliminata con successo: "+userFromFile.getId());
					result += "\nUtente pendente "+userFromFile.getId()+" eliminato con successo.";
				}
            }
        }catch(Exception e){
			System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m\033[31m  W A R N I N G : Impossibile eliminare tutte le richieste pendenti\033[0m");
            e.printStackTrace();
            return "";
        }
        System.out.println("\n\033[90m*** *** *** *** ***\033[0m");
        return result;	
		
	}//chiudo delAllPendUser
	
	/**
	 * @see Registration#userList()
	 */
	public User[] userList() throws RemoteException, ClassNotFoundException, IOException{
		
		System.out.println("\n\033[90m***\033[0m\033[36m "+new Date()+" \033[0m\033[90m*** \033[0m  Richiesta lista completa di tutti gli utenti registrati");

		//se non funzia guarda qui
			User[] result= null;
			User userFromFile = null;
            File db = new File("dbUni/");
            String files[] = db.list();
            result = new User[files.length];
            for(int i=0; i<result.length;i++){
               
                    FileInputStream f = new FileInputStream("dbUni/"+files[i]);
                    ObjectInputStream in = new ObjectInputStream(f);
                    userFromFile = (User)(in.readObject());
                    in.close();
                    result[i] = userFromFile;
            }
       
		    System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m  Lista completa utenti del sistema uniBCM recuperata con successo");
            System.out.println("\n\033[90m*** *** *** *** ***\033[0m");
            return result;
	}

	/**
	 * @see RegistrationServer#pendingUserList()
	 */
    public User[] pendingUserList() throws IOException, ClassNotFoundException, RemoteException{
        
		System.out.println("\n\033[90m***\033[0m\033[36m "+new Date()+" \033[0m\033[90m*** \033[0m  Richiesta lista completa di tutte le richieste pendenti");

        //verificare se dir e' vuota
        User[] result = null;
        User userFromFile = null;

            File db = new File("pending/");
            String files[] = db.list();
            result = new User[files.length];
            for(int i=0; i<files.length;i++){
                FileInputStream f = new FileInputStream("pending/"+files[i]);
                ObjectInputStream in = new ObjectInputStream(f);
                userFromFile = (User)(in.readObject());
                in.close();
                result[i] = userFromFile;
            }
       
		    System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m  Lista completa delle richieste pendenti recuperata con successo");
            System.out.println("\n\033[90m*** *** *** *** ***\033[0m");
            return result;
    }
    
    /**
     * @see Registration#setUserState(String, boolean)
     */
    public String setUserState (String id, boolean suspend) throws RemoteException{
    	
		System.out.println("\n\033[90m***\033[0m\033[36m "+new Date()+" \033[0m\033[90m*** \033[0m  Richiesta di modifica stato dell'utente "+ id);

    	String userFile = "dbUni/"+id+".usr";
		String result="";

        if(id.equals("admin")){
			System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m\033[31m  W A R N I N G : Tentativo di modifica dello stato di attivazione dell'utente Admin!\033[0m");
			return "Impossibile cambiare lo stato di attivazione dell'Amministratore O_o";
        }

		try{
			File f;
			if( !((f = new File(userFile)).exists()) ){
				System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m\033[31m  W A R N I N G : Tentativo di modifica dello stato di un utente inesistente. Possibile hacking...\033[0m");
				return "L'utente "+id+" non esiste.";
			}
			else {
				FileInputStream fis = new FileInputStream (userFile);
				ObjectInputStream in = new ObjectInputStream(fis);
				//controllare se il cast ritorna sottotipo!!!! --> sembra ok :P
				User userFromFile = (User)(in.readObject());
				in.close();
				userFromFile.setSuspend(suspend);
				//salvo l'oggetto su disco
				FileOutputStream fos = new FileOutputStream (userFile);
	      		ObjectOutputStream out = new ObjectOutputStream(fos);
				out.writeObject(userFromFile);
				out.close();
				}
		}catch(Exception e){
			System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m\033[31m  W A R N I N G : Impossibile modificare lo stato dell'utente "+id+"\033[0m");
			e.printStackTrace();
			return "Impossibile settare lo stato dell'utente "+id;
		}
		
		if(suspend){
		    System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m  Utente "+ id+" disabilitato con successo!");
			result = "Utente "+id+" disabilitato!";
		}
		else{
		    System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m  Utente "+ id+" abilitato con successo!");
			result = "Utente "+id+" abilitato!";
		}
		
        System.out.println("\n\033[90m*** *** *** *** ***\033[0m");
		return result;
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

	    System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m  De-esportazione del server dal sistema RMI effettuata");
	    System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m  Sistema RMID in attesa di nuove invocazioni");
	    System.out.println("\033[30m\033[46m--- REGISTRATION SERVER ---\033[0m  Lancio della gc() locale");
		//lancio garbage collector
		System.gc();
        System.out.println("\n\033[90m*** *** *** *** ***\033[0m");
		
	}
	
}
