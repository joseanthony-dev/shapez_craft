/**
 * Fichier d'énumération des couleurs
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.item
 */
package modele.item;

/**
 * Enumération permettant de stocker les couleurs disponibles dans le jeu.
 * Ces couleurs sont utilisées pour colorier les formes et les gisements de couleur.
 * Les couleurs primaires sont Red, Green et Blue.
 * Les couleurs secondaires Yellow, Purple et Cyan sont obtenues par mélange.
 * White est la couleur par défaut et le résultat de mélanges non standards.
 */
public enum Color {
    Red, Green, Blue, Yellow, Purple, Cyan, White
}