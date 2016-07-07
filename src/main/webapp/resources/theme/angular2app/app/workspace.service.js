'use strict';
(function (app) {
    /* global ng */
    var l = app.AppUtils.logger("workspace.service");

    app.WorkspaceService = ng.core
        .Class({
            constructor: [app.ProjectService, app.FileService, function WorkspaceService(projectService, fileService) {
                this.session = {
                    autorun: false,
                    returnType: "DOUBLE",
                    script: `# Define a variable.
x <- rnorm(10)

# calculate the mean of x
mean(x)`
                };

                this.returnTypes = [
                    "DOUBLE",
                    "SCALAR",
                    "JSON_STRING",
                    "VECTOR",
                    "FILE"
                ];

                this.availableFunctions = [
                   
                    {
                        func: "GlobalMinimum",
                        name: "Global minimum"
                    },
                    {
                        func: "GlobalMaximum",
                        name: "Global maximum"
                   },
                   {
                       func: "KolmogorovSmirnovTest",
                       name: "Kolmogorov-Smirnov Test"
                   }
                ];
                this.availableFunctions.push({
                    func: "UserScript",
                    name: "Your script"
                });

                this._projectService = projectService;
                this._fileService = fileService;
            }]
        });

})(window.app || (window.app = {}));
