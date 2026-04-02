/**
 * Fichier de classe définissant la zone de livraison
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.plateau
 */
package modele.plateau;
import modele.jeu.Jeu;
import modele.item.ItemShape;

/**
 * Classe livraison fille de la classe machine
 */
public class Livraison extends Machine {
    private Jeu jeu;

    public Livraison(Jeu jeu) {
        this.jeu = jeu;
    }

    /**
     * Méthode qui incrémente le compteur seulement
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
     * Méthode d'envoi, par défault la zone de livraison n'envoi rien, elle accumulle seulement
     */
    @Override
    public void send() {
        // La livraison ne renvoie rien
    }
}
