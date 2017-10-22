package Jcoinche.server;

public class Cards
{
    public enum Rang
    {
        SEPT,
        HUIT,
        NEUF,
        DIX,
        VALET,
        DAME,
        ROI,
        AS
    }

    public enum Couleur
    {
        PIQUE,
        COEUR,
        CARREAU,
        TREFLE
    }

    private Rang rang;

    private Couleur couleur;

    public Cards(Rang rang, Couleur couleur) {
        this.rang = rang;
        this.couleur = couleur;
    }

    public Rang getRang()
    {
        return this.rang;
    }


    public Couleur getCouleur()
    {
        return this.couleur;
    }


    @Override
    public String toString()
    {
        return this.rang + "_" + this.couleur;
    }
}