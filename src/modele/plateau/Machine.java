/**
 * Fichier de classe abstraite définissant le comportement commun des machines
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
 * Classe abstraite Machine implémentant {@link Runnable}, servant de classe mère
 * pour toutes les machines du jeu.
 * Chaque machine possède une liste d'items ({@link #current}), une direction ({@link #d})
 * et une référence vers sa case ({@link #c}).
 * Le cycle de fonctionnement d'une machine se déroule en deux phases à chaque tick :
 * <ol>
 *     <li>{@link #work()} : traitement de l'item (rotation, découpe, mélange, etc.)</li>
 *     <li>{@link #send()} : envoi de l'item vers la machine voisine dans la direction de sortie</li>
 * </ol>
 * Les sous-classes redéfinissent {@link #work()}, {@link #send()} et {@link #accepteDepuis(Direction)}
 * pour implémenter leur comportement spécifique.
 *
 * @see Tapis
 * @see Mine
 * @see Poubelle
 * @see Rotateur
 * @see Decoupeur
 * @see Peinture
 * @see Melangeur
 * @see Empileur
 * @see Livraison
 */
public abstract class Machine implements Runnable {
    /**
     * @serial Liste chaînée contenant les items actuellement dans la machine.
     *         En général, une machine ne contient qu'un seul item à la fois.
     *         La liste est vidée après l'envoi réussi de l'item à la machine suivante.
     */
    LinkedList<Item> current;

    /**
     * @serial Référence vers la case sur laquelle cette machine est posée.
     *         Initialisée par {@link Case#setMachine(Machine)} lors du placement.
     *         Permet à la machine d'accéder au plateau et aux cases voisines pour l'envoi d'items.
     */
    Case c;

    /**
     * @serial Direction dans laquelle la machine est orientée, détermine la sortie principale.
     *         Par défaut initialisée à {@link Direction#North}.
     *         Modifiable par le joueur via {@link #tourner()}.
     */
    Direction d = Direction.North;

    /**
     * Constructeur permettant d'initialiser la liste d'items vide
     */
    public Machine() {
        current = new LinkedList<Item>();
    }

    /**
     * Constructeur permettant d'initialiser la liste d'items et d'y ajouter un item directement
     * @param _item l'item à ajouter à la liste dès la création de la machine
     */
    public Machine(Item _item) {
        this();
        current.add(_item);
    }

    /**
     * Méthode permettant d'associer la machine à une case du plateau.
     * Appelée automatiquement par {@link Case#setMachine(Machine)} lors du placement.
     *
     * @param _c la case sur laquelle la machine est posée
     */
    public void setCase(Case _c) {
        c = _c;
    }

    /**
     * Fonction permettant de récupérer le premier item actuellement dans la machine.
     * Utilisée par la vue pour afficher l'item transporté sur la case.
     *
     * @return le premier item de la liste, ou null si la machine est vide
     */
    public Item getCurrent() {
        if (current.size() > 0) {
            return current.get(0);
        } else {
            return null;
        }
    }

    /**
     * Fonction permettant de déterminer si la machine accepte un item depuis une direction donnée.
     * Par défaut, la machine accepte depuis toutes les directions.
     * Les sous-classes redéfinissent cette méthode pour restreindre les entrées
     * (par exemple le {@link Tapis} n'accepte que depuis l'arrière).
     *
     * @param provenance la direction depuis laquelle l'item arrive
     * @return true si la machine accepte un item depuis cette direction, false sinon
     */
    public boolean accepteDepuis(Direction provenance) {
        return true;
    }

    /**
     * Méthode permettant de faire pivoter la machine de 90 degrés dans le sens horaire.
     * Change la direction de la machine vers la direction suivante via {@link Direction#direction_suivante()}.
     * Appelée par {@link modele.jeu.Jeu#tournerMachine(int, int)} lorsque le joueur appuie sur R.
     */
    public void tourner() {
        d = d.direction_suivante();
    }

    /**
     * Fonction permettant de retourner la direction actuelle de la machine.
     * Utilisée par la vue pour déterminer quel sprite directionnel afficher.
     *
     * @return la direction actuelle de la machine
     */
    public Direction getDirection() {
        return d;
    }

    /**
     * Méthode permettant d'envoyer l'item actuellement stocké vers la machine voisine
     * dans la direction de sortie ({@link #d}).
     * L'envoi est effectué uniquement si :
     * <ul>
     *     <li>La case voisine dans la direction de sortie existe</li>
     *     <li>La case voisine contient une machine</li>
     *     <li>La liste d'items n'est pas vide</li>
     *     <li>La machine voisine accepte un item depuis la direction opposée
     *         via {@link #accepteDepuis(Direction)}</li>
     *     <li>La liste d'items de la machine voisine est vide</li>
     * </ul>
     * Les sous-classes peuvent redéfinir cette méthode pour ajouter des sorties supplémentaires
     * (par exemple le {@link Decoupeur} envoie aussi par la gauche).
     */
    public void send() {
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
     * Méthode permettant d'effectuer le travail spécifique de la machine sur l'item.
     * Par défaut ne fait rien. Les sous-classes redéfinissent cette méthode pour implémenter
     * leur logique de traitement (rotation de forme, découpe, mélange de couleurs, etc.).
     */
    public void work() {
    }

    /**
     * Méthode d'exécution appelée à chaque tick du jeu par {@link Plateau#run()}.
     * Exécute le cycle complet de la machine : d'abord le traitement via {@link #work()},
     * puis l'envoi de l'item via {@link #send()}.
     */
    @Override
    public void run() {
        work();
        send();
    }
}
