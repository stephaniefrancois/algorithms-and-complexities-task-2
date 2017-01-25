package com.francois.algo.pdb.data;

import com.francois.algo.pdb.core.domain.AppException;

public final class DataFileNotFoundException extends AppException {
    public DataFileNotFoundException(String filePath) {
        super("'" + filePath + "' is not a valid file path!");
    }
}
