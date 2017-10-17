package Jcoinche.server;

import org.apache.mina.core.service.IoAcceptor;

import java.util.ArrayList;

public class GameHandler
{

    /**
     * Classe qui va gérer tout le jeu (nombre de tour initialisation etc...)
     * @param acceptor
     *      Les sessions des differents joueurs ainsi
     * @param nb_players
     *      Le nombres de joueurs réels (entre 0 et 4) le reste seront des IA
     */

    public GameHandler(IoAcceptor acceptor, int nb_players) throws InterruptedException {
        boolean start = true;
        int nb = -1;
        while (start) {
            if (acceptor.getManagedSessionCount() == nb_players)
                start = false;
            else
            {
                if (acceptor.getManagedSessionCount() != nb) {
                    nb = acceptor.getManagedSessionCount();
                    System.out.println("Waiting for players to connect : " + acceptor.getManagedSessionCount() + "/" + nb_players + " connected");
                }
            }
        }

        Paquet paquet = new Paquet();
        System.out.println("--------------------------------------------");
        System.out.println("COWABUNGAAAA - Game can start");
        System.out.println("--------------------------------------------");
        ArrayList<Long> intKeys = new ArrayList<>(acceptor.getManagedSessions().keySet());
        Player p1;
        Player p2;
        Player p3;
        Player p4;
        if (nb_players >= 1)
            p1 = new Player(1, intKeys.get(0), acceptor.getManagedSessions().get(intKeys.get(0)));
        else
            p1 = new Player("Leonardo", 1, 42);
        if (nb_players >= 2)
            p2 = new Player(2, intKeys.get(1), acceptor.getManagedSessions().get(intKeys.get(1)));
        else
            p2 = new Player("Donatello", 2, 42);
        if (nb_players >= 3)
            p3 = new Player(1, intKeys.get(2), acceptor.getManagedSessions().get(intKeys.get(2)));
        else
            p3 = new Player("Michelangelo", 1, 42);
        if (nb_players >= 4)
            p4 = new Player(2, intKeys.get(3), acceptor.getManagedSessions().get(intKeys.get(3)));
        else
            p4 = new Player("Raphael", 2, 42);
        Game game = new Game(acceptor, p1, p2, p3, p4, paquet);
        System.out.println("--------------------------------------------");
        TimeServerHandler toto;
        toto = (TimeServerHandler) acceptor.getHandler();
        toto.setGame(game);

        int turn = 1;
        while (game.getScoreTeamA() < 1000 && game.getScoreTeamB() < 1000) {
            while (game.players[0].getNbCards() != 0 && game.getScoreTeamA() < 1000 && game.getScoreTeamB() < 1000) {
                Thread.sleep(4000);
                System.out.println("--------------------------------------------");
                System.out.println("Turn #" + turn);
                System.out.println("--------------------------------------------\n");
                game.players[0].printMain();
                game.players[1].printMain();
                game.players[2].printMain();
                game.players[3].printMain();
                turn++;
                game.turn();
            }
            paquet = new Paquet();
            game.players[0].setCards(paquet);
            game.players[1].setCards(paquet);
            game.players[2].setCards(paquet);
            game.players[3].setCards(paquet);
        }
        if (game.getScoreTeamA() > game.getScoreTeamB()) {
            for (int i = 0; i < 4; i++) {
                if (!game.players[i].isIA())
                    game.players[i].session.write("END: Team A won with " + game.getScoreTeamA() + " points");
            }
        }
        else
        {
            for (int i = 0; i < 4; i++) {
                if (!game.players[i].isIA())
                    game.players[i].session.write("END: Team B won with " + game.getScoreTeamB() + " points");
            }
        }
        if (game.getScoreTeamA() > game.getScoreTeamB())
            System.out.println("END: Team A won with " + game.getScoreTeamA() + " points");
        else
            System.out.println("END: Team B won with " + game.getScoreTeamB() + " points");
        System.exit(0);
        game.end();
    }
}
