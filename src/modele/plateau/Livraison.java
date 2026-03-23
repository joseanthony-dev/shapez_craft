package modele.plateau;

public class Livraison extends Machine {
    private int compteur = 0;

    @Override
    public void work() {
        if (!current.isEmpty()) {
            compteur++;
            current.clear();
        }
    }

    @Override
    public void send() {
        // La livraison ne renvoie rien
    }

    public int getCompteur() {
        return compteur;
    }
}
