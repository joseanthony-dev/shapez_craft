/**
 * Fichier de classe des items colorés
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.item
 */
package modele.item;

/**
 * Classe ItemColor éritante de Item permettant de définir les formes colorées
 */
public class ItemColor extends Item {
    /**
     * @serial Définit l'attribut couleur de la forme
     */
    Color color;

    /**
     * Constructeur permettant d'initialiser un ItemColor
     * @param color est la couleur de l'initialisation de l'item
     */
    public ItemColor(Color color){
        this.color=color;
    }

    /**
     * Fonction permettant de renvoyer la couleur actuelle de l'objet
     * @return la couleur de l'item
     */
    public Color getColor(){
        return color;
    }

    /**
     * Fonction utilisée par le mixer permettant de faire varier la couleur de l'objet selon les règles RGB
     * @param add est la couleur que l'on veut mixer avec celle actuellement de l'item
     */
    public void transform(Color add) { // faire varier la couleur suivant la couleur ajoutée
    }
}
