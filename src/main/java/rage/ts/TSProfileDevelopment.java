package rage.ts;

import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Profile(value = {"default"})
public class TSProfileDevelopment {

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
        factory.setPersistenceUnitName("development");
        factory.setPackagesToScan("rage.ts.domain");
        factory.setDataSource(dataSource());

        factory.afterPropertiesSet();
        return factory;
    }

    @Bean
    public DataSource dataSource() {
        // assumes that you have a database called "mydatabase" in your mysql
        String dbUrl = "jdbc:mysql://localhost:3306/mydatabase?zeroDateTimeBehavior=convertToNull";

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        basicDataSource.setUrl(dbUrl);
        basicDataSource.setUsername("root");
        basicDataSource.setPassword("");

        return basicDataSource;
    }
}
