# JAXB XJC Plugin for Javadoc

[![Dependency Status](https://www.versioneye.com/user/projects/587ab9985450ea0042210150/badge.svg?style=flat)](https://www.versioneye.com/user/projects/587ab9985450ea0042210150)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/711cd823176249e7acd92e0ce710fe9f)](https://www.codacy.com/app/pinguet62/jaxb-xjc-javadoc?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=pinguet62/jaxb-xjc-javadoc&amp;utm_campaign=Badge_Grade)
[![Build Status](https://travis-ci.org/pinguet62/jaxb-xjc-javadoc.svg?branch=master)](https://travis-ci.org/pinguet62/jaxb-xjc-javadoc)
[![codecov.io](https://codecov.io/github/pinguet62/jaxb-xjc-javadoc/coverage.svg?branch=master)](https://codecov.io/github/pinguet62/jaxb-xjc-javadoc?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/pinguet62/jaxb-xjc-javadoc/badge.svg?branch=master)](https://coveralls.io/github/pinguet62/jaxb-xjc-javadoc?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/fr.pinguet62/jaxb-xjc-javadoc/badge.svg)](https://maven-badges.herokuapp.com/maven-central/fr.pinguet62/jaxb-xjc-javadoc)

Automatically adding javadoc to generated classes from an XSD.

## Sample

XSD:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" targetNamespace="http://fr/pinguet62/jaxb/javadoc/model">
	<xs:complexType name="SampleClass">
		<xs:annotation>
			<xs:documentation>Class comment...</xs:documentation>
		</xs:annotation>
		<xs:sequence>
			<xs:element name="attr" type="xs:string">
				<xs:annotation>
					<xs:documentation>Comment of property...</xs:documentation>
				</xs:annotation>
			</xs:element>
		</xs:sequence>
	</xs:complexType>
</xs:schema>
```

Java: (generated by JAXB XJC)
```java
// [...]
/**
 * Class comment...
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SampleClass", propOrder = {
    "attr"
})
public class SampleClass {

    /**
     * Comment of property...
     * 
     */
    @XmlElement(required = true)
    protected String attr;

    /**
     * Comment of property...
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttr() {
        return attr;
    }

    /**
     * Comment of property...
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttr(String value) {
        this.attr = value;
    }

}
```

## Usage

Maven:
* Add **this plugin** to JAXB plugins
* Add `-Xjavadoc` **argument** to JAXB arguments


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
                        <arg>-Xjavadoc</arg>
                    </args>
                    <plugins>
                        <plugin>
                            <groupId>fr.pinguet62</groupId>
                            <artifactId>jaxb-xjc-javadoc</artifactId>
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

* Maven configuration:
	* *documentation formatter processor*
	* *naming strategy*: replace VS append
