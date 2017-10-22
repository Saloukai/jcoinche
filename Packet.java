package Jcoinche.server;
import java.util.Arrays;
import java.util.Random;

class Paquet
{
    Cards[] cartes;
    private static final int NB_ITERATIONS = 3;

    Paquet()
    {
        this.cartes = new Cards[Cards.Rang.values().length * Cards.Couleur.values().length];
        for(int i = 0; i < Cards.Couleur.values().length; i++)
            for(int j = 0; j < Cards.Rang.values().length; j++)
                this.cartes[i * Cards.Rang.values().length + j] = new Cards(Cards.Rang.values()[j], Cards.Couleur.values()[i]);
        melanger();
    }

    private void melanger()
    {
        Random r = new Random();
        for(int i = 0; i < NB_ITERATIONS; i++)
        {
            for(int j = 0; j < this.cartes.length; j++)
                echanger(r.nextInt(this.cartes.length), r.nextInt(this.cartes.length));
        }
    }

    private void echanger(int i, int j)
    {
        Cards temp;
        temp = this.cartes[i];
        this.cartes[i] = this.cartes[j];
        this.cartes[j] = temp;
    }

    Cards[] piocher(int n)
    {
        if(n <= this.cartes.length + 1)
        {
            Cards[] main = Arrays.copyOfRange(this.cartes, 0, n);
            this.cartes = Arrays.copyOfRange(this.cartes, n, this.cartes.length);

            return main;
        }
        return null;
    }
}
