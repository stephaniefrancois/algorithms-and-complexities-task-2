package com.francois.algo.pdb.app;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public final class DashboardUI implements SearchEventListener {
    private final Scanner userInput;
    private final PrintStream out;
    private final SearchUI searchUi;
    private List<MenuChoice> menuChoices;

    public DashboardUI(InputStream in, PrintStream out, SearchUI searchUi) {
        Objects.requireNonNull(in);
        Objects.requireNonNull(out);
        Objects.requireNonNull(searchUi);
        this.userInput = new Scanner(System.in);
        this.out = out;
        this.searchUi = searchUi;
        this.searchUi.addListener(this);
        menuChoices = PopulateMenuChoices();
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private List<MenuChoice> PopulateMenuChoices() {
        List<MenuChoice> menu = new ArrayList<>();
        menu.add(new MenuChoice(1, "Search for a PROTEIN DATA STRUCTURE using PDB and CHAIN ..."));
        menu.add(new MenuChoice(2, "Search for a PROTEIN DATA STRUCTURE using exact or partial match on EC NUMBER:"));
        menu.add(new MenuChoice(3, "To exit the application"));
        return menu;
    }

    public void displayMainMenu() {

        out.println("Please type in NUMBER for the menu item you wish to select:");
        for (MenuChoice menuChoice : menuChoices) {
            out.println("   " + menuChoice.getNumber() + ". " + menuChoice.getDescription());
        }

        String userInput = this.userInput.nextLine();
        if (validMenuSelectionMade(userInput)) {
            MenuChoice selectedMenuItem = getSelectedMenuItemIndex(userInput);
            executeActionForMenuSelection(selectedMenuItem);
        } else {
            out.println("'" + userInput + "' is NOT a valid menu item! Please try again.");
        }
        displayMainMenu();
    }

    private void executeActionForMenuSelection(MenuChoice selectedMenuItem) {
        switch (selectedMenuItem.getNumber()) {
            case 1: {
                searchUi.requestSearchCriteriaForPdbAndChain();
                break;
            }
            case 2: {
                searchUi.requestSearchCriteriaForEcNumber();
                break;
            }
            case 3: {
                out.println("You have selected to exit the application. Bye bye.");
                System.exit(0);
                break;
            }
        }
    }

    private MenuChoice getSelectedMenuItemIndex(String userInput) {
        List<MenuChoice> matchingChoices = findMatchingMenuChoices(userInput);
        return matchingChoices.get(0);
    }

    private boolean validMenuSelectionMade(String menuSelection) {
        if (isNumeric(menuSelection)) {
            List<MenuChoice> matchingChoices = findMatchingMenuChoices(menuSelection);

            if (matchingChoices.size() > 0) {
                return true;
            }
        }
        return false;
    }

    private List<MenuChoice> findMatchingMenuChoices(String menuSelection) {
        int userSelection = Integer.parseInt(menuSelection);
        return menuChoices.stream()
                .filter(c -> c.getNumber() == userSelection)
                .collect(Collectors.toList());
    }

    @Override
    public void searchCompleted() {
        this.displayMainMenu();
    }

    private class MenuChoice {
        private final int number;
        private final String description;

        public MenuChoice(int number, String description) {
            this.number = number;
            this.description = description;
        }

        public int getNumber() {
            return number;
        }

        public String getDescription() {
            return description;
        }
    }
}
