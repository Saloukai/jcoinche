package Jcoinche.server;
import org.apache.mina.core.service.IoAcceptor;
import java.util.Random;


public class Jeu {
    Joueurs[] players;
    private int score1;
    private int score2;
    private IoAcceptor ioa;
    private Joueurs premier;
    private Cards.Couleur atout;
    private Cards.Couleur value;
    boolean debut;

    Jeu (IoAcceptor ioa, Joueurs jA, Joueurs jB, Joueurs jC, Joueurs jD, Paquet pack)
    {
        this.debut = true;
        jA.setCards(pack);
        jB.setCards(pack);
        jC.setCards(pack);
        jD.setCards(pack);
        premier = jA;
        this.players = new Joueurs[4];
        this.players[0] = jA;
        this.players[1] = jB;
        this.players[2] = jC;
        this.players[3] = jD;
        this.score1 = 0;
        this.score2 = 0;
        this.atout = randomAtout();
        this.ioa = ioa;
    }

    Jeu ()
    {
        this.debut = false;
        this.score1 = 0;
        this.score2 = 0;
    }

    private static Cards.Couleur randomAtout()
    {
        Random RANDOM = new Random();
        return Cards.Couleur.values()[RANDOM.nextInt(4)];
    }

    int getScoreTeamA()
    {
        return this.score1;
    }

    int getScoreTeamB()
    {
        return this.score2;
    }

    private void setValue(Cards.Couleur v)
    {
        this.value = v;
        System.out.println("Value is now : " + this.value);
    }

    private void setScoreTeamA(int points)
    {
        this.score1 += points;
    }

    private void setScoreTeamB(int points)
    {
        this.score2 += points;
    }

    private void setAtout()
    {
        this.atout = randomAtout();
        System.out.println("\nAtout is now : " + this.atout);
    }

    private Joueurs[] setOrder()
    {
        Joueurs order[] = new Joueurs[4];
        int i = 0;
        while (players[i] != this.premier)
            i+=1;
        int j = 0;
        while (j < 4)
        {
            order[j] = players[i];
            i++;
            if (i == 4)
                i = 0;
            j++;
        }
        return order;
    }

    void end()
    {
        for (int i = 0; i < 4; i++)
            if (!players[i].isIA())
                players[i].session.write("QUIT");
        System.exit(0);
    }

    void turn()
    {
        this.setAtout();
        Joueurs order[] = setOrder();
        Cards set[] = new Cards[4];
        int atoutPoints[] = {0,0,14,10,20,3,4,11};
        int normalPoints[] = {0,0,0,10,20,3,4,11};
        if (!premier.isIA())
            premier.session.write("PLAY:NULL-" + this.atout);
        order[0].setTurn(true);
        Cards tmp = null;

        if (order[0].isIA())
        {
            while (tmp == null)
            {
            for (int i = 0; i < order[0].getNbCards(); i++)
            {
                tmp = order[0].play(order[0].getMain()[i].toString());
                if (tmp)
                    i = order[0].getNbCards();
                }
            }
        }
        else {
            while (!tmp) {
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

            Cards max = set[0];
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
            premier = players[nb];
            premier.setScore(score);
            System.out.println("Played:");
            System.out.println(order[0].getName() + ": " + set[0].toString());
            System.out.println(order[1].getName() + ": " + set[1].toString());
            System.out.println(order[2].getName() + ": " + set[2].toString());
            System.out.println(order[3].getName() + ": " + set[3].toString());
            if (premier.getTeam() == 1)
                this.setScoreTeamA(score);
            else
                this.setScoreTeamB(score);
            System.out.println("\nJoueur: " + premier.getName() + " won this round with " + score + " points");
            System.out.println("Score : TeamA " + this.getScoreTeamA() + " - TeamB " + this.getScoreTeamB());
            System.out.println("--------------------------------------------\n");
            for (int x = 0; x < 4; x++) {
                if (!order[x].isIA())
                    order[x].session.write("SCORE:" + this.getScoreTeamA() + "-" + this.getScoreTeamB());
            }
    }
}
