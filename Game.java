package Jcoinche.server;

import org.apache.mina.core.service.IoAcceptor;

import java.util.Random;


/**
 * Created by oreo on 21/11/16.
 * Classe représentant le jeu.
 */
public class Game {
    /** Liste des joueurs **/
    Player[] players;
    /** Score de la Team A **/
    private int scoreTeamA;
    /** Score de la Team B **/
    private int scoreTeamB;
    /** Acceptor contenant les sessions des clients **/
    private IoAcceptor acceptor;
    /** Le premier joueur à jouer ors du tour **/
    private Player first;
    /** La couleur de l'atout pendant le tour **/
    private Cartes.Couleur atout;
    /** La couleur à jouer pendant le tour **/
    private Cartes.Couleur value;
    /** Permet de savoir si la partie à commencer ou si l'on attend encore des joueurs **/
    boolean start;

    /**
     * Initialise le jeu avec tous les paramètres nécessaires.
     *
     * @param acceptor
     *      contients les sessions clients des differents joeurs
     * @param p1
     *      joueur 1
     * @param p2
     *      joueur 2
     * @param p3
     *      joueur 3
     * @param p4
     *      joueur 4
     * @param paquet
     *      paquet initial
     */
    Game (IoAcceptor acceptor, Player p1, Player p2, Player p3, Player p4, Paquet paquet)
    {
        this.start = true;
        p1.setCards(paquet);
        p2.setCards(paquet);
        p3.setCards(paquet);
        p4.setCards(paquet);
        first = p1;
        this.players = new Player[4];
        this.players[0] = p1;
        this.players[1] = p2;
        this.players[2] = p3;
        this.players[3] = p4;
        this.scoreTeamA = 0;
        this.scoreTeamB = 0;
        this.atout = randomAtout();
        this.acceptor = acceptor;
    }

    /***
     * Dans le cas où tous les clients ne sont pas encore instanciés
     */
    Game ()
    {
        this.start = false;
        this.scoreTeamA = 0;
        this.scoreTeamB = 0;
    }

    /**
     * Permet de générer la valeur de l'atout (appelé à chaque tour)
     * @return
     *          Couleur de l'atout
     */
    private static Cartes.Couleur randomAtout()
    {
        Random RANDOM = new Random();
        return Cartes.Couleur.values()[RANDOM.nextInt(4)];
    }

    /***
     * Permet de recuperer le score de la Team A
     * @return
     *      score Team A
     */
    int getScoreTeamA()
    {
        return this.scoreTeamA;
    }

    /***
     * Permet de recuperer le score de la Team A
     * @return
     *      score Team A
     */
    int getScoreTeamB()
    {
        return this.scoreTeamB;
    }

    /**
     * Permet de set la valeur dominante lors du tour de jeu
     * @param v
     *      La couleur de la carte jouée par le premier joueur
     */
    private void setValue(Cartes.Couleur v)
    {
        this.value = v;
        System.out.println("Value is now : " + this.value);
    }

    /**
     * Permet d'ajouter des points au score de la Team A
     * @param points
     *      Le nombre de points à ajouter
     */
    private void setScoreTeamA(int points)
    {
        this.scoreTeamA += points;
    }

    /**
     * Permet d'ajouter des points au score de la Team B
     * @param points
     *      Le nombre de points à ajouter
     */
    private void setScoreTeamB(int points)
    {
        this.scoreTeamB += points;
    }

    /**
     * Permet de set la couleur de l'atout pour ce tour.
     */

    private void setAtout()
    {
        this.atout = randomAtout();
        System.out.println("\nAtout is now : " + this.atout);
    }

    /**
     * Ordre de jeu des jouerus en fonction du gagnant de la manche précédantes
     * @return
     *      La liste des joueurs dans l'ordre de jeu.
     */
    private Player[] setOrder()
    {
        Player order[] = new Player[4];
        int i = 0;
        while (players[i] != this.first)
            i+=1;
        int j = 0;
        while (j < 4)
        {
            order[j] = players[i];
            i+=1;
            if (i == 4)
                i = 0;
            j+=1;
        }
        return order;
    }

    /**
     *Fin du jeu. Ferme tous les clients et le serveur.
     *
     */
    void end()
    {
        for (int i = 0; i < 4; i++)
        {
            if (!players[i].isIA())
                players[i].session.write("QUIT");
        }
        System.exit(0);
    }

    /**
     * Correspond à un tour de jeu. Cela permet de faire jouer tour à tour chacun des joueurs.
     */
    void turn()
    {
        this.setAtout();
        Player order[] = setOrder();
        Cartes set[] = new Cartes[4];
        int atoutPoints[] = {0,0,14,10,20,3,4,11};
        int normalPoints[] = {0,0,0,10,20,3,4,11};
        if (!first.isIA())
            first.session.write("PLAY:NULL-" + this.atout);
        order[0].setTurn(true);
        Cartes tmp = null;

        if (order[0].isIA())
        {
            while (tmp == null) {
            for (int i = 0; i < order[0].getNbCards(); i++) {
                tmp = order[0].play(order[0].getMain()[i].toString());
                if (tmp != null)
                    i = order[0].getNbCards();
                }
            }
        }
        else {
            while (tmp == null) {
                while (order[0].getMsg() == null) {
                    System.out.print("");
                }
                tmp = order[0].play(order[0].getMsg());
                order[0].setMsg(null);
            }
        }
        order[0].setTurn(false);
        order[1].setMsg(null);
        set[0] = tmp;
        this.setValue(set[0].getCouleur());
        for (int i = 0; i < 4; i++) {
            if (!order[i].isIA())
                order[i].session.write("PLAYED:" + order[0].getName() + "-" + order[0].getTeam() + "-" + set[0].toString());
        }

        for (int i = 1; i < 4; i++) {
            if (!order[i].isIA())
                order[i].session.write("PLAY:" + this.value + "-" + this.atout);
            tmp = null;
            order[i].setTurn(true);
            if (order[i].isIA()) {
                while (tmp == null) {
                    for (int b = 0; b < order[i].getNbCards(); b++) {
                        tmp = order[i].play(order[i].getMain()[b].toString(), this.value, this.atout);
                        if (tmp != null)
                            b = order[i].getNbCards();
                    }
                }
            } else {
                while (tmp == null) {
                    while (order[i].getMsg() == null) {
                        System.out.print("");
                    }
                    tmp = order[i].play(order[i].getMsg(), this.value, this.atout);
                    order[i].setMsg(null);
                }
            }
            order[i].setMsg(null);
            order[i].setTurn(false);
            set[i] = tmp;
            for (int j = 0; j < 4; j++) {
                if (!order[j].isIA())
                    order[j].session.write("PLAYED:" + order[i].getName() + "-" + order[i].getTeam() + "-" + set[i].toString());
            }
        }

            int score = 0;
            int s;
            System.out.println();
            for (int c = 0; c < 4; c++) {
                s = 0;
                if (set[c].getCouleur().equals(this.atout))
                    s = atoutPoints[set[c].getRang().ordinal()];
                else
                    s += normalPoints[set[c].getRang().ordinal()];
                score += s;
            }

            boolean at = false;
            for (int t = 0; t < 4; t++) {
                if (set[t].getCouleur().equals(this.atout))
                    at = true;
            }

            Cartes max = set[0];
            int nb = 0;
            for (int e = 0; e < 4; e++) {
                if (at) {
                    if (set[e].getCouleur().equals(this.atout)) {
                        if (set[e].getRang().ordinal() > max.getRang().ordinal() || !max.getCouleur().equals(this.atout)) {
                            max = set[e];
                            nb = e;
                        }
                    }
                } else {
                    if (set[e].getCouleur().equals(this.value)) {
                        if (set[e].getRang().ordinal() > max.getRang().ordinal() || !max.getCouleur().equals(this.value)) {
                            max = set[e];
                            nb = e;
                        }
                    }
                }
            }
            first = players[nb];
            first.setScore(score);
            System.out.println("Played:");
            System.out.println(order[0].getName() + ": " + set[0].toString());
            System.out.println(order[1].getName() + ": " + set[1].toString());
            System.out.println(order[2].getName() + ": " + set[2].toString());
            System.out.println(order[3].getName() + ": " + set[3].toString());
            if (first.getTeam() == 1)
                this.setScoreTeamA(score);
            else
                this.setScoreTeamB(score);
            System.out.println("\nJoueur: " + first.getName() + " won this round with " + score + " points");
            System.out.println("Score : TeamA " + this.getScoreTeamA() + " - TeamB " + this.getScoreTeamB());
            System.out.println("--------------------------------------------\n");
            for (int x = 0; x < 4; x++) {
                if (!order[x].isIA())
                    order[x].session.write("SCORE:" + this.getScoreTeamA() + "-" + this.getScoreTeamB());
            }
    }
}
