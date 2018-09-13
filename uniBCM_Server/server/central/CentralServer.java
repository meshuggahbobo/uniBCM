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

package server.central;

import java.io.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.rmi.*;
import java.util.*;

import java.net.InetAddress; 

import lib.Reference;
import lib.*;

/**
 * Implementazione di un server centrale che implementa l'interfaccia Central
 * @author uniBCM team
 *
 */
public class CentralServer implements Central {

	/** Vettore di referenze ai client loggati */
	private Vector refs;
	/** Ultimo client loggato */
	private String lastUser;

	/**
	 * Costruisce un nuovo server centrale
	 * @throws RemoteException
	 */
	public CentralServer() throws RemoteException {
		
		this.refs = new Vector();
		java.util.Timer timer = new java.util.Timer();
		TimerTask task = new CountingReference();
		timer.schedule(task, 15000, 5000);
		this.lastUser = "";
		
	}
    	
	/**
	 * @see Central#getInfo(String)
	 */
	public String getInfo(String user) throws RemoteException {
		
		System.out.println("\n\033[90m***\033[0m\033[36m "+new Date()+" \033[0m\033[90m*** \033[0m  Richiesta informazioni utente "+user);

		//String fileName = new String();
		String fileName = "dbUni/"+user+".usr";
		//rimpiazzare con null per risp mem
		User userFromFile = null;
		boolean exists = (new File(fileName)).exists();
		
		if (exists) {
			try {
				FileInputStream f = new FileInputStream (fileName);
				ObjectInputStream in = new ObjectInputStream(f);
				userFromFile = (User)(in.readObject());
				in.close();
			} catch (Exception e) {
				System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m\033[31m  W A R N I N G : Recupero dati utente "+user+"fallito!\033[0m");
				e.printStackTrace();
				return "Impossibile restituire le informazioni per l'utente "+user;
			}
		}
		
		else{
				//fare timestamp per i warning
				System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m\033[31m  W A R N I N G : Tentativo recupero dati utente inesistente! Possibile hacking...\033[0m");
				return "L'utente "+user+" non esiste.";
			}
				
		
			System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m  Utente "+user+" estratto da dbUni.");
			
			String infos = new String();		
			infos = userFromFile.printFullInfo();
			System.out.println("\n\033[90m*** *** *** *** ***\033[0m");
			return infos;
	}
	
	/**
	 * @see Central#addExam(String, String, int)
	 */
	public String addExam(String user, String examName, int value) throws RemoteException{
		
		System.out.println("\n\033[90m***\033[0m\033[36m "+new Date()+" \033[0m\033[90m*** \033[0m  Richiesta registrazione esame "+examName+" per l'utente "+user);

		Esame myExam = new Esame(examName, value);
		
		String fileName = new String();
		fileName = "dbUni/"+user+".usr";
		//rimpiazzare con null per risp mem
		User userFromFile = null;
		boolean exists = (new File(fileName)).exists();
		
		if (exists) {
			try {
				FileInputStream f = new FileInputStream (fileName);
				ObjectInputStream in = new ObjectInputStream(f);
				userFromFile = (User)(in.readObject());
				in.close();
			} catch (Exception e) {
				System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m\033[31m  W A R N I N G : Recupero dati utente "+user+"fallito!\033[0m");
				e.printStackTrace();
				return "Impossibile aggiungere l'esame "+examName+" all'utente "+user;
			}
		}
		
		else {
			System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m\033[31m  W A R N I N G : Tentativo di aggiunta esame ad un utente inesistente! Possibile hacking...!\033[0m");
			return "L'utente "+user+" non esiste.";
		}
			
		System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m  Utente "+user+" estratto da dbUni.");
		
		if(userFromFile instanceof Stud){
			try{
				((Stud)userFromFile).addExam(myExam);
			}catch(Exception e){
				System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m  Tentativo bloccato inserimento esame "+examName+" all'utente "+user+" in quanto gia' presente in carriera.");
				e.printStackTrace();
				return "L'esame e' gia' presente nella carriera dello studente "+user;
			}
			
			try{
				FileOutputStream fos = new FileOutputStream (fileName);
	      		ObjectOutputStream out = new ObjectOutputStream(fos);
				out.writeObject(userFromFile);
				out.close();
				System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m  Esame "+examName+" aggiunto con successo alla carriera dell'utente "+user+"!");
			} catch(IOException ioe){
				System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m\033[31m  W A R N I N G : Impossibile aggiungere l'esame "+examName+" all'utente "+user+"!\033[0m");
				ioe.printStackTrace();
				return "Impossibile aggiungere l'esame "+examName+" all'utente "+user;
			}
			
			System.out.println("\n\033[90m*** *** *** *** ***\033[0m");
			return "L'Esame "+examName+" e' stato aggiunto alla carriera dello studente " +user;
		}
		else
		{
			System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m\033[31m  W A R N I N G : Tentativo bloccato inserimento esame ad un utente che non e' studente! Possibile hacking...\033[0m");
			System.out.println("\n\033[90m*** *** *** *** ***\033[0m");
			return "Non puoi aggiungere un esame ad un utente che non e' studente!";	
		}
	}
	
	/**
	 * @see Central#addCourse(String, String, String)
	 */
	public String addCourse(String user, String courseName, String year) throws RemoteException{
		
		System.out.println("\n\033[90m***\033[0m\033[36m "+new Date()+" \033[0m\033[90m*** \033[0m  Richiesta aggiunta corso "+courseName+" per l'utente "+user);
		
		Corso myCourse = new Corso(courseName,year);
		
		String fileName = new String();
		fileName = "dbUni/"+user+".usr";
		//rimpiazzare con null per risp mem
		User userFromFile = null;
		boolean exists = (new File(fileName)).exists();
		
		if (exists) {
			try {
				FileInputStream f = new FileInputStream (fileName);
				ObjectInputStream in = new ObjectInputStream(f);
				userFromFile = (User)(in.readObject());
				in.close();
			} catch (Exception e) {
				System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m\033[31m  W A R N I N G : Recupero dati utente "+user+"fallito!\033[0m");
				e.printStackTrace();
				return "Impossibile aggiungere il corso "+courseName+" all'utente "+user;
			}
		}
		
		else {
			System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m\033[31m  W A R N I N G : Tentativo di aggiunta corso ad un utente inesistente! Possibile hacking...!\033[0m");
			return "L'utente "+user+" non esiste.";
		}
		
		System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m  Utente "+user+" estratto da dbUni.");

		if(userFromFile instanceof Prof){
			try{
				((Prof)userFromFile).addCourseProf(myCourse);
			}catch(Exception e){
				System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m  Tentativo bloccato inserimento corso "+courseName+" all'utente "+user+" in quanto gia' presente tra i suoi ingegnamenti.");
				e.printStackTrace();
				return "Il corso e' gia' presente tra gli insegnamenti del docente "+user;
			}
			
			try{
				FileOutputStream fos = new FileOutputStream (fileName);
	      		ObjectOutputStream out = new ObjectOutputStream(fos);
				out.writeObject(userFromFile);
				out.close();
				System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m  Corso "+courseName+" aggiunto con successo all'utente "+user+"!");
			} catch(IOException ioe){
				System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m\033[31m  W A R N I N G : Impossibile aggiungere il corso "+courseName+" all'utente "+user+"!\033[0m");
				ioe.printStackTrace();
				return "Impossibile aggiungere il corso "+courseName+" all'utente "+user;
			}
			
			System.out.println("\n\033[90m*** *** *** *** ***\033[0m");
			return "Il corso "+courseName+" e' stato aggiunto al professore " +user;
		}
		else{
			System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m\033[31m  W A R N I N G : Tentativo bloccato inserimento corso ad un utente che non e' professore! Possibile hacking...\033[0m");
			System.out.println("\n\033[90m*** *** *** *** ***\033[0m");
			return "Non puoi aggiungere un corso ad un utente che non e' professore!";
		}
	}
	
	/**
	 * @see Central#getCoursesList(String)
	 */
	public Corso[] getCoursesList(String user) throws ClassNotFoundException, IOException, RemoteException{
		
		System.out.println("\n\033[90m***\033[0m\033[36m "+new Date()+" \033[0m\033[90m*** \033[0m  Richiesta lista dei corsi per l'utente "+user);

		String fileName = new String();
		fileName = "dbUni/"+user+".usr";
		//rimpiazzare con null per risp mem
		User userFromFile = null;
		boolean exists = (new File(fileName)).exists();
		
		if (exists) {
			try{
				FileInputStream f = new FileInputStream (fileName);
				ObjectInputStream in = new ObjectInputStream(f);
				userFromFile = (User)(in.readObject());
				in.close();
			}catch(Exception e){
				System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m\033[31m  W A R N I N G : Recupero dati utente "+user+"fallito!\033[0m");
				e.printStackTrace();
				return null;
			}

		}
		else {
			System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m\033[31m  W A R N I N G : Tentativo richiesta lista dei corsi di un utente inesistente! Possibile hacking...!\033[0m");
			return null;
		}
			
		System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m  Utente "+user+" estratto da dbUni.");

		if(userFromFile instanceof Prof){
			System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m  Lista dei corsi per l'utente "+user+" recuperata con successo!");
			System.out.println("\n\033[90m*** *** *** *** ***\033[0m");
			return ((Prof)userFromFile).getCourses();
		}
		else{
			System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m\033[31m  W A R N I N G : Tentativo bloccato richiesta lista dei corsi di un utente che non e' professore! Possibile hacking...\033[0m");
			System.out.println("\n\033[90m*** *** *** *** ***\033[0m");
			return null;
		}
	}
	
	/**
	 * @see Central#getExamsList(String)
	 */
    public Esame[] getExamsList(String user) throws ClassNotFoundException, IOException, RemoteException{

		System.out.println("\n\033[90m***\033[0m\033[36m "+new Date()+" \033[0m\033[90m*** \033[0m  Richiesta lista degli esami per l'utente "+user);
    	
        String fileName = new String();
		fileName = "dbUni/"+user+".usr";
		//rimpiazzare con null per risp mem
		User userFromFile = null;
		boolean exists = (new File(fileName)).exists();

		if (exists) {
			try{
				FileInputStream f = new FileInputStream (fileName);
				ObjectInputStream in = new ObjectInputStream(f);
				userFromFile = (User)(in.readObject());
				in.close();
			}catch(Exception e){
				System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m\033[31m  W A R N I N G : Recupero dati utente "+user+"fallito!\033[0m");
				e.printStackTrace();
				return null;
			}

		}
		else {
			System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m\033[31m  W A R N I N G : Tentativo recupero lista esami di un utente inesistente! Possibile hacking...!\033[0m");
			return null;
		}

		System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m  Utente "+user+" estratto da dbUni.");

			if(userFromFile instanceof Stud){
				System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m  Lista degli esami per l'utente "+user+" recuperata con successo!");
				System.out.println("\n\033[90m*** *** *** *** ***\033[0m");
				return ((Stud)userFromFile).getExams();
			}
			else{
				System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m\033[31m  W A R N I N G : Tentativo bloccato richiesta lista degli esami di un utente che non e' studente! Possibile hacking...\033[0m");
				System.out.println("\n\033[90m*** *** *** *** ***\033[0m");
				return null;
			}
    }
    
    /**
     * @see Central#addRef(Reference)
     */
	public void addRef(Reference ref) throws RemoteException{
		//facciamo try e catch???
		refs.add(ref);
		System.out.println("\n\033[90m***\033[0m\033[36m "+new Date()+" \033[0m\033[90m*** \033[0m \033[32mConnessione\033[0m da parte dell'utente\033[0m\033[35m "+ref.getUser()+"\033[0m.");
		System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m  IP\033[35m : "+ref.getHost()+"\033[0m");
		System.out.println("\n\033[90m*** *** *** *** ***\033[0m");
		lastUser = "Ultimo utente connesso: --- "+ref.getDate()+" --- "+ref.getUser()+" "+ref.getHost();
		//System.out.println(refs.size());
	}

	/**
	 * @see Central#removeRef(Reference)
	 */
	public void removeRef(Reference ref) throws RemoteException{
		refs.remove(ref);
		System.out.println("\n\033[90m***\033[0m\033[36m "+new Date()+" \033[0m\033[90m*** \033[0m  \033[31mDisconnessione\033[0m da parte dell'utente\033[0m\033[35m "+ref.getUser()+"\033[0m.");
		System.out.println("\033[37m\033[44m--- CENTRAL SERVER ---\033[0m  IP\033[35m : "+ref.getHost()+"\033[0m");
		System.out.println("\n\033[90m*** *** *** *** ***\033[0m");
	}
	
	/**
	 * @see Central#getLastRef()
	 */
	public String getLastRef() throws RemoteException{
		return this.lastUser;
	}

	/**
	 * @see Central#getReferenceCount()
	 */
	public int getReferenceCount() throws RemoteException{
		return this.refs.size();
	}

	/**
	 * Restituisce il vettore delle referenze
	 * @return
	 */
	public Vector getReferences(){
		return refs;
	}

	/**
	 * Task che periodicamente controlla quante refereze ci sono al server centrales
	 * @author uniBCM team
	 *
	 */
	private class CountingReference extends TimerTask{
		public void run(){
			for(int i=0;i<refs.size();i++){
				Reference ref = (Reference)refs.get(i);
				int timeout = 3000;
				String host = ref.getHost();
				try{
					if(!InetAddress.getByName(host).isReachable(timeout))
						removeRef(ref);
				}catch(Exception e){
					e.printStackTrace();
				}	
					
			}
			//stamapare data
			System.out.println("\n\033[90m***\033[0m\033[36m "+new Date()+" \033[0m\033[90m*** \033[0m Numero di utenti attualmente connessi: \033[32m"+refs.size()+"\033[0m");
		}
	}
	
}
