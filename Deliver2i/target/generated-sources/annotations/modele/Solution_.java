package modele;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import modele.Instance;
import modele.Shift;

@Generated(value="EclipseLink-2.7.5.v20191004-rNA", date="2020-01-29T22:31:20")
@StaticMetamodel(Solution.class)
public class Solution_ { 

    public static volatile SingularAttribute<Solution, Instance> instance;
    public static volatile SingularAttribute<Solution, Integer> id;
    public static volatile ListAttribute<Solution, Shift> shiftList;
    public static volatile SingularAttribute<Solution, Integer> tempsMort;

}