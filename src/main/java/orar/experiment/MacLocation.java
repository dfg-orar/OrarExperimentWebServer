package orar.experiment;

public class MacLocation implements Location {

	@Override
	public String getAppFolderPath() {

		return "/Users/kien/workspaceJava/DockerOrarExperiment2/app/";
	}

	@Override
	public String getOntoFolderPath() {

		return "/Users/kien/workspaceJava/DockerOrarExperiment2/onto/";
	}

	@Override
	public String getAuxFolderPath() {
		return "/Users/kien/workspaceJava/DockerOrarExperiment2/aux/";
	}

	@Override
	public String getUnzipOntoFolderPath() {
		return "/Users/kien/workspaceJava/DockerOrarExperiment2/unzipOntologies/";
	}

	@Override
	public String getScriptFolderPath() {
		return "/Users/kien/workspaceJava/DockerOrarExperiment2/script/";
	}

	@Override
	public String getOutputFolderPath() {
		return "/Users/kien/workspaceJava/DockerOrarExperiment2/output/";
	}

	@Override
	public String getKoncludePath() {
		return "/Users/kien/workspaceJava/DockerOrarExperiment2/app/KoncludeMac";
	}

	@Override
	public String getPagodaPath() {

		return "/Users/kien/workspaceJava/DockerOrarExperiment2/app/pagodaMac.jar";
	}

	@Override
	public String getOrarPath() {
		return "/Users/kien/workspaceJava/DockerOrarExperiment2/app/Orar21.jar";
	}

	@Override
	public String getConverterPath() {
		return "/Users/kien/workspaceJava/DockerOrarExperiment2/app/turtle2OWLConverter.jar";
	}

	@Override
	public String getScriptToRunKoncludePath() {

		return getScriptFolderPath() + "runKoncludeMac.sh";
	}

	@Override
	public String getLogFolderPath() {
	
		return "/Users/kien/workspaceJava/DockerOrarExperiment2/log/";
	}

}
