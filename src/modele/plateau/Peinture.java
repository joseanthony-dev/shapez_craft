package modele.plateau;

import modele.item.Item;
import modele.item.ItemColor;
import modele.item.ItemShape;

public class Peinture extends Machine {
    private ItemColor couleurEnAttente = null;
    private ItemShape formeEnAttente = null;

    @Override
    public boolean accepteDepuis(Direction provenance) {
        if (provenance == d.direction_suivante()) {
            return couleurEnAttente == null;
        }
        if (provenance == d.direction_opossee()) {
            return formeEnAttente == null;
        }
        return false;
    }

    @Override
    public void work() {
        if (!current.isEmpty()) {
            Item item = current.getFirst();
            if (item instanceof ItemColor && couleurEnAttente == null) {
                couleurEnAttente = (ItemColor) item;
                current.clear();
            } else if (item instanceof ItemShape && formeEnAttente == null) {
                formeEnAttente = (ItemShape) item;
                current.clear();
            }
        }

        if (couleurEnAttente != null && formeEnAttente != null) {
            formeEnAttente.Color(couleurEnAttente.getColor());
            current.clear();
            current.add(formeEnAttente);
            formeEnAttente = null;
            couleurEnAttente = null;
        }
    }

    @Override
    public void send() {
        if (!current.isEmpty() && current.getFirst() instanceof ItemShape) {
            super.send();
        }
    }
}