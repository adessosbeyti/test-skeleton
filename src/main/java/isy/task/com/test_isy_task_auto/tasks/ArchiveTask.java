package isy.task.com.test_isy_task_auto.tasks;

import de.bund.bva.isyfact.datetime.util.DateTimeUtil;
import de.bund.bva.isyfact.logging.*;
import de.bund.bva.isyfact.task.model.AbstractTask;
import isy.task.com.test_isy_task_auto.util.ThreadHandOverData;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
//@Gesichert("Recht2")
public class ArchiveTask extends AbstractTask {

    @Autowired
    private ThreadHandOverData fileNameHintsProvider;
    private final IsyLoggerStandard LOG = IsyLoggerFactory.getLogger(ArchiveTask.class);
    boolean[] taskDone = {false};
    final int RANDOMRANGE= 400;
    final String schluessel_archive_task="archive_task_profiling_schluessel";
    String targetFile = "output//archive//logdata";
    Random randObject;
    static int count_construt=1;

    public ArchiveTask() {
        super();
        LOG.info(LogKategorie.PROFILING, schluessel_archive_task, "{}  ArchiveTask Task wird vorbereitet: {}",
                DateTimeUtil.localDateTimeNow(),count_construt);
        count_construt++;

    }

    @Override
    public void execute() {

        String sourceFile = "logdata.log";
        randObject = new Random();
        int randomNumber = randObject.nextInt(RANDOMRANGE);
        Path source = Paths.get("logdata.log");

        FileOutputStream fos = null;

        try {
            LOG.info(LogKategorie.PROFILING, schluessel_archive_task, "{}  ArchiveTask Task Started",
                    DateTimeUtil.localDateTimeNow());
            fos = new FileOutputStream(targetFile+"_"+ fileNameHintsProvider.getTargetFileNameHint()+ "_"+randomNumber+".zip");
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            File fileToZip = new File(sourceFile);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            zipOut.close();
            fis.close();
            fos.close();
        } catch (IOException ex) {
            String message = ex.getMessage();
            LOG.error( "BackupTask Task", "{}  ArchiveTask failed to execute: " + message,
                    DateTimeUtil.localDateTimeNow());
            throw new RuntimeException(ex);
        }
       LOG.info(LogKategorie.PROFILING, schluessel_archive_task, "{}  ArchiveTask Task done",
                    DateTimeUtil.localDateTimeNow());

        taskDone[0] = true;
    }

}