/**
 * Fichier de classe définissant la machine empileur
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.plateau
 * @see modele.item.ItemShape
 */
package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;

/**
 * Classe Empileur héritante de {@link Machine}, représentant un empileur
 * qui superpose deux formes ({@link ItemShape}) l'une sur l'autre.
 * La machine possède deux entrées :
 * <ul>
 *     <li>Entrée arrière (direction opposée) : reçoit la forme de base (couche inférieure)</li>
 *     <li>Entrée latérale droite ({@link Direction#direction_suivante()}) : reçoit la forme supérieure</li>
 * </ul>
 * L'empilement s'effectue quand les deux formes sont présentes via {@link ItemShape#stack(ItemShape)}.
 * Le résultat peut contenir jusqu'à {@link ItemShape#MAX_LAYERS} couches au total.
 *
 * @see Machine
 * @see ItemShape#stack(ItemShape)
 */
public class Empileur extends Machine {
    /**
     * @serial Forme de base (couche inférieure) en attente d'empilement, reçue depuis l'arrière.
     *         Vaut null avant réception ou après l'empilement.
     */
    private ItemShape formeBase = null;

    /**
     * @serial Forme supérieure en attente d'empilement, reçue depuis le côté droit.
     *         Vaut null avant réception ou après l'empilement.
     */
    private ItemShape formeSup = null;

    /**
     * Fonction déterminant les deux directions d'entrée de l'empileur :
     * <ul>
     *     <li>Depuis l'arrière ({@link Direction#direction_opossee()}) : accepte si la forme de base est libre</li>
     *     <li>Depuis le côté droit ({@link Direction#direction_suivante()}) : accepte si la forme supérieure est libre</li>
     * </ul>
     *
     * @param provenance la direction depuis laquelle arrive l'item
     * @return true si la machine accepte une forme depuis cette direction
     */
    @Override
    public boolean accepteDepuis(Direction provenance) {
        if (provenance == d.direction_opossee()) return formeBase == null;
        if (provenance == d.direction_suivante()) return formeSup == null;
        return false;
    }

    /**
     * Méthode de travail de l'empileur qui stocke les formes reçues et les empile
     * quand les deux sont présentes.
     * Les formes sont stockées dans les attributs en attente en fonction de l'ordre d'arrivée.
     * Quand les deux formes sont disponibles, la forme supérieure est empilée
     * sur la forme de base via {@link ItemShape#stack(ItemShape)} et le résultat est placé
     * dans la liste pour envoi. Les attributs en attente sont réinitialisés à null.
     */
    @Override
    public void work() {
        if (!current.isEmpty()) {
            Item item = current.getFirst();
            if (item instanceof ItemShape) {
                if (formeBase == null) formeBase = (ItemShape) item;
                else if (formeSup == null) formeSup = (ItemShape) item;
                current.clear();
            }
        }
        if (formeBase != null && formeSup != null) {
            formeBase.stack(formeSup);
            current.clear();
            current.add(formeBase);
            formeBase = null;
            formeSup = null;
        }
    }

    /**
     * Méthode d'envoi qui transmet la forme empilée uniquement si le résultat est un {@link ItemShape}.
     * Utilise le comportement par défaut de {@link Machine#send()} pour l'envoi vers l'avant.
     */
    @Override
    public void send() {
        if (!current.isEmpty() && current.getFirst() instanceof ItemShape) {
            super.send();
        }
    }
}