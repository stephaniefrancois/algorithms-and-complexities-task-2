package com.francois.algo.pdb.core.search;

import com.francois.algo.pdb.common.StringExtensions;
import com.francois.algo.pdb.core.domain.AppException;

public final class InvalidSearchArgumentException extends AppException {
    public InvalidSearchArgumentException(String searchCriteria) {
        super(buildMessage(searchCriteria));
    }

    private static String buildMessage(String searchCriteria) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("'%s' is NOT a valid search criteria!", searchCriteria));
        sb.append(StringExtensions.NewLineSeparator);
        sb.append("  Accepted EC NUMBER formats are as follows:");
        sb.append(StringExtensions.NewLineSeparator);
        sb.append("  1.1.1.1 = Full Match");
        sb.append(StringExtensions.NewLineSeparator);
        sb.append("  1.1.1.- = Partial Match");
        sb.append(StringExtensions.NewLineSeparator);
        sb.append("  1.1.-.1 = Partial Match");
        sb.append(StringExtensions.NewLineSeparator);
        sb.append("  1.-.1.1 = Partial Match");
        sb.append(StringExtensions.NewLineSeparator);
        sb.append("  1.1.-.- = Partial Match");
        sb.append(StringExtensions.NewLineSeparator);
        sb.append("  1.-.-.1 = Partial Match");
        sb.append(StringExtensions.NewLineSeparator);
        sb.append("  1.-.1.- = Partial Match");
        sb.append(StringExtensions.NewLineSeparator);
        sb.append(" Where '-' matches any number from 0 to 9 and first LEVEL must be supplied at all times.");
        sb.append(StringExtensions.NewLineSeparator);
        sb.append(" No more than 2 wildcards can be used! And 1 can be any NUMBER from 0 to 99 or more");
        return sb.toString();
    }
}
