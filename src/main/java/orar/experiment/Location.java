package orar.experiment;

public interface Location {
	/*
	 * methods to get app locations
	 */
	public String getKoncludePath();

	public String getPagodaPath();

	public String getOrarPath();

	public String getConverterPath();

	/*
	 * method to get folders' locations
	 */
	public String getAppFolderPath();

	public String getOntoFolderPath();

	public String getAuxFolderPath();

	public String getUnzipOntoFolderPath();

	public String getScriptFolderPath();

	public String getOutputFolderPath();

	public String getLogFolderPath();
	/*
	 * script to run Konclude
	 */
	public String getScriptToRunKoncludePath();

}
