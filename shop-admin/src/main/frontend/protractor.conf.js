/* jshint undef: true, unused: true */
/* globals browser:true element:true */
exports.config = {

    // Spec patterns are relative to the current working directly when
    // protractor is called.
    specs: ['tests/functional/product/**/*.spec.js'],

    allScriptsTimeout: 99999,

    // The address of a running selenium server.
    seleniumAddress: 'http://localhost:4444/wd/hub',

    // Capabilities to be passed to the webdriver instance.
    capabilities: {
        browserName: 'chrome',
        maxInstances: 10
    },

    baseUrl: 'http://localhost:3000/',

    framework: 'jasmine',

    // Login
    onPrepare: function () {

        protractor.ElementFinder.prototype.scrollTo = function () {
            browser.executeScript('var scrollHeight = arguments[0].offsetTop - 100; window.scrollTo( 0, scrollHeight );', this.getWebElement());
            browser.sleep(300);
            return this;
        };

        browser.get(this.baseUrl);
        return browser.sleep(600).then(function () {

            element(by.model('ctrl.loginForm.username')).sendKeys('testuser');
            element(by.model('ctrl.loginForm.password')).sendKeys('testtest');
            element(by.id('loginForm')).submit();

            return browser.waitForAngular();
        });
    },

    // Options to be passed to Jasmine-node.
    jasmineNodeOpts: {
        showColors: true,
        defaultTimeoutInterval: 45000,
        // isVerbose: true,
        includeStackTrace: true
    },

    verbose: false
};