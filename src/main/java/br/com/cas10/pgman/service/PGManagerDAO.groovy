package br.com.cas10.pgman.service

import br.com.cas10.pgman.domain.Database
import br.com.cas10.pgman.domain.HistSize
import br.com.cas10.pgman.domain.TopQueriesSnapshot
import br.com.cas10.pgman.domain.TopQuery
import liquibase.structure.core.Table
import org.mapdb.BTreeMap
import org.mapdb.DB
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.sql.DataSource
import java.util.Map.Entry

@Service
@Transactional
public class PGManagerDAO {

    private NamedParameterJdbcTemplate jdbc;
	
    @Autowired
    private DatabaseService databaseService;
	
    @Autowired
    private PostgresqlService postgresqlService;

    @Autowired
    private DB mapDB;

    @Autowired
    void setDataSource(DataSource dataSource) {
        jdbc = new NamedParameterJdbcTemplate(dataSource);
    }
        
    void updateDbSizes() {
        Long now = System.currentTimeMillis();
        Map<String, Object> params = new HashMap<String, Object>();
        List<Map<String, Object>> result = jdbc.queryForList("SELECT datname, pg_database_size(datname) as dbsize FROM pg_database", params);
        BTreeMap<String, Database> databases = mapDB.getTreeMap("databases");
        result.each { Map<String, Object> row ->
            Database db = databases.get(row.datname);
            if (db == null) {
                db = new Database();
                db.name = row.datname;
            }
            db.size = row.dbsize;
            HistSize hist = new HistSize(size: row.dbsize);
            db.hist.put(hist.date, hist);
            databases.put(db.name, db);
        }
        mapDB.commit();
    }
	
    void cleanDbSizes(int daysToKeep) {
        GregorianCalendar breakPoint = new GregorianCalendar()
        breakPoint.add(Calendar.DAY_OF_YEAR, -daysToKeep)
        BTreeMap<String, Database> databases = mapDB.getTreeMap("databases")
        new ArrayList<Database>(databases.values()).each { Database db -> 
            db.hist = db.hist.tailMap(breakPoint.time); 
            databases.put(db.name, db);
        }
        mapDB.commit();
    }
	
    Map<String, List<Long>> selectDbSizes(int days) {
        GregorianCalendar breakPoint = new GregorianCalendar()
        breakPoint.add(Calendar.DAY_OF_MONTH, -days)
        Map<String, List<Long>> dbSizes = new HashMap<String>();
        BTreeMap<String, Database> databases = mapDB.getTreeMap("databases")
        databases.values().each { Database db -> 
            List<Long> sizes = new ArrayList<Long>();
            db.hist.tailMap(breakPoint.time).values().each { HistSize h -> sizes.add(h.size) }
            dbSizes.put(db.name, sizes);
        }
        return dbSizes
    }
	
    void updateRelationSizes() {
        Date now =  new Date()
        BTreeMap<String, Database> databases = mapDB.getTreeMap("databases")
        for (String database : databaseService.databases) {
            Database db = databases.get(database);
            if (db == null) {
                db.name = database;
            }
            List<Map<String, Object>> result = postgresqlService.getTopRelationSizes(-1, database, 1048576L)
            result.each { Map<String, Object> row ->
                Table table = (Table) db.tables[row.relation];
                if (table == null) {
                    table = new Table();
                    table.name = row.relation;
                    table.sizeData = row.sizebytes;
                    table.sizeIndex = row.indexsizebytes;
                    db.tables[table.name] = table;
                }
                HistSize dataHist = new HistSize(size: row.sizebytes);
                HistSize indexHist = new HistSize(size: row.indexsizebytes);
                table.histData.put(hist.date, dataHist);
                table.histIndex.put(hist.date, indexHist);
            }
            databases.put(db.name, db);
        }
        mapDB.commit();
    }
	
    void cleanRelationSizes(int daysToKeep) {
        GregorianCalendar breakPoint = new GregorianCalendar()
        breakPoint.add(Calendar.DAY_OF_YEAR, -daysToKeep)
        BTreeMap<String, Database> databases = mapDB.getTreeMap("databases")
        for (String database : databaseService.databases) {
            Database db = databases.get(database);
            if (db != null) {
                db.tables.values().each { Table tb -> 
                    tb.histData = tb.histData.tailMap(breakPoint.time); 
                    tb.histIndex = tb.histIndex.tailMap(breakPoint.time); 
                }
            }
            databases.put(db.name, db);
        }
        mapDB.commit();
    }
    
    public void snapshotQueries() {
        BTreeMap<Date, TopQueriesSnapshot> topQueriesSnaps = mapDB.getTreeMap("topqueries");
        Date now = new Date();
        Date lastDate = now;
        if (!topQueriesSnaps.isEmpty()) {
            lastDate = topQueriesSnaps.lastKey();
        }
        TopQueriesSnapshot snap = new TopQueriesSnapshot();
        snap.start = lastDate;
        snap.end = now;
        snap.totalTime = 0;
        snap.readTime = 0;
        snap.writeTime = 0;
        List<Map<String, Object>> topQueries = postgresqlService.getTopStatements(20);
        postgresqlService.resetStatementsStats();
        topQueries.each { Map<String, Object> row ->
            TopQuery top = new TopQuery();
            top.database = row.DATABASE;
            top.query = row.QUERY;
            top.calls = row.CALLS;
            top.totalTime = row.TOTAL_TIME;
            snap.totalTime += row.TOTAL_TIME;
            top.avgTime = row.AVG_TIME;
            top.rows = row.ROWS;
            top.blkReadTime = row.BLK_READ_TIME;
            snap.readTime += row.BLK_READ_TIME;
            top.blkWriteTime = row.BLK_WRITE_TIME;
            snap.writeTime += row.BLK_WRITE_TIME;
            snap.queries.add(top);
        }
        topQueriesSnaps.put(now, snap);
        mapDB.commit();
    }
    
    public List<TopQueriesSnapshot> getSnapshotQueries(int last) {
        BTreeMap<Date, TopQueriesSnapshot> topQueriesSnaps = mapDB.getTreeMap("topqueries");
        NavigableMap<Date, TopQueriesSnapshot> lasts = topQueriesSnaps.descendingMap();
        LinkedList<TopQueriesSnapshot> tops = new LinkedList<>();
        int count = 0;
        for (Iterator<Entry<Date, TopQueriesSnapshot>> it = lasts.iterator(); it.hasNext() && count < last; count++) {
            Entry<Date, TopQueriesSnapshot> entry = it.next();
            tops.addFirst(entry.value);
        }
        return tops;
    }

    public List<TopQueriesSnapshot> getSnapshotQueries(Date init, Date end) {
        BTreeMap<Date, TopQueriesSnapshot> topQueriesSnaps = mapDB.getTreeMap("topqueries");
        NavigableMap<Date, TopQueriesSnapshot> lasts = topQueriesSnaps.subMap(init, end);
        LinkedList<TopQueriesSnapshot> tops = new LinkedList<>();
        for (Iterator<Entry<Date, TopQueriesSnapshot>> it = lasts.iterator(); it.hasNext();) {
            Entry<Date, TopQueriesSnapshot> entry = it.next();
            tops.add(entry.value);
        }
        return tops;
    }
    
    public List<TopQuery> getConslidatedQueries(Date init, Date end) {
        BTreeMap<Date, TopQueriesSnapshot> topQueriesSnaps = mapDB.getTreeMap("topqueries");
        NavigableMap<Date, TopQueriesSnapshot> lasts = topQueriesSnaps.subMap(init, end);
        List<TopQuery> tops = new ArrayList<TopQuery>();
        Map<String, TopQuery> sqls = new HashMap<String, TopQuery>();
        for (Iterator<Entry<Date, TopQueriesSnapshot>> it = lasts.iterator(); it.hasNext(); ) {
            Entry<Date, TopQueriesSnapshot> entry = it.next();
            TopQueriesSnapshot snap = entry.value;
            for (TopQuery query : snap.getQueries()) {
                String key = query.database + ':' + query.query;
                TopQuery existing = sqls[key];
                if (!existing) {
                    TopQuery tq = query.clone();
                    sqls[key] = tq;
                    tq.avgTime = 0;
                    tops.add(tq);
                }
                else {
                    existing.totalTime += query.totalTime;
                    existing.calls += query.calls;
                    existing.rows += query.rows;
                    existing.blkReadTime += query.blkReadTime;
                    existing.blkWriteTime += query.blkWriteTime;
                }
            }
        }
        Collections.sort(tops);
        tops.each { TopQuery top ->
            top.avgTime = Math.round((double) top.totalTime / (double) top.calls);
                }
                return tops;
                }
    
                public void compactDB() {
                mapDB.compact();
                }
                }
