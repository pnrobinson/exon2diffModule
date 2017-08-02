package org.jax.exon2dm;


import org.jax.exon2dm.io.EnsemblJSONParser;


import org.apache.log4j.Logger;

public class Exon2DiffModule {
    static Logger logger = Logger.getLogger(Exon2DiffModule.class.getName());


    public static void main(String args[]) {
        System.out.println("Exon2DiffModule");
        Exon2DiffModule e2m=new Exon2DiffModule();
        e2m.testSchema();

    }



    public Exon2DiffModule(){}





    public void testSchema()  {
        EnsemblJSONParser gene = new EnsemblJSONParser("ENSMUSG00000024241");
    }


}
