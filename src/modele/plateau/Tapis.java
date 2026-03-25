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

    public void detecterCoin(Case c){
    }

}