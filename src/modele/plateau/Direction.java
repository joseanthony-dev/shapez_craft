/**
 * Fichier d'énumération des directions
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.plateau
 */
package modele.plateau;

/**
 * Enumération permettant de définir les 4 directions cardinales utilisées par les machines.
 * Chaque direction possède un décalage en x ({@link #dx}) et en y ({@link #dy}) permettant
 * de calculer la case voisine dans cette direction.
 * Les conventions sont : x croissant vers la droite (Est), y croissant vers le bas (Sud).
 * <p>
 * L'ordre de rotation horaire est : Nord → Est → Sud → Ouest → Nord.
 * Cet ordre est utilisé par {@link Machine#tourner()} pour faire pivoter les machines.
 *
 * @see Machine#tourner()
 * @see Plateau#getCase(Case, Direction)
 */
public enum Direction {
    /**
     * @serial Les différentes directions en fonction des coordonnées
     */
    North(0, -1), South(0, 1), East(1, 0), West(-1, 0);
    /**
     * @serial Décalage horizontal de la direction.
     *         Vaut -1 pour Ouest, 0 pour Nord/Sud, 1 pour Est.
     */
    int dx;

    /**
     * @serial Décalage vertical de la direction.
     *         Vaut -1 pour Nord, 0 pour Est/Ouest, 1 pour Sud.
     */
    int dy;

    /**
     * Constructeur de l'énumération pour initialiser les décalages de la direction
     * @param _dx le décalage horizontal de la direction
     * @param _dy le décalage vertical de la direction
     */
    private Direction(int _dx, int _dy) {
        dx = _dx;
        dy = _dy;
    }

    /**
     * Fonction permettant de renvoyer la direction suivante dans le sens horaire.
     * L'ordre est : Nord → Est → Sud → Ouest → Nord.
     * Utilisée par {@link Machine#tourner()} pour faire pivoter les machines.
     *
     * @return la direction suivante dans le sens horaire
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
     * Fonction permettant de renvoyer la direction opposée à la direction actuelle.
     * Utilisée par les machines pour déterminer si un item arrive par l'arrière
     * via {@link Machine#accepteDepuis(Direction)}.
     *
     * @return la direction opposée (Nord↔Sud, Est↔Ouest)
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
     * Fonction permettant de renvoyer la direction à gauche dans le sens anti-horaire.
     * L'ordre est : Nord → Ouest → Sud → Est → Nord.
     * Utilisée par le {@link Decoupeur} pour déterminer la sortie latérale gauche.
     *
     * @return la direction de gauche par rapport à la direction actuelle
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