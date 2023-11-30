package isy.task.com.test_isy_task_auto.controller;

import de.bund.bva.isyfact.datetime.util.DateTimeUtil;
import de.bund.bva.isyfact.logging.IsyLogger;
import de.bund.bva.isyfact.logging.IsyLoggerFactory;
import de.bund.bva.isyfact.logging.LogKategorie;
import de.bund.bva.isyfact.task.TaskScheduler;
import de.bund.bva.isyfact.task.konfiguration.TaskKonfiguration;
import de.bund.bva.isyfact.task.model.AbstractTask;
import de.bund.bva.isyfact.task.model.Task;
import de.bund.bva.isyfact.task.model.TaskRunner;
import de.bund.bva.isyfact.task.model.impl.TaskRunnerImpl;
import de.bund.bva.isyfact.task.sicherheit.impl.NoOpAuthenticator;
import isy.task.com.test_isy_task_auto.tasks.ArchiveTask;
import isy.task.com.test_isy_task_auto.tasks.BackupTask;
import isy.task.com.test_isy_task_auto.util.ThreadHandOverData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api/v2/tasks")
public class TasksSchedulingController {
    private final IsyLogger LOG = IsyLoggerFactory.getLogger(TasksSchedulingController.class);
    private List<TaskRunner> allRunningTasks= new ArrayList<>();
    @Autowired
    private ThreadHandOverData threadHandOverData;
    @Autowired
    private BackupTask backupTask;

    @Autowired
    private ArchiveTask archiveTask;
    @Autowired
    private TaskScheduler taskScheduler;


    private TaskKonfiguration taskKonfiguration;

    @GetMapping("/runagain")
    public String executeBackupTaskOnce() {
        taskScheduler.starteKonfigurierteTasks();
        LOG.info(LogKategorie.JOURNAL, "Hello", "say Hallo to Auto Tasks Again",
                DateTimeUtil.localDateTimeNow());
        return "Task wuerde ausgeführt with plan: Once";
    }

    @GetMapping("/stopprog")
    public String stopProgTasks() {
        int taskCounter= stopAllRunningProgTasks();
        LOG.info(LogKategorie.JOURNAL, "Hello", "Number of tasks sopped:" + taskCounter,
                DateTimeUtil.localDateTimeNow());
        return "Number of tasks sopped:" + taskCounter+" That is all";
    }
    @GetMapping("/stopauto")
    public String stopAutoTasks() {
        int taskCounter= stopAllRunningAutoTasks();
        LOG.info(LogKategorie.JOURNAL, "Hello", "Number of tasks sopped:" + taskCounter,
                DateTimeUtil.localDateTimeNow());
        return "Number of tasks sopped:" + taskCounter+" That is all";
    }
    @GetMapping("/backup/once") //?loga=S
    public String executeBackupTaskOnce(@RequestParam(defaultValue = "S") String logs) {
        TaskKonfiguration.Ausfuehrungsplan planTaken = TaskKonfiguration.Ausfuehrungsplan.ONCE;
        threadHandOverData.setLogFramework(logs);
        scheduleThisTask("backupTask", backupTask, planTaken, "seconds", 0);
        LOG.info(LogKategorie.JOURNAL, "Hello", "{} say Hallo to Backup Task with plan: Once: Logs {}",
                DateTimeUtil.localDateTimeNow(),logs);
        return "backupTask wuerde ausgeführt with plan: Once";
    }

    @GetMapping("/archive/once")
    public String executeArchiveTaskOnce() {
        TaskKonfiguration.Ausfuehrungsplan planTaken = TaskKonfiguration.Ausfuehrungsplan.ONCE;
        scheduleThisTask("archiveTask", archiveTask, planTaken, "seconds", 0);
        LOG.info(LogKategorie.JOURNAL, "Hello", "say Hallo to archiveTask Task with plan: Once",
                DateTimeUtil.localDateTimeNow());
        return "archiveTask wuerde ausgeführt with plan: Once";
    }

    // plan can be: "fixedrate", "fixeddelay" or
    // "once" in this case, rate can be set to be null
    // timeUnit such as seconds, minutes hours ..
    @GetMapping("/backup/{plan}/{timeUnit}")//?rate=10
    public String executeBackupTask(@PathVariable String plan, @PathVariable String timeUnit, @RequestParam(defaultValue = "0") String rate) {
        TaskKonfiguration.Ausfuehrungsplan planTaken = null;
        planTaken = convertPlanToTaskPlan(plan);
        scheduleThisTask("backupTask", backupTask, planTaken, timeUnit, Integer.valueOf(rate));
        LOG.info(LogKategorie.JOURNAL, "Hello", "say Hallo to Backup Task with plan: " + plan + " in: " + timeUnit,
                DateTimeUtil.localDateTimeNow());
        return "backupTask wuerde ausgeführt with plan: " + plan + " in: " + timeUnit;
    }

    // plan can be: "fixedrate", "fixeddelay" or
    // "once" in this case, rate can be set to be null
    @GetMapping("/archive/{plan}/{timeUnit}")//?rate=10
    public String executeArchiveTask(@PathVariable String plan, @PathVariable String timeUnit, @RequestParam(defaultValue = "0") String rate) {
        TaskKonfiguration.Ausfuehrungsplan planTaken = null;
        planTaken = convertPlanToTaskPlan(plan);
        scheduleThisTask("archiveTask", archiveTask, planTaken, timeUnit, Integer.valueOf(rate));
        LOG.info(LogKategorie.JOURNAL, "Hello", "say Hallo to Archive Task with plan: " + plan + " in: " + timeUnit,
                DateTimeUtil.localDateTimeNow());
        return "archiveTask wuerde ausgeführt with plan: " + plan + " in: " + timeUnit;
    }


    private TaskKonfiguration.Ausfuehrungsplan convertPlanToTaskPlan(String plan) {

        TaskKonfiguration.Ausfuehrungsplan planTaken = null;
        int counter = 0;
        if (plan.equals("fixedrate")) {
            planTaken = TaskKonfiguration.Ausfuehrungsplan.FIXED_RATE;
            counter++;
        } else if (plan.equals("fixeddelay")) {
            planTaken = TaskKonfiguration.Ausfuehrungsplan.FIXED_DELAY;
            counter += 2;
        } else
            planTaken = TaskKonfiguration.Ausfuehrungsplan.ONCE;

        return planTaken;
    }

    private void scheduleThisTask(String taskID, AbstractTask taskObject, TaskKonfiguration.Ausfuehrungsplan plan, String timeUnit, Integer rate) {
        taskKonfiguration = new TaskKonfiguration();
        taskKonfiguration.setTaskId(taskID);
        taskKonfiguration.setAuthenticator(new NoOpAuthenticator());
        taskKonfiguration.setHostname("localhost");
        taskKonfiguration.setInitialDelay(Duration.ofSeconds(5));
        setPlan(taskKonfiguration, plan, timeUnit, rate);
        TaskRunner taskRunner = new TaskRunnerImpl(taskObject, taskKonfiguration);
        taskScheduler.addTask(taskRunner);
        allRunningTasks.add(taskRunner);
        taskScheduler.start();
    }
    private int stopAllRunningProgTasks(){
        int taskCounter=0;

        for (Iterator<TaskRunner> iter = allRunningTasks.iterator(); iter.hasNext(); ) {
            TaskRunner taskx = iter.next();
            Task task= taskx.getTask();
            if (!task.isDeaktiviert()) {
                task.deaktivieren();
                taskCounter++;
            }

        }
        return taskCounter;
    }
    private int stopAllRunningAutoTasks(){
        int taskCounter=0;
        List<TaskRunner>  allRunningTasksAUto;
        allRunningTasksAUto=taskScheduler.getLaufendeTasks();
        LOG.info(LogKategorie.JOURNAL, "Here", "trace scheduler {}",taskScheduler.getLaufendeTasks().size(),
                DateTimeUtil.localDateTimeNow());
        for (Iterator<TaskRunner> iter = allRunningTasksAUto.iterator(); iter.hasNext(); ) {
            TaskRunner taskx = iter.next();
            Task task= taskx.getTask();
            if (!task.isDeaktiviert()) {
                task.deaktivieren();
                taskCounter++;
            }

        }
        return taskCounter;
    }

    private void setPlan(TaskKonfiguration taskKonfiguration, TaskKonfiguration.Ausfuehrungsplan plan, String timeUnit, Integer rate) {
        taskKonfiguration.setAusfuehrungsplan(plan);
        switch (timeUnit) {
            case "seconds":
                taskKonfiguration.setFixedRate(Duration.ofSeconds(rate));
                taskKonfiguration.setFixedDelay(Duration.ofSeconds(rate));
                break;
            case "minutes":
                taskKonfiguration.setFixedRate(Duration.ofMinutes(rate));
                taskKonfiguration.setFixedDelay(Duration.ofMinutes(rate));
                break;
            case "hours":
                taskKonfiguration.setFixedRate(Duration.ofHours(rate));
                taskKonfiguration.setFixedDelay(Duration.ofHours(rate));
                break;
            case "days":
                taskKonfiguration.setFixedRate(Duration.ofDays(rate));
                taskKonfiguration.setFixedDelay(Duration.ofDays(rate));
                break;
        }

        threadHandOverData.setTargetFileNameHint(taskKonfiguration.getTaskId()+"_"+plan.toString().toLowerCase()+"_"+timeUnit+rate);
    }
}
