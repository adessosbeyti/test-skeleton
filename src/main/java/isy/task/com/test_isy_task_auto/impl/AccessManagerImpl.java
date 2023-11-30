package isy.task.com.test_isy_task_auto.impl;
import de.bund.bva.isyfact.aufrufkontext.impl.AufrufKontextImpl;
import de.bund.bva.isyfact.datetime.util.DateTimeUtil;
import de.bund.bva.isyfact.logging.IsyLogger;
import de.bund.bva.isyfact.logging.IsyLoggerFactory;
import de.bund.bva.isyfact.logging.LogKategorie;
import de.bund.bva.isyfact.sicherheit.accessmgr.AccessManager;
import de.bund.bva.isyfact.sicherheit.common.exception.AuthentifizierungFehlgeschlagenException;
import de.bund.bva.isyfact.sicherheit.common.exception.AuthentifizierungTechnicalException;

public class AccessManagerImpl implements AccessManager<AufrufKontextImpl, AuthentifizierungsErgebnisImpl>  {
    private final IsyLogger LOG = IsyLoggerFactory.getLogger(AccessManagerImpl.class);
        @Override
        public AuthentifizierungsErgebnisImpl authentifiziere(AufrufKontextImpl unauthentifizierterAufrufKontext)
                throws AuthentifizierungTechnicalException, AuthentifizierungFehlgeschlagenException {
            AuthentifizierungsErgebnisImpl ergebnis = null;
            int counter=0;

            switch (unauthentifizierterAufrufKontext.getDurchfuehrenderBenutzerKennung()) {
                case "TestUser1": ergebnis = new AuthentifizierungsErgebnisImpl();
                    ergebnis.setRollenIds(new String[] { "Rolle1" });
                    counter=1;
                    break;
                case "TestUser2": ergebnis = new AuthentifizierungsErgebnisImpl();
                    ergebnis.setRollenIds(new String[] { "Rolle2" });
                    counter=2;
                    break;
                case "TestUser3": ergebnis = new AuthentifizierungsErgebnisImpl();
                    ergebnis.setRollenIds(new String[] { });
                    counter=3;
                    break;
            }
            LOG.info(LogKategorie.JOURNAL, "AccessManagerImpl", "{}  authentifiziere executed: "+counter +ergebnis.getRollenIds(),
                    DateTimeUtil.localDateTimeNow());
            return ergebnis;
        }

        @Override
        public void logout(AuthentifizierungsErgebnisImpl authentifzierungErgebnis) {

        }

        @Override
        public boolean pingAccessManager() {
            return true;
        }

        @Override
        public boolean pingAccessManagerByLoginLogout(AufrufKontextImpl unauthentifizierterAufrufKontext) {
            return true;
        }

        @Override
        public void befuelleAufrufkontext(AufrufKontextImpl aufrufKontext,
                                          AuthentifizierungsErgebnisImpl authentifzierungErgebnis) {
            aufrufKontext.setRolle(authentifzierungErgebnis.getRollenIds());
            aufrufKontext.setRollenErmittelt(true);
        }

        @Override
        public Object erzeugeCacheSchluessel(AufrufKontextImpl aufrufKontext) {
            return null;
        }
    }

