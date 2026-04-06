/**
 * Fichier de classe définissant le plateau de jeu
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.plateau
 * @see java.util.Observable
 */
package modele.plateau;

import java.util.HashMap;
import java.util.Observable;
import modele.item.ItemColor;
import modele.item.Color;
import modele.item.ItemShape;

/**
 * Classe Plateau héritante de {@link Observable} et implémentant {@link Runnable},
 * représentant la grille de jeu de taille {@value #SIZE_X} x {@value #SIZE_Y}.
 * Le plateau contient un tableau bidimensionnel de {@link Case} et une HashMap
 * associant chaque case à sa position ({@link Point}) pour permettre la navigation
 * entre cases voisines.
 * <p>
 * À l'initialisation, le plateau place :
 * <ul>
 *     <li>4 gisements de couleur (Rouge, Blanc, Bleu, Vert) en bas à droite (colonne 15, lignes 12 à 15)</li>
 *     <li>4 gisements de forme (Carré, Cercle, Étoile, Éventail) en bas à gauche (colonne 0, lignes 12 à 15)</li>
 * </ul>
 * À chaque tick, la méthode {@link #run()} parcourt toutes les cases et exécute
 * les machines présentes, puis notifie les observateurs (la vue) du changement.
 *
 * @see Case
 * @see Machine
 * @see Point
 */
public class Plateau extends Observable implements Runnable {
    /**
     * Largeur du plateau en nombre de cases.
     * Définit le nombre de colonnes de la grille de jeu.
     * Les gisements de couleur sont placés à la colonne {@code SIZE_X - 1} (49)
     * et les gisements de forme à la colonne 0.
     */
    public static final int SIZE_X = 50;

    /**
     * Hauteur du plateau en nombre de cases.
     * Définit le nombre de lignes de la grille de jeu.
     * Les gisements sont placés sur les 4 dernières lignes ({@code SIZE_Y - 4} à {@code SIZE_Y - 1}).
     */
    public static final int SIZE_Y = 50;

    /**
     * @serial HashMap permettant de récupérer la position ({@link Point}) d'une case
     *         à partir de sa référence. Utilisée par {@link #getCase(Case, Direction)}
     *         pour calculer les cases voisines.
     */
    private HashMap<Case, Point> map = new HashMap<Case, Point>();

    /**
     * @serial Tableau bidimensionnel de cases représentant la grille du plateau.
     *         L'accès se fait par grilleCases[x][y] où x est la colonne et y la ligne.
     */
    private Case[][] grilleCases = new Case[SIZE_X][SIZE_Y];

    /**
     * Constructeur permettant d'initialiser le plateau avec une grille vide
     * et les gisements prédéfinis via {@link #initPlateauVide()}.
     */
    public Plateau() {
        initPlateauVide();
    }

    /**
     * Fonction permettant de retourner le tableau bidimensionnel de cases du plateau.
     * Utilisée par le jeu et la vue pour accéder aux cases et à leur contenu.
     *
     * @return le tableau de cases de taille {@value #SIZE_X} x {@value #SIZE_Y}
     */
    public Case[][] getCases() {
        return grilleCases;
    }

    /**
     * Fonction permettant de récupérer la case voisine d'une case source dans une direction donnée.
     * Utilise la HashMap {@link #map} pour trouver la position de la case source,
     * puis calcule la position voisine en ajoutant les décalages de la direction.
     * Retourne null si la case voisine est en dehors des limites du plateau.
     *
     * @param source la case de départ dont on cherche le voisin
     * @param d la direction dans laquelle chercher la case voisine
     * @return la case voisine dans la direction donnée, ou null si hors limites
     */
    public Case getCase(Case source, Direction d) {
        Point p = map.get(source);
        return caseALaPosition(new Point(p.x+d.dx, p.y+d.dy));
    }

    /**
     * Méthode privée permettant d'initialiser le plateau avec des cases vides
     * et de placer les gisements fixes.
     * Crée une case pour chaque position de la grille, l'enregistre dans la HashMap,
     * puis place les 4 gisements de couleur en bas à droite et les 4 gisements de forme en bas à gauche.
     */
    private void initPlateauVide() {
        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                grilleCases[x][y] = new Case(this);
                map.put(grilleCases[x][y], new Point(x, y));
            }
        }
        // On ajoute un gisement fixe de chaque couleur
        grilleCases[15][12].setGisement(new ItemColor(Color.Red));
        grilleCases[15][13].setGisement(new ItemColor(Color.White));
        grilleCases[15][14].setGisement(new ItemColor(Color.Blue));
        grilleCases[15][15].setGisement(new ItemColor(Color.Green));
        // On ajoute des gisements de formes
        grilleCases[0][12].setGisement(new ItemShape("CbCbCbCb"));
        grilleCases[0][13].setGisement(new ItemShape("ObObObOb"));
        grilleCases[0][14].setGisement(new ItemShape("Sb--Sb--"));
        grilleCases[0][15].setGisement(new ItemShape("FbFbFbFb"));
    }

    /**
     * Méthode permettant de poser ou retirer une machine sur une case du plateau.
     * Après le placement, met à jour la détection des coins des tapis voisins
     * via {@link #mettreAJourCoins(int, int)} et notifie les observateurs (la vue)
     * pour rafraîchir l'affichage.
     *
     * @param x la coordonnée x de la case (colonne)
     * @param y la coordonnée y de la case (ligne)
     * @param m la machine à poser, ou null pour retirer la machine existante
     */
    public void setMachine(int x, int y, Machine m) {
        grilleCases[x][y].setMachine(m);
        mettreAJourCoins(x, y);
        setChanged();
        notifyObservers();
    }

    /**
     * Fonction privée permettant de vérifier si un point est contenu dans les limites de la grille.
     *
     * @param p le point à vérifier
     * @return true si le point est dans la grille (0 ≤ x < {@value #SIZE_X} et 0 ≤ y < {@value #SIZE_Y})
     */
    private boolean contenuDansGrille(Point p) {
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }

    /**
     * Fonction privée permettant de récupérer la case à une position donnée.
     * Retourne null si la position est en dehors des limites de la grille.
     *
     * @param p le point représentant la position de la case à récupérer
     * @return la case à la position donnée, ou null si hors limites
     */
    private Case caseALaPosition(Point p) {
        Case retour = null;
        if (contenuDansGrille(p)) {
            retour = grilleCases[p.x][p.y];
        }
        return retour;
    }

    /**
     * Méthode permettant de mettre à jour la détection des coins des tapis autour d'une position.
     * Parcourt les 4 cases adjacentes (Nord, Sud, Est, Ouest) et appelle
     * {@link Tapis#detecterCoin()} sur chaque tapis trouvé, ainsi que sur le tapis
     * éventuellement présent à la position elle-même.
     * Cela permet aux tapis de détecter automatiquement s'ils doivent devenir des coins
     * lorsqu'un tapis voisin pointe vers eux latéralement.
     *
     * @param x la coordonnée x de la case centrale
     * @param y la coordonnée y de la case centrale
     */
    public void mettreAJourCoins(int x, int y) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (Math.abs(dx) + Math.abs(dy) != 1) continue;
                int nx = x + dx, ny = y + dy;
                if (contenuDansGrille(new Point(nx, ny)) && grilleCases[nx][ny].getMachine() instanceof Tapis)
                    ((Tapis) grilleCases[nx][ny].getMachine()).detecterCoin();
            }
        }
        if (grilleCases[x][y].getMachine() instanceof Tapis)
            ((Tapis) grilleCases[x][y].getMachine()).detecterCoin();
    }

    /**
     * Méthode d'exécution appelée à chaque tick du jeu par {@link modele.jeu.Jeu#jouerPartie()}.
     * Parcourt toutes les cases du plateau et exécute la méthode {@link Machine#run()}
     * de chaque machine présente. Après l'exécution de toutes les machines,
     * notifie les observateurs (la vue) pour rafraîchir l'affichage.
     */
    @Override
    public void run() {
        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                Case c = grilleCases[x][y];
                if (c.getMachine() != null) {
                    c.getMachine().run();
                }
            }
        }
        setChanged();
        notifyObservers();
    }
}