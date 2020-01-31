package modele;

import exception.TourneeException;
import io.InstanceReader;
import io.exception.ReaderException;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

@NamedQueries({
        @NamedQuery(name = "findInstances",
                query = "SELECT i.nom FROM modele.Instance i"
        ),
        @NamedQuery(name = "findInstancesByName",
                query = "SELECT i FROM modele.Instance i WHERE i.nom = :nomInstance"
        )
})

@Entity
public class Instance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "InstanceID")
    private int id;

    private String nom;
    private int dureeMin;
    private int dureeMax;

    public Date getDate() {
        return date;
    }

    public String getDateStr() {
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
        return formater.format(date);
    }

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @OneToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private Set<Tournee> tourneeList;

    @OneToMany(mappedBy = "instance", cascade = {CascadeType.PERSIST})
    private List<Solution> solution;

    public Instance() {
        nom = "<INSTANCE_NAME>";
        dureeMin = 0;
        dureeMax = 0;
        date = new Date();
        tourneeList = new HashSet<>();
    }

    public Instance(String nom, int dureeMin, int dureeMax, Date date) {
        this();
        if (!"".equals(nom))
            this.nom = nom;

        if (dureeMin > 0 && dureeMax > dureeMin) {
            this.dureeMin = dureeMin;
            this.dureeMax = dureeMax;
        }

        if (date != null)
            this.date = date;
    }

    public boolean addTournee(Tournee t) {
        if (t == null)
            return false;

        if (tourneeList.add(t)) {
            t.setInstance(this);
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instance instance = (Instance) o;
        return id == instance.id &&
                dureeMin == instance.dureeMin &&
                dureeMax == instance.dureeMax &&
                Objects.equals(nom, instance.nom) &&
                Objects.equals(date, instance.date) &&
                Objects.equals(tourneeList, instance.tourneeList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, dureeMin, dureeMax, date, tourneeList);
    }

    public String AfficheTourneeList() {
        String str = "";
        for (Tournee t : tourneeList)
            str += t.toString();

        return str;
    }

    public void setSolution(Solution solution) {
        if(this.solution == null) {
            this.solution = new ArrayList<>();
        }
        this.solution.add(solution);
    }

    public Set<Tournee> getTourneeList() {
        return tourneeList;
    }

    public int getDureeMin() {
        return dureeMin;
    }

    public int getDureeMax() {
        return dureeMax;
    }

    public List<Solution> getSolution() {
        return solution;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return l'heure à laquelle la premiere livraison commence
     */
    public int getTempsMinDebutInstance() {
        int temps_min = Integer.MAX_VALUE;
        for (Tournee t : tourneeList) {
            if (t.getDateDebut().getMinutesCorrespondantes() < temps_min)
                temps_min = t.getDateDebut().getMinutesCorrespondantes();
        }
        return temps_min;
    }

    /**
     * @return l'heure à laquelle la dernière livraison se termine
     */
    public int getTempsMaxFinInstance() {
        int temps_max = 0;
        for (Tournee t : tourneeList) {
            if (t.getDateFin().getMinutesCorrespondantes() > temps_max)
                temps_max = t.getDateFin().getMinutesCorrespondantes();
        }
        return temps_max;
    }


    @Override
    public String toString() {
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");

        return "Instance{" +
                "nom='" + nom + '\'' +
                ", dureeMin=" + dureeMin +
                ", dureeMax=" + dureeMax +
                ", date=" + formater.format(date) +
                "\n tourneeList : \n" + AfficheTourneeList() +
                '}';
    }

    public static void main(String[] args) throws ReaderException, TourneeException {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PersistanceUnitDeliver");
        EntityManager em = emf.createEntityManager();

        InstanceReader reader = new InstanceReader("instances/instance_test.csv");
        Instance c = reader.readInstance();
    }

}