# XJC plugin to change List property type to Set

XJC plugin to change **property type** to `java.util.Set` (from `java.util.List`)

## Sample

Input XSD:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" targetNamespace="http://fr/pinguet62">
    <xs:complexType name="SampleModel">
        <xs:sequence>
            <xs:element name="attr" type="xs:string" maxOccurs="unbounded" />
        </xs:sequence>
    </xs:complexType>
</xs:schema>
```

Generated JAXB class:
```java
// [...]

import java.util.HashSet;
import java.util.Set;

// [...]
public class SampleModel {

    @XmlElement
    protected Set<String> attr;
    
    public Set<String> getAttr() {
        if (attr == null) {
            attr = new HashSet<String>();
        }
    }
}
```

## Usage

Optional `binding.xjb`:
```xml
<jxb:bindings ... xmlns:p62="http://pinguet62.fr" jxb:extensionBindingPrefixes="p62">
	<jxb:bindings schemaLocation="./test.xsd">
		<jxb:bindings node="/xs:schema/xs:complexType[@name='SampleModel']//xs:element[@name='attr']">
			<p62:typeSet />
		</jxb:bindings>
	</jxb:bindings>
</jxb:bindings>
```

```xml
<project>
    <!-- ... -->
    <build>
        <plugins>
            <plugin>
                <groupId>com.evolvedbinary.maven.jvnet</groupId>
                <artifactId>jaxb30-maven-plugin</artifactId>
                <!-- ... -->
                <configuration>
                    <!-- ... -->
                    <args>
                        <arg>-Xlisttoset</arg>
						<!-- additional args -->
                    </args>
                    <plugins>
                        <plugin>
                            <groupId>fr.pinguet62.xjc</groupId>
                            <artifactId>xjc-listtoset-plugin</artifactId>
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

#### Process all

To process all properties:
```xml
<args>
	<arg>-Xlisttoset</arg>
		<arg>-Xlisttoset-processAll</arg>
</args>
```

*Default:* disabled.

## TODO

* Extension to any type
