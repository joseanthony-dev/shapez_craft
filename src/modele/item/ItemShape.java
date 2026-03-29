package modele.item;

public class ItemShape extends Item {

    private SubShape[] tabSubShapes;
    private Color[] tabColors;
    public enum Layer {one, two, three};
    private String code;

    public SubShape[] getSubShapes(Layer l) {
        switch(l) {
            case one : return new SubShape[] {tabSubShapes[0], tabSubShapes[1], tabSubShapes[2], tabSubShapes[3]};
            // TODO two & three
            default:
                throw new IllegalStateException("Unexpected value: " + l);
        }
    }

    public Color[] getColors(Layer l) {
        switch(l) {
            case one : return new Color[] {tabColors[0], tabColors[1], tabColors[2], tabColors[3]};
            // TODO two & three
            default:
                throw new IllegalStateException("Unexpected value: " + l);
        }
    }

    /**
     * Getter permettant de retourner le code de la forme coloré
     * @return
     */
    public String getCode(){
        return code;
    }

    /**;
     * Initialisation des formes par chaîne de caractères
     * @param str : codage : (sous forme + couleur ) * (haut-droit, bas-droit, bas-gauche, haut-gauche) * 3 Layers
     *            str.length multiple de 4
     */
    public ItemShape(String str) {
        // on stocke le code dans le constructeur
        this.code = str;
        tabSubShapes = new SubShape[str.length()/2 ];
        tabColors = new Color[str.length()/2];
        for (int i = 0; i < 4; i++) { // fait uniquement pour la première couche
            // ajout des différentes formes
            switch (str.charAt(i*2)) {
                case 'C' : tabSubShapes[i] = SubShape.Carre;break;
                case 'O' : tabSubShapes[i] = SubShape.Circle;break;
                case 'S' : tabSubShapes[i] = SubShape.Star;break;
                case 'F' : tabSubShapes[i] = SubShape.Fan;break;
                case '-' : tabSubShapes[i] = SubShape.None;break;
                default:
                    throw new IllegalStateException("Unexpected value: " + str.charAt(i));
            }
            switch (str.charAt((i*2 + 1))) {
                // ajout des différentes couleurs
                case 'r' : tabColors[i] = Color.Red; break;
                case 'b' : tabColors[i] = Color.White; break;
                case 'g' : tabColors[i] = Color.Green; break;
                case 'u' : tabColors[i] = Color.Blue; break;
                case 'y' : tabColors[i] = Color.Yellow; break;
                case 'p' : tabColors[i] = Color.Purple; break;
                case 'c' : tabColors[i] = Color.Cyan; break;
                case '-' : tabColors[i] = null; break;
                default:
                    throw new IllegalStateException("Unexpected value: " + str.charAt((i + 1)*2));
            }
        }
    }

    // TODO : écrire l'ensemble des fonctions de transformation souhaitées, définir les paramètres éventuels (sens, axe, etc.)
    public void rotate() {

        SubShape[] bufferSubShapes = new SubShape[4];
        bufferSubShapes[0] = tabSubShapes[3];
        bufferSubShapes [1] = tabSubShapes[0];
        bufferSubShapes [2] = tabSubShapes[1];
        bufferSubShapes [3] = tabSubShapes[2];

        Color[] bufferColors = new Color[4];
        bufferColors[0] = tabColors[3];
        bufferColors [1] = tabColors[0];
        bufferColors [2] = tabColors[1];
        bufferColors [3] = tabColors[2];

        tabSubShapes = bufferSubShapes;
        tabColors = bufferColors;
    }
    public void stack(ItemShape ShapeSup) { // ShapeSup est empilé sur this
    }

    /**
     * La méthode permet de couper une forme et retourner la partie gauche alors que la partie droite reste l'objet actuel
     * @return
     */
    public ItemShape Cut() { // this et l'objet retourné correpondent au deux sorties
        // Moitié gauche : on garde quadrants 2 et 3, on met 0 et 1 à None/null
        SubShape[] leftShapes = new SubShape[]{SubShape.None, SubShape.None, tabSubShapes[2], tabSubShapes[3]};
        Color[] leftColors = new Color[]{null, null, tabColors[2], tabColors[3]};

        // This devient la moitié droite : on garde quadrants 0 et 1, on met 2 et 3 à None/null
        tabSubShapes[2] = SubShape.None;
        tabSubShapes[3] = SubShape.None;
        tabColors[2] = null;
        tabColors[3] = null;

        // On reconstruit le code pour this
        code = buildCode();

        // On crée la moitié gauche
        ItemShape left = new ItemShape(leftShapes, leftColors);
        return left;
    }


    private ItemShape(SubShape[] shapes, Color[] colors) {
        tabSubShapes = shapes;
        tabColors = colors;
        code = buildCode();
    }

    private String buildCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            switch (tabSubShapes[i]) {
                case Carre: sb.append('C'); break;
                case Circle: sb.append('O'); break;
                case Star: sb.append('S'); break;
                case Fan: sb.append('F'); break;
                case None: sb.append('-'); break;
            }
            if (tabColors[i] == null) sb.append('-');
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
        return sb.toString();
    }

    public void Color(Color c) {
    }
}
