/**
 * Fichier de classe définissant la zone de livraison
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.plateau
 * @see modele.item.Item
 * @see java.util.LinkedList
 */
package modele.plateau;
import modele.item.Item;
import java.util.LinkedList;

/**
 * Classe abstraite machine qui implémente runnable
 */
public abstract class Machine implements Runnable {
    /**
     * @serial Liste qui contiendra les items
     */
    LinkedList<Item> current;

    /**
     * @serial Définit la case de la machine
     */
    Case c;

    /**
     * @serial Définit la direction par défaut vers le nord
     */
    Direction d = Direction.North;

    /**
     * Constructeur permettant d'initialiser la liste contenant les items
     */
    public Machine()
    {
        current = new LinkedList<Item>();
    }

    /**
     * Constructeur permettant d'initialiser la liste contenant les items et d'en ajouter un directement
     * @param _item l'item que l'on veut ajouter à la liste
     */
    public Machine(Item _item) {
        this();
        current.add(_item);
    }

    /**
     * Méthode permettant d'initialiser la case avec une case
     * @param _c la case qui va être affecté à l'attribut
     */
    public void setCase(Case _c) {
        c= _c;
    }

    /**
     * Fonction permettant de renvoyer le premier item de la liste
     * @return l'item de la liste
     */
    public Item getCurrent() {
        if (current.size() > 0) {
            return current.get(0);
        } else {
            return null;
        }
    }

    /**
     * Fonction permettant de savoir si on accepte ou non l'objet en fonction de la direction de la machine la classe machine elle, accepte tout, la fonction sera redéfini dans chaque sous machines
     * @param provenance la direction depuis laquelle arrive l'objet
     * @return un booléan si on peut accepter l'objet ou non
     */
    public boolean accepteDepuis(Direction provenance) {
        return true;
    }

    /**
     * Méthode permettant de changer la direction et donc de tourner la machine
     */
    public void tourner() {
        d = d.direction_suivante();
    }

    /**
     * Fonction qui permet de retourner la direction de la machine, en fonction de cette direction l'affichage sera changé
     * @return
     */
    public Direction getDirection() {
        return d;
    }

    /**
     * Méthode permettant d'envoyer l'item actuellement stocké sur une ou plusieurs sorties dans la direction voulu en vérifiant si la machine voisine peut l'accepter suivant son orientation
     */
    public void send()
    {
        Case up = c.plateau.getCase(c, d);
        if (up != null) {
            Machine m = up.getMachine();
            if (m != null && !current.isEmpty() && m.accepteDepuis(d.direction_opossee()) && m.current.isEmpty()) {
                Item item = current.getFirst();
                m.current.add(item);
                current.remove(item);
            }
        }
    }

    /**
     * Méthode permettant de faire tourner les objets sur eux même à chaque ticks
     */
    public void work() {
        //if (current.size() > 0 && current.get(0) instanceof ItemShape) {
        //    ((ItemShape) current.get(0)).rotate();
        //}
    }; // action de la machine, aucune par défaut

    /**
     * Redéfinition de la méthode run qui effectue le travail sur l'item et l'envoi à son prochain
     */
    @Override
    public void run() {
        work();
        send();
    }
}
