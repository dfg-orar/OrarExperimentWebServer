package orar.experiment;

public class LinuxLocation implements Location {

	@Override
	public String getAppFolderPath() {

		return "/workingdir/app/";
	}

	@Override
	public String getOntoFolderPath() {

		return "/workingdir/onto/";
	}

	@Override
	public String getAuxFolderPath() {
		return "/workingdir/aux/";
	}

	@Override
	public String getUnzipOntoFolderPath() {
		return "/workingdir/unzipOntologies/";
	}

	@Override
	public String getScriptFolderPath() {
		return "/workingdir/script/";
	}

	@Override
	public String getOutputFolderPath() {
		return "/workingdir/output/";
	}

	@Override
	public String getKoncludePath() {
		return "/workingdir/app/KoncludeLinux";
	}

	@Override
	public String getPagodaPath() {
		return "/workingdir/app/pagodaLinux.jar";
	}

	@Override
	public String getOrarPath() {
		return "/workingdir/app/Orar21.jar";
	}

	@Override
	public String getConverterPath() {
		return "/workingdir/app/turtle2OWLConverter.jar";
	}

	@Override
	public String getScriptToRunKoncludePath() {

		return getScriptFolderPath() + "runKoncludeLinux.sh";
	}

	@Override
	public String getLogFolderPath() {
		
		return "/workingdir/log/";
	}
}
