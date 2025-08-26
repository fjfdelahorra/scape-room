# Escape Room: Monosala Legacy

Pequeño juego de escape basado en código. Resuelve los puzzles para conseguir las palabras clave y salir de la sala.

## Requisitos
- [Java Development Kit 21](https://jdk.java.net/)
- [Apache Maven 3.x](https://maven.apache.org/)

## Levantar el servidor
1. Compila el proyecto:
   ```bash
   mvn package
   ```
   Esto genera el artefacto `target/escape-room-java-1.0.0.jar`.
2. Ejecuta el servidor HTTP embebido:
   ```bash
   java --add-modules jdk.httpserver -jar target/escape-room-java-1.0.0.jar
   ```
   El servicio quedará disponible en `http://localhost:8080`.

> Si prefieres ejecutar sin empaquetar, usa:
> ```bash
> mvn exec:java -Dexec.mainClass=com.chapterescape.Main
> ```

## Cómo jugar
1. En `src/main/java/com/chapterescape` hay cuatro clases `PuzzleX_*.java`. Cada una contiene un pequeño bug.
2. Corrige el bug y ejecuta la clase para obtener un token:
   ```bash
   mvn exec:java -Dexec.mainClass=com.chapterescape.Puzzle1_OffByOne
   ```
   Repite el proceso con las demás (`Puzzle2_Deadlock`, `Puzzle3_RegexIntel`, `Puzzle4_CryptoChain`) hasta reunir los cuatro tokens.
3. Con el servidor levantado, abre `http://localhost:8080` en tu navegador e introduce los tokens en el formulario.
4. Si los cuatro tokens son correctos, el juego te dará la enhorabuena ¡y habrás escapado!

¡Diviértete!
