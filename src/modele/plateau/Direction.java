package modele.plateau;

public enum Direction {
    North(0, -1), South(0, 1), East(1, 0), West(-1, 0);

    int dx;
    int dy;
    private Direction(int _dx, int _dy) {
        dx = _dx;
        dy = _dy;
    }

    /**
     * Permet de modifier la direction dans l'ordre : Nord -> Est -> Sud -> Ouest
     * @return
     */
    public Direction direction_suivante() {
        switch (this) {
            case North: return East;
            case East: return South;
            case South: return West;
            case West: return North;
            default: return North;
        }
    }

}
