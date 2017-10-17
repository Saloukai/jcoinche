package Jcoinche.server;

import java.util.Date;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 * Classe gerant les clients et le serveur
 *
 */
public class TimeServerHandler extends IoHandlerAdapter
{
    /** Stocke le jeu pour acceder au joueurs par la suite **/
    public Game game;

    /**
     * Constructeur
     * @param game
     *      Le jeu à set
     */
    TimeServerHandler(Game game)
    {
        this.game = game;
    }

    /**
     * Permet de set le jeu
     * @param game
     *      Jeu à set
     */
    public void setGame(Game game)
    {
        this.game = game;
    }

    /** Apellé lors d'une exception sur une session
     *
     * @param session
     *      La session en cause
     * @param cause
     *      La cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught( IoSession session, Throwable cause ) throws Exception
    {
        cause.printStackTrace();
    }

    /**
     * Recois un message et l'attribue à un joeur ou quitte le jeu si necessaire
     * @param session
     *      La session ayant envoyé le message
     * @param message
     *      Le message envoyé
     * @throws Exception
     */
    @Override
    public void messageReceived( IoSession session, Object message ) throws Exception
    {
        String str = message.toString();
        if( str.trim().equalsIgnoreCase("quit") ) {
            if (this.game.start == true)
            {
                for (int i = 0; i < 4; i++) {
                    if (game.players[i].isIA() == false) {
                        if (game.players[i].session.equals(session))
                            this.game.end();
                    }
                }

            }
            else
                session.close();
            return;
        }
        if (this.game.start)
        {
            for(int i = 0; i < 4; i++)
            {
                if (game.players[i].isIA() == false) {
                    if (game.players[i].session.equals(session)) {
                        if (game.players[i].isTurn() && str != null) {
                            game.players[i].setMsg(str);
                        }
                    }
                    else
                    {
                        session.write("QUIT");
                        session.close();
                    }
                }
            }
        }
    }

    /**
     * Apellé lorsqu'une session est inactive
     * @param session
     *      La session en cause
     * @param status
     *      Son etat d'inactivité
     * @throws Exception
     */
    @Override
    public void sessionIdle( IoSession session, IdleStatus status ) throws Exception {
        /*System.out.println("IDLE " + session.getIdleCount(status));
        if (session.getIdleCount(status) == 5) {
            session.close();
            return;
        }*/
    }
}