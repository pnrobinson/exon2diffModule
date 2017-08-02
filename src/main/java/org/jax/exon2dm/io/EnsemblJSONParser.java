package org.jax.exon2dm.io;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import net.minidev.json.*;
import net.minidev.json.parser.*;
import org.apache.log4j.Logger;
import org.jax.exon2dm.data.Exon;
import org.jax.exon2dm.data.Gene;
import org.jax.exon2dm.data.Transcript;
import org.jax.exon2dm.data.Translation;

public class EnsemblJSONParser {
    static Logger logger = Logger.getLogger(EnsemblJSONParser.class.getName());
    public static final String SERVER = "https://rest.ensembl.org";
    public static final JSONParser PARSER = new JSONParser(JSONParser.MODE_JSON_SIMPLE);

    public static int requestCount = 0;
    public static long lastRequestTime = System.currentTimeMillis();


    public String endpoint=null;


    /** Make and endpoint like /lookup/id/ENSMUSG00000024241?content-type=application/json;expand=1";*/
    public EnsemblJSONParser(String ensemblid){
        this.endpoint=String.format("/lookup/id/%s?content-type=application/json;expand=1\";",ensemblid);
        logger.trace("Looking up Ensembl Gene " +ensemblid);
        logger.trace(endpoint);
        Gene gene = new Gene(ensemblid);
        try {
            JSONObject jarray = getGeneInfo();
            String dbtype= jarray.getAsString("db_type");
            String biotype= jarray.getAsString("biotype");
            gene.setBiotype(biotype);
            JSONArray transcriptarray = (JSONArray) jarray.get("Transcript");
            for (Object obj:transcriptarray) {
                Transcript myTranscript=new Transcript();
                JSONObject transcript =(JSONObject)obj;
                biotype= jarray.getAsString("biotype");
                myTranscript.setBiotype(biotype);
                JSONObject translation = (JSONObject) transcript.get("Translation");
                if (translation!=null) {
                    int length=translation.getAsNumber("length").intValue();
                    int start=translation.getAsNumber("start").intValue();
                    int end=translation.getAsNumber("end").intValue();
                    String id=translation.getAsString("id");
                    Translation tr = new Translation(length,start,end,id);
                    myTranscript.setTranslation(tr);
                }
                int iscanonical=transcript.getAsNumber("is_canonical").intValue();
                if (iscanonical==1) myTranscript.setCanonical();
                String seqregion=transcript.getAsString("seq_region_name");
                myTranscript.setSequenceRegion(seqregion);
                String assemblyName=transcript.getAsString("assembly_name");
                myTranscript.setAssemblyName(assemblyName);
                String displayname=transcript.getAsString("display_name");
                myTranscript.setDisplayName(displayname);
                int start=transcript.getAsNumber("start").intValue();
                int end=transcript.getAsNumber("end").intValue();
                String id=transcript.getAsString("id");
                String species=transcript.getAsString("species");
                myTranscript.setStart(start);
                myTranscript.setEnd(end);
                myTranscript.setEnsemblTranscriptID(id);
                myTranscript.setSpecies(species);
                JSONArray exons = (JSONArray) transcript.get("Exon");
                for (Object exonobject: exons) {
                    JSONObject exonjsonobject=(JSONObject)exonobject;
                    System.err.println("EXON"+exonjsonobject.toString());
                    //"start":80479837,"seq_region_name":"17","end":80480453,"id":"ENSMUSE00000358919",
                    start=exonjsonobject.getAsNumber("start").intValue();
                    end=exonjsonobject.getAsNumber("end").intValue();
                    id=exonjsonobject.getAsString("id");
                    Exon exon=new Exon(start,end,id);
                    myTranscript.addExon(exon);
                }
               // logger.trace(translation.toString());


            }
            logger.trace(jarray.toString());
            logger.trace(String.format("dbtype=%s",dbtype));
            logger.trace(String.format("biotype=%s",biotype));
            logger.trace(transcriptarray.toString());


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public JSONObject getGeneInfo() throws ParseException, MalformedURLException, IOException, InterruptedException {
        return  (JSONObject) getJSON(this.endpoint);
    }

    public  Object getJSON(String endpoint) throws ParseException, MalformedURLException, IOException, InterruptedException {
        String jsonString = getContent(endpoint);
        return PARSER.parse(jsonString);
    }


    public  String getContent(String endpoint) throws MalformedURLException, IOException, InterruptedException {

        if(requestCount == 15) { // check every 15
            long currentTime = System.currentTimeMillis();
            long diff = currentTime - lastRequestTime;
            //if less than a second then sleep for the remainder of the second
            if(diff < 1000) {
                Thread.sleep(1000 - diff);
            }
            //reset
            lastRequestTime = System.currentTimeMillis();
            requestCount = 0;
        }

        URL url = new URL(SERVER+endpoint);
        URLConnection connection = url.openConnection();
        HttpURLConnection httpConnection = (HttpURLConnection)connection;
        httpConnection.setRequestProperty("Content-Type", "application/json");
        requestCount++;

        InputStream response = httpConnection.getInputStream();
        int responseCode = httpConnection.getResponseCode();

        if(responseCode != 200) {
            if(responseCode == 429 && httpConnection.getHeaderField("Retry-After") != null) {
                double sleepFloatingPoint = Double.valueOf(httpConnection.getHeaderField("Retry-After"));
                double sleepMillis = 1000 * sleepFloatingPoint;
                Thread.sleep((long)sleepMillis);
                return getContent(endpoint);
            }
            throw new RuntimeException("Response code was not 200. Detected response was "+responseCode);
        }

        String output;
        Reader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(response, "UTF-8"));
            StringBuilder builder = new StringBuilder();
            char[] buffer = new char[8192];
            int read;
            while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
                builder.append(buffer, 0, read);
            }
            output = builder.toString();
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException logOrIgnore) {
                    logOrIgnore.printStackTrace();
                }
            }
        }

        return output;
    }





}
