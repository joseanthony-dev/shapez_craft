package vuecontroleur;
import java.awt.*;
import java.awt.event.KeyEvent;
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

    private Image icoRotateurHaut;
    private Image icoRotateurBas;
    private Image icoRotateurGauche;
    private Image icoRotateurDroit;

    private Image icoDecoupeurDroit;
    private Image icoDecoupeurGauche;
    private Image icoDecoupeurHaut;
    private Image icoDecoupeurBas;

    private Image icoPeintureHaut;
    private Image icoPeintureBas;
    private Image icoPeintureGauche;
    private Image icoPeintureDroit;

    private Image icoMelangeurHaut;
    private Image icoMelangeurBas;
    private Image icoMelangeurGauche;
    private Image icoMelangeurDroit ;

    private Image icoEmpileurHaut;
    private Image icoEmpileurBas;
    private Image icoEmpileurGauche;
    private Image icoEmpileurDroit;

    // pour montrer que c'est en pause
    private JLabel labelPause;

    // pour la livraison
    private JLabel labelNiveau;

    //Icones pour le rond
    private Image icoDecoupeur;
    private JComponent grilleIP;
    private boolean mousePressed = false; // permet de mémoriser l'état de la souris
    private int mouseX = -1, mouseY = -1;
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
        icoRotateurHaut = new ImageIcon("./data/sprites/buildings/rotater.png").getImage();
        icoRotateurBas = new ImageIcon("./data/sprites/buildings/rotater_bas.png").getImage();
        icoRotateurGauche = new ImageIcon("./data/sprites/buildings/rotater_gauche.png").getImage();
        icoRotateurDroit = new ImageIcon("./data/sprites/buildings/rotater_droit.png").getImage();
        //Icones du découpeur
        icoDecoupeurDroit = new ImageIcon("./data/sprites/buildings/cutter_droit.png").getImage();
        icoDecoupeurGauche = new ImageIcon("./data/sprites/buildings/cutter_gauche.png").getImage();
        icoDecoupeurHaut = new ImageIcon("./data/sprites/buildings/cutter.png").getImage();
        icoDecoupeurBas = new ImageIcon("./data/sprites/buildings/cutter_bas.png").getImage();
        //Icones de l'atelier de peinture
        icoPeintureHaut = new ImageIcon("./data/sprites/buildings/painter_haut.png").getImage();
        icoPeintureBas = new ImageIcon("./data/sprites/buildings/painter_bas.png").getImage();
        icoPeintureGauche = new ImageIcon("./data/sprites/buildings/painter_gauche.png").getImage();
        icoPeintureDroit = new ImageIcon("./data/sprites/buildings/painter.png").getImage();
        // Icones du mixer
        icoMelangeurHaut = new ImageIcon("./data/sprites/buildings/mixer.png").getImage();
        icoMelangeurBas = new ImageIcon("./data/sprites/buildings/mixer_bas.png").getImage();
        icoMelangeurGauche = new ImageIcon("./data/sprites/buildings/mixer_gauche.png").getImage();
        icoMelangeurDroit = new ImageIcon("./data/sprites/buildings/mixer_droit.png").getImage();
        // Icones de l'empileur
        icoEmpileurHaut = new ImageIcon("./data/sprites/buildings/stacker.png").getImage();
        icoEmpileurBas = new ImageIcon("./data/sprites/buildings/stacker_bas.png").getImage();
        icoEmpileurGauche = new ImageIcon("./data/sprites/buildings/stacker_gauche.png").getImage();
        icoEmpileurDroit = new ImageIcon("./data/sprites/buildings/stacker_droit.png").getImage();
    }

    private void placerLesComposantsGraphiques() {
        setTitle("ShapeCraft JOSE Anthony");
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
                            jeu.supprimerMachine(xx,yy);
                        }
                    }
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        mouseX = xx;
                        mouseY = yy;
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
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_R && mouseX >= 0 && mouseY >= 0) {
                    jeu.tournerMachine(mouseX, mouseY);
                } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_SPACE) {
                    jeu.togglePause();
                    labelPause.setVisible(jeu.isEnPause());
                    // Si la touche échap est préssée, on tue l'application
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            }
        });
        setFocusable(true);
        requestFocus();

        JPanel panneauOutils = new JPanel(); // On définit notre panneau
        panneauOutils.setLayout(new GridLayout(8, 1)); // On met une colonne et 4 item pour l'instant
        JButton btnTapis = new JButton(); // On créer notre bouton pour le tapis
        JButton btnMine = new JButton(); // On créer notre bouton pour la mine
        JButton btnPoubelle = new JButton(); // On créer notre bouton pour la poubelle
        JButton btnRotateur = new JButton(); // On créer notre bouton pour sélectionner le rotateur
        JButton btnDecoupeur = new JButton(); // On créer notre bouton pour sélectionner le découpeur
        JButton btnPeinture = new JButton(); // On créer notre bouton pour la peinture
        JButton btnMelangeur = new JButton(); // On créer notre bouton pour le mixer
        JButton btnEmpileur = new JButton();

        btnRotateur.setIcon(new ImageIcon("./data/sprites/buildings/rotater.png"));
        btnTapis.setIcon(new ImageIcon("./data/sprites/buildings/belt_top.png"));
        btnMine.setIcon(new ImageIcon("./data/sprites/buildings/miner.png"));
        btnPoubelle.setIcon(new ImageIcon("./data/sprites/buildings/trash.png"));
        btnDecoupeur.setIcon(new ImageIcon("./data/sprites/buildings/cutter.png"));
        btnPeinture.setIcon(new ImageIcon("./data/sprites/buildings/painter.png"));
        btnMelangeur.setIcon(new ImageIcon("./data/sprites/buildings/mixer.png"));
        btnEmpileur.setIcon(new ImageIcon("./data/sprites/buildings/stacker.png"));

        btnTapis.addActionListener(e -> jeu.setOutilSelectionne(Outil.TAPIS)); // On ajoute notre listener sur le bouton, si il est cliqué on met l'outil sur le tapis
        btnMine.addActionListener(e -> jeu.setOutilSelectionne(Outil.MINE)); // On ajoute notre listener sur le bouton, si il est cliqué on met l'outil sur la mine
        btnPoubelle.addActionListener(e -> jeu.setOutilSelectionne(Outil.POUBELLE)); // On ajoute notre listener sur le bouton, si il est cliqué on met l'outil sur la poubelle
        btnRotateur.addActionListener(e -> jeu.setOutilSelectionne(Outil.ROTATEUR)); // On ajoute notre listener sur le bouton, si il est cliqué on met l'outil sur le rotateur
        btnDecoupeur.addActionListener(e -> jeu.setOutilSelectionne(Outil.DECOUPEUR)); // On ajoute notre listener sur notre bouton de découpage
        btnPeinture.addActionListener(e -> jeu.setOutilSelectionne(Outil.PEINTURE)); // On ajoute notre listener sur notre bouton de peinture
        btnMelangeur.addActionListener(e -> jeu.setOutilSelectionne(Outil.MELANGEUR)); // On ajoute notre listener sur notre bouton mixer
        btnEmpileur.addActionListener(e -> jeu.setOutilSelectionne(Outil.EMPILEUR));

        // Permet d'éviter des soucis de focus pour la touche R de rotation
        btnTapis.setFocusable(false);
        btnMine.setFocusable(false);
        btnPoubelle.setFocusable(false);
        btnRotateur.setFocusable(false);
        btnDecoupeur.setFocusable(false);
        btnPeinture.setFocusable(false);
        btnMelangeur.setFocusable(false);
        btnEmpileur.setFocusable(false);

        panneauOutils.add(btnTapis); // On incorpore notre bouton tapis dans le panneau créer
        panneauOutils.add(btnMine); // On incorpore notre bouton mine dans le panneau créer
        panneauOutils.add(btnPoubelle); // On incorpore notre bouton poubelle dans le panneau créer
        panneauOutils.add(btnRotateur); // On incorpore notre bouton rotation dans le panneau créer
        panneauOutils.add(btnDecoupeur); // On incorpore notre bouton découpeur dans le panneau créer
        panneauOutils.add(btnPeinture); // On incorpore notre bouton peubture dans le panneau créer
        panneauOutils.add(btnMelangeur); // On incorpore notre bouton mixer dans le panneau créer
        panneauOutils.add(btnEmpileur);

        // Jlabel (légende des touches de l'application ajouter au sud en bas de la fenetre)
        JLabel legende = new JLabel("  Clic droit : Supprimer  |  R : Tourner  |  Clic gauche : Placer  |  Espace : Pause  | Escape : Quitter ");
        legende.setHorizontalAlignment(SwingConstants.CENTER);
        legende.setFont(new Font("Arial", Font.BOLD, 13));
        legende.setBorder(BorderFactory.createEtchedBorder());
        add(legende, BorderLayout.SOUTH);

        labelPause = new JLabel("JEU EN PAUSE", SwingConstants.CENTER);
        labelPause.setFont(new Font("Arial", Font.BOLD, 60));
        labelPause.setForeground(java.awt.Color.RED);
        labelPause.setVisible(false);
        setGlassPane(labelPause);

        labelNiveau = new JLabel("", SwingConstants.CENTER);
        labelNiveau.setFont(new Font("Arial", Font.BOLD, 16));
        labelNiveau.setBorder(BorderFactory.createEtchedBorder());
        add(labelNiveau, BorderLayout.NORTH);

        // On met la fenêtre en plein écran
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // On enlève les boutons réduire, agrandir et fermer par défault sous Windows
        this.setUndecorated(true);
        // On empêche le redimenssionement de la fenêtre
        this.setResizable(false);

        // On réduit la taille de la boite à outils
        panneauOutils.setPreferredSize(new Dimension(250, 0));
        add(panneauOutils, BorderLayout.WEST); // On ajoute notre panneau d'outils à la bordure gauche
        add(grilleIP, BorderLayout.CENTER); // On centre désormais notre grille car la boite à outil est ajouté sur la bordure gauche
    }

    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabIP)
     */
    private void mettreAJourAffichage() {
        if (jeu.isPartieTerminee()) {
            labelNiveau.setText("BRAVO ! Tous les niveaux sont termines !");
        } else {
            labelNiveau.setText("Niveau " + jeu.getNumeroNiveau() + " — Objectif : "
                    + jeu.getNiveauCourant().getDescription());
        }
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                tabIP[x][y].setBackground((Image) null);
                tabIP[x][y].setFront(null);
                tabIP[x][y].setTexte(null);
                tabIP[x][y].setShape(null);
                Case c = plateau.getCases()[x][y];
                // Afficher les images d'arriere plan sur les cases
                Item gisement = c.getGisement();
                if (gisement instanceof ItemColor) {
                    ItemColor ic = (ItemColor) gisement;
                    switch (ic.getColor()) {
                        case Red:    tabIP[x][y].setBackground(new ImageIcon("./data/sprites/colors/red.png").getImage()); break;
                        case Green:  tabIP[x][y].setBackground(new ImageIcon("./data/sprites/colors/green.png").getImage()); break;
                        case Blue:   tabIP[x][y].setBackground(new ImageIcon("./data/sprites/colors/blue.png").getImage()); break;
                        case Yellow: tabIP[x][y].setBackground(new ImageIcon("./data/sprites/colors/yellow.png").getImage()); break;
                        case Purple: tabIP[x][y].setBackground(new ImageIcon("./data/sprites/colors/purple.png").getImage()); break;
                        case Cyan:   tabIP[x][y].setBackground(new ImageIcon("./data/sprites/colors/cyan.png").getImage()); break;
                        case White:  tabIP[x][y].setBackground(new ImageIcon("./data/sprites/colors/white.png").getImage()); break;
                    }
                }
                if (gisement instanceof ItemShape) {
                    tabIP[x][y].setShape((ItemShape) gisement);
                }
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
                        switch (m.getDirection()) {
                            case North:
                                tabIP[x][y].setBackground(icoRotateurHaut);
                                break;
                            case East:
                                tabIP[x][y].setBackground(icoRotateurDroit);
                                break;
                            case West:
                                tabIP[x][y].setBackground(icoRotateurGauche);
                                break;
                            case South:
                                tabIP[x][y].setBackground(icoRotateurBas);
                                break;
                        }
                    } else if (m instanceof Decoupeur) {
                        switch (m.getDirection()) {
                            case North:
                                tabIP[x][y].setBackground(icoDecoupeurHaut);
                                break;
                            case East:
                                tabIP[x][y].setBackground(icoDecoupeurDroit);
                                break;
                            case West:
                                tabIP[x][y].setBackground(icoDecoupeurGauche);
                                break;
                            case South:
                                tabIP[x][y].setBackground(icoDecoupeurBas);
                                break;
                        }
                    } else if (m instanceof Poubelle) {
                        tabIP[x][y].setBackground(icoPoubelle);
                    } else if (m instanceof Livraison) {
                        tabIP[x][y].setBackground(icoLivraison);
                        if (jeu.isPartieTerminee()) {
                            tabIP[x][y].setTexte("GG !");
                        } else {
                            tabIP[x][y].setTexte("Niv." + jeu.getNumeroNiveau());
                        }
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
                    } else if (m instanceof Peinture) {
                        switch (m.getDirection()) {
                            case North:
                                tabIP[x][y].setBackground(icoPeintureHaut);
                                break;
                            case East:
                                tabIP[x][y].setBackground(icoPeintureDroit);
                                break;
                            case West:
                                tabIP[x][y].setBackground(icoPeintureGauche);
                                break;
                            case South:
                                tabIP[x][y].setBackground(icoPeintureBas);
                                break;
                        }
                    } else if (m instanceof Melangeur) {
                        switch (m.getDirection()) {
                            case North: tabIP[x][y].setBackground(icoMelangeurHaut); break;
                            case East:  tabIP[x][y].setBackground(icoMelangeurDroit); break;
                            case West:  tabIP[x][y].setBackground(icoMelangeurGauche); break;
                            case South: tabIP[x][y].setBackground(icoMelangeurBas); break;
                        }
                    } else if (m instanceof Empileur) {
                        switch (m.getDirection()) {
                            case North:
                                tabIP[x][y].setBackground(icoEmpileurHaut);
                                break;
                            case East:
                                tabIP[x][y].setBackground(icoEmpileurDroit);
                                break;
                            case West:
                                tabIP[x][y].setBackground(icoEmpileurGauche);
                                break;
                            case South:
                                tabIP[x][y].setBackground(icoEmpileurBas);
                                break;
                        }
                    }
                    Item current = m.getCurrent();
                    if (current instanceof ItemShape) {
                        tabIP[x][y].setShape((ItemShape) current);
                    }
                    // affichage des couleurs transporté par les machines
                    if (current instanceof ItemColor) {
                        ItemColor ic = (ItemColor) current;
                        switch (ic.getColor()) {
                            case Red:    tabIP[x][y].setFront(new ImageIcon("./data/sprites/colors/red.png").getImage()); break;
                            case Green:  tabIP[x][y].setFront(new ImageIcon("./data/sprites/colors/green.png").getImage()); break;
                            case Blue:   tabIP[x][y].setFront(new ImageIcon("./data/sprites/colors/blue.png").getImage()); break;
                            case Yellow: tabIP[x][y].setFront(new ImageIcon("./data/sprites/colors/yellow.png").getImage()); break;
                            case Purple: tabIP[x][y].setFront(new ImageIcon("./data/sprites/colors/purple.png").getImage()); break;
                            case Cyan:   tabIP[x][y].setFront(new ImageIcon("./data/sprites/colors/cyan.png").getImage()); break;
                            case White:  tabIP[x][y].setFront(new ImageIcon("./data/sprites/colors/white.png").getImage()); break;
                        }
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
