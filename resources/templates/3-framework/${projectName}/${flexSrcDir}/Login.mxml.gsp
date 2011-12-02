<?xml version="1.0" encoding="utf-8"?>

<s:Group
	xmlns:fx="http://ns.adobe.com/mxml/2009"
	xmlns:s="library://ns.adobe.com/flex/spark"
	xmlns:mx="library://ns.adobe.com/flex/mx"
    xmlns="*"
    width="100%" height="100%">
    <s:layout>
        <s:VerticalLayout horizontalAlign="center" verticalAlign="middle"/>
    </s:layout>

	<fx:Metadata>[Name]</fx:Metadata>
	
    <fx:Script>
    <![CDATA[<%
	if (javaFramework == "Spring3") {%>
        import org.granite.tide.spring.Identity;<%
	} else if (javaFramework == "Seam2") {%>
    	import org.granite.tide.seam.security.Identity;<%
	} else if (javaFramework == "EJB31") {%>
        import org.granite.tide.ejb.Identity;<%
	} else if (javaFramework == "CDI") {%>
        import org.granite.tide.cdi.Identity;<%
	}%>
        import org.granite.tide.events.TideResultEvent;
        import org.granite.tide.events.TideFaultEvent;
        
        [Bindable] [Inject]
        public var identity:Identity;
        
        [Bindable]
        public var message:String = null;
        
        private function tryLogin(username:String, password:String):void {<%
        if (javaFramework == "Seam2") {%>
        	identity.username = username;
        	identity.password = password;
        	identity.login(loginResult, loginFault);<%        
        } else {%>
            identity.login(username, password, loginResult, loginFault);<%      
        }%>
        }

        private function loginResult(event:TideResultEvent):void {
            message = null;
        }
        
        private function loginFault(event:TideFaultEvent):void {
        	message = event.fault.faultString;
        }
    ]]>
    </fx:Script>

    <s:Panel title="Log in (admin:admin or user:user)">
        <s:Form>
            <s:FormItem label="Username">
                <s:TextInput id="username"/>
            </s:FormItem>
            <s:FormItem label="Password">
                <s:TextInput id="password" displayAsPassword="true"
                    enter="tryLogin(username.text, password.text);"/>
            </s:FormItem>
            <s:FormItem>
                <s:Label text="{message}" textAlign="center"/>
                <s:Button label="Login" 
                    click="tryLogin(username.text, password.text);"/>
            </s:FormItem>
        </s:Form>
    </s:Panel>
</s:Group>
