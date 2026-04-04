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
 * Classe représentant une case individuelle du plateau de jeu.
 * Chaque case peut contenir une {@link Machine} posée par le joueur
 * et un gisement ({@link modele.item.Item}) prédéfini lors de l'initialisation du plateau.
 * La case conserve une référence vers son {@link Plateau} parent pour permettre
 * aux machines d'accéder aux cases voisines via {@link Plateau#getCase(Case, Direction)}.
 *
 * @see Plateau
 * @see Machine
 * @see modele.item.Item
 */
public class Case {
    /**
     * @serial Référence vers le plateau contenant cette case, permet aux machines
     *         d'accéder aux cases voisines pour l'envoi et la réception d'items
     */
    protected Plateau plateau;

    /**
     * @serial Machine actuellement posée sur cette case par le joueur.
     *         Peut être null si aucune machine n'est posée.
     *         Lorsqu'une machine est affectée, sa référence vers cette case est
     *         automatiquement mise à jour via {@link Machine#setCase(Case)}.
     */
    protected Machine machine;

    /**
     * @serial Gisement fixe présent sur cette case, défini à l'initialisation du plateau.
     *         Peut être un {@link modele.item.ItemColor} pour les gisements de couleur
     *         ou un {@link modele.item.ItemShape} pour les gisements de forme.
     *         Peut être null si la case ne contient aucun gisement.
     *         Les gisements ne sont pas modifiés durant le jeu.
     */
    protected Item gisement;

    /**
     * Méthode permettant de définir une machine sur la case.
     * Si la machine n'est pas null, sa référence vers cette case est automatiquement
     * initialisée via {@link Machine#setCase(Case)} pour lui permettre d'accéder
     * au plateau et aux cases voisines.
     *
     * @param m la machine à poser sur la case, ou null pour retirer la machine existante
     */
    public void setMachine(Machine m) {
        machine = m;
        if (m != null) {
            m.setCase(this);
        }
    }

    /**
     * Fonction permettant de retourner la machine actuellement posée sur cette case
     * @return la machine présente sur la case, ou null si aucune machine n'est posée
     */
    public Machine getMachine() {
        return machine;
    }

    /**
     * Constructeur permettant d'initialiser la case avec une référence vers son plateau parent
     * @param _plateau le plateau contenant cette case
     */
    public Case(Plateau _plateau) {
        plateau = _plateau;
    }

    /**
     * Fonction permettant de retourner le gisement fixe présent sur cette case
     * @return le gisement de la case ({@link modele.item.ItemColor} ou {@link modele.item.ItemShape}),
     *         ou null si la case ne contient aucun gisement
     */
    public Item getGisement() {
        return gisement;
    }

    /**
     * Méthode permettant de définir un gisement fixe sur cette case.
     * Appelée lors de l'initialisation du plateau dans {@link Plateau#initPlateauVide()}.
     *
     * @param g l'item à définir comme gisement sur cette case
     */
    public void setGisement(Item g) {
        gisement = g;
    }
}