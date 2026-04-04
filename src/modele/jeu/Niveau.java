/**
 * Fichier de classe définissant un niveau du jeu
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.jeu
 */
package modele.jeu;

/**
 * Classe représentant un niveau du jeu avec un objectif de forme à livrer.
 * Chaque niveau possède un code objectif correspondant au code d'un {@link modele.item.ItemShape}
 * que le joueur doit produire et livrer à la zone de {@link modele.plateau.Livraison}.
 * La description est un texte lisible affiché dans l'interface pour indiquer au joueur
 * ce qu'il doit produire.
 *
 * @see Jeu#getNiveauCourant()
 * @see modele.plateau.Livraison
 */
public class Niveau {
    /**
     * @serial Code de la forme objectif à livrer, correspondant au format de codage
     *         de {@link modele.item.ItemShape} (ex: "CbCbCbCb" pour un carré blanc complet)
     */
    private final String codeObjectif;

    /**
     * @serial Description lisible du niveau affichée dans l'interface graphique
     *         (ex: "Carré blanc", "Cercle rouge")
     */
    private final String description;

    /**
     * Constructeur permettant d'initialiser un niveau avec son objectif et sa description
     * @param codeObjectif le code de la forme à livrer respectant le format de {@link modele.item.ItemShape}
     * @param description la description lisible du niveau affichée au joueur
     */
    public Niveau(String codeObjectif, String description) {
        this.codeObjectif = codeObjectif;
        this.description = description;
    }

    /**
     * Fonction permettant de retourner le code objectif du niveau.
     * Ce code est comparé avec le code de la forme livrée par le joueur
     * dans la méthode {@link modele.plateau.Livraison#work()}.
     *
     * @return le code de la forme objectif sous forme de String
     */
    public String getCodeObjectif() {
        return codeObjectif;
    }

    /**
     * Fonction permettant de retourner la description lisible du niveau
     * @return la description du niveau sous forme de String
     */
    public String getDescription() {
        return description;
    }
}