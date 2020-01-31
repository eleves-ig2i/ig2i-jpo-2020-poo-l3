package modele;

import exception.HoraireException;
import exception.TourneeException;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Tournee implements Serializable,Comparable<Tournee>{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TourneeID")
    private int id;

    @AttributeOverrides({
            @AttributeOverride(name="heure",column=@Column(name="heureFin")),
            @AttributeOverride(name="minutes",column=@Column(name="minutesFin")),
            })
    @Embedded
    private Horaire dateFin;

    @AttributeOverrides({
            @AttributeOverride(name="heure",column=@Column(name="heureDebut")),
            @AttributeOverride(name="minutes",column=@Column(name="minutesDebut")),
    })
    @Embedded
    private Horaire dateDebut;

    public Shift getShift() {
        return shift;
    }

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name = "ShiftID")
    private Shift shift;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name = "InstanceID")
    private Instance instance;

    public Tournee() {
        dateFin = new Horaire();
        dateDebut = new Horaire();
    }

    /**
     * Crée une tournée si la date de début est avant la date de fin
     * Si cette condition n'est pas respectée, on met les horaires par défaut
     */
    public Tournee(Horaire dateDebut, Horaire dateFin) throws TourneeException {
        this();
        if (dateDebut != null && dateFin != null && dateDebut.getMinutesCorrespondantes()<dateFin.getMinutesCorrespondantes()) {
            this.dateDebut = dateDebut;
            this.dateFin = dateFin;
        }
        else{
            throw new TourneeException(dateDebut, dateFin);
        }
    }

    @Override
    public String toString() {
        return "Tournee{" +
                "dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                "}\n";
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    @Override
    public int compareTo(Tournee o) {
        if (o == null)
            return 0;
        if (o.dateDebut.getMinutesCorrespondantes()!=this.dateDebut.getMinutesCorrespondantes())
            return dateDebut.getMinutesCorrespondantes()-o.dateDebut.getMinutesCorrespondantes();
        return dateFin.getMinutesCorrespondantes()-o.dateFin.getMinutesCorrespondantes();
    }

    public Horaire getDateFin() {
        return dateFin;
    }

    public Horaire getDateDebut() {
        return dateDebut;
    }

    public int getDuree(){
        return dateFin.getMinutesCorrespondantes() - dateDebut.getMinutesCorrespondantes();
    }

    public static void main(String[] args) throws HoraireException, TourneeException {
        Tournee tournee = new Tournee(new Horaire("10:24"),new Horaire("10:24"));
    }
//        EntityManagerFactory emf= Persistence.createEntityManagerFactory("PersistanceUnitDeliver");
//        EntityManager em=emf.createEntityManager();
//
//        try {
//            final EntityTransaction et = em.getTransaction();
//            et.begin();
//
//            Tournee e = new Tournee(new Horaire("18:11"),new Horaire("11:00"));
//            em.persist(e);
//
//            et.commit();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}