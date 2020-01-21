package br.com.cas10.pgman;


import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SqlResourceLoader {

    private static SqlResourceLoader instance = null;

    public static SqlResourceLoader getInstance() {
        if (instance == null) {
            instance = new SqlResourceLoader();
        }
        return instance;
    }

    public String loadQuery(String queryName) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try (InputStream in = cl.getResourceAsStream("br/com/cas10/pgman/pg10/"+ queryName +".sql")) {
            return IOUtils.toString(in);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
