package org.jax.exon2dm;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


import org.jax.exon2dm.ensembl.EnsemblGene;
import org.jax.exon2dm.ensembl.JEnsembl;
import org.jax.exon2dm.ensembl.RestClient;



import org.apache.log4j.Logger;

public class Exon2DiffModule {
    static Logger logger = Logger.getLogger(Exon2DiffModule.class.getName());


    public static void main(String args[]) {
        System.out.println("Exon2DiffModule");
        Exon2DiffModule e2m=new Exon2DiffModule();
        try {
            e2m.testSchema(args);
            //e2m.testJEnsembl();
        } catch (Exception e) {
            logger.error(e,e);
        }
    }



    public Exon2DiffModule(){}


    public void testJEnsembl() throws Exception {
        JEnsembl jEnsembl=new JEnsembl();
        jEnsembl.downloadGenes();
    }



    public void testSchema(String args[]) throws Exception {
        EnsemblGene gene = new EnsemblGene("ENSMUSG00000024241");

    }


}
