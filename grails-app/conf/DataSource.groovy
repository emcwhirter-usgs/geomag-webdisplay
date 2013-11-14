dataSource {
    pooled = true
    //driverClassName = "org.hsqldb.jdbcDriver"
    driverClassName = "com.mysql.jdbc.Driver"
    username = "sa"
    password = "xxxxxxxx"
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
    development {
        dataSource {
            //dbCreate = "create-drop" // one of 'create', 'create-drop','update'
            //url = "jdbc:hsqldb:mem:devDB"
            //url = "jdbc:hsqldb:file:devDb;shutdown=true"
            //logSql=true
            dbCreate = "update" // one of 'create', 'create-drop','update'
            username = "webdisplay"
            password = "xxxxxxxx"
            url = "jdbc:mysql://localhost:3306/webdisplay"
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            username = "geomag"
            password = 'xxxxxxxx'
            url = "jdbc:mysql://dev-mysql.cr.usgs.gov:3306/geomag"
        }
    }
    production {
        dataSource {
            dbCreate = "update"
            username = "webdisplay"
            password = "xxxxxxxx"
            url = "jdbc:mysql://localhost:3306/webdisplay"
        }
    }
}
