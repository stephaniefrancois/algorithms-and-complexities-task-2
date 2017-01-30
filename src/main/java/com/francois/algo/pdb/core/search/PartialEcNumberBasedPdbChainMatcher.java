package com.francois.algo.pdb.core.search;

import com.francois.algo.pdb.RootLogger;
import com.francois.algo.pdb.core.domain.PdbChainDescriptor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/*
    Matches full or partial EC NUMBER
    Given search criteria of 2.5.1.7 should match the following:
        1. 2.5.1.-
        2. 2.5.-.7
        3. 2.-.1.7
        4. 2.5.-.-
        5. 2.-.-.-
        6. 2.5.1.7

    Given search criteria of 2.5.1.- should match the following:
        1. 2.5.1.-
        2. 2.5.-.7
        3. 2.-.1.7
        4. 2.5.-.-
        5. 2.-.-.-
        6. 2.5.1.0 to 2.5.1.9

    Given search criteria of 2.5.-.7 should match the following:
        1. 2.5.1.-
        2. 2.5.-.7
        3. 2.-.1.7
        4. 2.5.-.-
        5. 2.-.-.-
        6. 2.5.0.7 to 2.5.9.7

    Given search criteria of 2.-.1.7 should match the following:
        1. 2.5.1.-
        2. 2.5.-.7
        3. 2.-.1.7
        4. 2.5.-.-
        5. 2.-.-.-
        6. 2.0.1.7 to 2.9.1.7

    Given search criteria of 2.5.-.- should match the following:
        1. 2.5.1.-
        2. 2.5.-.7
        3. 2.-.1.7
        4. 2.5.-.-
        5. 2.-.-.-
        6. 2.5.0.0 to 2.5.9.9

    Given search criteria of 2.-.-.7 should match the following:
        1. 2.5.1.-
        2. 2.5.-.7
        3. 2.-.1.7
        4. 2.5.-.-
        5. 2.-.-.-
        6. 2.0.0.7 to 2.9.9.7
 */

public final class PartialEcNumberBasedPdbChainMatcher implements PdbChainMatcher {

    private static final Logger log = RootLogger.get();
    private static final String EC_NUMBER_SEPARATOR = "\\.";
    private static final String MATCH_ANY_WILDCARD = "-";
    private final String searchCriteria;
    private final List<String> searchCriteriaParts;
    private final SearchCriteriaValidator validator;

    public PartialEcNumberBasedPdbChainMatcher(String searchCriteria, SearchCriteriaValidator validator) {
        Objects.requireNonNull(searchCriteria);
        Objects.requireNonNull(validator);
        this.searchCriteria = searchCriteria;
        this.validator = validator;
        this.searchCriteriaParts = Arrays.asList(searchCriteria.split(EC_NUMBER_SEPARATOR));
    }

    @Override
    public final boolean doesMatch(PdbChainDescriptor pdbChainToMatch) {
        if (pdbChainToMatch == null) {
            logNoPdbChainSupplied();
            return false;
        }

        if (!this.validator.valid(searchCriteria)) {
            logInvalidSearchArgument();
            return false;
        }

        return (exactMatch(pdbChainToMatch) || partialMatch(pdbChainToMatch));
    }

    private boolean exactMatch(PdbChainDescriptor pdbChainToMatch) {
        return pdbChainToMatch.getEcNumber().equals(searchCriteria);
    }

    private boolean partialMatch(PdbChainDescriptor pdbChainToMatch) {
        List<String> ecNumberToMatchParts = Arrays.asList(pdbChainToMatch.getEcNumber().split(EC_NUMBER_SEPARATOR));

        for (int index = 0; index < 4; index++) {
            String ecNumberPart = ecNumberToMatchParts.get(index);
            String searchCriteriaPart = searchCriteriaParts.get(index);

            if (ecNumberPart.equals(MATCH_ANY_WILDCARD) ||
                searchCriteriaPart.equals(MATCH_ANY_WILDCARD) ||
                ecNumberPart.equals(searchCriteriaPart)) {
                continue;
            }

            logNoPartialMatch(pdbChainToMatch);

            return false;
        }

        logPartialMatch(pdbChainToMatch);

        return true;
    }

    private void logInvalidSearchArgument() {
        log.warning(() -> String.format("'%s' is not a valid search argument!",
                this.searchCriteria));
    }

    private void logNoPdbChainSupplied() {
        log.fine(() -> String.format("No PDB CHAIN passed in for matching. '%s' failed to match!",
                this.searchCriteria));
    }

    private void logNoPartialMatch(PdbChainDescriptor pdbChainToMatch) {
        log.fine(() -> String.format("PDB CHAIN with '%s' EC NUMBER failed ton partially match given '%s' search criteria!",
                pdbChainToMatch.getEcNumber(),
                this.searchCriteria));
    }

    private void logPartialMatch(PdbChainDescriptor pdbChainToMatch) {
        log.fine(() -> String.format("PDB CHAIN with '%s' EC NUMBER has been partially matched given '%s' search criteria.",
                pdbChainToMatch.getEcNumber(),
                this.searchCriteria));
    }

    @Override
    public String toString() {
        return "PartialEcNumberBasedPdbChainMatcher{" +
                "searchCriteria='" + searchCriteria + '\'' +
                '}';
    }
}
