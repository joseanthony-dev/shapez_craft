/**
 * Fichier de classe définissant la machine rotateur
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.plateau
 * @see modele.item.ItemShape
 */
package modele.plateau;
import modele.item.ItemShape;

/**
 * Classe Rotateur héritante de {@link Machine}, représentant un rotateur
 * qui fait pivoter les formes ({@link ItemShape}) de 90 degrés dans le sens horaire.
 * Le rotateur n'accepte les items que depuis l'arrière (direction opposée à sa sortie).
 * Un flag {@link #dejaTourne} empêche de tourner la même forme plusieurs fois
 * tant qu'elle n'a pas été envoyée à la machine suivante.
 *
 * @see Machine
 * @see ItemShape#rotate()
 */
public class Rotateur extends Machine {
    /**
     * @serial Booléen indiquant si la forme actuelle a déjà été tournée.
     *         Empêche les rotations multiples sur le même item entre deux envois.
     *         Réinitialisé à false quand la machine est vidée après un envoi réussi.
     */
    private boolean dejaTourne = false;

    /**
     * Méthode de travail du rotateur qui fait pivoter la forme de 90 degrés dans le sens horaire.
     * La rotation n'est effectuée que si la machine contient un {@link ItemShape}
     * et que la forme n'a pas déjà été tournée ({@link #dejaTourne} == false).
     * Appelle {@link ItemShape#rotate()} pour effectuer la rotation des quadrants.
     */
    @Override
    public void work() {
        if (!current.isEmpty() && current.getFirst() instanceof ItemShape && !dejaTourne) {
            ((ItemShape) current.getFirst()).rotate();
            dejaTourne = true;
        }
    }

    /**
     * Méthode d'envoi qui transmet la forme tournée et réinitialise le flag de rotation.
     * Utilise le comportement par défaut de {@link Machine#send()} pour l'envoi.
     * Si la machine est vide après l'envoi (envoi réussi), le flag {@link #dejaTourne}
     * est réinitialisé à false pour préparer la prochaine forme.
     */
    @Override
    public void send() {
        super.send();
        if (current.isEmpty()) {
            dejaTourne = false;
        }
    }

    /**
     * Fonction déterminant que le rotateur n'accepte les items que depuis l'arrière
     * (direction opposée à sa direction de sortie)
     *
     * @param provenance la direction depuis laquelle arrive l'item
     * @return true uniquement si l'item vient de l'arrière de la machine
     */
    @Override
    public boolean accepteDepuis(Direction provenance) {
        return provenance == d.direction_opossee();
    }
}
