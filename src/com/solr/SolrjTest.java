package com.solr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.beans.SongTag;
import com.downloader.GenericHTTPClient;
import com.google.gson.Gson;


public class SolrjTest
{
    public StringBuffer query(String q)
    {
        CommonsHttpSolrServer server = null;
        StringBuffer sb = new StringBuffer();
        try
        {
            server = new CommonsHttpSolrServer("http://projectbajao.homeunix.com/solr/");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        SolrQuery query = new SolrQuery();
        query.setQuery(q);
        try
        {
        	
            QueryResponse qr = server.query(query);
            SolrDocumentList sdl = qr.getResults();
            List beans = qr.getBeans(SongTag.class);
             
            Gson gson = new Gson();
            sb.append("{\"responseHeader\":{\"params\":{\"q\":\""+q+"\"}},");
         
            sb.append("\"response\":{\"numFound\":"+sdl.getNumFound() +",\"start\":"+ sdl.getStart() +",\"docs\":");
            sb.append(gson.toJson(beans));
            sb.append("}}");
        	 
            }
        catch (SolrServerException e)
        {
            e.printStackTrace();
        }
        return sb;
    }
    
    

    public static void main(String[] args)
    {
        SolrjTest solrj = new SolrjTest();
        System.out.println(solrj.query("look"));
//        String response = HTTPClient.getResponse("http://projectbajao.homeunix.com/solr/select?wt=json&indent=on&q=dil");
//        System.out.println(response);
    }
}
