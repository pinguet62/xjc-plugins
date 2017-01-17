# JAXB XJC Plugin for Swagger 2

[![Dependency Status](https://www.versioneye.com/user/projects/587d382b20bf410029a09c93/badge.svg?style=flat)](https://www.versioneye.com/user/projects/587d382b20bf410029a09c93)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/51fe4a0f7a424ff889021a18d804fd63)](https://www.codacy.com/app/pinguet62/jaxb-xjc-swagger?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=pinguet62/jaxb-xjc-swagger&amp;utm_campaign=Badge_Grade)
[![Build Status](https://travis-ci.org/pinguet62/jaxb-xjc-swagger.svg?branch=master)](https://travis-ci.org/pinguet62/jaxb-xjc-swagger)
[![codecov.io](https://codecov.io/github/pinguet62/jaxb-xjc-swagger/coverage.svg?branch=master)](https://codecov.io/github/pinguet62/jaxb-xjc-swagger?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/pinguet62/jaxb-xjc-swagger/badge.svg?branch=master)](https://coveralls.io/github/pinguet62/jaxb-xjc-swagger?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/fr.pinguet62/jaxb-xjc-swagger/badge.svg)](https://maven-badges.herokuapp.com/maven-central/fr.pinguet62/jaxb-xjc-swagger)

Automatically adding annotations from Swagger to generated classes from an XSD.

## Support

* **Comments:** use XSD `<xs:annotation><xs:documentation>` value  
	`@ApiModel("Class comment")`  
	`@ApiModelProperty(value = "Attribute comment")`
* **Required constraint:** if `minOccurs="1"` attribute on XSD  
	`@ApiModelProperty(required = true)`
* **Data type:** full qualified name (fix naming collision)  
	`@ApiModelProperty(dataType = "java.util.List<java.lang.String>")` (example for `type="xs:string" maxOccurs="unbounded"`).

## Sample

XSD:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" targetNamespace="http://fr/pinguet62/jaxb/swagger/model">
    <xs:complexType name="SampleModel">
        <xs:annotation>
            <xs:documentation>Class comments...</xs:documentation>
        </xs:annotation>
        <xs:sequence>
            <xs:element name="attr" type="xs:string" minOccurs="1" maxOccurs="1">
                <xs:annotation>
                    <xs:documentation>Attribute comments...</xs:documentation>
                </xs:annotation>
            </xs:element>
        </xs:sequence>
    </xs:complexType>
</xs:schema>
```

Java: (generated by JAXB XJC)
```java
// [...]
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "SampleModel", description = "Class comments...")
public class SampleModel {

    @XmlElement(required = true)
    @ApiModelProperty(value = "Attribute comments...", required = true, dataType = "java.lang.String")
    protected BigDecimal attr;
	
// [...]
```

## Usage

Maven:
* Add **this plugin** to JAXB plugins
* Add `-Xswagger` **argument** to JAXB arguments


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
                            <groupId>fr.pinguet62</groupId>
                            <artifactId>jaxb-xjc-swagger</artifactId>
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

* JAXB `XmlAccessType` support: FIELD/PROPERTY/PUBLIC_MEMBER/NONE
* Maven configuration:
	* *documentation formatter processor*
	* *naming strategy*: full-qualified VS simple name
* `@ApiModelProperty(dataType)` convertion
