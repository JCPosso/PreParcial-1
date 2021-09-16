package edu.eci.arep.app.springpro;

import edu.eci.arep.app.httpserver.HttpServer;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        HttpServer server = new HttpServer();
        server.start();
    }
}
