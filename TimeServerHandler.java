package Jcoinche.server;

import java.util.Date;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class TimeServerHandler extends IoHandlerAdapter
{
    public Jeu game;

    TimeServerHandler(Jeu game)
    {
        this.game = game;
    }

    public void setJeu(Jeu game)
    {
        this.game = game;
    }

    @Override
    public void exceptionCaught( IoSession session, Throwable cause ) throws Exception
    {
        cause.printStackTrace();
    }

    @Override
    public void messageReceived( IoSession session, Object message ) throws Exception
    {
        String str = message.toString();
        if( str.trim().equalsIgnoreCase("quit") ) {
            if (this.game.debut == true)
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
        if (this.game.debut)
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

    @Override
    public void sessionIdle( IoSession session, IdleStatus status ) throws Exception {
        /*System.out.println("IDLE " + session.getIdleCount(status));
        if (session.getIdleCount(status) == 5) {
            session.close();
            return;
        }*/
    }
}