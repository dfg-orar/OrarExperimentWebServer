package orar.experiment;

public class HTMLString {
	public static final String TEMPLATE_RESULT_VIEW_=""
			+ "<!DOCTYPE html>\n" + 
			"<body>\n" +
			"<style>\n" + 
			"\n" + 
			".axis .domain {\n" + 
			"  display: none;\n" + 
			"}\n" + 
			"\n" + 
			"</style>\n" + 
			"<svg width=\"900\" height=\"600\"></svg>\n" + 
			"<script src=\"https://d3js.org/d3.v4.min.js\"></script>\n" + 
			"<script>\n" + 
			"\n" + 
			"var svg = d3.select(\"svg\"),\n" + 
			"    margin = {top: 50, right: 50, bottom: 30, left: 100},\n" + 
			"    width = +svg.attr(\"width\") - margin.left - margin.right,\n" + 
			"    height = +svg.attr(\"height\") - margin.top - margin.bottom,\n" + 
			"    g = svg.append(\"g\").attr(\"transform\", \"translate(\" + margin.left + \",\" + margin.top + \")\");\n" + 
			"\n" + 
			"var x0 = d3.scaleBand()\n" + 
			"    .rangeRound([0, width])\n" + 
			"    .paddingInner(0.1);\n" + 
			"\n" + 
			"var x1 = d3.scaleBand()\n" + 
			"    .padding(0.05);\n" + 
			"\n" + 
			"var y = d3.scaleLinear()\n" + 
			"    .rangeRound([height, 0]);\n" + 
			"\n" + 
			"var z = d3.scaleOrdinal()\n" + 
			"    .range([\"#000000\", \"#DD0000\", \"#FFCE00\", \"#6b486b\", \"#a05d56\", \"#d0743c\", \"#ff8c00\"]);\n" + 
			"\n" + 
			"d3.csv(\"file:///Users/kien/dockerdirtest/chart/data2.csv\", function(d, i, columns) {\n" + 
			"  for (var i = 1, n = columns.length; i < n; ++i) d[columns[i]] = +d[columns[i]];\n" + 
			"  return d;\n" + 
			"}, function(error, data) {\n" + 
			"  if (error) throw error;\n" + 
			"\n" + 
			"  var keys = data.columns.slice(1);\n" + 
			"\n" + 
			"  x0.domain(data.map(function(d) { return d.State; }));\n" + 
			"  x1.domain(keys).rangeRound([0, x0.bandwidth()]);\n" + 
			"  y.domain([0, d3.max(data, function(d) { return d3.max(keys, function(key) { return d[key]; }); })]).nice();\n" + 
			"\n" + 
			"  g.append(\"g\")\n" + 
			"    .selectAll(\"g\")\n" + 
			"    .data(data)\n" + 
			"    .enter().append(\"g\")\n" + 
			"      .attr(\"transform\", function(d) { return \"translate(\" + x0(d.State) + \",0)\"; })\n" + 
			"    .selectAll(\"rect\")\n" + 
			"    .data(function(d) { return keys.map(function(key) { return {key: key, value: d[key]}; }); })\n" + 
			"    .enter().append(\"rect\")\n" + 
			"      .attr(\"x\", function(d) { return x1(d.key); })\n" + 
			"      .attr(\"y\", function(d) { return y(d.value); })\n" + 
			"      .attr(\"width\", x1.bandwidth())\n" + 
			"      .attr(\"height\", function(d) { return height - y(d.value); })\n" + 
			"      .attr(\"fill\", function(d) { return z(d.key); });\n" + 
			"\n" + 
			"  g.append(\"g\")\n" + 
			"      .attr(\"class\", \"axis\")\n" + 
			"      .attr(\"transform\", \"translate(0,\" + height + \")\")\n" + 
			"      .call(d3.axisBottom(x0));\n" + 
			"\n" + 
			"  g.append(\"g\")\n" + 
			"      .attr(\"class\", \"axis\")\n" + 
			"      .call(d3.axisLeft(y).ticks(20))\n" + 
			"    .append(\"text\")\n" + 
			"      .attr(\"x\",-50)\n" + 
			"      .attr(\"y\", y(y.ticks().pop()) - 20)\n" + 
			"      .attr(\"dy\", \"0.32em\")\n" + 
			"      .attr(\"fill\", \"#000\")\n" + 
			"      .attr(\"font-weight\", \"bold\")\n" + 
			"      .attr(\"text-anchor\", \"start\")\n" + 
			"      .text(\"Reasoning Time in Seconds\");\n" + 
			"\n" + 
			"  var legend = g.append(\"g\")\n" + 
			"      .attr(\"font-family\", \"sans-serif\")\n" + 
			"      .attr(\"font-size\", 15)\n" + 
			"      .attr(\"text-anchor\", \"end\")\n" + 
			"    .selectAll(\"g\")\n" + 
			"    .data(keys.slice())\n" + 
			"    .enter().append(\"g\")\n" + 
			"      .attr(\"transform\", function(d, i) { return \"translate(0,\" + i * 20 + \")\"; });\n" + 
			"\n" + 
			"  legend.append(\"rect\")\n" + 
			"      .attr(\"x\", width - 350)\n" + 
			"      .attr(\"width\", 69)\n" + 
			"      .attr(\"height\", 19)\n" + 
			"      .attr(\"fill\", z);\n" + 
			"     \n" + 
			"\n" + 
			"  legend.append(\"text\")\n" + 
			"      .attr(\"x\", width - 360)\n" + 
			"      .attr(\"y\", 9.5)\n" + 
			"      .attr(\"dy\", \"0.32em\")\n" + 
			"      .text(function(d) { return d; });\n" + 
			"});\n" + 
			"\n" + 
			"</script>"+
					 
			"\n" + 
			"<h1>My First Heading</h1>\n" + 
			"\n" + 
			"<p>My first paragraph.</p>\n" + 
			"\n" + 
			"</body>\n" ; 
		public static String TEST_BAR=""+"<!DOCTYPE html>\n" + 
				"<script src=\"https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.min.js\"></script>\n" + 
				"<canvas id=\"bar-chart-grouped\" ></canvas>\n" + 
				"<script>\n" + 
				"new Chart(document.getElementById(\"bar-chart-grouped\"), {\n" + 
				"    type: 'bar',\n" + 
				"    data: {\n" + 
				"      labels: [\"IMDb\", \"NPD\", \"L10\",\"L50\",\n" + 
				"\"L100\",\"L500\",\"ChemBL\",\"UniPort\", \"DBPedia+\",\n" + 
				"\"U10\",\"U50\",\"U100\",\"U500\"],\n" + 
				"      datasets: [\n" + 
				"        {\n" + 
				"          label: \"Orar\",\n" + 
				"          backgroundColor: \"#000000\",\n" + 
				"          data: [39,5,4,12,20,130,425,274,337,38,101,142,497]\n" + 
				"        }, {\n" + 
				"          label: \"Konclude\",\n" + 
				"          backgroundColor: \"#DD0000\",\n" + 
				"data: [172,5,5,31,67,351,1188,1263,145,25,144,315,2107]\n" + 
				"       },{\n" + 
				"          label: \"PagodA\",\n" + 
				"          backgroundColor: \"#FFCE00\",\n" + 
				"          data: [209,49,8,48,97,621,906,1028,167,25,138,298,1591]\n" + 
				"        }\n" + 
				"      ]\n" + 
				"    },\n" + 
				"    options: {\n" + 
				"      title: {\n" + 
				"        display: true,\n" + 
				"        text: 'Reasoning times in seconds'\n" + 
				"      }\n" + 
				"    }\n" + 
				"});\n" + 
				"</script>"
				+ "<html>\n" + 
				"<body>\n" + 
				"\n" + 
				"Go back <a href=/doneall/>here</a></p>\n"+ 
				"\n" + 
				"</body>\n" + 
				"</html>\n" ; 
		public static String TABLE=""+"<!DOCTYPE html>\n" + 
				"<html lang=\"en\">\n" + 
				"    <head>\n" + 
				"        <meta charset=\"utf-8\">\n" + 
				"        <style>\n" + 
				"            table {\n" + 
				"                border-collapse: collapse;\n" + 
				"                border: 2px black solid;\n" + 
				"                font: 12px sans-serif;\n" + 
				"            }\n" + 
				"\n" + 
				"            td {\n" + 
				"                border: 1px black solid;\n" + 
				"                padding: 5px;\n" + 
				"            }\n" + 
				"        </style>\n" + 
				"    </head>\n" + 
				"    <body>\n" + 
				"        <!-- <script src=\"http://d3js.org/d3.v3.min.js\"></script> -->\n" + 
				"        <script src=\"d3.min.js?v=3.2.8\"></script>\n" + 
				"\n" + 
				"        <script type=\"text/javascript\"charset=\"utf-8\">\n" + 
				"            d3.text(\"file:///Users/kien/dockerdirtest/chart/tabdata.csv\", function(data) {\n" + 
				"                var parsedCSV = d3.csv.parseRows(data);\n" + 
				"\n" + 
				"                var container = d3.select(\"body\")\n" + 
				"                    .append(\"table\")\n" + 
				"\n" + 
				"                    .selectAll(\"tr\")\n" + 
				"                        .data(parsedCSV).enter()\n" + 
				"                        .append(\"tr\")\n" + 
				"\n" + 
				"                    .selectAll(\"td\")\n" + 
				"                        .data(function(d) { return d; }).enter()\n" + 
				"                        .append(\"td\")\n" + 
				"                        .text(function(d) { return d; });\n" + 
				"            });\n" + 
				"        </script>\n" + 
				"    </body>\n" + 
				"</html>\n" ; 
public static String TABLE2=""+"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><link type=\"text/css\" rel=\"stylesheet\" href=\"file:///Users/kien/dockerdirtest/chart/resources/sheet.css\" >\n" + 
		"<style type=\"text/css\">.ritz .waffle a { color: inherit; }.ritz .waffle .s2{border-bottom:1px SOLID #000000;border-right:1px SOLID #000000;background-color:#ffffff;text-align:right;color:#000000;font-family:'CMR10',Arial;font-size:11pt;vertical-align:bottom;white-space:nowrap;direction:ltr;padding:2px 3px 2px 3px;}.ritz .waffle .s3{border-bottom:1px SOLID #000000;border-right:1px SOLID #000000;background-color:#ffffff;text-align:right;color:#000000;font-family:'CMSY10',Arial;font-size:11pt;vertical-align:bottom;white-space:nowrap;direction:ltr;padding:2px 3px 2px 3px;}.ritz .waffle .s4{border-bottom:1px SOLID #000000;border-right:1px SOLID #000000;background-color:#f2f2f2;text-align:right;color:#000000;font-family:'CMR10',Arial;font-size:11pt;vertical-align:bottom;white-space:nowrap;direction:ltr;padding:2px 3px 2px 3px;}.ritz .waffle .s6{border-bottom:1px SOLID #000000;border-right:1px SOLID #000000;background-color:#ffffff;text-align:right;color:#000000;font-family:'CMMI10',Arial;font-size:11pt;vertical-align:bottom;white-space:nowrap;direction:ltr;padding:2px 3px 2px 3px;}.ritz .waffle .s0{border-bottom:1px SOLID #000000;border-right:1px SOLID #000000;background-color:#ffffff;text-align:left;color:#000000;font-family:'CMR10',Arial;font-size:11pt;vertical-align:bottom;white-space:nowrap;direction:ltr;padding:2px 3px 2px 3px;}.ritz .waffle .s5{border-bottom:1px SOLID #000000;border-right:1px SOLID #000000;background-color:#f2f2f2;text-align:right;color:#000000;font-family:'CMSY10',Arial;font-size:11pt;vertical-align:bottom;white-space:nowrap;direction:ltr;padding:2px 3px 2px 3px;}.ritz .waffle .s7{border-bottom:1px SOLID #000000;border-right:1px SOLID #000000;background-color:#f2f2f2;text-align:right;color:#000000;font-family:'CMMI10',Arial;font-size:11pt;vertical-align:bottom;white-space:nowrap;direction:ltr;padding:2px 3px 2px 3px;}.ritz .waffle .s1{border-bottom:1px SOLID #000000;border-right:1px SOLID #000000;background-color:#f2f2f2;text-align:left;color:#000000;font-family:'CMR10',Arial;font-size:11pt;vertical-align:bottom;white-space:nowrap;direction:ltr;padding:2px 3px 2px 3px;}</style><div class=\"ritz grid-container\" dir=\"ltr\"><table class=\"waffle\" cellspacing=\"0\" cellpadding=\"0\"><thead><tr><th class=\"row-header freezebar-origin-ltr\"></th><th id=\"0C0\" style=\"width:100px\" class=\"column-headers-background\">A</th><th id=\"0C1\" style=\"width:100px\" class=\"column-headers-background\">B</th><th id=\"0C2\" style=\"width:100px\" class=\"column-headers-background\">C</th><th id=\"0C3\" style=\"width:100px\" class=\"column-headers-background\">D</th><th id=\"0C4\" style=\"width:100px\" class=\"column-headers-background\">E</th><th id=\"0C5\" style=\"width:100px\" class=\"column-headers-background\">F</th><th id=\"0C6\" style=\"width:100px\" class=\"column-headers-background\">G</th></tr></thead><tbody><tr style='height:20px;'><th id=\"0R0\" style=\"height: 20px;\" class=\"row-headers-background\"><div class=\"row-header-wrapper\" style=\"line-height: 20px;\">1</div></th><td class=\"s0\" dir=\"ltr\" colspan=\"7\"></td></tr><tr style='height:20px;'><th id=\"0R1\" style=\"height: 20px;\" class=\"row-headers-background\"><div class=\"row-header-wrapper\" style=\"line-height: 20px;\">2</div></th><td class=\"s0\" dir=\"ltr\" rowspan=\"2\">Ontology</td><td class=\"s0\" dir=\"ltr\" colspan=\"3\">First Abstraction</td><td class=\"s0\" dir=\"ltr\" colspan=\"3\">Last Abstraction</td></tr><tr style='height:20px;'><th id=\"0R2\" style=\"height: 20px;\" class=\"row-headers-background\"><div class=\"row-header-wrapper\" style=\"line-height: 20px;\">3</div></th><td class=\"s0\" dir=\"ltr\">#types</td><td class=\"s1\" dir=\"ltr\"># ast.</td><td class=\"s0\" dir=\"ltr\">% ast. </td><td class=\"s0\" dir=\"ltr\"># types</td><td class=\"s1\" dir=\"ltr\"># ast.</td><td class=\"s1\" dir=\"ltr\">% ast.</td></tr><tr style='height:20px;'><th id=\"0R3\" style=\"height: 20px;\" class=\"row-headers-background\"><div class=\"row-header-wrapper\" style=\"line-height: 20px;\">4</div></th><td class=\"s0\" dir=\"ltr\">IMDb 366</td><td class=\"s2\" dir=\"ltr\">366</td><td class=\"s2\" dir=\"ltr\">2 160</td><td class=\"s2\" dir=\"ltr\">0.008</td><td class=\"s2\" dir=\"ltr\">-</td><td class=\"s3\" dir=\"ltr\">−</td><td class=\"s3\" dir=\"ltr\">−</td></tr><tr style='height:20px;'><th id=\"0R4\" style=\"height: 20px;\" class=\"row-headers-background\"><div class=\"row-header-wrapper\" style=\"line-height: 20px;\">5</div></th><td class=\"s0\" dir=\"ltr\">L10</td><td class=\"s4\" dir=\"ltr\">29</td><td class=\"s4\" dir=\"ltr\">227</td><td class=\"s2\" dir=\"ltr\">0.027</td><td class=\"s2\" dir=\"ltr\">29</td><td class=\"s4\" dir=\"ltr\">321</td><td class=\"s4\" dir=\"ltr\">0.038</td></tr><tr style='height:20px;'><th id=\"0R5\" style=\"height: 20px;\" class=\"row-headers-background\"><div class=\"row-header-wrapper\" style=\"line-height: 20px;\">6</div></th><td class=\"s0\" dir=\"ltr\">L50</td><td class=\"s2\" dir=\"ltr\">27</td><td class=\"s2\" dir=\"ltr\">217</td><td class=\"s2\" dir=\"ltr\">0.005</td><td class=\"s2\" dir=\"ltr\">27</td><td class=\"s2\" dir=\"ltr\">309</td><td class=\"s2\" dir=\"ltr\">0.007</td></tr><tr style='height:20px;'><th id=\"0R6\" style=\"height: 20px;\" class=\"row-headers-background\"><div class=\"row-header-wrapper\" style=\"line-height: 20px;\">7</div></th><td class=\"s0\" dir=\"ltr\">L100</td><td class=\"s4\" dir=\"ltr\">27</td><td class=\"s4\" dir=\"ltr\">217</td><td class=\"s2\" dir=\"ltr\">0.002</td><td class=\"s2\" dir=\"ltr\">27</td><td class=\"s4\" dir=\"ltr\">309</td><td class=\"s4\" dir=\"ltr\">0.003</td></tr><tr style='height:20px;'><th id=\"0R7\" style=\"height: 20px;\" class=\"row-headers-background\"><div class=\"row-header-wrapper\" style=\"line-height: 20px;\">8</div></th><td class=\"s0\" dir=\"ltr\">L500</td><td class=\"s2\" dir=\"ltr\">27</td><td class=\"s2\" dir=\"ltr\">217</td><td class=\"s2\" dir=\"ltr\">0</td><td class=\"s2\" dir=\"ltr\">27</td><td class=\"s2\" dir=\"ltr\">309</td><td class=\"s2\" dir=\"ltr\">0.001</td></tr><tr style='height:20px;'><th id=\"0R8\" style=\"height: 20px;\" class=\"row-headers-background\"><div class=\"row-header-wrapper\" style=\"line-height: 20px;\">9</div></th><td class=\"s0\" dir=\"ltr\">NPD</td><td class=\"s4\" dir=\"ltr\">102</td><td class=\"s4\" dir=\"ltr\">21 263</td><td class=\"s2\" dir=\"ltr\">2.333</td><td class=\"s2\" dir=\"ltr\">-</td><td class=\"s5\" dir=\"ltr\">−</td><td class=\"s5\" dir=\"ltr\">−</td></tr><tr style='height:20px;'><th id=\"0R9\" style=\"height: 20px;\" class=\"row-headers-background\"><div class=\"row-header-wrapper\" style=\"line-height: 20px;\">10</div></th><td class=\"s0\" dir=\"ltr\">ChemBL</td><td class=\"s2\" dir=\"ltr\">343</td><td class=\"s2\" dir=\"ltr\">2 151</td><td class=\"s6\" dir=\"ltr\">&lt; 0.001 </td><td class=\"s6\" dir=\"ltr\">-</td><td class=\"s3\" dir=\"ltr\">−</td><td class=\"s3\" dir=\"ltr\">−</td></tr><tr style='height:20px;'><th id=\"0R10\" style=\"height: 20px;\" class=\"row-headers-background\"><div class=\"row-header-wrapper\" style=\"line-height: 20px;\">11</div></th><td class=\"s0\" dir=\"ltr\">UniPort</td><td class=\"s4\" dir=\"ltr\">663</td><td class=\"s4\" dir=\"ltr\">4 903</td><td class=\"s6\" dir=\"ltr\">&lt; 0.001</td><td class=\"s6\" dir=\"ltr\">663</td><td class=\"s4\" dir=\"ltr\">5551</td><td class=\"s7\" dir=\"ltr\">&lt; 0.001</td></tr><tr style='height:20px;'><th id=\"0R11\" style=\"height: 20px;\" class=\"row-headers-background\"><div class=\"row-header-wrapper\" style=\"line-height: 20px;\">12</div></th><td class=\"s0\" dir=\"ltr\">DBPedia+</td><td class=\"s2\" dir=\"ltr\">187911</td><td class=\"s2\" dir=\"ltr\">2 890 338</td><td class=\"s2\" dir=\"ltr\">12.816</td><td class=\"s2\" dir=\"ltr\">186441</td><td class=\"s2\" dir=\"ltr\">3 561 749</td><td class=\"s2\" dir=\"ltr\">15.487</td></tr><tr style='height:20px;'><th id=\"0R12\" style=\"height: 20px;\" class=\"row-headers-background\"><div class=\"row-header-wrapper\" style=\"line-height: 20px;\">13</div></th><td class=\"s0\" dir=\"ltr\">U10</td><td class=\"s4\" dir=\"ltr\">4477</td><td class=\"s4\" dir=\"ltr\">91 889</td><td class=\"s2\" dir=\"ltr\">4.769</td><td class=\"s2\" dir=\"ltr\">9204</td><td class=\"s4\" dir=\"ltr\">369 277</td><td class=\"s4\" dir=\"ltr\">19.164</td></tr><tr style='height:20px;'><th id=\"0R13\" style=\"height: 20px;\" class=\"row-headers-background\"><div class=\"row-header-wrapper\" style=\"line-height: 20px;\">14</div></th><td class=\"s0\" dir=\"ltr\">U50</td><td class=\"s2\" dir=\"ltr\">8205</td><td class=\"s2\" dir=\"ltr\">172 627</td><td class=\"s2\" dir=\"ltr\">1.77</td><td class=\"s2\" dir=\"ltr\">18674</td><td class=\"s2\" dir=\"ltr\">780 930</td><td class=\"s2\" dir=\"ltr\">8.008</td></tr><tr style='height:20px;'><th id=\"0R14\" style=\"height: 20px;\" class=\"row-headers-background\"><div class=\"row-header-wrapper\" style=\"line-height: 20px;\">15</div></th><td class=\"s0\" dir=\"ltr\">U100</td><td class=\"s4\" dir=\"ltr\">10220</td><td class=\"s4\" dir=\"ltr\">216 750</td><td class=\"s2\" dir=\"ltr\">1.107</td><td class=\"s2\" dir=\"ltr\">23362</td><td class=\"s4\" dir=\"ltr\">1 036 017</td><td class=\"s4\" dir=\"ltr\">5.293</td></tr><tr style='height:20px;'><th id=\"0R15\" style=\"height: 20px;\" class=\"row-headers-background\"><div class=\"row-header-wrapper\" style=\"line-height: 20px;\">16</div></th><td class=\"s0\" dir=\"ltr\">U500 </td><td class=\"s2\" dir=\"ltr\">15521</td><td class=\"s2\" dir=\"ltr\">335 809</td><td class=\"s2\" dir=\"ltr\">0.341</td><td class=\"s2\" dir=\"ltr\">41814</td><td class=\"s2\" dir=\"ltr\">1 836 102</td><td class=\"s2\" dir=\"ltr\">1.866</td></tr></tbody></table></div>\n" + 
		"<script type='text/javascript' nonce='NP2Ae9kinw5ti+lDIyKkZZJmX0E'>\n" + 
		"function posObj(sheet, id, row, col, x, y) {\n" + 
		"  var rtl = false;\n" + 
		"  var sheetElement = document.getElementById(sheet);\n" + 
		"  if (!sheetElement) {\n" + 
		"    sheetElement = document.getElementById(sheet + '-grid-container');\n" + 
		"  }\n" + 
		"  if (sheetElement) {\n" + 
		"    rtl = sheetElement.getAttribute('dir') == 'rtl';\n" + 
		"  }\n" + 
		"  var r = document.getElementById(sheet+'R'+row);\n" + 
		"  var c = document.getElementById(sheet+'C'+col);\n" + 
		"  if (r && c) {\n" + 
		"    var objElement = document.getElementById(id);\n" + 
		"    var s = objElement.style;\n" + 
		"    var t = y;\n" + 
		"    while (r && r != sheetElement) {\n" + 
		"      t += r.offsetTop;\n" + 
		"      r = r.offsetParent;\n" + 
		"    }\n" + 
		"    var offsetX = x;\n" + 
		"    while (c && c != sheetElement) {\n" + 
		"      offsetX += c.offsetLeft;\n" + 
		"      c = c.offsetParent;\n" + 
		"    }\n" + 
		"    if (rtl) {\n" + 
		"      offsetX -= objElement.offsetWidth;\n" + 
		"    }\n" + 
		"    s.left = offsetX + 'px';\n" + 
		"    s.top = t + 'px';\n" + 
		"    s.display = 'block';\n" + 
		"    s.border = '1px solid #000000';\n" + 
		"  }\n" + 
		"};\n" + 
		"function posObjs() {\n" + 
		"};\n" + 
		"posObjs();</script>\n" ; 
		
}
