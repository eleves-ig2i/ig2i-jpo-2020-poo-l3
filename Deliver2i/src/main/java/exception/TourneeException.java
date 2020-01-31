package exception;

import modele.Horaire;

public class TourneeException extends Exception{

    private Horaire dateDebut;
    private Horaire dateFin;

    public TourneeException(Horaire dateDebut, Horaire dateFin) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public String getMessageErreur() {
        return "ERREUR : La date de début est "+dateDebut + " et la date de fin est "+dateFin;
    }

    public Horaire getDateDebut() {
        return dateDebut;
    }

    public Horaire getDateFin() {
        return dateFin;
    }

    @Override
    public String toString() {
        return "TournéeException{" +
                "dateDebut : "  + dateDebut + " et dateFin : " + dateFin
                +'}';
    }
}
