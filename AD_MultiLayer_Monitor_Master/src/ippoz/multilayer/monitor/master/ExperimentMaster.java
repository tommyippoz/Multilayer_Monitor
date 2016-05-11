/**
 * 
 */
package ippoz.multilayer.monitor.master;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
