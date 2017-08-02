package org.jax.exon2dm.data;

public class Gene {
    /** e.g., ENSMUSG00000024241.*/
    private String id=null;
    private String biotype=null;

    public Gene(String ensemblgeneid) {
        this.id=ensemblgeneid;
    }

    public void setBiotype(String bt) {this.biotype=bt;}
}
