/**
 * Fichier de classe définissant la zone de livraison
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.plateau
 * @see modele.jeu.Jeu
 */
package modele.plateau;
import modele.jeu.Jeu;
import modele.item.ItemShape;

/**
 * Classe Livraison héritante de {@link Machine}, représentant la zone de livraison fixe du jeu.
 * La livraison est toujours placée à la position (7, 8) sur le plateau et ne peut pas
 * être supprimée ni déplacée par le joueur.
 * <p>
 * À chaque tick, la livraison vérifie si l'item reçu correspond à l'objectif du niveau en cours.
 * Si la forme livrée a le même code que l'objectif défini dans {@link modele.jeu.Niveau#getCodeObjectif()},
 * le jeu passe au niveau suivant via {@link Jeu#niveauSuivant()}.
 * Tous les items reçus sont détruits après vérification (la liste est vidée).
 * La livraison n'envoie jamais d'items vers d'autres machines.
 *
 * @see Machine
 * @see Jeu
 * @see modele.jeu.Niveau
 */
public class Livraison extends Machine {
    /**
     * @serial Référence vers le jeu permettant de vérifier l'objectif du niveau en cours
     *         et de déclencher le passage au niveau suivant lors d'une livraison correcte
     */
    private Jeu jeu;

    /**
     * Constructeur permettant d'initialiser la livraison avec une référence vers le jeu
     * @param jeu la référence vers le jeu pour accéder aux niveaux et déclencher la progression
     */
    public Livraison(Jeu jeu) {
        this.jeu = jeu;
    }

    /**
     * Méthode de travail de la livraison qui vérifie si l'item reçu correspond à l'objectif.
     * Si la liste contient un {@link ItemShape} dont le code ({@link ItemShape#getCode()})
     * correspond au code objectif du niveau en cours ({@link modele.jeu.Niveau#getCodeObjectif()}),
     * le jeu passe au niveau suivant via {@link Jeu#niveauSuivant()}.
     * Dans tous les cas, la liste d'items est vidée après vérification pour accepter
     * le prochain item au tick suivant.
     */
    @Override
    public void work() {
        if (!current.isEmpty()) {
            if (current.getFirst() instanceof ItemShape) {
                ItemShape shape = (ItemShape) current.getFirst();
                if (jeu.getNiveauCourant() != null
                        && shape.getCode().equals(jeu.getNiveauCourant().getCodeObjectif())) {
                    jeu.niveauSuivant();
                }
            }
            current.clear();
        }
    }

    /**
     * Méthode d'envoi redéfinie pour ne rien faire.
     * La livraison est un puits terminal : elle accumule et détruit les items
     * sans jamais les retransmettre à une autre machine.
     */
    @Override
    public void send() {
        // La livraison ne renvoie rien
    }
}
