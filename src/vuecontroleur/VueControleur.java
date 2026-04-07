/**
 * Fichier de classe définissant la vue et le contrôleur de l'application
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see vuecontroleur
 * @see javax.swing
 */

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
import modele.jeu.Outil;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * Classe VueControleur héritante de {@link JFrame} et implémentant {@link Observer}.
 * Cette classe remplit deux rôles dans le patron MVC :
 * <ol>
 *     <li><b>Vue</b> : affichage graphique du plateau sous forme d'une grille de {@link ImagePanel},
 *         d'un panneau d'outils sur la gauche, d'une barre de niveau en haut et d'une légende en bas</li>
 *     <li><b>Contrôleur</b> : écoute les événements souris (clic gauche pour placer, clic droit pour supprimer,
 *         glisser pour poser en série) et clavier (R pour tourner, Espace pour pause,
 *         Ctrl+S pour sauvegarder, Ctrl+L pour charger, Escape pour quitter)</li>
 * </ol>
 * La fenêtre est affichée en plein écran sans bordure.
 * L'affichage est rafraîchi automatiquement via le patron {@link Observer} à chaque tick du jeu
 * lorsque le {@link modele.plateau.Plateau} notifie ses observateurs.
 *
 * @see Jeu
 * @see modele.plateau.Plateau
 * @see ImagePanel
 */
public class VueControleur extends JFrame implements Observer {
    /**
     * Taille actuelle en pixels du côté de chaque case de la grille.
     * Modifiée par le zoom (Ctrl + molette) et appliquée à tous les {@link ImagePanel}
     * via {@link ImagePanel#setCellSize(int)}.
     * Initialisée à 60 pixels par défaut.
     */
    private int cellSize = 60;

    /**
     * Taille minimale en pixels autorisée pour une case lors du dézoom.
     * Empêche les cases de devenir trop petites pour être lisibles.
     */
    private static final int CELL_SIZE_MIN = 20;

    /**
     * Taille maximale en pixels autorisée pour une case lors du zoom.
     * Empêche les cases de devenir trop grandes et de consommer trop de mémoire.
     */
    private static final int CELL_SIZE_MAX = 120;

    /**
     * Incrément en pixels appliqué à {@link #cellSize} à chaque cran de molette.
     * Détermine la vitesse du zoom : une valeur plus grande donne un zoom plus rapide.
     */
    private static final int ZOOM_STEP = 5;

    /**
     * Panneau défilant contenant la grille de jeu {@link #grilleIP}.
     * Permet de naviguer sur le plateau lorsque celui-ci dépasse la taille de la fenêtre,
     * notamment après un zoom ou sur un plateau de grande taille.
     * Les barres de défilement s'affichent automatiquement selon les besoins.
     */
    private JScrollPane scrollPane;
    /**
     * @serial Référence vers le plateau de jeu permettant d'accéder aux données du modèle
     *         pour le rafraîchissement et de communiquer les actions souris
     */
    private Plateau plateau;

    /**
     * @serial Référence vers le jeu permettant d'accéder à l'état de la partie
     *         (pause, niveau, outil sélectionné) et de déclencher les actions du joueur
     */
    private Jeu jeu;

    /**
     * @serial Taille horizontale de la grille affichée en nombre de cases
     */
    private final int sizeX;

    /**
     * @serial Taille verticale de la grille affichée en nombre de cases
     */
    private final int sizeY;

    /**
     * Nombre de pixels par case dans la grille d'affichage
     */
    private static final int pxCase = 82;

    /**
     * @serial Image du sprite de couleur bleue, utilisée comme icône par défaut
     */
    private Image icoRouge;

    /**
     * @serial Image du sprite du tapis orienté vers la droite (Est)
     */
    private Image icoTapisDroite;

    /**
     * @serial Image du sprite du tapis orienté vers la gauche (Ouest)
     */
    private Image icoTapisGauche;

    /**
     * @serial Image du sprite du tapis orienté vers le haut (Nord)
     */
    private Image icoTapisHaut;

    /**
     * @serial Image du sprite du tapis orienté vers le bas (Sud)
     */
    private Image icoTapisBas;

    /**
     * @serial Image du sprite de la poubelle
     */
    private Image icoPoubelle;

    /**
     * @serial Image du sprite de la mine orientée vers le haut (Nord)
     */
    private Image icoMineHaut;

    /**
     * @serial Image du sprite de la mine orientée vers le bas (Sud)
     */
    private Image icoMineBas;

    /**
     * @serial Image du sprite de la mine orientée vers la droite (Est)
     */
    private Image icoMineDroit;

    /**
     * @serial Image du sprite de la mine orientée vers la gauche (Ouest)
     */
    private Image icoMineGauche;

    /**
     * @serial Image du sprite de la zone de livraison (hub)
     */
    private Image icoLivraison;

    /**
     * @serial Image du sprite du tapis en coin bas-gauche
     */
    private Image icoTapisBasGauche;

    /**
     * @serial Image du sprite du tapis en coin bas-droite
     */
    private Image icoTapisBasDroite;

    /**
     * @serial Image du sprite du tapis en coin haut-gauche
     */
    private Image icoTapisHautGauche;

    /**
     * @serial Image du sprite du tapis en coin haut-droite
     */
    private Image icoTapisHautDroite;

    /**
     * @serial Image du sprite du rotateur orienté vers le haut (Nord)
     */
    private Image icoRotateurHaut;

    /**
     * @serial Image du sprite du rotateur orienté vers le bas (Sud)
     */
    private Image icoRotateurBas;

    /**
     * @serial Image du sprite du rotateur orienté vers la gauche (Ouest)
     */
    private Image icoRotateurGauche;

    /**
     * @serial Image du sprite du rotateur orienté vers la droite (Est)
     */
    private Image icoRotateurDroit;

    /**
     * @serial Image du sprite du découpeur orienté vers la droite (Est)
     */
    private Image icoDecoupeurDroit;

    /**
     * @serial Image du sprite du découpeur orienté vers la gauche (Ouest)
     */
    private Image icoDecoupeurGauche;

    /**
     * @serial Image du sprite du découpeur orienté vers le haut (Nord)
     */
    private Image icoDecoupeurHaut;

    /**
     * @serial Image du sprite du découpeur orienté vers le bas (Sud)
     */
    private Image icoDecoupeurBas;

    /**
     * @serial Image du sprite de la peinture orientée vers le haut (Nord)
     */
    private Image icoPeintureHaut;

    /**
     * @serial Image du sprite de la peinture orientée vers le bas (Sud)
     */
    private Image icoPeintureBas;

    /**
     * @serial Image du sprite de la peinture orientée vers la gauche (Ouest)
     */
    private Image icoPeintureGauche;

    /**
     * @serial Image du sprite de la peinture orientée vers la droite (Est)
     */
    private Image icoPeintureDroit;

    /**
     * @serial Image du sprite du mélangeur orienté vers le haut (Nord)
     */
    private Image icoMelangeurHaut;

    /**
     * @serial Image du sprite du mélangeur orienté vers le bas (Sud)
     */
    private Image icoMelangeurBas;

    /**
     * @serial Image du sprite du mélangeur orienté vers la gauche (Ouest)
     */
    private Image icoMelangeurGauche;

    /**
     * @serial Image du sprite du mélangeur orienté vers la droite (Est)
     */
    private Image icoMelangeurDroit;

    /**
     * @serial Image du sprite de l'empileur orienté vers le haut (Nord)
     */
    private Image icoEmpileurHaut;

    /**
     * @serial Image du sprite de l'empileur orienté vers le bas (Sud)
     */
    private Image icoEmpileurBas;

    /**
     * @serial Image du sprite de l'empileur orienté vers la gauche (Ouest)
     */
    private Image icoEmpileurGauche;

    /**
     * @serial Image du sprite de l'empileur orienté vers la droite (Est)
     */
    private Image icoEmpileurDroit;

    /**
     * @serial Label affiché au centre de l'écran en rouge quand le jeu est en pause,
     *         utilisé comme glass pane de la fenêtre
     */
    private JLabel labelPause;

    /**
     * @serial Label affiché en haut de la fenêtre indiquant le numéro du niveau
     *         et la description de l'objectif en cours
     */
    private JLabel labelNiveau;

    /**
     * @serial Image du sprite du découpeur (non utilisée, remplacée par les versions directionnelles)
     */
    private Image icoDecoupeur;

    /**
     * @serial Composant contenant la grille de {@link ImagePanel} disposés en {@link GridLayout}
     */
    private JComponent grilleIP;

    /**
     * @serial Booléen mémorisant l'état de la souris (bouton gauche enfoncé) pour le placement
     *         en continu par glissement
     */
    private boolean mousePressed = false;

    /**
     * @serial Coordonnée x de la case actuellement survolée par la souris, -1 si aucune
     */
    private int mouseX = -1;

    /**
     * @serial Coordonnée y de la case actuellement survolée par la souris, -1 si aucune
     */
    private int mouseY = -1;

    /**
     * @serial Tableau à deux dimensions contenant les cases graphiques {@link ImagePanel}
     *         du plateau. Chaque case est associée à une icône de fond et de premier plan
     *         lors du rafraîchissement de l'affichage.
     */
    private ImagePanel[][] tabIP;

    /**
     * Constructeur permettant d'initialiser la vue et le contrôleur du jeu.
     * Charge les icônes, place les composants graphiques, s'abonne comme observateur
     * du plateau et effectue un premier rafraîchissement de l'affichage.
     *
     * @param _jeu le jeu à afficher et contrôler, ne doit pas être null
     */
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

    /**
     * Méthode privée permettant de charger tous les sprites depuis le dossier ./data/sprites/.
     * Charge les icônes directionnelles de chaque machine (4 orientations) ainsi que
     * les icônes des tapis en coin (4 combinaisons).
     */
    private void chargerLesIcones() {
        icoRouge = new ImageIcon("./data/sprites/colors/blue.png").getImage();
        icoPoubelle = new ImageIcon("./data/sprites/buildings/trash.png").getImage();
        icoLivraison = new ImageIcon("./data/sprites/buildings/hub.png").getImage();
        icoMineHaut = new ImageIcon("./data/sprites/buildings/miner.png").getImage();
        icoMineBas = new ImageIcon("./data/sprites/buildings/miner_bot.png").getImage();
        icoMineDroit = new ImageIcon("./data/sprites/buildings/miner_right.png").getImage();
        icoMineGauche = new ImageIcon("./data/sprites/buildings/miner_left.png").getImage();
        icoTapisGauche = new ImageIcon("./data/sprites/buildings/belt_left_straight.png").getImage();
        icoTapisDroite = new ImageIcon("./data/sprites/buildings/belt_right_straight.png").getImage();
        icoTapisHaut = new ImageIcon("./data/sprites/buildings/belt_top.png").getImage();
        icoTapisBas = new ImageIcon("./data/sprites/buildings/belt_bottom.png").getImage();
        icoTapisBasGauche = new ImageIcon("./data/sprites/buildings/belt_left_bottom.png").getImage();
        icoTapisBasDroite = new ImageIcon("./data/sprites/buildings/belt_right_bottom.png").getImage();
        icoTapisHautGauche = new ImageIcon("./data/sprites/buildings/belt_left_top.png").getImage();
        icoTapisHautDroite = new ImageIcon("./data/sprites/buildings/belt_right_top.png").getImage();
        icoRotateurHaut = new ImageIcon("./data/sprites/buildings/rotater.png").getImage();
        icoRotateurBas = new ImageIcon("./data/sprites/buildings/rotater_bas.png").getImage();
        icoRotateurGauche = new ImageIcon("./data/sprites/buildings/rotater_gauche.png").getImage();
        icoRotateurDroit = new ImageIcon("./data/sprites/buildings/rotater_droit.png").getImage();
        icoDecoupeurDroit = new ImageIcon("./data/sprites/buildings/cutter_droit.png").getImage();
        icoDecoupeurGauche = new ImageIcon("./data/sprites/buildings/cutter_gauche.png").getImage();
        icoDecoupeurHaut = new ImageIcon("./data/sprites/buildings/cutter.png").getImage();
        icoDecoupeurBas = new ImageIcon("./data/sprites/buildings/cutter_bas.png").getImage();
        icoPeintureHaut = new ImageIcon("./data/sprites/buildings/painter_haut.png").getImage();
        icoPeintureBas = new ImageIcon("./data/sprites/buildings/painter_bas.png").getImage();
        icoPeintureGauche = new ImageIcon("./data/sprites/buildings/painter_gauche.png").getImage();
        icoPeintureDroit = new ImageIcon("./data/sprites/buildings/painter.png").getImage();
        icoMelangeurHaut = new ImageIcon("./data/sprites/buildings/mixer.png").getImage();
        icoMelangeurBas = new ImageIcon("./data/sprites/buildings/mixer_bas.png").getImage();
        icoMelangeurGauche = new ImageIcon("./data/sprites/buildings/mixer_gauche.png").getImage();
        icoMelangeurDroit = new ImageIcon("./data/sprites/buildings/mixer_droit.png").getImage();
        icoEmpileurHaut = new ImageIcon("./data/sprites/buildings/stacker.png").getImage();
        icoEmpileurBas = new ImageIcon("./data/sprites/buildings/stacker_bas.png").getImage();
        icoEmpileurGauche = new ImageIcon("./data/sprites/buildings/stacker_gauche.png").getImage();
        icoEmpileurDroit = new ImageIcon("./data/sprites/buildings/stacker_droit.png").getImage();
    }

    /**
     * Méthode privée permettant de placer tous les composants graphiques de la fenêtre.
     * Configure la fenêtre en plein écran sans bordure et organise les éléments :
     * <ul>
     *     <li><b>Centre</b> : grille de {@link ImagePanel} avec écouteurs souris sur chaque case</li>
     *     <li><b>Ouest</b> : panneau d'outils avec 8 boutons correspondant aux machines</li>
     *     <li><b>Nord</b> : label affichant le niveau et l'objectif en cours</li>
     *     <li><b>Sud</b> : légende des raccourcis clavier</li>
     * </ul>
     * Configure également les écouteurs clavier :
     * <ul>
     *     <li>R : tourner la machine survolée</li>
     *     <li>Espace : basculer la pause</li>
     *     <li>Ctrl+S : sauvegarder la partie</li>
     *     <li>Ctrl+L : charger une partie</li>
     *     <li>Escape : quitter l'application</li>
     * </ul>
     */
    private void placerLesComposantsGraphiques() {
        setTitle("ShapeCraft JOSE Anthony");
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        grilleIP = new JPanel(new GridLayout(sizeY, sizeX));
        tabIP = new ImagePanel[sizeX][sizeY];
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                ImagePanel iP = new ImagePanel();
                iP.setCellSize(cellSize);
                tabIP[x][y] = iP;
                final int xx = x;
                final int yy = y;
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
                } else if (e.getKeyCode() == KeyEvent.VK_S && e.isControlDown()) {
                    JFileChooser fc = new JFileChooser();
                    fc.setDialogTitle("Sauvegarder la partie");
                    if (fc.showSaveDialog(VueControleur.this) == JFileChooser.APPROVE_OPTION) {
                        try {
                            modele.jeu.Sauvegarde.sauvegarder(jeu, fc.getSelectedFile());
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(VueControleur.this, "Erreur : " + ex.getMessage());
                        }
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_L && e.isControlDown()) {
                    JFileChooser fc = new JFileChooser();
                    fc.setDialogTitle("Charger une partie");
                    if (fc.showOpenDialog(VueControleur.this) == JFileChooser.APPROVE_OPTION) {
                        try {
                            modele.jeu.Sauvegarde.charger(jeu, fc.getSelectedFile());
                            mettreAJourAffichage();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(VueControleur.this, "Erreur : " + ex.getMessage());
                        }
                    }
                }
            }
        });
        setFocusable(true);
        requestFocus();

        JPanel panneauOutils = new JPanel();
        panneauOutils.setLayout(new GridLayout(8, 1));
        JButton btnTapis = new JButton();
        JButton btnMine = new JButton();
        JButton btnPoubelle = new JButton();
        JButton btnRotateur = new JButton();
        JButton btnDecoupeur = new JButton();
        JButton btnPeinture = new JButton();
        JButton btnMelangeur = new JButton();
        JButton btnEmpileur = new JButton();

        int btnIconSize = 170;
        btnRotateur.setIcon(new ImageIcon(new ImageIcon("./data/sprites/buildings/rotater.png").getImage().getScaledInstance(btnIconSize, btnIconSize, java.awt.Image.SCALE_SMOOTH)));
        btnTapis.setIcon(new ImageIcon(new ImageIcon("./data/sprites/buildings/belt_top.png").getImage().getScaledInstance(btnIconSize, btnIconSize, java.awt.Image.SCALE_SMOOTH)));
        btnMine.setIcon(new ImageIcon(new ImageIcon("./data/sprites/buildings/miner.png").getImage().getScaledInstance(btnIconSize, btnIconSize, java.awt.Image.SCALE_SMOOTH)));
        btnPoubelle.setIcon(new ImageIcon(new ImageIcon("./data/sprites/buildings/trash.png").getImage().getScaledInstance(btnIconSize, btnIconSize, java.awt.Image.SCALE_SMOOTH)));
        btnDecoupeur.setIcon(new ImageIcon(new ImageIcon("./data/sprites/buildings/cutter.png").getImage().getScaledInstance(btnIconSize, btnIconSize, java.awt.Image.SCALE_SMOOTH)));
        btnPeinture.setIcon(new ImageIcon(new ImageIcon("./data/sprites/buildings/painter.png").getImage().getScaledInstance(btnIconSize, btnIconSize, java.awt.Image.SCALE_SMOOTH)));
        btnMelangeur.setIcon(new ImageIcon(new ImageIcon("./data/sprites/buildings/mixer.png").getImage().getScaledInstance(btnIconSize, btnIconSize, java.awt.Image.SCALE_SMOOTH)));
        btnEmpileur.setIcon(new ImageIcon(new ImageIcon("./data/sprites/buildings/stacker.png").getImage().getScaledInstance(btnIconSize, btnIconSize, java.awt.Image.SCALE_SMOOTH)));

        btnTapis.addActionListener(e -> jeu.setOutilSelectionne(Outil.TAPIS));
        btnMine.addActionListener(e -> jeu.setOutilSelectionne(Outil.MINE));
        btnPoubelle.addActionListener(e -> jeu.setOutilSelectionne(Outil.POUBELLE));
        btnRotateur.addActionListener(e -> jeu.setOutilSelectionne(Outil.ROTATEUR));
        btnDecoupeur.addActionListener(e -> jeu.setOutilSelectionne(Outil.DECOUPEUR));
        btnPeinture.addActionListener(e -> jeu.setOutilSelectionne(Outil.PEINTURE));
        btnMelangeur.addActionListener(e -> jeu.setOutilSelectionne(Outil.MELANGEUR));
        btnEmpileur.addActionListener(e -> jeu.setOutilSelectionne(Outil.EMPILEUR));

        btnTapis.setFocusable(false);
        btnMine.setFocusable(false);
        btnPoubelle.setFocusable(false);
        btnRotateur.setFocusable(false);
        btnDecoupeur.setFocusable(false);
        btnPeinture.setFocusable(false);
        btnMelangeur.setFocusable(false);
        btnEmpileur.setFocusable(false);

        panneauOutils.add(btnTapis);
        panneauOutils.add(btnMine);
        panneauOutils.add(btnPoubelle);
        panneauOutils.add(btnRotateur);
        panneauOutils.add(btnDecoupeur);
        panneauOutils.add(btnPeinture);
        panneauOutils.add(btnMelangeur);
        panneauOutils.add(btnEmpileur);

        JLabel legende = new JLabel("  Clic droit : Supprimer  |  R : Tourner  |  Clic gauche : Placer  |  Espace : Pause  |  Ctrl+S : Sauvegarder  |  Ctrl+L : Charger  | Escape : Quitter ");
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

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        this.setResizable(false);

        panneauOutils.setPreferredSize(new Dimension(200, 0));
        add(panneauOutils, BorderLayout.WEST);
        scrollPane = new JScrollPane(grilleIP);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        /**
         * Écouteur de molette sur le panneau défilant permettant de zoomer et dézoomer sur la grille.
         * Le zoom est activé uniquement si la touche Ctrl est maintenue enfoncée :
         * <ul>
         *     <li><b>Ctrl + molette haut</b> : augmente la taille des cases (zoom avant)</li>
         *     <li><b>Ctrl + molette bas</b> : diminue la taille des cases (zoom arrière)</li>
         * </ul>
         * Sans Ctrl, le scroll normal du {@link JScrollPane} s'applique.
         *
         * @param e l'événement de molette contenant la direction de rotation
         *          et l'état des modificateurs ({@link MouseWheelEvent#isControlDown()})
         */
        scrollPane.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.isControlDown()) {
                    e.consume();
                    if (e.getWheelRotation() < 0) {
                        cellSize = Math.min(cellSize + ZOOM_STEP, CELL_SIZE_MAX);
                    } else {
                        cellSize = Math.max(cellSize - ZOOM_STEP, CELL_SIZE_MIN);
                    }
                    for (int x = 0; x < sizeX; x++) {
                        for (int y = 0; y < sizeY; y++) {
                            tabIP[x][y].setCellSize(cellSize);
                        }
                    }
                    grilleIP.revalidate();
                    grilleIP.repaint();
                }
            }
        });
    }

    /**
     * Méthode privée permettant de rafraîchir l'affichage de toutes les cases du plateau.
     * Pour chaque case de la grille, met à jour :
     * <ul>
     *     <li>L'image de fond selon le gisement ou la machine présente et son orientation</li>
     *     <li>L'image de premier plan selon l'item de couleur transporté par la machine</li>
     *     <li>La forme vectorielle selon l'item forme transporté par la machine ou le gisement</li>
     *     <li>Le texte pour la zone de livraison (numéro de niveau ou "GG !")</li>
     * </ul>
     * Met également à jour le label de niveau en haut de la fenêtre avec le numéro
     * du niveau en cours et la description de l'objectif, ou un message de félicitations
     * si tous les niveaux sont terminés.
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
    /**
     * Méthode de callback appelée automatiquement par le pattern Observer lorsque le plateau
     * notifie un changement via {@link Plateau#notifyObservers()}.
     * Délègue le rafraîchissement de l'affichage au thread Swing EDT via
     * {@link SwingUtilities#invokeLater(Runnable)} pour garantir la sécurité des threads.
     *
     * @param o l'objet observable qui a notifié le changement (le {@link Plateau})
     * @param arg argument optionnel passé par l'observable (non utilisé)
     */
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
