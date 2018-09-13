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

package server.central;

import java.io.IOException;
import java.lang.ClassNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import lib.*;

/**
 * Definisce i metodi fondamentali per un server Central
 * @author uniBCM team
 *
 */
public interface Central extends Remote {
    
	/**
	 * Ottiene le informazioni dell'utente user
	 * @param user utente
	 * @return Stringa di informazioni
	 * @throws RemoteException
	 */
	public String getInfo(String user) throws RemoteException;
	
	/**
	 * Aggiunge un esame ad un utente.
	 * Utente, nome dell'esame e voto vengono passati come parametri.
	 * -ATTENZIONE-l'utente deve essere uno studente
	 * @param user utente
	 * @param examName nome dell'esame
	 * @param value voto in trentesimi
	 * @return stringa di conferma
	 * @throws RemoteException
	 */
	public String addExam(String user, String examName, int value) throws RemoteException;

	/**
	 * Aggiunge un corso ad un utente
	 * Utente, nome del corso e anno vengono passati come parametri
	 * -ATTENZIONE-l'utente deve essere un professore
	 * @param user utente
	 * @param courseName nome del corso
	 * @param year anno accademico
	 * @return Stringa di conferma
	 * @throws RemoteException
	 */
	public String addCourse(String user, String courseName, String year) throws RemoteException;
	
	/**
	 * Restituisce la lista di corsi tenuti dal docente user nei vari anni accademici
	 * @param user utente professore
	 * @return array di corsi
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws RemoteException
	 */
	public Corso[] getCoursesList(String user) throws ClassNotFoundException, IOException, RemoteException;

	/**
	 * Restituisce la lista degli esami sostenuti dallo studente user
	 * @param user utente studente
	 * @return array di Esami
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws RemoteException
	 */
    public Esame[] getExamsList(String user) throws ClassNotFoundException, IOException, RemoteException;

    /**
     * Aggiunge la referenza ad un utente attualmente loggato.
     * Questo server per tenere traccia degli utente attualmente loggati
     * Viene invocato quando un client si connette al sistema
     * @param ref referenza client
     * @throws RemoteException
     */
	public void addRef(Reference ref) throws RemoteException;

	/**
	 * Rimuove la referenza ad un utente attualmente loggato.
     * Questo server per tenere traccia degli utente attualmente loggati
     * Viene invocato quando un client si esce dal sistema o quando il client non e' piu'
     * raggiungibile(per eventuali crash di sistema)
	 * @param ref referenza client
	 * @throws RemoteException
	 */
	public void removeRef(Reference ref) throws RemoteException;
	
	/**
	 * Ottiene la referenza all'ultimo client loggato
	 * @return Ip dell'ultimo client loggato
	 * @throws RemoteException
	 */
	public String getLastRef() throws RemoteException;

	/**
	 * Restituisce il numero di client attualmente loggati al sistema uniBCM
	 * @return intero rappresentante il numero di client loggati
	 * @throws RemoteException
	 */
	public int getReferenceCount() throws RemoteException;

}
