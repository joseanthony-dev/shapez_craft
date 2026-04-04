package modele.jeu;

import modele.plateau.*;
import java.io.*;

public class Sauvegarde {

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