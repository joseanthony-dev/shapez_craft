/**
 * Fichier d'énumération des directions
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.plateau
 */
package modele.plateau;

/**
 * Classe permettant de définir toutes les directions
 */
public enum Direction {
    /**
     * @serial Les différentes directions
     */
    North(0, -1), South(0, 1), East(1, 0), West(-1, 0);
    /**
     * @serial la coordonnée x de la direction
     */
    int dx;
    /**
     * @serial la coordonnée y de la direction
     */
    int dy;

    /**
     * Constructeur de l'énumération pour initialiser les directions
     * @param _dx définit la coordonnée x de la direction
     * @param _dy définit la coordonnée y de la direction
     */
    private Direction(int _dx, int _dy) {
        dx = _dx;
        dy = _dy;
    }

    /**
     * Fonction permettant renvoyer la direction suivante dans l'ordre : Nord -> Est -> Sud -> Ouest
     * @return la direction suivante à la direction actuelle
     */
    public Direction direction_suivante() {
        switch (this) {
            case North: return East;
            case East: return South;
            case South: return West;
            case West: return North;
            default: return North;
        }
    }

    /**
     * Fonction permettant de renvoyer la direction opposée
     * @return la direction opposée à la direction actuelle
     */
    public Direction direction_opossee() {
        switch (this) {
            case North: return South;
            case South: return North;
            case East: return West;
            case West: return East;
            default: return North;
        }
    }

    /**
     * Fonction permettant de renvoyer la direction gauche
     * @return la direction de gauche à la direction acutelle
     */
    public Direction direction_gauche() {
        switch (this) {
            case North: return West;
            case West: return South;
            case South: return East;
            case East: return North;
            default: return North;
        }
    }
}