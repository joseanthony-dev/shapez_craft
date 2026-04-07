/**
 * Fichier de classe définissant la machine poubelle
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.plateau
 */
package modele.plateau;

/**
 * Classe Poubelle héritante de {@link Machine}, représentant une poubelle
 * qui accepte et détruit tous les items reçus.
 * La poubelle accepte les items depuis toutes les directions (comportement par défaut de {@link Machine}).
 * Les items s'accumulent dans la liste {@link Machine#current} mais ne sont jamais envoyés,
 * ce qui les supprime effectivement du circuit de production.
 *
 * @see Machine
 */
public class Poubelle extends Machine {

    /**
     * Méthode d'envoi redéfinie pour ne rien faire.
     * Les items reçus sont conservés dans la liste interne mais ne sont jamais transmis,
     * ce qui les détruit effectivement du point de vue du circuit de production.
     */
    @Override
    public void send() {
    }

    @Override
    public void work() {
        current.clear();
    }
}
