package com.francois.algo.pdb.core;

import com.francois.algo.pdb.data.DataFileNotFoundException;

import java.util.List;

public interface IRepository<TEntity> {
    List<TEntity> getAll() throws DataFileNotFoundException;
}
