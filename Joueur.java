package Jcoinche.server;
import org.apache.mina.core.session.IoSession;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Joueurs {
    private Cards[] main;
    private boolean turn;
    private String name;
    private String msg;
    private int team;
    private long id;
    private int score;
    private boolean IA;
    IoSession session;

    Joueurs(int team, long id, IoSession session)
    {
        turn = false;
        IA = false;
        this.name = String.format("Joueurs%d", id);
        this.team = team;
        this.session = session;
        this.msg = null;
        this.id = id;
        this.score = 0;
        if (!this.isIA())
            session.write("INFO:" + this.name + "-" + team);
        System.out.println("New player : " + this.name + " is in team " + team);
    }

    Joueurs(String name, int team, long id)
    {
        turn = false;
        this.IA = true;
        this.name = name;
        this.team = team;
        this.msg = null;
        this.id = id;
        this.session = session;
        this.score = 0;
        if (!this.isIA())
            session.write("INFO:" + this.name + "-" + team);
        System.out.println("New player : " + this.name + " is in team " + team);
    }

    public int getScore() {
        return score;
    }

    public boolean isTurn() { return this.turn;}

    public boolean isIA() {return this.IA;}

    public String getMsg() {return this.msg;}

    public void setMsg(String msg) {this.msg = msg;}

    public void setTurn(boolean t) {this.turn = t;}

    public int getTeam()
    {
        return this.team;
    }

    public long getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public Cards[] getMain()
    {
        return this.main;
    }

    public void setCards(Paquet pack)
    {
       this.main = pack.piocher(8);
    }

    public void setName(String name) {this.name = name;}

    public void setTeam(int t) {this.team = t;}

    public void setId(long id) {this.id= id;}

    public void setScore(int points) {this.score += points;}

    public void printMain()
    {
        int nb = this.getNbCards();
        String tmp;
        tmp = "CARDS:";
        tmp += nb;
        System.out.print(this.name + ": ");
        for (int i = 0; i < nb; i++) {
            tmp += '-';
            tmp += this.main[i].toString();
            System.out.print(this.main[i].toString());
            System.out.print(" ");
        }
        System.out.print("\n");
        if (!this.isIA())
            session.write(tmp);
    }

    public int getNbCards()
    {
        return this.main.length;
    }

    public int getNbCards(Cards.Couleur c) {
        int total = 0;
        for (int j = 0; j < this.main.length; j++) {
            if (main[j].getCouleur() == c)
                total += 1;
        }
        return total;
    }

    public Cards findCard(String card)
    {
        Paquet p = new Paquet();
        Cards c[] = p.piocher(32);
        for (int i = 0; i < 32; i++)
            if (c[i].toString().equals(card))
                return c[i];
        return null;
    }

    public Cards play(String card)
    {
        Cards check = findCard(card);
        if (!check)
        {
            if (!this.isIA())
                session.write("ERROR");
            return null;
        }
        Cards tmp[] = new Cards[this.getNbCards() - 1];
        int j = 0;
        for (int i = 0; i < this.getNbCards(); i++)
        {
            if (!check.getCouleur().equals(this.getMain()[i].getCouleur()) || !check.getRang().equals(this.getMain()[i].getRang())) {
                tmp[j] = getMain()[i];
                j++;
            }
        }
        this.main = tmp;
        return check;
    }

    public Cards play(String card, Cards.Couleur v, Cards.Couleur atout)
    {
        Cards check = findCard(card);
        if (check == null)
        {
            if (this.isIA() == false)
                session.write("ERROR");
            return null;
        }
        Cards tmp[] = new Cards[this.getNbCards() - 1];
        int j = 0;
        for (int i = 0; i < this.getNbCards(); i++)
        {
            if (!check.getCouleur().equals(this.getMain()[i].getCouleur()) || !check.getRang().equals(this.getMain()[i].getRang())) {
                tmp[j] = getMain()[i];
                j++;
            }
        }
        if (check.getCouleur().equals(v))
        {
            this.main = tmp;
            return check;
        }
        else if(!this.getNbCards(v) && check.getCouleur().equals(atout)) {
            this.main = tmp;
            return check;
        }
        else if(!this.getNbCards(v) && !this.getNbCards(atout)) {
            this.main = tmp;
            return check;
        }
        else {
            if (!this.isIA()) {
                session.write("ERROR");
            }
            return null;
        }
    }
}
