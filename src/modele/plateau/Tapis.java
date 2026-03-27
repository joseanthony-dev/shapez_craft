package modele.plateau;

import modele.plateau.Direction;
import modele.item.ItemShape;

public class Tapis extends Machine{

    private Direction coinEntree;

    @Override
    public boolean accepteDepuis(Direction provenance) {
        if(coinEntree !=null){
            return provenance == coinEntree;
        }else{
            return provenance == d.direction_opossee();
        }
    }

    public void setCoinEntree(Direction coinEntree) {
        this.coinEntree = coinEntree;
    }

    public Direction getCoinEntree() {
        return coinEntree;
    }

    public void detecterCoin(){
        coinEntree = null;
        if (c == null) return;

        // Vérifier les 4 directions voisines
        for (Direction dir : Direction.values()) {
            if (dir == d.direction_opossee()) continue; // ignorer l'entrée normale (tapis droit)
            if (dir == d) continue; // ignorer la sortie

            Case voisine = c.plateau.getCase(c, dir);
            if (voisine != null && voisine.getMachine() instanceof Tapis) {
                Tapis voisin = (Tapis) voisine.getMachine();
                if (voisin.getDirection() == dir.direction_opossee()) {
                    coinEntree = dir;
                    return;
                }
            }
        }
    }
}