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

import java.io.Serializable;
import java.util.Vector;

/**
 * Implementazione di una lista che non puo' contenere due oggetti uguali
 * @author uniBCM Team
 *
 */
public class List implements Serializable{
	
	/**
	 * Descrittore della classe
	 */
	private static final long serialVersionUID = 1L;
	/** Riferimento alla testa della lista */
	private Node head;
	/** Contatore del numero di elementi nella lista */
	private int nOggetti;
	
	/**
	 * Implementa un nodo della lista
	 * @author BCM
	 *
	 */
	public class Node implements Serializable{
		
		/**
		 * Descrittore della classe
		 */
		private static final long serialVersionUID = 1L;
		/** Riferimento all'oggetto contenuto nel nodo */
		Object key;
		/** Riferimento al sucessore del nodo */
		Node next;
		
		/**
		 * Costruisce un nuovo nodo con chiave key e successore a null
		 * @param key chiave del nodo
		 */
		public Node(Object key){
			this.key = key;
			this.next = null;
		}
		
		/**
		 * @see Object#equals(Object)
		 */
		public boolean equals(Object o){
			Node n = (Node)o;
			return this.key.equals(n.key);
		}
		
		/**
		 * @see Object#toString()
		 */
		public String toString(){
			return key.toString();
		}
	}
	
	/**
	 * Costruisce una nuova lista di 0 elementi che il campo head a null
	 */
	public List(){
		this.head = null;
		this.nOggetti = 0;
	}
	
	/**
	 * Inserisce un nuovo oggetto nella lista. Solleva un eccezione se l'oggetto e' null
	 * oppure se e' gia presente nella lista
	 * @param nuovo oggetto da aggiungere
	 * @throws NullPointerException se nuovo e' null
	 * @throws IllegalArgumentException se nuovo e' gia presente nella lista
	 */
	public void add(Object nuovo)throws NullPointerException,IllegalArgumentException{
		System.out.println("Nuovo nodo"+ nuovo);
		if (nuovo == null)	throw new NullPointerException("nuovo null");
		Node n = new Node(nuovo);
		if( nOggetti == 0 ){
			this.head = n;
		}else
			if(! check(n) ){
				//nodo non presente
				n.next = this.head;
				this.head = n;
			}else
				throw new IllegalArgumentException("Oggetto gia presente nella lista");
		nOggetti++;
	}
	
	/**
	 * Rimuove un oggetto dalla lista. Solleva un eccezione nel caso l'oggetto non sia presente
	 * @param daRimuovere oggetto da rimuovere dalla lista
	 * @throws IllegalArgumentException se l'oggetto non e' presente nella lista
	 * @throws NullPointerException s l'oggetto e' null
	 */
	public void remove(Object daRimuovere) throws IllegalArgumentException, NullPointerException{
		if ( daRimuovere == null)	throw new NullPointerException("daRimuovere null");
		if ( nOggetti ==0 )	throw new IllegalArgumentException("Lista vuota");
		Node rm = new Node(daRimuovere);
		Node tmp = this.head;
		boolean trovato = false;
		if ( nOggetti == 1 ){
			if(tmp.equals(rm)){
				trovato=true;
				this.head = null;
			}
			else throw new IllegalArgumentException("oggetto non presente nella lista");
		}else{
			Node tmpsucc = tmp.next;
			if(tmp.equals(rm)){
				this.head = tmpsucc;
				trovato=true;
			}
			while( tmpsucc != null && trovato == false){
				if( tmpsucc.equals(rm) ){
					trovato = true;
					tmp.next = tmpsucc.next;
				}
				tmp = tmpsucc;
				tmpsucc = tmpsucc.next;
			}
		}
		if (trovato != true )
			throw new IllegalArgumentException("Oggetto non presente nella lista");
		nOggetti--;
	}
	
	/**
	 * Restituisce tutti gli oggetti della lista
	 * @return stringa di tutti gli oggetti della lista
	 */
	public String printItems(){
		Node tmp = this.head;
		String result="";
		while( tmp != null ){
			result+=tmp.toString()+" - ";
			tmp = tmp.next;
		}
		return result;
	}
	
	/**
	 * Routine privata che controlla se un elemento e' presente nella lista
	 * @param n nodo da controllare
	 * @return true se il nodo e' presente, false altrimenti
	 */
	private boolean check(Node n){
		if (nOggetti == 0 )
			return false;
		Node tmp = this.head;
		while( tmp != null){
			if(tmp.equals(n))
				return true;
			tmp = tmp.next;
		}
		return false;
	}
	
	/**
	 * Copia tutti gli elementi della lista corrente in un Vector
	 * @return il Vector risultante
	 * @see java.util.Vector
	 */
	public Vector<Object> copyToVector(){
		Vector<Object> array = new Vector<Object>();
		Node myHead = this.head;
		while(myHead != null){
            array.add(myHead.key);
			myHead = myHead.next;
		}
		return array;
	}

}
