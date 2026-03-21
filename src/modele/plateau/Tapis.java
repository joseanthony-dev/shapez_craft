package modele.plateau;
import modele.plateau.Direction;
import modele.item.ItemShape;

public class Tapis extends Machine{
    @Override
    public boolean accepteDepuis(Direction provenance) {
        return provenance == d.direction_opossee();
    }
}
