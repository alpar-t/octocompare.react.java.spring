var x = require('casper').selectXPath;
casper.options.viewportSize = {width: 1024, height: 720};
casper.on('page.error', function(msg, trace) {
   this.echo('Error: ' + msg, 'ERROR');
   for(var i=0; i<trace.length; i++) {
       var step = trace[i];
       this.echo('   ' + step.file + ' (line ' + step.line + ')', 'ERROR');
   }
   this.capture('build/error.png')
   this.exit(1);
});
casper.on("resource.error", function(resourceError){
    this.echo('Unable to load resource (#' + resourceError.id + 'URL:' + resourceError.url + ')', 'ERROR');
    this.echo('Error code: ' + resourceError.errorCode + '. Description: ' + resourceError.errorString, 'ERROR');
   this.exit(1);
});

casper.options.waitTimeout = 10000;

//Capture all fails
casper.test.on("fail", function () {
    casper.capture('build/fail.png')
    casper.exit(1);
});

casper.test.begin('Login test', function(test) {
   casper.start(); 
   casper.setHttpAuth('admin', 'password');
   casper.thenOpen('http://127.0.0.1:8080/');
   
   casper.waitForSelector("form input[name='username']",
       function success() {
           casper.capture('build/loginForm.png');
           test.assertExists("form input[name='username']");
           casper.fill("form", {
               "username": "admin",
               "password": "password",
           }, true)
       },
       function fail() {
           this.capture("build/fail_input_form.png")
           test.assertExists("form input[name='username']");
   });

   casper.waitWhileVisible("form input[name='username']");
    
   casper.waitUntilVisible(".fa.fa-bar-chart",
       function success() {
           casper.capture('build/after_login.png');
           test.assertVisible(".fa.fa-bar-chart");
           this.click(".fa.fa-bar-chart");
       },
       function fail() {
           test.assertVisible(".fa.fa-bar-chart");
   });

   casper.run(function() {test.done();});
});
