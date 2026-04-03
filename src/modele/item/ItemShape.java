/**
 * Fichier de classe des items
 * @author JOSE Anthony
 * @since 2026-03-18
 * @version 1.0
 * @see modele.item
 */
package modele.item;

/**
 * Classe héritante de Item, permettant de définir une forme
 */
public class ItemShape extends Item {
    public static final int MAX_LAYERS = 3;
    /**
     * @serial Tableau stockant les 4 cadrants de l'item
     */
    private SubShape[][] tabSubShapes;

    /**
     * @serial Tableau stockant les 4 couleurs de l'item
     */
    private Color[][] tabColors;

    /**
     * @serial Enumération définissant les couches de l'item
     */
    public enum Layer {one, two, three};

    /**
     * @serial String permettant de stocker le code de l'item
     */
    private String code;

    /**
     * Fonction permettant de retourner une nouveau tableau constitué des 4 cadrants de l'objet
     * @param l définit la couche que l'on veut récupérer
     * @return le tableau de taille 4 contenant les 4 cadrants de la forme
     * @throws Lève une exception si la couche n'est pas connu
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
     * Fonction permettant de retourner une nouveau tableau constitué des 4 cadrants de couleur de l'objet
     * @param l définit la couche que l'on veut récupérer
     * @return le tableau de taille 4 contenant les 4 cadrants de couleur de la forme
     * @throws Lève une exception si la couche n'est pas connu
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
     * Getter permettant de retourner le code de la forme coloré
     * @return le code correspondant à la forme coloré sous forme de String
     */
    public String getCode(){
        return code;
    }

    /**
     * Fonction permettant de renvoyer le nombre de couches remplit
     * @return le nombre de couches remplit
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

    /**;
     * Constructeur permettant l'initialisation des formes par chaîne de caractères
     * @param str : codage : (sous forme + couleur ) * (haut-droit, bas-droit, bas-gauche, haut-gauche) * 3 Layers
     * @throws Leve une exception si le caractère n'est pas connu
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
     * Méthode permettant de faire tourner un item
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
     * Méthode permettant d'empiler des formes les unes sur les autres
     * @param ShapeSup est la forme à empiler sur celle actuelle
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
     * La méthode permet de couper une forme et retourner la partie gauche alors que la partie droite reste l'objet actuel
     * @return un nouvel Item correspondant à la partie gauche, la partie droite elle deviens l'objet actuel
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
     * Constructeur permettant l'initialisation d'une forme coloré et alimente la String
     * @param shapes correspondant au tableau de formes que l'item doit devenir
     * @param colors correspondant au tableau de couleurs que l'item doit devenir
     */
    private ItemShape(SubShape[][] shapes, Color[][] colors) {
        tabSubShapes = shapes;
        tabColors = colors;
        code = buildCode();
    }

    /**
     * Fonction permettant de retourner le code construit en String de la forme coloré stocké dans notre attribut code
     * @return la string correspondant au code de la forme coloré
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
     * Méthode permettant de colorié une forme et de reconstruire le code ensuite
     * @param c correspondant à la couleur avec laquelle on veut colorié notre item
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
