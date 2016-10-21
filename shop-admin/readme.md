# Commerce Shopfront

## Standalone Server

### Prerequisites

* [Maven](https://maven.apache.org/)
* [Java 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

### Install and run
N.B. ports and profiles can be changed as appropriate.
```
-- Run application (with hot swapping resources)
mvn spring-boot:run -Drun.arguments="--server.port=3000,--spring.profiles.active=local"

-- Build only
mvn clean install

-- Run only (no hot swapping)
java -Dspring.profiles.active=local -Dserver.port=3000 -jar target/cms-ui.
```

### Notable features

- Web controller passes all non-resource requests to common HTML
- Uses freemarker templating to generate HTML for web view and errors
- Supports multiple environment config

## Frontend code

Whilst the CMS can be run completely through the maven build, the frontend has it's own build system that can be run individually for development. 

### Dependencies
Before managing gulp tasks, the following dependencies need to be installed:

- [NodeJS](https://nodejs.org/en/)
- Gulp `npm install --global gulp-cli`

Dependencies need to be updated before development.
```
npm install
bower install
```

### Gulp tasks

While there are many gulp subtasks available, the key ones to use are documented below:

**`build` (default task)**

- Completely build all the project's scripts, styles, images and fonts

**`watch`**

- Watch for any changes in scripts, styles, and font source files and validates / rebuilds on change

**`scripts`**

- Concatenates JS files into `scripts.js` and templates to `templates.js` with sourcemaps for debugging
- Combines scripts / templates into production app.min.js
- Combines lib files into vendor.js

**`style`**

- Compiles source style files
- Copies style assets to deploy folder

**`font`**

- Builds CMS icon font
- Copies icon font and vendor fonts to deploy folder

**`images`**

- Copies images to deploy folder

## E2E Testing

Testing is run manually from the `src/main/frontend` folder.

### Dependencies

The following dependencies need to be installed / updated:
```
npm install -g protractor
webdriver-manager update
```

### Running tests
```
webdriver-manager start
protractor protractor.conf.js
```