/*==============================================================================*/
/* Casper generated Thu Nov 03 2016 11:28:21 GMT+0200 (EET) */
/*==============================================================================*/

var x = require('casper').selectXPath;
casper.options.viewportSize = {width: 608, height: 1259};
casper.on('page.error', function(msg, trace) {
   this.echo('Error: ' + msg, 'ERROR');
   for(var i=0; i<trace.length; i++) {
       var step = trace[i];
       this.echo('   ' + step.file + ' (line ' + step.line + ')', 'ERROR');
   }
   casper.exit(1);
});


//Capture all fails
casper.test.on("fail", function () {
    casper.exit(1);
});

casper.test.begin('Resurrectio test', function(test) {
   casper.start('http://localhost:8080/');
   casper.waitForSelector("form input[name='username']",
       function success() {
           test.assertExists("form input[name='username']");
           this.click("form input[name='username']");
       },
       function fail() {
           test.assertExists("form input[name='username']");
   });
   casper.waitForSelector("input[name='username']",
       function success() {
           this.sendKeys("input[name='username']", "alpartralpar");
       },
       function fail() {
           test.assertExists("input[name='username']");
   });
   casper.waitForSelector("body > div:nth-child(1) > div",
       function success() {
           test.assertExists("body > div:nth-child(1) > div");
           this.click("body > div:nth-child(1) > div");
       },
       function fail() {
           test.assertExists("body > div:nth-child(1) > div");
   });
   /* submit form */
   casper.waitForSelector(".fa.fa-bar-chart",
       function success() {
           test.assertExists(".fa.fa-bar-chart");
           this.click(".fa.fa-bar-chart");
       },
       function fail() {
           test.assertExists(".fa.fa-bar-chart");
   });
   casper.waitForSelector(".fa.fa-bar-chart",
       function success() {
           test.assertExists(".fa.fa-bar-chart");
           this.click(".fa.fa-bar-chart");
       },
       function fail() {
           test.assertExists(".fa.fa-bar-chart");
   });
   casper.waitForSelector(".fa.fa-bar-chart",
       function success() {
           test.assertExists(".fa.fa-bar-chart");
           this.click(".fa.fa-bar-chart");
       },
       function fail() {
           test.assertExists(".fa.fa-bar-chart");
   });
   casper.waitForSelector(".fa.fa-plus",
       function success() {
           test.assertExists(".fa.fa-plus");
           this.click(".fa.fa-plus");
       },
       function fail() {
           test.assertExists(".fa.fa-plus");
   });
   casper.waitForSelector("form input[name='distanceMeters']",
       function success() {
           test.assertExists("form input[name='distanceMeters']");
           this.click("form input[name='distanceMeters']");
       },
       function fail() {
           test.assertExists("form input[name='distanceMeters']");
   });
   casper.waitForSelector("input[name='distanceMeters']",
       function success() {
           this.sendKeys("input[name='distanceMeters']", "3000");
       },
       function fail() {
           test.assertExists("input[name='distanceMeters']");
   });
   casper.waitForSelector("input[name='timeSeconds']",
       function success() {
           this.sendKeys("input[name='timeSeconds']", "60");
       },
       function fail() {
           test.assertExists("input[name='timeSeconds']");
   });

   casper.run(function() {test.done();});

});
