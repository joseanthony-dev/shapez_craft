package modele.plateau;

import modele.item.ItemShape;

import java.util.Random;

import modele.item.ItemColor;
import modele.item.Item;

public class Mine extends Machine {
    @Override
    public void work() { // TODO : modifier, suivant le gisement fait
        // ajout d'une condition permettant à la mine de ne pas produire si la mine est déjà pleine évitant de gaspiller de la mémoire
        if(current.isEmpty()){
            if (new Random().nextInt(4) == 0) {
                Item gisement = c.getGisement();
                // On ajoute en fonction du type de gisement si couleur ou forme coloré
                if (gisement instanceof ItemShape) {
                    current.add(new ItemShape(((ItemShape) gisement).getCode()));
                } else if (gisement instanceof ItemColor) {
                    current.add(new ItemColor(((ItemColor) gisement).getColor()));
                }
            }
        }
    }

    @Override
    public void send() {
        super.send();
    }
}
