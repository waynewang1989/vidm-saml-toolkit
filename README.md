
# vidm-saml-toolkit

## Overview

vIDM SAML Toolkit is a simple Java SDK (software development kit) for web developers who want to implement "Sign In with VMware Identity Manager" for their web applications. 

It is not a small task for web developers to encode SAML request and decode SAML response without using any SAML library.

This toolkit can save developers' effort by providing some easy to use functions. With this toolkit, a web developer can implement SSO function with vIDM, even if he has no knowledge about SAML 2.0. This toolkit is platform independent, since it is written in Java. 

This Toolkit supports the following 4 functions:      
1, Initialize SSO service with vIDM URL      
2, Create an authentication request to vIDM      
3, Process an authentication result from vIDM      
4, Sign Out

## Try it out

### Prerequisites

* A valid vIDM administrator account, so you can add a web application from vIDM adminstration console
* JDK 1.6 and Maven are required to build this project. The suggested development IDE is Eclipse + Maven plugin. You can choose any other IDE that you like.
* Windows 7 platform or above

### Build

Option 1: Build in Eclipse

1. Import these 3 projects as existing Maven project into your workspace: vidmsaml, SamlSample, and SamlToolkit

2. Run -> Run Configurations -> Maven Build, select "vidmsaml" as base directory, use "clean package" as goals. 

Option 2: Build with Maven command line tool

Execute "mvn clean package" command in "vidmsaml" folder

Once your build is successful, you should get "SamlToolkit-jar-with-dependencies.jar" and "SamlToolkit.jar" in "vidm-saml-toolkit\SamlToolkit\target". 

You also get a Sample web server which guide you to integrate with vIDM server easily in "vidm-saml-toolkit\SamlSample\target".

### Run

The Sample web server can be started with one of the following options:

Option 1: Use the startup script for Windows OS 

Go to "vidm-saml-toolkit\SamlSample\target" folder, double click "start.bat"

Option 2: Debug in Eclipse

Select "MyServer.java" in SamlSample project, Run As -> Java Application.

### Access

Access http://localhost:8080/SamlSample with any Internet browser after your Sample web server is started.

## Documentation

You may need the following Java Classes in "SamlToolkit.jar" to integrate your web application with vIDM:

    com.vmware.samltoolkit.SSOService

    com.vmware.samltoolkit.SAMLToolkitConf

    com.vmware.samltoolkit.SamlSsoResponse

Step 1. New a SSOService object when your web server is started

    SAMLToolkitConf conf = new SAMLToolkitConf();
   
    conf.setIdpURL("tenantxxx.vmwareidentity.com");
   
    conf.setConsumerURL("http://localhost:8080/SamlSample/consume");
   
    conf.setByPassSSLCertValidation(true);
   
    SSOService service = new SSOService(conf);
  
Step 2. Redirect the login request to vIDM. 
   
    String redirectURL = service.getSSOURLRedirect("");

    response.sendRedirect(redirectURL);
   
Step 3. Implement your own SAML consumer service like this (can be a Servlet, JSP, or any other):
   
	String samlresponse = ((HttpServletRequest)req).getParameter("SAMLResponse");	
		
	SAMLSsoResponse ssoResponse = service.decodeSSOResponse(samlresponse);
		
	if((ssoResponse != null) && (ssoResponse.isValid()) && (ssoResponse.ssoSucceed())) {
			   
		String username = ssoResponse.getNameId();
			   
		//TODO: SSO is successful, continue your service with this username
	}else{
		
		//TODO: SSO is failed, show an error page or remind the user to login again
		
	}


## Contributing

The vidm-saml-toolkit project team welcomes contributions from the community. If you wish to contribute code and you have not
signed our contributor license agreement (CLA), our bot will update the issue when you open a Pull Request. For any
questions about the CLA process, please refer to our [FAQ](https://cla.vmware.com/faq). For more detailed information,
refer to [CONTRIBUTING.md](CONTRIBUTING.md).

## License

This product is licensed to you under the BSD-2 license (the "License").  You may not use this product except in compliance with the BSD-2 License.

This product may include a number of subcomponents with separate copyright notices and license terms. Your use of these subcomponents is subject to the terms and conditions of the subcomponent's license, as noted in the LICENSE file.

