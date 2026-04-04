/**
 * Fichier de classe définissant la machine peinture
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.plateau
 * @see modele.item.ItemShape
 * @see modele.item.ItemColor
 */
package modele.plateau;

import modele.item.Item;
import modele.item.ItemColor;
import modele.item.ItemShape;

/**
 * Classe Peinture héritante de {@link Machine}, représentant un atelier de peinture
 * qui applique une couleur ({@link ItemColor}) sur une forme ({@link ItemShape}).
 * La machine possède deux entrées :
 * <ul>
 *     <li>Entrée arrière (direction opposée) : reçoit la forme à peindre</li>
 *     <li>Entrée latérale droite ({@link Direction#direction_suivante()}) : reçoit la couleur à appliquer</li>
 * </ul>
 * La peinture s'effectue quand les deux items sont présents : la couleur est appliquée
 * sur tous les quadrants non vides de la forme via {@link ItemShape#Color(modele.item.Color)}.
 *
 * @see Machine
 * @see ItemShape#Color(modele.item.Color)
 */
public class Peinture extends Machine {
    /**
     * @serial Couleur en attente d'application sur la forme.
     *         Stockée temporairement en attendant la réception de la forme.
     *         Vaut null avant réception ou après application de la peinture.
     */
    private ItemColor couleurEnAttente = null;

    /**
     * @serial Forme en attente de peinture.
     *         Stockée temporairement en attendant la réception de la couleur.
     *         Vaut null avant réception ou après application de la peinture.
     */
    private ItemShape formeEnAttente = null;

    /**
     * Fonction déterminant les deux directions d'entrée de la peinture :
     * <ul>
     *     <li>Depuis le côté droit ({@link Direction#direction_suivante()}) : accepte si aucune couleur en attente</li>
     *     <li>Depuis l'arrière ({@link Direction#direction_opossee()}) : accepte si aucune forme en attente</li>
     * </ul>
     *
     * @param provenance la direction depuis laquelle arrive l'item
     * @return true si la machine accepte un item depuis cette direction
     */
    @Override
    public boolean accepteDepuis(Direction provenance) {
        if (provenance == d.direction_suivante()) return couleurEnAttente == null;
        if (provenance == d.direction_opossee()) return formeEnAttente == null;
        return false;
    }

    /**
     * Méthode de travail de la peinture qui stocke les items reçus et applique la couleur
     * sur la forme quand les deux sont présents.
     * Les items sont d'abord triés par type et stockés dans les attributs en attente.
     * Quand une couleur et une forme sont disponibles, la couleur est appliquée
     * via {@link ItemShape#Color(modele.item.Color)} et la forme peinte est placée
     * dans la liste pour envoi. Les attributs en attente sont réinitialisés à null.
     */
    @Override
    public void work() {
        if (!current.isEmpty()) {
            Item item = current.getFirst();
            if (item instanceof ItemColor && couleurEnAttente == null) {
                couleurEnAttente = (ItemColor) item;
                current.clear();
            } else if (item instanceof ItemShape && formeEnAttente == null) {
                formeEnAttente = (ItemShape) item;
                current.clear();
            }
        }
        if (couleurEnAttente != null && formeEnAttente != null) {
            formeEnAttente.Color(couleurEnAttente.getColor());
            current.clear();
            current.add(formeEnAttente);
            formeEnAttente = null;
            couleurEnAttente = null;
        }
    }

    /**
     * Méthode d'envoi qui transmet la forme peinte uniquement si le résultat est un {@link ItemShape}.
     * Utilise le comportement par défaut de {@link Machine#send()} pour l'envoi vers l'avant.
     */
    @Override
    public void send() {
        if (!current.isEmpty() && current.getFirst() instanceof ItemShape) {
            super.send();
        }
    }
}