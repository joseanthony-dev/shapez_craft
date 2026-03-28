package modele.plateau;
import modele.item.ItemShape;

public class Rotateur extends Machine {
    private boolean dejaTourne = false;

    @Override
    public void work() {
        if (!current.isEmpty() && current.getFirst() instanceof ItemShape && !dejaTourne) {
            ((ItemShape) current.getFirst()).rotate();
            dejaTourne = true;
        }
    }

    @Override
    public void send() {
        super.send();
        if (current.isEmpty()) {
            dejaTourne = false;
        }
    }

    @Override
    public boolean accepteDepuis(Direction provenance) {
        return provenance == d.direction_opossee();
    }
}
