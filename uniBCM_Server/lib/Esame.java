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

/**
 * Implemetazione di una classe Esame 
 * @author uniBCM Team
 *
 */
public class Esame implements Serializable {
	
	/**
	 * Descrittore della classe
	 */
	private static final long serialVersionUID = 1L;
	/** Nome dell'esame sostenuto e passatp */
	private String nomeEsame;
	/** voto con cui l'esame e' stato passato */
	private int voto;
	
	/**
	 * Costruisce un nuovo esame con nome null e voto 18
	 */
	public Esame(){
		this.nomeEsame = null;
		this.voto = 18;
	}
	
	/**
	 * Costruisce un nuovo esame con nome e voto passati come parametro
	 * @param nomeEsame nome dell'esame
	 * @param voto voto
	 * @throws IllegalArgumentException se voto e' < 18 o > 33
	 */
	public Esame(String nomeEsame, int voto) throws IllegalArgumentException{
		this.nomeEsame = nomeEsame;
		this.setVoto(voto);
	}
	
	/**
	 * Setta il nome dell'esame con la stringa passata come parametro
	 * @param nomeEsame stringa con cui settare il nome dell'esame
	 */
	public void setNomeEsame(String nomeEsame){
		this.nomeEsame = nomeEsame;
	}
	
	/**
	 * Setta il voto dell'esame con l'intero passato come parametro
	 * @param voto
	 * @throws IllegalArgumentException se voto < 18 oppure voto > 33
	 */
	public void setVoto(int voto)throws IllegalArgumentException{
		//controllare se >= 18
		if (voto < 18 || voto > 33) throw new IllegalArgumentException("Voto non valido");
		this.voto = voto;
	}
	
	/**
	 * Restituisce il nome dell'esame
	 * @return nome dell'esame
	 */
	public String getNomeEsame(){
		return this.nomeEsame;
	}
	
	/**
	 * Restituisce il voto dell'esame
	 * @return voto esame
	 */
	public int getVoto(){
		return this.voto;
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object o){
		Esame e = (Esame)o;
		return ( this.nomeEsame.equals(e.getNomeEsame()) );
	}
	
	/**
	 * @see Object#toString()
	 */
	public String toString(){
		return this.nomeEsame+":"+this.voto;
	}

}
