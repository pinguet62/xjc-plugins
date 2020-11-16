# XJC plugin for Javadoc

XJC plugin to add **Javadoc** to generated JAXB classes (from `<xs:documentation>` XSD tags).

## Sample

Input XSD:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" targetNamespace="http://fr/pinguet62">
	<xs:complexType name="SampleModel">
		<xs:annotation>
			<xs:documentation>Class comment...</xs:documentation>
		</xs:annotation>
		<xs:sequence>
			<xs:element name="attr" type="xs:string">
				<xs:annotation>
					<xs:documentation>
						This is the best
						comment of the world!
					</xs:documentation>
				</xs:annotation>
			</xs:element>
		</xs:sequence>
	</xs:complexType>
</xs:schema>
```

Generated JAXB classes:
```java
// [...]

/**
 * Class comment...
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SampleModel", propOrder = {
    "attr"
})
public class SampleModel {

    /**
     * This is the best
     * comment of the world!
     * 
     */
    @XmlElement(required = true)
    protected String attr;

    /**
     * This is the best
     * comment of the world!
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
     * This is the best
     * comment of the world!
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

### Plugin

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
						<!-- additional args -->
                    </args>
                    <plugins>
                        <plugin>
                            <groupId>fr.pinguet62.xjc</groupId>
                            <artifactId>xjc-javadoc-plugin</artifactId>
                            <version>...</version>
                        </plugin>
                    </plugins>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

### Options

#### Formatting

You can use *regex* replacement to customize generated javadoc:
```xml
<args>
	<arg>-Xjavadoc</arg>
		<arg>-Xjavadoc-formatting</arg>
			<arg>-Xjavadoc-formatting-replace=...</arg>
			<arg>-Xjavadoc-formatting-by=...</arg>
			<arg>-Xjavadoc-formatting-replace=...</arg>
			<arg>-Xjavadoc-formatting-by=...</arg>
			<!-- ... -->
</args>
```

*Default:*
* remove indentation;
* remove first+last empty lines.

#### Strategy

```xml
<args>
	<arg>-Xjavadoc</arg>
		<arg>-Xjavadoc-strategy=...</arg>
</args>
```
where `...` can be:
* `APPEND_BEGIN`;
* `REPLACE`.

*Default:* `REPLACE`.

## TODO

* *Target* (field, getter, setter, ...)
