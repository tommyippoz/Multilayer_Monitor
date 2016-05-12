/**
 * 
 */
package ippoz.multilayer.monitor.master;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import ippoz.multilayer.monitor.support.AppLogger;
import ippoz.multilayer.monitor.support.PreferencesManager;


/**
 * @author Tommy
 *
 */
public class ExperimentMaster {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PreferencesManager masterPreferences;
		MasterManager master;
		try {
			masterPreferences = new PreferencesManager(new File("masterPreferences.preferences"));
			master = new MasterManager(masterPreferences);
			master.setupEnvironment();
			master.startExperimentalCampaign();
			master.flush();
			System.exit(0);
		} catch (IOException e) {
			AppLogger.logException(ExperimentMaster.class, e, "Unhandled IOException");
		} catch (SQLException e) {
			AppLogger.logException(ExperimentMaster.class, e, "Unhandled SQLException");
		}
	}

}
