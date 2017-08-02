package org.jax.exon2dm.data;

import org.apache.log4j.Logger;

public class Translation {
    static Logger logger = Logger.getLogger(Translation.class.getName());
    int length;
    int chromosomalStart;
    int chromosomalEnd;
    String ensemblProteinID=null;

    public Translation(int length,int start,int end,String id){
        length=length;
        chromosomalStart=start;
        chromosomalEnd=end;
        ensemblProteinID=id;
        logger.trace(String.format("%s [length=%d, start=%d, end=%d]",ensemblProteinID,length,start,end));
    }
}
