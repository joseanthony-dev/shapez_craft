package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;

import java.util.LinkedList;
import java.util.Queue;

public abstract class Machine implements Runnable {

    LinkedList<Item> current;
    Case c;
    Direction d = Direction.North; // par défaut, pour commencer, tout est orienté au north

    public Machine()
    {
        current = new LinkedList<Item>();
    }

    public Machine(Item _item) {
        this();
        current.add(_item);
    }

    public void setCase(Case _c) {
        c= _c;
    }

    public Item getCurrent() {
        if (current.size() > 0) {
            return current.get(0);
        } else {
            return null;
        }
    }

    /**
     * Fonction permettant de retourner vraie ou faux selon la provenance de l'objet si on acppete de la faire rentrer dans la machine, on redéfinira les fonctions pour chaque machine
     * @param provenance la direction depuis laquelle arrive l'objet (la forme)
     * @return un booléan si on peut accepter l'objet ou non
     */
    public boolean accepteDepuis(Direction provenance) {
        return true; // par defaut, une machine accepte de partout
    }

    // On définit la fonction tourner qui fait le changement de direction
    public void tourner() {
        d = d.direction_suivante();
    }

    /**
     * Permet de retourner la direction de la machine, on fera des affichages différents en fonction
     * @return
     */
    public Direction getDirection() {
        return d;
    }

    public void send() // la machine dépose un item sur sa ou ses sorties
    {
        Case up = c.plateau.getCase(c, d); // On envoit désormais suivant la direction
        if (up != null) {
            Machine m = up.getMachine();
            // permet d'envoyer que si la machine est vide, que sa voisine peut accepter et que la file de sa voisine est vide
            if (m != null && !current.isEmpty() && m.accepteDepuis(d.direction_opossee()) && m.current.isEmpty()) {
                Item item = current.getFirst();
                m.current.add(item);
                current.remove(item);
            }
        }
    }

    public void work() {
        if (current.size() > 0 && current.get(0) instanceof ItemShape) {
            ((ItemShape) current.get(0)).rotate();
        }
    }; // action de la machine, aucune par défaut

    @Override
    public void run() {
        work();
        send();
    }
}
