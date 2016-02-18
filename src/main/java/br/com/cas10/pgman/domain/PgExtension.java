package br.com.cas10.pgman.domain;

/**
 * @author kasten
 * @since 18/02/16
 */
public class PgExtension {
    private String name;
    private String defaultVersion;
    private String installedVersion;
    private String comment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultVersion() {
        return defaultVersion;
    }

    public void setDefaultVersion(String defaultVersion) {
        this.defaultVersion = defaultVersion;
    }

    public String getInstalledVersion() {
        return installedVersion;
    }

    public void setInstalledVersion(String installedVersion) {
        this.installedVersion = installedVersion;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isInstalled() {
        return installedVersion != null;
    }
}
