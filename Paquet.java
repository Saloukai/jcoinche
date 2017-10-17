package Jcoinche.server;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by oreo on 21/11/16.
 * Classe représentant un jeu de cartes.
 */
class Paquet
{
    /** Paquet de cartes. */
    Cartes[] cartes;

    /** Nombre d'itérations pour mélanger les cartes. */
    private static final int NB_ITERATIONS = 3;


    /**
     * Constructeur.
     */
    Paquet()
    {
        // Initialisation des cartes du paquet.
        this.cartes = new Cartes[Cartes.Rang.values().length * Cartes.Couleur.values().length];
        for(int i = 0; i < Cartes.Couleur.values().length; i++)
        {
            for(int j = 0; j < Cartes.Rang.values().length; j++)
            {
                this.cartes[i * Cartes.Rang.values().length + j] = new Cartes(Cartes.Rang.values()[j], Cartes.Couleur
                        .values()[i]);
            }
        }
        // Mélange le jeu de cartes.
        melanger();
    }


    /**
     * Mélange les cartes du paquet.
     */
    private void melanger()
    {
        Random r = new Random();
        for(int i = 0; i < NB_ITERATIONS; i++)
        {
            for(int j = 0; j < this.cartes.length; j++)
            {
                echanger(r.nextInt(this.cartes.length), r.nextInt(this.cartes.length));
            }
        }
    }


    /**
     * Echange deux cartes d'un paquet.
     * @param
     *      i L'indice de la première carte à échanger.
     * @param
     *      j L'indice de la seconde carte à échanger.
     */
    private void echanger(int i, int j)
    {
        Cartes temp;
        temp = this.cartes[i];
        this.cartes[i] = this.cartes[j];
        this.cartes[j] = temp;
    }
    /**
     * <p>Renvoie un tableau des cartes piochées.</p>
     * @param n Le nombre de cartes poichées.
     * @return Retourne un tableau des cartes piochées ou null s'il n'y a plus assez de cartes dans
     *         le paquet.
     */
    Cartes[] piocher(int n)
    {
        if(n <= this.cartes.length + 1)
        {
            Cartes[] main = Arrays.copyOfRange(this.cartes, 0, n);
            this.cartes = Arrays.copyOfRange(this.cartes, n, this.cartes.length);

            return main;
        }
        return null;
    }
}
