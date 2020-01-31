package modele;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import modele.Solution;
import modele.Tournee;

@Generated(value="EclipseLink-2.7.5.v20191004-rNA", date="2020-01-29T22:31:20")
@StaticMetamodel(Instance.class)
public class Instance_ { 

    public static volatile SingularAttribute<Instance, Date> date;
    public static volatile SingularAttribute<Instance, Integer> dureeMin;
    public static volatile SetAttribute<Instance, Tournee> tourneeList;
    public static volatile ListAttribute<Instance, Solution> solution;
    public static volatile SingularAttribute<Instance, Integer> dureeMax;
    public static volatile SingularAttribute<Instance, Integer> id;
    public static volatile SingularAttribute<Instance, String> nom;

}