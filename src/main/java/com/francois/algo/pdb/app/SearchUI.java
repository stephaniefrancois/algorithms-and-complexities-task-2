package com.francois.algo.pdb.app;

import com.francois.algo.pdb.RootLogger;
import com.francois.algo.pdb.common.IRaiseEvents;
import com.francois.algo.pdb.common.ListenersManager;
import com.francois.algo.pdb.composition.ServiceFactory;
import com.francois.algo.pdb.core.PdbChainFinder;
import com.francois.algo.pdb.core.domain.AppException;
import com.francois.algo.pdb.core.domain.PdbChainDescriptor;
import com.francois.algo.pdb.core.search.PdbChainMatcher;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SearchUI implements IRaiseEvents<SearchEventListener> {
    private static final Logger log = RootLogger.get();
    private final Scanner userInput;
    private final PrintStream out;
    private final PdbChainFinder finder;
    private final ListenersManager<SearchEventListener> listenersManager;
    private final ServiceFactory serviceFactory;
    private final SearchResultsPrinterUI searchResultsPrinterUi;

    public SearchUI(InputStream in,
                    PrintStream out,
                    PdbChainFinder finder,
                    SearchResultsPrinterUI searchResultsPrinterUi,
                    ListenersManager<SearchEventListener> listenersManager,
                    ServiceFactory serviceFactory) {
        Objects.requireNonNull(in);
        Objects.requireNonNull(out);
        Objects.requireNonNull(finder);
        Objects.requireNonNull(searchResultsPrinterUi);
        Objects.requireNonNull(listenersManager);
        Objects.requireNonNull(serviceFactory);
        this.userInput = new Scanner(System.in);
        this.out = out;
        this.finder = finder;
        this.searchResultsPrinterUi = searchResultsPrinterUi;
        this.listenersManager = listenersManager;
        this.serviceFactory = serviceFactory;
    }

    public void requestSearchCriteriaForPdbAndChain() {
        String pdbValue = getCriteria("Please enter valid PDB value for search, ex.: '102l' or '11as'");
        String chainValue = getCriteria("Now please enter valid CHAIN value for search, ex.: 'A' or '1'");
        PdbChainMatcher matcher = this.serviceFactory.createMatcherByPdbAndChain(pdbValue, chainValue);
        Comparator<PdbChainDescriptor> comparator = this.serviceFactory.createComparator();
        executeSearch(matcher, comparator);
    }

    private void executeSearch(PdbChainMatcher matcher, Comparator<PdbChainDescriptor> comparator) {
        try {
            List<PdbChainDescriptor> descriptors = this.finder.findMatchingPdbChains(
                    matcher,
                    comparator
            );
            this.searchResultsPrinterUi.print(descriptors);
        } catch (Exception e) {
            this.out.println(String.format("Failed to execute search for '%s'!", matcher.toString()));
            this.out.println();
            logSearchFailedForPdbAndChain(matcher.toString(), e);
        } finally {
            this.listenersManager.notifyListeners(l -> l.searchCompleted());
        }
    }

    public void requestSearchCriteriaForEcNumber() {
        this.printPossibleEcNumberCombinations();
        String ecNumber = getCriteria("Please enter valid FULL or PARTIAL EC NUMBER value for search.");
        PdbChainMatcher matcher = this.serviceFactory.createMatcherByEcNumber(ecNumber);
        Comparator<PdbChainDescriptor> comparator = this.serviceFactory.createComparator();
        executeSearch(matcher, comparator);
    }

    private void printPossibleEcNumberCombinations() {
        this.out.println();
        this.out.println("  Accepted EC NUMBER formats are as follows:");
        this.out.println("  1.1.1.1 = Full Match");
        this.out.println("  1.1.1.- = Partial Match");
        this.out.println("  1.1.-.1 = Partial Match");
        this.out.println("  1.-.1.1 = Partial Match");
        this.out.println("  1.1.-.- = Partial Match");
        this.out.println("  1.-.-.1 = Partial Match");
        this.out.println("  1.-.1.- = Partial Match");
        this.out.println(" Where '-' matches any number from 0 to 9 and first LEVEL must be supplied at all times.");
        this.out.println(" No more than 2 wildcards can be used! And 1 can be any NUMBER from 0 to 99 or more");
        this.out.println();
    }

    private String getCriteria(String message) {
        this.out.println(message);
        String userInput = this.userInput.nextLine();
        if (userInput == null || userInput.isEmpty() || userInput.trim().isEmpty()) {
            this.out.println("Entered value is NOT valid! Please try again:");
            this.getCriteria(message);
        }

        return userInput;
    }

    @Override
    public void addListener(SearchEventListener listenerToAdd) {
        this.listenersManager.addListener(listenerToAdd);
    }

    @Override
    public void removeListener(SearchEventListener listenerToRemove) {
        this.listenersManager.addListener(listenerToRemove);
    }

    private void logSearchFailedForPdbAndChain(String searchCriteria, Exception e) {
        log.log(Level.SEVERE, e, () -> String.format("Failed to execute search for '%s'!", searchCriteria));
    }
}
