/**
 * Fichier de classe définissant la machine tapis roulant
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.plateau
 */
package modele.plateau;

/**
 * Classe Tapis héritante de {@link Machine}, représentant un tapis roulant qui transporte
 * les items d'une case à l'autre dans sa direction de sortie.
 * Le tapis accepte les items uniquement depuis l'arrière (direction opposée à sa sortie).
 * <p>
 * Le tapis possède un système de détection de coins : si un tapis voisin latéral
 * pointe vers ce tapis, celui-ci devient un tapis en coin et accepte les items
 * depuis la direction du voisin au lieu de l'arrière. Cela permet de créer
 * des virages automatiques dans les lignes de tapis.
 *
 * @see Machine
 * @see Plateau#mettreAJourCoins(int, int)
 */
public class Tapis extends Machine{
    /**
     * @serial Direction d'entrée en coin, indiquant depuis quelle direction latérale
     *         le tapis accepte les items lorsqu'il est en mode coin.
     *         Vaut null si le tapis est droit (entrée depuis l'arrière uniquement).
     */
    private Direction coinEntree;

    /**
     * Fonction déterminant si le tapis accepte un item depuis une direction donnée.
     * Si le tapis est en mode coin ({@link #coinEntree} != null), il accepte uniquement
     * depuis la direction du coin. Sinon, il accepte uniquement depuis l'arrière
     * (direction opposée à sa direction de sortie).
     *
     * @param provenance la direction depuis laquelle arrive l'item
     * @return true si l'item est accepté depuis cette direction
     */
    @Override
    public boolean accepteDepuis(Direction provenance) {
        if (coinEntree != null) {
            return provenance == coinEntree;
        } else {
            return provenance == d.direction_opossee();
        }
    }

    /**
     * Méthode permettant de définir manuellement la direction d'entrée en coin
     * @param coinEntree la direction d'entrée latérale, ou null pour un tapis droit
     */
    public void setCoinEntree(Direction coinEntree) {
        this.coinEntree = coinEntree;
    }

    /**
     * Fonction permettant de retourner la direction d'entrée en coin actuelle
     * @return la direction d'entrée en coin, ou null si le tapis est droit
     */
    public Direction getCoinEntree() {
        return coinEntree;
    }

    /**
     * Méthode permettant de détecter automatiquement si ce tapis doit se transformer en coin.
     * Parcourt les 4 directions voisines en ignorant l'entrée normale (arrière) et la sortie (avant).
     * Si un tapis voisin latéral a sa direction de sortie pointant vers ce tapis,
     * alors ce tapis devient un coin acceptant les items depuis cette direction latérale.
     * Si aucun tapis voisin ne pointe vers lui, le tapis repasse en mode droit.
     * <p>
     * Appelée automatiquement par {@link Plateau#mettreAJourCoins(int, int)} lors du placement,
     * de la suppression ou de la rotation d'une machine voisine.
     */
    public void detecterCoin() {
        coinEntree = null;
        if (c == null) return;

        for (Direction dir : Direction.values()) {
            if (dir == d.direction_opossee()) continue;
            if (dir == d) continue;

            Case voisine = c.plateau.getCase(c, dir);
            if (voisine != null && voisine.getMachine() instanceof Tapis) {
                Tapis voisin = (Tapis) voisine.getMachine();
                if (voisin.getDirection() == dir.direction_opossee()) {
                    coinEntree = dir;
                    return;
                }
            }
        }
    }
}