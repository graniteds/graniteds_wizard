
Granite Data Services Wizard (http://www.graniteds.org)
--------------------------------------------------------------

* Provide:

  A generic project wizard system, with specific templates for GraniteDS.

* Requirements:

  Eclipse 3.4+, JDK 6+.

* Installation:

  Close Eclipse, remove any older version of the plugin, unzip the new
  org.granite.wizard_x.x.x.zip file into your <ECLIPSE_HOME>/plugins directory,
  then restart Eclipse.

* Building:

  The binary distribution (org.granite.wizard_x.x.x.zip) is actually an Eclipse
  project and may be imported into your workspace as is, provided that you
  rename the root directory to "graniteds_wizard" (instead of "org.granite...).
  
  You may then modify sources and build a release by running the build_zip.xml
  Ant file.
  