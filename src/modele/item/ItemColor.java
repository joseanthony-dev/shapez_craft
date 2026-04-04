/**
 * Fichier de classe des items colorés
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.item
 */
package modele.item;

/**
 * Classe ItemColor héritante de Item permettant de définir un item de type couleur.
 * Les ItemColor sont produits par les mines placées sur des gisements de couleur
 * et sont utilisés par la machine {@link modele.plateau.Peinture} pour colorier les formes
 * et par la machine {@link modele.plateau.Melangeur} pour créer de nouvelles couleurs.
 *
 * @see Item
 * @see Color
 * @see modele.plateau.Peinture
 * @see modele.plateau.Melangeur
 */
public class ItemColor extends Item {
    /**
     * @serial Définit la couleur de l'item, correspondant à une valeur de l'énumération {@link Color}
     */
    Color color;

    /**
     * Constructeur permettant d'initialiser un ItemColor avec une couleur donnée
     * @param color la couleur à attribuer à l'item, ne doit pas être null
     */
    public ItemColor(Color color){
        this.color=color;
    }

    /**
     * Fonction permettant de renvoyer la couleur actuelle de l'objet
     * @return la couleur de l'item sous forme de valeur de l'énumération {@link Color}
     */
    public Color getColor(){
        return color;
    }

    /**
     * Méthode utilisée par le mélangeur permettant de transformer la couleur de l'item
     * en la mélangeant avec une autre couleur selon les règles RGB suivantes :
     * <ul>
     *     <li>Rouge + Bleu = Violet</li>
     *     <li>Rouge + Vert = Jaune</li>
     *     <li>Bleu + Vert = Cyan</li>
     *     <li>Blanc + n'importe quelle couleur = Blanc</li>
     *     <li>Même couleur + même couleur = pas de changement</li>
     *     <li>Toute autre combinaison = Blanc</li>
     * </ul>
     *
     * @param add la couleur que l'on veut mélanger avec la couleur actuelle de l'item
     */
    public void transform(Color add) {
        if (add == color) return;
        if (add == Color.White || color == Color.White) {
            color = Color.White; return;
        }
        // Rouge + Bleu = Violet
        if ((color == Color.Red && add == Color.Blue) || (color == Color.Blue && add == Color.Red)) {
            color = Color.Purple; return;
        }
        // Rouge + Vert = Jaune
        if ((color == Color.Red && add == Color.Green) || (color == Color.Green && add == Color.Red)) {
            color = Color.Yellow; return;
        }
        // Bleu + Vert = Cyan
        if ((color == Color.Blue && add == Color.Green) || (color == Color.Green && add == Color.Blue)) {
            color = Color.Cyan; return;
        }
        // Toute autre combinaison => Blanc
        color = Color.White;
    }
}
