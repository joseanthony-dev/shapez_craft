package modele.item;

/**
 * Classe héritante de Item, permettant de définir une forme
 */
public class ItemShape extends Item {
    private SubShape[] tabSubShapes; // Tableau stockant les 4 cadrants de l'item
    private Color[] tabColors; // Tableau stockant les 4 couleurs de l'item
    public enum Layer {one, two, three}; // Enumération permettant de stocker les couches de l'item
    private String code; // String permettant de stocker le code de l'item

    /**
     * Fonction permettant de retourner une nouveau tableau constitué des 4 cadrants de l'objet
     * @param l définit la couche que l'on veut récupérer
     * @return le tableau de taille 4 contenant les 4 cadrants de la forme
     */
    public SubShape[] getSubShapes(Layer l) {
        switch(l) {
            case one : return new SubShape[] {tabSubShapes[0], tabSubShapes[1], tabSubShapes[2], tabSubShapes[3]}; // Retourne le tableau des 4 cadrants
            // TODO two & three
            default:
                throw new IllegalStateException("Unexpected value: " + l); // Si pas de couhes, on lève une exception
        }
    }

    /**
     * Fonction permettant de retourner une nouveau tableau constitué des 4 cadrants de couleur de l'objet
     * @param l définit la couche que l'on veut récupérer
     * @return le tableau de taille 4 contenant les 4 cadrants de couleur de la forme
     */
    public Color[] getColors(Layer l) {
        switch(l) {
            case one : return new Color[] {tabColors[0], tabColors[1], tabColors[2], tabColors[3]}; // Retourne le tableau des 4 cadrants de couleur
            // TODO two & three
            default:
                throw new IllegalStateException("Unexpected value: " + l); // Si pas de couhes, on lève une exception
        }
    }

    /**
     * Getter permettant de retourner le code de la forme coloré
     * @return le code correspondant à la forme coloré sous forme de String
     */
    public String getCode(){
        return code; // On retourne le code de l'objet
    }

    /**;
     * Constructeur permettant l'initialisation des formes par chaîne de caractères
     * @param str : codage : (sous forme + couleur ) * (haut-droit, bas-droit, bas-gauche, haut-gauche) * 3 Layers
     *            str.length multiple de 4
     */
    public ItemShape(String str) {
        this.code = str; // Affectation du code à l'attribut
        tabSubShapes = new SubShape[str.length()/2 ]; // On créer un nouveau tableau pour contenir les formes de taille /2
        tabColors = new Color[str.length()/2]; // ON créer un nouveau tableau pour contenir les couleurs de taille /2
        for (int i = 0; i < 4; i++) {
            // On affecte l'énumération de forme selon le caractère donnée
            switch (str.charAt(i*2)) {
                case 'C' : tabSubShapes[i] = SubShape.Carre;break;
                case 'O' : tabSubShapes[i] = SubShape.Circle;break;
                case 'S' : tabSubShapes[i] = SubShape.Star;break;
                case 'F' : tabSubShapes[i] = SubShape.Fan;break;
                case '-' : tabSubShapes[i] = SubShape.None;break;
                default:
                    throw new IllegalStateException("Unexpected value: " + str.charAt(i)); // On lève une exception si il y a une erreur
            }
            switch (str.charAt((i*2 + 1))) {
                // On affecte l'énumération de couleur selon le caractère donnée
                case 'r' : tabColors[i] = Color.Red; break;
                case 'b' : tabColors[i] = Color.White; break;
                case 'g' : tabColors[i] = Color.Green; break;
                case 'u' : tabColors[i] = Color.Blue; break;
                case 'y' : tabColors[i] = Color.Yellow; break;
                case 'p' : tabColors[i] = Color.Purple; break;
                case 'c' : tabColors[i] = Color.Cyan; break;
                case '-' : tabColors[i] = null; break;
                default:
                    throw new IllegalStateException("Unexpected value: " + str.charAt((i + 1)*2)); // On lève une exception si il y a une erreur
            }
        }
    }

    /**
     * Méthode permettant de faire tourner un item
     */
    public void rotate() {
        SubShape[] bufferSubShapes = new SubShape[4]; // On stocke un tableau temporaire pour faire la rotation
        // On tourne les formes dans le nouveau tableau
        bufferSubShapes[0] = tabSubShapes[3];
        bufferSubShapes [1] = tabSubShapes[0];
        bufferSubShapes [2] = tabSubShapes[1];
        bufferSubShapes [3] = tabSubShapes[2];
        Color[] bufferColors = new Color[4]; // On stocke un tableau de couleurs temporaire pour faire la rotation
        // On tourne les couleurs dans le nouveau tableau
        bufferColors[0] = tabColors[3];
        bufferColors [1] = tabColors[0];
        bufferColors [2] = tabColors[1];
        bufferColors [3] = tabColors[2];
        // On réattribut à nos attributs les nouveaux tableaux crées
        tabSubShapes = bufferSubShapes;
        tabColors = bufferColors;
    }

    /**
     * Méthode permettant d'empiler des formes les unes sur les autres
     * @param ShapeSup est la forme à empiler sur celle actuelle
     */
    public void stack(ItemShape ShapeSup) { // ShapeSup est empilé sur this
    }

    /**
     * La méthode permet de couper une forme et retourner la partie gauche alors que la partie droite reste l'objet actuel
     * @return un nouvel Item correspondant à la partie gauche, la partie droite elle deviens l'objet actuel
     */
    public ItemShape Cut() { // this et l'objet retourné correpondent au deux sorties
        SubShape[] leftShapes = new SubShape[]{SubShape.None, SubShape.None, tabSubShapes[2], tabSubShapes[3]}; // Moitié gauche : on garde quadrants 2 et 3, on met 0 et 1 à null
        Color[] leftColors = new Color[]{null, null, tabColors[2], tabColors[3]}; // Moitié gauche : on garde quadrants 2 et 3, on met 0 et 1 à null pour le tableau de couleurs
        // This devient la moitié droite : on garde quadrants 0 et 1, on met 2 et 3 à null
        tabSubShapes[2] = SubShape.None;
        tabSubShapes[3] = SubShape.None;
        tabColors[2] = null;
        tabColors[3] = null;
        code = buildCode(); // On reconstruit le code pour this
        ItemShape left = new ItemShape(leftShapes, leftColors); // On crée la moitié gauche avec notre item et notre couleur stocké au début
        return left;
    }

    /**
     * Constructeur permettant l'initialisation d'une forme coloré et alimente la String
     * @param shapes correspondant au tableau de formes que l'item doit devenir
     * @param colors correspondant au tableau de couleurs que l'item doit devenir
     */
    private ItemShape(SubShape[] shapes, Color[] colors) {
        tabSubShapes = shapes; // On affecte à notre attribut les formes
        tabColors = colors; // On affecte à notre attribut les couleurs
        code = buildCode(); // On construit la String
    }

    /**
     * Fonction permettant de retourner le code construit en String de la forme coloré stocké dans notre attribut code
     * @return la string correspondant au code de la forme coloré
     */
    private String buildCode() {
        StringBuilder sb = new StringBuilder(); // Création d'une nouvelle string
        // On boucle pour ajouter notre caractère selon la forme à notre string
        for (int i = 0; i < 4; i++) {
            switch (tabSubShapes[i]) {
                case Carre: sb.append('C'); break;
                case Circle: sb.append('O'); break;
                case Star: sb.append('S'); break;
                case Fan: sb.append('F'); break;
                case None: sb.append('-'); break;
            }
            if (tabColors[i] == null) sb.append('-');
            // On boucle pour ajouter notre caractère selon la couleur à notre string
            else switch (tabColors[i]) {
                case Red: sb.append('r'); break;
                case White: sb.append('b'); break;
                case Green: sb.append('g'); break;
                case Blue: sb.append('u'); break;
                case Yellow: sb.append('y'); break;
                case Purple: sb.append('p'); break;
                case Cyan: sb.append('c'); break;
            }
        }
        return sb.toString(); // Retourne le code selon le tableau de formes et de couleurs
    }

    /**
     * Méthode permettant de colorié une forme et de reconstruire le code ensuite
     * @param c correspondant à la couleur avec laquelle on veut colorié notre item
     */
    public void Color(Color c) {
        // On parcours notre tableau et on affecte la couleur au tableau de couleur
        for (int i = 0; i < 4; i++) {
            if (tabSubShapes[i] != SubShape.None) {
                tabColors[i] = c;
            }
        }
        code = buildCode(); // On reconstruit le code
    }
}
