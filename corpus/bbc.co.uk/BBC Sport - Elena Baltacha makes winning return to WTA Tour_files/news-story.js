(function(){bbc.fmtj.utils.createObject("bbc.fmtj.view.story");var a=function(b){var d={};var c="bbc.fmtj.view.story";var e="0.0.33";if(b===undefined){d=bbc.fmtj.utils.createObject(c);}else{d=b;}d.namespace=c;d.version=e;d["av-stories-best"]=function(f){if(f===undefined){return false;}gloader.load(["glow","1","glow.dom","glow.events","glow.widgets.Carousel"],{async:true,onLoad:function(g){g.ready(function(){var j=g.dom.get("#"+f+" .av-best-carousel");if(j.get("li").length<3){return false;}var i=new g.widgets.Carousel(j,{loop:false,size:2,step:2,className:"gvl3-carousel",theme:"light",pageNav:true});h();g.events.addListener(i,"afterScroll",h);function h(k){i.items.addClass("not-visible");i.visibleItems().removeClass("not-visible");}});}});};bbc.fmtj.components.registerNamespace(d);return d;};if(typeof gloader!="undefined"){gloader.provide({name:"bbc.fmtj.view.story",library:["bbc.fmtj.view","0.0.33"],depends:[["bbc.fmtj.view","0.0.33","bbc.fmtj.view.datetime","bbc.fmtj.view.hypertabs","bbc.fmtj.view.pageBookmarkLinks"]],builder:a});}else{a(bbc.fmtj.view);}})();(function(){bbc.fmtj.utils.createObject("bbc.fmtj.view.hypertabs");var a=function(b){var d={};var c="bbc.fmtj.view.hypertabs";var e="0.0.33";if(b===undefined){d=bbc.fmtj.utils.createObject(c);}else{d=b;}d.namespace=c;d.version=e;d.hypertabs=function(g){function f(i){var h=parseInt(i,10);if(isNaN(h)){return 0;}else{return h;}}gloader.load(["glow","1","glow.dom","glow.events","glow.anim"],{onLoad:function(h){h.ready(function(){var t=0,u,o=[],j=0,y=h.dom.get("#"+g),i=y.height(),n=y.width(),s,p,z=y.get("ul"),v=z.get("li"),k,l,w,x;s=z.wrap('<div class="hypertab-container"></div>');p=s.width()+f(z.css("border-left-width"))+f(z.css("border-right-width"));s.width(y.width());u=v.length;v.each(function(A){var B=h.dom.get(this);var C=B.width()+(f(B.css("margin-left"))+f(B.css("border-left-width")))+(f(B.css("margin-right"))+f(B.css("border-right-width")));if(A==0){C+=f(z.css("border-left-width"));}o.push(j);j+=C;});z.width(j);if(p<j){k=h.dom.create('<a href="#previous-hypertab" class="hypertab-nav hypertab-prev">Previous</a>');l=h.dom.create('<a href="#next-hypertab" class="hypertab-nav hypertab-next">Next</a>');h.events.addListener(k,"click",r);h.events.addListener(l,"click",q);z.before(k);z.after(l);w=k.width();x=l.width();z.css("left",w);k.css("position","absolute").css("left","0");l.css("position","absolute").css("left",p-x);m();y.get(".hypertab-container").height(i).width(n);}function r(A){if(t<1){return false;}t--;m();h.anim.css(z,0.3,{"margin-left":{to:o[t]*-1}},{destroyOnComplete:true}).start();return false;}function q(A){if((t>u-2)||(j-o[t]-(t==u-1?f(listItem.css("margin-right"),10)+f(listItem.css("border-right-width")):0)<p)){return false;}t++;m();h.anim.css(z,0.3,{"margin-left":{to:o[t]*-1}},{destroyOnComplete:true}).start();return false;}function m(){if(t<1){k.addClass("hypertab-prev-disabled");}else{k.removeClass("hypertab-prev-disabled");}if((t>u-2)||(j-o[t]-(t==u-1?f(listItem.css("margin-right"))+f(listItem.css("border-right-width")):0)<p)){l.addClass("hypertab-next-disabled");}else{l.removeClass("hypertab-next-disabled");}}});}});};bbc.fmtj.components.registerNamespace(d);return d;};if(typeof gloader!="undefined"){gloader.provide({name:"bbc.fmtj.view.hypertabs",library:["bbc.fmtj.view","0.0.33"],builder:a});}else{a(bbc.fmtj.view);}})();(function(){bbc.fmtj.utils.createObject("bbc.fmtj.view.pageBookmarkLinks");var a=function(h){var q={};var x="bbc.fmtj.view.pageBookmarkLinks";var p="0.0.33";if(h===undefined){q=bbc.fmtj.utils.createObject(x);}else{q=h;}q.namespace=x;q.version=p;var l,u,y,t;function m(){gloader.load(["glow","1","glow.dom","glow.events","glow.net"],{onLoad:function(z){l=z;u=z.dom;y=z.events;t=z.net;}});}var v=false;var f={};var s=[];var r=0;function w(){var z=u.get(".emp");z.each(function(A){var B=u.get(this);B.children().each(function(C){var D=u.get(this);if(D.is("embed")||D.is("object")){B.addClass("page-bookmark-link-aware");if(B.css("position")=="static"){B.css("position","relative");}}});});}function i(){l.ready(function(){var z=u.get(".page-bookmark-link-aware");for(var A=0;A<z.length;A++){g(u.get(z[A]));}});}function g(A){var C;var B;var G=A.get("embed");if(G.length>0){C=A.get("embed").attr("flashvars").split("&");}else{G=A.get("object");var D=G.get("param").filter(function(z){return(this.name.toLowerCase()=="flashvars");});if(D.length==0){return;}C=D.attr("value").split("&");}if(G.length>0){var H={};for(var E=0;E<C.length;E++){var I=C[E].split("=");H[I[0].toLowerCase()]=I[1];}if(H.holdingimage!==undefined){B=unescape(H.holdingimage);}var F=false;for(infoPanel in f){if(f[infoPanel]!==undefined&&f[infoPanel].isShown){F=true;}}if(F||v){if(B!==undefined&&A.get(".emp-overlay-panel-holding-image").length===0){A.prepend('<img class="emp-overlay-panel-holding-image" width="100%" src="'+B+'" style="position:absolute;top:0;left:0;" />');}if(!v){G.css("visibility","hidden");}}else{A.get(".emp-overlay-panel-holding-image").remove();G.css("visibility","");}}}q["page-bookmark-links"]=function(B,z){if(B===undefined){return false;}var A=e(z);m();l.ready(w);if(A.useForgeShareTools=="true"&&window.disableForgeShareTools!==true){v=true;j(B,A);n();}else{o(B,A);}};function e(z){var A={};if(z===undefined){z={};}A=z;(z.useForgeShareTools===undefined?A.useForgeShareTools=false:A.useForgeShareTools=z.useForgeShareTools);(z.position===undefined?A.position="top":A.position=z.position);A.site=z.site;A.headline=z.headline;A.storyId=z.storyId;A.sectionId=z.sectionId;A.url=z.url;A.edition=z.edition;return A;}function c(A){var z=u.get(A.attachedTo).get("a").attr("href");bbc.fmtj.common.window.createPopup({url:z,resizable:1,scrollbars:1,width:370,height:445});var B=this;bbc.fmtj.common.liveStats.createWebBug({referrer:document.location,pageType:"email",sectionId:B.sectionId,edition:B.edition,url:B.url,queryString:"-"});return false;}function k(z){var A=this;bbc.fmtj.common.liveStats.createWebBug({referrer:document.location,pageType:"print",sectionId:A.sectionId,edition:A.edition,url:A.url,queryString:"-"});}function d(z){bbc.fmtj.common.liveStats.createWebBug({referrer:document.location,pageType:"soc_"+z.service,sectionId:z.sectionId,edition:z.edition,url:z.url,queryString:"-"});return false;}var b=false;function n(){if(b){return;}l.ready(function(){t.loadScript("http://static.bbc.co.uk/modules/sharetools/v1/script/sharetools.js",{useCache:true});u.get("head").append('<style type="text/css">img.emp-overlay-panel-holding-image {display:none;} .bbc-st-flash-hidden img.emp-overlay-panel-holding-image {display:block;}</style>');});b=true;}function j(G,F){var A=u.get("#"+G);s.push(G);A.get(".delicious, .digg, .reddit, .stumbleupon").remove();var z="/modules/sharetools/share?url="+encodeURIComponent(F.url)+"&title="+encodeURIComponent(F.headline)+"&appId="+bbc.fmtj.page.siteToServe+"&facebook-message=";if(F.position==="top"){A.append('<div id="'+F.position+'-share-toolbar" class="bbc-st bbc-st-slim bbc-st-colour bbc-st-dark bbc-st-force-flash-hide bbc-st-disable-facebook-panel"><p class="bbc-st-basic"><a href="'+z+'">Share this page</a></p></div>');}else{if(F.position==="inline"){var E='<div id="'+F.position+"-"+(++r)+'-share-toolbar" class="bbc-st bbc-st-slim bbc-st-colour bbc-st-dark bbc-st-force-flash-hide bbc-st-disable-facebook-panel"><p class="bbc-st-basic"><a href="'+z+'">Share this page</a></p></div>';var C=A.get("h3");if(C.length>0){C.after(E);}else{A.prepend(E);}}else{A.get("h3").after('<div id="'+F.position+'-share-toolbar" class="bbc-st bbc-st-slim bbc-st-colour bbc-st-dark bbc-st-force-flash-hide bbc-st-disable-facebook-panel"><p class="bbc-st-basic"><a href="'+z+'">Share this page</a></p></div>');}}var D=A.get(".email");var B=A.get(".print");y.addListener(D,"click",c,F);y.addListener(B,"click",k,F);if(window.sharetools===undefined){window.sharetools={onReady:function(){for(var H=0;H<s.length;H++){u.get("#"+s[H]).get(".facebook, .twitter").remove();}sharetools.sharePanel.onShow=function(){i();if(sharetools.toolbars["top-share-toolbar"]!==undefined&&sharetools.toolbars["top-share-toolbar"].isPanelShowing){var I="";if(window.BBC!==undefined){if(BBC.adverts!==undefined&&F.position=="top"){I=BBC.adverts.getAdvertTag("module_page-bookmark-links-top",{is_module:"true",module:"page-bookmark-links-top"},"iframe","module_page-bookmark-links");u.get(sharetools.sharePanel.getFooter()).html(I);}}}};sharetools.sharePanel.onAfterHide=function(){i();if(window.BBC!==undefined){u.get(sharetools.sharePanel.getFooter()).html("");}};sharetools.onShare=function(I){if(I.usingPanel){i();}F.service=I.network;d(F);};}};}}function o(C,D){function A(L){D.service=u.get(L.attachedTo).text().toLowerCase().replace(/[\W]*/g,"");d(D);var K=I(D.service,encodeURIComponent(D.url),encodeURIComponent(D.headline));bbc.fmtj.common.window.createPopup({url:K,resizable:1,scrollbars:1,width:750,height:370});return false;}function I(K,L,N){var M="";switch(K){case"delicious":M="http://del.icio.us/post?v=4&noui&jump=close&url="+L+"&title="+N;break;case"digg":M="http://digg.com/remote-submit?phase=2&url="+L+"&title="+N;break;case"facebook":M="http://www.facebook.com/sharer.php?u="+L+"&t="+N;break;case"reddit":M="http://reddit.com/submit?url="+L+"&title="+N;break;case"stumbleupon":M="http://www.stumbleupon.com/submit?url="+L+"&title="+N;break;case"twitter":M="http://twitter.com/home?status="+N+"+"+L;break;default:break;}return M;}function H(L){var K="";var M;if(window.BBC!==undefined){if(BBC.adverts!==undefined&&D.position=="top"){K=BBC.adverts.getAdvertTag("module_page-bookmark-links-top",{is_module:"true",module:"page-bookmark-links-top"},"iframe","module_page-bookmark-links");}}if(D.position=="top"){M="share-links-panel-top";}else{M="share-links-panel-bottom";}if(f[D.position]===undefined){gloader.load(["glow","1","glow.dom","glow.events","glow.widgets.InfoPanel"],{async:true,onLoad:function(N){f[D.position]=new N.widgets.InfoPanel(N.dom.create('<div id="socialBookMarks" class="share-form"><h2 class="hd">Share</h2><a class="share-help-link" href="/news/10623543">What are these?</a><ul class="networks"><li class="delicious"><a href="#delicious" title="Post this story to Delicious">Delicious</a></li><li class="digg"><a href="#digg" title="Post this story to Digg">Digg</a></li><li class="facebook"><a href="#facebook" title="Post this story to Facebook">Facebook</a></li><li class="reddit"><a href="#reddit" title="Post this story to reddit">Reddit</a></li><li class="stumbleupon"><a href="#stumbleupon" title="Post this story to StumbleUpon">StumbleUpon</a></li><li class="twitter"><a href="#twitter" title="Post this story to Twitter">Twitter</a></li></ul>'+K+"</div>"),{context:L.attachedTo,id:M,hideWindowedFlash:false});N.events.addListener(f[D.position],"afterShow",i);N.events.addListener(f[D.position],"afterHide",i);N.events.addListener(".share-form .networks li","click",A);f[D.position].show();}});}else{f[D.position].show();}return false;}var B=u.get("#"+C);B.get(".delicious, .digg, .facebook, .reddit, .stumbleupon").remove();var E=B.get(".twitter");var J=u.create('<li class="share"><a title="Share this story" href="#">Share</a></li>');E.after(J);var G=B.get(".email");var z=B.get(".print");y.addListener(E,"click",A);y.addListener(J,"click",H);y.addListener(G,"click",c,D);y.addListener(z,"click",k,D);var F=u.create('<li class="facebook-popup"><a href="#facebook" title="Post this story to Facebook">Facebook</a></li>');B.get("ul").prepend(F);y.addListener(F,"click",A);}bbc.fmtj.components.registerNamespace(q);return q;};if(typeof gloader!="undefined"){gloader.provide({name:"bbc.fmtj.view.pageBookmarkLinks",library:["bbc.fmtj.view","0.0.33"],builder:a});}else{a(bbc.fmtj.view);}})();(function(){bbc.fmtj.utils.createObject("bbc.fmtj.view.news");var a=function(b){var d={};var c="bbc.fmtj.view.news";var e="0.0.33";if(b===undefined){d=bbc.fmtj.utils.createObject(c);}else{d=b;}d.namespace=c;d.version=e;d["accordion-horizontal"]=function(g,f){bbc.fmtj.view.accordion.createAccordion(g,f);};d.avCarousel=function(f){if(f.container===undefined){return false;}gloader.load(["glow","1","glow.dom","glow.widgets.Carousel"],{async:true,onLoad:function(g){g.ready(function(){if(f.container===undefined){return false;}var h=new g.widgets.Carousel(f.container,{className:"most-pop-carousel",vertical:true,loop:true,step:4,theme:"light"});g.dom.get(h.element).get("ol").removeClass("js-offscreen");});}});};d["market-data-include"]=function(f){gloader.load(["glow","1","glow.dom","glow.events"],{onLoad:function(i){var g=i.dom.get("#"+f+" .mkt-footer a.mkt-ticker");if(g.length<1){return false;}g.prepend('<span class="gvl3-icon gvl3-icon-popout">Popout </span>');i.events.addListener(g,"click",h);function h(){var j=g.attr("href");bbc.fmtj.common.window.createPopup({url:j,width:450,height:557});return false;}}});};bbc.fmtj.components.registerNamespace(d);return d;};if(typeof gloader!="undefined"){gloader.provide({name:"bbc.fmtj.view.news",library:["bbc.fmtj.view","0.0.33"],depends:[["bbc.fmtj.view","0.0.33","bbc.fmtj.view.dropdown","bbc.fmtj.view.accordion"]],builder:a});}else{a(bbc.fmtj.view);}})();(function(){bbc.fmtj.utils.createObject("bbc.fmtj.view.datetime");var a=function(c){var h={};var e="bbc.fmtj.view.datetime";var i="0.0.33";if(c===undefined){h=bbc.fmtj.utils.createObject(e);}else{h=c;}h.namespace=e;h.version=i;var d;gloader.load(["glow","1","glow.dom"],{onLoad:function(j){d=j.dom;}});var g;switch(bbc.fmtj.page.siteToServe.toLowerCase()){case"newyddion":g="cy";break;case"knowledge_learning":if(bbc.fmtj.page.uri=="/darganfod/"){g="cy";}else{g="en";}break;case"naidheachdan":g="gd";break;default:g="en";}var f={en:{NEW:"New"},cy:{NEW:"Newydd"},gd:{NEW:"&Ugrave;r"}};var b=f[g];h.newTimestampNotification=function(l){var k='<span class="new-story-icon"> '+b.NEW+"</span>";var j=d.get("#"+l+" a.story");j.each(function(o){var p=d.get(this);if(p.hasClass("is-live")){return false;}var m=p.attr("rel");if(m==null||m==""){return false;}var r=/published-(\d*)/;var n=m.match(r);if(n==null){return false;}var q=parseInt(n[1]);if((bbc.fmtj.page.serverTime-q)<3600000){if(p.get("span.new-story-icon").length==0){p.append(k);}}});};bbc.fmtj.components.registerNamespace(h);return h;};if(typeof gloader!="undefined"){gloader.provide({name:"bbc.fmtj.view.datetime",library:["bbc.fmtj.view","0.0.33"],builder:a});}else{a(bbc.fmtj.view);}})();(function(){bbc.fmtj.utils.createObject("bbc.fmtj.view.dropdown");var a=function(b){var d={};var c="bbc.fmtj.view.dropdown";var e="0.0.33";if(b===undefined){d=bbc.fmtj.utils.createObject(c);}else{d=b;}d.namespace=c;d.version=e;d.dropdown=function(f){gloader.load(["glow","1","glow.dom","glow.events"],{onLoad:function(m){var D=m.dom;var G=m.events;var p=true;var t=0;var C=1;var k=t;var A=0;var F=D.get(f);var E=F.clone(true);var l=D.create('<div class="dropdown-wrapper" />');var z=D.create('<div class="dropdown" />');z.append('<div class="tr"></div><div class="tl"></div><div class="tb"></div>');var n=D.create('<div class="tc"></div>');var h=D.create('<div class="button"></div>');n.append(E);n.append(h);z.append(n);z.append('<div class="br"></div><div class="bl"></div><div class="bb"></div>');l.append(z);F.replaceWith(l);var B=n.get("ul");var r=B.get("li");G.addListener(l,"click",y);G.addListener(B,"keyup",q);G.addListener(B,"keydown",s);var i;var o;var u;function w(){p=true;}function v(){p=false;window.setTimeout(w,1);}function q(H){if(!p){return;}if(H.type==="keyup"){if(H.key=="ENTER"){return;}}if(k===t){l.parent().addClass("dropdown-open");l.addClass("open");k=C;i=G.addListener(h,"click",g);o=G.addListener(document,"click",j);u=G.addListener(r,"click",x);v();}}function s(H){if(!p){return;}if(H.type==="keydown"){if(H.key!="TAB"&&H.key!="ENTER"){return;}if(H.key=="TAB"){if(H.shiftKey){if(D.get(H.source).parent().prev().length!=0){return;}}else{if(D.get(H.source).parent().next().length!=0){return;}}}if(H.key=="ENTER"){x({attachedTo:D.get(H.source).parent(),source:H.source,type:"keydown"});}}if(k===C){l.parent().removeClass("dropdown-open");l.removeClass("open");k=t;G.removeListener(i);G.removeListener(o);G.removeListener(u);v();}}function y(H){if(!p){return;}if(k===t){q({type:"openDropDown"});H.preventDefault();H.stopPropagation();v();return false;}else{if(!D.get(H.source).is("a")){H.preventDefault();H.stopPropagation();v();return false;}}}function g(H){s({type:"closeDropDown"});H.preventDefault();H.stopPropagation();return false;}function j(H){s({type:"closeDropDownBody"});}function x(H){if(D.get(H.source).is("a")){r.removeClass("selected");D.get(H.attachedTo).addClass("selected");}}}});};bbc.fmtj.components.registerNamespace(d);return d;};if(typeof gloader!="undefined"){gloader.provide({name:"bbc.fmtj.view.dropdown",library:["bbc.fmtj.view","0.0.33"],builder:a});}else{a(bbc.fmtj.view);}})();(function(){bbc.fmtj.utils.createObject("bbc.fmtj.view.accordion");var a=function(l){var p={};var r="bbc.fmtj.view.accordion";var o="0.0.33";if(l===undefined){p=bbc.fmtj.utils.createObject(r);}else{p=l;}p.namespace=r;p.version=o;var t={};var m,q,s,e,j;var d=0,b=1,h=2;p.createAccordion=i;function n(u){if(m!==undefined){if(typeof u==="function"){m.ready(u);}}gloader.load(["glow","1","glow.dom","glow.events","glow.anim"],{onLoad:function(v){m=v;q=v.dom;s=v.events;e=v.anim;j=v.tweens;if(typeof u==="function"){m.ready(u);}}});}function f(u){var v={};if(u===undefined){u={};}(u.overlayFadeOutDuration===undefined?v.overlayFadeOutDuration=0.05:v.overlayFadeOutDuration=u.overlayFadeOutDuration);(u.overlayFadeInDuration===undefined?v.overlayFadeInDuration=0.2:v.overlayFadeInDuration=u.overlayFadeInDuration);(u.accordionSlideDuration===undefined?v.accordionSlideDuration=0.3:v.accordionSlideDuration=u.accordionSlideDuration);(u.openHeight===undefined?v.openHeight=171:v.openHeight=u.openHeight);(u.openWidth===undefined?v.openWidth=304:v.openWidth=u.openWidth);(u.closedHeight===undefined?v.closedHeight=171:v.closedHeight=u.closedHeight);(u.closedWidth===undefined?v.closedWidth=159:v.closedWidth=u.closedWidth);(u.closedImages===undefined?v.closedImages=[]:v.closedImages=u.closedImages);return v;}function i(w,u){t[w]={status:d};var v=f(u);v.accordionId=w;n(function(){var z=q.get("#"+w);var x=z.get(".accordion-item");var y=z.get("a.accordion-link");x.each(function(B){var A=q.get(x[B]);var C=A.get(".accordion-content");C.after('<span class="accordion-hover-layer"></span>');if(v.closedImages[B]!==undefined&&v.closedImages[B]!==null&&v.closedImages[B]!==""){A.css("background-image","url('"+v.closedImages[B]+"')");if(A.hasClass("accordion-closed")){C.css("opacity",0);}A.data("fade",true);}else{A.data("fade",false);}if(A.hasClass("accordion-closed")){A.get(".accordion-link").addClass("accordion-link-disabled");}});y.each(function(A){var B=q.get(y[A]);if(B.hasAttr("href")){B.data("location",B.attr("href"));}});s.addListener(x,"focus",c,{opts:v});s.addListener(x,"click",c,{opts:v});s.addListener(y,"click",g,{opts:v});s.addListener(x,"mouseover",k,{opts:v,hover:true});s.addListener(x,"mouseout",k,{opts:v,hover:false});t[w].status=b;});}function g(u){if(q.get(u.attachedTo).hasClass("accordion-link-disabled")){u.preventDefault();return false;}}function k(w){var v=this.hover;var u=q.get(w.attachedTo);if(this.hover){u.addClass("accordion-hover");}else{u.removeClass("accordion-hover");}}function c(S){var M=this.opts;var J=q.get(S.attachedTo);if(J.hasClass("accordion-closed")){if(t[M.accordionId].status===h&&S.type=="click"){return false;}t[M.accordionId].status=h;var Q=J.get("a.accordion-link");var A=J.parent().get(".accordion-item");var I=A.get("a.accordion-link");var K=q.get(A.filter(function(T){return !q.get(this).hasClass("accordion-closed");}));var C=J.get(".accordion-overlay-content");var L=K.get(".accordion-overlay-content");var y=J.get(".accordion-content");var G=K.get(".accordion-content");var B=J.width()+K.width();var z=new e.css(J,M.accordionSlideDuration,{width:M.openWidth},{tween:j.easeIn()});var F=new e.css(C,M.overlayFadeOutDuration,{opacity:{from:1,to:0}});var H=new e.css(C,M.overlayFadeInDuration,{opacity:{from:0,to:1}});var x=new e.css(L,M.overlayFadeOutDuration,{opacity:{from:1,to:0}});var R=new e.css(L,M.overlayFadeInDuration,{opacity:{from:0,to:1}});var O=0;var E=0;if(J.data("fade")==true){O=new e.css(y,M.overlayFadeOutDuration,{opacity:{from:0,to:1}});}if(K.data("fade")==true){E=new e.css(G,M.overlayFadeInDuration,{opacity:{from:1,to:0}});}var P=false;var N=false;var w=function(){if(P){return false;}P=true;J.removeClass("accordion-closed").removeClass("accordion-right");I.addClass("accordion-link-disabled");};var v=function(){if(N){return false;}N=true;K.addClass("accordion-closed");Q.removeClass("accordion-link-disabled");var T=true;A.each(function(V){var U=q.get(A[V]);if(U.hasClass("accordion-closed")){if(T){U.removeClass("accordion-right");}else{U.addClass("accordion-right");}}else{T=false;}});};function u(){w();v();J.width(M.openWidth);K.width(B-J.width());if(J.data("fade")==true){y.css("opacity","");}if(K.data("fade")==true){G.css("opacity","0");}L.css("opacity","");C.css("opacity","");t[M.accordionId].status=b;}var D=new e.Timeline([[O,w,z,M.overlayFadeInDuration],[M.overlayFadeOutDuration,M.accordionSlideDuration,v,E],[F,M.accordionSlideDuration,H],[x,M.accordionSlideDuration,R]]);s.addListener(D,"complete",u);s.addListener(z,"frame",function(){K.width(B-J.width());});D.start();}else{var Q=J.get("a.accordion-link");if(S.type=="click"&&Q.length>0&&Q.data("location").length>0&&!Q.hasClass("accordion-link-disabled")){document.location=Q.data("location");}}}bbc.fmtj.components.registerNamespace(p);return p;};if(typeof gloader!="undefined"){gloader.provide({name:"bbc.fmtj.view.accordion",library:["bbc.fmtj.view","0.0.33"],builder:a});}else{a(bbc.fmtj.view);}})();(function(){bbc.fmtj.utils.createObject("bbc.fmtj.view.news.story");var a=function(b){var d={};var c="bbc.fmtj.view.news.story";var e="0.0.33";if(b===undefined){d=bbc.fmtj.utils.createObject(c);}else{d=b;}d.namespace=c;d.version=e;d["enlarge-image"]=function(g,f){bbc.fmtj.view.ImageLightBox.createImageLightBox(g,f);};d["range-top-stories"]=function(f){bbc.fmtj.view.datetime.newTimestampNotification(f);};d.relatedLinks=function(f){bbc.fmtj.view.dropdown(f+" .dropdown-menu");};d.commentForm=function(f){gloader.load(["glow","1","glow.dom","glow.forms"],{async:true,onLoad:function(h){var g=new h.forms.Form(f).addTests("email_name",["required",{on:"change submit",message:"Please tell us your name"}]).addTests("email_from",["required",{on:"change submit",message:"Please tell us your email address"}],["isEmail",{on:"change submit",message:"Email address is invalid"}]).addTests("email_country",["required",{on:"change submit",message:"Please let us know where you are from"}]).addTests("email_comments",["required",{on:"change submit",message:"Please leave a comment"}]);}});};bbc.fmtj.components.registerNamespace(d);return d;};if(typeof gloader!="undefined"){gloader.provide({name:"bbc.fmtj.view.news.story",library:["bbc.fmtj.view","0.0.33"],depends:[["bbc.fmtj.view","0.0.33","bbc.fmtj.view.story","bbc.fmtj.view.news"]],builder:a});}else{a(bbc.fmtj.view);}})();