package com.francois.algo.pdb.core.search;

import com.francois.algo.pdb.RootLogger;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/*
    1. First digit must be supplied
	2. At least two digits must be supplied (including the first) for example:
		a. 1.1.-.-
		b. 1.-.1.-
		c. 1.-.-.1
		d. 1._._._ - NOT VALID
		e. _.1.1.1 - NOT VALID
    3. All 4 parts must be supplied for the search criteria
 */
public final class EcNumberBasedSearchCriteriaValidator implements SearchCriteriaValidator {

    private static final Logger log = RootLogger.get();
    private static final String EC_NUMBER_SEPARATOR = "\\.";
    private static final String MATCH_ANY_WILDCARD = "-";
    private static final int EC_LEVELS_COUNT = 4;
    private static final int MAX_WILDCARDS_ALLOWED = 2;

    @Override
    public boolean valid(String searchCriteria) {
        List<String> searchCriteriaParts = Arrays.asList(searchCriteria.split(EC_NUMBER_SEPARATOR));

        if (searchCriteriaParts.size() != EC_LEVELS_COUNT) {
            logInvalidLevelsCount(searchCriteria);
            return false;
        }

        if (searchCriteriaParts.get(0).equals(MATCH_ANY_WILDCARD)) {
            logInvalidFirstLevel(searchCriteria);
            return false;
        }

        if (searchCriteriaParts.stream()
                .filter(level -> level.equals(MATCH_ANY_WILDCARD)).count() > MAX_WILDCARDS_ALLOWED) {
            logTooManyWildcards(searchCriteria);
            return false;
        }

        long validLevelsCount = searchCriteriaParts.stream().filter(level ->
                level.equals(MATCH_ANY_WILDCARD) || isInteger(level)).count();

        return validLevelsCount == EC_LEVELS_COUNT;
    }

    // Source: http://stackoverflow.com/questions/5439529/determine-if-a-string-is-an-integer-in-java
    private boolean isInteger(String s) {
        return isInteger(s, 10);
    }

    private boolean isInteger(String s, int radix) {
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) return false;
                else continue;
            }
            if (Character.digit(s.charAt(i), radix) < 0) return false;
        }
        return true;
    }

    private void logInvalidLevelsCount(String searchCriteria) {
        log.warning(() -> String.format("'%s' search criteria does NOT have '%d' levels specified!",
                searchCriteria,
                EC_LEVELS_COUNT));
    }

    private void logInvalidFirstLevel(String searchCriteria) {
        log.warning(() -> String.format("First level of '%s' search criteria CAN'T be a WILDCARD!",
                searchCriteria));
    }

    private void logTooManyWildcards(String searchCriteria) {
        log.warning(() -> String.format("Only 2 wildcards allowed per search criteria! '%s' has more!",
                searchCriteria));
    }
}
