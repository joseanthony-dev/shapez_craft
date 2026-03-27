package vuecontroleur;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import modele.item.Item;
import modele.item.ItemColor;
import modele.item.ItemShape;
import modele.jeu.Jeu;
import modele.plateau.*;
// Ajout pour importer le type enum outil
import modele.jeu.Outil;

/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle
 *
 */
public class VueControleur extends JFrame implements Observer {
    private Plateau plateau; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)
    private Jeu jeu;
    private final int sizeX; // taille de la grille affichée
    private final int sizeY;
    private static final int pxCase = 82; // nombre de pixel par case
    // icones affichées dans la grille
    private Image icoRouge;
    // Définission des icônes selon l'orientation
    private Image icoTapisDroite;
    private Image icoTapisGauche;
    private Image icoTapisHaut;
    private Image icoTapisBas;
    private Image icoPoubelle;
    private Image icoMineHaut;
    private Image icoMineBas;
    private Image icoMineDroit;
    private Image icoMineGauche;
    private Image icoLivraison;
    //Icones pour les tapis de coins
    private Image icoTapisBasGauche;
    private Image icoTapisBasDroite;
    private Image icoTapisHautGauche;
    private Image icoTapisHautDroite;
    //Icones pour le rond
    private Image icoRotateur;
    private Image icoDecoupeur;
    private JComponent grilleIP;
    private boolean mousePressed = false; // permet de mémoriser l'état de la souris
    private ImagePanel[][] tabIP; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône background et front, suivant ce qui est présent dans le modèle)

    public VueControleur(Jeu _jeu) {
        jeu = _jeu;
        plateau = jeu.getPlateau();
        sizeX = plateau.SIZE_X;
        sizeY = plateau.SIZE_Y;
        chargerLesIcones();
        placerLesComposantsGraphiques();
        plateau.addObserver(this);
        mettreAJourAffichage();
    }

    private void chargerLesIcones() {
        icoRouge = new ImageIcon("./data/sprites/colors/blue.png").getImage();
        icoPoubelle = new ImageIcon("./data/sprites/buildings/trash.png").getImage();
        icoLivraison = new ImageIcon("./data/sprites/buildings/hub.png").getImage();
        icoMineHaut = new ImageIcon("./data/sprites/buildings/miner.png").getImage();
        icoMineBas = new ImageIcon("./data/sprites/buildings/miner_bot.png").getImage();
        icoMineDroit = new ImageIcon("./data/sprites/buildings/miner_right.png").getImage();
        icoMineGauche = new ImageIcon("./data/sprites/buildings/miner_left.png").getImage();
        // Icônes pour les différents tapis
        icoTapisGauche = new ImageIcon("./data/sprites/buildings/belt_left_straight.png").getImage();
        icoTapisDroite = new ImageIcon("./data/sprites/buildings/belt_right_straight.png").getImage();
        icoTapisHaut = new ImageIcon("./data/sprites/buildings/belt_top.png").getImage();
        icoTapisBas = new ImageIcon("./data/sprites/buildings/belt_bottom.png").getImage();
        //Icones des tapis en coins
        icoTapisBasGauche = new ImageIcon("./data/sprites/buildings/belt_left_bottom.png").getImage();
        icoTapisBasDroite = new ImageIcon("./data/sprites/buildings/belt_right_bottom.png").getImage();
        icoTapisHautGauche = new ImageIcon("./data/sprites/buildings/belt_left_top.png").getImage();
        icoTapisHautDroite = new ImageIcon("./data/sprites/buildings/belt_right_top.png").getImage();
        //Icones de la rotation
        icoRotateur = new ImageIcon("./data/sprites/buildings/rotater.png").getImage();
        icoDecoupeur = new ImageIcon("./data/sprites/buildings/cutter.png").getImage();
    }

    private void placerLesComposantsGraphiques() {
        setTitle("ShapeCraft");
        setResizable(true);
        setSize(sizeX * pxCase, sizeX * pxCase);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre
        setLayout(new BorderLayout()); // utiliser pour inclure notre boite à outil en bordure de layout
        grilleIP = new JPanel(new GridLayout(sizeY, sizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille
        tabIP = new ImagePanel[sizeX][sizeY];
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                ImagePanel iP = new ImagePanel();
                tabIP[x][y] = iP; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                final int xx = x; // permet de compiler la classe anonyme ci-dessous
                final int yy = y;
                // écouteur de clics
                iP.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON3) {
                            mousePressed = false;
                            jeu.tournerMachine(xx,yy);
                            System.out.println(xx + "-" + yy);
                        }
                    }
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (mousePressed) {
                            jeu.slide(xx, yy);
                        }
                    }
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            mousePressed = true;
                            jeu.press(xx, yy);
                        }
                    }
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        mousePressed = false;
                    }
                });
                grilleIP.add(iP);
            }
        }

        JPanel panneauOutils = new JPanel(); // On définit notre panneau
        panneauOutils.setLayout(new GridLayout(6, 1)); // On met une colonne et 4 item pour l'instant
        JButton btnTapis = new JButton(); // On créer notre bouton pour le tapis
        JButton btnMine = new JButton(); // On créer notre bouton pour la mine
        JButton btnPoubelle = new JButton(); // On créer notre bouton pour la poubelle
        JButton btnSupprimer = new JButton(); // On créer notre bouton pour supprimer une machine
        JButton btnRotateur = new JButton(); // On créer notre bouton pour sélectionner le rotateur
        JButton btnDecoupeur = new JButton(); // On créer notre bouton pour sélectionner le découpeur

        btnRotateur.setIcon(new ImageIcon("./data/sprites/buildings/rotater.png"));
        btnTapis.setIcon(new ImageIcon("./data/sprites/buildings/belt_top.png"));
        btnMine.setIcon(new ImageIcon("./data/sprites/buildings/miner.png"));
        btnPoubelle.setIcon(new ImageIcon("./data/sprites/buildings/trash.png"));
        btnDecoupeur.setIcon(new ImageIcon("./data/sprites/buildings/cutter.png"));
        btnSupprimer.setIcon(new ImageIcon("./data/sprites/misc/slot_bad_arrow.png"));

        btnTapis.addActionListener(e -> jeu.setOutilSelectionne(Outil.TAPIS)); // On ajoute notre listener sur le bouton, si il est cliqué on met l'outil sur le tapis
        btnMine.addActionListener(e -> jeu.setOutilSelectionne(Outil.MINE)); // On ajoute notre listener sur le bouton, si il est cliqué on met l'outil sur la mine
        btnPoubelle.addActionListener(e -> jeu.setOutilSelectionne(Outil.POUBELLE)); // On ajoute notre listener sur le bouton, si il est cliqué on met l'outil sur la poubelle
        btnSupprimer.addActionListener(e -> jeu.setOutilSelectionne(Outil.SUPPRIMER)); // On ajoute notre listener sur le bouton, si il est cliqué on met l'outil sur la suppression
        btnRotateur.addActionListener(e -> jeu.setOutilSelectionne(Outil.ROTATEUR)); // On ajoute notre listener sur le bouton, si il est cliqué on met l'outil sur le rotateur
        btnDecoupeur.addActionListener(e -> jeu.setOutilSelectionne(Outil.DECOUPEUR)); // On ajoute notre listener sur notre bouton de découpage

        panneauOutils.add(btnTapis); // On incorpore notre bouton tapis dans le panneau créer
        panneauOutils.add(btnMine); // On incorpore notre bouton mine dans le panneau créer
        panneauOutils.add(btnPoubelle); // On incorpore notre bouton poubelle dans le panneau créer
        panneauOutils.add(btnSupprimer); // On incorpore notre bouton supprimer dans le panneau créer
        panneauOutils.add(btnRotateur); // On incorpore notre bouton rotation dans le panneau créer
        panneauOutils.add(btnDecoupeur); // On incorpore notre bouton découpeur dans le panneau créer

        add(panneauOutils, BorderLayout.WEST); // On ajoute notre panneau d'outils à la bordure gauche
        add(grilleIP, BorderLayout.CENTER); // On centre désormais notre grille car la boite à outil est ajouté sur la bordure gauche
    }

    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabIP)
     */
    private void mettreAJourAffichage() {
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                tabIP[x][y].setBackground((Image) null);
                tabIP[x][y].setFront(null);
                tabIP[x][y].setTexte(null);
                Case c = plateau.getCases()[x][y];
                Machine m = c.getMachine();
                if (m != null) {
                    if (m instanceof Tapis) {
                        Tapis t = (Tapis) m;
                        if(t.getCoinEntree() != null){
                            Direction s = t.getDirection();
                            Direction e = t.getCoinEntree();
                            if ((s == Direction.North && e == Direction.West) || (s == Direction.West && e == Direction.North))
                                tabIP[x][y].setBackground(icoTapisBasGauche);
                            else if ((s == Direction.North && e == Direction.East) || (s == Direction.East && e == Direction.North))
                                tabIP[x][y].setBackground(icoTapisBasDroite);
                            else if ((s == Direction.South && e == Direction.West) || (s == Direction.West && e == Direction.South))
                                tabIP[x][y].setBackground(icoTapisHautGauche);
                            else if ((s == Direction.South && e == Direction.East) || (s == Direction.East && e == Direction.South))
                                tabIP[x][y].setBackground(icoTapisHautDroite);
                        }else{
                            switch (m.getDirection()) {
                                case North:
                                    tabIP[x][y].setBackground(icoTapisHaut);
                                    break;
                                case East:
                                    tabIP[x][y].setBackground(icoTapisDroite);
                                    break;
                                case West:
                                    tabIP[x][y].setBackground(icoTapisGauche);
                                    break;
                                case South:
                                    tabIP[x][y].setBackground(icoTapisBas);
                                    break;
                            }
                        }
                    } else if (m instanceof Rotateur) {
                        tabIP[x][y].setBackground(icoRotateur);
                    } else if (m instanceof Decoupeur) {
                        tabIP[x][y].setBackground(icoDecoupeur);
                    } else if (m instanceof Poubelle) {
                        tabIP[x][y].setBackground(icoPoubelle);
                    } else if (m instanceof Livraison) {
                        tabIP[x][y].setBackground(icoLivraison);
                        tabIP[x][y].setTexte(String.valueOf(((Livraison) m).getCompteur()));
                    } else if (m instanceof Mine) {
                        switch (m.getDirection()){
                            case North:
                                tabIP[x][y].setBackground(icoMineHaut);
                                break;
                            case East:
                                tabIP[x][y].setBackground(icoMineDroit);
                                break;
                            case West:
                                tabIP[x][y].setBackground(icoMineGauche);
                                break;
                            case South:
                                tabIP[x][y].setBackground(icoMineBas);
                                break;
                        }
                    }
                    Item current = m.getCurrent();
                    if (current instanceof ItemShape) {
                        tabIP[x][y].setShape((ItemShape) current);
                    }
                    if (current instanceof ItemColor) {
                        // tabIP[x][y].setFront(); TODO : placer l'icone des couleurs approprié
                    }
                }
            }
        }
        grilleIP.repaint();
    }
    @Override
    public void update(Observable o, Object arg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mettreAJourAffichage();
            }
        });
    }
}
