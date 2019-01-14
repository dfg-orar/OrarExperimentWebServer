package orar.experiment;
/*-
 * #%L
 * This project is based on the following project:
 * 
 * Docker Image for Axiom Pinpointing Experiments
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2017 - 2018 Live Ontologies Project
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

//import javax.annotation.concurrent.GuardedBy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

public class ExperimentServer extends NanoHTTPD {

	private static final Logger LOGGER_ = LoggerFactory.getLogger(ExperimentServer.class);

	public static final String OPT_COMMAND = "command";
	private Boolean konclude_standalone_is_choosen = false;
	private Boolean get_out_put = false;
	private Boolean orar_is_choosen = false;
	private Boolean pagoda_is_choosen = false;
	private Boolean run_many_ontologies = false;
	public static final Integer DEFAULT_PORT = 4040;
	private File resultsFile_;

	public static void main(final String[] args) {

		try {

			LOGGER_.info("Binding server to port number {}", DEFAULT_PORT);

			new ExperimentServer(DEFAULT_PORT);

		} catch (final IOException e) {
			LOGGER_.error("Cannot start server!", e);
			System.exit(1);

		}
	}

	// public ExperimentServer(final int port), final File availableExpsDir, final
	// File workspace, final String... command)
	public ExperimentServer(final int port) throws IOException {
		super(port);
		// this.workspace_ = workspace;
		// Utils.cleanIfNotDir(this.workspace_);

		// this.command_ = command;
		start(NanoHTTPD.SOCKET_READ_TIMEOUT * 5, false);

		LOGGER_.info("Server running ;-)");
	}

	// private final File workspace_;
	// private final File inputDir_;
	// private final File expsDir_;
	// private final File resultsDir_;
	// private final File plotsDir_;
	// private final File resultsFile_;
	// private final String[] command_;

	// @GuardedBy("this")
	private Process experimentProcess_ = null;
	// @GuardedBy("this")
	private StringBuilder experimentLog_ = new StringBuilder();
	// @GuardedBy("this")
	private StringBuilder experimentLogLastLine_ = new StringBuilder();

	private static final String FIELD_TIMEOUT_ = "timeout";
	private static final String FIELD_GLOBAL_TIMEOUT_ = "global_timeout";
	private static final String FIELD_TBOX_ = "tbox";
	private static final String FIELD_ABOX_ = "abox";
	private static final String FIELD_ZIP_ONTO_ = "zipOnto";

	// @formatter:off
	private static final String TEMPLATE_RUNALL = "<!DOCTYPE html>\n" + "<html>\n" + "<body>\n" + "<style>"
			+ "input[type=submit] {\n" + "    width: 50px;\n" + "    background-color: #4CAF50;\n"
			+ "    color: white;\n" + "    padding: 7px 10px;\n" + "    margin: 8px 0;\n" + "    border: none;\n"
			+ "    border-radius: 4px;\n" + "    cursor: pointer;\n" + "}" + "</style>"
			+ "<center><h1>Web interface for Evaluation of Orar</h1></center>"

			+ "<form method='post' enctype='multipart/form-data'>\n"

			+ "<p><label for='" + FIELD_GLOBAL_TIMEOUT_
			+ "'> <h4>Timeout:</h4> Timeout for each reasoner in seconds, 0 for no timeout:</label>\n"
			+ "<input type='number' name='" + FIELD_GLOBAL_TIMEOUT_ + "' min='0' step='1' value='%s'></p>\n"
			+ "<input type=\"checkbox\" name=\"lubm10\" value=\"yes\" checked> LUBM 10 &nbsp;&nbsp;\n"
			+ "  <input type=\"checkbox\" name=\"lubm50\" value=\"yes\" checked> LUBM 50 &nbsp;&nbsp;\n"
			+ "  <input type=\"checkbox\" name=\"lubm100\" value=\"yes\" checked> LUBM 100 &nbsp;&nbsp;\n"
			+ "  <input type=\"checkbox\" name=\"lubm500\" value=\"yes\" checked> LUBM 500<br>\n"
			+ "  <input type=\"checkbox\" name=\"imdb\" value=\"yes\" checked> IMDB &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n"
			+ "  <input type=\"checkbox\" name=\"npd\" value=\"yes\" checked> NPD &nbsp;&nbsp;\n"
			+ "  <input type=\"checkbox\" name=\"dbpedia\" value=\"yes\" checked> DBPedia+&nbsp;&nbsp;\n"
			+ "  <input type=\"checkbox\" name=\"chembl\" value=\"yes\" checked> ChemBL\n"
			+ "  <input type=\"checkbox\" name=\"uniport\" value=\"yes\" checked> Uniport <br>\n"
			+ "  <input type=\"checkbox\" name=\"uobm10\" value=\"yes\" checked> UOBM 10 &nbsp;&nbsp;\n"
			+ "  <input type=\"checkbox\" name=\"uobm50\" value=\"yes\" checked> UOBM 50 &nbsp;&nbsp;\n"
			+ "  <input type=\"checkbox\" name=\"uobm100\" value=\"yes\" checked> UOBM 100 &nbsp;&nbsp;\n"
			+ "  <input type=\"checkbox\" name=\"uobm500\" value=\"yes\" checked> UOBM 500<br>"
			+ "<p>Click this <input type='submit' name='button' value='Run all'> button to reproduce the evaluation results presented in the IJCAI submission\n"
			+ "  </form>\n" + "Or run with your own ontologies <a href=/>here</a>.</p>\n" + "  <pre id='log'>\n"
			+ "</body>\n" + "</body>\n" + "</html>";
	private static final String TEMPLATE_INDEX_ = "<!DOCTYPE html>\n" + "<html>\n" + "<body>\n" + "<style>"
			+ "input[type=submit] {\n" + "    width: 50px;\n" + "    background-color: #4CAF50;\n"
			+ "    color: white;\n" + "    padding: 7px 10px;\n" + "    margin: 8px 0;\n" + "    border: none;\n"
			+ "    border-radius: 4px;\n" + "    cursor: pointer;\n" + "}" + "</style>"
			+ "<center><h1>Web interface for Evaluation of Orar</h1></center>"

			+ "<form method='post' enctype='multipart/form-data'>\n"

			/*
			 * Elements for input Ontology
			 */
			+ "<h4>Input Option 1</h4>" //
			+ " Upload TBox file loadable by OWL API: <input type=\"file\" name=\"" + FIELD_TBOX_ + "\"><br>\n"//
			+ " Upload ABox file in Turtle/N3 syntax: <input type='file' name='" + FIELD_ABOX_ + "'><br> \n" //
			+ "<h4>Input Option 2 (Choosing this option will override Option 1)</h4>" //
			+ " Upload .zip file containing pairs of TBox and ABox of the <mark>same base name</mark>: <input type='file' accept=\".zip\" name='"
			+ FIELD_ZIP_ONTO_ + "'><br> \n" //

			/*
			 * Elements for Reasoners
			 */
			+ "<h4>Reasoners</h4>" + " <input type=\"checkbox\" name=\"reasonerOrar\" value=\"orar\"> Orar &nbsp"
			+ " <input type=\"checkbox\" name=\"output\" value=\"output\"> with output ABox/result (only available if Orar is chosen) <br>"
			+ " <input type=\"checkbox\" name=\"reasonerKonclude\" value=\"konclude\"> Konclude<br>"
			+ " <input type=\"checkbox\" name=\"reasonerPagoda\" value=\"pagoda\"> PagodA<br>"
			+ "    <p>Click this <input type='submit' value='Run'> button to run the selected reasoners\n"
			// + " <p>If you want to cancel your selected input, refresh you webpage\n"
			+ "  </form>\n"//
			+ "<h4>Instructions</h4>" + "<ul>" //
			+ "<li> You need to provide input (Option 1 or Option 2) and select at least one reasoner to run the experiment </li>" //

			+ "<li> Use <b>Input Option 1</b> to upload one ontolgy consiting of its TBox and ABox files  </li>"//
			+ "<li> Use <b>Input Option 2 </b>to upload .zip file of several ontologies; each ontology consists of a pair of a TBox-file and an ABox-file:<br> "
			+ "The TBox-file must be loadable by OWLAPI and has <mark>.owl</mark> extension <br>"
			+ "The ABox-file must has Turtle/N3 syntax and <mark>.ttl</mark> extension <br>"//
			+ "All files must be in the top folder <br>"
			+ " <b>Example</b>: the <i>onto.zip</i> file consists of: <i>nameOntology1.owl, nameOntology1.ttl, nameOntology2.owl, nameOntology2.ttl </i> <br>" //
			+ " will be processed as two ontologies: <br>"//
			+ "the first one is with <i>nameOntology1.owl</i> as a TBox and <i>nameOntolog1.ttl</i> as an ABox <br>"//
			+ "the second one is with <i>nameOntology2.owl</i> as a TBox and <i>nameOntolog2.ttl</i> as an ABox <br>"//
			+ "</li>"//
			+ "<li>Refresh your webpage to remove the selected input file(s) </li>" //
			+ "<li><b>Note 1</b>: The maximal size of the file can be uploaded in both options is 2 GB </li>"//
			+ "<li><b>Note 2</b>: Each time you access the start page, e.g. localhost:4040/, the previous experiment (if any) will be stopped</li>"//
			+ "</ul>" //
			+ "</body>\n"//
			+ "</html>";//
	private static final String TEMPLATE_LOG_ = "<!DOCTYPE html>\n" + "<html>\n" + "<body>\n"
			+ "  <h1>Experiment log</h1>\n"
			+ "  <p><a href=/kill/>Kill</a> (Carefully! No confirmation, no questions asked.)"
			+ "  <pre id='log'>%s</pre>\n" + "  <script type='text/javascript'>\n"
			+ "    var source = new EventSource('/log_source/');\n" + "    source.onmessage = function(event) {\n"
			+ "      document.getElementById('log').innerHTML = event.data;\n"
			+ "      if (event.lastEventId == 'last_log') {\n" + "        source.close();\n"
			+ "        window.location.assign('/done/');\n" + "      }\n" + "    };\n" + "  </script>\n" + "</body>\n"
			+ "</html>";
	private static final String TEMPLATE_LOG_ALL_ = "<!DOCTYPE html>\n" + "<html>\n" + "<body>\n"
			+ "  <h1>Experiment log</h1>\n"
			+ "  <p><a href=/killall/>Kill</a> (Carefully! No confirmation, no questions asked.)"
			+ "  <pre id='log'>%s</pre>\n" + "  <script type='text/javascript'>\n"
			+ "    var source = new EventSource('/log_source/');\n" + "    source.onmessage = function(event) {\n"
			+ "      document.getElementById('log').innerHTML = event.data;\n"
			+ "      if (event.lastEventId == 'last_log') {\n" + "        source.close();\n"
			+ "        window.location.assign('/doneall/');\n" + "      }\n" + "    };\n" + "  </script>\n"
			+ "</body>\n" + "</html>";

	private static final String TEMPLATE_DONE_ = "<!DOCTYPE html>\n" + "<html>\n" + "<body>\n"
			+ "  <h1>Experiment finished</h1>\n"
			// + " <p>View the results <a href=/results/>here</a>."
			+ " Download the result (the materialized ABox in OWLXML format) <a href=/results.zip>here</a>."
			+ " <br>Start from beginning <a href=/>here</a>.</p>\n" + "  <pre id='log'>%s</pre>\n" + "</body>\n"
			+ "</html>";
	private static final String TEMPLATE_DONE_WITHOUT_RESULTDOWNLOAD = "<!DOCTYPE html>\n" + "<html>\n" + "<body>\n"
			+ "  <h1>Experiment finished</h1>\n"
			// + " <p>The output file (the materialized ABox in OWLXML file) is only
			// available when you choose Orar."
			// + " Download the results from <a href=/results.zip>here</a>."
			+ "  Start from beginning <a href=/>here</a>.</p>\n" + "  <pre id='log'>%s</pre>\n" + "</body>\n"
			+ "</html>";
	private static final String TEMPLATE_DONE_ALL_ = "<!DOCTYPE html>\n" + "<html>\n" + "<body>\n"
			+ "  <h1>Experiment finished</h1>\n" + "  <p>View the results <a href=/resultsall/>here</a>."
			// + " Download the results from <a href=/results.zip>here</a>."
			+ "  Or run with your own ontologies <a href=/>here</a>.</p>\n" + "  <pre id='log'>%s</pre>\n" + "</body>\n"
			+ "</html>";
	private static final String TEMPLATE_RESULTS_ = "<!doctype html>\n" + "<body>\n" + "  <h1>Experiment results</h1>\n"
			+ "  <p>Download the results from <a href=/results.zip>here</a>."
			+ "  See the log <a href=/done/>here</a>.\n" + "  Or start from beginning <a href=/>here</a>.</p>\n"
			+ "  %s\n"// The plot
			+ "  %s\n"// The result list
			+ "</body>";
	// The first line and no <html> tag seem to have huge impact on performance!
	private static final String TEMPLATE_RESULT_FILE_ = "<!doctype html>\n" + "<body>\n" + "  <h1>%s</h1>\n"// Title
			+ "  <div id=\"handsontable-container\"></div>\n" + "  <pre id='data'>%s</pre>\n"// Result file
			+ "  <script src=\"https://cdn.jsdelivr.net/handsontable/0.28.4/handsontable.full.min.js\"></script>\n"
			+ "  <script src=\"https://cdn.jsdelivr.net/papaparse/4.1.2/papaparse.min.js\"></script>\n"
			+ "  <script type='text/javascript'>\n" + "    var dataElement = document.getElementById('data')\n"
			+ "    var dataString = dataElement.innerHTML\n" + "    dataElement.innerHTML = ''\n"
			+ "    var data = Papa.parse(dataString, {header: true, skipEmptyLines: true})\n"
			+ "    var handsontableContainer = document.getElementById('handsontable-container')\n"
			+ "    Handsontable(handsontableContainer, {data: data.data, rowHeaders: true, colHeaders: data.meta.fields, columnSorting: true, wordWrap: false})\n"
			+ "  </script>\n"
			+ "  <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/handsontable/0.28.4/handsontable.full.min.css\">\n"
			+ "</body>";
	private static final String TEMPLATE_ALREADY_RUNNING_ = "<!DOCTYPE html>\n" + "<html>\n" + "<body>\n"
			+ "  <h1>An experiment is already running!</h1>\n" + "  <a href=/log/>See the log here.</a>\n" + "</body>\n"
			+ "</html>";
	private static final String TEMPLATE_NOT_FOUND_ = "<!DOCTYPE html>\n" + "<html>\n" + "<body>\n"
			+ "  <h1>404 Not Found!</h1>\n" + "  <pre>\n" + "%s"// uri path
			+ "  </pre>\n" + "</body>\n" + "</html>";
	private static final String TEMPLATE_INTERNAL_ERROR_ = "<!DOCTYPE html>\n" + "<html>\n" + "<body>\n"
			+ "  <h1>500 %s</h1>\n" + "  <pre>\n" + "%s"// exception message
			+ "  </pre>\n" + "</body>\n" + "</html>";

	// @formatter:on
	private static final Pattern URI_INDEX_ALL_ = Pattern.compile("^/runall/$");
	private static final Pattern URI_INDEX_ = Pattern.compile("^/$");
	private static final Pattern URI_LOG_ = Pattern.compile("^/log/?$");
	private static final Pattern URI_LOG_ALL_ = Pattern.compile("^/logall/?$");
	private static final Pattern URI_LOG_SOURCE_ = Pattern.compile("^/log_source/?$");
	private static final Pattern URI_DONE_ = Pattern.compile("^/done/?$");
	private static final Pattern URI_DONE_ALL_ = Pattern.compile("^/doneall/?$");
	private static final Pattern URI_KILL_ = Pattern.compile("^/kill/?$");
	private static final Pattern URI_KILL_ALL_ = Pattern.compile("^/killall/?$");
	private static final Pattern URI_RESULTS_ = Pattern.compile("^/results/?$");
	private static final Pattern URI_RESULTS_ALL_ = Pattern.compile("^/resultsall/$");
	private static final Pattern URI_RESULTS_FILE_ = Pattern.compile("^/results/(?<file>[^/]+)$");
	private static final Pattern URI_PLOT_FILE_ = Pattern.compile("^/results/plots/(?<file>[^/]+)$");
	private static final Pattern URI_RESULTS_ARCHIVE_ = Pattern.compile("^/results.zip$");

	@Override
	public Response serve(final IHTTPSession session) {
		LOGGER_.info("request URI: {}", session.getUri());
		try {

			final URI requestUri = new URI(session.getUri());
			if (URI_LOG_.matcher(requestUri.getPath()).matches()) {
				return logView(session);
			} else if (URI_INDEX_.matcher(requestUri.getPath()).matches()) {
				return responseToIndexView(session);
			} else if (URI_LOG_.matcher(requestUri.getPath()).matches()) {
				return logView(session);
			} else if (URI_LOG_ALL_.matcher(requestUri.getPath()).matches()) {
				return logAllView(session);
			} else if (URI_LOG_SOURCE_.matcher(requestUri.getPath()).matches()) {
				return logSourceView(session);
			} else if (URI_RESULTS_ALL_.matcher(requestUri.getPath()).matches()) {
				return resultAllView(session);
			} else if (URI_DONE_.matcher(requestUri.getPath()).matches()) {
				return doneView(session);
			} else if (URI_KILL_.matcher(requestUri.getPath()).matches()) {
				return killView(session);
			} else if (URI_KILL_ALL_.matcher(requestUri.getPath()).matches()) {
				return killAllView(session);
			} else if (URI_DONE_ALL_.matcher(requestUri.getPath()).matches()) {
				return doneAllView(session);
			} else if (URI_RESULTS_ARCHIVE_.matcher(requestUri.getPath()).matches()) {
				return resultsArchiveView(session);
			} else {
				return newFixedLengthResponse(Status.NOT_FOUND, NanoHTTPD.MIME_HTML,
						String.format(TEMPLATE_NOT_FOUND_, requestUri.getPath()));
			}
		} catch (final URISyntaxException e) {
			return newErrorResponse("Illegal request URI!", e);
		}
	}

	/*
	 * Process for Start page of Orar-web
	 */
	private Response responseToIndexView(final IHTTPSession session) {
		LOGGER_.info("index view");

		/*
		 * reset options
		 */
		boolean formDataIsReady = true;
		this.orar_is_choosen = false;
		this.konclude_standalone_is_choosen = false;
		this.pagoda_is_choosen = false;
		this.get_out_put = false;
		this.run_many_ontologies = false;

		/*
		 * Kill all apps if still running
		 */
		UtilMethod.kill_all_apps();
		/*
		 * Delete all old files
		 */
		String deleteOldFiles = "rm -r " + Config.ONTO_FOLDER + "* ";
		deleteOldFiles += Config.UNZIP_ONTO_FOLDER + "* ";
		deleteOldFiles += Config.OUTPUT_FOLDER + "* ";
		deleteOldFiles += Config.AUX_FOLDER + "* ";

		String deleteTmpDirGeneratedByPagoda = "rm -r tmp_*";
		LOGGER_.info("Deleting existing files from previous experiemtns...");
		UtilMethod.executeCommand(deleteOldFiles);
		UtilMethod.executeCommand(deleteTmpDirGeneratedByPagoda);

		/*
		 * ****************************************
		 */
		final Map<String, String> validationMessages = new HashMap<>(2);
		validationMessages.put(FIELD_TIMEOUT_, "");
		validationMessages.put(FIELD_GLOBAL_TIMEOUT_, "");
		validationMessages.put(FIELD_TBOX_, "");
		String ontologies = "";
		try {
			final Map<String, String> files = new HashMap<String, String>();
			session.parseBody(files);
			final Map<String, String> params = session.getParms();
			Map<String, String> header = session.getHeaders();
			LOGGER_.info("Header= " + header.toString());
			LOGGER_.info("Query parameter string: " + session.getQueryParameterString());

			LOGGER_.info("params: " + params.toString());
			LOGGER_.info("Size=" + files.size());

			/*
			 * Check necessary input is provided first, check if zip file containing
			 * ontologies is provided
			 */
			boolean zipOntologiesProvided = getInputFromZipFileOption2(params, files);
			if (zipOntologiesProvided) {
				this.run_many_ontologies = true;
			} else {
				/*
				 * if not, then check if the TBox is provided
				 */
				boolean tboxIsProvided = getInputTBoxOption1(params, files);
				if (!tboxIsProvided) {
					formDataIsReady = false;
				}
				/*
				 * and check if the ABox is provided
				 */
				boolean aboxIsProvided = getInputABoxOption1(params, files);
				if (!aboxIsProvided) {
					formDataIsReady = false;
				}
			}

			/*
			 * check if one of the reasoners is provided
			 */
			if (!params.containsKey("reasonerOrar") && !params.containsKey("reasonerKonclude")
					&& !params.containsKey("reasonerPagoda")) {
				formDataIsReady = false;
			}

			/*
			 * Start experiment
			 */
			if (formDataIsReady) {
				LOGGER_.info("Starting the experiments!");
				try {
					synchronized (this) {
						if (experimentProcess_ != null) {
							if (experimentProcess_.isAlive()) {
								return newFixedLengthResponse(TEMPLATE_ALREADY_RUNNING_);
							} else {
								try {
									updateExperimentLog();
								} catch (final IOException e) {
									return newErrorResponse("Cannot read experiment log!", e);
								}
								experimentProcess_ = null;
								final Response response = newFixedLengthResponse(Status.REDIRECT, NanoHTTPD.MIME_HTML,
										"");
								response.addHeader("Location", "/done/");
								return response;
							}
						} else {

							experimentLog_.setLength(0);
							experimentLogLastLine_.setLength(0);

							if (!this.run_many_ontologies) {
								String command = createAllCommands(params, "input_ontology", Config.TBOX_FULLPATH,
										Config.ABOX_FULLPATH);
								executeCommandToRunExperiment(command);
							} else {

								UtilMethod.executeCommand(
										"unzip -o " + Config.ZIP_ONTOS_FULLPATH + " -d " + Config.UNZIP_ONTO_FOLDER);
								SortedSet<String> nameOfOntologies = getNamesOfOntologies();
								LOGGER_.info("Names of ontologies:" + nameOfOntologies);
								String command_for_all_ontologies = "";
								for (String eachOntoName : nameOfOntologies) {
									String tboxFullPath = Config.UNZIP_ONTO_FOLDER + eachOntoName + ".owl";
									String aboxFullPath = Config.UNZIP_ONTO_FOLDER + eachOntoName + ".ttl";
									String commandForThisOnto = createAllCommands(params, eachOntoName, tboxFullPath,
											aboxFullPath);
									command_for_all_ontologies += commandForThisOnto;
								}
								executeCommandToRunExperiment(command_for_all_ontologies);
							}

							final Response response = newFixedLengthResponse(Status.REDIRECT, NanoHTTPD.MIME_HTML, "");
							response.addHeader("Location", "/log/");
							return response;

						}

					}

				} catch (final IOException e) {
					return newErrorResponse("Cannot start the experiment!", e);
				}
			} else {
				return newFixedLengthResponse(TEMPLATE_INDEX_);
			}

		} catch (final IOException | ResponseException e) {
			return newErrorResponse("Cannot parse the request!", e);
		}
	}

	private SortedSet<String> getNamesOfOntologies() {
		SortedSet<String> names = new TreeSet<>();
		File folder = new File(Config.UNZIP_ONTO_FOLDER);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			File file = listOfFiles[i];
			if (file.isFile()) {
				// LOGGER_.info("***DEBUG file: ="+file.getAbsolutePath());
				String name = FilenameUtils.getBaseName(file.getName());
				if (!name.isEmpty()) {
					names.add(name);
				}
			}
		}
		return names;
	}

	// private void executeCommandFor1Ontology(Map<String, String> params, String
	// ontoName, String tboxFullPath,
	// String aboxFullPath) throws IOException {
	// // UtilMethod.createABoxListFile(Location.ABOX_FULLPATH);
	// String allCommands = createAllCommands(params, ontoName, tboxFullPath,
	// aboxFullPath);
	// LOGGER_.info("Commands to run experiment: {}", allCommands);
	// ProcessBuilder processBuilder = new ProcessBuilder("sh", "-c", allCommands);
	// processBuilder.redirectErrorStream(true);
	//
	// experimentProcess_ = processBuilder.start();
	//
	// }

	private void executeCommandToRunExperiment(String command) throws IOException {
		// UtilMethod.createABoxListFile(Location.ABOX_FULLPATH);
		LOGGER_.info("commands to run experiment: {}\"", command);
		ProcessBuilder processBuilder = new ProcessBuilder("sh", "-c", command);
		processBuilder.redirectErrorStream(true);

		experimentProcess_ = processBuilder.start();

	}

	private boolean getInputFromZipFileOption2(Map<String, String> params, Map<String, String> files) {
		if (params.containsKey(FIELD_ZIP_ONTO_) && files.containsKey(FIELD_ZIP_ONTO_)) {
			final File source = new File(files.get(FIELD_ZIP_ONTO_));
			final File target = new File(Config.ZIP_ONTOS_FULLPATH);
			LOGGER_.info("ontolgiesTmpFile: {}", source);
			if (source.exists()) {
				LOGGER_.info("copying ontologies");
				try {
					Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private boolean getInputTBoxOption1(Map<String, String> params, Map<String, String> files) {
		if (params.containsKey(FIELD_TBOX_) && files.containsKey(FIELD_TBOX_)) {
			final File source = new File(files.get(FIELD_TBOX_));
			final File target = new File(Config.TBOX_FULLPATH);
			LOGGER_.info("ontolgiesTmpFile: {}", source);
			if (source.exists()) {
				LOGGER_.info("copying ontologies");
				try {
					Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	private boolean getInputABoxOption1(Map<String, String> params, Map<String, String> files) {
		if (params.containsKey(FIELD_ABOX_) && files.containsKey(FIELD_ABOX_)) {
			final File source = new File(files.get(FIELD_ABOX_));
			final File target = new File(Config.ABOX_FULLPATH);
			LOGGER_.info("ontolgiesTmpFile: {}", source);
			if (source.exists()) {
				LOGGER_.info("copying ABox");
				try {
					Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private Response resultsArchiveView(final IHTTPSession session) {
		LOGGER_.info("results archive view");

		// UtilMethod.createZipFile(Location.MATERIALIZED_ABOX_ZIP,
		// Location.OUTPUT_FOLDER);
		this.resultsFile_ = new File(Config.MATERIALIZED_ABOX_ZIP);
		if (!this.resultsFile_.exists()) {
			UtilMethod.zipAFolder(Config.MATERIALIZED_ABOX_ZIP, Config.OUTPUT_FOLDER);
		}
		if (!resultsFile_.exists() || resultsFile_.isDirectory()) {
			return newFixedLengthResponse(Status.NOT_FOUND, NanoHTTPD.MIME_HTML,
					String.format(TEMPLATE_NOT_FOUND_, resultsFile_.getPath()));
		}
		// else
		try {
			final InputStream data = new FileInputStream(resultsFile_);
			final Response response = newChunkedResponse(Status.OK, "application/octet-stream", data);
			response.addHeader("Content-Disposition", "attachment; filename=\"results.zip\"");
			return response;
		} catch (final FileNotFoundException e) {
			return newErrorResponse("Cannot find the results file!", e);
		}
	}

	private String createAllCommands(Map<String, String> params, String ontoName, String fullPathTBox,
			String fullPathABox) {
		/*
		 * update options based on params
		 */
		if (params.containsKey("reasonerOrar")) {
			this.orar_is_choosen = true;
		}

		if (params.containsKey("reasonerKonclude")) {
			this.konclude_standalone_is_choosen = true;
		}

		if (params.containsKey("reasonerPagoda")) {
			this.pagoda_is_choosen = true;
		}

		if (params.containsKey("output")) {
			this.get_out_put = true;
		}

		String allCommands = "";
		allCommands += "printf \"====== BEGIN EXPERIMENT FOR ONTOLOGY: " + ontoName + " ======\n\" +; ";

		/*
		 * Orar
		 */
		String aboxListFullPath = Config.AUX_FOLDER + "aboxList" + ontoName + ".txt";
		UtilMethod.createABoxListFile(fullPathABox, aboxListFullPath);

		List<String> arrayCommandForOrar = new ArrayList<>();
		arrayCommandForOrar.add("java");
		arrayCommandForOrar.add("-jar");
		arrayCommandForOrar.add("-Xmx512G ");
		arrayCommandForOrar.add(Config.ORAR_PATH);
		arrayCommandForOrar.add("-koncludepath");
		arrayCommandForOrar.add(Config.KONCLUDE_PATH);
		arrayCommandForOrar.add("-port");
		arrayCommandForOrar.add("9090");
		arrayCommandForOrar.add("-tbox");
		arrayCommandForOrar.add(fullPathTBox);
		arrayCommandForOrar.add("-abox");
		arrayCommandForOrar.add(aboxListFullPath);
		arrayCommandForOrar.add("-reasoningtime");
		arrayCommandForOrar.add("-statistic");
		if (this.get_out_put) {
			arrayCommandForOrar.add("-outputabox " + Config.OUTPUT_FOLDER + "materializedABox_" + ontoName + ".owl");
		}
		// arrayCommandForOrar.add(" > ");
		// arrayCommandForOrar.add(FileLocation.Konclude_LOG_FULLPATH + " ; ");
		// arrayCommandForOrar.add("cat "+ FileLocation.Konclude_LOG_FULLPATH);
		String bashCommandForOrar = "";
		for (String s : arrayCommandForOrar) {
			bashCommandForOrar += s + " ";
		}
		/*
		 * Pagoda
		 */
		String commandForPagoda = "java -jar -Xmx512G " + Config.PAGODA_PATH + " " + fullPathTBox + " "
				+ aboxListFullPath;
		/*
		 * konclude
		 */
		String createOntoForCondludeCommand = " java -jar -Xmx512G " + Config.CONVERTER_PATH + " -tbox " + fullPathTBox
				+ " -abox " + aboxListFullPath + " -format functional " + "-output "
				+ Config.OWLAPIONTO_FULLPATH_FOR_KONCLUDE;

		String commandForKonclude = Config.KONCLUDE_PATH + " realization -i "
		// + Location.OWLAPIONTO_FULLPATH_FOR_KONCLUDE +" 2>&1 | head ; ";
		// + "printf \"END EXPERIMENT FOR ONTOLOGY\" ;";
				+ Config.OWLAPIONTO_FULLPATH_FOR_KONCLUDE + " -a -v > " + Config.Konclude_LOG_FULLPATH + " ; ";
		// String logString = UtilMethod.readContent(Location.Konclude_LOG_FULLPATH);
		// commandForKonclude+="printf \""+logString+"\"" + " ;";

		if (this.orar_is_choosen) {
			allCommands += "echo \"****************************************************************\" ; ";
			allCommands += "echo \"Orar started\" ; ";
			allCommands += bashCommandForOrar + " ; ";
			allCommands += "echo \"Orar finished\" ; ";
			allCommands += "echo \"****************************************************************\" ; ";
		}

		if (this.pagoda_is_choosen) {
			allCommands += "echo \"****************************************************************\" ; ";
			allCommands += "echo \"Pagoda started\" ; ";
			allCommands += commandForPagoda + " ; ";
			allCommands += "echo \"Pagoda finished\" ; ";
			allCommands += "echo \"****************************************************************\" ; ";
		}

		if (this.konclude_standalone_is_choosen) {
			allCommands += "echo \"****************************************************************\" ; ";
			allCommands += "echo \"Converting the input ontology to OWL-Functional syntax\" ; ";
			allCommands += createOntoForCondludeCommand + " ; ";
			allCommands += "echo \"Conversion finished\" ; ";
			allCommands += "echo \"****************************************************************\" ; ";
			// allCommands += "echo \"Konclude started\" ; ";
			allCommands += Config.RUN_KONCLUDE_SCRIPT + " ; ";
			// allCommands += Location.ONTO_FOLDER+"catKoncludelog.sh" +" ; " ;
			// String logString = UtilMethod.readContent(Location.Konclude_LOG_FULLPATH);
			// commandForKonclude+="printf \""+logString+"\"" + " ;";

		}

		// allCommands = createOntoForCondludeCommand + " ; " +
		// FileLocation.echoOrarStart + " ; " + bashCommandForOrar
		// + " ; " + FileLocation.echoOrarStop + " ; " + commandForPagoda + " ; " +
		// commandForKonclude;
		allCommands += "printf \"====== END EXPERIMENT FOR ONTOLOGY: " + ontoName + " ======\n\n\" ; ";

		return allCommands;
	}

	private Response resultAllView(final IHTTPSession session) {
		LOGGER_.info("index view");
		return newFixedLengthResponse(HTMLString.TEST_BAR);
		// return newFixedLengthResponse(HTMLString.TABLE2+ HTMLString.TEST_BAR );
	}

	private Response logView(final IHTTPSession session) {
		LOGGER_.info("log view");
		return newFixedLengthResponse(String.format(TEMPLATE_LOG_, ""));
	}

	private Response logAllView(final IHTTPSession session) {
		LOGGER_.info("log view");
		return newFixedLengthResponse(String.format(TEMPLATE_LOG_ALL_, ""));
	}

	private synchronized Response doneView(final IHTTPSession session) {
		LOGGER_.info("done view");
		String logMessage = experimentLogToString();
		// if (this.konclude_standalone_is_choosen) {
		// logMessage += UtilMethod.readContent(Location.Konclude_LOG_FULLPATH);
		// }
		/*
		 * Get timestamp
		 */
		// Date date = new Date();
		// String strDateFormat = "DD_MM_yyyy_HH_mm";
		// DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
		// String timeNow = dateFormat.format(date);
		// String currentLogFilePath = Config.Orar_LOG_FULLPATH + "." + timeNow;
		UtilMethod.createLogFile(Config.Orar_LOG_FULLPATH, logMessage);
		LOGGER_.info("A log file was created at:" + Config.Orar_LOG_FULLPATH);
		if (this.get_out_put) {
			// return html with link to download result
			return newFixedLengthResponse(String.format(TEMPLATE_DONE_, logMessage));
		} else {
			return newFixedLengthResponse(String.format(TEMPLATE_DONE_WITHOUT_RESULTDOWNLOAD, logMessage));
		}
	}

	private synchronized Response doneAllView(final IHTTPSession session) {
		LOGGER_.info("done all view");
		String logMessage = experimentLogToString();
		logMessage += UtilMethod.readContent(Config.Konclude_LOG_FULLPATH);
		UtilMethod.createLogFile(Config.Orar_LOG_FULLPATH, logMessage);
		LOGGER_.info("A log file was created at:" + Config.Orar_LOG_FULLPATH);
		return newFixedLengthResponse(String.format(TEMPLATE_DONE_ALL_, logMessage));
	}

	private synchronized Response killView(final IHTTPSession session) {
		LOGGER_.info("kill view");
		if (experimentProcess_ != null && experimentProcess_.isAlive()) {
			/*
			 * Note that: experimentProcess_.destroyForcibly() only terminate the shell I
			 * used to run actual commands.
			 */
			// experimentProcess_.destroyForcibly();

			UtilMethod.kill_sh_process_and_its_subprocesses();
			UtilMethod.kill_all_apps();
			experimentProcess_ = null;

		}
		final Response response = newFixedLengthResponse(Status.REDIRECT, NanoHTTPD.MIME_HTML, "");
		response.addHeader("Location", "/");
		response.addHeader("Cache-Control", "no-cache");
		return response;
	}

	private synchronized Response killAllView(final IHTTPSession session) {
		LOGGER_.info("kill view");
		if (experimentProcess_ != null && experimentProcess_.isAlive()) {
			experimentProcess_.destroyForcibly();
			experimentProcess_ = null;
		}
		final Response response = newFixedLengthResponse(Status.REDIRECT, NanoHTTPD.MIME_HTML, "");
		response.addHeader("Location", "/runall/");
		response.addHeader("Cache-Control", "no-cache");
		return response;
	}

	private synchronized Response logSourceView(final IHTTPSession session) {
		LOGGER_.info("log source view");

		try {

			final StringBuilder message = new StringBuilder();

			if (experimentProcess_ != null) {

				final boolean isDead = !experimentProcess_.isAlive();
				updateExperimentLog();

				// encode to text/event-stream
				final String[] lines = experimentLogToString().split("\n", -1);
				if (isDead) {
					experimentProcess_ = null;
					message.append("id: last_log\n");
				} else {
					message.append("id: log\n");
				}
				for (final String line : lines) {
					message.append("data: ").append(line).append("\n");
				}
				message.append("\n");

			}

			final Response response = newFixedLengthResponse(message.toString());
			response.addHeader("Content-Type", "text/event-stream");
			response.addHeader("Cache-Control", "no-cache");
			return response;

		} catch (final IOException e) {
			LOGGER_.error("Cannot read experiment log!", e);
			final Response response = newFixedLengthResponse(Status.INTERNAL_ERROR, NanoHTTPD.MIME_HTML,
					"Cannot read experiment log!\n" + e.getMessage());
			response.addHeader("Content-Type", "text/event-stream");
			response.addHeader("Cache-Control", "no-cache");
			return response;
		}

	}

	private void updateExperimentLog() throws IOException {
		final InputStream in = experimentProcess_.getInputStream();
		final int available = in.available();
		LOGGER_.debug("available: {}", available);
		final byte[] buffer = new byte[available];
		final int read = in.read(buffer);
		LOGGER_.debug("read: {}", read);
		if (read >= 0) {
			// something was actually read
			final String s = new String(buffer, 0, read);
			LOGGER_.debug("string: {}", s);

			final String[] lines = s.split("\n", -1);
			int i = 0;
			updateExperilentLogLastLine(lines[i]);
			for (i = 1; i < lines.length; i++) {
				experimentLog_.append(experimentLogLastLine_).append("\n");
				experimentLogLastLine_.setLength(0);
				updateExperilentLogLastLine(lines[i]);
			}
		}
	}

	private void updateExperilentLogLastLine(final String line) {
		final int lastIndex = line.lastIndexOf("\r");
		if (lastIndex < 0) {
			experimentLogLastLine_.append(line);
		} else {
			experimentLogLastLine_.setLength(0);
			experimentLogLastLine_.append(line.substring(lastIndex + 1));
		}
	}

	private String experimentLogToString() {
		return experimentLog_.toString() + experimentLogLastLine_.toString();
	}

	private Response newErrorResponse(final String message) {
		LOGGER_.error(message);
		return newFixedLengthResponse(Status.INTERNAL_ERROR, NanoHTTPD.MIME_HTML,
				String.format(TEMPLATE_INTERNAL_ERROR_, message, ""));
	}

	private Response newErrorResponse(final String message, final Throwable cause) {
		LOGGER_.error(message, cause);
		return newFixedLengthResponse(Status.INTERNAL_ERROR, NanoHTTPD.MIME_HTML,
				String.format(TEMPLATE_INTERNAL_ERROR_, message, cause.getMessage()));
	}

	private static final String PATTERN_TIMEPUT_ = "<t>";
	private static final String PATTERN_GLOBAL_TIMEPUT_ = "<g>";
	private static final String PATTERN_SOURCE_ = "<s>";
	private static final String PATTERN_ONTOLOGIES_ = "<o>";
	private static final String PATTERN_QUERY_GENERATION_OPTIONS_ = "<q>";

	// private static String substituteCommand(final String command, final String
	// pattern, final String value) {
	// final Pattern p = Pattern.compile(Pattern.quote(pattern));
	// final Matcher m = p.matcher(command);
	// if (m == null) {
	// return command;
	// } else {
	// return m.replaceAll(value);
	// }
	// }

	// private static String[] substituteCommand(final String[] command, final int
	// localTimeout, final int globalTimeout,
	// final String source, final String ontologies, final String
	// queryGenerationOptions) {
	// final String[] result = new String[command.length];
	// for (int i = 0; i < command.length; i++) {
	// result[i] = substituteCommand(substituteCommand(
	// substituteCommand(
	// substituteCommand(substituteCommand(command[i], PATTERN_TIMEPUT_, "" +
	// localTimeout),
	// PATTERN_GLOBAL_TIMEPUT_, "" + globalTimeout),
	// PATTERN_SOURCE_, source),
	// PATTERN_ONTOLOGIES_, ontologies), PATTERN_QUERY_GENERATION_OPTIONS_,
	// queryGenerationOptions);
	// }
	// return result;
	// }

}
