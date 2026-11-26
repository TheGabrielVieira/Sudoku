package br.com.sudokuproject.util;

import java.util.HashMap;
import java.util.Map;

public final class GameConfigParser {

    private static final int BOARD_LIMIT = 9;

    private GameConfigParser() {}

    public static Map<String, String> parse(final String[] args) {
        final Map<String, String> gameConfig = new HashMap<>();

        for (int i = 0; i < args.length; i++) {
            final var argument = args[i];
            final var parts = argument.split(";", -1);
            if (parts.length != 2) {
                throw invalidArgument(i, "formato esperado: coluna,linha;valor,fixed");
            }

            final var position = parts[0].split(",", -1);
            if (position.length != 2) {
                throw invalidArgument(i, "posição deve estar no formato coluna,linha");
            }

            final int col = parseCoordinate(position[0], i);
            final int row = parseCoordinate(position[1], i);
            final var values = parts[1].split(",", -1);
            if (values.length != 2) {
                throw invalidArgument(i, "valor deve estar no formato número,true|false");
            }

            final int expected;
            try {
                expected = Integer.parseInt(values[0]);
            } catch (NumberFormatException exception) {
                throw invalidArgument(i, "o valor esperado deve ser um número entre 1 e 9");
            }
            if (expected < 1 || expected > 9) {
                throw invalidArgument(i, "o valor esperado deve estar entre 1 e 9");
            }
            if (!values[1].equalsIgnoreCase("true") && !values[1].equalsIgnoreCase("false")) {
                throw invalidArgument(i, "fixed deve ser true ou false");
            }

            final var key = "%s,%s".formatted(col, row);
            if (gameConfig.putIfAbsent(key, "%s,%s".formatted(expected, values[1])) != null) {
                throw invalidArgument(i, "posição %s informada mais de uma vez".formatted(key));
            }
        }

        final int expectedPositions = BOARD_LIMIT * BOARD_LIMIT;
        if (gameConfig.size() != expectedPositions) {
            throw new IllegalArgumentException(
                    "Configuração incompleta: esperadas %s posições, recebidas %s."
                            .formatted(expectedPositions, gameConfig.size()));
        }

        return gameConfig;
    }

    private static int parseCoordinate(final String value, final int argumentIndex) {
        final int coordinate;
        try {
            coordinate = Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw invalidArgument(argumentIndex, "coordenadas devem ser números entre 0 e 8");
        }
        if (coordinate < 0 || coordinate >= BOARD_LIMIT) {
            throw invalidArgument(argumentIndex, "coordenadas devem estar entre 0 e 8");
        }
        return coordinate;
    }

    private static IllegalArgumentException invalidArgument(final int argumentIndex, final String reason) {
        return new IllegalArgumentException("Argumento %s inválido: %s."
                .formatted(argumentIndex + 1, reason));
    }
}
