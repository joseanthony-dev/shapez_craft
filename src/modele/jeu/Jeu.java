package modele.jeu;

import modele.plateau.*;
import modele.item.ItemShape;

public class Jeu extends Thread{
    private Plateau plateau;
    //On initialise l'outil de départ qui sera le tapis
    private Outil outilSelectionne = Outil.TAPIS;

    public Jeu() {
        plateau = new Plateau();
        plateau.setMachine(5, 10, new Mine());
        plateau.setMachine(5, 5, new Poubelle());
        plateau.setMachine(8, 8, new Livraison());
        start();
    }

    public Plateau getPlateau() {
        return plateau;
    }

    /**
     * Permet de retourner (connaitre) l'outil actuellement utilisé
     * @return une énumération d'outil, l'outil actuellement en cours
     */
    public Outil getOutilSelectionne() {
        return outilSelectionne;
    }

    /**
     * Permet de mettre à jour la variable outil pour un nouvel outil sélectionné
     * @param outil définira le nouvel outil utilisé
     */
    public void setOutilSelectionne(Outil outil) {
        this.outilSelectionne = outil;
    }

    /**
     * Méthode permettant de faire pivoter une machine dans toute les directions
     * @param x
     * @param y
     */
    public void tournerMachine(int x, int y) {
        Machine m = plateau.getCases()[x][y].getMachine();
        if (m != null) {
            m.tourner();
            plateau.mettreAJourCoins(x, y);
        }
    }

    public void supprimerMachine(int x, int y) {
        if (plateau.getCases()[x][y].getMachine() instanceof Livraison) return;
        plateau.setMachine(x, y, null);
    }

    /**
     * Permet maintenant en fonction de l'outil choisit de poser un tapis, une mine, une poubelle ou bien de faire disparaitre la machine
     * @param x indique la coordonnée x de la case du plateau
     * @param y indique la coordonnée y de la case du plateau
     */
    public void press(int x, int y) {
        if (plateau.getCases()[x][y].getMachine() instanceof Livraison) return;
        switch (outilSelectionne) {
            case TAPIS:
                plateau.setMachine(x, y, new Tapis());
                break;
            case MINE:
                plateau.setMachine(x, y, new Mine());
                break;
            case POUBELLE:
                plateau.setMachine(x, y, new Poubelle());
                break;
            case ROTATEUR:
                plateau.setMachine(x, y, new Rotateur());
                break;
            case DECOUPEUR:
                plateau.setMachine(x, y, new Decoupeur());
                break;
        }
    }

    /**
     * Permet maintenant en fonction de l'outil choisit de poser un tapis, une mine, une poubelle ou bien de faire disparaitre la machine si le clic est maintenu
     * @param x indique la coordonnée x de la case du plateau
     * @param y indique la coordonnée y de la case du plateau
     */
    public void slide(int x, int y) {
        if (plateau.getCases()[x][y].getMachine() instanceof Livraison) return;
        switch (outilSelectionne) {
            case TAPIS:
                plateau.setMachine(x, y, new Tapis());
                break;
            case MINE:
                plateau.setMachine(x, y, new Mine());
                break;
            case POUBELLE:
                plateau.setMachine(x, y, new Poubelle());
                break;
            case ROTATEUR:
                plateau.setMachine(x, y, new Rotateur());
                break;
            case DECOUPEUR:
                plateau.setMachine(x, y, new Decoupeur());
                break;
        }
    }

    public void run() {
        jouerPartie();
    }

    public void jouerPartie() {
        while(true) {
            try {
                plateau.run();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
