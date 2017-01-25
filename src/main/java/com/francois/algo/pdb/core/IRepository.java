package com.francois.algo.pdb.core;

import com.francois.algo.pdb.core.domain.AppException;
import com.francois.algo.pdb.core.domain.InvalidPdbChainMapException;
import com.francois.algo.pdb.data.DataFileNotFoundException;

import java.util.List;

public interface IRepository<TEntity> {
    List<TEntity> getAll() throws AppException;
}
