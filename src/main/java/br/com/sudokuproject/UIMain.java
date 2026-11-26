package br.com.sudokuproject;

import br.com.sudokuproject.ui.custom.screen.MainScreen;
import br.com.sudokuproject.util.GameConfigParser;

public class UIMain {

    public static void main(String[] args) {
        final var gameConfig = parseGameConfig(args);
        if (gameConfig == null) return;
        var mainsScreen = new MainScreen(gameConfig);
        mainsScreen.buildMainScreen();
    }

    private static java.util.Map<String, String> parseGameConfig(final String[] args) {
        try {
            return GameConfigParser.parse(args);
        } catch (IllegalArgumentException exception) {
            System.err.println(exception.getMessage());
            System.err.println("Uso: coluna,linha;valor,fixed (informe todas as 81 posições)");
            return null;
        }
    }
}
