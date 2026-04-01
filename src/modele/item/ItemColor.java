package modele.item;

/**
 * Classe ItemColor éritant de Item permettant de définir les formes colorés
 */
public class ItemColor extends Item {
    Color color; // Définit l'attribut couleur de la forme

    /**
     * Constructeur permettant d'initialiser un ItemColor
     * @param color est la couleur de l'initialisation de l'item
     */
    public ItemColor(Color color){
        this.color=color; // Affecte la couleur à l'objet au moment de sa création
    }

    /**
     * Fonction permetttant de renvoyer la couleur actuelle de l'objet
     * @return la couleur de l'item
     */
    public Color getColor(){
        return color; // Retourne la couleur de l'objet
    }

    /**
     * Fonction utilisé par le mixer permettant de faire varier la couleur de l'objet selon les règles RGB
     * @param add est la couleur que l'on veut mixer avec celle actuellement de l'item
     */
    public void transform(Color add) { // faire varier la couleur suivant la couleur ajoutée
    }
}
