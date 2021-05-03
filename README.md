# VertConfig (Json Config Api)

VertConfig strives to provide a clean and easy to use JSON config api.

## Info

**JavaDocs:** https://javadocs.vertcode.eu/vertconfigs/

**Discord:** VertCode#0001

## Download

**Last Stable Version:** `1.0.0`

**Maven**

```xml
<repository>
    <id>vertcode</id>
    <url>https://repo.wesleybreukers.nl/repository/vertcode/</url>
</repository>
```

```xml
<dependency>
    <groupId>eu.vertcode</groupId>
    <artifactId>vertconfig</artifactId>
    <version>VERSION</version>
</dependency>
```

**Gradle**

```gradle
dependencies {
    compile 'eu.vertcode:vertconfig:VERSION'
}

repositories {
    mavenCentral()
    maven {
      name 'vertcode'
      url 'https://repo.wesleybreukers.nl/repository/vertcode/'
    }
}
```

## Usage

**Example Class**

```java
import eu.vertcode.vertconfig.VertConfigs;
import eu.vertcode.vertconfig.object.VertConfig;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Example {

    public static void main(String[] args) throws IOException {
        VertConfig config = VertConfigs.getInstance().getConfig(new File("config.json"),
                Example.class.getClassLoader().getResourceAsStream("config.json"));

        String example1 = config.getString("example.1");
        List<String> examples = config.getStringList("examples");

        System.out.println("example1 = " + example1);
        System.out.println("examples = " + String.join(", ", examples));

        config.set("example.1", "Hello!");
        config.set("examples", Arrays.asList("Example3", "Example4"));
        config.save();

        example1 = config.getString("example.1");
        examples = config.getStringList("examples");

        System.out.println("example1 = " + example1);
        System.out.println("examples = " + String.join(", ", examples));
    }

}
```

**config.json in the resources folder**

```json
{
  "example": {
    "1": "Example1"
  },
  "examples": [
    "Example1",
    "Example2"
  ]
}
```

**Output**

```console
example1 = "Example1"
examples = Example1, Example2
example1 = "Hello!"
examples = Example3, Example4
```