package modele;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import modele.Solution;
import modele.Tournee;

@Generated(value="EclipseLink-2.7.5.v20191004-rNA", date="2020-01-29T22:31:20")
@StaticMetamodel(Shift.class)
public class Shift_ { 

    public static volatile ListAttribute<Shift, Tournee> tourneeList;
    public static volatile SingularAttribute<Shift, Solution> solution;
    public static volatile SingularAttribute<Shift, Integer> id;

}