package modele;

import graphics.SolutionVisualisationDebug;
import io.InstanceReader;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

@Entity
public class Solution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SolutionID")
    private int id;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name = "InstanceID")
    private Instance instance;

    @OneToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private List<Shift> shiftList;

    private int tempsMort;

    public Solution() {
        instance = null;
        shiftList = new ArrayList<>();
        tempsMort = Integer.MAX_VALUE;
    }

    public Solution(Instance instance) {
        this();
        instance.setSolution(this);
        this.instance = instance;
    }

    /**
     * Ajoute un shift à la liste de shift
     *
     * @param s
     * @return true si ajout, false sinon
     */
    public boolean addShift(Shift s) {
        if (s == null)
            return false;
        if (shiftList.add(s)) {
            s.setSolution(this);
            return true;
        }
        return false;
    }

    /**
     * Calcule le temps mort totale de la solution
     */
    public int calculeTempsMort() {
        tempsMort = 0;
        for (Shift s : shiftList) {
            tempsMort += s.calculeTempsMort();
        }
        return tempsMort;
    }

    /**
     * Ecris la solution la plus triviale possible dans la liste de shift
     */
    public void findSolutionTriviale() {
        shiftList.clear();
        List<Tournee> tournees = new ArrayList<>(instance.getTourneeList());
        Collections.sort(tournees);
        for (Tournee t : tournees) {
            Shift shift = new Shift(instance.getDureeMin(), instance.getDureeMax());
            shift.addTournee(t);
            shiftList.add(shift);
        }
        calculeTempsMort();
    }

    /**
     * trouve la solution intermédiaire
     */
    public void findSolutionInter() {
        boolean flag = false, flag2 = false;
        shiftList.clear();
        List<Tournee> tournees = new ArrayList<>(instance.getTourneeList());
        Collections.sort(tournees);
        int temps;
        for (Tournee t : tournees) {
            temps = 0;
            Shift shift = new Shift(instance.getDureeMin(), instance.getDureeMax());
            for (Shift s : shiftList) {
                if (s.getTourneeList().contains(t)) {
                    flag = true;
                    break;
                }
            }
            if (!flag && shift.addTournee(t)) {
                temps += t.getDuree();
            }

            for (Tournee t2 : tournees) {
                if (!t2.equals(t) && !shiftList.contains(t2.getShift())) {
                    if (temps + t2.getDuree() < instance.getDureeMax()) {

                        for (Shift s : shiftList) {
                            if (s.getTourneeList().contains(t2)) {
                                flag2 = true;
                                break;
                            }
                        }

                        if (!flag2 && shift.addTournee(t2)) {
                            temps += t2.getDuree();
                        }
                    }
                }
            }
            if(shift.getTourneeList().size() > 0)
                shiftList.add(shift);
        }
        calculeTempsMort();
    }

    /**
     * trouve la solution v3
     */
    public void findSolutionv3(){
        shiftList.clear();
        List<Tournee> tournees = new ArrayList<>(instance.getTourneeList());
        int nb_tournee_total = tournees.size();
//        System.out .println(tournees.size());
        Collections.sort(tournees);

        while(tournees.size()>0){
            trouveLaMeilleureCombinaison(tournees);
            //enregistreLimageDeLaSolutionDebug();
        }

        //Je supprime le shift vide
        supprimeShiftsVides();

        for(int i=0;i<10;i++) {

            echangeDeShiftsSiCestMieux();

            bougeDesTourneesSiCestMieux();
            bougeDesBlocksDeTourneesSiCestMieux();

        }

        //Compte le nombre de tournée pour vérifié que le résultat est cohérent
        int counttourneeAdd=0;

        for (Shift s:shiftList) {
            counttourneeAdd+=s.getTourneeList().size();
        }

        //System.out.println(counttourneeAdd);

        calculeTempsMort();
    }

    /**
     * trouve la solution v4
     */
    public void findSolutionv4(){
        findSolutionInter();

        echangeDeShiftsSiCestMieux();

        bougeDesTourneesSiCestMieux();
        bougeDesBlocksDeTourneesSiCestMieux();

        //Compte le nombre de tournée pour vérifié que le résultat est cohérent
        int counttourneeAdd=0;

        for(int i=0;i<10;i++) {

            echangeDeShiftsSiCestMieux();

            bougeDesTourneesSiCestMieux();
            bougeDesBlocksDeTourneesSiCestMieux();

        }

//        System.out.println(counttourneeAdd);

        calculeTempsMort();
    }

    /**
     * Algorithme à appeler une fois qu'une solution est déjà trouvé : elle vient l'améliorer en bougeant des tournées si c'est mieux
     */
    private void bougeDesTourneesSiCestMieux() {
        List<Tournee> tournees;
        List<Tournee> tourneesToMove = new LinkedList<>();
        for (Shift s:shiftList) {
            tournees = s.getTourneeList();
            for(int i=0;i<tournees.size();i++){//Je dois faire attention car la liste de tournée est mise à jour et donc le i peut dépasser le nombre max de tournée..(je break si c'est le cas)
                tournees = s.getTourneeList();
                if (i>=tournees.size())
                    break;
                tourneesToMove.clear();
                for (int c = i;c<tournees.size();c++) {
                    tourneesToMove.add(tournees.get(c));
                }
                for (Tournee t:tourneesToMove) {
                    s.removeTournee(t);
                }
                for(Tournee t:tourneesToMove){
                    insertAuMieuxLaTournee(t);
                }
            }

            for(int i=tournees.size()-1;i>=0;i--){//Je dois faire attention car la liste de tournée est mise à jour et donc le i peut dépasser le nombre max de tournée.. (je break si c'est le cas)
                tournees = s.getTourneeList();
                if (i>=tournees.size())
                    break;
                tourneesToMove.clear();
                for (int c = i;c>=0;c--) {
                    tourneesToMove.add(tournees.get(c));
                }
                for (Tournee t:tourneesToMove) {
                    s.removeTournee(t);
                }
                for(Tournee t:tourneesToMove){
                    insertAuMieuxLaTournee(t);
                }
            }
        }
    }

    /**
     * Algorithme à appeler une fois qu'une solution est déjà trouvé : elle vient l'améliorer en bougeant des blocks de tournées
     */
    private void bougeDesBlocksDeTourneesSiCestMieux(){
        List<Tournee> tournees;
        List<Tournee> tourneesToMove = new LinkedList<>();
        for (Shift s:shiftList) {
            tournees = s.getTourneeList();
            for(int i=0;i<tournees.size();i++){
                tournees = s.getTourneeList();
                if (i>=tournees.size())
                    break;
                tourneesToMove.clear();
                for (int c = i;c<tournees.size();c++) {
                    tourneesToMove.add(tournees.get(c));
                }
                for (Tournee t:tourneesToMove) {
                    s.removeTournee(t);
                }
                if (!insertAuMieuxLesTournees(tourneesToMove)){
                    for (Tournee t:tourneesToMove) {
                        s.addTournee(t);
                    }
                }
            }
            for(int i=tournees.size()-1;i>=0;i--){
                tournees = s.getTourneeList();
                if (i>=tournees.size())
                    break;
                tourneesToMove.clear();
                for (int c = i;c>=0;c--) {
                    tourneesToMove.add(tournees.get(c));
                }
                for (Tournee t:tourneesToMove) {
                    s.removeTournee(t);
                }
                if (!insertAuMieuxLesTournees(tourneesToMove)){
                    for (Tournee t:tourneesToMove) {
                        s.addTournee(t);
                    }
                }
            }
        }
    }

    /**
     * Insère au mieux la tournée passée en param dans la liste de shift (en minimisant le temps mort)
     */
    private void insertAuMieuxLaTournee(Tournee t) {
        int min_tm = Integer.MAX_VALUE;
        Shift best_shift = null;
        for (Shift s:shiftList) {
            int tm = s.combienDeTempsMortsSiAjout(t);
            if (tm<min_tm){
                min_tm = tm;
                best_shift = s;
            }
        }
        best_shift.addTournee(t);
    }

    /**
     * Insère au mieux les tournées dans les shifts (Les tournées seront ajoutés à la suite!)
     * @param tourneesToInsert la liste des tournées à inserer
     * si return true, tournées ont été insérées, sinon on dois les réinsérer
     */
    private boolean insertAuMieuxLesTournees(List<Tournee> tourneesToInsert) {
        int min_tm = Integer.MAX_VALUE;
        Shift best_shift = null;
        int tm = 0;

        for (Shift s:shiftList) {
            tm = 0;

            for (Tournee t:tourneesToInsert) {
                tm += s.combienDeTempsMortsSiAjout(t);
            }

            if (tm < min_tm) {
                min_tm = tm;
                best_shift = s;
            }
        }

        for (Tournee t:tourneesToInsert) {
            if (!best_shift.addTournee(t)){
                for (Tournee t2:tourneesToInsert) {
                    best_shift.removeTournee(t2);
                }
                return false;
            }
        }
        return true;
    }

    private void supprimeShiftsVides() {
        for (int i=0;i<shiftList.size();i++) {
            Shift s = shiftList.get(i);
            if (s.getTourneeList().size()==0)
                shiftList.remove(s);
        }
    }

    /**
     * -	Une combinaison est une liste de tournée qui rentrent dans un shift
     * -	Je pars d’une liste de tournée vide et en utilisant de la récurrence, j’ajoute la tournée qui minimise le temps mort puis je rappelle la fonction pour ajouter une nouvelle tournée qui suivra la tournée précédente. J’arrête la récursivité lorsque l’ajout de la tournée augmente le temps mort.
     * -	On obtient donc un bloc de tournée par shift.
     */
    private void trouveLaMeilleureCombinaison(List<Tournee> tournees) {
        int bestCombinaison = Integer.MAX_VALUE;
        Shift shiftForBestCombinaison = null;
        for (Shift s :shiftList) {
            int c = trouveLaMeilleureCombinaisonPourCeShift(s, tournees, false, false);
            if (c<bestCombinaison){
                bestCombinaison = c;
                shiftForBestCombinaison = s;
            }
        }
        if (shiftForBestCombinaison==null){
            //Normalement, si l'instance est normale, ne devrait pas arriver
            Shift shift = new Shift(instance.getDureeMin(), instance.getDureeMax());
            addShift(shift);
            shiftForBestCombinaison = shift;
        }
        trouveLaMeilleureCombinaisonPourCeShift(shiftForBestCombinaison, tournees, true, true);
    }

    /**
     * Trouve une combinaison de tournée rentrant dans ce shift et return le temps mort du shift
     * @param s_ n'est pas modifié si modifShift == false
     * @param tournees n'est pas modifié si modifTournee == false
     * @return
     */
    private int trouveLaMeilleureCombinaisonPourCeShift(Shift s_, List<Tournee> tournees, boolean modifShift, boolean modifTournee) {
        Shift s;
        int bestTm = Integer.MAX_VALUE;
        int tm;
        int tm_add = 0;
        Tournee bestTournee=null;

        if (!modifShift)
            s = new Shift(s_);//On crée une copie du shift passé en paramètre pour pas le modifier
        else
            s = s_;

        for (Tournee t:tournees) {
            if (s.estTourneeValide(t)) {
                tm = s.combienDeTempsMortsSiAjout(t);
                if (tm < bestTm) {
                    bestTm = tm;
                    bestTournee = t;
                }
            }
        }

        if (bestTournee==null){
            return Integer.MAX_VALUE;
        }

        //Le principe de l'algo est basé sur le fait qu'il faut toujours un shift vide dans la liste de shift
        //Si le shift était vie et que l'on ajoute une tournée, on crée un nouveau shift vide
        if (s.getTourneeList().size()==0 && modifTournee){
            addShift(new Shift(instance.getDureeMin(),instance.getDureeMax()));
        }
        s.addTournee(bestTournee);

        if (modifTournee){
            tournees.remove(bestTournee);
        }

        int tm_add_local = 0;

        while (tm_add_local<=0){
            tm_add += tm_add_local;
            tm_add_local = trouveLaMeilleureCombinaisonPourCeShift(s,tournees,true, modifTournee);
        }

        return tm_add + bestTm;
    }

    private void enregistreLimageDeLaSolutionDebug(){

        int counttourneeAdd = 0;

        for (Shift s:shiftList) {
            counttourneeAdd+=s.getTourneeList().size();
        }

        supprimeShiftsVides();

        SolutionVisualisationDebug s = new SolutionVisualisationDebug(this, "SolutionDebug_"+counttourneeAdd);
        try {
            s.saveImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Shift shift = new Shift(instance.getDureeMin(), instance.getDureeMax());
        addShift(shift);

    }

    /**
     * Algorithme à appeler une fois qu'une solution est déjà trouvé : elle vient l'améliorer en échangeant deux tournées de shift si c'est mieux
     */
    private void echangeDeShiftsSiCestMieux(){
        Set<Tournee> tournees = instance.getTourneeList();
        for (Tournee t:tournees) {
            for (Tournee t2:tournees) {

                if (t.getShift() != t2.getShift()) {
                    int tm = combienDeTempsMortsSiEchange(t, t2);
                    if (tm < 0) {
                        echangeDeShift(t, t2);
                    }
                }

            }
        }
    }

    /**
     * L'échange ne se fait pas si c'est possible
     * @param t passe dans le shift de t2
     * @param t2 passe dans le shift de t
     */
    private void echangeDeShift(Tournee t, Tournee t2) {

        Shift s2 = t2.getShift();
        Shift s = t.getShift();

        if (!s.removeTournee(t)){
            return;
        }

        if(!s2.removeTournee(t2)){
            s.addTournee(t);
            return;
        }

        if (s2.estTourneeValide(t) && s.estTourneeValide(t2)) {

            if (!s2.addTournee(t)) {
                s2.addTournee(t2);
                s.addTournee(t);
                return;
            }

            if (!s.addTournee(t2)) {
                s2.removeTournee(t);
                s2.addTournee(t2);
                s.addTournee(t);
                return;
            }

        }else{
            s.addTournee(t);
            s2.addTournee(t2);
        }

    }

    /**
     * Return le gain de temps mort que l'on fait si on échange les tournées de shift
     * Elle sera supérieure à 0 si la tournee augmente le temps mort
     * et inférieure à 0 si elle diminue le temps mort
     * si return Integer.MAX_VALUE c'est que on ne peut pas ajouter la tournee
     */
    public int combienDeTempsMortsSiEchange(Tournee t, Tournee t2){
        int temps_mort_before = calculeTempsMort();
        int temps_mort_after;

        if (t.getShift()==null || t2.getShift()==null)
            return Integer.MAX_VALUE;

        Shift s2 = t2.getShift();
        Shift s = t.getShift();

        s.removeTournee(t);
        s2.removeTournee(t2);

        if (s2.estTourneeValide(t) && s.estTourneeValide(t2)){

            s2.addTournee(t);
            s.addTournee(t2);

            temps_mort_after = calculeTempsMort();

            s2.removeTournee(t);
            s.removeTournee(t2);

            s.addTournee(t);
            s2.addTournee(t2);

            return temps_mort_after-temps_mort_before;
        }else{
            s.addTournee(t);
            s2.addTournee(t2);
        }
        return Integer.MAX_VALUE;
    }

    public List<Shift> getShiftList() {
        return shiftList;
    }

    /**
     * @return l'heure à laquelle la premiere livraison commence
     */
    public int getTempsMinDebutInstance() {
        return instance.getTempsMinDebutInstance();
    }

    public int getTempsMort() {
        return tempsMort;
    }

    /**
     * @return l'heure à laquelle la dernière livraison se termine
     */
    public int getTempsMaxFinInstance() {
        return instance.getTempsMaxFinInstance();
    }

    public int getTempsMax() {
        return instance.getDureeMax();
    }

    public String getInstanceName() {
        return instance.getNom();
    }


    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PersistanceUnitDeliver");
        EntityManager em = emf.createEntityManager();

        try {
            final EntityTransaction et = em.getTransaction();
            et.begin();
            InstanceReader reader = new InstanceReader("instances/instance_1.csv");
            Instance c = reader.readInstance();
            Solution e = new Solution(c);
            Solution e2 = new Solution(c);
            e.findSolutionTriviale();
            System.out.println(e.getTempsMort());
            e2.findSolutionInter();
            System.out.println(e2.getTempsMort());
            em.persist(e);
            et.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
