package modele.plateau;

import modele.item.Item;
import modele.item.ItemColor;

public class Melangeur extends Machine {
    private ItemColor couleur1 = null;
    private ItemColor couleur2 = null;

    @Override
    public boolean accepteDepuis(Direction provenance) {
        if (provenance == d.direction_opossee()) {
            return couleur1 == null;
        }
        if (provenance == d.direction_suivante()) {
            return couleur2 == null;
        }
        return false;
    }

    @Override
    public void work() {
        if (!current.isEmpty()) {
            Item item = current.getFirst();
            if (item instanceof ItemColor) {
                if (couleur1 == null) {
                    couleur1 = (ItemColor) item;
                } else if (couleur2 == null) {
                    couleur2 = (ItemColor) item;
                }
                current.clear();
            }
        }

        if (couleur1 != null && couleur2 != null) {
            couleur1.transform(couleur2.getColor());
            current.clear();
            current.add(couleur1);
            couleur1 = null;
            couleur2 = null;
        }
    }

    @Override
    public void send() {
        if (!current.isEmpty() && current.getFirst() instanceof ItemColor) {
            super.send();
        }
    }
}