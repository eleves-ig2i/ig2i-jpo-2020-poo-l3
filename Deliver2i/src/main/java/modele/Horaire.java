package modele;

import exception.HoraireException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class Horaire {

    private int heure;
    private int minutes;

   // private String

    public Horaire() {
        minutes = 0;
        heure = 0;
    }

    public Horaire(int minutes) throws HoraireException {
        this();
        this.heure = minutes/60;
        this.minutes = minutes%60;
    }



    public Horaire(int heure, int minutes) throws HoraireException {
        this();
        this.heure = heure;
        this.minutes = minutes;

        String s = toString();

        if (!(heure>=0 && heure<=23))
            throw new HoraireException(s);
        if (!(minutes>=0 && minutes<=59))
            throw new HoraireException(s);


    }

    public Horaire(String s) throws HoraireException{
        String[] tab = s.split(":");

        if (tab.length<2){
            throw new HoraireException(s);
        }

        try {
            this.heure = Integer.parseInt(tab[0]);
            this.minutes = Integer.parseInt(tab[1]);
        }catch (NumberFormatException e){
            throw new HoraireException(s);
        }

        if (!(heure>=0 && heure<=23))
            throw new HoraireException(s);
        if (!(minutes>=0 && minutes<=59))
            throw new HoraireException(s);
    }

    public int getMinutesCorrespondantes(){
        return 60*heure + minutes;
    }

    @Override
    public String toString() {
        return heure + ":"+minutes;
    }

    public static void main(String[] args) throws HoraireException {
//        Horaire h1 = new Horaire(24,60);
        Horaire h2 = new Horaire();
//        Horaire h3 = new Horaire(-12,-32);
        Horaire h4 = new Horaire(10,24);
//        System.out.println(h1);
        System.out.println(h2);
//        System.out.println(h3);
        System.out.println(h4);
    }
}