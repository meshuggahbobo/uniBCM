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

package lib;

/**
 * Descrive un tipo di dato User 
 * @author uniBCM team
 *
 */
public interface User {

		/**
		 * Setta il parametro name alla stringa passata come parametro
		 * @param name nome dell'utente
		 */
		public void setName(String name);

		/**
		 * Setta il parametro surname alla stringa passata come parametro
		 * @param surname cognome dell'utente
		 */
		public void setSurname(String surname);
		
		/**
		 * Setta il campo password dell'utente alla stringa passata come parametro.
		 * La password non può essere null oppure la stringa vuota.
		 * @param password password dell'utente
		 * @throws NullPointerException se la password è null
		 * @throws IllegalArgumentException se la password è la stringa vuota
		 */
		public void setPassword(String password) throws NullPointerException, IllegalArgumentException;
		
		/**
		 * Setta il parametro Id dell'user alla stringa passata come parametro.
		 * Id non può essere null oppure la stringa vuota
		 * @param id identificativo dell'utente
		 * @throws NullPointerException se id è null
		 * @throws IllegalArgumentException se id è la stringa vuota
		 */
		public void setId(String id) throws NullPointerException, IllegalArgumentException;
		
		/**
		 * Setta lo stato dell'utente a sospeso o abilitato
		 * @param suspended true se l'utente è sospeso, false altrimenti
		 */
		public void setSuspend(boolean suspended);
		
		/**
		 * Restituisce il nome dell'utente
		 * @return nome dell'utente
		 */
		public String getName();
		
		/**
		 * Restituisce il cognome dell'utente
		 * @return cognome dell'utente
		 */
		public String getSurname();
		
		/**
		 * Restituisce la password dell'utente
		 * @return password dell'utente
		 */
		public String getPassword();
		
		/**
		 * Restituisce l'identificativo dell'utente
		 * @return id dell'utente
		 */
		public String getId();
		
		/**
		 * Restituisce lo stato dell'utente
		 * @return true se l'utente è sospeso, false altrimenti
		 */
		public boolean getSuspend();

		/**
		 * Restituisce il tipo dell'utente
		 * @return tipo
		 */
        public String getType();

		/**
		 * Restituisce le informazioni relative all'utente.
		 * @return stringa con informazioni utente
		 */
		public String printUser();
		
		/**
		 * Verifica l'identità di uno User passato come parametro.
		 * Controlla se username(id) e password sono le stesse dell'oggetto this
		 * @param user Utente da controllare
		 * @return true se l'identità è stata confermata, false altrimenti
		 * @throws NullPointerException se user è null
		 */
		public boolean verifyId(User user) throws NullPointerException;
		
		/**
		 * Restituisce le informazioni complete relative all'utente
		 * @return stringa con info complete
		 */
		public String printFullInfo();

}
