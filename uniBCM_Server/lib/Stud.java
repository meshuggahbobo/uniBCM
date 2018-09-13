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

import java.util.Vector;

/**
 * Implementa un tipo di user Studente
 * @author uniBCM team
 *
 */
public class Stud extends UserImpl{
	
	/**
	 * Descrittore della classe
	 */
	private static final long serialVersionUID = 1L;
	/** Lista di esami sostenuti e passati dall'utente */
	private List exams;

	/**
	 * Restituisce la lista di esami passati dallo studente
	 * @return stringa di esami dello studente
	 */
	public Esame[] getExams(){
		
		Vector<Object> tmp = exams.copyToVector();
        Esame[] result = (Esame[])tmp.toArray(new Esame[tmp.size()]);
        return result;
        
	}
	
	/**
	 * Costruisce una nuova lista di esami sostenuti dallo studente
	 */
	public void setExams(){
		exams = new List();
	}

	/**
	 * @see User#getType()
	 */
    public String getType(){
        return "Studente";
    }

    /**
     * @see User#printFullInfo()
     */
	public String printFullInfo(){
		return printUser() + "\n" + getExams();
	}
	
	/**
	 * Aggiunge un nuovo esame alla carriera dello studente
	 * @param e nuovo esame
	 */
	public void addExam(Esame e){
		this.exams.add(e);
	}
	
}
