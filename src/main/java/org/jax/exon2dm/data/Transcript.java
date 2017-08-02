package org.jax.exon2dm.data;

import java.util.ArrayList;
import java.util.List;

public class Transcript {

    private boolean isCanonical=false;
    private String biotype=null;
    private Translation translation=null;
    /** Usually a chromosome, e.g., "17" */
    private String sequenceRegion=null;
    private String assemblyName=null;
    private String displayname=null;
    private List<Exon> exons;
    int start;
    int end;
    String ensemblTranscriptID=null;
    String species=null;


    public Transcript() {
        exons=new ArrayList<>();
    }


    public void setCanonical(){this.isCanonical=true;}
    public void setBiotype(String bt){this.biotype=bt;}
    public void setTranslation(Translation t) { this.translation=t;}
    public void setSequenceRegion(String sr){this.sequenceRegion=sr;}
    public void setAssemblyName(String an){assemblyName=an;}
    public void setDisplayName(String dn){displayname=dn;}
    public void addExon(Exon e) { exons.add(e);}
    public void setStart(int s){start=s;}
    public void setEnd(int e){end=e;}
    public void setEnsemblTranscriptID(String id){ensemblTranscriptID=id;}
    public void setSpecies(String s){species=s;}






}
