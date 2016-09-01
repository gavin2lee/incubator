package com.lachesis.mnisqm.test.module.db;

import java.io.File;
import java.io.FileOutputStream;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatDtdDataSet;

import com.lachesis.mnisqm.test.service.DataSourceFactoryBean;

/**
 *
 * @author Paul Xu.
 * @since 1.0.0
 *
 */
public class DbUnitUtil {

    public static void main(String[] args) throws Exception {
        DataSource dataSource = new DataSourceFactoryBean().getDataSource();
        File file = new File("src/main/resources/mnisqm.dtd");
        IDatabaseConnection connection = new DatabaseDataSourceConnection(dataSource);
        connection.getConfig().setProperty(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, true);
        // write DTD file
        FlatDtdDataSet.write(connection.createDataSet(), new FileOutputStream(file));
    }
}
