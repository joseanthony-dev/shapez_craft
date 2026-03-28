/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;
import java.util.HashMap;
import java.util.Observable;
import modele.item.ItemColor;
import modele.item.Color;
import modele.item.ItemShape;

public class Plateau extends Observable implements Runnable {
    public static final int SIZE_X = 16;
    public static final int SIZE_Y = 16;
    private HashMap<Case, Point> map = new HashMap<Case, Point>(); // permet de récupérer la position d'une case à partir de sa référence
    private Case[][] grilleCases = new Case[SIZE_X][SIZE_Y]; // permet de récupérer une case à partir de ses coordonnées

    public Plateau() {
        initPlateauVide();
    }

    public Case[][] getCases() {
        return grilleCases;
    }

    public Case getCase(Case source, Direction d) {
        Point p = map.get(source);
        return caseALaPosition(new Point(p.x+d.dx, p.y+d.dy));
    }

    private void initPlateauVide() {
        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                grilleCases[x][y] = new Case(this);
                map.put(grilleCases[x][y], new Point(x, y));
            }
        }
        // On ajoute un gisement fixe de chaque couleure
        grilleCases[10][10].setGisement(new ItemColor(Color.Red));
        grilleCases[10][11].setGisement(new ItemColor(Color.White));
        grilleCases[10][12].setGisement(new ItemColor(Color.Blue));
        grilleCases[10][13].setGisement(new ItemColor(Color.Cyan));
        grilleCases[11][10].setGisement(new ItemColor(Color.Green));
        grilleCases[11][12].setGisement(new ItemColor(Color.Purple));
        grilleCases[11][13].setGisement(new ItemColor(Color.Yellow));
        // On ajoute des gisements de formes
        grilleCases[5][10].setGisement(new ItemShape("CrCr CrCr"));  // carré rouge complet
        grilleCases[5][11].setGisement(new ItemShape("OuOu OuOu"));  // cercle bleu complet
        grilleCases[5][12].setGisement(new ItemShape("Sg--Sg--"));   // étoile verte demi
        grilleCases[5][13].setGisement(new ItemShape("FyFyFyFy"));   // fan jaune complet
    }

    public void setMachine(int x, int y, Machine m) {
        grilleCases[x][y].setMachine(m);
        // On met à jour les coins pour détecter les tapis de coins
        mettreAJourCoins(x, y);
        setChanged();
        notifyObservers();
    }

    /**
     * Indique si p est contenu dans la grille
     */
    private boolean contenuDansGrille(Point p) {
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }

    private Case caseALaPosition(Point p) {
        Case retour = null;

        if (contenuDansGrille(p)) {
            retour = grilleCases[p.x][p.y];
        }
        return retour;
    }

    /**
     * Fonction permettant de mettre à jour les coins pour détecter les tapis et les mettres en coin
     * @param x
     * @param y
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
        // aussi le tapis posé lui-même
        if (grilleCases[x][y].getMachine() instanceof Tapis)
            ((Tapis) grilleCases[x][y].getMachine()).detecterCoin();
    }

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