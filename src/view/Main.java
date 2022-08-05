package view;

import controller.Iflists;
import controller.Options;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Options.deserializeOption(new File("./options.json"));
            Iflists.generateGeneral();
            Iflists.generateGoodcards();
            Iflists.generateTrimmedGoodcards();
            Iflists.generateBanlistWhitelist();
            Iflists.generateBanlistWhitelistTrimmed();
            Iflists.generateDraftWhitelists();
            if (GraphicalConsole.getMessage() != null) {
                if (Options.optionsImpl.displayInfos) GraphicalConsole.showDialog();
                GraphicalConsole.flush();
            }
        } catch (IOException e) {
            new ErrorDialog(e.getMessage()).showDialog();
        }
    }
}
