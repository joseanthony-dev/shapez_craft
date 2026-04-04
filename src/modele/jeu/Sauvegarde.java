/**
 * Fichier de classe définissant le système de sauvegarde et chargement
 * @author JOSE Anthony
 * @since 2026-04-04
 * @version 1.0
 * @see modele.jeu
 * @see modele.plateau
 */
package modele.jeu;

import modele.plateau.*;
import java.io.*;

/**
 * Classe utilitaire permettant de sauvegarder et charger l'état du jeu dans un fichier texte.
 * Le format de sauvegarde est le suivant :
 * <ul>
 *     <li>Première ligne : "NIVEAU:" suivi du numéro du niveau en cours (base 1)</li>
 *     <li>Lignes suivantes : une ligne par machine au format "x,y,TYPE,DIRECTION"</li>
 * </ul>
 * Exemple de fichier :
 * <pre>
 *     NIVEAU:1
 *     3,5,TAPIS,North
 *     3,6,MINE,East
 *     7,5,POUBELLE,North
 * </pre>
 * La zone de {@link modele.plateau.Livraison} n'est pas sauvegardée car elle est toujours
 * replacée automatiquement à la position fixe (7, 8) lors du chargement.
 * Les items en transit sur les machines ne sont pas sauvegardés car ils sont éphémères.
 *
 * @see Jeu
 * @see modele.plateau.Plateau
 * @see modele.plateau.Machine
 */
public class Sauvegarde {

    /**
     * Méthode permettant de sauvegarder l'état complet du jeu dans le fichier spécifié.
     * Parcourt toutes les cases du plateau et écrit chaque machine trouvée
     * (sauf la {@link modele.plateau.Livraison}) avec sa position, son type et sa direction.
     * Le numéro du niveau en cours est écrit en première ligne.
     *
     * @param jeu le jeu dont on veut sauvegarder l'état, ne doit pas être null
     * @param fichier le fichier de destination dans lequel écrire la sauvegarde
     * @throws IOException en cas d'erreur d'écriture dans le fichier
     */
    public static void sauvegarder(Jeu jeu, File fichier) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fichier))) {
            pw.println("NIVEAU:" + jeu.getNumeroNiveau());
            Plateau plateau = jeu.getPlateau();
            Case[][] cases = plateau.getCases();
            for (int x = 0; x < Plateau.SIZE_X; x++) {
                for (int y = 0; y < Plateau.SIZE_Y; y++) {
                    Machine m = cases[x][y].getMachine();
                    if (m == null || m instanceof Livraison) continue;
                    String type = null;
                    if (m instanceof Tapis) type = "TAPIS";
                    else if (m instanceof Mine) type = "MINE";
                    else if (m instanceof Poubelle) type = "POUBELLE";
                    else if (m instanceof Rotateur) type = "ROTATEUR";
                    else if (m instanceof Decoupeur) type = "DECOUPEUR";
                    else if (m instanceof Peinture) type = "PEINTURE";
                    else if (m instanceof Melangeur) type = "MELANGEUR";
                    else if (m instanceof Empileur) type = "EMPILEUR";
                    if (type != null) {
                        pw.println(x + "," + y + "," + type + "," + m.getDirection().name());
                    }
                }
            }
        }
    }

    /**
     * Méthode permettant de charger l'état du jeu depuis le fichier spécifié.
     * Le chargement se déroule en trois étapes :
     * <ol>
     *     <li>Suppression de toutes les machines existantes sur le plateau</li>
     *     <li>Lecture du fichier ligne par ligne pour restaurer le niveau et les machines</li>
     *     <li>Replacement de la {@link modele.plateau.Livraison} à sa position fixe (7, 8)</li>
     * </ol>
     * Chaque machine est recréée puis orientée dans la bonne direction en appelant
     * {@link modele.plateau.Machine#tourner()} autant de fois que nécessaire.
     * Les coins des tapis sont automatiquement recalculés par {@link modele.plateau.Plateau#setMachine(int, int, Machine)}.
     *
     * @param jeu le jeu dont on veut restaurer l'état, ne doit pas être null
     * @param fichier le fichier source contenant la sauvegarde à charger
     * @throws IOException en cas d'erreur de lecture du fichier
     */
    public static void charger(Jeu jeu, File fichier) throws IOException {
        Plateau plateau = jeu.getPlateau();
        Case[][] cases = plateau.getCases();
        for (int x = 0; x < Plateau.SIZE_X; x++) {
            for (int y = 0; y < Plateau.SIZE_Y; y++) {
                cases[x][y].setMachine(null);
            }
        }
        try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                ligne = ligne.trim();
                if (ligne.startsWith("NIVEAU:")) {
                    int niveau = Integer.parseInt(ligne.substring(7));
                    jeu.setNiveauActuel(niveau - 1);
                } else if (!ligne.isEmpty()) {
                    String[] parts = ligne.split(",");
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);
                    String type = parts[2];
                    Direction dir = Direction.valueOf(parts[3]);
                    Machine m = null;
                    switch (type) {
                        case "TAPIS": m = new Tapis(); break;
                        case "MINE": m = new Mine(); break;
                        case "POUBELLE": m = new Poubelle(); break;
                        case "ROTATEUR": m = new Rotateur(); break;
                        case "DECOUPEUR": m = new Decoupeur(); break;
                        case "PEINTURE": m = new Peinture(); break;
                        case "MELANGEUR": m = new Melangeur(); break;
                        case "EMPILEUR": m = new Empileur(); break;
                    }
                    if (m != null) {
                        while (m.getDirection() != dir) {
                            m.tourner();
                        }
                        plateau.setMachine(x, y, m);
                    }
                }
            }
        }
        plateau.setMachine(7, 8, new Livraison(jeu));
    }
}