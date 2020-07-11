# Firebase-Wrapper ![Tests](https://github.com/emrecankoc/firebase-wrapper/workflows/Tests/badge.svg)

Spring like repository wrapper for firestore

## Install

### Install with maven

First add github authentication settings to your ./m2/settings.xml file

Note: I think this authentication method below will be depreceted, and might not work in the future
```
<servers>
    <server>
        <id>github</id>
        <username>GITHUB_USERNAME</username>
        <password>GITHUB_TOKEN</password>
    </server>
</servers>
```

Then add repositroy tag to your project pom.xml
```
<repositories>
    <repository>
        <id>central</id>
        <url>https://repo1.maven.org/maven2</url>
        <releases><enabled>true</enabled></releases>
        <snapshots><enabled>true</enabled></snapshots>
    </repository>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/emrecankoc/firebase-wrapper</url>
    </repository>
</repositories>
```

Then add dependency to your pom.xml
```
<dependency>
    <groupId>io.github.emrecankoc</groupId>
    <artifactId>firebase-wrapper</artifactId>
    <version>0.1</version>
</dependency>
```

Yes, it's too much effort to use this package.

## Contribution

I've no idea what to do with this package. I'm just experiencing github's package registry and actions features. If you find good use of this package feel free to contribute.

