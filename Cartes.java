package Jcoinche.server;

/**
 * Created by oreo on 21/11/16.
 * Classe représentant une carte de jeu.
 */
public class Cartes
{
    /** Rang de la carte. */
    public enum Rang
    {
        SEPT, HUIT, NEUF, DIX, VALET, DAME, ROI, AS
    }

    /** Couleur de la carte. */
    public enum Couleur
    {
        PIQUE, COEUR, CARREAU, TREFLE
    }

    /** Le rang de la carte. */
    private Rang rang;

    /** La couleur de la carte. */
    private Couleur couleur;


    /**
     * Constructeur de carte
     * @param
     *      rang Le rang de la carte
     * @param
     *      couleur La couleur de la carte
     */
    public Cartes(Rang rang, Couleur couleur) {
        this.rang = rang;
        this.couleur = couleur;
    }
    /**
     * Retourne le rang de la carte
     * @return
     *      le rang de la carte
     */
    public Rang getRang()
    {
        return this.rang;
    }


    /**
     * Retourne la couleur de la carte
     * @return
     *     la couleur de la carte
     */
    public Couleur getCouleur()
    {
        return this.couleur;
    }


    /**
     * Retourne une représentation de la carte
     * @return
     *      Une string de la carte
     */
    @Override
    public String toString()
    {
        return this.rang + "_" + this.couleur;
    }
}