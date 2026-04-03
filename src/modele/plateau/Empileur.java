package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;

public class Empileur extends Machine {
    private ItemShape formeBase = null;
    private ItemShape formeSup = null;

    @Override
    public boolean accepteDepuis(Direction provenance) {
        if (provenance == d.direction_opossee()) return formeBase == null;
        if (provenance == d.direction_suivante()) return formeSup == null;
        return false;
    }

    @Override
    public void work() {
        if (!current.isEmpty()) {
            Item item = current.getFirst();
            if (item instanceof ItemShape) {
                if (formeBase == null) formeBase = (ItemShape) item;
                else if (formeSup == null) formeSup = (ItemShape) item;
                current.clear();
            }
        }
        if (formeBase != null && formeSup != null) {
            formeBase.stack(formeSup);
            current.clear();
            current.add(formeBase);
            formeBase = null;
            formeSup = null;
        }
    }

    @Override
    public void send() {
        if (!current.isEmpty() && current.getFirst() instanceof ItemShape) {
            super.send();
        }
    }
}