package vuecontroleur;

import modele.item.ItemShape;
import modele.item.SubShape;
import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {

    private Image imgBackground;
    private Image imgFront;
    private ItemShape shape;
    private String texte;

    public void setShape(ItemShape _shape) {
        shape = _shape;
    }

    public void setBackground(Image _imgBackground) {
        imgBackground = _imgBackground;
    }

    public void setFront(Image _imgFront) {
        imgFront = _imgFront;
    }

    public void setTexte(String _texte) {
        texte = _texte;
    }

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
            // TODO autres layers
            SubShape[] tabS = shape.getSubShapes(ItemShape.Layer.one);
            modele.item.Color[] tabC = shape.getColors(ItemShape.Layer.one);
            for (int i = 0; i < 4; i++) {
                SubShape ss = tabS[i];
                // création des couleurs
                if (ss != SubShape.None) {
                    switch (tabC[i]) {
                        case modele.item.Color.Red:
                            g.setColor(Color.RED);
                            break;
                        case modele.item.Color.White:
                            g.setColor(Color.WHITE);
                            break;
                        case modele.item.Color.Green:
                            g.setColor(Color.GREEN);
                            break;
                        case modele.item.Color.Blue:
                            g.setColor(Color.BLUE);
                            break;
                        case modele.item.Color.Yellow:
                            g.setColor(Color.YELLOW);
                            break;
                        case modele.item.Color.Purple:
                            g.setColor(new Color(128, 0, 128));
                            break;
                        case modele.item.Color.Cyan:
                            g.setColor(Color.CYAN);
                            break;
                    }
                    // création des autres formes
                    switch (ss) {
                        case SubShape.Carre:
                            g.fillRect(xFront + (widthFront / 2) * ((i >> 1) ^ 1), yFront + (heigthFront / 2) * ((i & 1) ^ ((i >> 1) & 1)), widthFront / 2, heigthFront / 2);
                            break;
                        case Circle:
                            g.fillOval(xFront + (widthFront / 2) * ((i >> 1) ^ 1), yFront + (heigthFront / 2) * ((i & 1) ^ ((i >> 1) & 1)), widthFront / 2, heigthFront / 2);
                            break;
                        case Star:
                            int sx = xFront + (widthFront / 2) * ((i >> 1) ^ 1);
                            int sy = yFront + (heigthFront / 2) * ((i & 1) ^ ((i >> 1) & 1));
                            int sw = widthFront / 2;
                            int sh = heigthFront / 2;
                            g.fillPolygon(
                                    new int[]{sx + sw/2, sx + sw, sx + sw/2, sx},
                                    new int[]{sy, sy + sh/2, sy + sh, sy + sh/2},
                                    4
                            );
                            break;
                        case Fan:
                            g.fillArc(xFront + (widthFront / 2) * ((i >> 1) ^ 1), yFront + (heigthFront / 2) * ((i & 1) ^ ((i >> 1) & 1)), widthFront / 2, heigthFront / 2, 0, 90);
                            break;
                    }
                }
            }
        }
    }
}