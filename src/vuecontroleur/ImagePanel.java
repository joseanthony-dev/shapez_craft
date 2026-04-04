/**
 * Fichier de classe définissant le composant graphique d'une case du plateau
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see vuecontroleur
 * @see javax.swing.JPanel
 */
package vuecontroleur;

import modele.item.ItemShape;
import modele.item.SubShape;
import javax.swing.*;
import java.awt.*;

/**
 * Classe ImagePanel héritante de {@link JPanel} représentant une case graphique du plateau.
 * Chaque case peut afficher jusqu'à quatre couches visuelles superposées :
 * <ol>
 *     <li>Un arrière-plan ({@link #imgBackground}) : image du gisement ou de la machine</li>
 *     <li>Un texte centré ({@link #texte}) : utilisé pour afficher le numéro de niveau sur la livraison</li>
 *     <li>Un premier plan ({@link #imgFront}) : image de l'item couleur transporté par la machine</li>
 *     <li>Une forme ({@link #shape}) : rendu graphique d'un {@link ItemShape} avec ses quadrants et couches</li>
 * </ol>
 * Le rendu des formes utilise {@link Graphics2D} avec antialiasing pour dessiner les 4 types de
 * sous-formes ({@link SubShape}) : carré, cercle, étoile et éventail, chacune dans son quadrant.
 * Les couches supérieures sont dessinées légèrement plus petites pour créer un effet de profondeur.
 *
 * @see VueControleur
 * @see modele.item.ItemShape
 * @see modele.item.SubShape
 */
public class ImagePanel extends JPanel {
    /**
     * @serial Image d'arrière-plan de la case, représente le gisement ou la machine posée.
     *         Peut être null si la case est vide.
     */
    private Image imgBackground;

    /**
     * @serial Image de premier plan de la case, représente l'item couleur ({@link modele.item.ItemColor})
     *         actuellement transporté par la machine. Peut être null si aucun item couleur n'est présent.
     */
    private Image imgFront;

    /**
     * @serial Forme colorée à dessiner sur la case via le rendu graphique par quadrants.
     *         Utilisée pour afficher les {@link ItemShape} sur les gisements et les machines.
     *         Peut être null si aucune forme n'est à afficher.
     */
    private ItemShape shape;

    /**
     * @serial Texte à afficher centré sur la case en blanc et en gras.
     *         Utilisé pour afficher le numéro de niveau sur la zone de {@link modele.plateau.Livraison}.
     *         Peut être null si aucun texte n'est à afficher.
     */
    private String texte;

    /**
     * Méthode permettant de définir la forme à dessiner sur la case
     * @param _shape la forme colorée à afficher, ou null pour ne rien afficher
     */
    public void setShape(ItemShape _shape) {
        shape = _shape;
    }

    /**
     * Méthode permettant de définir l'image d'arrière-plan de la case
     * @param _imgBackground l'image de fond à afficher, ou null pour un fond vide
     */
    public void setBackground(Image _imgBackground) {
        imgBackground = _imgBackground;
    }

    /**
     * Méthode permettant de définir l'image de premier plan de la case
     * @param _imgFront l'image de premier plan à afficher, ou null pour ne rien afficher
     */
    public void setFront(Image _imgFront) {
        imgFront = _imgFront;
    }

    /**
     * Méthode permettant de définir le texte à afficher centré sur la case
     * @param _texte le texte à afficher, ou null pour ne rien afficher
     */
    public void setTexte(String _texte) {
        texte = _texte;
    }

    /**
     * Méthode de rendu graphique redéfinie pour dessiner le contenu de la case.
     * Le dessin s'effectue dans l'ordre suivant :
     * <ol>
     *     <li>Cadre arrondi autour de la case avec une bordure de 1 pixel</li>
     *     <li>Image d'arrière-plan ({@link #imgBackground}) occupant toute la case</li>
     *     <li>Texte centré ({@link #texte}) en blanc, police Arial gras 16px</li>
     *     <li>Image de premier plan ({@link #imgFront}) centrée dans la moitié intérieure de la case</li>
     *     <li>Forme colorée ({@link #shape}) dessinée quadrant par quadrant pour chaque couche</li>
     * </ol>
     * Pour le rendu des formes, chaque couche est dessinée avec un rayon légèrement réduit
     * (4 pixels de moins par couche) pour créer un effet d'empilement visuel.
     * Les 4 quadrants sont ordonnés : 0 = haut-droit, 1 = bas-droit, 2 = bas-gauche, 3 = haut-gauche.
     * <p>
     * Types de sous-formes rendus :
     * <ul>
     *     <li>{@link SubShape#Carre} : rectangle rempli dans le quadrant</li>
     *     <li>{@link SubShape#Circle} : arc de 90 degrés clippé dans le quadrant</li>
     *     <li>{@link SubShape#Star} : polygone à 4 sommets formant une branche d'étoile</li>
     *     <li>{@link SubShape#Fan} : arc de 90 degrés décalé formant un éventail</li>
     * </ul>
     *
     * @param g le contexte graphique fourni par Swing pour le dessin
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final int bordure= 1;
        final int xBack = bordure;
        final int yBack = bordure;
        final int widthBack = getWidth() - bordure*2;
        final int heigthBack = getHeight() - bordure*2;
        final int subPartWidth = widthBack / 4;
        final int subPartHeigth = heigthBack / 4;
        final int xFront = bordure + subPartWidth;
        final int yFront = bordure + subPartHeigth;
        final int widthFront = subPartWidth*2;
        final int heigthFront = subPartHeigth*2;
        // cadre
        g.drawRoundRect(bordure, bordure, widthBack, heigthBack, bordure, bordure);
        if (imgBackground != null) {
            g.drawImage(imgBackground, xBack, yBack, widthBack, heigthBack, this);
        }
        if (texte != null) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            FontMetrics fm = g.getFontMetrics();
            int textX = xBack + (widthBack - fm.stringWidth(texte)) / 2;
            int textY = yBack + (heigthBack + fm.getAscent()) / 2;
            g.drawString(texte, textX, textY);
        }
        if (imgFront != null) {
            g.drawImage(imgFront, xFront, yFront, widthFront, heigthFront, this);
        }
        if (shape != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            ItemShape.Layer[] allLayers = {ItemShape.Layer.one, ItemShape.Layer.two, ItemShape.Layer.three};
            for (int layerIdx = 0; layerIdx < shape.getLayerCount(); layerIdx++) {
                int shrink = layerIdx * 4;
                int centerX = xFront + widthFront / 2;
                int centerY = yFront + heigthFront / 2;
                int r = Math.min(widthFront, heigthFront) / 2 - shrink;

                SubShape[] tabS = shape.getSubShapes(allLayers[layerIdx]);
                modele.item.Color[] tabC = shape.getColors(allLayers[layerIdx]);
                int[] circleStartAngles = {0, 270, 180, 90};

                for (int i = 0; i < 4; i++) {
                    SubShape ss = tabS[i];
                    if (ss != SubShape.None && tabC[i] != null) {
                        switch (tabC[i]) {
                            case Red:    g2d.setColor(Color.RED); break;
                            case White:  g2d.setColor(Color.WHITE); break;
                            case Green:  g2d.setColor(Color.GREEN); break;
                            case Blue:   g2d.setColor(Color.BLUE); break;
                            case Yellow: g2d.setColor(Color.YELLOW); break;
                            case Purple: g2d.setColor(new Color(128, 0, 128)); break;
                            case Cyan:   g2d.setColor(Color.CYAN); break;
                        }

                        switch (ss) {
                            case Carre:
                                int gap = 2;
                                int qx = (i == 0 || i == 1) ? centerX + gap : centerX - r;
                                int qy = (i == 0 || i == 3) ? centerY - r : centerY + gap;
                                g2d.fillRect(qx, qy, r - gap, r - gap);
                                break;
                            case Circle:
                                int cGap = 2;
                                int clipX = (i == 0 || i == 1) ? centerX + cGap : centerX - r;
                                int clipY = (i == 0 || i == 3) ? centerY - r : centerY + cGap;
                                Shape oldClip = g2d.getClip();
                                g2d.setClip(clipX, clipY, r - cGap, r - cGap);
                                g2d.fillArc(centerX - r, centerY - r, 2 * r, 2 * r, circleStartAngles[i], 90);
                                g2d.setClip(oldClip);
                                break;
                            case Star:
                                int narrow = (int)(r * 0.35);
                                int[] sx, sy;
                                switch(i) {
                                    case 0:  sx = new int[]{centerX, centerX+narrow, centerX+r, centerX}; sy = new int[]{centerY, centerY, centerY-r, centerY-narrow}; break;
                                    case 1:  sx = new int[]{centerX, centerX+narrow, centerX+r, centerX}; sy = new int[]{centerY, centerY, centerY+r, centerY+narrow}; break;
                                    case 2:  sx = new int[]{centerX, centerX-narrow, centerX-r, centerX}; sy = new int[]{centerY, centerY, centerY+r, centerY+narrow}; break;
                                    default: sx = new int[]{centerX, centerX-narrow, centerX-r, centerX}; sy = new int[]{centerY, centerY, centerY-r, centerY-narrow}; break;
                                }
                                g2d.fillPolygon(sx, sy, 4);
                                break;
                            case Fan:
                                switch(i) {
                                    case 0: g2d.fillArc(centerX, centerY-r, 2*r, 2*r, 90, 90); break;
                                    case 1: g2d.fillArc(centerX-r, centerY, 2*r, 2*r, 0, 90); break;
                                    case 2: g2d.fillArc(centerX-2*r, centerY-r, 2*r, 2*r, 270, 90); break;
                                    case 3: g2d.fillArc(centerX-r, centerY-2*r, 2*r, 2*r, 180, 90); break;
                                }
                                break;
                        }
                    }
                }
            }
        }
    }
}