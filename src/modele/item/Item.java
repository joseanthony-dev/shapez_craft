/**
 * Fichier de classe abstraite des items
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.item
 */
package modele.item;

/**
 * Classe abstraite Item servant de classe mère pour tous les types d'items du jeu.
 * Un item peut être transporté par les machines sur le plateau.
 * Les deux types concrets sont {@link ItemColor} pour les couleurs
 * et {@link ItemShape} pour les formes colorées.
 *
 * @see ItemColor
 * @see ItemShape
 */
public abstract class Item {
}
