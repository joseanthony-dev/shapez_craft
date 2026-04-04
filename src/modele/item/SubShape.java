/**
 * Fichier d'énumération permettant de lister les formes
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.item
 */
package modele.item;

/**
 * Enumération permettant de stocker les types de sous-formes composant un quadrant d'un item.
 * Chaque item est composé de 4 quadrants par couche, et chaque quadrant est une de ces sous-formes.
 * Le codage en caractère est utilisé pour la sérialisation des formes en chaîne de caractères.
 */
public enum SubShape {
    Circle, Carre, Fan, Star, None
}
