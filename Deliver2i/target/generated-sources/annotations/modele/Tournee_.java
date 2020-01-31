package modele;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import modele.Horaire;
import modele.Instance;
import modele.Shift;

@Generated(value="EclipseLink-2.7.5.v20191004-rNA", date="2020-01-29T22:31:20")
@StaticMetamodel(Tournee.class)
public class Tournee_ { 

    public static volatile SingularAttribute<Tournee, Instance> instance;
    public static volatile SingularAttribute<Tournee, Horaire> dateDebut;
    public static volatile SingularAttribute<Tournee, Shift> shift;
    public static volatile SingularAttribute<Tournee, Integer> id;
    public static volatile SingularAttribute<Tournee, Horaire> dateFin;

}