package com.francois.algo.pdb.data;

import com.francois.algo.pdb.core.domain.InvalidPdbChainMapException;

import java.util.List;

public interface IDataParser<TModel> {
    List<TModel> parseToModel(List<String> dataToParse) throws InvalidPdbChainMapException;
}