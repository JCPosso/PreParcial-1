package edu.eci.arep.app.springpro.testComponent;

import edu.eci.arep.app.springpro.RequestSpringPro;

public class Controller {

    @RequestSpringPro("/probando")
    public static String tfunction() {
        return "Hello , this work! ";
    }

}