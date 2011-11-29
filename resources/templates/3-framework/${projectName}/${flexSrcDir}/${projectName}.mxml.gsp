<?xml version="1.0" encoding="utf-8"?>

<s:Application
	xmlns:fx="http://ns.adobe.com/mxml/2009"
	xmlns:s="library://ns.adobe.com/flex/spark"
	xmlns:mx="library://ns.adobe.com/flex/mx"
    xmlns="*"
    controlBarVisible="{identity.loggedIn}"<%
    if (javaFramework == "Spring3") {%>
    preinitialize="Spring.getInstance().initApplication()"<%
    } else if (javaFramework == "Seam2") {%>
    preinitialize="Seam.getInstance().initApplication()"<%
    } else if (javaFramework == "EJB31") {%>
    preinitialize="Ejb.getInstance().initApplication()"<%
    } else if (javaFramework == "CDI") {%>
    preinitialize="Cdi.getInstance().initApplication()"<%
    }%>
    creationComplete="init()">
   	
    <fx:Script>
        <![CDATA[
            <% if (javaFramework == "Spring3") {%>
            import org.granite.tide.spring.Spring;
            import org.granite.tide.spring.Identity;<%
            } else if (javaFramework == "Seam2") {%>
            import org.granite.tide.seam.Seam;
            import org.granite.tide.seam.security.Identity;<%
            } else if (javaFramework == "EJB31") {%>
            import org.granite.tide.ejb.Ejb;
            import org.granite.tide.ejb.Identity;<%
            } else if (javaFramework == "CDI") {%>
            import org.granite.tide.cdi.Cdi;
            import org.granite.tide.cdi.Identity;<%
            }%>
            import org.granite.tide.Tide;
            import org.granite.tide.service.DefaultServiceInitializer;
            import org.granite.tide.data.DataObserver;
            
            [Bindable] [Inject]
            public var identity:Identity;
            
            private function init():void {
                // Define service endpoint resolver
                Tide.getInstance().getContext().serviceInitializer = new DefaultServiceInitializer('/${projectName}');
                
                // Configuration for data push observer subscribed upon login/logout
                // Remove if not using DataObserver
                Tide.getInstance().addComponent("welcomeTopic", DataObserver);
                Tide.getInstance().addEventObserver("org.granite.tide.login", "welcomeTopic", "subscribe");
                Tide.getInstance().addEventObserver("org.granite.tide.logout", "welcomeTopic", "unsubscribe");
                
                // Check current authentication state
                identity.isLoggedIn();
            }
            
            [Observer("org.granite.tide.login")]
            public function login():void {
                removeElementAt(0);
                addElement(new Home());
            }
            
            [Observer("org.granite.tide.logout")]
            public function logout():void {
                removeElementAt(0);
                addElement(new Login());
            }
        ]]>
    </fx:Script>

    <s:controlBarContent>
        <s:Label text="GraniteDS Application testtidespring" fontSize="18" fontWeight="bold" width="100%"/>
        <s:Button label="Logout" click="identity.logout();"/>
    </s:controlBarContent>
 
    <Login id="loginView"/>

</s:Application>
