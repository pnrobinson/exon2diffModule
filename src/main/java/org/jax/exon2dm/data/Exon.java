package org.jax.exon2dm.data;

public class Exon {

    private int start;
    private int end;
    private String ensemblExonID=null;

    public Exon(int start, int end, String id){
        start=start;
        end=end;
        ensemblExonID=id;
    }

}
