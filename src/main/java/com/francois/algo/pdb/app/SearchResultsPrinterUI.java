package com.francois.algo.pdb.app;

import com.francois.algo.pdb.core.domain.PdbChainDescriptor;

import java.io.PrintStream;
import java.util.List;
import java.util.Objects;

public final class SearchResultsPrinterUI {
    private final PrintStream out;

    public SearchResultsPrinterUI(PrintStream out) {
        Objects.requireNonNull(out);
        this.out = out;
    }

    public void print(List<PdbChainDescriptor> descriptors) {
        if (descriptors.isEmpty()) {
            this.out.println("No PROTEIN DATA STRUCTURES found :(");
        } else {
            this.out.println();
            this.out.println();
            this.out.println(String.format("'%d' PROTEIN DATA STRUCTURES have been found:", descriptors.size()));
            this.out.println();
            String format = "%-10s%-10s%-15s%-15s%-15s%-15s\n";
            this.out.format(format,"PDB", "CHAIN", "ACCESSION", "EC NUMBER", "SP PRIMARY", "PFAM ID");
            this.out.format(format,"----------","----------","---------------",
                    "---------------","---------------","---------------");

            for (PdbChainDescriptor descriptor : descriptors) {
                printSequence(descriptor, format);
            }
        }
        this.out.println();
        this.out.println(String.format("   End of search results. '%d' total results have been displayed!",
                descriptors.size()));
        this.out.println();
        this.out.println();
    }

    private void printSequence(PdbChainDescriptor descriptor, String format) {
        this.out.format(format,
                descriptor.getPdb(),
                descriptor.getChain(),
                descriptor.getAccession(),
                descriptor.getEcNumber(),
                descriptor.getSpPrimary(),
                descriptor.getPfamId());
    }
}
