pg_manager
==========

PostgreSQL Enterprise Manager, for 9.X and above

Language: Groovy, Java and HMTL 5

Screenshots
===========

![alt tag](https://raw.github.com/fernandopeinado/pg_manager/master/screenshot1.png)

Installation Procedure
======================

Application Environment
-----------------------

* JDK 7
* Tomcat 7

server.xml: add Datasource Resource and configure 

<GlobalNamingResources>
  <Resource name="jdbc/pgman" auth="Container"
          factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
          type="javax.sql.DataSource"
          driverClassName="org.postgresql.Driver"
          url="jdbc:postgresql:postgres"
          defaultAutoCommit="false"
          username="postgres"
          password=""
          maxActive="10"
          testOnBorrow="true"
          validationQuery="SELECT 1"
          validationQueryTimeout="60"
          fairQueue="true"
          />

</GlobalNamingResources>

<Context docBase="/opt/deploy/pgman.war" path="/pgman" reloadable="false"/>


context.xml: add datasource link

<ResourceLink name="jdbc/pgman" global="jdbc/pgman" type="javax.sql.DataSource" />



PostgreSQL Environment
----------------------

* PostgreSQL Server should be running on the same machine of this application.
* pg_hba.conf: Access must be in "trust" mode for all users from the same host (127.0.0.1)
* postgres.conf: load the contrib extension pg_stat_staments

