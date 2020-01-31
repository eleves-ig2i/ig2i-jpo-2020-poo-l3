package modele;

import exception.HoraireException;
import exception.TourneeException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Entity
public class Shift implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ShiftID")
    private int id;

    @Transient
    private int dureeMin;

    @Transient
    private int dureeMax;

    @OneToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private List<Tournee> tourneeList;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name = "SolutionID")
    private Solution solution;

    public Shift(){
        dureeMin = 0;
        dureeMax = 0;
        tourneeList = new ArrayList<>(); //ArrayList car les tournées seront dans un certain ordre
    }

    public Shift (int dureeMin, int dureeMax) {
        this();
        if (dureeMin > 0 && dureeMax > dureeMin)
        {
            this.dureeMin = dureeMin;
            this.dureeMax = dureeMax;
        }
    }

    public Shift (Shift s) {
        this();
        dureeMin = s.getDureeMin();
        dureeMax = s.getDureeMax();
        tourneeList.addAll(s.tourneeList);
    }

    /**
     * Ajoute une tournée si et seulement si on peut l'ajouter (si ça ne chevauche pas une autre tournée, ...)
     * @return true si ajout, false sinon
     */
    public boolean addTournee(Tournee tournee){
        if (tournee==null)
            return false;
        if (!estTourneeValide(tournee))
            return false;
        if (tourneeList.add(tournee)){
            tournee.setShift(this);
            return true;
        }
        return false;
    }

    public boolean removeTournee(Tournee tournee){
        if (tournee==null)
            return false;
        if (tourneeList.remove(tournee)){
            tournee.setShift(null);
            return true;
        }
        return false;
    }

    /**
     * @return false si les dates début et de fin ne coordonnent pas avec les tournées déjà présente dans le shift ou si le shift devient trop long
     * @param tournee
     */
    public boolean estTourneeValide(Tournee tournee) {
        int max_duree_fin = tournee.getDateFin().getMinutesCorrespondantes();
        int min_duree_debut = tournee.getDateDebut().getMinutesCorrespondantes();

        for (Tournee t:tourneeList) {

            if (t.getDateDebut().getMinutesCorrespondantes()<min_duree_debut)
                min_duree_debut = t.getDateDebut().getMinutesCorrespondantes();

            if (t.getDateFin().getMinutesCorrespondantes()>max_duree_fin)
                max_duree_fin = t.getDateFin().getMinutesCorrespondantes();

            if (t.getDateDebut().getMinutesCorrespondantes() < tournee.getDateFin().getMinutesCorrespondantes()) { //Si la tournée 1 finit après le début de la tournée 2
                if (t.getDateFin().getMinutesCorrespondantes() > tournee.getDateDebut().getMinutesCorrespondantes()){ //et que la tournée 2 finit après le début de la tournée 1
                    return false;
                }
            }
        }

        if (max_duree_fin-min_duree_debut>dureeMax)
            return false;

        return true;
    }

    /**
     * Return le gain de temps mort que l'on fait si on ajoute la tournee au shift
     * Elle sera supérieure à 0 si la tournee augmente le temps mort
     * et inférieure à 0 si elle diminue le temps mort
     * si return Integer.MAX_VALUE c'est que on ne peut pas ajouter la tournee
     */
    public int combienDeTempsMortsSiAjout(Tournee t){
        int temps_mort_before = calculeTempsMort();
        int temps_mort_after;
        if (estTourneeValide(t)){
            addTournee(t);
            temps_mort_after = calculeTempsMort();
            removeTournee(t);
            return temps_mort_after-temps_mort_before;
        }
        return Integer.MAX_VALUE;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    @Override
    public String toString() {
        return "Shift{" +
                "dureeMin=" + dureeMin +
                ", dureeMax=" + dureeMax +
                '}';
    }

    /**
     * @return le temps mort en minutes du shift
     */
    public int calculeTempsMort(){
        Collections.sort(tourneeList);
        int tempsMort = 0;
        Tournee tournee,lastTournee=null;
        Iterator<Tournee> iterator = tourneeList.iterator();

        //On gère le temps mort pour les espaces entre les tournées
        while (iterator.hasNext()) {
            tournee = iterator.next();
            if (lastTournee == null){
                //On est à la première tournée
                //On ne fait rien
            }else{
                if (lastTournee.getDateFin().getMinutesCorrespondantes()!=tournee.getDateDebut().getMinutesCorrespondantes())
                    tempsMort+=tournee.getDateDebut().getMinutesCorrespondantes()-lastTournee.getDateFin().getMinutesCorrespondantes();
            }
            lastTournee = tournee;
        }

        //On gère le temps mort pour les shift durant moins de 2h
        if (getDureeTotalShift()<dureeMin) // Si la durée du shift est inférieure à la durée min
            tempsMort+=dureeMin-getDureeTotalShift();

        return tempsMort;
    }

    public int getDureeMin() {
        return dureeMin;
    }

    public int getDureeMax() {
        return dureeMax;
    }

    public int getTempsDebut(){
        int min = Integer.MAX_VALUE;
        for (Tournee t:tourneeList) {
            if (t.getDateDebut().getMinutesCorrespondantes()<min)
                min=t.getDateDebut().getMinutesCorrespondantes();
        }
        return min;
    }

    public ArrayList<Tournee> getListeTempsMort() {
        ArrayList<Tournee> listTempsMorts = new ArrayList<>();
        Collections.sort(tourneeList);
        int tempsMort = 0;
        Tournee tournee,lastTournee=null;
        Iterator<Tournee> iterator = tourneeList.iterator();

        //On gère le temps mort pour les espaces entre les tournées
        try {
            while (iterator.hasNext()) {
                tournee = iterator.next();
                if (lastTournee == null) {
                    //On est à la première tournée
                    //On ne fait rien
                } else {
                    if (lastTournee.getDateFin().getMinutesCorrespondantes() != tournee.getDateDebut().getMinutesCorrespondantes())
                        listTempsMorts.add(new Tournee(lastTournee.getDateFin(),tournee.getDateDebut()));
                }
                lastTournee = tournee;
            }

            //On gère le temps mort pour les shift durant moins de 2h
            if (getDureeTotalShift() < dureeMin) { // Si la durée du shift est inférieure à la durée min
                listTempsMorts.add(new Tournee(tourneeList.get(tourneeList.size() - 1).getDateFin(), new Horaire(tourneeList.get(tourneeList.size() - 1).getDateFin().getMinutesCorrespondantes() + dureeMin - getDureeTotalShift())));
            }
        }catch (TourneeException e){
            e.printStackTrace();
        } catch (HoraireException e) {
            e.printStackTrace();
        }
        return listTempsMorts;
    }

    /**
     * @return la durée totale du shift en minute
     */
    private int getDureeTotalShift(){
        if (tourneeList.size() == 0)
            return 0;
        if (tourneeList.size() == 1)
            return tourneeList.get(0).getDateFin().getMinutesCorrespondantes()-tourneeList.get(0).getDateDebut().getMinutesCorrespondantes();
        return tourneeList.get(tourneeList.size()-1).getDateFin().getMinutesCorrespondantes()-tourneeList.get(0).getDateDebut().getMinutesCorrespondantes();
    }

    public List<Tournee> getTourneeList() {
        return tourneeList;
    }

    public static void main(String[] args) throws HoraireException, TourneeException {
        Shift shift = new Shift(120,180);
        Tournee t2 = new Tournee(new Horaire("10:00"),new Horaire("11:00"));
        Tournee t1 = new Tournee(new Horaire("11:15"),new Horaire("11:30"));
        Tournee t3 = new Tournee(new Horaire("11:29"),new Horaire("11:45"));
        System.out.println(shift.addTournee(t1));
        System.out.println(shift.addTournee(t2));
        System.out.println(shift.addTournee(t3));
        System.out.println(shift.calculeTempsMort());
    }

}