/**
 * Fichier d'énumération définissant les outils possibles
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.jeu
 */
package modele.jeu;

/**
 * Enumération des outils possibles utilisés pour savoir quel outil est sélectionné
 * et quelle machine sera placée sur le plateau lors d'un clic gauche.
 * Chaque valeur correspond à une machine du jeu que le joueur peut poser.
 *
 * @see Jeu#getOutilSelectionne()
 * @see Jeu#setOutilSelectionne(Outil)
 */
public enum Outil {
    TAPIS, MINE, POUBELLE, ROTATEUR, DECOUPEUR, PEINTURE, MELANGEUR, EMPILEUR
}