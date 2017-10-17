package Jcoinche.server;

import org.apache.mina.core.session.IoSession;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by oreo on 21/11/16.
 * Classe représentant un joueur
 */
public class Player {
    /**Liste des Cartes du joueur**/
    private Cartes[] main;
    /**Booleen pour savoir si oui ou non c'est le tour du joueur**/
    private boolean turn;
    /** Nom du joueur **/
    private String name;
    /** Dernier message envoyé par le client**/
    private String msg;
    /** La Team à laquelle il appartient **/
    private int team;
    /** Son id distinctif **/
    private long id;
    /** Le score total du joueur **/
    private int score;
    /** Permet de savoir si c'est un client ou une IA **/
    private boolean IA;
    /** La session avec laquelle le client est connecté au serveur **/
    IoSession session;

    /**
     * Constructeur
     * @param team
     *      Team A ou B (1 / 2)
     * @param id
     *      Id correspondant à sa session
     * @param session
     *      Session du client permettant au serveur de communiquer avec.
     */
    Player(int team, long id, IoSession session)
    {
        turn = false;
        IA = false;
        this.name = String.format("Player%d", id);
        this.team = team;
        this.session = session;
        this.msg = null;
        this.id = id;
        this.score = 0;
        if (!this.isIA())
            session.write("INFO:" + this.name + "-" + team);
        System.out.println("New player : " + this.name + " is in team " + team);
    }

    /**
     * Constructeur réservé aux IA
     * @param name
     *      Nom de l'IA (une des tortues ninja)
     * @param team
     *      Team A ou B (1 / 2)
     * @param id
     *      Id aléatoire
     */
    Player(String name, int team, long id)
    {
        turn = false;
        this.IA = true;
        this.name = name;
        this.team = team;
        this.msg = null;
        this.id = id;
        this.session = session;
        this.score = 0;
        if (this.isIA() == false)
            session.write("INFO:" + this.name + "-" + team);
        System.out.println("New player : " + this.name + " is in team " + team);
    }

    /**
     * Permet de recevoir le score du joueur
     * @return
     *      Le score du joueur
     */
    public int getScore() {
        return score;
    }

    /**
     * Permet de savoir si c'est le tour du joueur ou non
     * @return
     *      Boolean indiquant si oui ou non le joueur peut jouer
     */
    public boolean isTurn() { return this.turn;}

    /**
     * Indique si oui ou non le joueur est une IA
     * @return
     *      true ou false
     */
    public boolean isIA() {return this.IA;}

    /**
     * Permet de récupérer le dernier message envoyé par le joueur
     * @return
     *      le dernier message ecrit par le joueur
     */
    public String getMsg() {return this.msg;}

    /**
     * Permet de set un message au joueur
     * @param msg
     *      Le message que l'on veut appliquer au joueur
     */
    public void setMsg(String msg) {this.msg = msg;}

    /**
     * Permet d'indiquer si c'est le tour du joueur
     * @param t
     *      true ou false
     */
    public void setTurn(boolean t) {this.turn = t;}

    /**
     * Permet de savoir à quelle Team appartient le joueur
     * @return
     *      La team du joueur
     */
    public int getTeam()
    {
        return this.team;
    }

    /**
     * Permet de récupérer l'ID du joueur
     * @return
     *      ID
     */
    public long getId()
    {
        return this.id;
    }

    /**
     * Permet de récupérer le nom du joueur
     * @return
     *      Nom du joueur
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Permet de connaitre la main du joueur
     * @return
     *      La liste des cartes
     */
    public Cartes[] getMain()
    {
        return this.main;
    }

    /**
     * Permet de set la main d'un joueur en piochiant 8 cartes
     * @param paquet
     *      Le paquet dans lequel on va piocher
     */
    public void setCards(Paquet paquet)
    {
       this.main = paquet.piocher(8);
    }

    /**
     * Permet de set le nom d'un joueur
     * @param name
     *      Name
     */
    public void setName(String name) {this.name = name;}

    /**
     * Permet de set la team d'un joueur
     * @param t
     *      La team (1 ou 2)
     */
    public void setTeam(int t) {this.team = t;}

    /**
     * Permet de set l'id du joueur
     * @param id
     *      l'id à set
     */
    public void setId(long id) {this.id= id;}

    /**
     * Permet d'ajouter des points au score d'un joueur
     * @param points
     *      nb de points à ajouter
     */
    public void setScore(int points) {this.score += points;}

    /**
     * Envoie au client la main du joueur
     */
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
        if (this.isIA() == false)
            session.write(tmp);
    }

    /**
     * Permet de connaitre le nombre de cartes d'un joueur
     * @return
     *      Le nombre de cartes
     */
    public int getNbCards()
    {
        //System.out.println("Joueur : " + this.name + " a " + this.main.length + " cartes");
        return this.main.length;
    }

    /**
     * Permet de connaitre le nombre de cartes d'une certaine couleur d'un joueur
     * @param c
     *      La couleur désirée
     * @return
     *      Le nombre de cartes de la couleur demandée
     */
    public int getNbCards(Cartes.Couleur c) {
        int total = 0;
        for (int j = 0; j < this.main.length; j++) {
            if (main[j].getCouleur() == c)
                total += 1;
        }
        //System.out.println("Joueur : " + this.name + " a " + total + " cartes de " + c);
        return total;
    }

    /**
     * Crée une carte à partir d'une String
     * @param card
     *      La String représetant la carte (Rang_Couleur)
     * @return
     *      La carte si existante ou null dans le cas contraire
     */
    public Cartes findCard(String card)
    {
        Paquet p = new Paquet();
        Cartes c[] = p.piocher(32);
        for (int i = 0; i < 32; i++)
            if (c[i].toString().equals(card))
                return c[i];
        return null;
    }

    /**
     * Permet au premier joueur de jouer une carte
     * @param card
     *      La String représetant la carte jouée
     * @return
     *      Null si il ne peut pas jouer cette carte sinon la carte jouée
     */
    public Cartes play(String card)
    {
        Cartes check = findCard(card);
        if (check == null)
        {
            if (this.isIA() == false)
                session.write("ERROR");
            return null;
        }
        Cartes tmp[] = new Cartes[this.getNbCards() - 1];
        int j = 0;
        for (int i = 0; i < this.getNbCards(); i++)
        {
            if (check.getCouleur().equals(this.getMain()[i].getCouleur()) == false || check.getRang().equals(this.getMain()[i].getRang()) == false) {
                tmp[j] = getMain()[i];
                j++;
            }
        }
        this.main = tmp;
        return check;
    }

    /**
     * Permet au joueur de jouer une carte
     * @param card
     *      La String représetant la carte jouée
     * @param v
     *      La couleur à jouer pour ce tour
     * @param atout
     *      La couleur de l'atout à jouer
     * @return
     *      Null si il ne peut pas jouer cette carte sinon la carte jouée
     */
    public Cartes play(String card, Cartes.Couleur v, Cartes.Couleur atout)
    {
        Cartes check = findCard(card);
        if (check == null)
        {
            if (this.isIA() == false)
                session.write("ERROR");
            return null;
        }
        Cartes tmp[] = new Cartes[this.getNbCards() - 1];
        int j = 0;
        for (int i = 0; i < this.getNbCards(); i++)
        {
            if (check.getCouleur().equals(this.getMain()[i].getCouleur()) == false || check.getRang().equals(this.getMain()[i].getRang()) == false) {
                tmp[j] = getMain()[i];
                j++;
            }
        }
        if (check.getCouleur().equals(v))
        {
            this.main = tmp;
            return check;
        }
        else if(this.getNbCards(v) == 0 && check.getCouleur().equals(atout)) {
            this.main = tmp;
            return check;
        }
        else if(this.getNbCards(v) == 0 && this.getNbCards(atout) == 0) {
            this.main = tmp;
            return check;
        }
        else {
            if (this.isIA() == false) {
                session.write("ERROR");
            }
            return null;
        }
        //return null;
    }
}
