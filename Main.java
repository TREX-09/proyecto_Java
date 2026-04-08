import java.util.Scanner;

// Clase abstracta
abstract class Personaje {

    // Propiedades
    protected int vida;
    protected int defensaActual;
    protected int contadorAtaques;
    protected int pocionesRestantes;
    protected boolean especialDisponible;

    // Constructor
    public Personaje(int vida, int defensaActual, int contadorAtaques, int pocionesRestantes, boolean especial) {
        this.vida = vida;
        this.defensaActual = defensaActual;
        this.contadorAtaques = contadorAtaques;
        this.pocionesRestantes = pocionesRestantes;
        this.especialDisponible = especial;
    }

    // Métodos
    public int atacar() {
        int daño = (int)(Math.random() * (30 - 18 + 1)) + 18;
        contadorAtaques++;

        if (contadorAtaques >= 3) {
            especialDisponible = true;
        }

        return daño;
    }

    public int defender() {
        defensaActual = (int)(Math.random() * (25 - 5 + 1)) + 5;
        return defensaActual;
    }

    public int ataqueEspecial(int defensaEnemigo) {
        if (!especialDisponible) return 0;

        int daño = 40 - (defensaEnemigo - 10);
        contadorAtaques = 0;
        especialDisponible = false;

        return Math.max(daño, 0);
    }

    public void usarPocion() {
        if (pocionesRestantes > 0) {
            vida += 20;
            if (vida > 100) vida = 100;
            pocionesRestantes--;
        }
    }

    public void recibirDaño(int daño) {
        int dañoReal = daño - defensaActual;
        if (dañoReal < 0) dañoReal = 0;
        vida -= dañoReal;
        if (vida < 0) vida = 0;
    }

    public boolean estaVivo() {
        return vida > 0;
    }

    //destructor
    @Override
    protected void finalize() throws Throwable{
        super.finalize();
    }
}

// Clase heredada
class Jugador extends Personaje {
    public Jugador(int vida, int defensaActual, int contadorAtaques, int pocionesRestantes, boolean especial) {
        super(vida, defensaActual, contadorAtaques, pocionesRestantes, especial);
    }
}

// Clase principal
public class Main {
    public static void main(String[] args) {

        try (Scanner sc = new Scanner(System.in)) {
            Personaje jugador = new Jugador(100, 0, 0, 2, false);
            Personaje enemigo = new Jugador(100, 0, 0, 1, false);
            
            while (jugador.estaVivo() && enemigo.estaVivo()) {
                
                System.out.println("\n--- TU TURNO ---");
                System.out.println("1. Atacar");
                System.out.println("2. Defender");
                System.out.println("3. Ataque especial");
                System.out.println("4. Usar poción");
                
                int opcion = sc.nextInt();
                
                switch (opcion) {
                    case 1 -> {
                        int daño = jugador.atacar();
                        enemigo.recibirDaño(daño);
                        System.out.println("Atacaste con " + daño);
                    }
                        
                    case 2 -> {
                        jugador.defender();
                        System.out.println("Te defendiste");
                    }
                        
                    case 3 -> {
                        int dañoEsp = jugador.ataqueEspecial(enemigo.defensaActual);
                        enemigo.recibirDaño(dañoEsp);
                        System.out.println("Ataque especial: " + dañoEsp);
                    }
                        
                    case 4 -> {
                        jugador.usarPocion();
                        System.out.println("Usaste poción");
                    }
                        
                    default -> System.out.println("Opción inválida");
                }
                
                if (!enemigo.estaVivo()) break;
                
                // Turno enemigo 
                System.out.println("\n--- TURNO ENEMIGO ---");
                int accion = (int)(Math.random() * 3) + 1;
                
                switch (accion) {
                    case 1 -> {
                        int daño = enemigo.atacar();
                        jugador.recibirDaño(daño);
                        System.out.println("El enemigo ataca con " + daño);
                    }
                        
                    case 2 -> {
                        enemigo.defender();
                        System.out.println("El enemigo se defiende");
                    }
                        
                   case 3 -> {
                        if (enemigo.especialDisponible) {
                         int dañoEsp = enemigo.ataqueEspecial(jugador.defensaActual);
                          jugador.recibirDaño(dañoEsp);
                          System.out.println("El enemigo usa ataque especial: " + dañoEsp);
                     } else {
                             // cambia a ataque normal
                             int daño = enemigo.atacar();
                             jugador.recibirDaño(daño);
                            System.out.println("El enemigo no tenía especial y atacó: " + daño);
                             }
                    }

                    case 4 -> {
                        enemigo.usarPocion();
                          System.out.println("enemigo uso poción");
                    }
                }
                
                System.out.println("\nVida jugador: " + jugador.vida);
                System.out.println("Vida enemigo: " + enemigo.vida);
            }
            
            if (jugador.estaVivo()) {
                System.out.println("¡Ganaste!");
            } else {
                System.out.println("Perdiste...");
            }
        }
    }
}