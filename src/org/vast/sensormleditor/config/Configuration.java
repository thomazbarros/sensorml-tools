package org.vast.sensormleditor.config;



public class Configuration {

	//public static String coreSchema = "file:///C:/temp/workspace-SensorMLTools/RelaxNG-v1.0/sml/sensorML.rng";
	public static String coreSchema = "./RelaxNG-v1.0/sml/sensorML.rng";
	
	public static String getSchema(){
		 String coreSchema = Configuration.class.getResource("./RelaxNG-v1.0/sml/sensorML.rng").toString();
		return coreSchema;
	}
	
}
