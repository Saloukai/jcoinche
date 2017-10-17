package Jcoinche.server;

/**
 * Created by oreo on 21/11/16.
 */
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * Main du serveur Initialise l'acceptor et appelle le GameHandler
 */
public class MinaTimeServer
{
    public static void main( String[] args ) throws IOException {
        Game game = new Game();
        Scanner reader = new Scanner(System.in);
        int port = 0;
        while (port < 1024 || port > 65535) {
            System.out.println("Enter port number (Choose between 1024 and 65535)");
            port = reader.nextInt();
        }
        int players = -1;
        while (players < 0 || players > 4) {
            System.out.println("How many real players do you want ? (Choose between 0 and 4)");
            players = reader.nextInt();
        }
        IoAcceptor acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast( "logger", new LoggingFilter() );
        acceptor.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new TextLineCodecFactory( Charset.forName( "UTF-8" ))));
        acceptor.setHandler(  new TimeServerHandler(game) );
        acceptor.getSessionConfig().setReadBufferSize( 2048 );
        acceptor.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, 10 );
        acceptor.bind( new InetSocketAddress(port) );
        try {
            new GameHandler(acceptor, players);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
