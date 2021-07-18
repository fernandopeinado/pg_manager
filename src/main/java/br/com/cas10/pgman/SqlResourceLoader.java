package br.com.cas10.pgman;


import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

public class SqlResourceLoader {

    private static SqlResourceLoader instance = null;

    public static SqlResourceLoader getInstance() {
        if (instance == null) {
            instance = new SqlResourceLoader();
        }
        return instance;
    }

    private Integer databaseMajorVersion;

    public boolean hasMetaData() {
        return databaseMajorVersion != null;
    }

    public void collectMetaData(Connection con) throws SQLException {
        this.databaseMajorVersion = con.getMetaData().getDatabaseMajorVersion();
    }

    public String loadQuery(String queryName) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        String baseDir = "pg" + databaseMajorVersion;
        if (databaseMajorVersion > 13) {
            baseDir = "pg" + databaseMajorVersion;
        }
        try (InputStream in = cl.getResourceAsStream("br/com/cas10/pgman/"+ baseDir +"/"+ queryName +".sql")) {
            return IOUtils.toString(in);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
