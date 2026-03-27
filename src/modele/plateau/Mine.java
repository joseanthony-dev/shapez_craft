package modele.plateau;

import modele.item.ItemShape;

import java.util.Random;

import modele.item.ItemColor;
import modele.item.Item;

public class Mine extends Machine {
    @Override
    public void work() { // TODO : modifier, suivant le gisement fait
        if (new Random().nextInt(4) == 0) {
            Item gisement = c.getGisement();
            // On ajoute en fonction du type de gisement si couleur ou forme
            if (gisement instanceof ItemShape) {
                current.add(new ItemShape("CrCb--Cb"));
            } else if (gisement instanceof ItemColor) {
                current.add(new ItemColor(((ItemColor) gisement).getColor()));
            }
        }
    }

    @Override
    public void send() {
        super.send();
    }
}
