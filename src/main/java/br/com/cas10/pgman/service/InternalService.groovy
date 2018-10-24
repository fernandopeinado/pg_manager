package br.com.cas10.pgman.service

import liquibase.Liquibase
import liquibase.database.DatabaseConnection
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

import javax.sql.DataSource
import java.sql.Connection

/**
 * @author kasten
 * @since 19/09/17
 */
@Service
@Transactional(transactionManager = "internalTransactionManager")
class InternalService {

    private DataSource ds
    private NamedParameterJdbcTemplate jdbc

    @Autowired
    void setDataSource(DataSource dataSource) {
        this.ds = dataSource
        this.jdbc = new NamedParameterJdbcTemplate(dataSource)
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void initDatabase() {
        Connection con = ds.connection
        try {
            DatabaseConnection connection = new JdbcConnection(con)
            Liquibase liquibase = new Liquibase("br/com/cas10/pgman/changelog/db.changelog-master.xml", new ClassLoaderResourceAccessor(), connection)
            liquibase.update("")
        } finally {
            con.close()
        }
    }
}
