package orar.experiment;

public class Config {

	// TODO: use LinuxLocation when creating jar for the DockerImage
	public static Location location = new LinuxLocation();
//	 public static Location location = new MacLocation(); 
	

	public static String KONCLUDE_PATH = location.getKoncludePath();
	public static String ORAR_PATH = location.getOrarPath();
	public static String PAGODA_PATH = location.getPagodaPath();
	public static String CONVERTER_PATH = location.getConverterPath();
	/// folders for app
	public static String APP_FOLDER = location.getAppFolderPath();
	// for input ontology from web
	public static String ONTO_FOLDER = location.getOntoFolderPath();
	// for extraced ontologies from zip file
	public static String UNZIP_ONTO_FOLDER = location.getUnzipOntoFolderPath();
	// for aboxlist, and converted ontology
	public static String AUX_FOLDER = location.getAuxFolderPath();
	public static String OUTPUT_FOLDER = location.getOutputFolderPath();
	public static String SCRIPT_FOLDER = location.getScriptFolderPath();
	public static String RUN_KONCLUDE_SCRIPT = location.getScriptToRunKoncludePath();
	public static String LOG_FOLDER=location.getLogFolderPath();
	/*
	 * end of folder names
	 */

	public static String ZIP_ONTOS_FILE_NAME = "pairsOfOntologies.zip";
	public static String ZIP_ONTOS_FULLPATH = ONTO_FOLDER + "pairsOfOntologies.zip";

	public static String MATERIALIZED_ABOX_ZIP = OUTPUT_FOLDER + "result.zip";

	public static String TBOX_FILENAME = "tbox.owl";
	public static String TBOX_FULLPATH = ONTO_FOLDER + TBOX_FILENAME;

	public static String ABOX_FILENAME = "abox.ttl";
	public static String ABOX_FULLPATH = ONTO_FOLDER + ABOX_FILENAME;

	public static String ABOX_LIST_FILENAME = "aboxList.txt";
	public static String ABOX_LIST_FULLPATH = AUX_FOLDER + ABOX_LIST_FILENAME;

	public static String Orar_LOG_FULLPATH = LOG_FOLDER + "experiment.log";
	public static String Konclude_LOG_FULLPATH = ONTO_FOLDER + "konclude.log";
	public static String OWLAPIONTO_FULLPATH_FOR_KONCLUDE = AUX_FOLDER + "onto.functionalsyntax.owl";

}
