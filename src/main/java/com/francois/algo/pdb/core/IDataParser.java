package com.francois.algo.pdb.core;

import java.util.List;

public interface IDataParser<TModel> {
    <TModel> List<TModel> parseToModel(List<String> dataToParse);
}
