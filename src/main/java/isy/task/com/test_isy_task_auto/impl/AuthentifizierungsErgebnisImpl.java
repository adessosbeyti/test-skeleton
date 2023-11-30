package isy.task.com.test_isy_task_auto.impl;
import de.bund.bva.isyfact.sicherheit.accessmgr.AuthentifzierungErgebnis;
public class AuthentifizierungsErgebnisImpl implements AuthentifzierungErgebnis {


        private static final long serialVersionUID = 1L;
        private String[] rollenIds;

        public String[] getRollenIds() {
            return rollenIds;
        }

        public void setRollenIds(String[] rollenIds) {
            this.rollenIds = rollenIds;
        }
    }

