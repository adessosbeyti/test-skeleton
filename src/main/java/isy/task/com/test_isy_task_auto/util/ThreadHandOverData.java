package isy.task.com.test_isy_task_auto.util;

public class ThreadHandOverData {
    private String targetFileNameHint="0";

    private String logFramework= "S";

    public ThreadHandOverData() {

        targetFileNameHint = "AutoTask";
    }
    public void setTargetFileNameHint(String targetFileNameContent){

           targetFileNameHint = targetFileNameContent;

    }
    public void setLogFramework(String logFramework){
         this.logFramework=logFramework;
    }
    public String getLogFramework(){
        return  logFramework;
    }
    public String getTargetFileNameHint(){

         return targetFileNameHint;
    }
}
