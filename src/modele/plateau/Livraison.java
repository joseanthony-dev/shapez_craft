/**
 * Fichier de classe définissant la zone de livraison
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.plateau
 */
package modele.plateau;

/**
 * Classe livraison fille de la classe machine
 */
public class Livraison extends Machine {
    /**
     * @serial compteur incrémenté à chaque forme reçu
     */
    private int compteur = 0;

    /**
     * Méthode qui incrémente le compteur seulement
     */
    @Override
    public void work() {
        if (!current.isEmpty()) {
            compteur++;
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

    /**
     * Fonction qui renvoi la valeur du compteur actuelle
     * @return la valeur du compteur de la zone de livraison
     */
    public int getCompteur() {
        return compteur;
    }
}
