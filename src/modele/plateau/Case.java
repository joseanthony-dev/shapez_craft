/**
 * Fichier de classe définissant une case de plateau
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.plateau
 * @see modele.item.Item
 */
package modele.plateau;
import modele.item.Item;

/**
 * Classe permettant de définir une case
 */
public class Case {
    /**
     * @serial Définit un plateau
     */
    protected Plateau plateau;
    /**
     * @serial Définit une machine
     */
    protected Machine machine;
    /**
     * @serial Définit un gisement
     */
    protected Item gisement;

    /**
     * Méthode permettant de définir une machine sur la case
     * @param m définit la machine à init sur la case
     */
    public void setMachine(Machine m) {
        machine = m;
        if (m != null) {
            m.setCase(this);
        }
    }

    /**
     * Fonction permettant de retourner la machine actuelle stocké sur la case
     * @return la machine actuelle de la case
     */
    public Machine getMachine() {
        return machine;
    }

    /**
     * Constructeur permettant d'initialiser le plateau
     * @param _plateau définit le plateau
     */
    public Case(Plateau _plateau) {
        plateau = _plateau;
    }

    /**
     * Fonction permettant de retourner le type de gisement de la case
     * @return le gisement actuel de la case
     */
    public Item getGisement() {
        return gisement;
    }

    /**
     * Méthode permettant d'initialiser un gisement sur la case
     * @param g définit le gisement à initialiser
     */
    public void setGisement(Item g) {
        gisement = g;
    }
}