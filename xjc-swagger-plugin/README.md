# XJC plugin for Swagger

XJC plugin to add Swagger annotation to generate JAXB classes.

## Support

* **Comments:** use XSD `<xs:annotation><xs:documentation>` value  
	* class: `@ApiModel(value = "Class comment...")`  
	* property: `@ApiModelProperty(value = "Attribute comment...")`
* **Required constraint:** if `minOccurs="1"` attribute on XSD  
	`@ApiModelProperty(required = true)`
* **Data type:** full qualified name (to fix naming collision od Swagger)  
	`@ApiModelProperty(dataType = "java.util.List<java.lang.String>")`

## Sample

Input XSD:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" targetNamespace="http://fr/pinguet62">
    <xs:complexType name="SampleModel">
        <xs:annotation>
            <xs:documentation>Class comments...</xs:documentation>
        </xs:annotation>
        <xs:sequence>
            <xs:element name="attr" type="xs:string" minOccurs="1" maxOccurs="unbounded">
                <xs:annotation>
                    <xs:documentation>Attribute comments...</xs:documentation>
                </xs:annotation>
            </xs:element>
        </xs:sequence>
    </xs:complexType>
</xs:schema>
```

Generated JAXB class:
```java
// [...]

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

// [...]
@ApiModel(value = "SampleModel", description = "Class comments...")
public class SampleModel {

    // [...]
    @ApiModelProperty(value = "Attribute comments...", required = true, dataType = "java.util.List<java.lang.String>")
    protected List<String> attr;
	
    // [...]
}
```

## Usage

```xml
<project>
    <!-- ... -->
    <build>
        <plugins>
            <plugin>
                <groupId>org.jvnet.jaxb2.maven2</groupId>
                <artifactId>maven-jaxb2-plugin</artifactId>
                <!-- ... -->
                <configuration>
                    <!-- ... -->
                    <args>
                        <arg>-Xswagger</arg>
                    </args>
                    <plugins>
                        <plugin>
                            <groupId>fr.pinguet62.xjc</groupId>
                            <artifactId>xjc-swagger-plugin</artifactId>
                            <version>...</version>
                        </plugin>
                    </plugins>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

## TODO

* JAXB `XmlAccessType` support: `FIELD`, `PROPERTY`, `PUBLIC_MEMBER` or `NONE`
* `@ApiModelProperty(dataType)` conversion
* Javadoc options: formatter & strategy
