/**
 * Fichier de classe définissant le jeu
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.jeu
 * @see modele.plateau
 */
package modele.jeu;
import modele.plateau.*;

/**
 * Classe Jeu permettant de jouer une partie
 */
public class Jeu extends Thread{
    /**
     * @serial Définit un attribut plateau
     */
    private Plateau plateau;

    /**
     * @serial Définit l'outil sélectionné dans la barre d'outil par défaut, le tapis
     */
    private Outil outilSelectionne = Outil.TAPIS;

    /**
     * @serial Booléan permettant de mettre en pause le jeu, de base sur false
     */
    private boolean enPause = false;

    /**
     * Constructeur permettant de créer un plateau et d'ajouter la zone de livraison statique
     */
    public Jeu() {
        plateau = new Plateau();
        plateau.setMachine(7, 8, new Livraison());
        start();
    }

    /**
     * Méthode permettant d'inverse l'état du booléan afin de mettre en pause la partie
     */
    public void togglePause() {
        enPause = !enPause;
    }

    /**
     * Fonction permettant de retourner l'état actuel du booléan
     * @return l'état du booléan en pause
     */
    public boolean isEnPause() {
        return enPause;
    }

    /**
     * Méthode permettant de récupérer l'état actuel du plateau
     * @return le plateau actuellement utilisé par le jeu
     */
    public Plateau getPlateau() {
        return plateau;
    }

    /**
     * Permet de retourner l'outil actuellement utilisé
     * @return l'outil actuellement en cours défini dans l'énumération
     */
    public Outil getOutilSelectionne() {
        return outilSelectionne;
    }

    /**
     * Permet de mettre à jour la variable outil pour un nouvel outil sélectionné
     * @param outil définira le nouvel outil à utilisé
     */
    public void setOutilSelectionne(Outil outil) {
        this.outilSelectionne = outil;
    }

    /**
     * Méthode permettant de faire pivoter une machine dans toutes les directions avec une protection si le jeu est en pause
     * @param x définit la coordonnée x de la machine
     * @param y définit la coordonnée y de la machine
     */
    public void tournerMachine(int x, int y) {
        if (enPause) return;
        Machine m = plateau.getCases()[x][y].getMachine();
        if (m != null) {
            m.tourner();
            plateau.mettreAJourCoins(x, y);
        }
    }

    /**
     * Méthode permettant de supprimer une machine en la mettant à null avec une protection si le jeu est en pause ou si la machine est une livraison qu'on ne peut pas supprimer
     * @param x définit la coordonnée x de la machine
     * @param y définit la coordonnée y de la machine
     */
    public void supprimerMachine(int x, int y) {
        if (enPause) return;
        if (plateau.getCases()[x][y].getMachine() instanceof Livraison) return;
        plateau.setMachine(x, y, null);
    }

    /**
     * Permet de mettre en place une machine en clickant en fonction de l'outil choisit avec une protection si le jeu est en pause, ou si la case contient déjà une livraison. Une protection également est faite pour l'outil mine qu'on ne peut poser que sur des gisements
     * @param x indique la coordonnée x de la machine
     * @param y indique la coordonnée y de la machine
     */
    public void press(int x, int y) {
        if (enPause) return;
        if (plateau.getCases()[x][y].getMachine() instanceof Livraison) return;
        if (plateau.getCases()[x][y].getGisement() != null && outilSelectionne != Outil.MINE) return;
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
            case PEINTURE:
                plateau.setMachine(x, y, new Peinture());
                break;
        }
    }

    /**
     * Permet de mettre en place une machine en slidant en fonction de l'outil choisit avec une protection si le jeu est en pause, ou si la case contient déjà une livraison. Une protection également est faite pour l'outil mine qu'on ne peut poser que sur des gisements
     * @param x indique la coordonnée x de la machine
     * @param y indique la coordonnée y de la machine
     */
    public void slide(int x, int y) {
        if (enPause) return;
        if (plateau.getCases()[x][y].getMachine() instanceof Livraison) return;
        if (plateau.getCases()[x][y].getGisement() != null && outilSelectionne != Outil.MINE) return;
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
            case PEINTURE:
                plateau.setMachine(x, y, new Peinture());
                break;
        }
    }

    /**
     * Méthode permettant de lancer une partie
     */
    public void run() {
        jouerPartie();
    }

    /**
     * Méthode permettant de jouer une partie avec une protection pour arrêter le jeu si on fait pause
     */
    public void jouerPartie() {
        while(true) {
            try {
                if(!enPause){
                    plateau.run();
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
