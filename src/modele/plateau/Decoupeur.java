package modele.plateau;

import modele.item.ItemShape;

public class Decoupeur extends Machine {
    private ItemShape moitieGauche = null;

    @Override
    public void work() {
        if (!current.isEmpty() && current.getFirst() instanceof ItemShape && moitieGauche == null) {
            ItemShape shape = (ItemShape) current.getFirst();
            moitieGauche = shape.Cut();
        }
    }

    @Override
    public void send() {
        // Envoi moitié droite (direction normale)
        super.send();

        // Envoi moitié gauche (direction à gauche)
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