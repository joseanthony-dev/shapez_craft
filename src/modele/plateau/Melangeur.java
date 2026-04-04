/**
 * Fichier de classe définissant la machine mélangeur de couleurs
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.plateau
 * @see modele.item.ItemColor
 */
package modele.plateau;

import modele.item.Item;
import modele.item.ItemColor;

/**
 * Classe Melangeur héritante de {@link Machine}, représentant un mélangeur
 * qui combine deux couleurs ({@link ItemColor}) selon les règles RGB.
 * La machine possède deux entrées :
 * <ul>
 *     <li>Entrée arrière (direction opposée) : reçoit la première couleur</li>
 *     <li>Entrée latérale droite ({@link Direction#direction_suivante()}) : reçoit la deuxième couleur</li>
 * </ul>
 * Le mélange s'effectue quand les deux couleurs sont présentes via {@link ItemColor#transform(modele.item.Color)}.
 * Le résultat est la première couleur transformée par la deuxième.
 *
 * @see Machine
 * @see ItemColor#transform(modele.item.Color)
 */
public class Melangeur extends Machine {
    /**
     * @serial Première couleur en attente de mélange, reçue depuis l'arrière.
     *         Vaut null avant réception ou après le mélange.
     */
    private ItemColor couleur1 = null;

    /**
     * @serial Deuxième couleur en attente de mélange, reçue depuis le côté droit.
     *         Vaut null avant réception ou après le mélange.
     */
    private ItemColor couleur2 = null;

    /**
     * Fonction déterminant les deux directions d'entrée du mélangeur :
     * <ul>
     *     <li>Depuis l'arrière ({@link Direction#direction_opossee()}) : accepte si la première couleur est libre</li>
     *     <li>Depuis le côté droit ({@link Direction#direction_suivante()}) : accepte si la deuxième couleur est libre</li>
     * </ul>
     *
     * @param provenance la direction depuis laquelle arrive l'item
     * @return true si la machine accepte un item de couleur depuis cette direction
     */
    @Override
    public boolean accepteDepuis(Direction provenance) {
        if (provenance == d.direction_opossee()) return couleur1 == null;
        if (provenance == d.direction_suivante()) return couleur2 == null;
        return false;
    }

    /**
     * Méthode de travail du mélangeur qui stocke les couleurs reçues et les mélange
     * quand les deux sont présentes.
     * Les couleurs sont stockées dans les attributs en attente selon leur ordre d'arrivée.
     * Quand les deux couleurs sont disponibles, la première est transformée par la deuxième
     * via {@link ItemColor#transform(modele.item.Color)} et le résultat est placé
     * dans la liste pour envoi. Les attributs en attente sont réinitialisés à null.
     */
    @Override
    public void work() {
        if (!current.isEmpty()) {
            Item item = current.getFirst();
            if (item instanceof ItemColor) {
                if (couleur1 == null) couleur1 = (ItemColor) item;
                else if (couleur2 == null) couleur2 = (ItemColor) item;
                current.clear();
            }
        }
        if (couleur1 != null && couleur2 != null) {
            couleur1.transform(couleur2.getColor());
            current.clear();
            current.add(couleur1);
            couleur1 = null;
            couleur2 = null;
        }
    }

    /**
     * Méthode d'envoi qui transmet la couleur mélangée uniquement si le résultat est un {@link ItemColor}.
     * Utilise le comportement par défaut de {@link Machine#send()} pour l'envoi vers l'avant.
     */
    @Override
    public void send() {
        if (!current.isEmpty() && current.getFirst() instanceof ItemColor) {
            super.send();
        }
    }
}