package com.chapterescape;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private static final List<String> EXPECTED = List.of("VERTX", "KAFKA", "REDIS", "JAVA21");

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        Map<String, String> pages = Map.of(
                "/", "bienvenida.html",
                "/bienvenida.html", "bienvenida.html",
                "/puzzle1.html", "puzzle1.html",
                "/puzzle2.html", "puzzle2.html",
                "/puzzle3.html", "puzzle3.html",
                "/puzzle4.html", "puzzle4.html",
                "/formulario.html", "formulario.html",
                "/enhorabuena.html", "enhorabuena.html",
                "/fallo.html", "fallo.html"
        );

        server.createContext("/", exchange -> {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }
            String path = exchange.getRequestURI().getPath();
            String file = pages.get(path);
            if (file == null) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }
            byte[] bytes = Files.readAllBytes(Path.of(file));
            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (var os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        server.createContext("/check", exchange -> {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }
            Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
            List<String> provided = List.of(
                    params.getOrDefault("token1", "").toUpperCase(),
                    params.getOrDefault("token2", "").toUpperCase(),
                    params.getOrDefault("token3", "").toUpperCase(),
                    params.getOrDefault("token4", "").toUpperCase()
            );
            boolean ok = provided.equals(EXPECTED);
            String json = "{\"success\":" + ok + "}";
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (var os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        server.start();
        System.out.println("Servidor iniciado en http://localhost:8080");
    }

    private static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        if (query == null || query.isBlank()) {
            return result;
        }
        for (String param : query.split("&")) {
            String[] pair = param.split("=", 2);
            String key = URLDecoder.decode(pair[0], StandardCharsets.UTF_8);
            String value = pair.length > 1 ? URLDecoder.decode(pair[1], StandardCharsets.UTF_8) : "";
            result.put(key, value);
        }
        return result;
    }
}
