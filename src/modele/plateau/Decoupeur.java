/**
 * Fichier de classe définissant la machine découpeur
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.plateau
 * @see modele.item.ItemShape
 */
package modele.plateau;

import modele.item.ItemShape;

/**
 * Classe Decoupeur héritante de {@link Machine}, représentant un découpeur
 * qui coupe les formes ({@link ItemShape}) verticalement en deux moitiés.
 * Le découpeur possède deux sorties :
 * <ul>
 *     <li>Sortie avant (direction {@link Machine#d}) : la moitié droite (quadrants 0 et 1)</li>
 *     <li>Sortie gauche (direction {@link Direction#direction_gauche()}) : la moitié gauche (quadrants 2 et 3)</li>
 * </ul>
 * La moitié gauche est stockée temporairement dans {@link #moitieGauche} en attendant
 * qu'une machine voisine soit disponible pour la recevoir.
 *
 * @see Machine
 * @see ItemShape#Cut()
 */
public class Decoupeur extends Machine {
    /**
     * @serial Stockage temporaire de la moitié gauche de la forme découpée.
     *         Vaut null avant la découpe ou après l'envoi réussi vers la sortie gauche.
     *         Rempli par {@link #work()} lors de l'appel à {@link ItemShape#Cut()}.
     */
    private ItemShape moitieGauche = null;

    /**
     * Méthode de travail du découpeur qui coupe la forme en deux moitiés verticales.
     * La découpe n'est effectuée que si la machine contient un {@link ItemShape}
     * et que la moitié gauche précédente a déjà été envoyée ({@link #moitieGauche} == null).
     * Appelle {@link ItemShape#Cut()} qui modifie la forme actuelle en moitié droite
     * et retourne la moitié gauche stockée dans {@link #moitieGauche}.
     */
    @Override
    public void work() {
        if (!current.isEmpty() && current.getFirst() instanceof ItemShape && moitieGauche == null) {
            ItemShape shape = (ItemShape) current.getFirst();
            moitieGauche = shape.Cut();
        }
    }

    /**
     * Méthode d'envoi qui transmet les deux moitiés de la forme découpée :
     * <ol>
     *     <li>La moitié droite est envoyée vers l'avant via {@link Machine#send()}</li>
     *     <li>La moitié gauche est envoyée vers la sortie gauche ({@link Direction#direction_gauche()})
     *         si une machine voisine est présente, accepte depuis cette direction et est vide</li>
     * </ol>
     * La moitié gauche est mise à null après un envoi réussi.
     */
    @Override
    public void send() {
        super.send();
        if (moitieGauche != null) {
            Case gauche = c.plateau.getCase(c, d.direction_gauche());
            if (gauche != null) {
                Machine m = gauche.getMachine();
                if (m != null && m.accepteDepuis(d.direction_gauche().direction_opossee()) && m.current.isEmpty()) {
                    m.current.add(moitieGauche);
                    moitieGauche = null;
                }
            }
        }
    }
}