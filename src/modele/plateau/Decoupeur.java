/**
 * Fichier de classe définissant la machine découpeur
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.plateau
 * @see modele.item.ItemShape
 * @see
 */
package modele.plateau;
import modele.item.ItemShape;

/**
 * Classe définissant la machine Découpeur héritante de Machine
 */
public class Decoupeur extends Machine {
    /**
     * @serial Définit la forme moitié gauche de la machine
     */
    private ItemShape moitieGauche = null;

    /**
     * Méthode permettant de récupérer l'item actuel et de le couper
     */
    @Override
    public void work() {
        if (!current.isEmpty() && current.getFirst() instanceof ItemShape && moitieGauche == null) {
            ItemShape shape = (ItemShape) current.getFirst();
            moitieGauche = shape.Cut();
        }
    }

    /**
     * Méthode permettant d'envoyer l'item à la prochaine machine, envoit la moitié droite et par la sortie et accepter seulement depuis le côté gauche à la sortie
     */
    @Override
    public void send() {
        super.send();
        if (moitieGauche != null) {
            Case gauche = c.plateau.getCase(c, d.direction_gauche());
            if (gauche != null) {
                Machine m = gauche.getMachine();
                if (m != null && m.accepteDepuis(d.direction_gauche().direction_opossee()) && m.current.isEmpty()) {
                    m.current.add(moitieGauche);
                    moitieGauche = null;
                }
            }
        }
    }
}