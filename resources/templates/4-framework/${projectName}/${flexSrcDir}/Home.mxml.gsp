<?xml version="1.0" encoding="utf-8"?>

<s:VGroup
	xmlns:fx="http://ns.adobe.com/mxml/2009"
	xmlns:s="library://ns.adobe.com/flex/spark"
	xmlns:mx="library://ns.adobe.com/flex/mx"
    xmlns:v="org.granite.validation.*"
    xmlns="*"
    width="100%" height="100%">

	<fx:Metadata>[Name]</fx:Metadata>
   	
    <fx:Script>
        <![CDATA[
            import mx.collections.ArrayCollection;
            
            import org.granite.tide.events.TideResultEvent;
            import org.granite.tide.events.TideFaultEvent;
            
            import ${packageName}.entities.Welcome;
            import ${packageName}.services.WelcomeService;
            
            <% if (javaFramework == "EJB31" || javaFramework == "Seam2") {%>
            [In]<%
            } else {%>
            [Inject]<%
            }%>
            public var welcomeService:WelcomeService;
            
            [Bindable]
            public var allWelcomes:ArrayCollection;
            
            private function findAllResult(event:TideResultEvent):void {
                allWelcomes = ArrayCollection(event.result);
            }
            
            [Observer("org.granite.tide.data.persist.Welcome")]
            public function welcomeAdded(welcome:Welcome):void {
                allWelcomes.addItem(welcome);
            }
            
            private function hello(name:String):void {
                welcomeService.hello(iName.text, helloResult, helloFault)
            }
            
            private function helloResult(event:TideResultEvent):void {
                iName.text = "";
            }
            
            private function helloFault(event:TideFaultEvent):void {
                lMessage.text = event.fault.faultString;
            }
        ]]>
    </fx:Script>
    
    <s:VGroup paddingLeft="10" paddingRight="10" paddingTop="10" paddingBottom="10" width="800">
        <s:HGroup id="fHello">
            <s:TextInput id="iName"/>
            <s:Button id="bHello" label="Welcome!" click="hello(iName.text)"/>
        </s:HGroup>
    
        <s:Label id="lMessage"/>
        
        <s:List id="dgList" dataProvider="{allWelcomes}" labelField="message" width="100%" height="200"
            creationComplete="welcomeService.findAll(findAllResult)"/>
    </s:VGroup>

</s:VGroup>
