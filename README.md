jasper-reports-scaler
=====================
Since there is no page size scaler in the JasperReports library, this in an open source effort to create one. It seems the JasperReports library's lack of a scaler utility is striking considering that we live in an age where web apps are international and paper sizes are variable. It seems unreansonable to have to maintain separate JRXML files for every desired paper size which furthermore, violates the DRY principle.

There is one main class, JasperReportsScalerUtils that handles the scaling. Currently the scaling is very basic, it only scales the width and X values on report elements. Everything will squeeze in or out proportionally. It has only been tested and used with fairly simple report elements. Not sure how elements such as tables & crosstabs will work at this point.

Hopefully together, we can make this a robust scaler that maybe one day can be included in the JasperReports library itself.
