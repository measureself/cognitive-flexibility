package rage.ts;

import java.net.URISyntaxException;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Profile("production")
public class TSProfileProduction {

    @Value("${db.hostname}")
    private String databaseHost;
    @Value("${db.port}")
    private String databasePort;
    @Value("${db.path}")
    private String databasePath;

    @Value("${db.username}")
    private String databaseUsername;
    @Value("${db.password}")
    private String databasePassword;

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaDialect(new HibernateJpaDialect());

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("rage.ts.domain");
        factory.setDataSource(dataSource());

        factory.afterPropertiesSet();
        return factory;
    }

    @Bean
    public DataSource dataSource() {
        String dbUrl = "jdbc:mysql://" + databaseHost + ':' + databasePort + databasePath;

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        basicDataSource.setUrl(dbUrl);
        basicDataSource.setUsername(databaseUsername);
        basicDataSource.setPassword(databasePassword);

        return basicDataSource;
    }
}
