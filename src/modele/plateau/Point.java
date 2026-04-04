/**
 * Fichier de classe définissant un point sur le plateau
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.plateau
 */
package modele.plateau;

/**
 * Classe représentant un point avec des coordonnées x et y sur le plateau.
 * Utilisée par le {@link Plateau} pour associer chaque {@link Case} à sa position
 * dans la grille via une HashMap, et pour calculer les cases voisines selon une {@link Direction}.
 *
 * @see Plateau
 * @see Direction
 */
public class Point {
    /**
     * @serial Coordonnée x du point sur le plateau, comprise entre 0 et {@link Plateau#SIZE_X} - 1
     */
    public int x;

    /**
     * @serial Coordonnée y du point sur le plateau, comprise entre 0 et {@link Plateau#SIZE_Y} - 1
     */
    public int y;

    /**
     * Constructeur permettant d'initialiser un point avec ses coordonnées
     * @param x la coordonnée x du point
     * @param y la coordonnée y du point
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
