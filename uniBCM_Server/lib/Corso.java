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
 * Implementazione di un corso
 * @author uniBCM Team
 *
 */
public class Corso implements Serializable {

	/**
	 * Descrittore della classe
	 */
	private static final long serialVersionUID = 1L;
	/** Nome del corso */
	private String nomeCorso;
	/** Anno accademico in cui il corso e' stato svolto */
	private String annoAccademico;
	
	/**
	 * Costruisce un nuoco corso con nome null e anno di default 2008/2009
	 */
	public Corso(){
		this.nomeCorso = null;
		this.annoAccademico = "2008/2009";
	}
	
	/**
	 * Costruisce un nuovo corso con nome e anno passati come parametro
	 * @param nomeCorso nome del nuovo corso da costruire
	 * @param annoAccademico anno del nuovo corso da costruire
	 */
	public Corso(String nomeCorso, String annoAccademico){
		this.nomeCorso = nomeCorso;
		this.annoAccademico = annoAccademico;
	}
	
	/**
	 * Setta il nome del corso con la stringa passata come parametro
	 * @param nomeCorso stringa con cui settare il nome del corso
	 */
	public void setNomeCorso(String nomeCorso){
		this.nomeCorso = nomeCorso;
	}
	
	/**
	 * Setta l'anno accademico in cui si e' svolto il corso con lastringa passata come parametro
	 * @param annoAccademico stringa con cui settare l'anno accademico
	 */
	public void setAnnoAccademico(String annoAccademico){
		this.annoAccademico = annoAccademico;
	}
	
	/**
	 * Restituisce il nome del corso
	 * @return nome del corso
	 */
	public String getNomeCorso(){
		return this.nomeCorso;
	}
	
	/**
	 * Restituisce l'anno accademico in cui il corso e' stato svolto
	 * @return anno accademico
	 */
	public String getAnnoAccademico(){
		return this.annoAccademico;
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object o){
		Corso c = (Corso)o;
		return ( this.nomeCorso.equals(c.getNomeCorso())
								&&
				 this.annoAccademico.equals(c.getAnnoAccademico()) );
	}
	
	/**
	 * @see Object#toString()
	 */
	public String toString(){
		return this.nomeCorso+":"+this.annoAccademico;
	}

}
