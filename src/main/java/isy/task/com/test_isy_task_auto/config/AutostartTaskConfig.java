package isy.task.com.test_isy_task_auto.config;

import de.bund.bva.isyfact.aufrufkontext.AufrufKontextFactory;
import de.bund.bva.isyfact.aufrufkontext.AufrufKontextVerwalter;
import de.bund.bva.isyfact.aufrufkontext.impl.AufrufKontextVerwalterImpl;
import de.bund.bva.isyfact.sicherheit.Sicherheit;
import de.bund.bva.isyfact.sicherheit.accessmgr.AccessManager;
import de.bund.bva.isyfact.sicherheit.config.IsySicherheitConfigurationProperties;
import de.bund.bva.isyfact.sicherheit.impl.SicherheitImpl;
import isy.task.com.test_isy_task_auto.impl.AccessManagerImpl;
import isy.task.com.test_isy_task_auto.tasks.ArchiveTask;
import isy.task.com.test_isy_task_auto.tasks.BackupTask;
import isy.task.com.test_isy_task_auto.util.ThreadHandOverData;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
@EnableConfigurationProperties
public class AutostartTaskConfig {

    @Bean
    public ThreadHandOverData fileNameGenerator(){return new ThreadHandOverData();
    }


    @Bean
    public BackupTask backupTask() {
        return new BackupTask();
    }
    @Bean
    public ArchiveTask archiveTask() {
        return new ArchiveTask();
    }
    @Bean
    public AccessManager accessManager() {
        return new AccessManagerImpl();
    }

    @Bean
    public Sicherheit sicherheit(AufrufKontextVerwalter aufrufKontextVerwalter,
                                 AufrufKontextFactory aufrufKontextFactory, AccessManager accessManager,
                                 IsySicherheitConfigurationProperties properties) {
        return new SicherheitImpl("/sicherheit/rollenrechte.xml", aufrufKontextVerwalter,
                aufrufKontextFactory, accessManager, properties);
    }

    @Bean
    @Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public AufrufKontextVerwalter aufrufKontextVerwalter() {
        return new AufrufKontextVerwalterImpl();
    }

}
