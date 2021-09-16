package edu.eci.arsw.moneylaundering;
public class Hilo extends Thread {
    private static Object cerrojo = new Object();
    private boolean pausa;
    public Hilo() {
        pausa = false;
    }

    public void corra() throws InterruptedException {
            while(pausa){
                synchronized(cerrojo){
                    try {
                        cerrojo.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


    @Override
    public void run() {

        try {
            corra();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void pausa(){
        pausa = true;
    }
    public void reanudar(){
        pausa = false;
        synchronized(cerrojo){
            cerrojo.notifyAll();
        }
    }

}