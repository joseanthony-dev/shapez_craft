package modele.plateau;

import modele.item.ItemShape;

public class Decoupeur extends Machine {
    @Override
    public void work() {
        if (!current.isEmpty() && current.getFirst() instanceof ItemShape) {
            ItemShape shape = (ItemShape) current.getFirst();
            shape.Cut(); // coupe la forme, this garde la moitié droite
        }
    }
}