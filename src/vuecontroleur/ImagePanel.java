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