package edu.eci.arep.app.httpserver;

import edu.eci.arep.app.springpro.RequestSpringPro;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Juan C Posso
 * @version 1
 * Servidor web
 */
public class HttpServer {
    private static final HttpServer _instance = new HttpServer();
    public HttpServer(){}

    /*
    *   Método que inicializa el servidor
    * */
    public void start() throws IOException {
        try{
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(36000);
            } catch (IOException e) {
                System.err.println("Could not listen on port: 35000.");
                System.exit(1);
            }
            boolean running = true;
            while (running) {
                Socket clientSocket = null;
                try {
                    System.out.println( "Listo para recibir ..." );
                    clientSocket = serverSocket.accept();
                } catch (IOException e) {
                    System.err.println( "Accept failed." );
                    System.exit( 1 );
                }
                serveConnection(clientSocket);
                clientSocket.close();

            }
            serverSocket.close();
        } catch (IOException e) {
        Logger.getLogger(HttpServer.class.getName()).log( Level.SEVERE, null, e);
        }catch(Exception ex){
        Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metodo  procesar las peticiones.
     * @param clientSocket conexion Socket con cliente
     * @throws IOException Error de lectura del cliente socket
     */
    public void serveConnection (Socket clientSocket) throws Exception {
        OutputStream out= clientSocket.getOutputStream();
        BufferedReader in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
        String inputLine, outputLine;
        HashMap<String,String> request  = new HashMap<String,String>();
        boolean ready = false;
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Received: " + inputLine);
            if(!ready){
                request.put("rq", inputLine);
                ready =true;
            } else {
                String[] line = inputLine.split( ":" );
                if (line.length>1)
                request.put( line[0], line[1] );
            }
            if (!in.ready()) {
                break;
            }
        }
        getResource(request,out);
        in.close();
    }


    /**
     * Genera la respuesta de la peticion del cliente
     * @param ClientSocket OutputStream del socket del cliente
     * @param response información procesada de la petición del cliente
     * @throws IOException Error de escritura del recurso
     */
    public  void getResource(HashMap<String,String> response, OutputStream ClientSocket) throws Exception {
        String outputLine = "";
        if (response.get("rq")!= null) {
            PrintWriter printWriter = new PrintWriter( ClientSocket, true );
            String[] lineRequest = response.get( "rq" ).split( " " );
            String path = lineRequest[1];
            if ( path.equals( "/" ) ) path = "/index.html";
            if (path.contains(".")) {
                byte pathByte[] = findResource( path );
                if ( pathByte != null ) {
                    outputLine = generateHeader( path );
                    printWriter.println( outputLine );
                    ClientSocket.write( pathByte );
                }
            }
            //de lo contrario mirar si es app
            else{
                URI uri = null;
                try {
                    uri = new URI(path);
                    System.out.println("PATHHHHHHHHHHH"+uri.getPath().replace("/Apps",""));
                    getSpringResponse(uri.getPath().replace("/Apps",""), printWriter);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
        ClientSocket.close();
    }

    /**
     * Metodo que obtiene un recurso almacenado.
     * @param path nombre ruta del recurso
     * @return arreglo de bytes del recurso para el outputStream
     * @throws IOException error de lectura del archivo
     */
    private byte[] findResource(String path) throws IOException {
        String dir = "src/main/resources/html_public" +  path;
        File resource= new File(dir);
        byte content[] = new byte[(int)resource.length()];
        if (resource.exists() && resource.isFile() ){
            FileInputStream fileStream = new FileInputStream(resource);
            fileStream.read(content);
            fileStream.close();
        }else{
            content = null;
        }
        return content;
    }

    /**
     * Genera encabezado segun el tipo de recurso solicitado
     * @param path ruta del recurso
     * @return Encabezado string http
     */
    private String generateHeader(String path) {
        String header = "";
        String[] list = path.split("\\.");
        String type = list[list.length-1];
        if( type.equals( "jpg" ) || type.equals( "png" ) ){
            header += "HTTP/1.1 200 OK\r\n Content-Type: image/" + type +"\r\n";
        }else if (type.equals( "html" ) || type.equals( "js" ) || type.equals( "css" )){
            header += "HTTP/1.1 200 OK\r\n Content-Type: text/" + type +"\r\n" + "\r\n";
        }
        return header;
    }

    private void getSpringResponse(String nameuri, PrintWriter out)  throws Exception {
        String newUri = nameuri.substring( 1 );
        System.out.println(nameuri);
        String response = "";
        try{
            Class component  = Class.forName( newUri );
            for ( Method m : component.getMethods()) {
                if (m.isAnnotationPresent( RequestSpringPro.class)) {
                        RequestSpringPro rm = m.getAnnotation(RequestSpringPro.class);
                        response= m.invoke(null).toString();
                    System.out.println(response);
                }
            }
        } catch (IllegalAccessException e) {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, e);
        } catch (IllegalArgumentException e) {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, e);
        } catch (InvocationTargetException e) {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, e);
        }
        String header = "HTTP/1.1 200 OK\r\n"

                + "Content-Type: text/html\r\n"

                + "\r\n";
        out.println(header + response);
    }

}
