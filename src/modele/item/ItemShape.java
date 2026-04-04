/**
 * Fichier de classe des items de type forme colorée
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.item
 */
package modele.item;

/**
 * Classe héritante de Item, permettant de définir une forme colorée composée de quadrants.
 * Chaque forme est constituée de jusqu'à {@value #MAX_LAYERS} couches, chacune contenant 4 quadrants.
 * Chaque quadrant possède une sous-forme ({@link SubShape}) et une couleur ({@link Color}).
 * Les quadrants sont ordonnés : index 0 = haut-droit, 1 = bas-droit, 2 = bas-gauche, 3 = haut-gauche.
 * <p>
 * Le codage en chaîne de caractères suit le format : (lettre forme + lettre couleur) x 4 quadrants,
 * avec le séparateur ':' entre chaque couche. Exemple : "CbCbCbCb" = carré blanc complet sur 1 couche,
 * "CrCr--:--ObOb" = carrés rouges en haut sur couche 1 et cercles bleus en bas sur couche 2.
 * Le caractère '-' indique l'absence de forme ou de couleur dans un quadrant.
 *
 * @see Item
 * @see SubShape
 * @see Color
 */
public class ItemShape extends Item {
    /**
     * Nombre maximum de couches empilables sur une forme
     */
    public static final int MAX_LAYERS = 3;

    /**
     * @serial Tableau à deux dimensions stockant les sous-formes de chaque quadrant pour chaque couche.
     *         La première dimension représente la couche (0 à {@value #MAX_LAYERS}-1),
     *         la seconde dimension représente le quadrant (0 = haut-droit, 1 = bas-droit, 2 = bas-gauche, 3 = haut-gauche).
     */
    private SubShape[][] tabSubShapes;

    /**
     * @serial Tableau à deux dimensions stockant les couleurs de chaque quadrant pour chaque couche.
     *         La première dimension représente la couche (0 à {@value #MAX_LAYERS}-1),
     *         la seconde dimension représente le quadrant (0 = haut-droit, 1 = bas-droit, 2 = bas-gauche, 3 = haut-gauche).
     *         Une valeur null indique l'absence de couleur (quadrant vide).
     */
    private Color[][] tabColors;

    /**
     * Enumération définissant les trois couches possibles d'un item forme
     */
    public enum Layer {one, two, three};

    /**
     * @serial Chaîne de caractères permettant de stocker le code sérialisé de la forme colorée.
     *         Ce code est utilisé pour comparer les formes avec les objectifs des niveaux.
     */
    private String code;

    /**
     * Fonction permettant de retourner un nouveau tableau constitué des 4 sous-formes d'une couche donnée.
     * Le tableau retourné est une copie et ne modifie pas l'état interne de l'item.
     *
     * @param l la couche dont on veut récupérer les sous-formes ({@link Layer#one}, {@link Layer#two} ou {@link Layer#three})
     * @return un tableau de taille 4 contenant les 4 sous-formes de la couche
     * @throws IllegalStateException si la couche passée en paramètre n'est pas reconnue
     */
    public SubShape[] getSubShapes(Layer l) {
        switch(l) {
            case one : return new SubShape[] {tabSubShapes[0][0], tabSubShapes[0][1], tabSubShapes[0][2], tabSubShapes[0][3]};
            case two :   return new SubShape[] {tabSubShapes[1][0], tabSubShapes[1][1], tabSubShapes[1][2], tabSubShapes[1][3]};
            case three : return new SubShape[] {tabSubShapes[2][0], tabSubShapes[2][1], tabSubShapes[2][2], tabSubShapes[2][3]};
            default:
                throw new IllegalStateException("Unexpected value: " + l);
        }
    }

    /**
     * Fonction permettant de retourner un nouveau tableau constitué des 4 couleurs d'une couche donnée.
     * Le tableau retourné est une copie et ne modifie pas l'état interne de l'item.
     *
     * @param l la couche dont on veut récupérer les couleurs ({@link Layer#one}, {@link Layer#two} ou {@link Layer#three})
     * @return un tableau de taille 4 contenant les 4 couleurs de la couche, null si le quadrant est vide
     * @throws IllegalStateException si la couche passée en paramètre n'est pas reconnue
     */
    public Color[] getColors(Layer l) {
        switch(l) {
            case one : return new Color[] {tabColors[0][0], tabColors[0][1], tabColors[0][2], tabColors[0][3]};
            case two : return new Color[] {tabColors[1][0], tabColors[1][1], tabColors[1][2], tabColors[1][3]};
            case three : return new Color[] {tabColors[2][0], tabColors[2][1], tabColors[2][2], tabColors[2][3]};
            default:
                throw new IllegalStateException("Unexpected value: " + l);
        }
    }

    /**
     * Getter permettant de retourner le code sérialisé de la forme colorée.
     * Ce code est notamment utilisé pour la comparaison avec l'objectif du niveau dans {@link modele.plateau.Livraison}.
     *
     * @return le code correspondant à la forme colorée sous forme de String
     */
    public String getCode(){
        return code;
    }

    /**
     * Fonction permettant de compter et renvoyer le nombre de couches remplies de la forme.
     * Une couche est considérée comme remplie si au moins un de ses 4 quadrants contient une sous-forme
     * différente de {@link SubShape#None}.
     *
     * @return le nombre de couches remplies, entre 0 et {@value #MAX_LAYERS}
     */
    public int getLayerCount() {
        int count = 0;
        for (int layer = 0; layer < MAX_LAYERS; layer++) {
            for (int i = 0; i < 4; i++) {
                if (tabSubShapes[layer][i] != SubShape.None) {
                    count = layer + 1;
                    break;
                }
            }
        }
        return count;
    }

    /**
     * Constructeur permettant l'initialisation d'une forme colorée par chaîne de caractères.
     * Le format attendu est : (lettre forme + lettre couleur) x 4 quadrants, séparés par ':' entre couches.
     * <p>
     * Lettres de forme : 'C' = Carré, 'O' = Cercle, 'S' = Etoile, 'F' = Eventail, '-' = Vide.
     * Lettres de couleur : 'r' = Rouge, 'b' = Blanc, 'g' = Vert, 'u' = Bleu, 'y' = Jaune, 'p' = Violet, 'c' = Cyan, '-' = Aucune.
     * <p>
     * Exemple : "CbCbCbCb" crée un carré blanc complet sur la première couche.
     * Exemple : "OrOr--:--SgSg" crée des cercles rouges en haut (couche 1) et des étoiles vertes en bas (couche 2).
     *
     * @param str le code de la forme sous forme de chaîne de caractères respectant le format décrit
     * @throws IllegalStateException si un caractère de forme ou de couleur n'est pas reconnu
     */
    public ItemShape(String str) {
        this.code = str;
        tabSubShapes = new SubShape[MAX_LAYERS][4];
        tabColors = new Color[MAX_LAYERS][4];
        for (int l = 0; l < MAX_LAYERS; l++) {
            for (int i = 0; i < 4; i++) {
                tabSubShapes[l][i] = SubShape.None;
                tabColors[l][i] = null;
            }
        }
        String[] layers = str.split(":");
        for (int l = 0; l < layers.length && l < MAX_LAYERS; l++) {
            for (int i = 0; i < 4; i++) {
                switch (layers[l].charAt(i * 2)) {
                    case 'C' : tabSubShapes[l][i] = SubShape.Carre; break;
                    case 'O' : tabSubShapes[l][i] = SubShape.Circle; break;
                    case 'S' : tabSubShapes[l][i] = SubShape.Star; break;
                    case 'F' : tabSubShapes[l][i] = SubShape.Fan; break;
                    case '-' : tabSubShapes[l][i] = SubShape.None; break;
                    default: throw new IllegalStateException("Unexpected value: " + layers[l].charAt(i * 2));
                }
                switch (layers[l].charAt(i * 2 + 1)) {
                    case 'r' : tabColors[l][i] = Color.Red; break;
                    case 'b' : tabColors[l][i] = Color.White; break;
                    case 'g' : tabColors[l][i] = Color.Green; break;
                    case 'u' : tabColors[l][i] = Color.Blue; break;
                    case 'y' : tabColors[l][i] = Color.Yellow; break;
                    case 'p' : tabColors[l][i] = Color.Purple; break;
                    case 'c' : tabColors[l][i] = Color.Cyan; break;
                    case '-' : tabColors[l][i] = null; break;
                    default: throw new IllegalStateException("Unexpected value: " + layers[l].charAt(i * 2 + 1));
                }
            }
        }
    }

    /**
     * Méthode permettant de faire pivoter la forme de 90 degrés dans le sens horaire.
     * La rotation s'applique à toutes les couches simultanément.
     * Chaque quadrant se décale d'une position : le quadrant 3 (haut-gauche) prend la place du 0 (haut-droit),
     * le 0 prend la place du 1 (bas-droit), le 1 prend la place du 2 (bas-gauche), le 2 prend la place du 3.
     * Les couleurs sont décalées de la même manière que les sous-formes.
     */
    public void rotate() {
        for (int l = 0; l < MAX_LAYERS; l++) {
            SubShape[] bufS = {tabSubShapes[l][3], tabSubShapes[l][0], tabSubShapes[l][1], tabSubShapes[l][2]};
            Color[] bufC = {tabColors[l][3], tabColors[l][0], tabColors[l][1], tabColors[l][2]};
            tabSubShapes[l] = bufS;
            tabColors[l] = bufC;
        }
    }

    /**
     * Méthode permettant d'empiler une forme supérieure sur la forme actuelle.
     * Les couches de la forme supérieure sont ajoutées au-dessus des couches existantes
     * de la forme actuelle, dans la limite de {@value #MAX_LAYERS} couches au total.
     * Le code de la forme est recalculé après l'empilement via {@link #buildCode()}.
     *
     * @param ShapeSup la forme à empiler sur la forme actuelle, ne doit pas être null
     */
    public void stack(ItemShape ShapeSup) {
        int nextLayer = getLayerCount();
        if (nextLayer >= MAX_LAYERS) return;
        int supLayers = ShapeSup.getLayerCount();
        for (int l = 0; l < supLayers && (nextLayer + l) < MAX_LAYERS; l++) {
            for (int i = 0; i < 4; i++) {
                tabSubShapes[nextLayer + l][i] = ShapeSup.tabSubShapes[l][i];
                tabColors[nextLayer + l][i] = ShapeSup.tabColors[l][i];
            }
        }
        code = buildCode();
    }

    /**
     * Méthode permettant de couper la forme en deux moitiés verticales.
     * La moitié droite (quadrants 0 et 1) reste dans l'objet actuel,
     * les quadrants gauches (2 et 3) sont vidés.
     * La moitié gauche (quadrants 2 et 3) est retournée dans un nouvel objet ItemShape,
     * les quadrants droits (0 et 1) y sont vidés.
     * La découpe s'applique à toutes les couches.
     * Le code de la forme actuelle est recalculé après la découpe via {@link #buildCode()}.
     *
     * @return un nouvel ItemShape correspondant à la moitié gauche de la forme découpée
     */
    public ItemShape Cut() {
        SubShape[][] leftShapes = new SubShape[MAX_LAYERS][4];
        Color[][] leftColors = new Color[MAX_LAYERS][4];
        for (int l = 0; l < MAX_LAYERS; l++) {
            leftShapes[l] = new SubShape[]{SubShape.None, SubShape.None, tabSubShapes[l][2], tabSubShapes[l][3]};
            leftColors[l] = new Color[]{null, null, tabColors[l][2], tabColors[l][3]};
            tabSubShapes[l][2] = SubShape.None;
            tabSubShapes[l][3] = SubShape.None;
            tabColors[l][2] = null;
            tabColors[l][3] = null;
        }
        code = buildCode();
        return new ItemShape(leftShapes, leftColors);
    }

    /**
     * Constructeur privé permettant l'initialisation d'une forme colorée à partir de tableaux
     * de sous-formes et de couleurs. Utilisé en interne par la méthode {@link #Cut()} pour
     * créer la moitié gauche découpée. Le code est automatiquement calculé via {@link #buildCode()}.
     *
     * @param shapes le tableau de sous-formes à deux dimensions [couche][quadrant]
     * @param colors le tableau de couleurs à deux dimensions [couche][quadrant]
     */
    private ItemShape(SubShape[][] shapes, Color[][] colors) {
        tabSubShapes = shapes;
        tabColors = colors;
        code = buildCode();
    }

    /**
     * Fonction privée permettant de construire et retourner le code sérialisé de la forme colorée
     * à partir de l'état actuel des tableaux {@link #tabSubShapes} et {@link #tabColors}.
     * Le code est construit couche par couche, avec le séparateur ':' entre chaque couche.
     * Pour chaque quadrant, la lettre de la forme est suivie de la lettre de la couleur.
     * <p>
     * Correspondances forme : Carré='C', Cercle='O', Etoile='S', Eventail='F', Vide='-'.
     * Correspondances couleur : Rouge='r', Blanc='b', Vert='g', Bleu='u', Jaune='y', Violet='p', Cyan='c', Vide='-'.
     *
     * @return la chaîne de caractères correspondant au code de la forme colorée
     */
    private String buildCode() {
        StringBuilder sb = new StringBuilder();
        int layerCount = Math.max(1, getLayerCount());
        for (int l = 0; l < layerCount; l++) {
            if (l > 0) sb.append(':');
            for (int i = 0; i < 4; i++) {
                switch (tabSubShapes[l][i]) {
                    case Carre: sb.append('C'); break;
                    case Circle: sb.append('O'); break;
                    case Star: sb.append('S'); break;
                    case Fan: sb.append('F'); break;
                    case None: sb.append('-'); break;
                }
                if (tabColors[l][i] == null) sb.append('-');
                else switch (tabColors[l][i]) {
                    case Red: sb.append('r'); break;
                    case White: sb.append('b'); break;
                    case Green: sb.append('g'); break;
                    case Blue: sb.append('u'); break;
                    case Yellow: sb.append('y'); break;
                    case Purple: sb.append('p'); break;
                    case Cyan: sb.append('c'); break;
                }
            }
        }
        return sb.toString();
    }

    /**
     * Méthode permettant de colorier tous les quadrants non vides de la forme avec une couleur donnée.
     * Seuls les quadrants dont la sous-forme est différente de {@link SubShape#None} sont coloriés.
     * La coloration s'applique à toutes les couches de la forme.
     * Le code de la forme est recalculé après la coloration via {@link #buildCode()}.
     *
     * @param c la couleur à appliquer sur tous les quadrants non vides de la forme
     */
    public void Color(Color c) {
        for (int l = 0; l < MAX_LAYERS; l++) {
            for (int i = 0; i < 4; i++) {
                if (tabSubShapes[l][i] != SubShape.None) {
                    tabColors[l][i] = c;
                }
            }
        }
        code = buildCode();
    }
}
