/**
 * Fichier de classe définissant la machine mine
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.plateau
 * @see modele.item
 */
package modele.plateau;

import modele.item.ItemShape;
import java.util.Random;
import modele.item.ItemColor;
import modele.item.Item;

/**
 * Classe Mine héritante de {@link Machine}, représentant une mine qui extrait
 * des items depuis le gisement de la case sur laquelle elle est posée.
 * La mine ne peut être posée que sur une case contenant un gisement.
 * À chaque tick, la mine a une probabilité de 25% (1 chance sur 4) de produire
 * une copie de l'item du gisement. La mine ne produit pas si elle contient déjà un item,
 * évitant ainsi l'accumulation en mémoire.
 * <p>
 * La mine produit un {@link modele.item.ItemShape} si le gisement est une forme,
 * ou un {@link modele.item.ItemColor} si le gisement est une couleur.
 *
 * @see Machine
 * @see Case#getGisement()
 */
public class Mine extends Machine {
    /**
     * Méthode de travail de la mine qui extrait un item du gisement avec une probabilité de 25%.
     * Ne produit pas si la mine contient déjà un item (liste non vide).
     * Crée une copie de l'item du gisement en fonction de son type :
     * <ul>
     *     <li>{@link ItemShape} : crée un nouvel ItemShape avec le même code</li>
     *     <li>{@link ItemColor} : crée un nouvel ItemColor avec la même couleur</li>
     * </ul>
     */
    @Override
    public void work() {
        if (current.isEmpty()) {
            if (new Random().nextInt(4) == 0) {
                Item gisement = c.getGisement();
                if (gisement instanceof ItemShape) {
                    current.add(new ItemShape(((ItemShape) gisement).getCode()));
                } else if (gisement instanceof ItemColor) {
                    current.add(new ItemColor(((ItemColor) gisement).getColor()));
                }
            }
        }
    }

    /**
     * Méthode d'envoi de l'item extrait vers la machine voisine dans la direction de la mine.
     * Utilise le comportement par défaut de {@link Machine#send()}.
     */
    @Override
    public void send() {
        super.send();
    }
}
