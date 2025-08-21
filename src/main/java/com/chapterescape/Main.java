package com.chapterescape;

import java.util.List;

public class Main {
    // Cuando resolváis cada puzzle, copiad el token aquí:
    private static final String TOKEN1 = "Vertx".toUpperCase();
    private static final String TOKEN2 = "Kafka".toUpperCase();
    private static final String TOKEN3 = "Redis".toUpperCase();
    private static final String TOKEN4 = "Java21".toUpperCase();

    public static void main(String[] args) {
        System.out.println("== ESCAPE ROOM: Monosala Legacy ==");
        System.out.println("Arregla los puzzles, copia los tokens aquí y ejecuta de nuevo.\n");

        var expected = List.of("VERTX","KAFKA","REDIS","JAVA21");
        var provided  = List.of(TOKEN1, TOKEN2, TOKEN3, TOKEN4);

        if (provided.equals(expected)) {
            System.out.println("🎉 Puerta abierta: Váis a producción sin rollback.");
            System.out.println("Código final: VERTX-KAFKA-REDIS-JAVA21");
        } else {
            System.out.println("Aún no. Tokens actuales:");
            for (int i = 0; i < expected.size(); i++) {
                System.out.printf("  TOKEN%d = '%s'%s%n", i+1, provided.get(i),
                        provided.get(i).isEmpty() ? "  <-- falta" :
                        (provided.get(i).equals(expected.get(i)) ? "  ✔" : "  ✖"));
            }
            System.out.println("\nPistas: ejecuta cada PuzzleX con su main y arregla lo que rompe.");
            System.out.println("Ejemplos: mvn -q -DskipTests exec:java -Dexec.mainClass=com.chapterescape.Puzzle1_OffByOne");
        }
    }
}
