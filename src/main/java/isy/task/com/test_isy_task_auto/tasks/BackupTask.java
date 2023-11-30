package isy.task.com.test_isy_task_auto.tasks;

import de.bund.bva.isyfact.datetime.util.DateTimeUtil;
import de.bund.bva.isyfact.logging.*;
import de.bund.bva.isyfact.task.model.AbstractTask;
import isy.task.com.test_isy_task_auto.util.ThreadHandOverData;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
//@Gesichert("Recht1")
public class BackupTask extends AbstractTask {

    private final IsyLogger LOG = IsyLoggerFactory.getLogger(BackupTask.class);

    boolean[] taskDone = {false};
    String targetFile = "output//backup//logdata";
    final String schluessel_backup_task="backup_task_profiling_schluessel";
    final int RANDOMRANGE= 400;
    @Autowired
    private ThreadHandOverData threadHandOverData;

    Random randObject;
    public BackupTask() {
        super();


    }

    @Override
    public void execute() {
        setLogInterface("BackupTask Task started");
        randObject = new Random();
        int randomNumber = randObject.nextInt(RANDOMRANGE);
        Path source = Paths.get("logdata.log");
        Path target = Paths.get(targetFile+ "_" + threadHandOverData.getTargetFileNameHint()+ "_"+randomNumber+".log");
        try {
            Files.copy(source, target);
        } catch (IOException e1) {
            e1.printStackTrace();
            String message = e1.getMessage();
            LOG.error(schluessel_backup_task, "{}  Backup file execution failed: " + message,
                    DateTimeUtil.localDateTimeNow());
        }
        setLogInterface("BackupTask Task done");


        taskDone[0] = true;
    }
    private void setLogInterface(String message){
        String log_message = "{} "+ message;
        switch(threadHandOverData.getLogFramework()){

            case "F":
                LOG.infoFachdaten(LogKategorie.PROFILING, schluessel_backup_task,log_message ,
                        DateTimeUtil.localDateTimeNow());
                break;
            default:
                LOG.info(LogKategorie.PROFILING, schluessel_backup_task, log_message,
                        DateTimeUtil.localDateTimeNow());
                break;

        }
    }

}

