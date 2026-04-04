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
 * Classe Jeu permettant de gérer une partie.
 * Elle hérite de {@link Thread} pour exécuter la boucle de jeu dans un thread séparé.
 * Le jeu gère la progression des niveaux, le placement des machines sur le plateau,
 * la mise en pause et la sélection des outils.
 * La boucle de jeu exécute un tick toutes les secondes, faisant fonctionner
 * toutes les machines du plateau via {@link Plateau#run()}.
 *
 * @see Plateau
 * @see Niveau
 * @see Outil
 */
public class Jeu extends Thread{
    /**
     * @serial Tableau contenant tous les niveaux du jeu dans l'ordre de progression.
     *         Chaque niveau définit un objectif de forme à livrer.
     */
    private Niveau[] niveaux = {
            new Niveau("CbCbCbCb", "Carre blanc"),
            new Niveau("OrOrOrOr", "Cercle rouge"),
            new Niveau("SpSpSpSp", "Etoile violette")
    };

    /**
     * @serial Index du niveau actuellement en cours, commence à 0 pour le premier niveau.
     *         Incrémenté à chaque livraison réussie via {@link #niveauSuivant()}.
     */
    private int niveauActuel = 0;

    /**
     * @serial Booléen indiquant si tous les niveaux ont été terminés.
     *         Passe à true quand {@link #niveauActuel} dépasse le nombre de niveaux disponibles.
     */
    private boolean partieTerminee = false;

    /**
     * @serial Référence vers le plateau de jeu contenant la grille de cases et les machines.
     */
    private Plateau plateau;

    /**
     * @serial Outil actuellement sélectionné dans la barre d'outils.
     *         Détermine quelle machine sera placée lors d'un clic gauche sur le plateau.
     *         Par défaut initialisé sur {@link Outil#TAPIS}.
     */
    private Outil outilSelectionne = Outil.TAPIS;

    /**
     * @serial Booléen permettant de mettre en pause le jeu.
     *         Quand true, la boucle de jeu n'exécute pas les ticks et le placement de machines est bloqué.
     */
    private boolean enPause = false;

    /**
     * Constructeur permettant de créer un plateau, d'ajouter la zone de livraison
     * statique à la position (7, 8) et de démarrer le thread de la boucle de jeu.
     */
    public Jeu() {
        plateau = new Plateau();
        plateau.setMachine(7, 8, new Livraison(this));
        start();
    }

    /**
     * Fonction permettant de récupérer le niveau actuellement en cours.
     * Retourne null si tous les niveaux ont été terminés.
     *
     * @return le {@link Niveau} courant ou null si la partie est terminée
     */
    public Niveau getNiveauCourant() {
        if (niveauActuel < niveaux.length) {
            return niveaux[niveauActuel];
        }
        return null;
    }

    /**
     * Fonction permettant de savoir si la partie est terminée,
     * c'est-à-dire si tous les niveaux ont été complétés.
     *
     * @return true si tous les niveaux sont terminés, false sinon
     */
    public boolean isPartieTerminee() {
        return partieTerminee;
    }

    /**
     * Méthode permettant de passer au niveau suivant.
     * Incrémente l'index du niveau actuel et marque la partie comme terminée
     * si tous les niveaux ont été complétés.
     * Appelée par {@link Livraison#work()} lorsqu'une forme correcte est livrée.
     */
    public void niveauSuivant() {
        niveauActuel++;
        if (niveauActuel >= niveaux.length) {
            partieTerminee = true;
        }
    }

    /**
     * Fonction permettant de récupérer le numéro du niveau affiché au joueur.
     * Le numéro commence à 1 (et non 0) pour un affichage plus naturel.
     *
     * @return le numéro du niveau courant (base 1)
     */
    public int getNumeroNiveau() {
        return niveauActuel + 1;
    }

    /**
     * Méthode permettant d'inverser l'état de pause du jeu.
     * Si le jeu est en cours, il passe en pause. Si le jeu est en pause, il reprend.
     */
    public void togglePause() {
        enPause = !enPause;
    }

    /**
     * Fonction permettant de retourner l'état actuel de la pause
     * @return true si le jeu est en pause, false sinon
     */
    public boolean isEnPause() {
        return enPause;
    }

    /**
     * Méthode permettant de récupérer le plateau de jeu
     * @return le {@link Plateau} actuellement utilisé par le jeu
     */
    public Plateau getPlateau() {
        return plateau;
    }

    /**
     * Fonction permettant de retourner l'outil actuellement sélectionné dans la barre d'outils
     * @return l'outil actuellement sélectionné défini dans l'énumération {@link Outil}
     */
    public Outil getOutilSelectionne() {
        return outilSelectionne;
    }

    /**
     * Méthode permettant de mettre à jour l'outil sélectionné pour un nouvel outil
     * @param outil le nouvel outil à sélectionner défini dans l'énumération {@link Outil}
     */
    public void setOutilSelectionne(Outil outil) {
        this.outilSelectionne = outil;
    }

    /**
     * Méthode permettant de faire pivoter une machine de 90 degrés dans le sens horaire.
     * Protégée par la pause : ne fait rien si le jeu est en pause.
     * Après la rotation, met à jour les coins des tapis voisins via {@link Plateau#mettreAJourCoins(int, int)}.
     *
     * @param x la coordonnée x de la case contenant la machine à tourner
     * @param y la coordonnée y de la case contenant la machine à tourner
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
     * Méthode permettant de supprimer une machine en la mettant à null.
     * Protégée par la pause : ne fait rien si le jeu est en pause.
     * La zone de {@link Livraison} ne peut pas être supprimée.
     *
     * @param x la coordonnée x de la case contenant la machine à supprimer
     * @param y la coordonnée y de la case contenant la machine à supprimer
     */
    public void supprimerMachine(int x, int y) {
        if (enPause) return;
        if (plateau.getCases()[x][y].getMachine() instanceof Livraison) return;
        plateau.setMachine(x, y, null);
    }

    /**
     * Méthode permettant de placer une machine par clic en fonction de l'outil sélectionné.
     * Protégée par la pause : ne fait rien si le jeu est en pause.
     * Protections supplémentaires :
     * <ul>
     *     <li>Impossible de placer sur une case contenant une {@link Livraison}</li>
     *     <li>Impossible de placer une machine autre que {@link Mine} sur un gisement</li>
     * </ul>
     *
     * @param x la coordonnée x de la case où placer la machine
     * @param y la coordonnée y de la case où placer la machine
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
            case MELANGEUR:
                plateau.setMachine(x, y, new Melangeur());
                break;
            case EMPILEUR:
                plateau.setMachine(x, y, new Empileur());
                break;
        }
    }

    /**
     * Méthode permettant de définir directement le niveau actuel.
     * Utilisée par le système de chargement de sauvegarde {@link Sauvegarde#charger(Jeu, java.io.File)}.
     * Met également à jour l'état de fin de partie en fonction de la valeur passée.
     *
     * @param n l'index du niveau à définir (base 0)
     */
    public void setNiveauActuel(int n) {
        this.niveauActuel = n;
        this.partieTerminee = (n >= niveaux.length);
    }

    /**
     * Méthode permettant de placer une machine en glissant la souris (drag) en fonction de l'outil sélectionné.
     * Fonctionne de manière identique à {@link #press(int, int)} mais est appelée lors du survol
     * d'une case avec le bouton gauche de la souris maintenu enfoncé.
     * Les mêmes protections de pause, de livraison et de gisement s'appliquent.
     *
     * @param x la coordonnée x de la case où placer la machine
     * @param y la coordonnée y de la case où placer la machine
     * @see #press(int, int)
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
            case MELANGEUR:
                plateau.setMachine(x, y, new Melangeur());
                break;
            case EMPILEUR:
                plateau.setMachine(x, y, new Empileur());
                break;
        }
    }

    /**
     * Méthode de démarrage du thread, appelle {@link #jouerPartie()} pour lancer la boucle de jeu
     */
    public void run() {
        jouerPartie();
    }

    /**
     * Méthode contenant la boucle principale du jeu.
     * Exécute un tick toutes les secondes en appelant {@link Plateau#run()}
     * qui fait fonctionner toutes les machines du plateau.
     * La boucle est protégée par les états de pause et de fin de partie :
     * si le jeu est en pause ou terminé, le tick n'est pas exécuté mais le thread continue de tourner.
     *
     * @throws RuntimeException si le thread est interrompu pendant le sleep
     */
    public void jouerPartie() {
        while(true) {
            try {
                if(!enPause && !partieTerminee){
                    plateau.run();
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
