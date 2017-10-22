package Jcoinche.server;

import org.apache.mina.core.service.IoAcceptor;

import java.util.ArrayList;

public class JeuHandler
{
    public JeuHandler(IoAcceptor ioa, int nb_players) throws InterruptedException {
        boolean debut = true;
        int nb = -1;
        while (debut)
        {
            if (ioa.getManagedSessionCount() == nb_players)
                debut = false;
            else
            {
                if (ioa.getManagedSessionCount() != nb)
                {
                    nb = ioa.getManagedSessionCount();
                    System.out.println("Waiting for players to connect : " + ioa.getManagedSessionCount() + "/" + nb_players + " connected");
                }
            }
        }

        Paquet pack = new Paquet();
        System.out.println("--------------------------------------------");
        System.out.println("Let's Go !");
        System.out.println("--------------------------------------------");
        ArrayList<Long> intKeys = new ArrayList<>(ioa.getManagedSessions().keySet());
        Joueurs jA;
        Joueurs jB;
        Joueurs jC;
        Joueurs jD;
        if (nb_players >= 1)
            jA = new Joueurs(1, intKeys.get(0), ioa.getManagedSessions().get(intKeys.get(0)));
        else
            jA = new Joueurs("Jean", 1, 42);
        if (nb_players >= 2)
            jB = new Joueurs(2, intKeys.get(1), ioa.getManagedSessions().get(intKeys.get(1)));
        else
            jB = new Joueurs("Frederique", 2, 42);
        if (nb_players >= 3)
            jC = new Joueurs(1, intKeys.get(2), ioa.getManagedSessions().get(intKeys.get(2)));
        else
            jC = new Joueurs("Yoan", 1, 42);
        if (nb_players >= 4)
            jD = new Joueurs(2, intKeys.get(3), ioa.getManagedSessions().get(intKeys.get(3)));
        else
            jD = new Joueurs("Paul", 2, 42);
        Jeu game = new Jeu(ioa, jA, jB, jC, jD, pack);
        System.out.println("--------------------------------------------");
        TimeServerHandler toto;
        toto = (TimeServerHandler) ioa.getHandler();
        toto.setJeu(game);

        int turn = 1;
        while (game.getScoreTeamA() < 1000 && game.getScoreTeamB() < 1000)
        {
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
            pack = new Paquet();
            game.players[0].setCards(pack);
            game.players[1].setCards(pack);
            game.players[2].setCards(pack);
            game.players[3].setCards(pack);
        }
        if (game.getScoreTeamA() > game.getScoreTeamB())
        {
            for (int i = 0; i < 4; i++)
                if (!game.players[i].isIA())
                    game.players[i].session.write("END: Team A won with " + game.getScoreTeamA() + " points");
        }
        else
        {
            for (int i = 0; i < 4; i++)
                if (!game.players[i].isIA())
                    game.players[i].session.write("END: Team B won with " + game.getScoreTeamB() + " points");
        }
        if (game.getScoreTeamA() > game.getScoreTeamB())
            System.out.println("END: Team A won with " + game.getScoreTeamA() + " points");
        else
            System.out.println("END: Team B won with " + game.getScoreTeamB() + " points");
        System.exit(0);
        game.end();
    }
}
