package si2026.alejandrodelpozoalu.p05;

import java.io.*;
import java.util.*;

public class Practica_04 {

    static int n;
    static int[][] tablero;

    static int[] sumaFilasObjetivo;
    static int[] sumaColumnasObjetivo;

    static int[] sumaFilasActual;
    static int[] sumaColumnasActual;
    
    static int[][] sumaFilasRestante;
    static int[][] sumaColumnasRestante;

    static Variable[][] variables;

    public static void main(String[] args) {

        String ficheroEntrada = "./src/tableros.txt";
        String ficheroSalida = "./src/soluciones.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(ficheroEntrada));
             BufferedWriter bw = new BufferedWriter(new FileWriter(ficheroSalida))) {

            String linea;

            while ((linea = br.readLine()) != null) {
                cargarTablero(linea);
                inicializarVariables();

                AC3 ac3 = new AC3();
                ac3.ejecutar(variables);

                boolean solucion = backtracking(0, 0);

                if (solucion) bw.write(obtenerSolucion());
                else bw.write("Sin solucion");

                bw.newLine();
            }

            System.out.println("Soluciones generadas");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void cargarTablero(String entrada) {
        String[] partes = entrada.split(";");
        n = partes.length - 1;

        tablero = new int[n][n];
        sumaFilasObjetivo = new int[n];
        sumaColumnasObjetivo = new int[n];
        
        sumaFilasActual = new int[n];
        sumaColumnasActual = new int[n];
        
        sumaFilasRestante = new int[n][n];
        sumaColumnasRestante = new int[n][n];

        for (int fila = 0; fila < n; fila++) {
            String[] numeros = partes[fila].trim().split("\\s+"); // \\s+ representa uno o más espacios en blanco

            for (int columna = 0; columna < n; columna++) {
                tablero[fila][columna] = Integer.parseInt(numeros[columna]);
            }
            sumaFilasObjetivo[fila] = Integer.parseInt(numeros[n]);
        }

        String[] columnas = partes[n].trim().split("\\s+");
        for (int columna = 0; columna < n; columna++) {
            sumaColumnasObjetivo[columna] = Integer.parseInt(columnas[columna]);
        }

        // Precalcular la suma máxima que queda disponible hacia la derecha y hacia abajo
        for (int fila = 0; fila < n; fila++) {
            int acumulado = 0;
            for (int columna = n - 1; columna >= 0; columna--) {
                acumulado += tablero[fila][columna];
                sumaFilasRestante[fila][columna] = acumulado;
            }
        }

        for (int columna = 0; columna < n; columna++) {
            int acumulado = 0;
            for (int fila = n - 1; fila >= 0; fila--) {
                acumulado += tablero[fila][columna];
                sumaColumnasRestante[fila][columna] = acumulado;
            }
        }
    }

    static void inicializarVariables() {
        variables = new Variable[n][n];
        for (int fila = 0; fila < n; fila++) {
            for (int columna = 0; columna < n; columna++) {
                variables[fila][columna] = new Variable(fila, columna);
            }
        }
    }

    static boolean backtracking(int fila, int columna) {
        if (fila == n) {
            return true; 
        }

        int siguienteFila = fila;
        int siguienteColumna = columna + 1;

        if (siguienteColumna == n) {
            siguienteColumna = 0;
            siguienteFila++;
        }

        Variable variable = variables[fila][columna];

        for (Integer valor : variable.dominio) {
            variable.valor = valor;

            int valorSumado = (valor == 1) ? tablero[fila][columna] : 0;
            sumaFilasActual[fila] += valorSumado;
            sumaColumnasActual[columna] += valorSumado;

            // Forward Checking
            if (esConsistenteParcial(fila, columna)) {
                if (backtracking(siguienteFila, siguienteColumna)) {
                    return true;
                }
            }

            // Backtrackin
            sumaFilasActual[fila] -= valorSumado;
            sumaColumnasActual[columna] -= valorSumado;
            variable.valor = -1;
        }

        return false;
    }

    static boolean esConsistenteParcial(int fila, int columna) {
        // Suma actual supera el objetivo; podar
        if (sumaFilasActual[fila] > sumaFilasObjetivo[fila]) return false;
        if (sumaColumnasActual[columna] > sumaColumnasObjetivo[columna]) return false;

        // Final de una fila; la suma tiene que ser igual a la del objetivo
        if (columna == n - 1) {
            if (sumaFilasActual[fila] != sumaFilasObjetivo[fila]) return false;
        } else {
            // Forward checking: Verificamos si con lo que queda disponible en la fila se puede alcanzar el objetivo
            int maximoPosibleFila = sumaFilasActual[fila] + sumaFilasRestante[fila][columna + 1];
            if (maximoPosibleFila < sumaFilasObjetivo[fila]) return false;
        }

        // Final de una columna; la suma tiene que ser igual a la del objetivo
        if (fila == n - 1) {
            if (sumaColumnasActual[columna] != sumaColumnasObjetivo[columna]) return false;
        } else {
            // Forward checking otra vez
            int maximoPosibleColumna = sumaColumnasActual[columna] + sumaColumnasRestante[fila + 1][columna];
            if (maximoPosibleColumna < sumaColumnasObjetivo[columna]) return false;
        }

        return true;
    }

    static String obtenerSolucion() {
        StringBuilder sb = new StringBuilder();
        for (int fila = 0; fila < n; fila++) {
            for (int columna = 0; columna < n; columna++) {
                if (variables[fila][columna].valor == 1) sb.append(tablero[fila][columna]);
                else sb.append(".");
                sb.append(" ");
            }
            sb.append(sumaFilasObjetivo[fila]);
            sb.append(" ; ");
        }
        
        for (int columna = 0; columna < n; columna++) {
            sb.append(sumaColumnasObjetivo[columna]);
            if (columna < n - 1) sb.append(" ");
        }
        return sb.toString();
    }
}

class Variable {
    int fila;
    int columna;
    int valor;
    List<Integer> dominio;

    Variable(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        valor = -1;
        dominio = new ArrayList<>();
        dominio.add(0); // No incluir número
        dominio.add(1); // Incluir número
    }
}

class Arco {
    Variable origen;
    Variable destino;

    Arco(Variable origen, Variable destino) {
        this.origen = origen;
        this.destino = destino;
    }
}

/**
 * AC3: Si un valor individual del tablero supera por sí mismo el objetivo de la fila o columna,
 * el valor '1' se elimina inmediatamente del dominio de esa variable antes de buscar.
 */
class AC3 {
    void ejecutar(Variable[][] variables) {
        int n = variables.length;
        for (int f = 0; f < n; f++) {
            for (int c = 0; c < n; c++) {
                if (Practica_04.tablero[f][c] > Practica_04.sumaFilasObjetivo[f] || 
                    Practica_04.tablero[f][c] > Practica_04.sumaColumnasObjetivo[c]) {
                    variables[f][c].dominio.remove(Integer.valueOf(1)); 
                }
            }
        }
    }
}