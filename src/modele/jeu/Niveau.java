package modele.jeu;

public class Niveau {
    private final String codeObjectif; // ex: "CbCbCbCb"
    private final String description;  // ex: "Carré blanc"

    public Niveau(String codeObjectif, String description) {
        this.codeObjectif = codeObjectif;
        this.description = description;
    }

    public String getCodeObjectif() {
        return codeObjectif;
    }

    public String getDescription() {
        return description;
    }
}