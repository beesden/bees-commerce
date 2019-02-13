/*! Template polyfill
* http://jsfiddle.net/brianblakely/h3EmY/
* ie8 / ie9
*/
(function(t){if("content"in t.createElement("template")){return false}var n=t.getElementsByTagName("template"),r=n.length,i,s,o,u;for(var a=0;a<r;++a){i=n[a];s=i.childNodes;o=s.length;u=t.createDocumentFragment();while(s[0]){u.appendChild(s[0])}i.content=u}})(document)
